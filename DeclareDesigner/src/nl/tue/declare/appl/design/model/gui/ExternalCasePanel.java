package nl.tue.declare.appl.design.model.gui;

import info.clearthought.layout.TableLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

import yawlservice.ExternalCase;

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
public class ExternalCasePanel
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 3955981775347327508L;
  private JTextField path = new JTextField();
  private JTextField file = new JTextField();
  private JTextField specification = new JTextField();
  private JTextField decomposition = new JTextField();
  
	private static double p = TableLayout.PREFERRED;
	private static double size[][] = {
			{ 100, TableLayout.FILL }, { p, p, p, p } };

  public ExternalCasePanel() {
    super(new TableLayout(size));

    final String defPath = "c:\\Documents and Settings\\All Users\\Desktop\\";
    final String defFile = "test";
    final String defSpec = "test.ywl";
    final String defDec = "mainNet";

    path.setToolTipText("<html>Enter a path YALW specification file.<br>This is a path of a YALW file (e.g., \""
                        + defPath + "\").</html>");

    file = new JTextField() {

		private static final long serialVersionUID = 5312240755748654594L;

	public String getToolTipText(MouseEvent me) {
        String p = path.getText().equals("") ? defPath : path.getText();
        String f = getText().equals("") ? defFile : getText();
        String msg = "<html>Enter a YALW specification file (without extention).<br> This must be a name of an  xml file in the given path. <br> E.g., \""
            + p + f + ".xml\" is a YAWL engine file.</html>";
        return msg;
      }
    };

    specification = new JTextField() {
		private static final long serialVersionUID = 1231519317221023429L;

	public String getToolTipText(MouseEvent me) {
        String p = path.getText().equals("") ? defPath : path.getText();
        String f = file.getText().equals("") ? defFile : file.getText();

        String spec = getText().equals("") ? defSpec : getText();
        String msg = "<html>Enter a YALW specification name.<br> This must be the specification uri in the given file. <br> E.g., \""
            + spec + "\" is the specificayion uri in \"" + p + f +
            ".xml\".</html>";
        return msg;
      }
    };

    decomposition = new JTextField() {

	private static final long serialVersionUID = -4866432837526026476L;

	public String getToolTipText(MouseEvent me) {
        String p = path.getText().equals("") ? defPath : path.getText();
        String f = file.getText().equals("") ? defFile : file.getText();

        String dec = getText().equals("") ? defDec : getText();
        String msg = "<html>Enter a YALW decomposition ID.<br> This must be an decompositon ID in the given specification. <br> E.g., \""
            + dec + "\" is an decomposition ID in \"" + p + f +
            ".xml\".</html>";
        return msg;
      }
    };
    ToolTipManager.sharedInstance().registerComponent(file);
    ToolTipManager.sharedInstance().registerComponent(specification);
    ToolTipManager.sharedInstance().registerComponent(decomposition);

    setGUI();
  }

  private void setGUI(){	  
	add(new JLabel("path"), "0,0");
	add(path, "1,0");

	add(new JLabel("file"), "0,1");
	add(file, "1,1");
	
	add(new JLabel("specification"), "0,2");
	add(specification, "1,2");
	
	add(new JLabel("decomposition"), "0,3");
	add(decomposition, "1,3");
  }

  public void fromExternalCase(ExternalCase ec){
    this.path.setText(ec.getFilePath());
    this.file.setText(ec.getFileName());
    this.specification.setText(ec.getStecificationID());
    this.decomposition.setText(ec.getDecomopositionID());
  }

  public void toExternalCase(ExternalCase ec){
    ec.setFilePath(this.path.getText());
    ec.setFileName(this.file.getText());
    ec.setSpecificationID(this.specification.getText());
    ec.setDecomopositionID(this.decomposition.getText());
  }

}
