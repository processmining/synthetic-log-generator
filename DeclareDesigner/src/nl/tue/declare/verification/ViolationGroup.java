package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.domain.model.*;

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
public class ViolationGroup
    extends ArrayList<ConstraintDefinition> {
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 6817686005909721694L;

public ViolationGroup(int initialCapacity) {
    super(initialCapacity);
  }

  public ViolationGroup() {
    super();
  }

  public ViolationGroup(Collection<ConstraintDefinition> c) {
    super(c);
  }

  public boolean isSuper(ViolationGroup group) {
    return ( (this != group) && this.containsAll(group));
  }

  public boolean isSub(ViolationGroup group) {
    return ( (this != group) && group.containsAll(this));
  }

  public String toString() {
    String result = "";
    Iterator<ConstraintDefinition> i = iterator();
    while (i.hasNext()) {
      ConstraintDefinition constraint = i.next();
      if (!result.equals("")) {
        result += ", ";
      }
      result += constraint.getName();
    }
    return "[" + result + "]";
  }
}
