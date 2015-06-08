package nl.tue.declare.appl.design.model.gui;

import java.util.*;
import java.util.List;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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
  private static final long serialVersionUID = -8115139267530376732L;
  private ConstraintTemplate template = null;
  private ParameterPanel.Listener parameterListener = null;
  private List<ParameterPanel> panels = new ArrayList<ParameterPanel>();
  private Color line = null;
  
  public ParametersPanel() {
    super(new GridLayout(), "parameters");
    line = Color.BLACK;
  }

  public void setTemplate(ConstraintTemplate templ) {
    this.template = templ;
    if (template != null) {
      setUp();
    } else{
        removeAll();
        panels.clear();
        updateUI();
    }
  }

  public void addParameterListener(ParameterPanel.Listener l) {
    parameterListener = l;
  }

  
  private void setUp() {
    removeAll();
    panels.clear();
    setLayout(new GridLayout(template.parameterCount(), 1));
    JLabel name = new JLabel("parameter");
    name.setBorder(BorderFactory.createLineBorder(line));
    JLabel activity = new JLabel("selected activity(ies)");
    activity.setBorder(BorderFactory.createLineBorder(line));
    JLabel select = new JLabel("sect");
    select.setBorder(BorderFactory.createLineBorder(line));
    
    for (Parameter p: template.getParameters()) {    	
      ParameterPanel panel = new ParameterPanel(p,line);
      panel.setListener(this.parameterListener);
      add(panel);
      panels.add(panel);
    }
    updateUI();
  }

  public ParameterPanel get(Parameter formal) {
    ParameterPanel panel = null;
    Iterator<ParameterPanel> iterator = panels.iterator();
    boolean found = false;
    while (iterator.hasNext() && !found) {
      panel = iterator.next();
      found = panel.getFormal().equals(formal);
    }
    if (found) {
      return panel;
    }
    else {
      return null;
    }
  }

  boolean ok() {
    boolean ok = true;
    Iterator<ParameterPanel> iterator = panels.iterator();
    while (iterator.hasNext() && ok) {
      ParameterPanel panel = iterator.next();
      ok = panel.ok();
    }
    return ok;
  }

  public void clear() {
    this.setUp();
  }
}
