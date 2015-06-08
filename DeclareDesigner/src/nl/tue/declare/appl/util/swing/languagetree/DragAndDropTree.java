package nl.tue.declare.appl.util.swing.languagetree;

import java.util.*;

import java.awt.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.tree.*;


public class DragAndDropTree
    extends JTree {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8575864465102499432L;
Insets autoscrollInsets = new Insets(20, 20, 20, 20); // insets

  public DragAndDropTree() {
    super();
    setAutoscrolls(true);
    setRootVisible(true);
    setShowsRootHandles(false); //to show the root icon
    getSelectionModel().setSelectionMode(TreeSelectionModel.
                                         SINGLE_TREE_SELECTION); //set single selection for the Tree
    setEditable(false);
    // new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
    AbstractTreeTransferHandler transferHandler = getTreeTransferHandler();
    transferHandler.setUp(this);
  }

  public DragAndDropTree(DefaultMutableTreeNode root) {
    this();
    //setAutoscrolls(true);
    DefaultTreeModel treemodel = new DefaultTreeModel(root);
    setModel(treemodel);
    /* setRootVisible(true);
     setShowsRootHandles(false);//to show the root icon
     getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //set single selection for the Tree
     setEditable(false);
                    // new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
     AbstractTreeTransferHandler transferHandler = getTreeTransferHandler();
                    transferHandler.setUp(this);*/
  }

  protected AbstractTreeTransferHandler getTreeTransferHandler() {
    return new DefaultTreeTransferHandler(DnDConstants.ACTION_COPY_OR_MOVE);
  }

  public void setRoot(Object obj) {
    setModel(new DefaultTreeModel(createNode(obj)));
  }

  public void autoscroll(Point cursorLocation) {
    Insets insets = getAutoscrollInsets();
    Rectangle outer = getVisibleRect();
    Rectangle inner = new Rectangle(outer.x + insets.left, outer.y + insets.top,
                                    outer.width - (insets.left + insets.right),
                                    outer.height - (insets.top + insets.bottom));
    if (!inner.contains(cursorLocation)) {
      Rectangle scrollRect = new Rectangle(cursorLocation.x - insets.left,
                                           cursorLocation.y - insets.top,
                                           insets.left + insets.right,
                                           insets.top + insets.bottom);
      scrollRectToVisible(scrollRect);
    }
  }

  public Insets getAutoscrollInsets() {
    return (autoscrollInsets);
  }

  public DefaultMutableTreeNode makeDeepCopy(DefaultMutableTreeNode node) {

    DefaultMutableTreeNode copy = createNode(node.getUserObject());
    for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
      Object element = e.nextElement();
      if (e instanceof DefaultMutableTreeNode)
      copy.add(makeDeepCopy( (DefaultMutableTreeNode) element));
    }
    return (copy);
  }

  protected DefaultMutableTreeNode createNode(Object obj) {
    return new DefaultMutableTreeNode(obj);
  }

  public void addNode(Object parent, Object child) {
    DefaultMutableTreeNode node = this.getNode(parent);
    ( (DefaultTreeModel) getModel()).insertNodeInto(createNode(child), node,
        node.getChildCount());
  }

  void addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
    ( (DefaultTreeModel) getModel()).insertNodeInto(child, parent,
        parent.getChildCount());
  }

  public void removeNode(DefaultMutableTreeNode node) {
    node.removeFromParent();
  }

  public void removeNodeForObject(Object obj) {
    removeNode(getNode(obj));
  }

  public void reload() {
    ( (DefaultTreeModel) getModel()).reload();
  }

  void select(DefaultMutableTreeNode node) {
    TreePath treePath = new TreePath(node.getPath());
    scrollPathToVisible(treePath);
    setSelectionPath(treePath);
  }

  public void select(Object obj) {
    DefaultMutableTreeNode node = this.getNode(obj);
    if (node != null) {
      select(node);
    }
  }

  private DefaultMutableTreeNode getNode(Object object) {
    return getNode( (DefaultMutableTreeNode) getModel().getRoot(), object);
  }

  public DefaultMutableTreeNode getParentNode(Object object) {
    return (DefaultMutableTreeNode) getNode( (DefaultMutableTreeNode) getModel().
                                            getRoot(), object).getParent();
  }

  private DefaultMutableTreeNode getNode(DefaultMutableTreeNode node,
                                         Object object) {
    if (node.getUserObject() == object) {
      return node;
    }

    DefaultMutableTreeNode temp = null;

    int i = 0;
    while ( (i < node.getChildCount()) && (temp == null)) {
      TreeNode current = node.getChildAt(i++);
      if (current instanceof DefaultMutableTreeNode) {
        DefaultMutableTreeNode myNode = (DefaultMutableTreeNode) current;
        temp = getNode(myNode, object);
      }
    }
    return temp;
  }

  public void expand(Object obj) {
    DefaultMutableTreeNode node = getNode(obj);
    if (node != null) {
      expandPath(new TreePath(node.getPath()));
    }
  }
}
