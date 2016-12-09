package nl.tue.declare.appl.util.swing.languagetree;

import javax.swing.tree.*;

import nl.tue.declare.domain.template.*;

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
public class LanguageTreeNode
    extends DefaultMutableTreeNode {
  /**
	 * 
	 */
	private static final long serialVersionUID = 721594299928270331L;

public LanguageTreeNode(Object obj) {
    super(obj);
  }

  public boolean isLeaf() {
    return! (getUserObject() instanceof LanguageGroup);
  }
}
