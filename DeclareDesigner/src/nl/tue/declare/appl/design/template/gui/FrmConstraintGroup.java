package nl.tue.declare.appl.design.template.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.template.*;

public class FrmConstraintGroup
    extends OkCancelDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = -238533349178843938L;
   private JTextField name = new JTextField();
  private JEditorPane description = new JEditorPane();


  public FrmConstraintGroup(Frame parent, String title, Container aMonitorFrame) {
    super(parent, title, aMonitorFrame);
  }

  public FrmConstraintGroup(Frame parent, Container aMonitorFrame) {
    super(parent, "", aMonitorFrame);
  }
  
  public void fromGroup(ConstraintGroup group){
	  if (group != null){
		  name.setText(group.getName());
		  description.setText(group.getDescription());
	  } else{
		  name.setText("");
		  description.setText("");
	  }
  }
  
  public void toGroup(ConstraintGroup group){
	  if (group != null){
		  group.setName(name.getText());
		  group.setDescription(description.getText());
	  }
  }


@Override
protected Component getContent() {
    JPanel main = new TPanel(new BorderLayout());

    JPanel labels = new JPanel(new BorderLayout());
    labels.add(new JLabel("name "), BorderLayout.NORTH);
    JPanel dsc = new JPanel(new BorderLayout());
    dsc.add(new JLabel("description "), BorderLayout.NORTH);
    labels.add(dsc, BorderLayout.CENTER);

    JPanel components = new JPanel(new BorderLayout());
    components.add(name, BorderLayout.NORTH);
    components.add(new JScrollPane(description), BorderLayout.CENTER);

    main.add(labels, BorderLayout.WEST);
    main.add(components, BorderLayout.CENTER);
    return main;
}
}
