package nl.tue.declare.appl.design.model.gui;

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
import java.awt.*;

public class WorkPanel
    extends ModelPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -7154269385081283885L;
GraphPanel graph;

  public WorkPanel() {
    super();
    graph = new GraphPanel();
    createGUI();
  }

  /**
   * createGUI
   */
  public void createGUI() {
    this.setLayout(new BorderLayout(2, 2));
    this.add(graph, BorderLayout.CENTER);
  }

  /**
   * preview
   */
  public void preview(Component com) {
    graph.preview(com);
  }
}
