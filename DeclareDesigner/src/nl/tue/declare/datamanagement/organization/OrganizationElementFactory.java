package nl.tue.declare.datamanagement.organization;

import org.w3c.dom.*;

import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import nl.tue.declare.domain.organization.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 * @author Maja Pesic
 * @version 1.0
 */
public class OrganizationElementFactory
    extends XMLElementFactory {

  private static final String TAG_ROLES = "roles";
  private static final String TAG_USERS = "users";

  // *** xml tags for the role element
  public static final String TAG_ROLE = "role";
  private static final String TAG_ROLE_NAME = "name";

  // *** xml tags for the user element
  private static final String TAG_USER = "user";
  private static final String TAG_USER_NAME = "name";
  private static final String TAG_USER_LASTNAME = "lastname";
  private static final String TAG_USER_USERNAME = "username";
  private static final String TAG_USER_PASSWORD = "password";
  // *** xml tags for the roles that are assigned to the user
  public static final String TAG_USER_ROLES = "roles";
  private static final String TAG_USER_ROLE = "role";


  /**
   * XMLElementFactory
   */
  public OrganizationElementFactory(XMLBroker broker) {
    super(broker);
  }

  /**
   * XMLElementFactory
   */
  public OrganizationElementFactory(XMLElementFactory factory) {
    super(factory);
  }

  /**
   * roleToElement
   *
   * @param role Role
   * @param broker XMLBroker
   * @return Element
   */
  public Element roleToElement(Role role) {
    // *** create an element for the role
    Element element = baseToElement(role, TAG_ROLE);

    // *** create tags for all attributes of the role
    Element nameTag = createObjectAttribute(TAG_ROLE_NAME, role.getName());

    // *** add all attribute tags to the role element
    element.appendChild(nameTag);

    // *** return the element for the role
    return element;
  }

  /**
   * updateRoleElement
   *
   * @param role Role
   * @param element Element
   * @param broker XMLBroker
   */
  protected void updateRoleElement(Role role, Element element) {
    updateObjectAttribute(element, TAG_ROLE_NAME, role.getName());
  }

  /**
   * elementToRole
   *
   * @param element Element
   * @param broker XMLBroker
   * @return Role
   */
  public Role elementToRole(Element element) {
    Role role = new Role(elementToBase(element).getId());
    role.setName(getSimpleElementText(element, TAG_ROLE_NAME));
    return role;
  }

  /**
   * getListRoles
   *
   * @param element Element
   * @return NodeList
   */
  protected static NodeList getListRoles(Element element) {
    return element.getElementsByTagName(TAG_ROLE);
  }

  Element getRoleElement(Role role, Element element) {
    NodeList roles = getListRoles(element);
    boolean found = false;
    Element current = null;
    Role currentRole;
    int i = 0;
    while ( (!found) && (i < roles.getLength())) {
      current = (Element) roles.item(i++);
      currentRole = elementToRole(current);
      found = currentRole.getId() == role.getId();
    }
    return found ? current : null;
  }

  /**
   * getUserElement
   *
   * @param user User
   * @param element Element
   * @param broker XMLBroker
   * @return Element
   */
  Element getUserElement(User user, Element element) {
    NodeList users = getListUsers(element);
    boolean found = false;
    Element current = null;
    User currentUser;
    int i = 0;
    while ( (!found) && (i < users.getLength())) {
      current = (Element) users.item(i++);
      currentUser = elementToUser(current);
      found = currentUser.getId() == user.getId();
    }
    return found ? current : null;
  }

  /**
   * userToElement
   *
   * @param user Role
   * @param broker XMLBroker
   * @return Element
   */
  protected Element userToElement(User user) {
    // *** create a new element for the user
    Element element = baseToElement(user, TAG_USER);

    // *** create tags for the attributes of the user
    Element nameTag = createObjectAttribute(TAG_USER_NAME, user.getName());
    Element lastnameTag = createObjectAttribute(TAG_USER_LASTNAME,
                                                user.getLastName());
    Element usernameTag = createObjectAttribute(TAG_USER_USERNAME,
                                                user.getUserName());
    Element passwordTag = createObjectAttribute(TAG_USER_PASSWORD,
                                                user.getPassword());

    // *** add all attributes tags to the user element
    element.appendChild(nameTag);
    element.appendChild(lastnameTag);
    element.appendChild(usernameTag);
    element.appendChild(passwordTag);
    // *** return the element for the user
    return element;
  }

  /**
   * getListUsers
   *
   * @param element Element
   * @return NodeList
   */
  protected static NodeList getListUsers(Element element) {
    return element.getElementsByTagName(TAG_USER);
  }

  /**
   * elementToRole
   *
   * @param element Element
   * @param broker XMLBroker
   * @return Role
   */
  protected User elementToUser(Element element) {
    // *** create a new User object with the ID of the element
    User user = new User(elementToBase(element).getId());

    // *** read all the attributes form the element
    user.setName(getSimpleElementText(element, TAG_USER_NAME));
    user.setLastName(getSimpleElementText(element, TAG_USER_LASTNAME));
    user.setUserName(getSimpleElementText(element, TAG_USER_USERNAME));
    user.setPassword(getSimpleElementText(element, TAG_USER_PASSWORD));

    // *** retunr the new User object
    return user;
  }

  /**
   * updateUserElement
   *
   * @param user Role
   * @param element Element
   * @param broker XMLBroker
   */
  protected void updateUserElement(User user, Element element) {
    // *** update all attributes of the User element according to the User object
    updateObjectAttribute(element, TAG_USER_NAME, user.getName());
    updateObjectAttribute(element, TAG_USER_LASTNAME, user.getLastName());
    updateObjectAttribute(element, TAG_USER_USERNAME, user.getUserName());
    updateObjectAttribute(element, TAG_USER_PASSWORD, user.getPassword());
  }

  /**
   * addUserRole
   *
   * @param user Element
   * @param role Role
   * @param broker XMLBroker
   */
  protected void addUserRole(Element user, Role role) {
    Element roles = getUserRolesElement(user);
    Element assignedRole = assignedRoleToElement(role);
    roles.appendChild(assignedRole);
  }

  /**
   * removeUserRole
   *
   * @param user Element
   * @param role Role
   * @param broker XMLBroker
   */
  protected void removeUserRole(Element user, Role role) {
    Element roles = getUserRolesElement(user);
    Element assignedRole = getAssignedRoleElement(user, role);
    deleteElement(assignedRole, roles);
  }

  /**
   * getRoleElement
   *
   * @param user Element
   * @param role Role
   * @return Element
   */
  private Element getAssignedRoleElement(Element user, Role role) {
    NodeList roles = OrganizationElementFactory.getListRoles(user);
    boolean found = false;
    Element current = null;
    Role currentRole;
    int i = 0;
    while ( (!found) && (i < roles.getLength())) {
      current = (Element) roles.item(i++);
      currentRole = new Role(elementToBase(current).getId());
      found = currentRole.getId() == role.getId();
    }
    return found ? current : null;
  }

  /**
   * getUserRolesElement
   *
   * @return Element
   * @param element Element
   * @param broker XMLBroker
   */
  public Element getUserRolesElement(Element element) {
    return getFirstElement(element, TAG_USER_ROLES);
  }

  /**
   * assignedRoleToElement
   *
   * @param role Role
   * @param broker XMLBroker
   * @return Element
   */
  public Element assignedRoleToElement(Role role) {
    // *** create an element for the role
    Element element = baseToElement(role, TAG_USER_ROLE);

    // *** return the element for the role
    return element;
  }

  /**
   * getRolesElement
   *
   * @return Element
   */
  Element getRolesElement(Element element) {
    return getFirstElement(element, TAG_ROLES);
  }

  /**
   * getUsersElement
   *
   * @return Element
   */
  Element getUsersElement(Element element) {
    return this.getFirstElement(element, TAG_USERS);
  }
}
