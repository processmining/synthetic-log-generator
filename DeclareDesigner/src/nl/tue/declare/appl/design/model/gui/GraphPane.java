package nl.tue.declare.appl.design.model.gui;

/*
 * @(#)GraphEd.java 3.3 23-APR-04
 *
 * Copyright (c) 2001-2004, Gaudenz Alder All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import org.jgraph.*;
import org.jgraph.event.*;
import org.jgraph.graph.*;
import nl.tue.declare.utils.*;

public class GraphPane extends JPanel implements GraphSelectionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4713673051931721601L;

	GraphListener graphListener = null;

	private final String INSERT_ICON = "insert.gif";
	private final String CONNECT_ICON = "connecton.gif";

	private final String COPY_ICON = "copy.gif";
	private final String PASTE_ICON = "paste.gif";
	private final String CUT_ICON = "cut.gif";
	private final String DELETE_ICON = "delete.gif";

	// private final String UNDO_ICON = "undo.gif";
	// private final String REDO_ICON = "redo.gif";

	private final String TOFRONT_ICON = "tofront.gif";
	private final String TOBACK_ICON = "toback.gif";

	private final String ZOOM_ICON = "zoom.gif";
	private final String ZOOMIN_ICON = "zoomin.gif";
	private final String ZOOMOUT_ICON = "zoomout.gif";

	private final String GROUP_ICON = "group.gif";
	private final String UNGROUP_ICON = "ungroup.gif";

	// JGraph instance
	protected JGraph graph;

	// Undo Manager
	protected GraphUndoManager undoManager;

	// Actions which Change State
	protected Action undo, redo, remove, group, ungroup, tofront, toback, cut,
			copy, paste;

	// cell count that gets put in cell label
	protected int cellCount = 0;

	// Status Bar
	protected EdStatusBar statusBar;

	private String language;

	//
	//
	// Editor Panel
	//

	// Construct an Editor Panel
	public GraphPane(GraphListener aGraphListener, JGraph aGraph,
			String language) {
		// instal the graph listener
		graphListener = aGraphListener;

		graph = aGraph;

		// Use a Custom Marquee Handler
		graph.setMarqueeHandler(createMarqueeHandler());

		this.language = language;

		// Construct Command History
		//
		// Create a GraphUndoManager which also Updates the ToolBar
		undoManager = new GraphUndoManager() {

			private static final long serialVersionUID = -5129678902440021445L;

			// Override Superclass
			public void undoableEditHappened(UndoableEditEvent e) {
				// First Invoke Superclass
				super.undoableEditHappened(e);
				// Then Update Undo/Redo Buttons
				updateHistoryButtons();
			}
		};
		populateContentPane();
		installListeners(graph);
	}

	// Hook for subclassers
	protected void populateContentPane() {
		// Use Border Layout
		setLayout(new BorderLayout());
		// Add a ToolBar

		JPanel temp = new JPanel(new BorderLayout());
		temp.add(createToolBar(), BorderLayout.WEST);
		add(temp, BorderLayout.NORTH);
		// Add the Graph as Center Component
		add(new JScrollPane(graph), BorderLayout.CENTER);
	}

	// Hook for subclassers
	protected void installListeners(JGraph graph) {
		// Add Listeners to Graph
		//
		// Register UndoManager with the Model
		graph.getModel().addUndoableEditListener(undoManager);
		// Update ToolBar based on Selection Changes
		graph.getSelectionModel().addGraphSelectionListener(this);
		// Listen for Delete Keystroke when the Graph has Focus
		graph.addKeyListener(this);
		graph.getModel().addGraphModelListener(statusBar);
	}

	// Hook for subclassers
	protected void uninstallListeners(JGraph graph) {
		graph.getModel().removeUndoableEditListener(undoManager);
		graph.getSelectionModel().removeGraphSelectionListener(this);
		graph.removeKeyListener(this);
		graph.getModel().removeGraphModelListener(statusBar);
	}

	// Hook for subclassers
	protected BasicMarqueeHandler createMarqueeHandler() {
		return new MyMarqueeHandler();
	}

	// Insert a new Vertex at point
	public void insert(Point2D point) {
		if (graphListener != null) {
			graphListener.insertVertex(point);
		}
	}

	// Hook for subclassers
	public Map<Object, Object> createCellAttributes(Point2D point) {
		Map<Object, Object> map = new Hashtable<Object, Object>();
		// Snap the Point to the Grid
		if (graph != null) {
			point = graph.snap((Point2D) point.clone());
		} else {
			point = (Point2D) point.clone();
		}
		// Add a Bounds Attribute to the Map
		GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(),
				point.getY(), 0, 0));
		return map;
	}

	// Hook for subclassers
	protected DefaultGraphCell createDefaultGraphCell() {
		DefaultGraphCell cell = new DefaultGraphCell("Cell "
				+ new Integer(cellCount++));
		// Add one Floating Port
		cell.addPort();
		return cell;
	}

	// Create a Group that Contains the Cells
	public void group(Object[] cells) {
		// Order Cells by Model Layering
		cells = graph.order(cells);
		// If Any Cells in View
		if (cells != null && cells.length > 0) {
			DefaultGraphCell group = createGroupCell();
			// Insert into model
			graph.getGraphLayoutCache().insertGroup(group, cells);
		}
	}

	// Hook for subclassers
	protected DefaultGraphCell createGroupCell() {
		return new DefaultGraphCell();
	}

	// Returns the total number of cells in a graph
	protected int getCellCount(JGraph graph) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		return cells.length;
	}

	// Ungroup the Groups in Cells and Select the Children
	public void ungroup(Object[] cells) {
		graph.getGraphLayoutCache().ungroup(cells);
	}

	// Determines if a Cell is a Group
	public boolean isGroup(Object cell) {
		// Map the Cell to its View
		CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
		if (view != null) {
			return !view.isLeaf();
		}
		return false;
	}

	// Brings the Specified Cells to Front
	public void toFront(Object[] c) {
		graph.getGraphLayoutCache().toFront(c);
	}

	// Sends the Specified Cells to Back
	public void toBack(Object[] c) {
		graph.getGraphLayoutCache().toBack(c);
	}

	// Undo the last Change to the Model or the View
	public void undo() {
		try {
			undoManager.undo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Redo the last Change to the Model or the View
	public void redo() {
		try {
			undoManager.redo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Update Undo/Redo Button State based on Undo Manager
	protected void updateHistoryButtons() {
		// The View Argument Defines the Context
		// undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
		// redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
	}

	//
	// Listeners
	//

	// From GraphSelectionListener Interface
	public void valueChanged(GraphSelectionEvent e) {
		// Group Button only Enabled if more than One Cell Selected
		group.setEnabled(graph.getSelectionCount() > 1);
		// Update Button States based on Current Selection
		boolean enabled = !graph.isSelectionEmpty();
		remove.setEnabled(enabled);
		ungroup.setEnabled(enabled);
		tofront.setEnabled(enabled);
		toback.setEnabled(enabled);
		copy.setEnabled(enabled);
		cut.setEnabled(enabled);
	}

	//
	// KeyListener for Delete KeyStroke
	//
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// Listen for Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			// Execute Remove Action on Delete Key Press
			remove.actionPerformed(null);
		}
	}

	//
	// Custom Edge Handle
	//

	// Defines a EdgeHandle that uses the Shift-Button (Instead of the Right
	// Mouse Button, which is Default) to add/remove point to/from an edge.
	public static class MyEdgeHandle extends EdgeView.EdgeHandle {

		/**
	 * 
	 */
		private static final long serialVersionUID = -4791937071623997342L;

		/**
		 * 
		 * @param edge
		 *            EdgeView
		 * @param ctx
		 *            GraphContext
		 */
		public MyEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
		}

		// Override Superclass Method
		public boolean isAddPointEvent(MouseEvent event) {
			// Points are Added using Shift-Click
			return event.isShiftDown();
		}

		// Override Superclass Method
		public boolean isRemovePointEvent(MouseEvent event) {
			// Points are Removed using Shift-Click
			return event.isShiftDown();
		}

	}

	//
	// Custom Model
	//

	// A Custom Model that does not allow Self-References
	public static class MyModel extends DefaultGraphModel {
		/**
	 * 
	 */
		private static final long serialVersionUID = -7775842083454100665L;

		// Override Superclass Method
		public boolean acceptsSource(Object edge, Object port) {
			// Source only Valid if not Equal Target
			return (((Edge) edge).getTarget() != port);
		}

		// Override Superclass Method
		public boolean acceptsTarget(Object edge, Object port) {
			// Target only Valid if not Equal Source
			return (((Edge) edge).getSource() != port);
		}
	}

	//
	// Custom MarqueeHandler

	// MarqueeHandler that Connects Vertices and Displays PopupMenus
	public class MyMarqueeHandler extends BasicMarqueeHandler {

		// Holds the Start and the Current Point
		protected Point2D start, current;

		// Holds the First and the Current Port
		protected PortView port, firstPort;

		// Override to Gain Control (for PopupMenu and ConnectMode)
		public boolean isForceMarqueeEvent(MouseEvent e) {
			if (e.isShiftDown()) {
				return false;
			}
			// If Right Mouse Button we want to Display the PopupMenu
			if (SwingUtilities.isRightMouseButton(e)) {
				// Return Immediately
				return true;
			}
			
			if (e.getClickCount() == 2){
				return true;
			}
			// Find and Remember Port
			port = getSourcePortAt(e.getPoint());
			// If Port Found and in ConnectMode (=Ports Visible)
			if (port != null && graph.isPortsVisible()) {
				return true;
			}
			// Else Call Superclass
			return super.isForceMarqueeEvent(e);
		}

		// Display PopupMenu or Remember Start Location and First Port
		public void mousePressed(final MouseEvent e) {
			// If Right Mouse Button
			if (SwingUtilities.isRightMouseButton(e)) {
				// Find Cell in Model Coordinates
				Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
				// Create PopupMenu for the Cell
				JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
				// Display PopupMenu
				menu.show(graph, e.getX(), e.getY());
			} else if (e.getClickCount() == 2) { // edit cell on double click
				Object cell = graph.getFirstCellForLocation(e.getX(), e
						.getY());
				if (graphListener != null) {
					graphListener.edit(cell);
				}
			} else if (port != null && graph.isPortsVisible()) {
				// Remember Start Location
				start = graph.toScreen(port.getLocation());
				// Remember First Port
				firstPort = port;				
			} else {
				// Call Superclass
				super.mousePressed(e);
			}
		}

		// Find Port under Mouse and Repaint Connector
		public void mouseDragged(MouseEvent e) {
			// If remembered Start Point is Valid
			if (start != null) {
				// Fetch Graphics from Graph
				Graphics g = graph.getGraphics();
				// Reset Remembered Port
				PortView newPort = getTargetPortAt(e.getPoint());
				// Do not flicker (repaint only on real changes)
				if (newPort == null || newPort != port) {
					// Xor-Paint the old Connector (Hide old Connector)
					paintConnector(Color.black, graph.getBackground(), g);
					// If Port was found then Point to Port Location
					port = newPort;
					if (port != null) {
						current = graph.toScreen(port.getLocation());
					}
					// Else If no Port was found then Point to Mouse Location
					else {
						current = graph.snap(e.getPoint());
					}
					// Xor-Paint the new Connector
					paintConnector(graph.getBackground(), Color.black, g);
				}
			}
			// Call Superclass
			super.mouseDragged(e);
		}

		public PortView getSourcePortAt(Point2D point) {
			// Disable jumping
			graph.setJumpToDefaultPort(false);
			PortView result;
			try {
				// Find a Port View in Model Coordinates and Remember
				result = graph.getPortViewAt(point.getX(), point.getY());
			} finally {
				graph.setJumpToDefaultPort(true);
			}
			return result;
		}

		// Find a Cell at point and Return its first Port as a PortView
		protected PortView getTargetPortAt(Point2D point) {
			return graph.getPortViewAt(point.getX(), point.getY());
		}

		// Connect the First Port and the Current Port in the Graph or Repaint
		public void mouseReleased(MouseEvent e) {
			firstPort = port = null;
			start = current = null;
			super.mouseReleased(e);
		}

		// Show Special Cursor if Over Port
		public void mouseMoved(MouseEvent e) {
			// Check Mode and Find Port
			if (e != null && getSourcePortAt(e.getPoint()) != null
					&& graph.isPortsVisible()) {
				// Set Cusor on Graph (Automatically Reset)
				graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
				// Consume Event
				// Note: This is to signal the BasicGraphUI's
				// MouseHandle to stop further event processing.
				e.consume();
			} else {
				// Call Superclass
				super.mouseMoved(e);
			}
		}

		// Use Xor-Mode on Graphics to Paint Connector
		protected void paintConnector(Color fg, Color bg, Graphics g) {
			// Set Foreground
			g.setColor(fg);
			// Set Xor-Mode Color
			g.setXORMode(bg);
			// Highlight the Current Port
			paintPort(graph.getGraphics());
			// If Valid First Port, Start and Current Point
			if (firstPort != null && start != null && current != null) {
				// Then Draw A Line From Start to Current Point
				g.drawLine((int) start.getX(), (int) start.getY(),
						(int) current.getX(), (int) current.getY());
			}
		}

		// Use the Preview Flag to Draw a Highlighted Port
		protected void paintPort(Graphics g) {
			// If Current Port is Valid
			if (port != null) {
				// If Not Floating Port...
				boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
				// ...Then use Parent's Bounds
				Rectangle2D r = (o) ? port.getBounds() : port.getParentView()
						.getBounds();
				// Scale from Model to Screen
				r = graph.toScreen((Rectangle2D) r.clone());
				// Add Space For the Highlight Border
				r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
						.getHeight() + 6);
				// Paint Port in Preview (=Highlight) Mode
				graph.getUI().paintCell(g, port, r, true);
			}
		}

	} // End of Editor.MyMarqueeHandler

	//
	//
	//

	//
	// PopupMenu and ToolBar
	//

	//
	//
	//

	//
	// PopupMenu
	//
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new JPopupMenu();
		if (cell != null) {
			// Edit
			menu.add(new AbstractAction("Edit") {

				private static final long serialVersionUID = -7443962183954077812L;

				public void actionPerformed(ActionEvent e) {
					if (graphListener != null) {
						graphListener.edit(cell);
					}
				}
			});
		}
		// Remove
		if (!graph.isSelectionEmpty()) {
			menu.addSeparator();
			menu.add(new AbstractAction("Remove") {

				private static final long serialVersionUID = 4697457628164658420L;

				public void actionPerformed(ActionEvent e) {
					remove.actionPerformed(e);
				}
			});
		}
		menu.addSeparator();
		// Insert
		menu.add(new AbstractAction("Insert") {

			private static final long serialVersionUID = -6948983936928359630L;

			public void actionPerformed(ActionEvent ev) {
				insert(pt);
			}
		});
		return menu;
	}

	//
	// ToolBar
	//
	public JToolBar createToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		// Insert

		URL insertUrl = URLLoader.loadResource(INSERT_ICON);

		ImageIcon insertIcon = new ImageIcon(insertUrl);
		toolbar.add(new AbstractAction("", insertIcon) {

			private static final long serialVersionUID = -5117615659829419980L;

			public void actionPerformed(ActionEvent e) {
				insert(new Point(10, 10));
			}
		});
		toolbar.addSeparator();
		// Toggle Connect Mode
		// URL connectUrl = URLLoader.loadURL(urlPath, CONNECT_ICON);
		URL connectUrl = URLLoader.loadResource(CONNECT_ICON);
		ImageIcon connectIcon = new ImageIcon(connectUrl);
		toolbar.add(new JButton(new AbstractAction(language, connectIcon) {

			private static final long serialVersionUID = 5495134695798327187L;

			public void actionPerformed(ActionEvent e) {
				if (graphListener != null) {
					graphListener.addConstraint();
				}
			}
		}));
		/*
		 * toolbar.add(new ToggleButton(new AbstractAction(language,
		 * connectIcon) { public void actionPerformed(ActionEvent e) {
		 * graph.setPortsVisible(!graph.isPortsVisible()); URL connectUrl =
		 * URLLoader.loadResource(CONNECT_ICON); ImageIcon connectIcon = new
		 * ImageIcon(connectUrl); putValue(SMALL_ICON, connectIcon); } }) );
		 */

		/*
		 * JPanel temp = new JPanel(new BorderLayout(2, 2)); toolbar.add(temp);
		 * temp.setSize(temp.getSize().width, 100);
		 * 
		 * temp.add(combo, BorderLayout.CENTER);
		 */
		//
		//
		// Edit Block
		//
		toolbar.addSeparator();
		Action action;
		URL url;

		// Copy
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getCopyAction();
		url = URLLoader.loadResource(COPY_ICON);
		;
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		copy = new EventRedirector(action);
		// MAJA toolbar.add(copy);

		// Paste
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getPasteAction();
		url = URLLoader.loadResource(PASTE_ICON);
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		paste = new EventRedirector(action);
		// MAJA toolbar.add(paste);

		// Cut
		action = javax.swing.TransferHandler // JAVA13:
				// org.jgraph.plaf.basic.TransferHandler
				.getCutAction();
		url = URLLoader.loadResource(CUT_ICON);
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		cut = new EventRedirector(action);
		// MAJA toolbar.add(cut);

		// Remove
		URL removeUrl = URLLoader.loadResource(DELETE_ICON);
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		remove = new AbstractAction("", removeIcon) {

			private static final long serialVersionUID = -3987505407429469551L;

			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					cells = graph.getDescendants(cells);
					// MAJA graph.getModel().remove(cells);
					if (graphListener != null) {
						graphListener.remove(cells);
					}
				}
			}
		};
		remove.setEnabled(false);
		toolbar.add(remove);

		// To Front
		toolbar.addSeparator();
		URL toFrontUrl = URLLoader.loadResource(TOFRONT_ICON);
		ImageIcon toFrontIcon = new ImageIcon(toFrontUrl);
		tofront = new AbstractAction("", toFrontIcon) {

			private static final long serialVersionUID = -5290200815996308318L;

			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					toFront(graph.getSelectionCells());
				}
			}
		};
		tofront.setEnabled(false);
		toolbar.add(tofront);

		// To Back
		URL toBackUrl = URLLoader.loadResource(TOBACK_ICON);
		ImageIcon toBackIcon = new ImageIcon(toBackUrl);
		toback = new AbstractAction("", toBackIcon) {

			private static final long serialVersionUID = -4027039198493265353L;

			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					toBack(graph.getSelectionCells());
				}
			}
		};
		toback.setEnabled(false);
		toolbar.add(toback);

		// Zoom Std
		toolbar.addSeparator();
		URL zoomUrl = URLLoader.loadResource(ZOOM_ICON);
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		toolbar.add(new AbstractAction("", zoomIcon) {

			private static final long serialVersionUID = 861452646598000974L;

			public void actionPerformed(ActionEvent e) {
				graph.setScale(1.0);
			}
		});
		// Zoom In
		URL zoomInUrl = URLLoader.loadResource(ZOOMIN_ICON);
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		toolbar.add(new AbstractAction("", zoomInIcon) {

			private static final long serialVersionUID = 6586618824506861199L;

			public void actionPerformed(ActionEvent e) {
				graph.setScale(2 * graph.getScale());
			}
		});
		// Zoom Out
		URL zoomOutUrl = URLLoader.loadResource(ZOOMOUT_ICON);
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		toolbar.add(new AbstractAction("", zoomOutIcon) {

			private static final long serialVersionUID = -7487439466122195858L;

			public void actionPerformed(ActionEvent e) {
				graph.setScale(graph.getScale() / 2);
			}
		});

		// Group
		toolbar.addSeparator();
		URL groupUrl = URLLoader.loadResource(GROUP_ICON);
		ImageIcon groupIcon = new ImageIcon(groupUrl);
		group = new AbstractAction("", groupIcon) {

			private static final long serialVersionUID = -6665586773523676432L;

			public void actionPerformed(ActionEvent e) {
				group(graph.getSelectionCells());
			}
		};
		group.setEnabled(false);

		// Ungroup
		URL ungroupUrl = URLLoader.loadResource(UNGROUP_ICON);
		ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
		ungroup = new AbstractAction("", ungroupIcon) {

			private static final long serialVersionUID = -7056946200912711852L;

			public void actionPerformed(ActionEvent e) {
				ungroup(graph.getSelectionCells());
			}
		};
		ungroup.setEnabled(false);

		return toolbar;
	}

	/**
	 * @return Returns the graph.
	 */
	public JGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 *            The graph to set.
	 */
	public void setGraph(JGraph graph) {
		this.graph = graph;
	}

	// This will change the source of the actionevent to graph.
	public class EventRedirector extends AbstractAction {

		private static final long serialVersionUID = -5756507929018022815L;
		protected Action action;

		// Construct the "Wrapper" Action
		public EventRedirector(Action a) {
			super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
			this.action = a;
		}

		// Redirect the Actionevent
		public void actionPerformed(ActionEvent e) {
			e = new ActionEvent(graph, e.getID(), e.getActionCommand(), e
					.getModifiers());
			action.actionPerformed(e);
		}
	}

	/**
	 * Create a status bar
	 * 
	 * @return JPanel
	 */
	protected JPanel createStatusBar() {
		return new EdStatusBar();
	}

	/**
	 * 
	 * @return a String representing the version of this application
	 */
	protected String getVersion() {
		return JGraph.VERSION;
	}

	/**
	 * @return Returns the redo.
	 */
	public Action getRedo() {
		return redo;
	}

	/**
	 * @param redo
	 *            The redo to set.
	 */
	public void setRedo(Action redo) {
		this.redo = redo;
	}

	/**
	 * @return Returns the undo.
	 */
	public Action getUndo() {
		return undo;
	}

	/**
	 * @param undo
	 *            The undo to set.
	 */
	public void setUndo(Action undo) {
		this.undo = undo;
	}

	public class EdStatusBar extends JPanel implements GraphModelListener {
		/**
	 * 
	 */
		private static final long serialVersionUID = 270492585394520466L;

		/**
     *
     */
		protected JLabel leftSideStatus;

		/**
		 * contains the scale for the current graph
		 */
		protected JLabel rightSideStatus;

		/**
		 * Constructor for GPStatusBar.
		 * 
		 */
		public EdStatusBar() {
			super();
			// Add this as graph model change listener
			setLayout(new BorderLayout());
			leftSideStatus = new JLabel(getVersion());
			rightSideStatus = new JLabel("0/0Mb");
			leftSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
			rightSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
			add(leftSideStatus, BorderLayout.CENTER);
			add(rightSideStatus, BorderLayout.EAST);
		}

		/**
		 * Graph Model change event
		 * 
		 * @param e
		 *            GraphModelEvent
		 */
		public void graphChanged(GraphModelEvent e) {
			updateHeapStatus();
		}

		protected void updateHeapStatus() {
			Runtime runtime = Runtime.getRuntime();
			int freeMemory = (int) (runtime.freeMemory() / 1024);
			int totalMemory = (int) (runtime.totalMemory() / 1024);
			int usedMemory = (totalMemory - freeMemory);
			String str = (usedMemory / 1024) + "/" + (totalMemory / 1024)
					+ "Mb";
			rightSideStatus.setText(str);
		}

		/**
		 * @return Returns the leftSideStatus.
		 */
		public JLabel getLeftSideStatus() {
			return leftSideStatus;
		}

		/**
		 * @param leftSideStatus
		 *            The leftSideStatus to set.
		 */
		public void setLeftSideStatus(JLabel leftSideStatus) {
			this.leftSideStatus = leftSideStatus;
		}

		/**
		 * @return Returns the rightSideStatus.
		 */
		public JLabel getRightSideStatus() {
			return rightSideStatus;
		}

		/**
		 * @param rightSideStatus
		 *            The rightSideStatus to set.
		 */
		public void setRightSideStatus(JLabel rightSideStatus) {
			this.rightSideStatus = rightSideStatus;
		}
	}

	/**
	 * @return Returns the copy.
	 */
	public Action getCopy() {
		return copy;
	}

	/**
	 * @param copy
	 *            The copy to set.
	 */
	public void setCopy(Action copy) {
		this.copy = copy;
	}

	/**
	 * @return Returns the cut.
	 */
	public Action getCut() {
		return cut;
	}

	/**
	 * @param cut
	 *            The cut to set.
	 */
	public void setCut(Action cut) {
		this.cut = cut;
	}

	/**
	 * @return Returns the paste.
	 */
	public Action getPaste() {
		return paste;
	}

	/**
	 * @param paste
	 *            The paste to set.
	 */
	public void setPaste(Action paste) {
		this.paste = paste;
	}

	/**
	 * @return Returns the toback.
	 */
	public Action getToback() {
		return toback;
	}

	/**
	 * @param toback
	 *            The toback to set.
	 */
	public void setToback(Action toback) {
		this.toback = toback;
	}

	/**
	 * @return Returns the tofront.
	 */
	public Action getTofront() {
		return tofront;
	}

	/**
	 * @param tofront
	 *            The tofront to set.
	 */
	public void setTofront(Action tofront) {
		this.tofront = tofront;
	}

	/**
	 * @return Returns the remove.
	 */
	public Action getRemove() {
		return remove;
	}

	/**
	 * @param remove
	 *            The remove to set.
	 */
	public void setRemove(Action remove) {
		this.remove = remove;
	}
}
