package nl.tue.declare.domain.template;

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

import nl.tue.declare.domain.*;

public class LanguageGroup
    extends Base implements IItem {

  private List<IItem> children;
  private String name;

  public LanguageGroup(int id) {
    super(id);
    children = new ArrayList<IItem> ();
    setName("");
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public boolean add(IItem child) {
    return children.add(child);
  }

  public boolean remove(IItem child) {
    boolean removed = false;
    if (children.contains(child)) {
      removed = children.remove(child);
    }
    Iterator<IItem> it = children.iterator();
    while (it.hasNext() && !removed) {
      IItem current = it.next();
      if (current instanceof LanguageGroup) {
        removed = ( (LanguageGroup) current).remove(child);
      }
    }
    return removed;
  }

  public List<IItem> getChildren() {
    return children;
  }

  public boolean exists(IItem item) {
    boolean found = children.contains(item);
    if (!found) {
      Iterator<IItem> it = children.iterator();
      while (it.hasNext() && !found) {
        IItem current = it.next();
        found = current.exists(item);
      }
    }
    return found;
  }

  /**
   * nextTemplateId
   *
   * @return int
   */
  public int getMaxId() {
    int id = getId();
    Iterator<IItem> it = children.iterator();
    while (it.hasNext()) {
      IItem item = it.next();
      if (id < item.getMaxId()) {
        id = item.getMaxId();
      }
    }
    return id + 1;
  }

  public IItem withId(int id) {
    IItem wanted = null;
    Iterator<IItem> iterator = children.iterator();
    while (iterator.hasNext() && wanted == null) {
      IItem item = iterator.next();
      wanted = item.withId(id);
    }
    return wanted;
  }

  public LanguageGroup getParent(IItem group) {
    LanguageGroup parent = null;
    if (children.contains(group)) {
      parent = this;
    }
    Iterator<IItem> iterator = children.iterator();
    while (iterator.hasNext() && parent == null) {
      IItem item = iterator.next();
      if (item instanceof LanguageGroup) {
        parent = ( (LanguageGroup) item).getParent(group);
      }
    }
    return parent;
  }

  public void visitAll(GroupVisitor v) {
    Iterator<IItem> i = children.iterator();
    while (i.hasNext()) {
      IItem child = i.next();
      v.visitChild(this, child);
      if (child instanceof LanguageGroup) {
        ( (LanguageGroup) child).visitAll(v);
      }
    }
  }

  public interface GroupVisitor {
    public void visitChild(IItem parent, IItem child);
  }

}
