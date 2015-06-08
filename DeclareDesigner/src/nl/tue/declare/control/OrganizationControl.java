package nl.tue.declare.control;

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

import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.BaseCollection;
import nl.tue.declare.domain.organization.*;

public class OrganizationControl {

  private static OrganizationControl INSTANCE = null;

  private BaseCollection<Role> roles;
  private BaseCollection<User> users;

  private OrganizationBroker broker;

  private OrganizationControl() {
    super();
    roles = new BaseCollection<Role>();
    users = new BaseCollection<User>();

    broker = XMLBrokerFactory.newOrganizationBroker();
    loadRoles();
    loadUsers();

    // add all roles to administrator
    for (Role r: roles) {
      Administrator.singleton().addRole(r);
    }
  }

  static OrganizationControl singleton() {
    if (INSTANCE == null) {
      INSTANCE = new OrganizationControl();
    }
    return INSTANCE;
  }

  /**
   * addRole
   *
   * @param anRole Role
   * @return boolean
   */
  public void addRole(Role anRole) {
    roles.add(anRole);
    broker.addRole(anRole);
  }
  
  /**
   * addRole
   *
   * @param anRole Role
   * @return boolean
   */
  public boolean containsRole(Role r) {
    Iterator<Role> it = roles.iterator();
    boolean found = false;
    while (it.hasNext() && !found){
    	found = it.next().equals(r);
    }
    return found;
  }

  /**
   * removeRole
   *
   * @param anRole Role
   * @return boolean
   */
  public boolean deleteRole(Role anRole) {
    boolean canRemove = true;
    Iterator<User> it = users.iterator();
    User user = null;
    while (it.hasNext() && canRemove) {
      user = it.next();
      canRemove = !user.getRoles().contains(anRole);
    }
    if (canRemove) {
      roles.remove(anRole);
      canRemove = broker.deleteRole(anRole);
    }
    return canRemove;
  }

  /**
   * getRole
   *
   * @param anIndex int
   * @return Role
   */
  public Role getRole(int anIndex) {
    return roles.get(anIndex);
  }

  /**
   * getIdRole
   *
   * @param anId int
   * @return Role
   */
  public Role getIdRole(int anId) {
    return roles.getItemWithId(anId);
  }

  /**
   * getRoles
   *
   * @return List
   */
  public Collection<Role> getRoles() {
    return roles;
  }

  /**
   * createRole
   *
   * @return Role
   */
  public Role createRole() {
    return new Role(roles.nextId());
  }

  /**
   * addUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean addUser(User anUser) {
    boolean canAdd = !users.contains(anUser) &&
        !anUser.equals(Administrator.singleton());
    if (canAdd) {
      users.add(anUser);
      broker.addUser(anUser);
    }
    return canAdd;
  }

  /**
   * deleteUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean deleteUser(User anUser) {
    users.remove(anUser);
    broker.editUser(anUser);
    return true;
  }

  /**
   * createUser
   *
   * @return User
   */
  public User createUser() {	  
    return new User(users.nextId());
  }

  /**
   * assignRole
   *
   * @param anUser User
   * @param anRole Role
   * @return boolean
   */
  public boolean assignRole(User anUser, Role anRole) {
    boolean assigned = anUser.addRole(anRole);
    if (assigned) {
      broker.assignRole(anUser, anRole);
    }
    return assigned;
  }

  /**
   * disassigneRole
   *
   * @param anUser User
   * @param anRole Role
   * @return boolean
   */
  public boolean disassignRole(User anUser, Role anRole) {
    anUser.deleteRole(anRole);
    broker.disassignRole(anUser, anRole);
    return true;
  }

  /**
   * getUsers
   *
   * @return List
   */
  public Collection<User> getUsers() {
    return users;
  }

  /**
   * editUser
   *
   * @param newUser User
   * @return boolean
   */
  public boolean editUser(User newUser) {
    boolean ok = true;
    Iterator<User> it = users.iterator();
    User user = null;
    // *** search for all users that have the user-name like the newUser object
    while (it.hasNext() && ok) {
      user = it.next();
      if (user.getUserName().equals(newUser.getUserName())) {
        // *** allow for the update only if the new user-name is not alreday
        //     assigned to another user (a user with different id)
        ok = user.getId() == newUser.getId();
      }
    }
    if (ok) {
      ok = broker.editUser(newUser);
    }
    return ok;
  }

  /**
   * getUserWithUserName
   *
   * @param anUserName int
   * @return User
   */
  public User getUserWithUserName(String anUserName) {
    if (Administrator.singleton().getUserName().equals(anUserName)) {
      return Administrator.singleton();
    }
    Iterator<User> it = users.iterator();
    User user = null;
    boolean found = false;
    while (it.hasNext() && !found) {
      user = it.next();
      found = (user.getUserName().equals(anUserName));
    }
    return found ? user : null;
  }

  /**
   * getUserWithUserName
   *
   * @param anUserName int
   * @return User
   */
  public User getUserWithID(int id) {
    Iterator<User> it = users.iterator();
    User user = null;
    boolean found = false;
    while (it.hasNext() && !found) {
      user = it.next();
      found = (user.getId() == id);
    }
    return found ? user : null;
  }

  /**
   * getRole
   *
   * @param anIndex int
   * @return Role
   */
  public User getUser(int anIndex) {
    return users.get(anIndex);
  }

  /**
   * getNotAssignedRoles
   *
   * @param anUser User
   * @return List
   */
  public List<Role> getNotAssignedRoles(User anUser) {
    List<Role> notAssigned = null;
    if (anUser != null) {
      Collection<Role> assigned = anUser.getRoles();
      notAssigned = new ArrayList<Role>();
      for (Role role: roles)
        if (!assigned.contains(role)) {
          notAssigned.add(role);
        }
      }
    return notAssigned;
  }

  /**
   * loadRoles
   */
  public void loadRoles() {
    List<Role> list = broker.readRoles();
    this.roles.clear();
    for (int i = 0; i < list.size(); i++) {
      this.roles.add(list.get(i));
    }
  }

  /**
   * editRole
   *
   * @param aRole Role
   * @return boolean
   */
  public boolean editRole(Role aRole) {
    boolean result = broker.editRole(aRole);
    return result;
  }

  /**
   * loadUsers
   */
  public void loadUsers() {
    List<User> list = broker.readUsers();
    this.users.clear();
    User user = null;
    for (int i = 0; i < list.size(); i++) {
      user = list.get(i);
      this.users.add(user);
      loadAssigendRoles(user);
    }
  }

  /**
   * loadAssigendRoles
   *
   * @param anUser User
   */
  public void loadAssigendRoles(User anUser) {
    List<Role> list = broker.readAssignedRoles(anUser);
    anUser.getRoles().clear();
    Role temp, role;
    for (int i = 0; i < list.size(); i++) {
      temp = list.get(i);
      role = roles.getItemWithId(temp.getId());
      anUser.addRole(role);
    }
  }

  /**
   * getUser
   *
   * @param username String
   * @param password String
   * @return User
   */
  public User getUser(String username, String password) {
    User user = null;
    if (username != null && password != null) {
      user = this.getUserWithUserName(username);
    }
    return user;
  }
}
