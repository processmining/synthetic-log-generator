package nl.tue.declare.execution;

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
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.utils.*;

public class WorkItemData {

  ActivityData data;
  DataValue value;

  /**
   *
   * @param data ActivityData
   */
  public WorkItemData(ActivityData data) {
    super();
    this.data = data;
    this.value = (DataValue) data.read().clone();
    this.read();
  }

  /**
   * Read the data from the actvity data. This can be as rewrite, fifo, lifo,...
   */
  private void read() {
    this.value.setValue(data.read().getValue());
  }

  /**
   * Write back my value to the activity data.
   */
  public void write() {
    this.data.write(this.value);
  }

  public boolean setValue(String v) {
    boolean ok = false;
    if (data != null) {
      ok = data.parse(v);
    }
    if (ok) {
      this.value.setValue(v);
    }
    return ok;
  }

  /**
   *
   * @return DataValue
   */
  public DataValue getValue() {
    return this.value;
  }

  public boolean forData(ActivityData data) {
    return this.data == data;
  }

  public ActivityData getData() {
    return data;
  }

  public void print() {
    data.print();
    SystemOutWriter.singleton().println(" value=" + value.getValue());
  }

  void copuValue(WorkItemData other) {
    if (other != null) {
      this.value.setValue(other.value.getValue());
    }
  }

}
