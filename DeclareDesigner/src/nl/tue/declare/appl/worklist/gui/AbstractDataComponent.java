package nl.tue.declare.appl.worklist.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.execution.*;

abstract class AbstractDataComponent
    extends JPanel implements IDataComponenet {

	private static final long serialVersionUID = -1685768436890442292L;
protected WorkItemData data;
  protected JComponent component;

  public AbstractDataComponent(WorkItemData data) {
    super();
    this.data = data;
    this.component = ini();
    this.setLayout(new GridLayout());
    this.add(new JLabel(" " + data.getData().getDataElement().getName() + " "),
             BorderLayout.WEST);
    String type = data.getData().getType().pretty();
    String special = " ";
    Color color = this.getForeground();
    this.add(component, BorderLayout.CENTER);
    if (data.getData().getType() ==
        ActivityDataDefinition.Type.INPUT_OUTPUT ||
        data.getData().getType() ==
        ActivityDataDefinition.Type.OUTPUT) {
      special = "*";
      color = Color.RED;
    }
    JLabel label = new JLabel(" " + special + " " + type);
    label.setForeground(color);
    this.add(label, BorderLayout.SOUTH);
  }

  protected abstract boolean empty();

  protected abstract JComponent ini();

  protected abstract boolean getValue();

  /**
   *
   * @return boolean
   * @todo Implement this
   *   nl.tue.declare.appl.worklist.gui.data.IDataComponenet method
   */
  public boolean readValue() {
    boolean ok = !this.empty();
    if (!ok) {
      MessagePane.error(this,
                        "Data element \'" +
                        this.data.getData().getDataElement().getName() +
                        "\' is an output element. Please provide a value!");
    }
    else {
      ok = this.getValue();
    }
    return ok;
  }
}
