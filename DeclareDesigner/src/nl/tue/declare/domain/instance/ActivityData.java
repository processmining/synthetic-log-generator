package nl.tue.declare.domain.instance;

import nl.tue.declare.domain.model.*;

public class ActivityData
    extends ActivityDataDefinition {

  /**
   *
   * @param data ActivityDataDefinition
   */
  public ActivityData(ActivityDataDefinition data) {
    super(data);
  }

  public ActivityData(int aId, DataElement data) {
    super(aId, data);
  }

  /**
   *
   * @return DataField
   */
  private DataField getField() {
    DataField field = null;
    DataElement element = super.getDataElement();
    if (element != null) {
      if (element instanceof DataField) {
        field = (DataField) element;
      }
    }
    return field;
  }

  /**
   *
   * @return DataValue
   */
  public DataValue read() {
    DataValue value = null;
    DataField field = this.getField();
    if (getType() == ActivityDataDefinition.Type.OUTPUT) {
      value = new DataValue(field.getType()); // for output data do not read, i.e., set empty value
    }
    else
    if (field != null) {
      value = field.pop(); // for not-outout data read data from data field
    }
    return value;
  }

  /**
   *
   * @param value DataValue
   */
  public boolean write(DataValue value) {
    DataField field = this.getField();
    boolean parse = false;
    if (getType() != ActivityDataDefinition.Type.INPUT) { // only write for non-input data
      if (field != null) {
        parse = field.push(value.getValue());
      }
    }
    return parse;
  }

}
