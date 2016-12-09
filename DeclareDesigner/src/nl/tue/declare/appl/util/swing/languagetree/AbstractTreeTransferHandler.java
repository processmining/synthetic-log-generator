package nl.tue.declare.appl.util.swing.languagetree;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.tree.*;


public abstract class AbstractTreeTransferHandler
    implements DragGestureListener, DragSourceListener, DropTargetListener {

  private DragAndDropTree tree;
  private DragSource dragSource; // dragsource
  //private DropTarget dropTarget; //droptarget
  private static DefaultMutableTreeNode draggedNode;
  private DefaultMutableTreeNode draggedNodeParent;
  private static BufferedImage image = null; //buff image
  private Rectangle rect2D = new Rectangle();
  private boolean drawImage;
  private int action;

  protected AbstractTreeTransferHandler( /*DNDTree tree, */int action,
      boolean drawIcon) {
    super();
    this.tree = null;
    drawImage = drawIcon;
    dragSource = new DragSource();
    this.action = action;
    //dragSource.createDefaultDragGestureRecognizer(tree, action, this);
    //dropTarget = null; //new DropTarget(tree, action, this);
  }

  void setUp(DragAndDropTree tree) {
    this.tree = tree;
    dragSource.createDefaultDragGestureRecognizer(tree, action, this);
    //dropTarget = new DropTarget(tree, action, this);
  }

  /* Methods for DragSourceListener */
  public void dragDropEnd(DragSourceDropEvent dsde) {
    if (dsde.getDropSuccess() &&
        dsde.getDropAction() == DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
      ( (DefaultTreeModel) tree.getModel()).nodeStructureChanged(
          draggedNodeParent);
    }
  }

  public final void dragEnter(DragSourceDragEvent dsde) {
    int action = dsde.getDropAction();
    if (action == DnDConstants.ACTION_COPY) {
      dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
    }
    else {
      if (action == DnDConstants.ACTION_MOVE) {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
      }
      else {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
      }
    }
  }

  public final void dragOver(DragSourceDragEvent dsde) {
    int action = dsde.getDropAction();
    if (action == DnDConstants.ACTION_COPY) {
      dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
    }
    else {
      if (action == DnDConstants.ACTION_MOVE) {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
      }
      else {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
      }
    }
  }

  public final void dropActionChanged(DragSourceDragEvent dsde) {
    int action = dsde.getDropAction();
    if (action == DnDConstants.ACTION_COPY) {
      dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
    }
    else {
      if (action == DnDConstants.ACTION_MOVE) {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
      }
      else {
        dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
      }
    }
  }

  public final void dragExit(DragSourceEvent dse) {
    dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
  }

  /* Methods for DragGestureListener */
  public final void dragGestureRecognized(DragGestureEvent dge) {
    TreePath path = tree.getSelectionPath();
    if (path != null) {
      draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
      draggedNodeParent = (DefaultMutableTreeNode) draggedNode.getParent();
      if (drawImage) {
        Rectangle pathBounds = tree.getPathBounds(path); //getpathbounds of selectionpath
        JComponent lbl = (JComponent) tree.getCellRenderer().
            getTreeCellRendererComponent(tree, draggedNode, false,
                                         tree.isExpanded(path),
                                         ( (DefaultTreeModel) tree.getModel()).
                                         isLeaf(path.getLastPathComponent()), 0, false); //returning the label
        lbl.setBounds(pathBounds); //setting bounds to lbl
        image = new BufferedImage(lbl.getWidth(), lbl.getHeight(),
                                  java.awt.image.BufferedImage.
                                  TYPE_INT_ARGB_PRE); //buffered image reference passing the label's ht and width
        Graphics2D graphics = image.createGraphics(); //creating the graphics for buffered image
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.
            SRC_OVER, 0.5f)); //Sets the Composite for the Graphics2D context
        lbl.setOpaque(false);
        lbl.paint(graphics); //painting the graphics to label
        graphics.dispose();
      }
      dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image,
                           new Point(0, 0), new TransferableNode(draggedNode), this);
    }
  }

  /* Methods for DropTargetListener */

  public final void dragEnter(DropTargetDragEvent dtde) {
    Point pt = dtde.getLocation();
    int action = dtde.getDropAction();
    if (drawImage) {
      paintImage(pt);
    }
    if (canPerformAction(tree, draggedNode, action, pt)) {
      dtde.acceptDrag(action);
    }
    else {
      dtde.rejectDrag();
    }
  }

  public final void dragExit(DropTargetEvent dte) {
    if (drawImage) {
      clearImage();
    }
  }

  public final void dragOver(DropTargetDragEvent dtde) {
    Point pt = dtde.getLocation();
    int action = dtde.getDropAction();
    tree.autoscroll(pt);
    if (drawImage) {
      paintImage(pt);
    }
    if (canPerformAction(tree, draggedNode, action, pt)) {
      dtde.acceptDrag(action);
    }
    else {
      dtde.rejectDrag();
    }
  }

  public final void dropActionChanged(DropTargetDragEvent dtde) {
    Point pt = dtde.getLocation();
    int action = dtde.getDropAction();
    if (drawImage) {
      paintImage(pt);
    }
    if (canPerformAction(tree, draggedNode, action, pt)) {
      dtde.acceptDrag(action);
    }
    else {
      dtde.rejectDrag();
    }
  }

  public final void drop(DropTargetDropEvent dtde) {
    try {
      if (drawImage) {
        clearImage();
      }
      int action = dtde.getDropAction();
      Transferable transferable = dtde.getTransferable();
      Point pt = dtde.getLocation();
      if (transferable.isDataFlavorSupported(TransferableNode.NODE_FLAVOR) &&
          canPerformAction(tree, draggedNode, action, pt)) {
        TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) transferable.
            getTransferData(TransferableNode.NODE_FLAVOR);
        DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)
            pathTarget.getLastPathComponent();
        if (executeDrop(tree, node, newParentNode, action)) {
          dtde.acceptDrop(action);
          dtde.dropComplete(true);
          return;
        }
      }
      dtde.rejectDrop();
      dtde.dropComplete(false);
    }
    catch (Exception e) {
      dtde.rejectDrop();
      dtde.dropComplete(false);
    }
  }

  private final void paintImage(Point pt) {
    tree.paintImmediately(rect2D.getBounds());
    rect2D.setRect( (int) pt.getX(), (int) pt.getY(), image.getWidth(),
                   image.getHeight());
    tree.getGraphics().drawImage(image, (int) pt.getX(), (int) pt.getY(), tree);
  }

  private final void clearImage() {
    tree.paintImmediately(rect2D.getBounds());
  }

  public abstract boolean canPerformAction(DragAndDropTree target,
                                           DefaultMutableTreeNode draggedNode,
                                           int action, Point location);

  public abstract boolean executeDrop(DragAndDropTree tree,
                                      DefaultMutableTreeNode draggedNode,
                                      DefaultMutableTreeNode newParentNode,
                                      int action);
}
