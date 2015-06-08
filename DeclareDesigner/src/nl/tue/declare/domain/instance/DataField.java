package nl.tue.declare.domain.instance;

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
import nl.tue.declare.domain.model.*;
import nl.tue.declare.utils.*;

public class DataField
    extends DataElement {

  private DataValue value;

  public DataField(DataElement element) {
    super(element);
    this.value = new DataValue(this.getType());
    this.init();
  }

  public DataField(DataField field) {
    super(field);
    this.value = new DataValue(this.getType(), field.getValue());
    this.init();
  }

  private DataField(DataElement element, DataValue value) {
    super(element);
    this.value = value;
    this.init();
  }

  private void init() {
    String ini = this.getType().getIni();
    if (this.getInitial() != null) {
      if (this.getInitial() != "") {
        ini = this.getInitial();
      }
    }
    this.push(ini);
  }

  public Object clone() {
    return new DataField(this, (DataValue)this.value.clone());
  }

  public DataValue pop() {
    return value;
  }

  public boolean push(String value) {
    boolean parse = this.getType().parse(value);
    if (parse) {
      this.value.setValue(value);
    }
    return parse;
  }

  public String getValue() {
    return value.getValue();
  }

  public String toString() {
    return super.toString();
  }

  public void print() {
    SystemOutWriter.singleton().println(getId() + " " + getName() + "=" +
                                        this.value.getValue());
  }
}
