package nl.tue.declare.datamanagement.template;

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

import org.w3c.dom.*;
import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.template.*;

public class XMLConstraintGroupBroker
    extends XMLBroker implements ConstraintGroupBroker {

  private static final String TAG_GROUPS = "constraintgroups";
  private static final String TAG_WARNING_LEVEL = "warning";

  TemplateGroupFactory factory;

  public XMLConstraintGroupBroker(String aConnectionString, String aName) {
    super(aConnectionString, aName);
    readDocument();
    this.factory = new TemplateGroupFactory(this);
  }

  public void addGroup(ConstraintGroup group) {
    Element newGroup = this.factory.groupToElement(group);
    Element groups = this.getGroupsElement();
    if (groups != null) {
      groups.appendChild(newGroup);
      writeDocument();
    }
  }

  public boolean editGroup(ConstraintGroup group) {
    Element element = factory.getGroupElement(group,
                                              this.getGroupsElement());
    if (element != null) {
      factory.groupToElement(group, element);
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   *
   * @param group ConstraintGroup
   * @return boolean
   */
  public boolean deleteGroup(ConstraintGroup group) {
    Element element = factory.getGroupElement(group,
                                              this.getGroupsElement());
    if (element != null) {
      deleteElement(element, this.getGroupsElement());
      writeDocument();
      return true;
    }
    return false;
  }

  /**
   *
   * @return Collection
   */
  public Collection<ConstraintGroup> readGroups() {
    List<ConstraintGroup> list = new ArrayList<ConstraintGroup> ();
    Element element;
    ConstraintGroup group;
    NodeList templates = factory.getListGroups(this.getGroupsElement());
    for (int i = 0; i < templates.getLength(); i++) {
      element = (Element) templates.item(i);
      group = factory.elementToGroup(element);
      list.add(group);
    }
    return list;
  }

  /**
   *
   * @param level ConstraintWarningLevel
   */
  public void writeWarningLevel(ConstraintWarningLevel level) {
    Element element = this.getWarningElement();
    if (element != null) {
      factory.warningToElement(level, element);
      writeDocument();
    }
  }

  /**
   *
   * @return ConstraintWarningLevel
   */
  public ConstraintWarningLevel getWarningLevel() {
    Element element = this.getWarningElement();
    ConstraintWarningLevel level = null;
    if (element != null) {
      level = factory.elementToConstraintWarningLevel(element);
    }
    return level;
  }

  /**
   * getGroupsElement()
   *
   * @return Element
   */
  public Element getGroupsElement() {
    return factory.getFirstElement(this.getDocumentRoot(), TAG_GROUPS);
  }

  /**
   * getWarningElement()
   *
   * @return Element
   */
  public Element getWarningElement() {
    return factory.getFirstElement(this.getDocumentRoot(), TAG_WARNING_LEVEL);
  }
}
