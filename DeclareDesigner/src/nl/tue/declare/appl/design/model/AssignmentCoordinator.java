package nl.tue.declare.appl.design.model;

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

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;

public class AssignmentCoordinator
    implements InternalFrameListener {

  AssignmentCoordinatorListener listener;

  private JFrame mainFrame;
  JInternalFrame frame;

  List<ModelCoordinator> coordinators;
  ModelCoordinator active;

  // dialog for add/edit Role
  private FrmAssignmentProperties frmProperties = null;

  public AssignmentCoordinator(JFrame aMainFrame) {
    super();
    mainFrame = aMainFrame;
    coordinators = new ArrayList<ModelCoordinator>();
    this.listener = null;
    active = null;
    frmProperties = new FrmAssignmentProperties(aMainFrame,
                                                "Assignment properties", 
                                                frame);
  }

  public JInternalFrame getFrame() {
    return this.frame;
  }

  public void init() {
    internalFrameDeactivated(null);
  }

  public void addListener(AssignmentCoordinatorListener list) {
    this.listener = list;
  }

  /**
   * newModel
   *
   * @return JInternalFrame
   */
  public JInternalFrame newModel(Language lang) {
    ModelCoordinator coordinator = new ModelCoordinator(mainFrame,
        getControl().getAssignmentModel().newModel(lang));
    coordinator.propertiesChanged();
    // removed 04-07-2006 coordinator.newModel();
    return this.startCoordinator(coordinator);
  }

  private JInternalFrame startCoordinator(ModelCoordinator coordinator) {
    coordinators.add(coordinator);
    frame = coordinator.getInternalFrame();
    frame.addInternalFrameListener(this);
    return frame;
  }

  /**
   * getModelCoordinator
   *
   * @param frame JInternalFrame
   * @return ModelCoordinator
   */
  public ModelCoordinator getModelCoordinator(JInternalFrame frame) {
    ModelCoordinator coordinator = null;
    int i = 0;
    boolean found = false;
    while (!found && i < coordinators.size()) {
      coordinator = coordinators.get(i++);
      found = coordinator.isActive(frame);
    }
    return found ? coordinator : null;
  }

  /**
   * activateModel
   *
   * @param frame JInternalFrame
   */
  public void activateModel(JInternalFrame frame) {
    ModelCoordinator coordinator = this.getModelCoordinator(frame);
    coordinator.start();
  }

  public void internalFrameDeactivated(InternalFrameEvent e) {
    if (this.listener != null) {
      if (e == null) {
        this.listener.deactivated(null);
      }
      else {
        this.listener.deactivated(e.getInternalFrame());
      }
    }
  }

  public void internalFrameActivated(InternalFrameEvent e) {
    if (e != null) {
      if (this.listener != null) {
        this.listener.activated(e.getInternalFrame());
      }
      // get the activated model coordinator
      active = getModelCoordinator(e.getInternalFrame());
    }
  }

  public void internalFrameDeiconified(InternalFrameEvent e) {}

  public void internalFrameIconified(InternalFrameEvent e) {}

  public void internalFrameClosed(InternalFrameEvent e) {
    Object source = e.getSource();
    if (source instanceof JInternalFrame) {
      JInternalFrame frame = (JInternalFrame) source;
      ModelCoordinator coordinator = this.getModelCoordinator(frame);
      if (coordinator != null) {
        // removed 04-07-2006  coordinator.onClosed();
        this.coordinators.remove(coordinator);
        if (this.listener != null) {
          this.listener.closed(frame);
        }
      }
    }
  }

  public void internalFrameClosing(InternalFrameEvent e) {}

  public void internalFrameOpened(InternalFrameEvent e) {}

  /**
   * save
   */
  public void save() {
    //XMLFileDialog dialog = new XMLFileDialog();
    //String file = dialog.saveFile(this.frame);
    String file = active.getFilePath();
    if (file != null) {
      this.saveAssignmentModel(file);
      /* if (active != null)
       this.getControl().getAssignmentModel().addAssignmentModelAndView(active.model,
             active.workCoordinator.modelView, file);*/
    }
    else {
      this.saveAs();
    }
  }

  /**
   * saveAs
   */
  public void saveAs() {
    // create a dilaog for XML files
    XMLFileDialog dialog = new XMLFileDialog();
    // open a dilaog for saving XML files with this frame as a parent frame
    // store the path of the seelcted file
    String file = dialog.saveFile(this.frame);
    // if the path is selected
    if (file != null) {
      this.saveAssignmentModel(file);
      /*if (active != null) {
       this.getControl().getAssignmentModel().addAssignmentModelAndView(active.
            model,
            active.workCoordinator.modelView, file);
             }*/
    }
  }

  private void saveAssignmentModel(String file) {
    if (active != null) {
      active.setFilePath(file);
      this.getControl().getAssignmentModel().addAssignmentModelAndView(active.
          model,
          active.getView(), active.getFilePath());
    }
  }

  /**
   * open
   *
   * @return JInternalFrame
   */
  public JInternalFrame open() {
    XMLFileDialog dialog = new XMLFileDialog();
    String file = dialog.openFile(this.mainFrame);
    if (file != null) {
      AssignmentModel model = getControl().getAssignmentModel().
          getAssignmentModel(file);
      if (model != null) {
        ModelCoordinator coordinator = new ModelCoordinator(mainFrame, model);
        coordinator.setFilePath(file);
        getControl().getAssignmentModel().getAssignmentModelGraphical(model,
            coordinator.getView(), file);
        return this.startCoordinator(coordinator);
      }
    }
    return null;
  }

  private Control getControl() {
    return Control.singleton();
  }

  /**
   * properties
   */
  public void properties() {
    if (active != null) {
      this.fillFormFromAssignmentModel(active.model, this.frmProperties);
      frmProperties.setTitle("Assignment properties");
      frmProperties.setMonitor(active.frame);
      if (frmProperties.showCentered()) {
        propertiesConfirmed();
      }
    }
  }

  /**
   * propertiesConfirmed
   */
  public void propertiesConfirmed() {
    if (active != null) {
      this.fillAssignmentModelFromForm(active.model, frmProperties);
      active.propertiesChanged();
    }
  }

  /**
   * fillAssignmentModelFromForm
   *
   * @param aModel Role
   * @param anForm FrmRole
   */
  public void fillAssignmentModelFromForm(AssignmentModel aModel,
                                          FrmAssignmentProperties anForm) {
    if ( (anForm != null) && (aModel != null)) {
      aModel.setName(anForm.getModelPanel().getModelName());

    }
  }

  /**
   * fillFormFromAssignmentModel
   *
   * @param aModel Role
   * @param anForm FrmRole
   */
  private void fillFormFromAssignmentModel(AssignmentModel aModel,
                                           FrmAssignmentProperties anForm) {
    String name = "";
    if (anForm != null) {
      if (aModel != null) {
        name = aModel.getName();
      }
      anForm.getModelPanel().setModelName(name);
    }
  }

  /**
   *
   */
  public void exportAsImage() {
    DefaultFileDialog dialog = new JGraphExportImageDialog();
    String path = dialog.saveFile(this.mainFrame);
    if (path != null) {
      File file = new File(path);
      this.active.exportAsImage(file, dialog.getExtension(file));
    }
  }

  /**
   *
   */
  public void exportAsProM() {
    active.exportAsProM();
  }

  /**
   * verify
   */
  public void verify() throws Throwable {
    this.active.verify();
  }
}
