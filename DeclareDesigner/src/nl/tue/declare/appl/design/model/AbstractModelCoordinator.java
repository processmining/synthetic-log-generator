package nl.tue.declare.appl.design.model;

import java.io.*;
import javax.imageio.*;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.appl.verification.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.graph.*;
import nl.tue.declare.graph.model.*;
import nl.tue.declare.utils.prom.*;
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
public class AbstractModelCoordinator {

  protected JFrame mainFrame = null;

   @SuppressWarnings("unused")
  private TeamCoordinator teamCoordinator = null;
   @SuppressWarnings("unused")
  private DataCoordinator dataCoordinator = null;
  private WorkCoordinator workCoordinator = null;

  protected AssignmentPanel panel;

  protected AssignmentModel model;
  protected VerificationGUI verificationGUI;

  private File file; // store the path of the file where the model is saved

  public AbstractModelCoordinator(JFrame aMainFrame, AssignmentModel aModel,
                                  boolean work, boolean team, boolean data) {
    this(aModel);
    setUp(aMainFrame, work, team, data);
  }

  public AbstractModelCoordinator(AssignmentModel aModel) {
    super();
    panel = new AssignmentPanel();
    model = aModel;
    file = null;
  }

  public AssignmentPanel getPanel() {
    return panel;
  }

  protected void setUp(JFrame aMainFrame, boolean work, boolean team,
                       boolean data) {
    mainFrame = aMainFrame;
    panel.set(work, team, data);
    if (work) {
      this.workCoordinator = new WorkCoordinator(aMainFrame, panel, model);
    }
    if (team) {
      this.teamCoordinator = new TeamCoordinator(aMainFrame, panel, model);
    }
    if (data) {
      this.dataCoordinator = new DataCoordinator(aMainFrame, panel, model);
    }
    this.verificationGUI = new VerificationGUI(aMainFrame, panel);
  }

  /**
   * setFilePath
   *
   * @param path String
   */
  public void setFilePath(String path) {
    file = new File(path);
  }

  /**
   *
   * @return String
   */
  public String getFilePath() {
    if (file == null) {
      return null;
    }
    return file.getAbsolutePath();
  }

  public void exportAsImage(File file, String ext) {
    DGraph graph = this.workCoordinator.getGraph();
    try {
      OutputStream out = new FileOutputStream(file);
      Color bg = graph.getBackground();
      BufferedImage img = graph.getImage(bg, 0);
      ImageIO.write(img, ext, out);
      out.flush();
      out.close();
    }
    catch (Exception ex) {
    }
  }

  public void exportAsProM() {
    ProMExport.model(model, panel);
  }

  /**
   * verify
   *
   * @return boolean
   */
  public boolean verify() throws Throwable {
    return this.verify(new ModelVerification(model), true);
  }

  protected boolean verify(IVerification verification, boolean informOk) throws
      Throwable {
    return verificationGUI.verify(verification, informOk);
  }

  public AssignmentModelView getView() {
    return this.workCoordinator.getView();
  }

}
