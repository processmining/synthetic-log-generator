package nl.tue.declare.appl.design.model;

import javax.swing.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.utils.prom.*;

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
public class ModelCoordinator
  extends AbstractModelCoordinator {
	
  protected FrmAssignmentModel frame;
    
   
  protected ModelCoordinator(JFrame aMainFrame, AssignmentModel aModel) {
    super(aMainFrame, aModel, true, true, true);
    frame = new FrmAssignmentModel(panel);
    frame.SetModelName(aModel.getName());
 }
  

  /**
   * end
   *
   * @todo Implement this
   *   nl.tue.declare.appl.design.coordinate.InternalCoordinator method
   */
  public void end() {
  }

  /**
   * getInternalFrame
   *
   * @return JInternalFrame
   */
  public JInternalFrame getInternalFrame() {
    return frame;
  }

  /**
   * start
   *
   * @todo Implement this
   *   nl.tue.declare.appl.design.coordinate.InternalCoordinator method
   */
  public void start() {
    this.frame.maximize();
  }

  /**
   * active
   *
   * @param aFrame FrmAssignmentModel
   * @return boolean
   */
  public boolean isActive(JInternalFrame aFrame) {
    return aFrame == frame;
  }

  /**
   * propertiesChanged
   */
  public void propertiesChanged() {
    writeModelNameOnFrame();
  }

  private void writeModelNameOnFrame() {
    String path = this.getFilePath();
    if (path == null) {
      path = "UNSAVED";
    }
    frame.SetModelName(path + " - " + model.getName());
  }

  /**
   * setFilePath
   *
   * @param path String
   */
  public void setFilePath(String path) {
    super.setFilePath(path);
    writeModelNameOnFrame();
  }

  public void exportAsProM() {
    ProMExport.model(model, frame);
  }
}
