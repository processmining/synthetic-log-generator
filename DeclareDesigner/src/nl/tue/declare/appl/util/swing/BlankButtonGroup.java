package nl.tue.declare.appl.util.swing;

import javax.swing.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: Extends the ButtonGroup in such a way, that it is possible
 * to have none of the buttons in the group selected.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
public class BlankButtonGroup
    extends ButtonGroup {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3683013639927198483L;
AbstractButton blank;

  public BlankButtonGroup() {
    super();
    blank = new JButton();
    blank.setVisible(false);
    blank.setEnabled(false);
    add(blank);
    setSelected(blank.getModel(), true);
  }

  /**
   * Sets the selected value for the <code>ButtonModel</code>.
   * Only one button in the group may be selected at a time.
   * If a button is unselected, then the hidden button 'blank' becomes selected.
   * @param m the <code>ButtonModel</code>
   * @param b <code>true</code> if this button is to be
   *   selected, otherwise <code>false</code>
   */
  public void setSelected(ButtonModel m, boolean b) {
    if (b && m != blank.getModel()) {
      super.setSelected(m, b);
    }
    else {
      super.setSelected(blank.getModel(), true);
    }
  }
}
