package nl.tue.declare.appl.worklist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.tue.declare.appl.util.swing.TPanel;
import nl.tue.declare.appl.worklist.WorklistAssignmentCoordinator;

import org.processmining.operationalsupport.messages.reply.Prediction;
import org.processmining.operationalsupport.messages.reply.Recommendation;
import org.processmining.operationalsupport.messages.reply.ResponseSet;

/**
 * 
 * @author mwesterg
 * 
 */
public class ResponsePanel extends TPanel {
	public static abstract class MonitorActionListener extends QueryActionListener {
		public abstract void monitorRemoved(final int id);

		public void setTabbedPane(final JTabbedPane tabbedPane, final Map<Integer, JPanel> panels,
				final List<Integer> tabToId) {
			synchronized (tabbedPane) {
				tabToId.add(-1);
				assert tabToId.size() == -1;
			}
			tabbedPane.addPropertyChangeListener("__index_to_remove__",
					new PropertyChangeListener() {

						@Override
						public void propertyChange(final PropertyChangeEvent evt) {
							if (evt.getNewValue() != null) {
								synchronized (tabbedPane) {
									final int id = tabToId
											.remove((int) (Integer) evt.getNewValue());
									// We need to do this cast to get the correct remove method
									if (panels.containsKey(id)) {
										monitorRemoved(id);
									}
								}
							}
						}
					});
			tabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(final ChangeEvent e) {
					tabbedPane.setBackgroundAt(tabbedPane.getSelectedIndex(), OK_COLOR);
				}
			});
		}

	}

	public static abstract class QueryActionListener implements ActionListener {
		protected JComboBox textField;

		public String getText() {
			final Object selectedItem = textField.getSelectedItem();
			if (selectedItem != null && selectedItem instanceof QueryItem)
				return ((QueryItem) selectedItem).getFormula();
			if (selectedItem != null && selectedItem instanceof String)
				return (String) selectedItem;
			return "<unknown format> " + selectedItem;
		}

		public void setQueryField(final JComboBox textField) {
			this.textField = textField;
		}
	}

	public static class QueryItem {
		private final String name;
		private final String formula;

		public QueryItem(final String name, final String formula) {
			this.name = name;
			this.formula = formula;
		}

		/**
		 * @return the formula
		 */
		public String getFormula() {
			return formula;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	private class OSPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final JPanel contents;
		JTabbedPane tabs;
		Map<Integer, JPanel> panels;
		Map<Integer, JComponent> components;
		private final List<Integer> tabToId;

		public OSPanel(final QueryActionListener queryActionListener,
				final MonitorActionListener monitorActionListener) {
			components = Collections.synchronizedMap(new HashMap<Integer, JComponent>());
			panels = Collections.synchronizedMap(new HashMap<Integer, JPanel>());
			tabToId = Collections.synchronizedList(new ArrayList<Integer>());
			contents = new JPanel();
			contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
			setLayout(new BorderLayout());
			tabs = new JTabbedPane(SwingConstants.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);
			add(tabs, BorderLayout.CENTER);
			tabs.addTab("Console", new JScrollPane(contents,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			tabs.setBackgroundAt(0, OK_COLOR);
			final JPanel query = new JPanel(new BorderLayout());
			add(query, BorderLayout.SOUTH);
			query.add(new JLabel("Query"), BorderLayout.WEST);
			final JComboBox textField = new JComboBox();
			textField.setEditor(new ComboBoxEditor() {
				JTextField editor = new JTextField();

				@Override
				public void addActionListener(final ActionListener l) {
					editor.removeActionListener(l);
				}

				@Override
				public Component getEditorComponent() {
					return editor;
				}

				@Override
				public Object getItem() {
					return editor.getText();
				}

				@Override
				public void removeActionListener(final ActionListener l) {
					editor.addActionListener(l);

				}

				@Override
				public void selectAll() {
					editor.selectAll();
				}

				@Override
				public void setItem(final Object anObject) {
					if (anObject == null) {
						editor.setText("");
					} else if (anObject instanceof QueryItem) {
						final QueryItem item = (QueryItem) anObject;
						editor.setText(item.getFormula());
					} else {
						editor.setText(anObject.toString());
					}
				}
			});
			textField.setEditable(true);
			query.add(textField, BorderLayout.CENTER);

			final JPanel buttonpanel = new JPanel(new BorderLayout());
			query.add(buttonpanel, BorderLayout.EAST);
			queryActionListener.setQueryField(textField);
			final JButton execute = new JButton("Execute");
			execute.addActionListener(queryActionListener);
			buttonpanel.add(execute, BorderLayout.WEST);
			final JButton monitor = new JButton("Monitor");
			monitorActionListener.setQueryField(textField);
			monitorActionListener.setTabbedPane(tabs, panels, tabToId);
			monitor.addActionListener(monitorActionListener);
			buttonpanel.add(monitor, BorderLayout.EAST);
		}

		public void removeMonitor(final int id) {
			synchronized (tabs) {
				if (components.containsKey(id)) {
					panels.remove(id);
					components.remove(id);
					if (tabToId.contains(id)) {
						tabs.removeTabAt(tabToId.indexOf(id));
						tabToId.remove((Integer) id);
					}
				}
			}
		}

		public void setResult(final int location, final JComponent result) {
			final int position;
			final JComponent component;
			if (location < 0) {
				position = 0;
				component = contents;
			} else {
				synchronized (tabs) {
					if (components.containsKey(location)) {
						position = tabs.indexOfComponent(components.get(location));
					} else {
						position = addNewTab("Monitor " + location, location);
					}
					component = panels.get(location);
				}
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					component.removeAll();
					component.add(result, BorderLayout.CENTER);
					if (position == 0) {
						tabs.setSelectedIndex(0);
					}
					if (tabs.getSelectedIndex() != position) {
						tabs.setBackgroundAt(position, CHANGED_COLOR);
					}
				}
			});
		}

		protected int addNewTab(final String name, final int newId) {
			final JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			final ButtonTabComponent buttonTabComponent = new ButtonTabComponent(tabs);
			final int tabCount;
			synchronized (tabs) {
				tabCount = tabs.getTabCount();
				final JScrollPane scrollPane = new JScrollPane(panel,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				tabs.insertTab(name, null, scrollPane, null, tabCount);
				tabs.setTabComponentAt(tabCount, buttonTabComponent);
				tabToId.add(newId);
				assert tabToId.get(tabCount).equals(newId);
				panels.put(newId, panel);
				components.put(newId, scrollPane);
			}

			buttonTabComponent.setMaximumSize(new Dimension(200, (int) buttonTabComponent
					.getMaximumSize().getHeight()));
			final Dimension d = new Dimension(buttonTabComponent.getPreferredSize());
			if (d.getWidth() > 200) {
				d.setSize(200, d.getHeight());
			}
			buttonTabComponent.setPreferredSize(d);
			return tabCount;
		}
	}

	public static final Color OK_COLOR = new Color(144, 238, 144);

	public static final Color CHANGED_COLOR = new Color(255, 230, 38);

	public static final Color FAILURE_COLOR = new Color(205, 91, 69);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4936361359815382915L;
	private final RecommendationVisualizer responseVisualizer;
	private final OSPanel simple;
	private final OSPanel compare;
	private final OSPanel recommend;
	private final OSPanel predict;

	private final JTabbedPane tabs;

	public ResponsePanel(final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		super(new BorderLayout(), "Operational support");
		responseVisualizer = new RecommendationVisualizer();
		tabs = new JTabbedPane(SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		add(tabs);
		simple = new OSPanel(createSimple(worklistAssignmentCoordinator),
				createSimpleMonitor(worklistAssignmentCoordinator));
		tabs.addTab("Simple", simple);
		compare = new OSPanel(createCompare(worklistAssignmentCoordinator),
				createCompareMonitor(worklistAssignmentCoordinator));
		tabs.addTab("Compare", compare);
		predict = new OSPanel(createPredict(worklistAssignmentCoordinator),
				createPredictMonitor(worklistAssignmentCoordinator));
		tabs.addTab("Predict", predict);
		recommend = new OSPanel(createRecommend(worklistAssignmentCoordinator),
				createRecommendMonitor(worklistAssignmentCoordinator));
		tabs.addTab("Recommend", recommend);
		for (int i = 0; i < tabs.getTabCount(); i++) {
			tabs.setBackgroundAt(i, OK_COLOR);
		}
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				tabs.setBackgroundAt(tabs.getSelectedIndex(), OK_COLOR);
			}
		});

	}

	/**
	 * 
	 * @param result
	 *            RecommendationResult
	 */
	public void deliverResult(final int id, final ResponseSet<?> result) {
		if (result != null && (result.iterator().hasNext() || result.failures().iterator().hasNext())) {
			final Class<?> resultClass = result.computeCommonAncestor();
			if (Prediction.class.isAssignableFrom(resultClass)) {
				predict.setResult(id, responseVisualizer.getVisualizationPanel(result));
				if (tabs.getSelectedIndex() != 2) {
					tabs.setBackgroundAt(2, CHANGED_COLOR);
				}
			} else if (Recommendation.class.isAssignableFrom(resultClass)) {
				recommend.setResult(id, responseVisualizer.getVisualizationPanel(result));
				if (tabs.getSelectedIndex() != 3) {
					tabs.setBackgroundAt(3, CHANGED_COLOR);
				}
			} else {
				simple.setResult(id, responseVisualizer.getVisualizationPanel(result));
				if (tabs.getSelectedIndex() != 0) {
					tabs.setBackgroundAt(0, CHANGED_COLOR);
				}
			}
			revalidate();
		}
	}

	public void removeMonitor(final int id) {
		simple.removeMonitor(id);
		compare.removeMonitor(id);
		predict.removeMonitor(id);
		recommend.removeMonitor(id);
	}

	private QueryActionListener createCompare(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new QueryActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.compare(getText());
				}
			}
		};
	}

	private MonitorActionListener createCompareMonitor(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new MonitorActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.compareMonitor(getText());
				}
			}

			@Override
			public void monitorRemoved(final int id) {
				worklistAssignmentCoordinator.removeMonitor(id);
			}
		};
	}

	private QueryActionListener createPredict(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new QueryActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.predict(getText());
				}
			}
		};
	}

	private MonitorActionListener createPredictMonitor(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new MonitorActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.predictMonitor(getText());
				}
			}

			@Override
			public void monitorRemoved(final int id) {
				worklistAssignmentCoordinator.removeMonitor(id);
			}
		};
	}

	private QueryActionListener createRecommend(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new QueryActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.recommend(getText());
				}
			}
		};
	}

	private MonitorActionListener createRecommendMonitor(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new MonitorActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.recommendMonitor(getText());
				}
			}

			@Override
			public void monitorRemoved(final int id) {
				worklistAssignmentCoordinator.removeMonitor(id);
			}
		};
	}

	private QueryActionListener createSimple(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new QueryActionListener() {

			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.simple(getText());
				}
			}

			@Override
			public void setQueryField(final JComboBox textField) {
				super.setQueryField(textField);
				textField
						.addItem(new QueryItem(
								"List all events",
								"for $e in //event return <item>{ $e/concept:name/text() }.{ $e/lifecycle:transition/text() }</item>"));
				textField.addItem(new QueryItem("Detailed event information", "//event"));
				textField
						.addItem(new QueryItem(
								"Timing information",
								"<html><table><tr><th>Duration</th><td>{ xs:dateTime(//event[last()]/time:timestamp/text()) - xs:dateTime(//event[position()=1]/time:timestamp/text()) }</td></tr><tr><th>Total duration</th><td>{ fn:current-dateTime() - xs:dateTime(//event[position()=1]/time:timestamp/text()) }</td></tr></table></html>"));
				textField
						.addItem(new QueryItem(
								"Timing information, Geocities edition",
								"<html><img src=\"http://westergaard.eu/wp-content/uploads/2010/06/logo1.png\"/><h1>Michael says...</h1><table><tr><th><font color=\"red\">Duration</font></th><td>{ xs:dateTime(//event[last()]/time:timestamp/text()) - xs:dateTime(//event[position()=1]/time:timestamp/text()) }</td></tr><tr><th>Total duration</th><td>{ fn:current-dateTime() - xs:dateTime(//event[position()=1]/time:timestamp/text()) }</td></tr></table></html>"));
		

				// changed the previous XQuery about timing information. The
				// results now is in minutes

				textField
						.addItem(new QueryItem(
								"New Timing information",
								"<html><table><tr><th>Duration</th><td>{ round-half-to-even((xs:dayTimeDuration(xs:dateTime(//event[last()]/time:timestamp/text()) "
										+ "- xs:dateTime(//event[position()=1]/time:timestamp/text())) div xs:dayTimeDuration('PT1M')),2) }</td></tr><tr><th>"
										+ "Total duration</th><td>{round-half-to-even((xs:dayTimeDuration( fn:current-dateTime() - xs:dateTime(//event[position()=1]/time:timestamp/text())) "
										+ "div xs:dayTimeDuration('PT1M')),2)}</td></tr></table></html>"));

				// list all complete events in the partial trace

				textField
						.addItem(new QueryItem(
						"List all complete events",
					"for $y in distinct-values(//event/concept:name/text()) where //event/lifecycle:transition/text() = 'complete' return <item> {$y} </item>"));
				
						
				// list all the resources in the partial trave

				textField
						.addItem(new QueryItem(
								"List all resources",
								"for $t in distinct-values(//event/org:resource/text()) return <item>{ $t }</item>"));

				// list the service time per event in the trace
				textField
						.addItem(new QueryItem(
								"Service time per activity",
								"for $e in //event, $y in //event where "
										+ "$e/concept:instance = $y/concept:instance "
										+ " and $e/concept:name = $y/concept:name and "
										+ "$e/lifecycle:transition/text()='complete' and $y/lifecycle:transition/text() = 'start'"
										+ "return  <item><time> {round-half-to-even((xs:dayTimeDuration(xs:dateTime( $e/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2)}</time><activity>{$e/concept:name/text()} </activity></item>"));

				textField
						.addItem(new QueryItem(
								"Service time per event",
								" for $t in distinct-values(//event/concept:name)"
										+ " return <item><time>{sum( for $y in //event ,$ft in //event "
										+ "where $ft/concept:instance = $y/concept:instance"
										+ " and $ft/concept:name = $t and $y/concept:name = $t and "
										+ "$ft/lifecycle:transition/text() eq 'complete' and $y/lifecycle:transition/text() eq 'start'"
										+ " return round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2))}</time><resource>{$t}</resource></item>"));

				// list the worktime per resource for each activity

				textField
						.addItem(new QueryItem(
								"Worktime per resource for each activity",
								"for $e in //event, $y in //event where "
										+ "$e/concept:instance = $y/concept:instance and  "
										+ "$e/concept:name/text() eq $y/concept:name/text()"
										+ "and $e/lifecycle:transition/text() eq 'complete' and $y/lifecycle:transition/text() eq 'start'"
										+ "return  <item><time> {round-half-to-even((xs:dayTimeDuration(xs:dateTime( $e/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2)}</time><activity> {$e/concept:name/text()}"
										+ "</activity><resource>{$e/org:resource/text()}</resource></item>"));

				// sum of all the service times per activity

				textField.addItem(new QueryItem(
						"Summation of Service times",
						"<html><table><tr><th> Sum of Service times: </th><td> {" +
						"sum(for $y in //event , $ft in //event where "
						+ "$ft/concept:instance = $y/concept:instance and $ft/concept:name = $y/concept:name and $ft/lifecycle:transition/text()= 'complete' and $y/lifecycle:transition/text()= 'start'"
						+ "return  round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
						+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2))} </td></tr></table></html>"));
				// waiting time for the entire case (this is calculated as the
				// difference between the duration, i.e., the last event seen
				// minus the last event
				// and the summation of the individual service times for each
				// activity)

				textField
						.addItem(new QueryItem(
								"Waiting time for entire case",
								"<html><table><tr><th>Waiting Case time:</th><td>{(round-half-to-even((xs:dayTimeDuration(xs:dateTime(//event[last()]/time:timestamp/text()) "
										+ "- xs:dateTime(//event[position()=1]/time:timestamp/text())) div xs:dayTimeDuration('PT1M')),2) )-"
										+ "( sum(for $y in //event , $ft in //event where "
										+ "$ft/concept:instance = $y/concept:instance and $ft/concept:name = $y/concept:name and $ft/lifecycle:transition/text()= 'complete' and $y/lifecycle:transition/text()= 'start'"
										+ " return  round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2)))} </td></tr></table></html>"));

				// average execution time for the entire case

				textField
						.addItem(new QueryItem(
								"Average Execution time for entire case",
								"<html><table><tr><th>Average Execution time:</th><td>{ avg(for $y in //event , $ft in //event where "
										+ "$ft/concept:name = $y/concept:name and "
										+ " $ft/concept:instance = $y/concept:instance "
										+ "and $ft/lifecycle:transition/text()= 'complete' and $y/lifecycle:transition/text()= 'start'"
										+ "return  round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2)) }</td></tr></table></html>"));

				// sum of worktime per resource (consider all the activities
				// that each resource executed)

				textField
						.addItem(new QueryItem(
								"Sum of Worktime per resource for all activities",
								" for $t in distinct-values(//event/org:resource/text())"
										+ "return <item> <time>{sum( for $y in //event ,$ft in //event where "
										+ "$ft/concept:instance = $y/concept:instance and  "
										+ " $ft/concept:name = $y/concept:name/text()"
										+ " and $ft/org:resource = $t and $y/org:resource = $t and "
										+ "$ft/lifecycle:transition/text()= 'complete' and $y/lifecycle:transition/text() = 'start'"
										+ "return  round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2))}</time><resource>{$t}</resource></item>"));

				// average worktime per resource

				textField
						.addItem(new QueryItem(
								"Average Worktime per resource for all activities",
								" for $t in distinct-values(//event/org:resource/text())"
										+ "return <item> <time>{avg( for $y in //event ,$ft in //event where "
										+ " $ft/concept:instance = $y/concept:instance and "
										+ " $ft/concept:name = $y/concept:name"
										+ " and $ft/org:resource = $t and $y/org:resource = $t and "
										+ "$ft/lifecycle:transition/text() = 'complete' and $y/lifecycle:transition/text() = 'start'"
										+ "return  round-half-to-even((xs:dayTimeDuration(xs:dateTime( $ft/time:timestamp/text()) -"
										+ " (xs:dateTime($y/time:timestamp/text()))) div xs:dayTimeDuration('PT1M')),2))}</time><resource>{$t}</resource> </item>"));

			
			}
		};
	}

	private MonitorActionListener createSimpleMonitor(
			final WorklistAssignmentCoordinator worklistAssignmentCoordinator) {
		return new MonitorActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!getText().isEmpty()) {
					worklistAssignmentCoordinator.simpleMonitor(getText());
				}
			}

			@Override
			public void monitorRemoved(final int id) {
				worklistAssignmentCoordinator.removeMonitor(id);
			}
		};
	}

}
