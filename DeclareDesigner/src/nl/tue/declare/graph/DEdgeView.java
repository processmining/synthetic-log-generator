package nl.tue.declare.graph;

import java.awt.event.*;

import org.jgraph.graph.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DEdgeView
    extends EdgeView {

  /** Drawing attributes that are created on the fly */

  /**
	 * 
	 */
	private static final long serialVersionUID = -5599481405889850931L;

/**
   * Constructs an edge view for the specified model object.
   *
   * @param cell
   *            reference to the model object
   */
  public DEdgeView(Object cell) {
    super(cell);
    renderer = new DEdgeRenderer();

  }

  /**
   * Returns a cell handle for the view.
   *
   * @param context GraphContext
   * @return CellHandle
   */
  public CellHandle getHandle(GraphContext context) {
    return new DEdgeHandle(this, context);
  }

  //
  // Custom Edge Handle
  //

  // Defines a EdgeHandle that uses the Shift-Button (Instead of the Right
  // Mouse Button, which is Default) to add/remove point to/from an edge.
  public static class DEdgeHandle
      extends EdgeView.EdgeHandle {

    /**
	 * 
	 */
	private static final long serialVersionUID = -395505643022569913L;

	/**
     *
     * @param edge EdgeView
     * @param ctx GraphContext
     */
    public DEdgeHandle(EdgeView edge, GraphContext ctx) {
      super(edge, ctx);
    }

    // Override Superclass Method
    public boolean isAddPointEvent(MouseEvent event) {
      // Points are Added using Shift-Click
      return event.isShiftDown();
    }

    // Override Superclass Method
    public boolean isRemovePointEvent(MouseEvent event) {
      // Points are Removed using Shift-Click
      return event.isShiftDown();
    }

  }

}
