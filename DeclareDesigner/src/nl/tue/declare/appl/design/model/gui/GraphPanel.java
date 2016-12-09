package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
import javax.swing.*;

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
public class GraphPanel
    extends DefaultPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 7893072832574958863L;
GraphPreview preview;
  JScrollPane scrollPane;
  private GraphPanel(String title) {
    super(null);
  }

  /**
   * buildInterface
   *
   * @todo Implement this nl.tue.declare.appl.util.swing.Panel method
   */
  protected void buildInterface() {
    this.setLayout(new BorderLayout(2, 2));
    preview = new GraphPreview();
    scrollPane = new JScrollPane(preview,
                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(preview, BorderLayout.CENTER);
  }

  /**
   * GraphPanel
   */
  public GraphPanel() {
    this("");

  }

  /**
   * preview
   */
  public void preview(Component com) {
    preview.preview(com);
  }
}
