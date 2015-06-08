package nl.tue.declare.appl.design.template.gui;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import nl.tue.declare.appl.design.template.ConstraintsTemplateCoordinator;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.util.swing.languagetree.*;
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
public class FrmLanguages
    extends DesignInternalFrame implements TreeSelectionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2632531363044572386L;

static final String TITLE = "Constraint templates";

  private ConstraintsTemplateCoordinator templateCoordinator;

  private static FrmLanguages single = null;

  private JTabbedPane tabbed = new JTabbedPane();

  private JComboBox languages = new JComboBox(new DefaultComboBoxModel());
  private JButton jButtonAddLangauge = new ButtonAdd();
  private JButton jButtonDeleteLangauge = new ButtonDelete();

  private LanguageTree templates = new LanguageTree();

  private JButton jButtonExport = new JButton("export to ProM");

  private JPanel preview;

  private ConstraintGroupPanel groups = new ConstraintGroupPanel();

  protected FrmLanguages(String title) {
    super(title);
  }

  private FrmLanguages(ConstraintsTemplateCoordinator
                                       anAssignmentCoordinator) {
    super(TITLE);
    this.templateCoordinator = anAssignmentCoordinator;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static FrmLanguages singleton(
      ConstraintsTemplateCoordinator
      anAssignmentCoordinator) {
    if (single == null) {
      single = new FrmLanguages(anAssignmentCoordinator);
    }
    return single;
  }

  protected void jbInit() throws Exception {
    JPanel templates = new TPanel(new BorderLayout(2, 2));

    prepareTemplatesPanel(templates);
    this.preview = new TPanel(new BorderLayout(), "preview");
    JPanel main = new JPanel(new BorderLayout(2, 2));
    main.add(new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, templates,
                            this.preview), BorderLayout.CENTER);

    this.tabbed.add("templates", main);
    this.tabbed.add("constraint groups", groups);
    this.setContentPane(this.tabbed);
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e ActionEvent
   * @todo Implement this java.awt.event.ActionListener method
   */
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == jButtonExport) {
      templateCoordinator.export();
      return;
    }
    ;
    if (source == languages) {
      templateCoordinator.langaugeChanged();
      return;
    }
    if (source == jButtonAddLangauge) {
      templateCoordinator.addLanguage();
      return;
    }
    ;
    if (source == jButtonDeleteLangauge) {
      templateCoordinator.deleteLanguage();
      return;
    }
    ;
  }

  /**
   *
   * @param e MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    Object source = e.getSource();
    if (source == this.templates) {
      templateCoordinator.templateSelected();
      return;
    }
  }

  /**
   * prepare TemplatesPanel
   *
   * @param panel JPanel
   */
  public void prepareTemplatesPanel(JPanel panel) {
    JPanel listPanel = this.prepareTemplateListPanel();
    panel.setLayout(new BorderLayout(2, 2));
    panel.add(listPanel, BorderLayout.CENTER);
  }

  /**
   * prepareTemplateListPanel
   *
   * @return JPanel
   */
  public JPanel prepareTemplateListPanel() {
    JPanel main = new JPanel(new BorderLayout(2, 2));

    JPanel languages = this.prepareLanguagesPanel();

    JPanel templates = new TPanel(new BorderLayout(2, 2), "templates");
    templates.add(new JScrollPane(this.templates), BorderLayout.CENTER);

    main.add(languages, BorderLayout.NORTH);
    main.add(templates, BorderLayout.CENTER);
    //listTemplates.addMouseListener(this);
    this.templates.addTreeSelectionListener(this);
    return main;
  }

  private JPanel prepareLanguagesPanel() {
    JPanel main = new JPanel(new BorderLayout());

    JPanel combo = new JPanel(new BorderLayout());
    combo.add(new JLabel("langauge "), BorderLayout.WEST);
    combo.add(languages, BorderLayout.CENTER);
    languages.addActionListener(this);

    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(jButtonAddLangauge);
    buttons.add(jButtonDeleteLangauge);
    buttons.add(jButtonExport);

    jButtonAddLangauge.addActionListener(this);
    jButtonDeleteLangauge.addActionListener(this);
    jButtonExport.addActionListener(this);

    main.add(combo, BorderLayout.CENTER);
    main.add(buttons, BorderLayout.SOUTH);
    return main;
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

  /**
   * getSelectedTemplate
   *
   * @return Object
   */
  public Object getSelectedTemplate() {
    //return getSelecetdList(listTemplates);
    Object selected = null;
    Object node = templates.getLastSelectedPathComponent();
    if (node instanceof DefaultMutableTreeNode) {
      selected = ( (DefaultMutableTreeNode) node).getUserObject();
    }
    // return listTemplates.getSelecetdList();
    return selected;
  }

  public JPanel getPreviewPanel() {
    return preview;
  }

  public ConstraintGroupPanel getConstraintGroupPanel() {
    return this.groups;
  }

  public void fillLanguages(List<Language> list) {
	 FrameUtil.fillComboBox(languages, new ArrayList<Object>(list));
  }

  public Object getSelectedLanguage() {
    return languages.getModel().getSelectedItem();
  }

  public void valueChanged(TreeSelectionEvent e) {
    templateCoordinator.templateSelected();
  }

  public void setLanguageTreeListener(ILanguageTreeListener l) {
    templates.setListener(l);
  }
}
