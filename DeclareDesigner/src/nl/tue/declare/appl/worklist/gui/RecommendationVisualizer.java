package nl.tue.declare.appl.worklist.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.impl.XEventImpl;
import org.processmining.operationalsupport.messages.reply.DiscretePrediction;
import org.processmining.operationalsupport.messages.reply.Prediction;
import org.processmining.operationalsupport.messages.reply.Recommendation;
import org.processmining.operationalsupport.messages.reply.ResponseSet;
import org.processmining.operationalsupport.messages.reply.ResponseSet.Failure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author mwesterg
 *
 */
public class RecommendationVisualizer {

	public static class EventCellRenderer extends DefaultTableCellRenderer implements
			TableCellRenderer {
		private final EventComponent helper = new EventComponent();
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object object,
				final boolean isSelected, final boolean hasFocus, final int row, final int column) {
			super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
			helper.update((XEvent) object);
			setText(helper.getText());
			setToolTipText(helper.getToolTipText());
			return this;
		}
	}

	public static class EventComponent extends JLabel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EventComponent() {

		}

		public EventComponent(final XEvent event) {
			update(event);
		}

		protected void update(final XEvent event) {
			final String name = XConceptExtension.instance().extractName(event);
			if (name == null) {
				setText("Unnamed event");
				return;
			}
			final String transition = XLifecycleExtension.instance().extractTransition(event);
			if (transition == null) {
				setText(name);
			} else {
				setText(name + "." + transition);
			}

			final Map<String, XAttribute> shortNames = simplifyAttributeMap(event);

			final String result = computeTooltip(shortNames);

			if (!result.isEmpty()) {
				setToolTipText(result);
			}
		}

		private String computeTooltip(final Map<String, XAttribute> shortNames) {
			if (shortNames.isEmpty()) return "";
			final StringBuilder sb = new StringBuilder();
			sb.append("<html><table>");
			for (final Entry<String, XAttribute> entry : shortNames.entrySet()) {
				sb.append("<tr><th align=\"right\">");
				sb.append(firstToUpper(entry.getKey()));
				sb.append(":</th><td>");
				sb.append(entry.getValue().toString());
				sb.append("</td></tr>");
			}
			sb.append("</table></html>");
			return sb.toString();
		}

		private Map<String, XAttribute> simplifyAttributeMap(final XEvent event) {
			final Map<String, XAttribute> shortNames = new TreeMap<String, XAttribute>();
			final Map<String, Entry<String, XAttribute>> longNames = new HashMap<String, Entry<String, XAttribute>>();
			for (final Entry<String, XAttribute> entry : event.getAttributes().entrySet()) {
				if (!(entry.getKey().equals(XConceptExtension.KEY_NAME) || entry.getKey().equals(
						XLifecycleExtension.KEY_TRANSITION))) {
					final String shortName = entry.getKey().replaceFirst(".*:", "");
					if (longNames.containsKey(shortName)) {
						shortNames.put(entry.getKey(), entry.getValue());
						shortNames.remove(shortName);
						shortNames.put(longNames.get(shortName).getKey(), longNames.get(shortName)
								.getValue());
					} else {
						shortNames.put(shortName, entry.getValue());
						longNames.put(shortName, entry);
					}
				}
			}
			return shortNames;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String firstToUpper(final String entry) {
		if (entry.isEmpty()) return entry;
		return entry.substring(0, 1).toUpperCase() + entry.substring(1);
	}

	private static String print(final Document document) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			serializer.transform(new DOMSource(document), new StreamResult(baos));
			return new String(baos.toByteArray());
		} catch (final Exception e) {
			return e.toString();
		}
	}

	public JComponent createEventVisualizer(final XEvent event) {
		return new EventComponent(event);
	}

	public JPanel createHeadlinedComponent(final String headerText, final JComponent component) {
		final JPanel result = new JPanel(new BorderLayout());
		result.add(component, BorderLayout.CENTER);
		final JLabel header = new JLabel(headerText);
		header.setFont(header.getFont().deriveFont(15.0f).deriveFont(Font.BOLD));
		result.add(header, BorderLayout.NORTH);
		return result;
	}

	public JComponent createVisualizationPanel(final Document document) {
		if (!checkNodeAndNamespace(document.getDocumentElement(), "sequence",
				"http://saxon.sf.net/xquery-results")) return new JTextArea(print(document));
		final List<Element> elements = getChildElements(document.getDocumentElement());
		for (final Element e : elements)
			if (!checkNodeAndNamespace(e, "element", "http://saxon.sf.net/xquery-results"))
				return new JTextArea(print(document));
		if (elements.size() == 1) { // Check for HTML
			final NodeList html = elements.get(0).getElementsByTagName("html");
			if (html.getLength() != 1) return new JTextArea(print(document));
			return new JLabel(print(html.item(0)));
		} else { // Check for list or table
			if (elements.size() == 0) return new JTextArea(print(document));
			final Element realElement = getLastParent(elements.get(0));
			final List<Element> childElements = getRealChildElements(realElement);

			for (final Element e : elements) {
				final Element f = getLastParent(e);
				if (!checkNodeAndNamespace(f, realElement.getLocalName(), realElement
						.getNamespaceURI())) return new JTextArea(print(document));
				int j = 0;
				for (final Element child : getRealChildElements(f)) {
					if (j >= childElements.size()) return new JTextArea(print(document));
					final Element childElement = childElements.get(j++);
					if (!checkNodeAndNamespace(child, childElement.getLocalName(), childElement
							.getNamespaceURI())) return new JTextArea(print(document));
				}
				if (j != childElements.size()) return new JTextArea(print(document));
			}

			if (childElements.size() == 1) return new JTextArea(print(document));
			if (childElements.size() == 0) {
				final Object[] values = new Object[elements.size()];
				int i = 0;
				for (final Element e : elements) {
					final Element f = getLastParent(e);
					values[i++] = f.getTextContent();
				}
				return new JScrollPane(new JList(values));
			} else {
				final Object[] header = new Object[childElements.size()];
				int i = 0;
				for (final Element e : childElements) {
					header[i++] = firstToUpper(e.getLocalName());
				}
				final Object[][] values = new Object[elements.size()][childElements.size()];
				i = 0;
				for (final Element e : elements) {
					int j = 0;
					for (final Element f : getRealChildElements(e)) {
						values[i][j++] = f.getTextContent();
					}
					i++;
				}
				JTable table = new JTable(values, header);
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
				for (int j = 0; j < childElements.size(); j++) {
					sorter.setComparator(j, new SmartStringComparator());
				}
				table.setRowSorter(sorter);
				return new JScrollPane(table);
			}
		}
	}

	public JComponent createVisualizationPanelObject(final Object response) {
		if (response instanceof Document) return createVisualizationPanel((Document) response);
		return new JLabel(response.toString());
	}

	public JComponent createVisualizationPanel(final Prediction<?> response) {
		return createVisualizationPanel(response, "hello");
	}
	
	@SuppressWarnings("unchecked")
	private <U extends Comparable<U>> JComponent createVisualizationPanel(final Prediction<?> response, U ignore) {
		JComponent component = response.getJComponent(false);
		if (response instanceof DiscretePrediction<?>) {
			GraphComponent graphComponent = new GraphComponent();
			DiscretePrediction<U> discretePrediction = (DiscretePrediction<U>) response;
			U predicted = discretePrediction.getPredictedValue();
			double max = 0;
			for (U value : discretePrediction.getValues()) {
				double probability = discretePrediction.getProbability(value) * 100;
				max = Math.max(max, probability);
				if (predicted != null && predicted.equals(value))
					graphComponent.addValue(value.toString(), probability, Color.GREEN.darker(), Color.DARK_GRAY);
				else
					graphComponent.addValue(value.toString(), probability, Color.GRAY.brighter(), Color.DARK_GRAY);	
			}
			if (max > 50) {
				graphComponent.setMax(100);
				graphComponent.setStep(10);
			} else if (max > 20) {
				graphComponent.setMax(50);
				graphComponent.setStep(5);
			} else {
				graphComponent.setMax(20);
				graphComponent.setStep(2);
			}
			component = graphComponent;
		}
		if (component == null) {
			component = new JLabel(response.toString());
		}
		if (component.getToolTipText() == null) {
			component.setToolTipText(response.getToolTip());
		}
		return component;
	}

	@SuppressWarnings("static-access")
	public <R> JComponent createVisualizationPanel(final Recommendation<R> result) {
		final JPanel panel = new JPanel(new BorderLayout());
		final JPanel recommendation = new JPanel(new BorderLayout());
		recommendation.add(new JLabel("Recommended action:"), BorderLayout.CENTER);
		recommendation.add(createEventVisualizer(result.getRecommendation()), BorderLayout.EAST);
		panel.add(recommendation, BorderLayout.NORTH);

		final DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Event",
				"Prediction" }, 0) {
			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(final int c) {
				if (getRowCount() > 0) return getValueAt(0, c).getClass();

				return Object.class;
			}
		};
		final Set<Entry<XEvent, Prediction<? extends R>>> entrySet = result.entrySet();
		for (final Entry<XEvent, Prediction<? extends R>> entry : entrySet) {
			tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
		}
		final JTable table = new JTable(tableModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		sorter.setComparator(0, new XEventComparator());
		sorter.setComparator(1, new PredictionComparator());
		table.setRowSorter(sorter);

		final JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		panel.add(scrollPane, BorderLayout.CENTER);

		final Set<Class<?>> defined = new HashSet<Class<?>>();
		for (final Entry<XEvent, Prediction<? extends R>> entry : entrySet) {
			if (!defined.contains(entry.getValue().getClass())) {
				defined.add(entry.getValue().getClass());
				final TableCellRenderer renderer = entry.getValue().getTableCellRenderer();
				table.setDefaultRenderer(entry.getValue().getClass(), new TableCellRenderer() {
					TableCellRenderer r = renderer == null ? new DefaultTableCellRenderer()
							: renderer;

					@Override
					public Component getTableCellRendererComponent(final JTable table,
							final Object prediction, final boolean isSelected,
							final boolean hasFocus, final int row, final int column) {
						final Component result = r.getTableCellRendererComponent(table, prediction,
								isSelected, hasFocus, row, column);
						if (result instanceof JComponent) {
							final JComponent c = (JComponent) result;
							final Prediction<?> p = (Prediction<?>) prediction;
							c.setToolTipText(p.getToolTip());
						}
						return result;
					}
				});
			}
		}
		table.setDefaultRenderer(XEvent.class, new EventCellRenderer());
		table.setDefaultRenderer(XEventImpl.class, new EventCellRenderer());

		return panel;
	}

	public JComponent createVisualizationPanel(final ResponseSet<?> result,
			final String providerName) {
		Collection<?> responses = result.getResponses(providerName);
		Collection<Failure> failures = result.getFailures(providerName);
		if (responses == null) {
			responses = Collections.emptyList();
		}
		if (failures == null) {
			failures = Collections.emptyList();
		}
		if (!responses.isEmpty() || !failures.isEmpty()) {
			final JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			final JPanel resultPanel = createHeadlinedComponent(providerName, panel);
			for (final Object response : responses) {
				if (response instanceof Prediction<?>) {
					panel.add(createVisualizationPanel((Prediction<?>) response));
				} else if (response instanceof Recommendation<?>) {
					panel.add(createVisualizationPanel((Recommendation<?>) response));
				} else {
					panel.add(createVisualizationPanelObject(response));
				}
			}
			for (final Failure failure : failures) {
				panel.add(createFailurePanel(failure));
			}
			return resultPanel;
		}
		return null;
	}

	public JComponent getVisualizationPanel(final ResponseSet<?> result) {
		System.out.println(result);
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		final Set<String> providers = new TreeSet<String>();
		for (final String provider : result) {
			providers.add(provider);
		}
		for (final String provider : result.failures()) {
			providers.add(provider);
		}

		for (final String providerName : providers) {
			final JComponent component = createVisualizationPanel(result, providerName);
			if (component != null) {
				panel.add(component);
			}
		}
		return panel;
	}

	private boolean checkNodeAndNamespace(final Element element, final String nodeName,
			final String nameSpace) {
		if (nodeName != null && !nodeName.equals(element.getLocalName())) return false;
		if (nameSpace != null && !nameSpace.equals(element.getNamespaceURI())) return false;
		return true;
	}

	private Component createFailurePanel(final Failure failure) {
		final StringBuilder error = new StringBuilder();
		error.append("<html>");
		error.append("<h4><font color=\"red\">Failed</font></h4>");
		error.append(htmlify(failure.getMessage()));
		error.append("</html>");
		final JLabel text = new JLabel(error.toString());
		text.setToolTipText("<html>" + htmlify(failure.toString()) + "</html>");
		return text;
	}

	private Document createFreshDocument(final Node node) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setExpandEntityReferences(true);
		try {
			final Document d = dbf.newDocumentBuilder().newDocument();
			Node n = d.adoptNode(node);
			if (n == null) {
				n = d.importNode(node, true);
			}
			d.appendChild(n);
			return d;
		} catch (final ParserConfigurationException e) {
			return null;
		}
	}

	private List<Element> getChildElements(final Node n) {
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			final List<Element> children = new ArrayList<Element>();
			final NodeList childNodes = n.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					children.add((Element) childNodes.item(i));
				}
			}
			return children;
		}
		return Collections.emptyList();
	}

	private Element getLastParent(final Node n) {
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			final List<Element> childElements = getChildElements(n);
			if (childElements.size() != 1) return (Element) n;
			return getLastParent(childElements.get(0));
		}
		return null;
	}

	private List<Element> getRealChildElements(final Node n) {
		final List<Element> childElements = getChildElements(n);
		if (childElements.size() == 1) return getRealChildElements(childElements.get(0));
		return childElements;
	}

	private String htmlify(final String message) {
		return message.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\n", "<br />");
	}

	private String print(final Node htmlNode) {
		final Document d = createFreshDocument(htmlNode);
		if (d != null)
			return print(d);
		else
			return "Error converting result to string";
	}

}
