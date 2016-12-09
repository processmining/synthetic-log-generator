package nl.tue.declare.domain.model;

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

import nl.tue.declare.domain.*;
import nl.tue.declare.domain.organization.*;

public class TeamModel {

  List<TeamRole> roles;
  AssignmentModel model;

  public TeamModel(AssignmentModel model) {
    super();
    this.model = model;
    roles = new ArrayList<TeamRole>();
  }

  public Object clone() {
    TeamModel clone = new TeamModel(this.model);
    Iterator<TeamRole> iterator = roles.iterator();
    while (iterator.hasNext()) {
      clone.add( (TeamRole) iterator.next().clone());
    }
    return clone;
  }

  /**
   * add
   *
   * @param teamRole Role
   * @return boolean
   */
  public boolean add(TeamRole teamRole) {
    boolean canAdd = !roles.contains(teamRole);
    if (canAdd) {
      roles.add(teamRole);
    }
    return canAdd;
  }

  /**
   * add
   *
   * @param Id Role
   * @param role Role
   * @return boolean
   */
  public TeamRole add(Role role) {
    TeamRole teamRole = this.createRole(role);
    return this.add(teamRole) ? teamRole : null;
  }

  private int nextTeamRoleId() {
	return Base.nextId(new ArrayList<Base>(roles));
  }

  /**
   * remove
   *
   * @param teamRole Role
   * @return boolean
   */
  public boolean delete(TeamRole teamRole) {
    return roles.remove(teamRole);
  }

  /**
   * get
   *
   * @param anIndex int
   * @return Role
   */
  public TeamRole get(int anIndex) {
    return roles.get(anIndex);
  }

  /**
   * getId
   *
   * @param anId int
   * @return Role
   */
  public TeamRole getId(int anId) {
    Iterator<TeamRole> it = roles.iterator();
    boolean found = false;
    TeamRole role = null;
    while (it.hasNext() && !found) {
      role = it.next();
      found = (role.getId() == anId);
    }
    return found ? role : null;
  }

  /**
   * createRole
   *
   * @return Role
   * @param role Role
   */
  public TeamRole createRole(Role role) {
    return new TeamRole(this.nextTeamRoleId(), role);
  }

  /**
   * contains
   *
   * @param role Role
   * @return boolean
   */
  public boolean contains(TeamRole role) {
    return roles.contains(role);
  }

  /**
   * getSize
   *
   * @return int
   */
  public int getSize() {
    return roles.size();
  }

  /**
   * clear
   */
  public void clear() {
    roles.clear();
  }

  public List<TeamRole> getRoles() {
    return roles;
  }
}
