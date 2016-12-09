package nl.tue.declare.appl.worklist.gui;

import javax.swing.*;

import nl.tue.declare.execution.*;

public class StringDataComponent
    extends AbstractDataComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = -599388413396744401L;

public StringDataComponent(WorkItemData data) {
    super(data);
  }

  protected JComponent ini() {
    JTextField component = new JTextField();
    component.setText(data.getValue().getValue());
    return component;
  }

  public boolean getValue() {
    return data.setValue( ( (JTextField) component).getText());
  }

  protected boolean empty() {
    JTextField text = (JTextField) component;
    boolean empty = true;
    if (text.getText() != null) {
      empty = text.getText().equals("");
    }
    return empty;
  }

}
