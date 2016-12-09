package nl.tue.declare.appl.util.swing.languagetree;

import java.awt.event.*;
import javax.swing.*;
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
public class LanguageTreePopupAdapter
    implements MouseListener {

  private LanguageTree tree = null;
  private ILanguageTreeListener listener = null;

  public LanguageTreePopupAdapter(LanguageTree t) {
    super();
    tree = t;
  }

  void setListener(ILanguageTreeListener l) {
    this.listener = l;
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
    if (e.isMetaDown()) {
      TreePath path = tree.getPathForLocation(e.getX(), e.getY());
      if (path != null) {
        Object node = path.getLastPathComponent();
        if (node != null) {
          if (node instanceof DefaultMutableTreeNode) {
            popup( (DefaultMutableTreeNode) node, e.getX(), e.getY());
          }
        }
      }
    }
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  private void popup(DefaultMutableTreeNode node, int x, int y) {
    final Object userObject = node.getUserObject();
    JPopupMenu menu = null;
    if (userObject instanceof Language) {
      menu = languagePopUp( (Language) userObject);
    }
    else {
      if (userObject instanceof LanguageGroup) {
        menu = groupPopUp( (LanguageGroup) userObject);
      }
      else {
        if (userObject instanceof ConstraintTemplate) {
          menu = templatePopUp( (ConstraintTemplate) userObject);
        }
      }

    }
    if (menu != null) {
      menu.show(tree, x, y);
    }
  }

  private JPopupMenu languagePopUp(final Language language) {
    JPopupMenu menu = new JPopupMenu();
    // create item for ADDING GROUP
    JMenuItem itemAddGroup = new JMenuItem("add group");
    itemAddGroup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addGroup(language);
      }
    });
    // create item for ADDING TEMPLATE
    /*JMenuItem itemAddTemplate = new JMenuItem("add template");
    itemAddTemplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addTemplate(language);
      }
    });*/
    
    menu.add(itemAddGroup);
    menu.add(addtemplatePopUp(language));
    return menu;
  }
  
  /*private JMenu addtemplatePopUp(final Language language) {
	    //JPopupMenu menu = new JPopupMenu();
	    // create item for ADDING TEMPLATE
	    JMenu itemAddTemplate = new JMenu("vvvv");
	    
	    JMenuItem itemAddTemplate1 = new JMenuItem("one parameter");
	    itemAddTemplate1.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,1);
	      }
	    });
	    
	    JMenuItem itemAddTemplate2 = new JMenuItem("two parameters");
	    itemAddTemplate2.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,2);
	      }
	    });
	    
	    JMenuItem itemAddTemplate3 = new JMenuItem("three parameters");
	    itemAddTemplate3.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,3);
	      }
	    });
	    
	    JMenuItem itemAddTemplate4 = new JMenuItem("more parameters ...");
	    itemAddTemplate4.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language);
	      }
	    });
	    
	    itemAddTemplate.add(itemAddTemplate1);
	    itemAddTemplate.add(itemAddTemplate2);
	    itemAddTemplate.add(itemAddTemplate3);
	    itemAddTemplate.add(itemAddTemplate4);
		return itemAddTemplate;
   }*/

  private JMenu addtemplatePopUp(final LanguageGroup language) {
	    //JPopupMenu menu = new JPopupMenu();
	    // create item for ADDING TEMPLATE
	    JMenu itemAddTemplate = new JMenu("add template");
	    
	    JMenuItem itemAddTemplate1 = new JMenuItem("one parameter");
	    itemAddTemplate1.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,1);
	      }
	    });
	    
	    JMenuItem itemAddTemplate2 = new JMenuItem("two parameters");
	    itemAddTemplate2.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,2);
	      }
	    });
	    
	    JMenuItem itemAddTemplate3 = new JMenuItem("three parameters");
	    itemAddTemplate3.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language,3);
	      }
	    });
	    
	    JMenuItem itemAddTemplate4 = new JMenuItem("more parameters ...");
	    itemAddTemplate4.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        addTemplate(language);
	      }
	    });
	    
	    itemAddTemplate.add(itemAddTemplate1);
	    itemAddTemplate.add(itemAddTemplate2);
	    itemAddTemplate.add(itemAddTemplate3);
	    itemAddTemplate.add(itemAddTemplate4);
		return itemAddTemplate;
 }
  
  private JPopupMenu groupPopUp(final LanguageGroup group) {
    JPopupMenu menu = new JPopupMenu();

    // create item for EIDT
    JMenuItem itemEdit = new JMenuItem("edit");
    itemEdit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editGroup(group);
      }
    });

    // create item for DELETE
    JMenuItem itemDelete = new JMenuItem("delete");
    itemDelete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (listener != null) {
          removeGroup(group);
        }
      }
    });

    // create item for ADDING GROUP
    JMenuItem itemAddGroup = new JMenuItem("add group");
    itemAddGroup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addGroup(group);
      }
    });

    // create item for ADDING TEMPLATE
    JMenuItem itemAddTemplate = new JMenuItem("add template");
    itemAddTemplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addTemplate(group);
      }
    });

    menu.add(itemEdit);
    menu.add(itemDelete);
    menu.addSeparator();
    menu.add(itemAddGroup);
    //menu.add(itemAddTemplate);
    menu.add(this.addtemplatePopUp(group));
    return menu;
  }

  private JPopupMenu templatePopUp(final ConstraintTemplate template) {
    JPopupMenu menu = new JPopupMenu();
    // create item for EDIT TEMPLATE
    JMenuItem itemEdit = new JMenuItem("edit");
    itemEdit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editTemplate(template);
      }
    });
    // create item for ADDING TEMPLATE
    JMenuItem itemDelete = new JMenuItem("delete");
    itemDelete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeTemplate(template);
      }
    });
    menu.add(itemEdit);
    menu.add(itemDelete);
    return menu;
  }

  private void addGroup(LanguageGroup parent) {
    if (listener != null) {
      LanguageGroup group = listener.addGroup(parent);
      if (group != null) {
        tree.addNode(parent, group);
        tree.select(group);
      }
    }
  }

  private void editGroup(LanguageGroup group) {
    if (listener != null) {
      listener.editGroup(group);
      tree.select(group);
    }
  }

  private void removeGroup(LanguageGroup group) {
    if (listener != null) {
      listener.deleteGroup(group);
      DefaultMutableTreeNode parent = tree.getParentNode(group);
      tree.removeNodeForObject(group);
      tree.reload();
      tree.select(parent.getUserObject());
    }
  }

  private void addTemplate(LanguageGroup parent, int nrParameters) {
    if (listener != null) {
      ConstraintTemplate template = listener.addTemplate(parent,nrParameters);
      if (template != null) {
        tree.addNode(parent, template);
        tree.select(template);
      }
    }
  }
  
  private void addTemplate(LanguageGroup parent) {
	    if (listener != null) {
	      int nrParameters = 1; 
	      ConstraintTemplate template = listener.addTemplate(parent,nrParameters);
	      if (template != null) {
	        tree.addNode(parent, template);
	        tree.select(template);
	      }
	    }
	  }

  private void editTemplate(ConstraintTemplate template) {
    if (listener != null) {
      listener.editTemplate(template);
      tree.select(template);
    }
  }

  private void removeTemplate(ConstraintTemplate template) {
    if (listener != null) {
      if (listener.deleteTemplate(template)) {
        DefaultMutableTreeNode parent = tree.getParentNode(template);
        tree.removeNodeForObject(template);
        tree.reload();
        tree.select(parent.getUserObject());
        tree.expand(parent.getUserObject());
      }
    }
  }

}
