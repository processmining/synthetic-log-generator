package nl.tue.declare.verification;

import java.util.*;

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
public class Violations
    extends ArrayList<ViolationGroup> {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5645380161232204495L;

public Violations(int initialCapacity) {
    super(initialCapacity);
  }

  public Violations() {
    super();
  }

  public Violations(Collection<ViolationGroup> c) {
    super(c);
  }

  void filter() {
    // sort on increasing group size
    for (int i = 0; i < size() - 1; i++) {
      ViolationGroup first = get(i);
      for (int j = i + 1; j < size(); j++) {
        ViolationGroup second = get(j);
        if (first.size() > second.size()) {
          set(i, second);
          set(j, first);
        }
      }
    }
    // find all supersets for each group
    Violations remove = new Violations();
    for (int i = 0; i < size() - 1; i++) {
      ViolationGroup first = get(i);
      for (int j = i + 1; j < size(); j++) {
        ViolationGroup second = get(j);
        if (second.isSuper(first)) {
          remove.add(second);
        }
      }
    }
    // remove all supersets
    removeAll(remove);
  }
}
