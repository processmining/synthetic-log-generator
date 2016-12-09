package nl.tue.declare.appl.framework;

import java.io.*;
import java.util.*;

import nl.tue.declare.appl.design.model.AbstractModelCoordinator;
import nl.tue.declare.appl.framework.gui.FrmAdapt;
import nl.tue.declare.appl.framework.gui.FrmAdaptResult;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.graph.model.*;
import nl.tue.declare.verification.*;

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
public class Adapter
    extends SwingWorker implements FrmAdapt.IFrmAdaptListener {

  private Assignment original;
  private Assignment clone;

  private AbstractModelCoordinator coordinator;

  private FrmAdapt frame;
  private FrmAdaptResult frmResult;

  private IAdaptListener listener = null;
  private ArrayList<Assignment>
      instances = new ArrayList<Assignment>();
  private File file;

  public Adapter(Assignment assignment, ArrayList<Assignment> instances,
                 String string,  File file) {
    super();
    this.original = assignment;
    this.file = file;
    this.clone = (Assignment) assignment.clone();
    frame = new FrmAdapt("Change model - " + clone.getName());
    this.coordinator = new AbstractModelCoordinator(frame, clone, true, true, false);
    Control.singleton().getAssignmentModel().
        getAssignmentModelGraphicalFromString(clone, coordinator.getView(),
                                              string);
    frame.setAssignmanePanel(coordinator.getPanel());
    frame.setListener(this);

    // add all instances to consider
    this.instances.addAll(instances);
    this.frmResult = new FrmAdaptResult(frame);
  }

  public void setListener(IAdaptListener listener) {
    this.listener = listener;
  }


  public synchronized void cancel() {

  }

  public Object construct() {
    frame.showMe();
    return null;
  }

   private void change(Assignment assignment) throws Throwable {
     if (listener != null) {
       Assignment clone = this.clone.clone(assignment.getId()); // clone new for the current case
       listener.change(assignment, clone, coordinator.getView());
     }
   }

  public void verifyModel() {
    try {
      coordinator.verify();
    }
    catch (Throwable ex) {
      ErrorMessage.showError(this.frame, ex, "Verification failed.");
    }
  }

  public boolean onOk(boolean migrate, boolean future) {
    boolean ok = false;
    boolean errors = false;
    try {
      frmResult.clear();
      VerificationResult result = this.verify(/*original,*/ clone);
      if (!result.isEmpty()) {
        errors = true;
      }

      if (result.isEmpty()) {
        ok = true;
        change(original);
      }
      if (migrate) { // migrate
          for (Assignment curr: instances) {  
           if (curr != original) { // because coordinator is already changed
        	Assignment temp = this.clone.clone(curr.getId());  
        	temp.clearEventLog(); // clear e
        	for(Event e: curr.getEventLog()){
        		temp.addEvent(e);
        	}
            result = this.verify(temp);
            if (!result.isEmpty()) {
              errors = true;
            }
            if (result.isEmpty()) {
              ok = ok || true;
              change(curr); // we need at least one change to succeed
            }
          }
        }
      }
      if (future) {
        Control.singleton().getAssignmentModel().addAssignmentModelAndView(
            clone, coordinator.getView(), file.getPath());
      }
      if (errors) {
        frmResult.pack();
        frmResult.showCentered();
      }
    }
    catch (Throwable ex) {
      ErrorMessage.showError(this.frame, ex, "History verification failed.");
    }
    return ok;
  }

  private VerificationResult verify( Assignment change) throws
      Throwable {
    IVerification verification = new HistoryVerification(change);
    VerificationResult result = verification.verify();
    frmResult.add(result, original);
    return result;
  }

  public interface IAdaptListener {
    void change(Assignment original, Assignment change,
                AssignmentModelView view) throws Throwable;
  }
}
