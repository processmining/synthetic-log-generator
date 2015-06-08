package nl.tue.declare.appl.design.organization;

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
import java.util.Collection;
import java.util.List;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.design.InternalCoordinator;
import nl.tue.declare.appl.design.organization.gui.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.organization.*;

public class OrganizationCoordinator
    extends InternalCoordinator {

  // frame for the organization
  private FrmOrganization frame = null;

  // dialog for add/edit Role
  private FrmRole frmRole = null;

  // dialog for add/edit User
  private FrmUser frmUser = null;

  // dialog for assign/dissasign Role to/from User
  private FrmRoles frmRoles = null;

  private static OrganizationCoordinator instance = null;

  private OrganizationCoordinator(JFrame aMainFrame) {
    super(aMainFrame);
    frame = FrmOrganization.singleton(this);
    frmRole = new FrmRole(aMainFrame, "Role",  frame);
    frmUser = new FrmUser(aMainFrame, "User",frame);
    frmRoles = new FrmRoles(aMainFrame, "Roles", frame);
    start();
  }

  public static OrganizationCoordinator singleton(JFrame aMainFrame) {
    if (instance == null) {
      instance = new OrganizationCoordinator(aMainFrame);
    }
    return (OrganizationCoordinator) instance;
  }

  public static boolean exists() {
    return instance != null;
  }

  public static void finish() {
    instance = null;
  }

  /**
   * start
   */
  public void start() {
    this.fillRoles();
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
   * end
   */
  public void end() {
    try {
      this.frame.setClosed(true);
    }
    catch (Exception pve) {}
    ;
  }

  /**
   * addRole
   */
  public void addRole() {
    fillFormFromRole(null, frmRole);
    frmRole.setTitle("Add role");
    if (frmRole.showCentered()) {
      addRoleConfirmed();
    }
  }

  /**
   * addRoleConfirmed
   */
  public void addRoleConfirmed() {
    Role role = this.getControl().getOrganization().createRole();
    this.fillRoleFromForm(role, frmRole);
    if (this.getControl().getOrganization().containsRole(role)) {
      JOptionPane.showInternalMessageDialog(frame,
                                            "You cannot add a role that alreday exists.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }

    fillRoles();
  }

  /**
   * fillRoleFromForm
   *
   * @param anRole Role
   * @param anForm FrmRole
   */
  public void fillRoleFromForm(Role anRole, FrmRole anForm) {
    if ( (anForm != null) && (anRole != null)) {
    	anForm.toRole(anRole);
    }
  }

  /**
   * fillFormFromRole
   *
   * @param anRole Role
   * @param anForm FrmRole
   */
  private void fillFormFromRole(Role anRole, FrmRole anForm) {
    if (anForm != null && anRole != null) {
      anForm.fromRole(anRole);
    }
  }

  /**
   * fillRoles
   */
  private void fillRoles() {
    frame.fillRoles(this.getControl().getOrganization().getRoles());
  }

  /**
   * getSelectedRole
   *
   * @return Role
   */
  public Role getSelectedRole() {
    Object selected = frame.getSelectedRole();
    Role role = null;
    if (selected != null) {
      if (selected instanceof Role) {
        role = (Role) selected;
      }
    }
    return role;
  }

  /**
   * editRole
   */
  public void editRole() {
    Role role = getSelectedRole();
    if (role != null) {
      fillFormFromRole(role, frmRole);
      frmRole.setTitle("Edit role");
      if (frmRole.showCentered()) {
        editRoleConfirmed(role);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No role is selected. You must first select a role you want to edit.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * deleteRole
   */
  public void deleteRole() {
    Role role = getSelectedRole();
    if (role != null) {
      Object[] options = {
          "OK", "CANCEL"};
      int response = JOptionPane.showOptionDialog(frame,
                                                  "Do you want to delete the rol of " +
                                                  role + "?",
                                                  "confirmation",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE, null,
                                                  options, options[1]);
      if (response == JOptionPane.OK_OPTION) {
        deleteRoleConfirmed(role);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No role is selected. You must first select the role you want to delete.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * editRoleConfirmed
   *
   * @param role Role
   */
  public void editRoleConfirmed(Role role) {
    this.fillRoleFromForm(role, frmRole);
    this.getControl().getOrganization().editRole(role);
    fillRoles();
  }

  /**
   * deleteRoleConfirmed
   *
   * @param role Role
   */
  public void deleteRoleConfirmed(Role role) {
    boolean canRemove = this.getControl().getOrganization().deleteRole(role);
    if (canRemove) {
      fillRoles();
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "The role " + role +
          " cannot be deleted because it is assigned to at least one user.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * addUser
   */
  public void addUser() {
    this.fillFormFromUser(null, frmUser);
    frmUser.setTitle("Add user");
    if (frmUser.showCentered()) {
      addUserConfirmed();
    }
  }

  /**
   * editUser
   */
  public void editUser() {
    User user = getSelectedUser();
    if (user != null) {
      fillFormFromUser(user, frmUser);
      frmUser.setTitle("Edit user");
      if (frmUser.showCentered()) {
        editUserConfirmed(user);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No user is selected. You must first select an user you want to edit.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * deleteUser
   */
  public void deleteUser() {
    User user = getSelectedUser();
    if (user != null) {
      Object[] options = {
          "OK", "CANCEL"};
      int response = JOptionPane.showOptionDialog(frame,
                                                  "Do you want to delete user " +
                                                  user + "?",
                                                  "confirmation",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE, null,
                                                  options, options[1]);
      if (response == JOptionPane.OK_OPTION) {
        deleteUserConfirmed(user);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No user is selected. You must first select the user you want to delete.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * assignRole
   */
  public void assignRole() {
    User user = getSelectedUser();
    if (user != null) {
      frmRoles.setTitle("Select a role");
      if (this.fillNotAssignedRolesList(user)) {
        if (frmRoles.showCentered()) {
          this.assignRoleConfirmed(user, this.getAssRoleSelected());
        }
      }
      else {
        JOptionPane.showInternalMessageDialog(frame,
                                              "All roles are already assigned to this user.",
                                              "information",
                                              JOptionPane.INFORMATION_MESSAGE);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No user is selected. You must first select an user you want to edit.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public static Role[] selectRole(List<Role> roles, Frame parent,
                                  JInternalFrame monitor) {
    FrmRoles frame = new FrmRoles(parent, "Roles",  monitor);
    frame.setTitle("Select a role");
    Role[] result = null;
    if (roles.size() > 0) {
      frame.fillListRoles(roles);
      if (frame.showCentered()) {
        Object[] selected = frame.getSelectedRoles();
        if (selected instanceof Role[]) {
          result = (Role[]) selected;
        }
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(monitor,
                                            "There is no role to select.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
    return result;
  }

  /**
   * disassignRole
   */
  public void disassignRole() {
    User user = getSelectedUser();
    Role role = this.getSelectedAssRole();
    if ( (user != null) && (role != null)) {
      Object[] options = {
          "OK", "CANCEL"};
      int response = JOptionPane.showOptionDialog(frame,
                                                  "Do you want to disassign the role" +
                                                  role + " from user " +
                                                  user + "?",
                                                  "confirmation",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE, null,
                                                  options, options[1]);
      if (response == JOptionPane.OK_OPTION) {
        this.disassignRoleConfirmed(user, role);
      }
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "No user or no role is selected. You must first select the user and the role you want to disassign.",
                                            "information",
                                            JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * selectedUser
   */
  public void selectedUser() {
    User user = this.getSelectedUser();
    if (user != null) {
      Collection<Role> roles = user.getRoles();
      frame.setAssignedRoles("assigned roles - " + user.getFullName());
      frame.fillAssignedRoles(roles);
    }
  }

  /**
   * fillUsers
   */
  public void fillUsers() {
    frame.fillUsers(this.getControl().getOrganization().getUsers());
  }

  /**
   * startUsers
   */
  public void startUsers() {
    fillUsers();
    selectedUser();
  }

  /**
   * getSelectedUser
   *
   * @return User
   */
  public User getSelectedUser() {
    Object selected = frame.getSelectedUser();
    User user = null;
    if (selected != null) {
      if (selected instanceof User) {
        user = (User) selected;
      }
    }
    return user;
  }

  /**
   * addUserConfirmed
   */
  public void addUserConfirmed() {
    User user = getOrganizationControl().createUser();
    boolean ok = this.fillUserFromForm(user, frmUser);
    if (ok) {
      if (!getOrganizationControl().addUser(user)) {
        JOptionPane.showInternalMessageDialog(frame,
                                              "You cannot add a user that alreday exists.",
                                              "information",
                                              JOptionPane.INFORMATION_MESSAGE);
      }
      else {
        this.startUsers();
      }
    }
  }

  /**
   * fillFormFromUser
   *
   * @param anUser User
   * @param anForm FrmUser
   */
  public void fillFormFromUser(User anUser, FrmUser anForm) {
    if (anForm != null) {
        anForm.fromUser(anUser);
    }
  }

  /**
   * fillUserFromForm
   *
   * @param anUser User
   * @param anForm FrmUser
   * @return boolean
   */
  public boolean fillUserFromForm(User anUser, FrmUser anForm) {
    boolean ok = true;
    if ( anUser != null) {
      ok = anForm.toUser(anUser);
      if (!ok) {
        JOptionPane.showInternalMessageDialog(frame,
                                              "Please type the same password in the two password fields! ",
                                              "error",
                                              JOptionPane.ERROR_MESSAGE);
      }
    }
    return ok;
  }

  /**
   * editUserConfirmed
   *
   * @param user User
   */
  public void editUserConfirmed(User user) {
    User temp = new User(user.getId());
    if (this.fillUserFromForm(temp, frmUser)) {
      if (getOrganizationControl().editUser(temp)) {
        this.fillUserFromForm(user, frmUser);
        this.startUsers();
      }
      else {
        JOptionPane.showInternalMessageDialog(frame,
                                              "This user name is already assigned to another user! ",
                                              "error",
                                              JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * deleteUserConfirmed
   *
   * @param user User
   */
  public void deleteUserConfirmed(User user) {
    boolean canRemove = getOrganizationControl().deleteUser(user);
    if (canRemove) {
      this.startUsers();
    }
    else {
      JOptionPane.showInternalMessageDialog(frame,
                                            "The user " + user + " could not be removed.",
                                            "error", JOptionPane.ERROR);
    }
  }

  /**
   * fillNotAssignedRolesList(JList jList)
   *
   * @param user user
   * @return boolean
   */
  public boolean fillNotAssignedRolesList(User user) {
    List<Role> notAssigned = getOrganizationControl().getNotAssignedRoles(user);
    frmRoles.fillListRoles(notAssigned);
    return (notAssigned.size() > 0);
  }

  /**
   * assignRoleConfirmed
   *
   * @param anUser User
   * @param anRole Role
   */
  public void assignRoleConfirmed(User anUser, Object[] roles) {
    boolean ok = false;
    if ( (anUser != null) && (roles != null)) {
      for (int i = 0; i < roles.length; i++) {
        Object role = roles[i];
        if (role instanceof Role) {
          ok = getOrganizationControl().assignRole(anUser, (Role) role);
        }
      }
    }
    if (ok) {
      this.selectedUser();
    }
  }

  /**
   * getAssRoleSelected
   *
   * @return Role
   */
  public Object[] getAssRoleSelected() {
    Object[] object = frmRoles.getSelectedRoles();
    return object;
  }

  /**
   * getSelectedAssRole
   *
   * @return Role
   */
  public Role getSelectedAssRole() {
    Role role = null;
    Object object = this.frame.getSelectedAssignedRole();
    if (object != null) {
      if (object instanceof Role) {
        role = (Role) object;
      }
    }
    return role;
  }

  /**
   * disassignRoleConfirmed
   *
   * @param anUser User
   * @param anRole Role
   */
  public void disassignRoleConfirmed(User anUser, Role anRole) {
    if ( (anUser != null) && (anRole != null)) {
      this.getOrganizationControl().disassignRole(anUser, anRole);
      this.selectedUser();
    }
  }

  private OrganizationControl getOrganizationControl() {
    return this.getControl().getOrganization();
  }
}
