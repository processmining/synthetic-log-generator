package nl.tue.declare.appl.worklist.gui;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.appl.worklist.IAssignmentExecutionListener;

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
public class ActivitiesPane extends JTabbedPane {

	private static final long serialVersionUID = -7198934072145347253L;

	public ActivitiesPane() {
		super();
		this.setUI(new ActivitiesTabbedPaneUI());
	}

	/**
	 * Adds a new tab for the activity
	 * 
	 * @param activity
	 *            Activity
	 */
	public void addActivity(WorkItem workItem,
			IAssignmentExecutionListener listener) {
		if (workItem != null) {
			ManualWorkItemPanel tab = new ManualWorkItemPanel(workItem);
			tab.setListener(listener);		
			addTab(workItem,tab);
		}
	}


	private void addTab(WorkItem item, WorkItemPanel panel) {
		String title = "[" + item.getIdString() + "] "
				+ item.getActivity().getName();
		this.add(title, panel);
		this.setSelectedIndex(this.getTabCount() - 1);
		this.validate();
	}

	/**
	 * 
	 * @param workItem
	 *            WorkItem
	 */
	public void removeWorkItem(WorkItem workItem) {
		if (workItem != null) {
			this.remove(this.getTab(workItem));
		}
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 * @return ActivityPanel
	 */
	private Collection<WorkItemPanel> getTab(Activity activity) {
		Collection<WorkItemPanel> panels = new ArrayList<WorkItemPanel>();
		// Get number of tabs
		int count = getTabCount();
		WorkItemPanel tab = null;
		int i = 0;
		while (i < count) {
			// Get component associated with tab
			Component component = getComponentAt(i++);
			if (component != null) {
				if (component instanceof WorkItemPanel) {
					tab = (WorkItemPanel) component;
					if (tab.getWorkItem().getActivity() == activity) {
						panels.add(tab);
					}
				}
			}
		}
		return panels;
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 * @return ActivityPanel
	 */
	private WorkItemPanel getTab(WorkItem workItem) {
		WorkItemPanel panel = null;
		// Get number of tabs
		int count = getTabCount();
		WorkItemPanel tab = null;
		int i = 0;
		while (i < count) {
			// Get component associated with tab
			Component component = getComponentAt(i++);
			if (component != null) {
				if (component instanceof WorkItemPanel) {
					tab = (WorkItemPanel) component;
					if (tab.getWorkItem() == workItem) {
						panel = tab;
					}
				}
			}
		}
		return panel;
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 * @param enabled
	 *            boolean
	 */
	public void setEnabledComplete(Activity activity, boolean enabled) {
		Iterator<WorkItemPanel> iterator = this.getTab(activity).iterator();
		if (iterator != null) {
			while (iterator.hasNext()) {
				WorkItemPanel tab = iterator.next();
				if (tab instanceof ManualWorkItemPanel) {
					((ManualWorkItemPanel) tab).setEnabledComplete(enabled);
				}
			}
		}
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 * @param enabled
	 *            boolean
	 */
	public void setEnabledCancel(Activity activity, boolean enabled) {
		Iterator<WorkItemPanel> iterator = this.getTab(activity).iterator();
		if (iterator != null) {
			while (iterator.hasNext()) {
				WorkItemPanel tab = iterator.next();
				if (tab instanceof ManualWorkItemPanel) {
					((ManualWorkItemPanel) tab).setEnabledCancel(enabled);
				}
			}
		}
	}

	/**
   *
   */
	public void disableAll() {
		// Get number of tabs
		int count = getTabCount();
		ManualWorkItemPanel tab = null;
		for (int i = count - 1; i >= 0; i--) {
			// Get component associated with tab
			Component component = getComponentAt(i);
			if (component != null) {
				if (component instanceof ManualWorkItemPanel) {
					tab = (ManualWorkItemPanel) component;
					tab.setEnabledComplete(false);
					tab.setEnabledCancel(false);
				}
			}
		}
	}

	/**
	 * Resets the UI property to a value from the current look and feel.
	 * 
	 * @see JComponent#updateUI
	 */

	public void updateUI() {
		setUI(new ActivitiesTabbedPaneUI());
	}

	/**
	 * 
	 * @param index
	 *            int
	 * @return boolean
	 */
	public boolean enabledAt(int index) {
		boolean enabled = false;
		if (index < this.getComponentCount()) {
			Component component = getComponentAt(index);
			if (component != null) {
				if (component instanceof ManualWorkItemPanel) {
					enabled = ((ManualWorkItemPanel) component)
							.getEnabledComplete();
				}
			}
		}
		return enabled;
	}

	/**
	 * 
	 * @param id
	 *            int
	 * @return WorkItem
	 */
	public WorkItem getPanel(int id) {
		boolean found = false;
		int i = 0;
		WorkItem result = null;
		while ((i < this.getComponentCount()) && !found) {
			Component component = getComponentAt(i++);
			if (component != null) {
				if (component instanceof WorkItemPanel) {
					WorkItem current = ((WorkItemPanel) component)
							.getWorkItem();
					found = current.getId() == id;
					if (found) {
						result = current;
					}
				}
			}
		}
		return result;
	}

	public boolean readData(WorkItem item) {
		boolean ok = true;
		WorkItemPanel panel = this.getTab(item);
		if (panel instanceof ManualWorkItemPanel) {
			ok = ((ManualWorkItemPanel) panel).readData();
		}
		return ok;
	}
}
