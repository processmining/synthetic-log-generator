package nl.tue.declare.appl.worklist.gui;

import javax.swing.*;

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
public class DoubleDataComponent
    extends AbstractDataComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = 5023107031422315868L;

public DoubleDataComponent(WorkItemData data) {
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
    JTextField text = (JTextField) component;
    boolean empty = true;
    if (text.getText() != null) {
      empty = (!text.getText().equals(""));
    }
    return empty;
  }

}
