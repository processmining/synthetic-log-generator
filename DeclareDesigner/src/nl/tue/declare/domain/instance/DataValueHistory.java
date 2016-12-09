package nl.tue.declare.domain.instance;

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
import java.util.*;

public class DataValueHistory {

  private ArrayList<DataValue> values;

  public DataValueHistory() {
    super();
    values = new ArrayList<DataValue>();
  }

  public void add(DataValue value) {
    this.values.add(value);
  }

  public DataValue get(int i) {
    DataValue value = null;
    if (i < values.size()) {
      value = values.get(i);
    }
    return value;
  }

  public int size() {
    return values.size();
  }

  DataValue last() {
    return get(size() - 1);
  }

  public Object clone() {
    DataValueHistory clone = new DataValueHistory();
    clone.values.addAll(values);
    return clone;
  }
}
