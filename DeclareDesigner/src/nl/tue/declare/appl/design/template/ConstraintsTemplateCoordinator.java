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
package nl.tue.declare.appl.design.template;

import java.util.*;

import javax.swing.*;

import nl.tue.declare.appl.design.InternalCoordinator;
import nl.tue.declare.appl.design.template.gui.*;
import nl.tue.declare.appl.util.JGraphLineComboBox;
import nl.tue.declare.appl.util.swing.languagetree.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;
import nl.tue.declare.graph.template.*;
import nl.tue.declare.utils.prom.*;

public class ConstraintsTemplateCoordinator extends InternalCoordinator
		implements ILTLSyntaxCheckListener, ILanguageTreeListener {

	private FrmLanguages frame = null;
	private FrmTemplate frmTemplate = null;
	private FrmLanguage frmLanguage = null;
	private FrmGroup frmGroup = null;
	private static ConstraintsTemplateCoordinator instance = null;
	
	private FormulaSyntaxChecker syntaxChecker; 

	private ConstraintsTemplateCoordinator(JFrame aMainFrame) {
		super(aMainFrame);
		syntaxChecker = new FormulaSyntaxChecker();
		frame = FrmLanguages.singleton(this);
		frame.setLanguageTreeListener(this);

		frmTemplate = new FrmTemplate(aMainFrame, frame);
		frmTemplate.addSyntaxCheckListener(this);
		this.frmLanguage = new FrmLanguage(aMainFrame, "LTL template", frame);
		new ConstraintGroupCoordinator(aMainFrame,frame.getConstraintGroupPanel(), frame);
		frmGroup = new FrmGroup(aMainFrame, frame);
	}

	/**
	 * 
	 * @param mainFrame
	 *            JFrame
	 * @return ConstraintsTemplateCoordinator
	 */
	public static ConstraintsTemplateCoordinator singleton(JFrame mainFrame) {
		if (instance == null) {
			instance = new ConstraintsTemplateCoordinator(mainFrame);
		}
		return (ConstraintsTemplateCoordinator) instance;
	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean exists() {
		return instance != null;
	}

	public static void finish() {
		instance = null;
	}

	/**
	 * end
	 * 
	 * @todo Implement this
	 *       nl.tue.declare.appl.design.coordinate.InternalCoordinator method
	 */
	public void end() {
	}

	/**
	 * getInternalFrame
	 * 
	 * @return JInternalFrame
	 * @todo Implement this
	 *       nl.tue.declare.appl.design.coordinate.InternalCoordinator method
	 */
	public JInternalFrame getInternalFrame() {
		return frame;
	}

	/**
	 * start
	 * 
	 * @todo Implement this
	 *       nl.tue.declare.appl.design.coordinate.InternalCoordinator method
	 */
	public void start() {
		this.fillLanguages();
		templateSelected();
	}

	private void fillLanguages() {
		frame.fillLanguages(this.getControl().getConstraintTemplate()
				.getLanguages());
		langaugeChanged();
	}


	private Language getLanguage() {
		Object selected = this.frame.getSelectedLanguage();
		Language lang = null;
		if (selected instanceof Language) {
			lang = (Language) selected;
		}
		return lang;
	}


	/**
	 * deleteTemplateConfirmed
	 * 
	 * @param template
	 *            ConstraintTemplate
	 */
	public void deleteTemplateConfirmed(ConstraintTemplate template) {
		boolean canRemove = getControl().getConstraintTemplate()
				.deleteTemplate(template);
		if (canRemove) {
		} else {
			JOptionPane
					.showInternalMessageDialog(
							frame,
							"The template "
									+ template
									+ " cannot be deleted because it is used in assignment definitons.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * fillListTemplates
	 * 
	 * @param lang
	 *            Language
	 */
	public void fillListTemplates(Language lang) {
		if (lang != null) {
			frame.fillTemplates(lang);
			templateSelected();
		}
	}

	/**
	 * templateSelected
	 */
	public void templateSelected() {
		ConstraintTemplate template = getSelectedTemplate();
		JPanel panel = this.frame.getPreviewPanel();
		panel.removeAll();
		if (template != null) {
			TemplateView view = new TemplateView(template);
			panel.add(view.createGraph());
		}
		panel.updateUI();
	}

	/**
	 * getSelectedTemplate
	 * 
	 * @return ConstraintTemplate
	 */
	public ConstraintTemplate getSelectedTemplate() {
		Object selected = frame.getSelectedTemplate();
		ConstraintTemplate template = null;
		if (selected != null) {
			if (selected instanceof ConstraintTemplate) {
				template = (ConstraintTemplate) selected;
			}
		}
		return template;
	}


	/**
	 * fillDashCombo
	 * 
	 * @param combo
	 *            JGraphLineComboBox
	 * @param style
	 *            LineStyle
	 */
	public void fillDashCombo(JGraphLineComboBox combo, LineStyle style) {
		ArrayList<LineStyle> styles = DCellFactory.getNumberLineStyles(
				DGraphConstants.DASH_NONE, DGraphConstants.ARROW_NONE,
				DGraphConstants.ARROW_NONE, DGraphConstants.ARROW_NONE);
		LineStyle select = null;
		combo.clear();
		for (int i = 0; i < styles.size(); i++) {
			LineStyle curr = styles.get(i);
			combo.addStyle(curr);
			if (style != null) {
				if (curr.getNumber() == style.getNumber()) {
					select = curr;
				}
			}
		}
		combo.setSelected(select);
	}

	/**
	 * fillMiddleCombo
	 * 
	 * @param combo
	 *            JGraphLineComboBox
	 * @param style
	 *            LineStyle
	 */
	public void fillMiddleCombo(JGraphLineComboBox combo, LineStyle style) {
		ArrayList<LineStyle> styles = DCellFactory.getMiddleStyles(
				DGraphConstants.DASH_NONE, DGraphConstants.ARROW_NONE,
				DGraphConstants.ARROW_NONE);
		LineStyle select = null;
		combo.clear();
		for (int i = 0; i < styles.size(); i++) {
			LineStyle curr = styles.get(i);
			combo.addStyle(curr);
			if (style != null) {
				if (curr.getMiddle() == style.getMiddle()) {
					select = curr;
				}
			}
		}
		combo.setSelected(select);
	}

	/**
	 * fillBeginCombo
	 * 
	 * @param combo
	 *            JGraphLineComboBox
	 * @param style
	 *            LineStyle
	 */
	public void fillBeginCombo(JGraphLineComboBox combo, LineStyle style) {
		ArrayList<LineStyle> styles = DCellFactory.getBeginStyles(
				DGraphConstants.DASH_NONE, DGraphConstants.ARROW_NONE,
				DGraphConstants.ARROW_NONE);
		LineStyle select = null;
		combo.clear();
		for (int i = 0; i < styles.size(); i++) {
			LineStyle curr = styles.get(i);
			combo.addStyle(curr);
			if (style != null) {
				if (curr.getBegin() == style.getBegin()) {
					select = curr;
				}
			}
		}
		combo.setSelected(select);
	}

	/**
	 * fillEndCombo
	 * 
	 * @param combo
	 *            JGraphLineComboBox
	 * @param style
	 *            LineStyle
	 */
	public void fillEndCombo(JGraphLineComboBox combo, LineStyle style) {
		ArrayList<LineStyle> styles = DCellFactory.getEndStyles(
				DGraphConstants.DASH_NONE, DGraphConstants.ARROW_NONE,
				DGraphConstants.ARROW_NONE);
		LineStyle select = null;
		combo.clear();
		for (int i = 0; i < styles.size(); i++) {
			LineStyle curr = styles.get(i);
			combo.addStyle(curr);
			if (style != null) {
				if (curr.getEnd() == style.getEnd()) {
					select = curr;
				}
			}
		}
		combo.setSelected(select);
	}

	/**
	 * 
	 * @param formula
	 *            String
	 * @return boolean
	 */
	public boolean checkSyntax(String formula) {
		return this.checkLTLSyntax(formula);
	}

	/**
	 * 
	 * @param formula
	 *            String
	 * @return boolean
	 */
	private boolean checkLTLSyntax(String formula) {
		return syntaxChecker.check(formula, this.frame);
	}

	/**
	 * 
	 * @param formula
	 *            String
	 * @return boolean
	 */
	private boolean checkLTLSyntaxNotify(String formula) {
		return syntaxChecker.checkNotify(formula, this.frame);
	}

	/**
	 * 
	 * @param formula
	 *            String
	 */
	public void checkSyntaxNotify(String formula) {
		this.checkLTLSyntaxNotify(formula);
	}

	private List<ConstraintTemplate> getTemplates() {
		final List<ConstraintTemplate> list = new ArrayList<ConstraintTemplate>();
		this.getLanguage().visitAll(new LanguageGroup.GroupVisitor() {
			public void visitChild(IItem parent, IItem child) {
				if (child instanceof ConstraintTemplate) {
					list.add((ConstraintTemplate) child);
				}
			}
		});
		return list;
	}

	public void export() {
		// ProMExport.templates(this.getLanguage().getTemplates(), frame);
		ProMExport.templates(this.getTemplates(), frame, this.getLanguage().getName());
	}

	public void langaugeChanged() {
		fillListTemplates(this.getLanguage());
	}

	public void addLanguage() {
		this.fillFormFromLang(null, this.frmLanguage);
		frmLanguage.setTitle("Add language");
		if (frmLanguage.showCentered()) {
			this.addLanguageConfirmed();
		}
	}

	private void addLanguageConfirmed() {
		Language lang = this.getControl().getConstraintTemplate()
				.createLanguage();
		this.fillLangFromForm(lang, frmLanguage);
		if (!this.getControl().getConstraintTemplate().addLanguage(lang)) {
			JOptionPane.showInternalMessageDialog(frame,
					"You cannot add a language with an existing name.",
					"information", JOptionPane.INFORMATION_MESSAGE);
		}

		this.fillLanguages();
	}

	public void deleteLanguage() {
		Language lang = this.getLanguage();
		if (lang != null) {
			Object[] options = { "OK", "CANCEL" };
			int response = JOptionPane.showOptionDialog(frame,
					"Do you want to delete language " + lang + "?",
					"confirmation", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (response == JOptionPane.OK_OPTION) {
				deleteLangConfirmed(lang);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							frame,
							"No role is selected. You must first select the role you want to delete.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * deleteRoleConfirmed
	 * 
	 * @param lang
	 *            Role
	 */
	public void deleteLangConfirmed(Language lang) {
		boolean canRemove = this.getControl().getConstraintTemplate()
				.deleteLanguage(lang);
		if (canRemove) {
			fillLanguages();
		} else {
			JOptionPane.showInternalMessageDialog(frame, "The language " + lang
					+ " cannot be deleted.", "information",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * fillRoleFromForm
	 * 
	 * @param lang
	 *            Role
	 * @param form
	 *            FrmRole
	 */
	public void fillLangFromForm(Language lang, FrmLanguage form) {
		if ((form != null) && (lang != null)) {
			form.toLanguage(lang);
		}
	}

	/**
	 * fillFormFromRole
	 * 
	 * @param lang
	 *            Role
	 * @param form
	 *            FrmRole
	 */
	private void fillFormFromLang(Language lang, FrmLanguage form) {
		if (form != null) {
			form.fromLanguage(lang);
		}
	}

	/**
	 * 
	 * @param parent
	 *            Group
	 * @return LanguageGroup
	 */
	public LanguageGroup addGroup(LanguageGroup parent) {
		LanguageGroup group = null;
		frmGroup.fromGroup(null);
		if (frmGroup.showCentered()) {
			group = getLanguage().createGroup();
			frmGroup.toGroup(group);
			addGroup(group, parent);
		}
		return group;
	}

	/**
	 * 
	 * @param parent
	 *            Group
	 * @return LanguageGroup
	 */
	private void addGroup(LanguageGroup group, LanguageGroup parent) {
		getControl().getConstraintTemplate().addLanguageGroup(group, parent,
				this.getLanguage());
	}

	/**
	 * 
	 * @param group
	 *            Group
	 * @return boolean
	 */
	public boolean deleteGroup(LanguageGroup group) {
		boolean ok = false;
		if (group != null) {
			Object[] options = { "OK", "CANCEL" };
			int response = JOptionPane.showOptionDialog(frame,
					"Do you want to delete group " + group
							+ " and all its items?", "confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[1]);
			ok = response == JOptionPane.OK_OPTION;
			if (ok) {
				deleteSimpleGroup(group);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							frame,
							"No role is selected. You must first select the role you want to delete.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
		return ok;
	}

	/**
	 * 
	 * @param group
	 *            Group
	 * @return boolean
	 */
	public void deleteSimpleGroup(LanguageGroup group) {
		Language lang = getLanguage();
		getControl().getConstraintTemplate().deleteLanguageGroup(group,
				lang.getParent(group), lang);
	}

	public void editGroup(LanguageGroup group) {
		frmGroup.fromGroup(group);
		if (frmGroup.showCentered()) {
			frmGroup.toGroup(group);
			getControl().getConstraintTemplate().editLanguageGroup(group,
					getLanguage());
		}
	}

	public ConstraintTemplate addTemplate(LanguageGroup parent,int nrParameters) {
		ConstraintTemplate template = getLanguage().createTemplate(nrParameters);
		frmTemplate.fromTemplate(template);
		frmTemplate.setTitle("Add template");
		if (frmTemplate.showCentered()) {
			frmTemplate.toTemplate(template);
			if (!addTemplate(template, parent)) {
				JOptionPane
						.showInternalMessageDialog(
								frame,
								"You cannot add a template for the formula that alreday exists.",
								"information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		return template;
	}

	public boolean addTemplate(ConstraintTemplate template, LanguageGroup parent) {
		return getControl().getConstraintTemplate().addTemplate(template,
				parent);
	}

	public void editTemplate(ConstraintTemplate template) {
		if (template != null) {
			frmTemplate.fromTemplate(template);
			frmTemplate.setTitle("Edit template");
			if (frmTemplate.showCentered()) {
				frmTemplate.toTemplate(template);
				this.getControl().getConstraintTemplate()
						.editTemplate(template);
				fillListTemplates(this.getLanguage());
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							frame,
							"No template is selected. You must first select a template you want to edit.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public boolean deleteTemplate(ConstraintTemplate template) {
		boolean ok = false;
		if (template != null) {
			Object[] options = { "OK", "CANCEL" };
			int response = JOptionPane.showOptionDialog(frame,
					"Do you want to delete the template " + template + "?",
					"confirmation", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			ok = response == JOptionPane.OK_OPTION;
			if (ok) {
				ok = deleteSimpleTemplate(template);
				if (!ok) {
					JOptionPane
							.showInternalMessageDialog(
									frame,
									"The template "
											+ template
											+ " cannot be deleted because it is used in assignment definitons.",
									"information",
									JOptionPane.INFORMATION_MESSAGE);

				}
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							frame,
							"No template is selected. You must first select the role you want to delete.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
		return ok;
	}

	private boolean deleteSimpleTemplate(ConstraintTemplate template) {
		return getControl().getConstraintTemplate().deleteTemplate(template);
	}

	public void move(LanguageGroup oldParent, IItem node,
			LanguageGroup newParent) {
		if (node instanceof LanguageGroup) {
			LanguageGroup group = (LanguageGroup) node;
			deleteSimpleGroup(group);
			addGroup(group, newParent);
		}
		if (node instanceof ConstraintTemplate) {
			ConstraintTemplate template = (ConstraintTemplate) node;
			deleteSimpleTemplate(template);
			addTemplate(template, newParent);
		}
	}
}
