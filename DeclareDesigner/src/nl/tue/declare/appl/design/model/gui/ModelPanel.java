package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
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

public class ModelPanel
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1944636478841722410L;

public ModelPanel(LayoutManager layout) {
    super(layout);
    setBorder(BorderFactory.createTitledBorder(""));
  }

  public ModelPanel() {
    super(null);
    setBorder(BorderFactory.createTitledBorder(""));
  }

  public ModelPanel(String title) {
    super(null);
    setBorder(BorderFactory.createTitledBorder(""));
  }
}
