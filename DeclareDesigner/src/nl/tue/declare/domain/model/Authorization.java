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

import nl.tue.declare.domain.organization.*;

public class Authorization {

  private Set<TeamRole> authorizedRoles;
  private ActivityDefinition activityDefiniton;

  /**
   *
   * @param activityDefiniton ActivityDefinition
   */
  public Authorization(ActivityDefinition activityDefiniton) {
    super();
    this.activityDefiniton = activityDefiniton;
    authorizedRoles = new HashSet<TeamRole>();
  }

  /**
  *
  * @param teamRole TeamRole
  * @return boolean
  */
 public void authorize(TeamRole role) {
   if (role != null){
     authorizedRoles.add(role);
   }
 }  
  
  /**
   *
   * @param teamRole TeamRole
   * @return boolean
   */
  public void authorize(Iterable<TeamRole> roles) {
	for (TeamRole role :roles){  
    if (!this.authorized(role)) {
      authorize(role);
    }
	}
  }

  /**
   *
   * @param teamRole TeamRole
   * @return boolean
   */
  public boolean authorized(TeamRole teamRole) {
    return authorizedRoles.contains(teamRole);
  }

  /**
   *
   * @param teamRole TeamRole
   * @return boolean
   */
  public boolean remove(TeamRole teamRole) {
    boolean canRemove = this.authorized(teamRole);
    if (canRemove) {
      return authorizedRoles.remove(teamRole);
    }
    return false;
  }

  /**
   *
   * @return List
   */
  public Iterable<TeamRole>  getAuthorizedList() {
    return authorizedRoles;
  }

  /**
   *
   * @param index int
   * @return TeamRole
   */
  public TeamRole authorizedAt(int index) {
    TeamRole role = null;
    if (index < this.count()) {
      Iterator<TeamRole> iterator = authorizedRoles.iterator();
      int i = 0;
      while (iterator.hasNext() && (i++) <= index) {
        role = iterator.next();
      }
    }
    return role;
  }

  /**
   *
   * @return List
   */
  public Iterable<TeamRole>  getUnauthorizedList() {
    List<TeamRole> unauthorized = new ArrayList<TeamRole>();
    TeamModel teamModel = this.activityDefiniton.getAssignmentModel().
        getTeamModel();
    for (int i = 0; i < teamModel.getSize(); i++) {
      TeamRole teamRole = teamModel.get(i);
      if (!this.authorized(teamRole)) {
        unauthorized.add(teamRole);
      }
    }
    return unauthorized;
  }

  /**
   *
   * @return Object
   */
  public Object clone() {
    Authorization myClone = new Authorization(this.activityDefiniton);
    myClone.authorizedRoles.addAll(this.authorizedRoles);
    return myClone;
  }

  /**
   *
   */
  public void clear() {
    authorizedRoles.clear();
  }

  /**
   *
   * @return int
   */
  public int count() {
    return authorizedRoles.size();
  }

  /**
   *
   * @param elements List
   */
  /*public void authorize(List<TeamRole> elements) {
    if (elements != null) {
      for (int i = 0; i < elements.size(); i++) {
        this.authorize(elements.get(i));
      }
    }
  }*/

  /**
   *
   * @param user User
   * @return boolean
   */
  public boolean authorized(User user) {
    if (authorizedRoles.size() == 0) {
      return true; // if no roles assigned, everyone can execute this.
    }
    boolean auth = false;
    Iterator<TeamRole> iterator = authorizedRoles.iterator();
    while (iterator.hasNext() && !auth) { // loop through all team roles authorized
      auth = iterator.next().authorized(user); // quit the loop when you fint the firs team role that the user has
    }
    return auth;
  }
}
