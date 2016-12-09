package nl.tue.declare.appl.design.model.gui;

import java.awt.*;

import nl.tue.declare.appl.util.swing.*;

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
public class GraphPreview
    extends DefaultPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 9117206822999250770L;

private GraphPreview(String title) {
    super(null);
  }

  /**
   * buildInterface
   *
   * @todo Implement this nl.tue.declare.appl.util.swing.Panel method
   */
  protected void buildInterface() {
    setLayout(new BorderLayout(2, 2));
  }

  /**
   * GraphPreview
   */
  public GraphPreview() {
    this("");
  }

  /**
   *
   *
   * @param preview Panel
   */
  public void preview(Component com) {
    removeAll();
    add(com, BorderLayout.CENTER);
    updateUI();
  }
}
