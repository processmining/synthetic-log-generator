package nl.tue.declare.appl.design.template.gui;

import info.clearthought.layout.TableLayout;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.tue.declare.appl.util.swing.*;
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
public class ParametersPanel
extends TPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6066154977292652642L;
	private static double p = TableLayout.PREFERRED;
	private static final String ALIGN = ", F, F";
	private static double COMBO = 100;

	private static double size[][] = {
			{ TableLayout.FILL, /* 0.99, */p, COMBO, p, COMBO, COMBO, p }, { p } };
	private TableLayout layout = new TableLayout(size);

	private HashMap<Parameter, ParameterRow> rows = new HashMap<Parameter, ParameterRow>();

	//private boolean editable = true;

	//private ParametersGrid grid = new ParametersGrid();

	public ParametersPanel() {
		super(null, "parameters");
		setLayout(layout);
		setGUI();
	}

	private void setGUI() {
		Box beginSym = new Box(BoxLayout.PAGE_AXIS);
		beginSym.add(new JLabel("begin"));
		beginSym.add(new JLabel("symbol"));

		Box beginFill = new Box(BoxLayout.PAGE_AXIS);
		beginFill.add(new JLabel("begin"));
		beginFill.add(new JLabel("fill"));

		Box endSym = new Box(BoxLayout.PAGE_AXIS);
		endSym.add(new JLabel("end"));
		endSym.add(new JLabel("symbol"));

		Box endFill = new Box(BoxLayout.PAGE_AXIS);
		endFill.add(new JLabel("end"));
		endFill.add(new JLabel("fill"));

		add(new MyPanel(new JLabel("name")), "0, 0" + ALIGN);
		add(new MyPanel(new JLabel("branch")), "1, 0" + ALIGN);
		add(new MyPanel(beginSym), "2, 0" + ALIGN);
		add(new MyPanel(beginFill), "3, 0" + ALIGN);
		add(new MyPanel(new JLabel("line")), "4, 0" + ALIGN);
		add(new MyPanel(endSym), "5, 0" + ALIGN);
		add(new MyPanel(endFill), "6, 0" + ALIGN);
	}

	public void addParameter(Parameter parameter) {
		ParameterRow row = new ParameterRow("param"
				+ Integer.toString(rows.size()), parameter);
		rows.put(parameter, row);

		int r = layout.getNumRow();
		layout.insertRow(r, p);
		String sr = Integer.toString(r);
		add(new MyPanel(row.name), "0, " + sr + ALIGN);
		add(new MyPanel(row.branch), "1, " + sr + ALIGN);
		add(new MyPanel(row.beginSym), "2, " + sr + ALIGN);
		add(new MyPanel(row.beginFill), "3, " + sr + ALIGN);
		add(new MyPanel(row.line), "4, " + sr + ALIGN);
		add(new MyPanel(row.endSym), "5, " + sr + ALIGN);
		add(new MyPanel(row.endFill), "6, " + sr + ALIGN);

		// Layout and repaint panel since the layout has changed
		doLayout();
		repaint();
	}

	public int count() {
		return rows.size();
	}

	ParameterRow get(Parameter p) {
		return rows.get(p);
	}

	public Iterable<Entry<Parameter, ParameterRow>> getRows() {
		return rows.entrySet();
	}

	void clear() {
		for (int i = layout.getNumRow() - 1; i > 0; i--) {
			layout.deleteRow(i);
		}
		rows.clear();
	}

	boolean ok() {
		Iterator<Entry<Parameter, ParameterRow>> i = rows.entrySet().iterator();
		boolean ok = true;
		while (i.hasNext() && ok) {
			ParameterRow panel = i.next().getValue();
			ok = panel.ok();
		}
		return ok;
	}

	private void change() {
		this.revalidate();
		this.updateUI();
	}

	public void from(ConstraintTemplate template) {
		this.clear();
		if (template != null) {
			for (Parameter parameter: template.getParameters()) {
				this.addParameter(parameter);
			}
		}
		change();
	}
	
	public void to(ConstraintTemplate template) {
		for (Parameter parameter: template.getParameters()) {
			ParameterRow panel = get(parameter);
			panel.toParameter(parameter);
		}
	}

	private class MyPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2599265583144226754L;

		public MyPanel(Component c) {
			super();
			setBorder(BorderFactory.createTitledBorder(""));
			setLayout(new BorderLayout());
			add(c, BorderLayout.CENTER);
		}
	}
}
