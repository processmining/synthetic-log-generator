package nl.tue.declare.appl.verification;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import nl.tue.declare.appl.util.FrameUtil;
import nl.tue.declare.appl.util.swing.*;
/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
import nl.tue.declare.verification.*;

public class VerificationPanel extends JPanel{

	private static final long serialVersionUID = 4995218758642589308L;
	private JList errors = new JList();
	private ViolationGroupTable constraints = new ViolationGroupTable();
	private TPanel constraintsPanel;

	public VerificationPanel() {
		super();
		errors.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				VerificationError error = (VerificationError) errors.getSelectedValue();
				if (error != null) {
					constraints.set(error.getGroup()); // display the
														// constraints that
														// cause the selected
														// error
					constraintsPanel.setTitle(error+ " due to constraints:");
				}				
			}});

		JPanel verificationPanel = new TPanel(new BorderLayout(), "Errors");
		verificationPanel.add(new JScrollPane(this.errors,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		constraintsPanel = new TPanel(new BorderLayout(),
				"this error is caused by constraints");
		constraintsPanel.add(new JScrollPane(this.constraints,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		setLayout(new BorderLayout());
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, verificationPanel,
				constraintsPanel), BorderLayout.CENTER);
	}

	public void fromVerificationResult(VerificationResult result) {
		FrameUtil.fillList(result, errors);
		if (result != null) {
			if (result.size() > 0) {
				errors.getSelectionModel().setSelectionInterval(0, 0);
			}
		}
	}

	public void clear() {
		FrameUtil.clearList(errors);
		constraints.clear();
		this.updateUI();
		this.revalidate();
	}
}
