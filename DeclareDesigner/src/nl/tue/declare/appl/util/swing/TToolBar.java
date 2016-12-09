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
public class TToolBar
    extends JToolBar implements TComponent {
  /**
	 * 
	 */
	private static final long serialVersionUID = -3827989722957058390L;

public TToolBar() {
    super();
    buildInterface();
  }

  public TToolBar(int orientation) {
    super(orientation);
    buildInterface();
  }

  public TToolBar(String name) {
    super(name);
    buildInterface();
  }

  public TToolBar(String name, int orientation) {
    super(name, orientation);
    buildInterface();
  }

  public void buildInterface() {
    setBorder(BorderFactory.createTitledBorder(""));
  }
}
