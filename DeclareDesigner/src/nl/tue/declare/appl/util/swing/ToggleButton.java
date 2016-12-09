package nl.tue.declare.appl.util.swing;

import javax.swing.*;

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
public class ToggleButton
    extends JToggleButton {
  /**
	 * 
	 */
	private static final long serialVersionUID = 4756065398141173079L;

public ToggleButton() {
    super();
  }

  public ToggleButton(Icon icon) {
    super(icon);
  }

  public ToggleButton(String text) {
    super(text);
  }

  public ToggleButton(Action a) {
    super(a);
  }

  public ToggleButton(String text, Icon icon) {
    super(text, icon);
  }
}
