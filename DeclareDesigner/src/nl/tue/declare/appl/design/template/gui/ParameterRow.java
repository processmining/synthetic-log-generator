package nl.tue.declare.appl.design.template.gui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import nl.tue.declare.appl.util.JGraphLineComboBox;
import nl.tue.declare.domain.template.Parameter;
import nl.tue.declare.graph.DCellFactory;
import nl.tue.declare.graph.DGraphConstants;
import nl.tue.declare.graph.LineStyle;

class ParameterRow {
	//final JCheckBox delete = new JCheckBox();
	JTextField name = new JTextField();
	final JCheckBox branch = new JCheckBox();
	final JGraphLineComboBox beginSym = new JGraphLineComboBox();
	final JCheckBox beginFill = new JCheckBox();
	final JGraphLineComboBox line = new JGraphLineComboBox();
	final JGraphLineComboBox endSym = new JGraphLineComboBox();
	final JCheckBox endFill = new JCheckBox();	
	
	final Parameter parameter;
	
	ParameterRow(String name, Parameter p){
		super();
		parameter = p;
	    fillDashCombo();
	    fillBeginAndEndCombo();
	    fromParameter(parameter);
	    if (this.name.getText().replace(" ", "").equals("")) {
	      this.name.setText(name);
	    }
	}
	
	  /**
	   * fillBeginCombo
	   */
	  public void fillBeginAndEndCombo() {
	    ArrayList<LineStyle>
	        begin = DCellFactory.getBeginStyles(DGraphConstants.DASH_NONE,
	                                            DGraphConstants.ARROW_NONE,
	                                            DGraphConstants.ARROW_NONE);
	    this.beginSym.addStyles(begin);

	    ArrayList<LineStyle>
	        end = DCellFactory.getEndStyles(DGraphConstants.DASH_NONE,
	                                        DGraphConstants.ARROW_NONE,
	                                        DGraphConstants.ARROW_NONE);
	    this.endSym.addStyles(end);
	  }

	  /**
	   * fillDashCombo
	   */
	  public void fillDashCombo() {
	    ArrayList<LineStyle>
	        styles = DCellFactory.getNumberLineStyles(DGraphConstants.DASH_NONE,
	                                                  DGraphConstants.ARROW_NONE,
	                                                  DGraphConstants.ARROW_NONE,
	                                                  DGraphConstants.ARROW_NONE);
	    line.clear();
	    for (int i = 0; i < styles.size(); i++) {
	      LineStyle curr = styles.get(i);
	      line.addStyle(curr);
	    }
	  }	
	
	  public void toParameter(Parameter parameter) {
		    if (parameter != null) {
		      parameter.setName(name.getText());
		      parameter.setBranchable(branch.isSelected());
		      parameter.getStyle().setLine(line.getSelectedStyle().getLine());
		      parameter.getStyle().setNumber(line.getSelectedStyle().getNumber());
		      parameter.getStyle().setBegin( beginSym.getSelectedStyle().getBegin());
		      parameter.getStyle().setEnd(endSym.getSelectedStyle().getEnd());
		      parameter.getStyle().setBeginFill(beginFill.isSelected());
		      parameter.getStyle().setEndFill(endFill.isSelected());
		    }
		  }

		  public void fromParameter(Parameter parameter) {
		    String name = "";
		    boolean branch = false;
		    int number = 0;
		    int begin = 0;
		    int end = 0;
		    boolean beginFill = false;
		    boolean endFill = false;

		    if (parameter != null) {
		      name = parameter.getName();
		      branch = parameter.isBranchable();
		      number = parameter.getStyle().getNumber();
		      begin = parameter.getStyle().getBegin();
		      end = parameter.getStyle().getEnd();
		      beginFill = parameter.getStyle().isBeginFill();
		      endFill = parameter.getStyle().isEndFill();
		    }

		    this.name.setText(name);
		    this.branch.setSelected(branch);
		    this.line.setSelected(this.getNumber(number));
		    this.beginSym.setSelected(this.getBegin(begin));
		    this.endSym.setSelected(this.getEnd(end));
		    this.beginFill.setSelected(beginFill);
		    this.endFill.setSelected(endFill);

		   // this.delete.setSelected(false);
		  }
		  private LineStyle getNumber(int number) {
			    Iterator<LineStyle> i = line.styles();
			    while (i.hasNext()) {
			      LineStyle line = i.next();
			      if (line.getNumber() == number)
			    	  return line;
			    }
			    return null;
			  }
		  
		  private LineStyle getBegin(int begin) {
			    Iterator<LineStyle> i = beginSym.styles();
			    while (i.hasNext()) {
			      LineStyle line = i.next();
			      if (line.getBegin() == begin)
			    	  return line;
			    }
			    return null;
			  }
		  
		 
		  private LineStyle getEnd(int end) {
			    Iterator<LineStyle> i = endSym.styles();
			    while (i.hasNext()) {
			      LineStyle line = i.next();
			      if (line.getEnd() == end)
			    	  return line;
			    }
			    return null;
			  }
		  
		  boolean ok() {
			    String name = this.name.getText().replace(" ", "");
			    return!name.equals("");
		  }
}