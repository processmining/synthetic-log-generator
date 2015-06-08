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

public class ActivityDataModel {

  List<ActivityDataDefinition> elements;
  ActivityDefinition activity;

  public ActivityDataModel(ActivityDefinition activity) {
    super();
    this.activity = activity;
    elements = new ArrayList<ActivityDataDefinition> ();
  }

  public int count() {
    return elements.size();
  }

  public Iterator<ActivityDataDefinition> elements() {
    return elements.iterator();
  }

  public Object[] elementsArray() {
    return elements.toArray();
  }

  public int nextElementId() {
    int id = 0;
    if (elements.size() > 0) {
      id = elements.get(elements.size() - 1).getId() + 1;
    }
    return id;
  }

  /*public ActivityDataDefinition add(DataElement data) {
    ActivityDataDefinition element = null;
    if ( ! this.contains(data) ){
      element = new ActivityDataDefinition(nextElementId(), data);
      elements.add(element);
    }
    return element;
     }*/

  boolean add(ActivityDataDefinition element) {
    boolean ok = !this.contains(element.getDataElement());
    if (ok) {
      elements.add(element);
    }
    return ok;
  }

  public void addElement(ActivityDataDefinition data) {
    if (!this.contains(data.getDataElement())) {
      elements.add(data);
    }
  }

  public void remove(ActivityDataDefinition element) {
    elements.remove(element);
  }

  public Object clone() {
    ActivityDataModel clone = new ActivityDataModel(this.activity);
    Iterator<ActivityDataDefinition> iterator = this.elements();
    while (iterator.hasNext()) {
      ActivityDataDefinition data = iterator.next();
      clone.elements.add( (ActivityDataDefinition) data.clone());
    }
    return clone;
  }

  boolean contains(DataElement element) {
    boolean found = false;
    if (element != null) {
      Iterator<ActivityDataDefinition> iterator = this.elements();
      while (iterator.hasNext() && !found) {
        ActivityDataDefinition activityData = iterator.next();
        found = activityData.getDataElement() == element;
      }
    }
    return found;
  }

  public Object[] availableDataElements() {
    ArrayList<DataElement> v = new ArrayList<DataElement>();
    AssignmentModel model = activity.getAssignmentModel();
    for (int i = 0; i < model.getDataCount(); i++) {
      DataElement data = model.dataAt(i);
      if (!this.contains(data)) {
        v.add(data);
      }
    }
    return v.toArray();
  }
}
