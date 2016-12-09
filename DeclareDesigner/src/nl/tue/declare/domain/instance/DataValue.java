package nl.tue.declare.domain.instance;

import nl.tue.declare.domain.model.*;

public class DataValue {

  DataElement.Type type;
  private String value;

  public DataValue(DataElement.Type type, String value) {
    super();
    this.type = type;
    this.setValue(value);
  }

  public DataValue(DataElement.Type type) {
    super();
    this.type = type;
    this.setValue(type.emptyValue());
  }

  public void setValue(String value) {
    if (value == null) {
      this.value = type.getIni();
    }
    else {
      this.value = value;
    }
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return value;
  }

  public Object clone() {
    return new DataValue(this.type, new String(this.value));
  }
}
