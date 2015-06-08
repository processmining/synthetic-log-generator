package nl.tue.declare.appl.design.organization.gui;

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
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.organization.Role;

public class PanelRoles
    extends DefaultPanel implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8988190331033263733L;

JList rolesList;

  JButton jButtonRoleAdd;
  JButton jButtonRoleDelete;
  JButton jButtonRoleEdit;

  public PanelRoles() {
    super(new GridLayout(1, 1));
  }

  protected void buildInterface() {
    rolesList = new JList();

    jButtonRoleAdd = new JButton();
    jButtonRoleDelete = new JButton();
    jButtonRoleEdit = new JButton();

    JPanel grid = new JPanel(new GridLayout(1, 1));
    JPanel controls = new JPanel(new GridLayout(2, 1));
    createPanelRolesGrid(grid);
    createPanelRolesControls(controls);
    add(grid);
    add(controls);
    setBtnRolesListener();
  }

  private void createPanelRolesGrid(JPanel panel) {
    rolesList.setSelectionModel(new DefaultListSelectionModel());
    rolesList.getSelectionModel().setSelectionMode(ListSelectionModel.
        SINGLE_SELECTION);
    panel.add(new JScrollPane(rolesList));
  }

  private void createPanelRolesControls(JPanel panel) {
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    panel.add(jButtonRoleAdd);
    panel.add(jButtonRoleEdit);
    panel.add(jButtonRoleDelete);
    jButtonRoleAdd.setText("add");
    jButtonRoleEdit.setText("edit");
    jButtonRoleDelete.setText("delete");
  }

  /**
   * setRolesAddEL
   */
  private void setBtnRolesListener() {
    this.jButtonRoleAdd.addActionListener(this);
    this.jButtonRoleEdit.addActionListener(this);
    this.jButtonRoleDelete.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    /* if ( listener == null ) return;
     Object source = e.getSource();
     if (source == jButtonRoleAdd)    { listener.addRole();   return; };
     if (source == jButtonRoleEdit)   { listener.editRole();   return; };
     if (source == jButtonRoleDelete) { listener.deleteRole(); return; };*/
  }

  /**
   *
   * @return Object
   */
  public Object getSelectedRole() {
    Object el = null;
    el = rolesList.getSelectedValue();
    return el;
  }

  /**
   * fillRoles
   *
   * @param anList List
   */
  public void fillRoles(List<Role> roles) {
    FrameUtil.fillList(new ArrayList<Object> (roles), rolesList);
    boolean empty = (roles.size() == 0);
    jButtonRoleDelete.setEnabled(!empty);
    jButtonRoleEdit.setEnabled(!empty);
  }
}
