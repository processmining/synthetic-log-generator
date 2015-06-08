package nl.tue.declare.appl.verification;

import java.awt.*;

import javax.swing.*;

import nl.tue.declare.appl.util.CloseDialog;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.verification.*;

public class VerificationGUI {

	private FrmVerification frmVerification;
	private Container monitor;

	public VerificationGUI(JFrame frame, Container monitor) {
		super();
		this.monitor = monitor;
		frmVerification = new FrmVerification(frame, monitor);
	}

	public boolean verify(IVerification verification) throws Throwable {
		return verify(verification, true);
	}

	public boolean verify(IVerification verification, boolean informOk)
			throws Throwable {
		VerificationResult result = verification.verify();
		boolean ok = result.isEmpty();
		if (ok) {
			if (informOk) {
				MessagePane.inform(this.monitor, "No errors were detected.");
			}
		} else {
			frmVerification.setResult(result);
			frmVerification.showCentered();
		}
		return ok;
	}

	private class FrmVerification extends CloseDialog {

		private static final long serialVersionUID = 4427610411091354266L;
		private VerificationPanel panel = new VerificationPanel();

		public FrmVerification(Frame parent, Container aMonitorFrame) {
			super(parent, "", aMonitorFrame);
		}

		void setResult(VerificationResult result) {
			panel.fromVerificationResult(result);
			this.setTitle(result.toString());
		}

		@Override
		protected Component getContent() {
			return panel;
		}

	}

}
