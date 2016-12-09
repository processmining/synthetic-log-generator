package nl.tue.declare.appl.util.swing.languagetree;

import java.awt.*;
import java.awt.dnd.*;
import javax.swing.tree.*;

import nl.tue.declare.domain.template.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LanguageTree
    extends DragAndDropTree {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1626160552125850930L;
private LanguageTreeTransferHandler handler;
  private LanguageTreePopupAdapter adapter;

  public LanguageTree() {
    super();
    adapter = new LanguageTreePopupAdapter(this);
    addMouseListener(adapter);
  }

  public void setListener(ILanguageTreeListener l) {
    adapter.setListener(l);
    handler.setListener(l);
  }

  protected AbstractTreeTransferHandler getTreeTransferHandler() {
    handler = new LanguageTreeTransferHandler(DnDConstants.ACTION_MOVE);
    return handler;
  }

  protected DefaultMutableTreeNode createNode(Object obj) {
    return new LanguageTreeNode(obj);
  }

  private class LanguageTreeTransferHandler
      extends
      DefaultTreeTransferHandler {

    private ILanguageTreeListener listener;

    public LanguageTreeTransferHandler(int action) {
      super(action);
    }

    public void setListener(ILanguageTreeListener l) {
      this.listener = l;
    }

    public boolean canPerformAction(DragAndDropTree target,
                                    DefaultMutableTreeNode draggedNode,
                                    int action, Point location) {
      if (!super.canPerformAction(target, draggedNode, action, location)) {
        return (false);
      }

      TreePath pathTarget = target.getPathForLocation(location.x,
          location.y);
      if (action == DnDConstants.ACTION_COPY ||
          action == DnDConstants.ACTION_MOVE) {
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)
            pathTarget.getLastPathComponent();
        if (parentNode.getUserObject() instanceof ConstraintTemplate) {
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

      boolean execute = super.executeDrop(target, draggedNode, newParentNode,
                                          action);
      if (execute && listener != null) {
        if (action == DnDConstants.ACTION_MOVE) {
          Object oldParent = ( (DefaultMutableTreeNode) draggedNode.getParent()).
              getUserObject();
          Object node = draggedNode.getUserObject();
          Object newParent = newParentNode.getUserObject();
          if (oldParent instanceof LanguageGroup && node instanceof IItem &&
              newParent instanceof LanguageGroup) {
            if (listener != null) {
              listener.move( (LanguageGroup) oldParent, (IItem) node,
                            (LanguageGroup) newParent);
            }
          }
          target.select(node);
        }
      }
      return execute;
    }
  }
}
