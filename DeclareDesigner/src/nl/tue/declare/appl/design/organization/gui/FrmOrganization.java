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

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.design.organization.OrganizationCoordinator;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.organization.Role;
import nl.tue.declare.domain.organization.User;

public class FrmOrganization
    extends DesignInternalFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = 108591224061988319L;

static final String TITLE = "Organization";

  private static FrmOrganization single = null;
  private OrganizationCoordinator organizationCoordinator;

  GridBagLayout gridLayoutRoles = new GridBagLayout();
  JTabbedPane tp = new JTabbedPane();
  JButton jButtonRoleAdd = new JButton();
  JButton jButtonRoleDelete = new JButton();
  JButton jButtonRoleEdit = new JButton();
  JPanel panelRoles = new JPanel(new GridLayout(3, 1));
  JPanel panelUsers = new JPanel(gridLayoutRoles);
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();

  JList rolesList = new TList();

  JList usersList = new TList();
  JButton jButtonUserAdd = new JButton();
  JButton jButtonUserDelete = new JButton();
  JButton jButtonUserEdit = new JButton();

  JList usersRolesList = new TList();
  JButton jButtonUserRoleAdd = new JButton();
  JButton jButtonUserRoleDelete = new JButton();

  TPanel panelAssignedRoles;

  private FrmOrganization(OrganizationCoordinator anOrganizationCoordinator) {
    super(TITLE);
    this.organizationCoordinator = anOrganizationCoordinator;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static FrmOrganization singleton(OrganizationCoordinator
                                          anOrganizationCoordinator) {
    if (single == null) {
      single = new FrmOrganization(anOrganizationCoordinator);
    }
    return single;
  }

  protected void jbInit() throws Exception {
    //JOptionPane.showInternalMessageDialog(this, "information", "poelse", JOptionPane.INFORMATION_MESSAGE);
    this.setContentPane(tp);
    panelRoles.setLayout(gridLayout1);
    //panelUsers.setLayout();
    tp.add("Roles", panelRoles);
    tp.add("Users", panelUsers);
    createPanelRoles(panelRoles);
    createPanelUsers(panelUsers);
    setBtnRolesListener(this);

    tp.addMouseListener(this);
    usersList.addMouseListener(this);
  }

  private void createPanelRoles(JPanel panel) {
    panel.setLayout(new GridLayout(1, 1));
    JPanel grid = new JPanel(new GridLayout(1, 1));
    JPanel controls = new JPanel(new GridLayout(2, 1));
    createPanelRolesGrid(grid);
    createPanelRolesControls(controls);
    panel.add(grid);
    panel.add(controls);
  }

  private void createPanelRolesGrid(JPanel panel) {

    rolesList.setSelectionModel(new DefaultListSelectionModel());
    rolesList.getSelectionModel().setSelectionMode(ListSelectionModel.
        SINGLE_SELECTION);
    new JScrollPane(rolesList);
    panel.add(rolesList);
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

  private void createPanelUsers(JPanel panel) {
    // *** prepare panel with users
    JPanel panelUsers = new TPanel(new BorderLayout(2, 2), "users");
    panelUsers.add(new JScrollPane(usersList), BorderLayout.CENTER);

    JPanel panelUsersButtons = new JPanel(new FlowLayout());
    jButtonUserAdd.setText("add");
    jButtonUserEdit.setText("edit");
    jButtonUserDelete.setText("delete");
    setBtnUsersListener(this);
    panelUsersButtons.add(jButtonUserAdd);
    panelUsersButtons.add(jButtonUserEdit);
    panelUsersButtons.add(jButtonUserDelete);
    panelUsers.add(panelUsersButtons, BorderLayout.SOUTH);

    // *** prepare panel for assigned roles
    panelAssignedRoles = new TPanel(new BorderLayout(2, 2), "assigned roles");
    panelAssignedRoles.add(new JScrollPane(usersRolesList), BorderLayout.CENTER);

    jButtonUserRoleAdd.setText("add role");
    jButtonUserRoleDelete.setText("delete role");
    setBtnUsersRolesListener(this);
    //panelUsersButtons.add(jButtonUserRoleAdd);
    // panelUsersButtons.add(jButtonUserRoleDelete);
    JPanel panelAssignedRolesButtons = new JPanel(new FlowLayout());
    panelAssignedRolesButtons.add(jButtonUserRoleAdd);
    panelAssignedRolesButtons.add(jButtonUserRoleDelete);
    panelAssignedRoles.add(panelAssignedRolesButtons, BorderLayout.SOUTH);

    // *** prepare panel
    /*panel.setLayout(new GridLayout(1,2,2,2));
         panel.add(panelUsers);
         panel.add(panelAssignedRoles);*/
    panel.setLayout(new BorderLayout());
    panel.add(new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelUsers,
                             panelAssignedRoles), BorderLayout.CENTER);
  }

  public Object getSelecetdList() {
    Object el = null;
    el = rolesList.getSelectedValue();
    return el;
  }

  /**
   * setRolesAddEL
   *
   * @param anObject ActionListener
   */
  public void setBtnRolesListener(ActionListener anObject) {
    this.jButtonRoleAdd.addActionListener(anObject);
    this.jButtonRoleEdit.addActionListener(anObject);
    this.jButtonRoleDelete.addActionListener(anObject);
  }

  /**
   * setUsersAddEL
   *
   * @param anObject ActionListener
   */
  public void setBtnUsersListener(ActionListener anObject) {
    this.jButtonUserAdd.addActionListener(anObject);
    this.jButtonUserEdit.addActionListener(anObject);
    this.jButtonUserDelete.addActionListener(anObject);
  }

  /**
   * setUsersRolesAddEL
   *
   * @param anObject ActionListener
   */
  public void setBtnUsersRolesListener(ActionListener anObject) {
    this.jButtonUserRoleAdd.addActionListener(anObject);
    this.jButtonUserRoleDelete.addActionListener(anObject);
  }

  /**
   * actionPerformed
   *
   * @param e ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == jButtonRoleAdd) {
      organizationCoordinator.addRole();
      return;
    }
    ;
    if (source == jButtonRoleEdit) {
      organizationCoordinator.editRole();
      return;
    }
    ;
    if (source == jButtonRoleDelete) {
      organizationCoordinator.deleteRole();
      return;
    }
    ;
    if (source == jButtonUserAdd) {
      organizationCoordinator.addUser();
      return;
    }
    ;
    if (source == jButtonUserEdit) {
      organizationCoordinator.editUser();
      return;
    }
    ;
    if (source == jButtonUserDelete) {
      organizationCoordinator.deleteUser();
      return;
    }
    ;
    if (source == jButtonUserRoleAdd) {
      organizationCoordinator.assignRole();
      return;
    }
    ;
    if (source == jButtonUserRoleDelete) {
      organizationCoordinator.disassignRole();
      return;
    }
    ;
  }

  public void mouseClicked(MouseEvent e) {
    Object source = e.getSource();
    if ( (source == this.tp) && (this.tp.getSelectedComponent() == panelUsers)) {
      organizationCoordinator.startUsers();
      return;
    }
    if (source == this.usersList) {
      organizationCoordinator.selectedUser();
      return;
    }
  }

  /**
   * fillRoles
   *
   * @param anList List
   */
  public void fillRoles(Collection<Role> roles) {
	Collection<Object> temp = new ArrayList<Object>();
	temp.addAll(roles);
    this.fillList(temp, rolesList);
  }

  /**
   * getSelectedRole
   *
   * @return Object
   */
  public Object getSelectedRole() {
    return getSelecetdList(rolesList);
  }

  /**
   * getSelectedUser
   *
   * @return Object
   */
  public Object getSelectedUser() {
    return getSelecetdList(usersList);
  }

  /**
   * fillUsers
   *
   * @param anList List
   */
  public void fillUsers(Collection<User> users) {
    this.fillList(new ArrayList<Object>(users), usersList);
  }

  /**
   * fillAssignedRoles
   *
   * @param anList List
   */
  public void fillAssignedRoles(Collection<Role> anList) {
    this.fillList(new ArrayList<Object> (anList), usersRolesList);
  }

  /**
   * getSelectedAssignedRole
   *
   * @return Object
   */
  public Object getSelectedAssignedRole() {
    return getSelecetdList(usersRolesList);
  }

  public void setAssignedRoles(String title) {
    panelAssignedRoles.setTitle(title);
  }
}
