package nl.tue.declare.appl.framework;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
import java.util.*;

import nl.tue.declare.appl.framework.engine.EngineAssignmentCoordinator;
import nl.tue.declare.appl.framework.engine.EngineCoordinator;
import nl.tue.declare.appl.framework.engine.EngineInitializer;
import nl.tue.declare.appl.framework.engine.IAssignmentStateListener;
import nl.tue.declare.appl.framework.engine.IAssignmnetCoordinatorFactory;
import nl.tue.declare.appl.framework.gui.*;
import nl.tue.declare.appl.framework.yawl.*;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.organization.Administrator;
import nl.tue.declare.execution.*;
import nl.tue.declare.graph.model.*;
import nl.tue.declare.utils.*;
import yawlservice.*;

public class FrameworkCoordinator
    implements IFrameworkFrameListener,
    IAssignmentStateListener, Adapter.IAdaptListener {

  private static FrameworkCoordinator instance = null; // for singleton
  private FrameworkFrame frame = null; // main frame in Framework
  private YAWLServiceCoordinator YAWLCoordinator;
  private EngineCoordinator engineCoordinator; //  Engine
  private List<InstanceHandler> managers; // list of (managers for) cases - active and available cases
  private IAssignmnetCoordinatorFactory factory;

  private FrameworkCoordinator() {
    super();
    Control.singleton(); // create Control to load data from archive
    managers = Collections.synchronizedList(new ArrayList<InstanceHandler>());
    frame = new FrameworkFrame();

    // setup YAWL connection
    EngineInitializer ini = EngineInitializer.singleton();
    int portServer = ini.getPortYAWLServer();
    int portClient = ini.getPortYAWLClient();
    String hostClient = ini.getHostYAWLClient();
    if (portServer > 0 && portClient > 0) {
      YAWLCoordinator = new YAWLServiceCoordinator(portServer, portClient,
          hostClient) {
        public void workItemArrived(ExternalWorkItem wi) {
          FrameworkCoordinator.this.arrivedYAWLWorkItem(wi);
        }

		@Override
		public void caseCompleted(WorkItem wi,ExternalCase cs) {
			 FrameworkCoordinator.this.YAWLcaseClosed(wi,cs);			
		}
      };
    }

    // prepare and start the framework main frame
    frame = new FrameworkFrame();
    frame.addFrameworkFrameListener(this);
    restoreSettings();
    this.arrivedSelected();
    this.activeSelected();
    frame.showMe();
  }

  public boolean initiate(IAssignmnetCoordinatorFactory factory) {
    boolean result = false;
    this.factory = factory;
    try {
      // create engine coodinator
      engineCoordinator = factory.createEngine();
      engineCoordinator.initiate();
      result = true;
    }
    catch (Exception e) {
      ErrorMessage.showError(frame, e,
                             "Another instance of framework is already active. Program will exit.");
    }
    return result;
  }

  /**
   *
   * @return FrameworkCoordinator
   */
  public static FrameworkCoordinator singleton() {
    if (instance == null) {
      instance = new FrameworkCoordinator();
    }
    return instance;
  }

  /**
   *
   * @param manager CaseManager
   */
  public synchronized void arrivedYAWLWorkItem(ExternalWorkItem wi) {
    InstanceHandler manager = new InstanceHandler(wi);
    managers.add(manager);
    MappingCoordinator mappingCoordinator = new MappingCoordinator(frame,manager);
      if (mappingCoordinator.automaticLoad()) {
        try {
          loaded(manager);
        }
        catch (Throwable ex) {
          ErrorMessage.showError(this.frame, ex, "Loading failed.");
        }
      } else {
        addArrived(manager);
      }
  }
  
  /**
  *
  * @param manager CaseManager
  */
 public synchronized void YAWLcaseClosed(WorkItem wi,ExternalCase cs) {
	 engineCoordinator.eventRequest(wi,new Event(Administrator.singleton(), wi.getActivity(), Event.Type.COMPLETED));
 }


  public synchronized boolean forwardWorkItemToYawl(WorkItem wi, ExternalCase ec){
    return YAWLCoordinator.launchInstance(wi,ec);
  }

  /**
   * Returns arrived (available) case manager that is currently selected by the user on the frame. Arrived cases are the
   * ones that have arrived from external system (e.g., YAWL), but they stil lhave to be started in the framework.
   * Thus, these cases are waiting to be launched as assignments in Framework.
   * @return CaseManager
   */
  private InstanceHandler getSelectedArrived() {
    return frame.getArrivedTable().getSelected();
  }

  /**
   * Returns active (launched) case manager that is currently selected by the user on the frame. Active cases are the
   * ones that have arrived from external system (e.g., YAWL), and are also started in the framework.
   * Thus, these cases are currently being executed as assignments in Framework.
   * @return CaseManager
   */
  private InstanceHandler getSelectedActive() {
    return frame.getActiveTable().getSelected();
  }

  /**
   *
   */
  public void load() {
    InstanceHandler manager = this.getSelectedArrived();
    if (manager != null) {
      MappingCoordinator mappingCoordinator = new MappingCoordinator(frame,
          manager);
      if (mappingCoordinator.load()) {
        try {
          loaded(manager);
        }
        catch (Throwable ex) {
          ErrorMessage.showError(this.frame, ex, "Loading failed.");
        }
      }
    }
  }

  /**
   *
   * @param manager CaseManager
   */
  private void loaded(InstanceHandler manager) throws Throwable {
    if (manager != null) {
      manager.readFromExternal();
      manager.setActivated(new PrettyTime());
      removeArrived(manager);
      EngineAssignmentCoordinator coordinator = factory.create(manager.getAssignment(),
          manager.getFile());
      coordinator.setAssignmentStateListener(this);
      engineCoordinator.add(coordinator);
      addActive(manager);
    }
  }

  /**
   *
   */
  public void complete() {
    InstanceHandler manager = getSelectedActive(); // get selected case
    close(manager);
  }

  public boolean complete(Assignment assignment) {
    InstanceHandler manager = getManager(assignment); // get manager for this assignment
    boolean ok = false;
    if (manager != null) {
      ok = completeManager(manager, true); // complete manager
    }
    else {
      ok = false;
    }
    return ok;
  }

  protected boolean close(InstanceHandler manager) {
    boolean ok = false;
    if (manager != null) {
      String reason = "";
      try {
        ok = canComplete(manager);
      }
      catch (Exception ex) {
        reason = ex.getMessage();
      }
      if (!ok) {
        String msg = "Assignment " + manager.getAssignment().toString() +
            " should not be closed at the moment.\n" + reason +
            "\n Are you sure you want to close it?";
        ok = MessagePane.ask(frame, msg);
      }
      if (ok) { // can close
        try {
          completeManager(manager, false);
        }
        catch (Throwable ex) {
          ok = false;
        }
      }
    }
    return ok;
  }

  /**
   *
   * @param manager CaseManager
   * @param secure boolean
   * @throws Exception
   * @return boolean
   */
  private synchronized boolean completeManager(InstanceHandler manager,
                                               boolean secure) {
    boolean ok = false;
    if (manager != null) {
      Assignment assignment = manager.getAssignment();
      ok = engineCoordinator.complete(assignment, secure);
      if (ok) {
        manager.finish();
        if (!manager.isEmpty()){ // if this was a YAWL work item, then return it to YAWL
          YAWLCoordinator.returnWorkItem(manager.getExternal());
        }
        removeActive(manager);
      }
    }
    return ok;
  }

  /**
   *
   * @param manager CaseManager
   * @return boolean
   * @throws Exception
   */
  public synchronized boolean canComplete(InstanceHandler manager) throws
      Exception {
    boolean ok = false;
    if (manager != null) {
      ok = engineCoordinator.canComplete(manager.getAssignment());
    }
    return ok;
  }

  /**
   *
   * @return int
   */
  /*public int nextManagerId() {
    return managers.size() + 1;
     }*/

  private InstanceHandler getManager(Assignment assignment) {
    InstanceHandler manager = null;
    Iterator<InstanceHandler> iterator = managers.iterator();
    boolean found = false;
    while (iterator.hasNext() && !found) {
      manager = iterator.next();
      found = manager.getAssignment().equals(assignment);
    }
    return found ? manager : null;
  }

  /**
   *
   */
  public void loadEmpty() {
    final InstanceHandler manager = new InstanceHandler(null);
    if (manager != null) {
      MappingCoordinator mappingCoordinator = new MappingCoordinator(this.frame,
          manager);
      final boolean loaded = mappingCoordinator.load();

      if (loaded) {

        final int number = FrameworkCoordinator.this.frame.getNumberEmpty();

        SwingWorker sw = new SwingWorker() {

          public Object construct() {

            ProgressFrame frame = new ProgressFrame("Loading " + number +
                " cases.", 0, number - 1);
            boolean ok = true;
            addPropertyChangeListener(frame);
            setProgress(0);
            frame.showMe();
            frame.setInfo("Adding instance nr. 1.");
            add(manager);
            setProgress(1);
            for (int i = 0; i < number - 1 && !frame.canceled() && ok; i++) {
              InstanceHandler clone = manager.clone();
              frame.setInfo("Adding instance " + Integer.toString(i + 2) + ".");
              ok = add(clone);
              setProgress(i + 2);
            }
            frame.close();
            return null;
          }

          private boolean add(InstanceHandler manager) {
            managers.add(manager);
            try {
              FrameworkCoordinator.this.loaded(manager);
            }
            catch (Throwable ex) {
              ErrorMessage.showError(FrameworkCoordinator.this.frame, ex,
                                     "Loading failed.");
              return false;
            }
            return true;
          }

          public void finished() {
          }
        };
        sw.start();
      }

    }
  }

  public void finish() {
    engineCoordinator.close();
  }

  public void stateChanged(AssignmentState state) {
    if (state != null) {
      frame.getActiveTable().setState(state);
    }
  }

  private void addArrived(InstanceHandler manager) {
    synchronized (frame.getArrivedModel()) {
      frame.getArrivedModel().addRow(new PrettyTime(), manager);
      this.arrivedSelected();
    }
  }

  private void removeArrived(InstanceHandler manager) {
    synchronized (this.frame.getArrivedModel()) {
      frame.getArrivedModel().remove(manager);
      this.arrivedSelected();
    }
  }

  private void addActive(InstanceHandler manager) {
    synchronized (this.frame.getActiveModel()) {
      frame.getActiveModel().addRow(manager);
      this.activeSelected();
    }
  }

  private void removeActive(InstanceHandler manager) {
    synchronized (this.frame.getActiveModel()) {
      frame.getActiveModel().remove(manager);
      this.activeSelected();
    }
  }

  public void saveSettings() {
    EngineInitializer ini = EngineInitializer.singleton();
    ini.setPortYAWLServer(frame.getYawlPost());

    ini.setRequestPort(frame.getReqestPort());
    ini.setInformationPort(frame.getInformationPort());

    ini.setRecommendationPort(frame.getRecommendationPort());
    ini.setRecommendationHost(frame.getRecommendationHost());

    ini.save();
  }

  public void restoreSettings() {
    EngineInitializer ini = EngineInitializer.singleton();
    ini.restore();
    frame.setYawlPort(Integer.toString(ini.getPortYAWLServer()));

    frame.setReqestPort(Integer.toString(ini.getPortRequest()));
    frame.setInformationPort(Integer.toString(ini.getPortInformation()));

    frame.setRecommendationPort(Integer.toString(ini.getPortRecommendation()));
    frame.setRecommendationHost(ini.getHostRecommendation());
  }

  public void changeModel() {
    InstanceHandler manager = getSelectedActive(); // get selected case
    if (manager != null) {
      Assignment assignment = manager.getAssignment();
      EngineAssignmentCoordinator coordinator = engineCoordinator.
          getAssignmentCoordinator(assignment);
      Adapter adapt = new Adapter(assignment, getInstances(coordinator),
                                  coordinator.getAssignmentString(),
                                  coordinator.getFile());
      adapt.setListener(this);
      adapt.start();
    }
  }

  private ArrayList<Assignment> getInstances(EngineAssignmentCoordinator coordinator) {
	ArrayList<Assignment> instances = new ArrayList<Assignment>();
    for (EngineAssignmentCoordinator c: engineCoordinator.getInstances(coordinator)) {
      instances.add(c.getAssignment());
    }
    return instances;
  }

  public void arrivedSelected() {
    frame.arrived(getSelectedArrived() != null);
  }

  public void activeSelected() {
    frame.active(getSelectedActive() != null);
  }

  public synchronized void change(Assignment original, Assignment change,
                                  AssignmentModelView view) throws Throwable {
    engineCoordinator.change(original, change, view);
    getManager(change).setAssignment(change);
  }

  public void changeTeam() {
  }
}
