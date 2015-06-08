package nl.tue.declare.domain.template;

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
public class ConstraintWarningLevel {

  private static final int MIN = 1; // minimal level allowed
  private static final int MAX = 10; // maximal level allowed
  private int level;

  public ConstraintWarningLevel() {
    super();
    this.setLevel(MIN);
  }

  private boolean verify(int level) {
    return (MIN <= level && level <= MAX);
  }

  public boolean setLevel(int level) {
    boolean result = this.verify(level);
    if (result) {
      this.level = level;
    }
    return result;
  }

  public int getLevel() {
    return level;
  }

  public static Integer[] possible() {
    Collection<Integer> levels = new ArrayList<Integer>();
    for (int i = MIN; i <= MAX; i++) {
      levels.add(new Integer(i));
    }

    Integer[] array = new Integer[levels.size()];
    Iterator<Integer> iterator = levels.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      Object o = iterator.next();
      if (o instanceof Integer) {
        array[i++] = (Integer) o;
      }
    }
    return array;
  }
}
