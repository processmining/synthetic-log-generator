package nl.tue.declare.appl.design.template.gui;

import info.clearthought.layout.TableLayout;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalBorders;

import nl.tue.declare.appl.design.template.ILTLSyntaxCheckListener;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.TPanel;
import nl.tue.declare.appl.util.swing.TTextArea;
import nl.tue.declare.domain.instance.State;
import nl.tue.declare.domain.template.*;

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
public class FrmTemplate extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -636672720402403876L;

	private JButton jButtonParseLTL = new JButton("check syntax");
	private JTextField name = new JTextField();
	private ParametersPanel parameters = new ParametersPanel();
	private JTextField display = new JTextField();
	private JTextArea description = new TTextArea();
	private JTextArea formula = new TTextArea();
	private JTextArea stateMessage = new TTextArea();
	private JList states = new JList();
	private HTMLLabel messagePanel = new HTMLLabel();
	private ILTLSyntaxCheckListener syntaxListener = null;
	private FrmStateMessage frmStateMessage;
	private JButton btnEditStateMessage = new JButton("edit message");

	private static double p = TableLayout.PREFERRED;
	private static double size[][] = { { 150, TableLayout.FILL },
			{ p, p, p, 10, p, p, 10, p, p, p, TableLayout.FILL } };
	private TableLayout layout = new TableLayout(size);

	private HashMap<State, String> stateMessages = new HashMap<State, String>();

	public FrmTemplate(Frame parent, JInternalFrame aMonitorFrame) {
		super(parent, "Constraint template", (Container) aMonitorFrame);
		frmStateMessage = new FrmStateMessage(this);
		btnEditStateMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editStateMessage();
			}

		});
		states.setBorder(new CompoundBorder(new BasicBorders.MarginBorder(),
				new MetalBorders.TextFieldBorder()));
		// fill JList with all possible state types
		states.removeAll();
		FrameUtil.fillList((Object[]) State.values(), this.states);
		for (State s : State.values()) {
			stateMessages.put(s, "");
		}
		states.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		jButtonParseLTL.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FrmTemplate.this.checkSyntaxNotify();

			}
		});
	}

	private void editStateMessage() {
		Object selected = FrmTemplate.this.states.getSelectedValue();
		if (selected instanceof State) {
			State state = (State) selected;
			frmStateMessage.fromState(state, stateMessages.get(state));
			if (frmStateMessage.showCentered()) {
				String msg = frmStateMessage.getMessage();
				stateMessages.put(state,msg);
				messagePanel.setText(msg);
			}
		}
	}

	public void fromTemplate(ConstraintTemplate template) {
		String name = "";
		String formulaBody = "";
		String description = "";
		String display = "";

		if (template != null) {
			name = template.getName();
			formulaBody = template.getText();
			description = template.getDescription();
			display = template.getDisplay();
			for (State s : State.values()) {
				this.stateMessages.put(s,template.getStateMessage(s));
			}
		}

		parameters.from(template);
		this.name.setText(name);
		this.display.setText(display);
		this.formula.setText(formulaBody);
		this.description.setText(description);
		pack();
	}

	public void setNumberParameters(int nrParameters) {

	}

	public void toTemplate(ConstraintTemplate template) {
		if (template != null) {
			template.setName(name.getText());
			template.setDescription(description.getText());
			template.setText(formula.getText());
			template.setDisplay(display.getText());
			parameters.to(template);
			for (State s : State.values()) {
				template.setStateMessage(s, this.stateMessages.get(s));
			}
		}
	}

	public int parametersCount() {
		return parameters.count();
	}

	/**
	 * checkSyntax
	 */
	private void checkSyntaxNotify() {
		if (this.syntaxListener != null) {
			this.syntaxListener.checkSyntaxNotify(formula.getText());
		}
	}

	public void addSyntaxCheckListener(ILTLSyntaxCheckListener l) {
		this.syntaxListener = l;
	}

	@Override
	protected Component getContent() {
		JPanel main = new JPanel(layout);
		main.add(new JLabel("name"), "0,0");
		main.add(name, "1,0");
		main.add(new JLabel("display label"), "0,1");
		main.add(display, "1,1");

		main.add(parameters, "0, 2, 1, 2");

		main.add(new JLabel("description"), "0,4");
		main.add(description, "0,5,1,5");

		main.add(new JLabel("formula"), "0,7");
		main.add(formula, "0,8,1,8");

		main.add(this.jButtonParseLTL, "0,9");

		final JPanel statesPanel = new TPanel(new BorderLayout(),
				"state messages");

		FrameUtil.readOnly(this.stateMessage, main);

		JPanel temp = new JPanel(new BorderLayout());
		temp.add(btnEditStateMessage, BorderLayout.WEST);

		statesPanel.add(new JScrollPane(states,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.WEST);
		statesPanel.add(messagePanel, BorderLayout.CENTER);
		statesPanel.add(temp, BorderLayout.SOUTH);

		states.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				Object o = FrmTemplate.this.states.getSelectedValue();
				if (o instanceof State) {
					messagePanel.setText(FrmTemplate.this.stateMessages.get((State) o));
				}
			}
		});
		// select the first state
		State s = State.SATISFIED;
		states.setSelectedValue(s, true);
		
		messagePanel.setText(stateMessages.get(s));
		main.add(statesPanel, "0,10,1,10");

		return main;
	}

	/**
	 * 
	 * @author mpesic
	 * 
	 */
	private class HTMLLabel extends JLabel {

		private static final long serialVersionUID = -8937415564774141937L;
		
		public HTMLLabel(){
			super();
		    Border in = new BasicBorders.MarginBorder();
		    Border out = new MetalBorders.TextFieldBorder();
		    setBorder(BorderFactory.createCompoundBorder(out,in));
		}

		public void setText(String text) {
			String html = new String(text);
			if (!html.contains("<html")) {
				html = "<html>" + html + "</html>";
			}
			super.setText(html);
		}
	}

	/**
	 * 
	 * @author mpesic
	 * 
	 */
	private class FrmStateMessage extends OkCancelDialog {

		private static final long serialVersionUID = -2100358121020751651L;
		private HTMLLabel html;
		private JTextArea message;

		public FrmStateMessage(Dialog parent) {
			super(parent, "");
			message = new TTextArea(10, 20);

			html = new HTMLLabel() {
				private static final long serialVersionUID = 1L;

				public Dimension getPreferredSize() {
					return new Dimension(200, 200);
				}

				public Dimension getMinimumSize() {
					return new Dimension(200, 200);
				}

				public Dimension getMaximumSize() {
					return new Dimension(200, 200);
				}
			};
			html.setVerticalAlignment(SwingConstants.CENTER);
			html.setHorizontalAlignment(SwingConstants.CENTER);

			message.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {
					// ignore this
				}

				public void keyReleased(KeyEvent e) {
					html.setText(message.getText());
				}

				public void keyTyped(KeyEvent e) {
					// ignore this
				}

			});
		}

		@Override
		protected Component getContent() {
			JPanel leftPanel = new TPanel(new BorderLayout(), "Message");
			leftPanel.add(new JScrollPane(message), BorderLayout.CENTER);

			JPanel rightPanel = new TPanel(new BorderLayout(), "HTML");
			rightPanel.add(html, BorderLayout.CENTER);

			return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel,
					rightPanel);
		}

		void fromState(State state, String msg) {
			message.setText(msg);
			html.setText(message.getText());
			setTitle("Enter message for state \"" + state.name() + "\"");
		}

		String getMessage() {
			return message.getText();
		}

	}
}
