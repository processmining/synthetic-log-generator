package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import nl.tue.declare.appl.util.FrameUtil;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.util.swing.languagetree.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.template.*;

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
public class TemplatePanel
    extends TPanel implements TreeSelectionListener {

  /**
	 * 
	 */
  private static final long serialVersionUID = 5039465398827022813L;
  private LanguageTree templates = new LanguageTree();
  private JPanel preview = new TPanel(new BorderLayout(), "preview");
  private TTextArea description = new TTextArea();

  private ArrayList<Listener> listeners = new ArrayList<Listener>();
  

  public TemplatePanel(Language language) {
    super(new BorderLayout(), "templates");
    this.fillTemplates(language);
    FrameUtil.readOnly(description, this);
    JPanel descr = new TPanel(new BorderLayout(),"description");
    descr.add(description, BorderLayout.CENTER);
    
    JPanel right = new JPanel(new BorderLayout());
    right.add(descr, BorderLayout.NORTH);
    right.add(preview, BorderLayout.CENTER);
    TSplitPane pane = new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(templates),
                      new JScrollPane(right));
    pane.setResizeWeight(0.25);
    add(pane, BorderLayout.CENTER);
    templates.addTreeSelectionListener(this);
  }

  public void setListener(Listener l) {
    listeners.add(l);
  }

  /**
   *
   */
  private void templateSelected() {
    Object selected = getSelectedTemplate();
    preview.removeAll();
    description.setText("");
    ConstraintTemplate template = null;
    if (selected instanceof ConstraintTemplate) {
      template = (ConstraintTemplate) selected;      
      description.setText(template.getDescription());
      TemplateView view = new TemplateView(template);
      preview.add(view.createGraph());
    }
    for (Listener l: listeners) {
      l.itemSelected(selected);
    }
    preview.updateUI();
  }

  /**
   * getSelectedTemplate
   *
   * @return Object
   */
  public Object getSelectedTemplate() {
    Object selected = null;
    Object node = templates.getLastSelectedPathComponent();
    if (node instanceof DefaultMutableTreeNode) {
      selected = ( (DefaultMutableTreeNode) node).getUserObject();
    }
    return selected;
  }

  /**
   * fillTemplates
   *
   * @param language List
   */
  public void fillTemplates(Language language) {
    // set language as root
    templates.setRoot(language);
    // add all groups and templates of the language
    language.visitAll(new LanguageGroup.GroupVisitor() {
      public void visitChild(IItem parent, IItem child) {
        templates.addNode(parent, child);
      }
    });
    // expand language
    templates.expand(language);
  }

  public void selectTemplate(ConstraintTemplate template) {
    templates.select(template);
  }

  public interface Listener {
    void itemSelected(Object item);
  }

  public void valueChanged(TreeSelectionEvent e) {
    templateSelected();
  }
}
