package nl.tue.declare.datamanagement;

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
import org.w3c.dom.*;

import nl.tue.declare.execution.*;

public class XMLWorkItemBroker
    extends XMLBroker {

  ElementFactory factory;
  WorkItem item;

  public XMLWorkItemBroker(WorkItem item) {
    super("", "workitem");
    factory = new ElementFactory(this);
    this.item = item;
  }

  public Element toElelemnt() {
    Element root = this.getDocumentRoot();
    if (item != null) {
      root.appendChild(factory.workItemToElement(item));
    }
    return root;
  }

  public void fromElement() {
    if (item != null) {
      Element root = this.getDocumentRoot();
      factory.elementToWorkItem(this.item, root);
    }
  }

  private class ElementFactory
      extends XMLElementFactory {

    private static final String WORK_ITEM = "item";
    private static final String ASSIGNMENT = "assignment";
    private static final String ACTIVITY = "activity";
    private static final String USER = "user";

    private static final String DATA = "data";
    private static final String ELEMENT = "element";
    private static final String ACTIVITY_DATA = "activitydata";
    private static final String VALUE = "value";

    /**
     * ElementFactory
     *
     * @param aBroker XMLBroker
     */
    public ElementFactory(XMLBroker aBroker) {
      super(aBroker);
    }

    Element workItemToElement(WorkItem item) {
      Element element = this.baseToElement(item, WORK_ITEM);
      // set attributes
      element.setAttribute(ASSIGNMENT,Integer.toString(item.getActivity().getAssignment().getId()));
      element.setAttribute(ACTIVITY, item.getActivity().getIdString());
      element.setAttribute(USER, item.getUser().getIdString());
      // create tags for data
      element.appendChild(this.dataToElement(item));

      //Element attributes = attributesToElement(item.getAttributes());
      //element.appendChild(attributes);

      attributesToElement(item.getAttributes(),element);


      return element;
    }

    /**
     *
     * @param item WorkItem
     * @return Element
     */
    Element dataToElement(WorkItem item) {
      Element element = createElement(DATA);
      for (int i = 0; i < item.dataCount(); i++) {
        element.appendChild(workItemDataToElement(item.getDataAt(i)));
      }
      return element;
    }

    /**
     *
     * @param data WorkItemData
     * @return Element
     */
    Element workItemDataToElement(WorkItemData data) {
      Element element = createElement(ELEMENT);
      // set attributes
      element.setAttribute(ACTIVITY_DATA, data.getData().getIdString());
      String value = "EMPTY";
      if (data.getValue() != null) {
        value = data.getValue().getValue();
      }
      element.setAttribute(VALUE, value);

      return element;
    }

    void elementToWorkItem(WorkItem item, Element element) {
      NodeList list = element.getElementsByTagName(ELEMENT);
      for (int i = 0; i < list.getLength(); i++) {
        Element data = (Element) list.item(i);
        String idString = data.getAttribute(ACTIVITY_DATA);
        String valueString = data.getAttribute(VALUE);

        int id = Integer.parseInt(idString);
        item.getDataWithId(id).setValue(valueString);
      }
      elementToAttributes(element, item.getAttributes());
    }

  }
}
