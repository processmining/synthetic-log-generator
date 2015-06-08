package nl.tue.declare.appl.design.template.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import nl.tue.declare.appl.util.JGraphLineComboBox;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;

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
class ParameterPanel
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8832034657037241935L;

private JCheckBox delete = new JCheckBox("delete");

  private JTextField name = new JTextField();
  private JGraphLineComboBox line = new JGraphLineComboBox();
  private Symbol begin = new Symbol("begin");
  //private JGraphLineComboBox beginStyle = new JGraphLineComboBox();
  //private JCheckBox beginFill = new JCheckBox("fill");
  private Symbol end = new Symbol("end");
  //private JGraphLineComboBox endStyle = new JGraphLineComboBox();
  //private JCheckBox endFill = new JCheckBox("fill");
  private JCheckBox branch = new JCheckBox("branch");

  public ParameterPanel(String name, Parameter parameter) {
    super(new BorderLayout());
    //setLayout(new BorderLayout());
    add(this.getDeletePanel(), BorderLayout.WEST);
    add(this.getDataPanel(), BorderLayout.CENTER);
    fillDashCombo();
    fillBeginAndEndCombo();
    fromParameter(parameter);
    if (this.name.getText().replace(" ", "").equals("")) {
      this.name.setText(name);
    }
  }

  private JPanel getDeletePanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.add(delete);
    return main;
  }

  private JPanel getDataPanel() {
    //JPanel main = new JPanel(new GridLayout(1, 5));
	  JPanel main = new JPanel(new FlowLayout());
    main.setBorder(new EtchedBorder());
    main.add(getNamePanel());
    JPanel temp = new JPanel(new BorderLayout());
    temp.add(branch,BorderLayout.CENTER);
    main.add(temp);
    main.add(begin);
    //main.add(beginStyle);
    //main.add(beginFill);
    main.add(getLinePanel());
    main.add(end);
    //main.add(endStyle);
    //main.add(endFill);
    return main;
  }

  private JPanel getNamePanel() {
    JPanel main = new TPanel(new BorderLayout(),"name");
    //main.add(new JLabel("name"), BorderLayout.WEST);
    main.add(name, BorderLayout.CENTER);
    return main;
  }

  private JPanel getLinePanel() {
    JPanel main = new TPanel(new BorderLayout(),"line");
    //main.add(new JLabel("line"), BorderLayout.WEST);
    main.add(line, BorderLayout.CENTER);
    return main;
  }

  boolean delete() {
    return delete.isSelected();
  }

  public void toParameter(Parameter parameter) {
    if (parameter != null) {
      parameter.setName(name.getText());
      parameter.setBranchable(branch.isSelected());
      parameter.getStyle().setLine(line.getSelectedStyle().getLine());
      parameter.getStyle().setNumber(line.getSelectedStyle().getNumber());
      parameter.getStyle().setBegin( begin.style.getSelectedStyle().getBegin());
      parameter.getStyle().setEnd(end.style.getSelectedStyle().getEnd());
      parameter.getStyle().setBeginFill(begin.fill.isSelected());
      parameter.getStyle().setEndFill(end.fill.isSelected());
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
    this.begin.style.setSelected(this.begin.getBegin(begin));
    this.end.style.setSelected(this.end.getEnd(end));
    this.begin.fill.setSelected(beginFill);
    this.end.fill.setSelected(endFill);

    this.delete.setSelected(false);
  }


  private LineStyle getNumber(int number) {
    Iterator<LineStyle> i = line.styles();
    boolean found = false;
    LineStyle line = null;
    while (i.hasNext() && !found) {
      line = i.next();
      found = line.getNumber() == number;
    }
    if (found) {
      return line;
    }
    return null;
  }

  /**
   * fillBeginCombo
   */
  public void fillBeginAndEndCombo() {
    ArrayList<LineStyle>
        begin = DCellFactory.getBeginStyles(DGraphConstants.DASH_NONE,
                                            DGraphConstants.ARROW_NONE,
                                            DGraphConstants.ARROW_NONE);
    this.begin.style.addStyles(begin);

    ArrayList<LineStyle>
        end = DCellFactory.getEndStyles(DGraphConstants.DASH_NONE,
                                        DGraphConstants.ARROW_NONE,
                                        DGraphConstants.ARROW_NONE);
    this.end.style.addStyles(end);
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

  boolean ok() {
    String name = this.name.getText().replace(" ", "");
    return!name.equals("");
  }

  String getParameterName() {
    return name.getText();
  }
  

  /**
   *
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
  class Symbol
      extends TPanel {

	private static final long serialVersionUID = 6333675814121391742L;
	JGraphLineComboBox style = new JGraphLineComboBox();
    JCheckBox fill = new JCheckBox("fill");

    Symbol(String title) {
      super(new BorderLayout(), title);
      //style.setPreferredSize(new Dimension(100,style.getPreferredSize().height));

      //add(new JLabel("  " + title + " "), BorderLayout.WEST);
      add(style, BorderLayout.CENTER);
      add(fill, BorderLayout.EAST);
    }

    LineStyle getEnd(int index) {
        Iterator<LineStyle> i = this.style.styles();
        boolean found = false;
        LineStyle line = null;
        while (i.hasNext() && !found) {
          line = i.next();
          found = line.getEnd() == index;
        }
        if (found) {
          return line;
        }
        return null;
      }
    
    LineStyle getBegin(int index) {
        Iterator<LineStyle> i = this.style.styles();
        boolean found = false;
        LineStyle line = null;
        while (i.hasNext() && !found) {
          line = i.next();
          found = line.getBegin() == index;
        }
        if (found) {
          return line;
        }
        return null;
      }
  }
}
