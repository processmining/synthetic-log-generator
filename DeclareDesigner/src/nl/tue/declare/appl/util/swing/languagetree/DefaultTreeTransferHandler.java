package nl.tue.declare.appl.util.swing.languagetree;

import java.awt.*;
import java.awt.dnd.*;
import javax.swing.tree.*;


public class DefaultTreeTransferHandler
    extends AbstractTreeTransferHandler {

  public DefaultTreeTransferHandler( /*DNDTree tree,*/int action) {
    super( /*tree,*/action, true);
  }

  public boolean canPerformAction(DragAndDropTree target,
                                  DefaultMutableTreeNode draggedNode,
                                  int action, Point location) {
    TreePath pathTarget = target.getPathForLocation(location.x, location.y);
    if (pathTarget == null) {
      target.setSelectionPath(null);
      return (false);
    }
    target.setSelectionPath(pathTarget);
    if (action == DnDConstants.ACTION_COPY) {
      return (true);
    }
    else
    if (action == DnDConstants.ACTION_MOVE) {
      DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) pathTarget.
          getLastPathComponent();
      if (draggedNode.isRoot() || parentNode == draggedNode.getParent() ||
          draggedNode.isNodeDescendant(parentNode)) {
        return (false);
      }
      else {
        return (true);
      }
    }
    else {
      return (false);
    }
  }

  public boolean executeDrop(DragAndDropTree target,
                             DefaultMutableTreeNode draggedNode,
                             DefaultMutableTreeNode newParentNode, int action) {
    if (action == DnDConstants.ACTION_COPY) {
      DefaultMutableTreeNode newNode = target.makeDeepCopy(draggedNode);
      target.addNode(newParentNode, newNode);
      target.select(newNode.getUserObject());
      return (true);
    }
    if (action == DnDConstants.ACTION_MOVE) {
      target.removeNode(draggedNode);
      target.addNode(newParentNode, draggedNode);
      target.select(draggedNode.getUserObject());
      return (true);
    }
    return (false);
  }
}
