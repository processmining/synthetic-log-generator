package nl.tue.declare.verification.impl;

import java.util.*;

import nl.tue.declare.utils.*;

public class PowerSet {

  private Elements elements = null;

  private Collection<Object> data;
  private Collection<PowerSet> children;

  private IPowerSetListener listener = null;

  private PowerSet(Collection<Object> data, Elements elements,
                   IPowerSetListener listener) {
    super();
    this.data = data;
    this.elements = elements;
    children = new ArrayList<PowerSet>();
    this.listener = listener;
  }

  public PowerSet() {
    this(new ArrayList<Object>(), new Elements(), null);
  }

  public Iterator<Object> getData() {
    return data.iterator();
  }

  public void add(Object element) {
    elements.add(element);
  }

  private Object last() {
    Object last = null;
    Iterator<Object> iterator = data.iterator();
    while (iterator.hasNext()) {
      last = iterator.next();
    }
    return last;
  }

  private Collection<Object> expand(Object next) {
    Collection<Object> clone = new ArrayList<Object>();
    clone.addAll(data);
    clone.add(next);
    return clone;
  }

  public void expand() throws Throwable {
    if (listener != null) {
       listener.expand(this); // as listener weather to continue
    }
  //  if (expand) {
      Object last = last();
      Iterator<Object> right = elements.right(last).iterator();
      while (right.hasNext()) {
        Object next = right.next();
        PowerSet child = new PowerSet(expand(next), this.elements,
                                      this.listener);
        children.add(child);
        child.expand();
      }
  //  }
  }

  public void addListener(IPowerSetListener listener) {
    this.listener = listener;
  }

  public String toString() {
    String str = "";
    Iterator<Object> iterator = data.iterator();
    while (iterator.hasNext()) {
      Object element = iterator.next();
      str += ( (!str.equals("")) ? ", " : "");
      str += element.toString();
    }
    return "[" + (data.isEmpty() ? "root" : str) + "]";
  }

  public void print() {
    Iterator<PowerSet> iterator = children.iterator();
    SystemOutWriter.singleton().println(this +" children " + children());
    while (iterator.hasNext()) {
      PowerSet item = iterator.next();
      item.print();
    }
  }

  public void printElemnets() {
    elements.print();
  }

  private String children() {
    String str = (children.isEmpty()) ? "empty" : "";
    Iterator<PowerSet> iterator = children.iterator();
    while (iterator.hasNext()) {
      PowerSet item = iterator.next();
      str += ( (!str.equals("")) ? ", " : "");
      str += item.toString();
    }
    return str;
  }
}

class Elements {

  Collection<Object> elements;

  Elements() {
    super();
    elements = new ArrayList<Object>();
  }

  public boolean add(Object element) {
    return elements.add(element);
  }

  public Collection<Object> right(Object element) {
    Iterator<Object> iterator = elements.iterator();
    Collection<Object> right = new ArrayList<Object>();
    boolean found = element == null; // if null add all items
    Object current = null;
    while (iterator.hasNext()) {
      current = iterator.next();
      if (found) { // if found add
        right.add(current);
      }
      else {
        found = current == element; // check if found
      }
    }
    return right;
  }

  public String toString() {
    return this.elements.size() + " " +
        this.elementsToString(this.elements.iterator());
  }

  private String elementsToString(Iterator<Object> iterator) {
    String str = "";
    while (iterator.hasNext()) {
      Object element = iterator.next();
      str += ( (!str.equals("")) ? ", " : "");
      str += element.toString();
    }
    return str;
  }

  void print() {
    SystemOutWriter.singleton().println("Elements: " + this);
  }

  Iterator<Object> iterator() {
    return elements.iterator();
  }
}
