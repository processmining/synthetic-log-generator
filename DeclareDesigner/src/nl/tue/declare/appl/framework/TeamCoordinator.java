package nl.tue.declare.appl.framework;

import java.util.*;
import java.util.List;

import java.awt.*;

import nl.tue.declare.appl.framework.gui.ITeamListener;
import nl.tue.declare.appl.framework.gui.TeamPanel;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;

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
public class TeamCoordinator
    implements ITeamListener {

  private Assignment assignment;

  private TeamPanel panel = new TeamPanel();

  private FrmSelectFromList frmSelect;

  public TeamCoordinator(Frame parent, Container monitor) {
    super();
    this.assignment = null;

    panel.setListener(this);

    frmSelect = new FrmSelectFromList(parent, "", monitor);
    frmSelect.setMyBounds(300, 400);
    frmSelect.setMultipleSelectionMode();
  }

  public void setAssignment(Assignment assignment) {
    this.assignment = assignment;
    if (assignment != null) {
      this.fillTeam();
    }
  }

  public Container teamContainer() {
    return this.panel;
  }

  /**
   * fill team info on the load form
   */
  private void fillTeam() {
    this.panel.clear();
    TeamModel model = assignment.getTeamModel();
    Team team = assignment.getTeam();
    for (int i = 0; i < model.getSize(); i++) {
      TeamRole teamRole = model.get(i);
      String members = team.getStringMembers(teamRole);
      panel.add(teamRole, members);
    }
  }

  /**
   * Finds all users that can be added to the team for the spcified role. These are
   * users who have authorizations to fill the team role.
   * @param teamRole team role for which the authorized users are found
   * @return a list for authorized users to be assigned to the team role.
   */
  private List<User> membersToAdd(TeamRole teamRole) {
    List<User> members = new ArrayList<User> ();
    Team team = this.assignment.getTeam();
    Control control = Control.singleton();
    for (User user: control.getOrganization().getUsers()) {
      if (team.canAssign(user, teamRole)) {
        members.add(user);
      }
    }
    return members;
  }

  /**
   *
   * @param teamRole TeamRole
   * @return List
   */
  private List<User> membersToRemove(TeamRole teamRole) {
    List<User> members = new ArrayList<User> ();
    Team team = this.assignment.getTeam();
    for (int i = 0; i < team.getSize(); i++) {
      Team.UserMap map = team.get(i);
      if (map.hasTeamRole(teamRole)) {
        members.add(map.getUser());
      }
    }
    return members;
  }

  public void addTeamMembers() {
    TeamRole teamRole = panel.getSelectedTeamRole();
    if (teamRole != null) {
      List<User> members = this.membersToAdd(teamRole);
      this.frmSelect.setTitle("Add participants");
      this.frmSelect.fillList(new ArrayList<Object> (members));
      if (this.frmSelect.showCentered()) {
        Object[] selected = frmSelect.getSelectedMultiple();
        if (selected != null) {
          this.addMembers(teamRole, selected);
          this.fillTeam();
        }
      }
    }
  }

  /**
   * Add a list of users to the team.
   * @param teamRole the team role for which the users are added to the team.
   * @param users the users that are assigned to the team role
   */
  private void addMembers(TeamRole teamRole, Object[] users) {
    Team team = assignment.getTeam();
    for (int i = 0; i < users.length; i++) {
      Object object = users[i];
      if (object != null) {
        if (object instanceof User) {
          User user = (User) object;
          team.assign(user, teamRole);
        }
      }
    }
  }

  public void removeTeamMembers() {
    TeamRole teamRole = panel.getSelectedTeamRole();
    if (teamRole != null) {
      List<User> members = this.membersToRemove(teamRole);
      this.frmSelect.setTitle("Remove participants");
      this.frmSelect.fillList(new ArrayList<Object> (members));
      if (this.frmSelect.showCentered()) {
        Object[] selected = frmSelect.getSelectedMultiple();
        if (selected != null) {
          this.removeMembers(teamRole, selected);
          this.fillTeam();
        }
      }
    }
  }

  private void removeMembers(TeamRole teamRole, Object[] users) {
    Team team = assignment.getTeam();
    for (int i = 0; i < users.length; i++) {
      Object object = users[i];
      if (object != null) {
        if (object instanceof User) {
          User user = (User) object;
          team.disassignTeamRole(user, teamRole);
        }
      }
    }
  }

  public boolean onOk() throws Exception {
    Team team = assignment.getTeam();

    assignAdministartor(assignment); // assigne administrator to all team roles
    boolean teamOk = team.ok(); // check if team roles mapping is ok

    if (!teamOk) {
      throw (new Exception("Some model roles do not have assigned participants!"));
    }
    return true;
  }

  void assignAdministartor(Assignment assignment) {
    assignment.getTeam().superUser(Administrator.singleton());
  }

  boolean isEmpty() {
    return assignment.getTeamModel().getSize() == 0;
  }

  public boolean onCancel() throws Exception {
    return false;
  }

  public void reload() {
    this.fillTeam();
  }
}
