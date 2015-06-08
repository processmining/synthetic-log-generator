package nl.tue.declare.appl.design.model.gui;

import java.util.List;

import java.awt.*;
import java.awt.event.*;

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
import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.organization.Role;

public class FrmAssignmentModel
    extends DesignInternalFrame {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1207580453413544345L;
private AssignmentPanel panel = null;

  protected FrmAssignmentModel(String title, AssignmentPanel panel) {
    super(title);
    this.panel = panel;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e ActionEvent
   * @todo Implement this java.awt.event.ActionListener method
   */
  public void actionPerformed(ActionEvent e) {
  }

  /**
   * newFrame
   *
   * @param aCoordinator ModelCoordinator
   */
  public FrmAssignmentModel(AssignmentPanel panel) {
    this("", panel);
  }

  /**
   * SetModelName
   *
   * @param aName String
   */
  public void SetModelName(String aName) {
    this.setTitle(aName);
  }

  /**
   *
   * @throws Exception
   */
  protected void jbInit() throws Exception {
    this.setContentPane(panel);
  }

  /**
   * preview
   *
   * @param com Component
   */
  public void preview(Component com) {
    panel.preview(com);
  }

  public void fillRoles(List<Role> list) {
    panel.fillRoles(list);
  }

  public Object getSelectedRole() {
    return panel.getSelectedRole();
  }

  public void setDataPanel(Component component) {
    panel.setDataPanel(component);
  }

  public void setTeamPanel(Component component) {
    panel.setTeamPanel(component);
  }

  public AssignmentPanel getPanel() {
    return this.panel;
  }
}
