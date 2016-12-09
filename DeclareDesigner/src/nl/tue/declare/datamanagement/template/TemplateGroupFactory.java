package nl.tue.declare.datamanagement.template;

import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import org.w3c.dom.Element;
import nl.tue.declare.domain.template.ConstraintGroup;
import org.w3c.dom.NodeList;
import nl.tue.declare.domain.Base;
import nl.tue.declare.domain.template.ConstraintWarningLevel;

/**
 *
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
class TemplateGroupFactory
    extends XMLElementFactory {

  private static final String TAG_GROUP = "group";
  private static final String TAG_GROUP_NAME = "name";
  private static final String TAG_GROUP_DESCRIPTION = "description";

  private static final String TAG_WARNING_LEVEL = "level";

  /**
   *
   * @param aBroker XMLBroker
   */
  public TemplateGroupFactory(XMLBroker aBroker) {
    super(aBroker);
  }

  /**
   *
   * @param group ConstraintGroup
   * @return Element
   */
  Element groupToElement(ConstraintGroup group) {
    Element element = baseToElement(group, TAG_GROUP);

    // *** fill the element with group attributes
    this.updateGroupElement(group, element);

    // *** return the group element
    return element;

  }

  /**
   *
   * @param group ConstraintGroup
   * @param element Element
   */
  void updateGroupElement(ConstraintGroup group, Element element) {
    this.groupToElement(group, element);
  }

  /**
   *
   * @param group ConstraintGroup
   * @param element Element
   */
  void groupToElement(ConstraintGroup group, Element element) {
    updateObjectAttribute(element, TAG_GROUP_NAME, group.getName());
    updateObjectAttribute(element, TAG_GROUP_DESCRIPTION,
                          group.getDescription());
  }

  /**
   *
   * @param group ConstraintGroup
   * @param element Element
   * @return Element
   */
  public Element getGroupElement(ConstraintGroup group, Element element) {
    NodeList groups = element.getElementsByTagName(TAG_GROUP);
    boolean found = false;
    Element current = null;
    ConstraintGroup currentGroup;
    int i = 0;
    while ( (!found) && (i < groups.getLength())) {
      current = (Element) groups.item(i++);
      currentGroup = this.elementToGroup(current);
      found = currentGroup.equals(group);
    }
    return found ? current : null;
  }

  /**
   *
   * @param element Element
   * @return ConstraintGroup
   */
  public ConstraintGroup elementToGroup(Element element) {
    Base base = elementToBase(element);
    String description = getSimpleElementText(element,
                                              TAG_GROUP_DESCRIPTION);
    String name = getSimpleElementText(element, TAG_GROUP_NAME);
    ConstraintGroup group = new ConstraintGroup(base.getId());
    group.setName(name);
    group.setDescription(description);
    return group;
  }

  /**
   *
   * @param element Element
   * @return NodeList
   */
  public NodeList getListGroups(Element element) {
    NodeList list = element.getElementsByTagName(TAG_GROUP);
    return list;
  }

  /**
   *
   * @param level ConstraintWarningLevel
   * @param element Element
   */
  void warningToElement(ConstraintWarningLevel level, Element element) {
    updateObjectAttribute(element, TAG_WARNING_LEVEL,
                          Integer.toString(level.getLevel()));
  }

  /**
   *
   * @param element Element
   * @return ConstraintWarningLevel
   */
  public ConstraintWarningLevel elementToConstraintWarningLevel(Element
      element) {
    String level = getSimpleElementText(element, TAG_WARNING_LEVEL);

    ConstraintWarningLevel warning = new ConstraintWarningLevel();
    try {
      warning.setLevel(Integer.parseInt(level));
    }
    catch (Exception e) {
     // e.printStackTrace();
    }
    return warning;
  }
}
