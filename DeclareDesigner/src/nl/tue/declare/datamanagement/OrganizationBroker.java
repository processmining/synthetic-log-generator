package nl.tue.declare.datamanagement;

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

public interface OrganizationBroker {
  /**
   * addUser
   *
   * @param anUser User
   */
  public void addUser(User anUser);

  /**
   * deleteUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean deleteUser(User anUser);

  /**
   * editUser
   *
   * @param anUser User
   * @return boolean
   */
  public boolean editUser(User anUser);

  /**
   * getUsers
   *
   * @return List
   */
  public List<User> readUsers();

  /**
   * This method returns a list of roles with only one field - the id!
   * For every item in this list, search for the real role with the id as the list item,
   * and then assign the found role to the user.
   *
   * @param anUser User
   */
  public List<Role> readAssignedRoles(User anUser);

  /**
   * getUsers
   *
   * @param anUser User
   * @param aRole Role
   */
  public void assignRole(User anUser, Role aRole);

  /**
   * getUsers
   *
   * @param anUser User
   * @param aRole Role
   */
  public void disassignRole(User anUser, Role aRole);

  /**
   * addRole
   *
   * @param anRole Role
   */
  public void addRole(Role anRole);

  /**
   * deleteRole
   *
   * @param anRole Role
   * @return boolean
   */
  public boolean deleteRole(Role anRole);

  /**
   * editRole
   *
   * @param aRole Role
   * @return boolean
   */
  public boolean editRole(Role aRole);

  /**
   * getRoles
   *
   * @return List
   */
  public List<Role> readRoles();
}
