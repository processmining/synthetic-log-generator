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

public class AssignmentDataModel {

  AssignmentModel model;
  List<DataElement> elements;

  public AssignmentDataModel(AssignmentModel model) {
    super();
    this.model = model;
    elements = new ArrayList<DataElement>();
  }

  public Object clone() {
    AssignmentDataModel clone = new AssignmentDataModel(this.model);
    Iterator<DataElement> i = elements.iterator();
    while (i.hasNext()) {
      clone.add( (DataElement) i.next().clone());
    }
    return clone;
  }

  /**
   * add
   *
   * @param element DataElement
   * @return boolean
   */
  public boolean add(DataElement element) {
    boolean canAdd = !elements.contains(element);
    if (canAdd) {
      elements.add(element);
    }
    return canAdd;
  }

  /**
   * add
   *
   * @param Id DataElement
   * @return boolean
   */
  DataElement add(int Id, String name, DataElement.Type type, String initial) {
    DataElement element = new DataElement(Id, this.model, name, initial, type);
    return this.add(element) ? element : null;
  }

  /**
   * remove
   *
   * @param element DataElement
   * @return boolean
   */
  public boolean delete(DataElement element) {
    return elements.remove(element);
  }

  /**
   * get
   *
   * @param anIndex int
   * @return DataElement
   */
  public DataElement get(int anIndex) {
    return elements.get(anIndex);
  }

  /**
   * getId
   *
   * @param anId int
   * @return DataElement
   */
  public DataElement getId(int anId) {
    Iterator<DataElement> it = elements.iterator();
    boolean found = false;
    DataElement element = null;
    while (it.hasNext() && !found) {
      element = it.next();
      found = (element.getId() == anId);
    }
    return found ? element : null;
  }

  /**
   * getDataElements
   *
   * @return List
   */
  public List<DataElement> getElements() {
    return elements;
  }

  /**
   * nextDataElementId
   *
   * @return int
   */
  public int nextDataElementId() {
    int id = 0;
    DataElement element = null;
    Iterator<DataElement> it = elements.iterator();
    while (it.hasNext()) {
      element = it.next();
      if (id < element.getId()) {
        id = element.getId();
      }
    }
    return++id;
  }

  /**
   * createDataElement
   *
   * @return DataElement
   */
  DataElement createDataElement() {
    return new DataElement(nextDataElementId(), model, "", "",
                           DataElement.Type.STRING);
  }

  /**
   * contains
   *
   * @param element DataElement
   * @return boolean
   */
  public boolean contains(DataElement element) {
    return elements.contains(element);
  }

  /**
   * getSize
   *
   * @return int
   */
  public int getSize() {
    return elements.size();
  }

  /**
   * clear
   */
  public void clear() {
    elements.clear();
  }
}
