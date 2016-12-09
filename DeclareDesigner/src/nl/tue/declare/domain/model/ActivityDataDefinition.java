package nl.tue.declare.domain.model;

import nl.tue.declare.domain.*;
import nl.tue.declare.utils.*;

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
public class ActivityDataDefinition
    extends Base {

  private DataElement data;
  private Type type;

  public ActivityDataDefinition(int aId, DataElement data) {
    super(aId);
    this.data = data;
    this.type = Type.INPUT_OUTPUT;
  }

  public ActivityDataDefinition(ActivityDataDefinition data) {
    super(data);
    this.data = data.getDataElement();
    this.type = data.getType();
  }

  public String toString() {
    return this.data.getName();
  }

  public Object clone() {
    ActivityDataDefinition clone = new ActivityDataDefinition(this.getId(),
        this.data);
    clone.type = this.type;
    return clone;
  }

  public Type getType() {
    return this.type;
  }


  public void setType(Type type) {
    this.type = type;
  }



  public DataElement getDataElement() {
    return this.data;
  }

  public void setDataElement(DataElement data) {
    this.data = data;
  }

  public boolean parse(String value) {
    boolean ok = true;
    if (type == Type.OUTPUT) {
      ok = value != null; // mandatory data element!
    }
    if (ok) {
      ok = data.parse(value);
    }
    return ok;
  }

  /**
   * toString
   */
  public void print() {
    SystemOutWriter.singleton().print(getId() + " " + this +" " + type);
  }

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
  public enum Type {
    INPUT(0, "input"),
    OUTPUT(1, "output"),
    INPUT_OUTPUT(2, "input/output");

    private final int code;
    private final String pretty;

    Type(int code, String pretty) {
      this.code = code;
      this.pretty = pretty;
    }

    public int code() {
      return code;
    }

    public String pretty() {
      return pretty;
    }

    public String toString() {
      return pretty;
    }

    public static Type getCode(int code) {
      Type[] values = Type.values();
      boolean found = false;
      int i = 0;
      Type type = null;
      while (!found && i < values.length) {
        type = values[i++];
        found = type.code() == code;
      }
      return found ? type : null;
    }
  }
}
