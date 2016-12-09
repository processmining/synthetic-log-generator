package nl.tue.declare.appl.worklist.gui;

import javax.swing.*;

import nl.tue.declare.execution.*;

public class BooleanDataComponent
    extends AbstractDataComponent {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2136521720928896657L;
JCheckBox btn = new JCheckBox();

  public BooleanDataComponent(WorkItemData data) {
    super(data);
  }

  protected JComponent ini() {
    btn.setSelected(data.getData().getDataElement().getType().bool(data.
        getValue().getValue()));
    return btn;
  }

  public boolean getValue() {
    return data.setValue(Boolean.toString( ( (JCheckBox) component).isSelected()));
  }

  protected boolean empty() {
    return true;
  }

}
