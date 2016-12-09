package nl.tue.declare.utils.prom;

import java.io.*;
import java.util.*;

import nl.tue.declare.appl.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.execution.ltl.ConstraintParser;
import nl.tue.declare.utils.*;
import ltl2aut.ltl.LTLFormula;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
class AbstractCollection /* extends Parser */{

	protected static final String EventType = "event";
	protected static final String Originator = "person";
	protected static final String Timestamp = "time";
	protected static final String WorkflowModelElement = "activity";
	protected static final String EQUALS = "==";

	protected static final String SEPARATOR = "#############################################################################";

	protected ConstraintParser parser;

	protected ArrayList<String> lines;

	private PrintStream out;

	/**
	 * 
	 * @param out
	 *            PrintStream
	 */
	AbstractCollection(ConstraintParser parser, PrintStream out) {
		super();
		lines = new ArrayList<String>();
		this.parser = parser;
		this.out = out;
		header();
	}

	/**
   *
   */
	void write() {
		if (out != null) {
			for (int i = 0; i < lines.size(); i++) {
				out.println(lines.get(i));
			}
		}
	}

	/**
   *
   */
	private void header() {
		lines.add("# version            : 0.1");
		PrettyTime time = new PrettyTime();
		lines.add("# date           : " + time.toString());
		lines.add("# author         : " + Project.NAME);
		lines.add("##");
		lines.add("## ");
		lines
				.add("# Start with defining the 'standard' attributes, as specified in the xml");
		lines
				.add("# specification of the workflow logs for the ProM framework.");
		lines.add("##");
		lines.add("set ate.EventType;");
		lines.add("set ate.Originator;");
		lines.add("date ate.Timestamp := \"yyyy-MM-dd\"; ");
		lines
				.add("# The format you should supply date literals to the attribute ate.Timestamp or");
		lines
				.add("# renamings of that attribute is a four digit year, a dash a two digit month,");
		lines
				.add("# again a dash and a two digit day. For example 2004-12-08, denoting December");
		lines.add("# the 8th of 2004.");
		lines.add("set ate.WorkflowModelElement;");
		lines.add("##");
		lines
				.add("# Some 'standard' renamings. First some names of the attributes without the");
		lines
				.add("# ate. and without capitals. Then some renamings which can be used more");
		lines
				.add("# often. You can add easily your own renamings, as long you remember that");
		lines.add("# every name must be unique.");
		lines.add("##");
		lines.add("rename ate.EventType as " + EventType + ";");
		lines.add("rename ate.Originator as " + Originator + ";");
		lines.add("rename ate.Timestamp as " + Timestamp + ";");
		lines.add("rename ate.WorkflowModelElement as " + WorkflowModelElement
				+ ";");
	}

	/**
	 * 
	 * @param ltlFormula
	 *            LTLFormula
	 * @return String
	 */
	protected String formula(ConstraintTemplate template) {
		String result = "";
		try {
			String ltl = new String(template.getText());
			result = formula(ltl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	protected String formula(String ltl){
		String result = "";
		try {
			ProMFormulaParser parser = new ProMFormulaParser(ltl);
			LTLFormula formula = parser.parse();
			process(formula);
			result = formulaToProMString(formula);
		} catch (Exception ex) {
			System.out.println(ltl);
			ex.printStackTrace();			
		}
		return result;
	}

	private void process(LTLFormula formula) {
		LTLFormula left = formula.getLeft();
		LTLFormula right = formula.getRight();
		if (left != null) {
			if (left.getType() == LTLFormula.PROPOSITION) {
				defaultCompleted(left);
			} else {
				process(left);
			}
		}
		if (right != null) {
			if (right.getType() == LTLFormula.PROPOSITION) {
				defaultCompleted(right);
			} else {
				process(right);
			}
		}
	}

	private void defaultCompleted(LTLFormula proposition) {
		StringTokenizer st = new StringTokenizer(proposition.getName(), ".");
		String task = WorkflowModelElement + " " + EQUALS + " \""
				+ st.nextToken() + "\"";
		String event = "";
		if (st.hasMoreTokens()) {
			event = st.nextToken();
		}
		event = EventType + " " + EQUALS + " \"" + ProM.convert(event) + "\"";
		proposition.setName(task + " /\\ " + event);
	}
	
	private String formulaToProMString(LTLFormula f){
			switch (f.getType()) {
			case LTLFormula.AND:
				return "( " +  formulaToProMString(f.getLeft()) + " /\\ " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.OR:
				return "( " + formulaToProMString(f.getLeft()) + " \\/ " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.UNTIL:
				return "( " + formulaToProMString(f.getLeft()) + " _U " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.RELEASE:
				return "( " + formulaToProMString(f.getLeft()) + " _V " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.WUNTIL:
				return "( " + formulaToProMString(f.getLeft()) + " _W " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.IMPLIES:
				return "( " + formulaToProMString(f.getLeft()) + " -> " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.EQUAL:
				return "( " + formulaToProMString(f.getLeft()) + " <-> " + formulaToProMString(f.getRight()) + " )";

			case LTLFormula.NEXT:
				return "_O ( " + formulaToProMString(f.getLeft()) + " )";

			case LTLFormula.NOT:
				return "! ( " + formulaToProMString(f.getLeft()) + " )";

			case LTLFormula.ALWAYS:
				return "[] ( " + formulaToProMString(f.getLeft()) + " )";
				
			case LTLFormula.EVENTUALLY:
				return "<> ( " + formulaToProMString(f.getLeft()) + " )";
				
			case LTLFormula.TRUE:
				return "( true )";

			case LTLFormula.FALSE:
				return "( false )";

			case LTLFormula.PROPOSITION:
				return "( " + f.getName() + " )";

			default:
				return new Character(f.getType()).toString();
			}

	}

	/*
	 * protected String formula(String formula) { String result = ""; /*
	 * parser.defaultCompleted(formula); Iterator<IProposition> propositions =
	 * formula.getPropositions().iterator(); while (propositions.hasNext()) {
	 * IProposition p = propositions.next(); if (p instanceof Proposition) {
	 * Proposition old = (Proposition) p; IFormula changed =
	 * this.changeProposition(old); formula.replace(old, changed); } } result =
	 * formula.ProM();
	 */
	/*
	 * return result; }
	 */

	/**
	 * 
	 * @param proposition
	 *            Proposition
	 * @return IFormula
	 */
	/*
	 * private IFormula changeProposition(Proposition proposition) { return
	 * FormulaFactory.and(activity(proposition), event(proposition)); }
	 * 
	 * protected IProposition activity(Proposition proposition) { return new
	 * Proposition(WorkflowModelElement + " " + EQUALS + " " +
	 * proposition.getActivity()); }
	 * 
	 * protected IProposition event(Proposition proposition) { return new
	 * Proposition(EventType + " " + EQUALS + " \"" +
	 * ProM.convert(proposition.getEvent()) + "\""); }
	 */
}
