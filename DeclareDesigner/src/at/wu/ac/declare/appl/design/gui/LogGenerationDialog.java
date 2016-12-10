package at.wu.ac.declare.appl.design.gui;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import at.wu.ac.declare.appl.design.model.ILogGenerator;
import at.wu.ac.declare.appl.design.model.LogCoordinator;
import nl.tue.declare.appl.util.DefaultFileDialog;

import java.beans.*; //property change stuff
import java.io.File;
import java.text.NumberFormat;
import java.awt.*;
import java.awt.event.*;

public class LogGenerationDialog extends JDialog implements ActionListener {
    private static final Long DEFAULT_LOG_SIZE = new Long(16);
    private static final Integer DEFAULT_TRACE_MIN_LEN = 0;
    private static final Integer DEFAULT_TRACE_MAX_LEN = 8;
	private static final String DEFAULT_EVENT_LOG_TYPE = "xes";
	
	private ILogGenerator logGenerator;
    
    private JLabel mainLbl = new JLabel("Generate a synthetic event log from the drawn declarative process model", SwingConstants.LEFT);
    
	private Long logSize = DEFAULT_LOG_SIZE;
    private JLabel logSizeLbl = new JLabel("Log size:", SwingConstants.LEFT);
    private JFormattedTextField logSizeField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private Container logSizePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private Integer traceMinLen = DEFAULT_TRACE_MIN_LEN;
    private JLabel traceMinLenLbl = new JLabel("Min. trace length:", SwingConstants.LEFT);
    private JFormattedTextField traceMinLenField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private Container traceMinPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private Integer traceMaxLen = DEFAULT_TRACE_MAX_LEN;
    private JLabel traceMaxLenLbl = new JLabel("Max. trace length:", SwingConstants.LEFT);
    private JFormattedTextField traceMaxLenField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private Container traceMaxPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    private String eventLogType = DEFAULT_EVENT_LOG_TYPE;
    private JLabel eventLogTypeLbl = new JLabel("Event log type:");
    private JRadioButton xesBtn = new JRadioButton("XES", true);
    private JRadioButton mxmlBtn = new JRadioButton("MXML");
    private ButtonGroup eventLogTypeGroup = new ButtonGroup();
    private Container eventLogTypePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    private JLabel fileLbl = new JLabel("Event log file path:");
    private JTextField fileField = new JTextField(50);
    private JButton fileBtn = new JButton("File...");
    private Container filePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private JButton genBtn = new JButton("Generate log");
    private JButton undoBtn = new JButton("Undo");
    private Container btnPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

    public LogGenerationDialog(Frame parent, String selectedFile, ILogGenerator logGenerator) {
        super(parent, "Generate event log", false);
        
        if (selectedFile != null) {
        	fileField.setText(selectedFile);
        }
        this.logGenerator = logGenerator;

        Container rootPane = new JPanel();
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.Y_AXIS));

//        rootPane.add(mainLbl);
        
        logSizeLbl.setPreferredSize(new Dimension(150, logSizeLbl.getPreferredSize().height));
        logSizeField.setValue(logSize);
        logSizeField.setColumns(7);
        logSizePnl.add(logSizeLbl);
        logSizePnl.add(logSizeField);
        rootPane.add(logSizePnl);
        
        traceMinLenLbl.setPreferredSize(new Dimension(150, traceMinLenLbl.getPreferredSize().height));
        traceMinLenField.setValue(traceMinLen);
        traceMinLenField.setColumns(4);
        traceMinPnl.add(traceMinLenLbl);
        traceMinPnl.add(traceMinLenField);
        rootPane.add(traceMinPnl);
        
        traceMaxLenLbl.setPreferredSize(new Dimension(150, traceMaxLenLbl.getPreferredSize().height));
        traceMaxLenField.setValue(traceMaxLen);
        traceMaxLenField.setColumns(4);
        traceMaxPnl.add(traceMaxLenLbl);
        traceMaxPnl.add(traceMaxLenField);
        rootPane.add(traceMaxPnl);
        
        eventLogTypeLbl.setPreferredSize(new Dimension(150, eventLogTypeLbl.getPreferredSize().height));
        eventLogTypePnl.add(eventLogTypeLbl);
        eventLogTypeGroup.add(xesBtn);
        eventLogTypePnl.add(xesBtn);
        eventLogTypeGroup.add(mxmlBtn);
        eventLogTypePnl.add(mxmlBtn);
        rootPane.add(eventLogTypePnl);
        
        fileLbl.setPreferredSize(new Dimension(150, eventLogTypeLbl.getPreferredSize().height));
        filePnl.add(fileLbl);
        filePnl.add(fileField);
        filePnl.add(fileBtn);
        rootPane.add(filePnl);
        
        btnPnl.add(genBtn);
        btnPnl.add(undoBtn);
        rootPane.add(btnPnl);

        undoBtn.addActionListener(this);
        genBtn.addActionListener(this);
        xesBtn.addActionListener(this);
        mxmlBtn.addActionListener(this);
        fileBtn.addActionListener(this);

        //Ensure the file button always gets the first focus.
        addComponentListener(new ComponentAdapter() {
        	@Override
            public void componentShown(ComponentEvent ce) {
                fileBtn.requestFocusInWindow();
            }
        });
        
        this.add(rootPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.toFront();
        this.setVisible(false);
    }

    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource() == undoBtn) {
            setVisible(false);
        } else if (e.getSource() == xesBtn) {
        	this.eventLogType = "xes";
        } else if (e.getSource() == mxmlBtn) {
        	this.eventLogType = "mxml";
        } else if (e.getSource() == fileBtn) {
        	// Pick a file

            JFileChooser fileChooser = new JFileChooser(fileField.getText());
            int returnVal = fileChooser.showSaveDialog(this.getParent());
            
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                //This is where a real application would save the file.
        		if (!path.endsWith(eventLogType)) {
        			path += "." + eventLogType;
        		}
                fileField.setText(path);
            }
        } else if (e.getSource() == genBtn) {
        	String inputNok = "";
        	// Save
        	File selectedFile = getSelectedFile();
        	try {
        		this.logSize = Long.valueOf(logSizeField.getText());
        	} catch (NumberFormatException nFE) {
        		inputNok += "Please check the specified input for the log size\n";
        	}
        	try {
        		this.traceMinLen = Integer.valueOf(traceMinLenField.getText());
	    	} catch (NumberFormatException nFE) {
	    		inputNok += "Please check the specified input for the minimum trace length\n";
	    	}
        	try {
	    		this.traceMaxLen = Integer.valueOf(traceMaxLenField.getText());
			} catch (NumberFormatException nFE) {
				inputNok += "Please check the specified input for the maximum trace length\n";
			}
        	if (selectedFile == null) {
        		inputNok += "Please check the specified path for the output file\n";
        	}
        	inputNok = inputNok.trim();
        	if (inputNok.isEmpty()) {
        		this.logGenerator.saveLog(this.logSize, this.traceMinLen, this.traceMaxLen, this.eventLogType, selectedFile);
                setVisible(false);
        	} else {
        		JOptionPane.showMessageDialog(this.getParent(), inputNok.trim(), "Input error", JOptionPane.WARNING_MESSAGE);
        	}
        }
    }
    
    private File getSelectedFile() {
    	if (fileField.getText() == null || fileField.getText().trim().isEmpty()) {
    		return null;
    	}
    	
		File file = new File(fileField.getText());

		if (file.exists()) {
			int response = JOptionPane.showConfirmDialog(null,
				"Overwrite existing file?", "Confirm overwrite",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.CANCEL_OPTION) {
				return null;
			}
		}
		return file;
    }
}