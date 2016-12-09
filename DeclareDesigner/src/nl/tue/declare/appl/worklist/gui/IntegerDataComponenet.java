package nl.tue.declare.appl.worklist.gui;

import javax.swing.*;

import nl.tue.declare.execution.*;

public class IntegerDataComponenet
    extends AbstractDataComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = -8094255303533477565L;

public IntegerDataComponenet(WorkItemData data) {
    super(data);
  }

  protected JComponent ini() {
    JTextField field = new JTextField();
    field.setText(data.getValue().getValue());
    return field;
  }

  public boolean getValue() {
    return data.setValue( ( (JTextField) component).getText());
  }

  protected boolean empty() {
    return ( ( (JTextField) component).getText() == null);
  }
}
