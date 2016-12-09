package nl.tue.declare.appl.worklist;

import javax.swing.*;

import nl.tue.declare.appl.worklist.gui.BooleanDataComponent;
import nl.tue.declare.appl.worklist.gui.DoubleDataComponent;
import nl.tue.declare.appl.worklist.gui.IntegerDataComponenet;
import nl.tue.declare.appl.worklist.gui.StringDataComponent;
import nl.tue.declare.execution.*;

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
public class DataComponentFactory {

  public static JComponent createDataComponent(WorkItemData data) {
    JComponent result = null;
    switch (data.getData().getDataElement().getType()) {
      case STRING:
        result = new StringDataComponent(data);
        break;
      case INTEGER:
        result = new IntegerDataComponenet(data);
        break;
      case DOUBLE:
        result = new DoubleDataComponent(data);
        break;
      case BOOLEAN:
        result = new BooleanDataComponent(data);
        break;
    }
    return result;
  }
}
