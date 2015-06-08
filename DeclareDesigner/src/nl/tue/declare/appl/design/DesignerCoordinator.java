package nl.tue.declare.appl.design;

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
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import nl.tue.declare.appl.design.gui.*;
import nl.tue.declare.appl.design.gui.MainFrame;
import nl.tue.declare.appl.design.model.AssignmentCoordinator;
import nl.tue.declare.appl.design.model.AssignmentCoordinatorListener;
import nl.tue.declare.appl.design.organization.OrganizationCoordinator;
import nl.tue.declare.appl.design.template.ConstraintsTemplateCoordinator;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.template.*;

public class DesignerCoordinator implements AssignmentCoordinatorListener {

	private static final int KEY_QUIT = KeyEvent.VK_Q;
	private static final int KEY_OPEN_MODEL = KeyEvent.VK_O;
	private static final int KEY_SAVE_MODEL = KeyEvent.VK_S;
	private static final int KEY_MODEL_PROPERTIES = KeyEvent.VK_P;
	private static final int KEY_MODEL_EXPORT_IMAGE = KeyEvent.VK_I;
	private static final int KEY_MODEL_VERIFY = KeyEvent.VK_V;
	private static final int KEY_ORGANIZATION = KeyEvent.VK_R;
	private static final int KEY_TEMPLATES = KeyEvent.VK_C;

	// this coordinator handles all events with all assignment models
	private AssignmentCoordinator assignmentCoordinator;

	/**
	 * mainFarme is the main frame for the Designer. Within mainFrame internal
	 * frames are open.
	 */
	private MainFrame mainFrame = null;

	private JDesktopPane desktop;

	private JMenuItem save, saveAs, properties, export, verify;

	final Control control;

	private WindowMenu frames;

	public DesignerCoordinator() {
		control = Control.singleton();
		mainFrame = new MainFrame();
		frames = new WindowMenu();
		frames.addListener(new WindowMenuListener() {
			public void itemSelected(JInternalFrame frame, boolean active) {
				if (active) {
					maximize(frame);
				}
			}
		});
		assignmentCoordinator = new AssignmentCoordinator(mainFrame);
		assignmentCoordinator.addListener(this);
		start();
		assignmentCoordinator.init();
	}

	/**
	 * start
	 */
	private void start() {

		boolean packFrame = false;

		// Set up the GUI.
		desktop = new JDesktopPane(); // a specialized layered pane
		mainFrame.setContentPane(desktop);
		mainFrame.setJMenuBar(createMenuBar());

		// Make dragging a little faster but perhaps uglier.
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// Validate frames that have preset sizes
		// Pack frames that have useful preferred size info, e.g. from their
		// layout
		if (packFrame) {
			mainFrame.pack();
		} else {
			mainFrame.validate();
		}

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = mainFrame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		mainFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		mainFrame.setVisible(true);
	}

	/**
	 * organization
	 */
	public void organization() {
	}

	/**
	 * maximize
	 * 
	 * @param aFrame
	 *            aFrame Maximize an internal frame aFrame.
	 */
	private void maximize(JInternalFrame aFrame) {
		try {
			aFrame.setVisible(true);
			aFrame.setSelected(true);
			aFrame.setMaximum(true);
		} catch (Exception e) {
		}
	}

	/**
	 * jMenuOrganizationRoles_actionPerformed
	 * 
	 * @param actionEvent
	 *            actionEvent actionEvent Execute this method when user selects
	 *            to work with "organization" Create the form for Organization
	 *            and start the organizationCoordinator
	 */
	void jMenuOrganizationRoles_actionPerformed(ActionEvent actionEvent) {
		boolean first = !OrganizationCoordinator.exists();
		OrganizationCoordinator coordinator = OrganizationCoordinator.singleton(mainFrame);
		JInternalFrame frame = coordinator.getInternalFrame();
		if (first) {
			frame.addInternalFrameListener(new InternalFrameListener() {
				public void internalFrameDeactivated(InternalFrameEvent e) {
					frames.activate(false, e.getInternalFrame());
				}

				public void internalFrameActivated(InternalFrameEvent e) {
					frames.activate(true, e.getInternalFrame());
				}

				public void internalFrameDeiconified(InternalFrameEvent e) {
				}

				public void internalFrameIconified(InternalFrameEvent e) {
				}

				public void internalFrameClosing(InternalFrameEvent e) {
				}

				public void internalFrameOpened(InternalFrameEvent e) {
				}

				public void internalFrameClosed(InternalFrameEvent e) {
					OrganizationCoordinator.finish();
					frames.remove(e.getInternalFrame());
				}
			});
			frames.add(true, frame);
			desktop.add(frame);
		}
		this.maximize(frame);
		coordinator.start();
	}

	/**
	 * jMenuAssignment_actionPerformed
	 * 
	 * @param actionEvent
	 *            actionEvent actionEvent Execute this method when user selects
	 *            to work with "assignment" Create the form for Organization and
	 *            start the organizationCoordinator
	 */
	void jMenuConstraintTemplate_actionPerformed(ActionEvent actionEvent) {
		boolean first = !ConstraintsTemplateCoordinator.exists();
		ConstraintsTemplateCoordinator coordinator = ConstraintsTemplateCoordinator.singleton(mainFrame);
		JInternalFrame frame = coordinator.getInternalFrame();
		if (first) {
			frame.addInternalFrameListener(new InternalFrameListener() {
				public void internalFrameDeactivated(InternalFrameEvent e) {
					frames.activate(false, e.getInternalFrame());
				}

				public void internalFrameActivated(InternalFrameEvent e) {
					frames.activate(true, e.getInternalFrame());
				}

				public void internalFrameDeiconified(InternalFrameEvent e) {
				}

				public void internalFrameIconified(InternalFrameEvent e) {
				}

				public void internalFrameClosing(InternalFrameEvent e) {
				}

				public void internalFrameOpened(InternalFrameEvent e) {
				}

				public void internalFrameClosed(InternalFrameEvent e) {
					ConstraintsTemplateCoordinator.finish();
					frames.remove(e.getInternalFrame());
				}
			});
			frames.add(true, frame);
			desktop.add(frame);
		}
		this.maximize(frame);
		coordinator.start();
	}

	void jMenuVerifyModel_actionPerformed(ActionEvent actionEvent) {
		if (assignmentCoordinator != null) {
			try {
				assignmentCoordinator.verify();
			} catch (Throwable ex) {
				ErrorMessage.showError(this.mainFrame, ex, "Verification failed.");
			}
			frames.frameChanged(assignmentCoordinator.getFrame());
		}
	}

	/**
	 * frameClosed / /* public void frameClosed() { internalFrame = null; }
	 * 
	 * @param actionEvent
	 *            ActionEvent
	 */
	void childClose_actionPerformed(ActionEvent actionEvent) {
	}

	private void setMenuKey(JMenuItem menuItem, int key) {
		menuItem.setMnemonic(key);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.ALT_MASK));
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Set up the lone menu.
		JMenu jMenuAssignment = new JMenu("Model");
		jMenuAssignment.setMnemonic(KeyEvent.VK_P);
		menuBar.add(jMenuAssignment);

		// Set up the menu item for new model
		jMenuAssignment.add(newItem());

		// Set up the menu item for open model
		{
			JMenuItem menuItem = new JMenuItem("Open");
			setMenuKey(menuItem, KEY_OPEN_MODEL);
			menuItem.setActionCommand("open");
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (assignmentCoordinator != null) {
						JInternalFrame frame = assignmentCoordinator.open();
						if (frame != null) {
							desktop.add(frame);
							assignmentCoordinator.activateModel(frame);
							frames.add(true, frame);
						}
					}
				}
			});
			jMenuAssignment.add(menuItem);
		}
		{
			JMenuItem menuItem = new JMenuItem("Generate Model");
			setMenuKey(menuItem, KEY_OPEN_MODEL);
			menuItem.setActionCommand("generate");
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ParameterSettings().show();
				}
			});
			jMenuAssignment.add(menuItem);
		}

		// Set up the menu item for save model
		this.save = new JMenuItem("Save");
		setMenuKey(this.save, KEY_SAVE_MODEL);
		this.save.setActionCommand("save");
		this.save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// jMenuSaveModel_actionPerformed(e);
				if (assignmentCoordinator != null) {
					assignmentCoordinator.save();
					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(this.save);

		// Set up the menu item for saveAs model
		this.saveAs = new JMenuItem("Save as");
		this.saveAs.setActionCommand("saveAs");
		this.saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (assignmentCoordinator != null) {
					assignmentCoordinator.saveAs();
					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(this.saveAs);

		// Set up the menu item for properties of the model
		this.properties = new JMenuItem("Properties");
		setMenuKey(this.properties, KEY_MODEL_PROPERTIES);
		this.properties.setActionCommand("properties");
		this.properties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (assignmentCoordinator != null) {
					assignmentCoordinator.properties();
					frames.frameChanged(assignmentCoordinator.getFrame());
				}
			}
		});

		jMenuAssignment.add(this.properties);

		// Set up the menu item for properties of the model
		this.verify = new JMenuItem("Verify");
		setMenuKey(this.verify, KEY_MODEL_VERIFY);
		this.verify.setActionCommand("verify");
		this.verify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuVerifyModel_actionPerformed(e);
			}
		});

		jMenuAssignment.add(this.verify);

		// Set up the menu item image exporting
		this.export = this.exportItem();
		jMenuAssignment.add(export);

		jMenuAssignment.addSeparator();

		// Set up the second menu item.
		JMenuItem menuItem = new JMenuItem("Quit");
		setMenuKey(menuItem, KEY_QUIT);
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		jMenuAssignment.add(menuItem);

		// setup the Design menu item
		JMenu jMenuDesign = new JMenu();
		jMenuDesign.setText("System");
		menuBar.add(jMenuDesign);

		// setup the Organization menu item
		JMenuItem jMenuOrganization = new JMenuItem();
		setMenuKey(jMenuOrganization, KEY_ORGANIZATION);
		jMenuOrganization.setText("Organization");
		jMenuOrganization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuOrganizationRoles_actionPerformed(e);
			}
		});

		jMenuDesign.add(jMenuOrganization);

		// setup the ConstraintTemplate menu item
		JMenuItem jMenuConstraintTemplate = new JMenuItem();
		setMenuKey(jMenuConstraintTemplate, KEY_TEMPLATES);
		jMenuConstraintTemplate.setText("Constraint templates");
		jMenuConstraintTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuConstraintTemplate_actionPerformed(e);
			}
		});

		jMenuDesign.add(jMenuConstraintTemplate);

		// setop window menu item
		menuBar.add(frames);

		// setup the Help menu item
		JMenu jMenuHelp = new JMenu();
		jMenuHelp.setText("Help");
		menuBar.add(jMenuHelp);

		// setup the About menu item
		JMenuItem jMenuHelpAbout = new JMenuItem();
		jMenuHelpAbout.setText("About");
		jMenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuHelpAbout_actionPerformed(e);
			}
		});

		jMenuHelp.add(jMenuHelpAbout);

		return menuBar;
	}

	private JMenu newItem() {
		// Set up the menu item for new model
		JMenu menu = new JMenu("New");
		Iterator<Language> iterator = control.getConstraintTemplate().getLanguages().iterator();
		while (iterator.hasNext()) {
			final Language lang = iterator.next();
			JMenuItem item = new JMenuItem(lang.getName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (assignmentCoordinator != null) {
						JInternalFrame frame = assignmentCoordinator.newModel(lang);
						desktop.add(frame);
						assignmentCoordinator.activateModel(frame);
						frames.add(true, frame);
					}
				}
			});
			menu.add(item);
		}
		return menu;
	}

	private JMenu exportItem() {
		// Set up the menu item for new model
		JMenu menu = new JMenu("Export");

		// Set up the menu item image exporting
		JMenuItem exportImage = new JMenuItem("As image...");
		setMenuKey(exportImage, KEY_MODEL_EXPORT_IMAGE);
		exportImage.setActionCommand("export_image");
		exportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (assignmentCoordinator != null) {
					assignmentCoordinator.exportAsImage();
				}
			}
		});
		menu.add(exportImage);

		// Set up the menu item ProM exporting
		JMenuItem exportProM = new JMenuItem("To ProM...");
		exportProM.setActionCommand("export_prom");
		exportProM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (assignmentCoordinator != null) {
					assignmentCoordinator.exportAsProM();
				}
			}
		});
		menu.add(exportProM);

		return menu;
	}

	void jMenuHelpAbout_actionPerformed(ActionEvent actionEvent) {
		MainFrame_AboutBox dlg = new MainFrame_AboutBox(mainFrame);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = mainFrame.getSize();
		Point loc = mainFrame.getLocation();
		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		dlg.setModal(true);
		dlg.pack();
		dlg.setVisible(true);
	}

	private void setModelMenuItems(boolean enabled) {
		this.save.setEnabled(enabled);
		this.saveAs.setEnabled(enabled);
		this.properties.setEnabled(enabled);
		this.verify.setEnabled(enabled);
		this.export.setEnabled(enabled);
	}

	public void deactivated(JInternalFrame frame) {
		this.setModelMenuItems(false);
		frames.activate(false, frame);
	}

	public void activated(JInternalFrame frame) {
		this.setModelMenuItems(true);
		frames.activate(true, frame);
	}

	public void closed(JInternalFrame frame) {
		frames.remove(frame);
	}
}
