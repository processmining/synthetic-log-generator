package nl.tue.declare.appl.util;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.util.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import org.jgraph.*;
import org.jgraph.graph.*;

import nl.tue.declare.graph.*;

public class JGraphLineComboBox
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1974862210235537707L;
ArrayList<LineStyle> lines = new ArrayList<LineStyle>();
  ArrayList<String> strings = new ArrayList<String>();

  static final int WIDTH = 200;
  static final int HEIGHT = 20;
  JComboBox lineList = new JComboBox();

  /**
   * addStyle
   */
  public void addStyle(LineStyle style) {
    this.addString(null);
    lines.add(style);
    lineList.addItem(style);
  }
  
  public void addStyles(ArrayList<LineStyle> styles) {
      clear();
      for (int i = 0; i < styles.size(); i++) {
        LineStyle curr = styles.get(i);
        addStyle(curr);
      }
    }

  /**
   * addStyle
   */
  public void addStyle(LineStyle style, String string) {
    this.addString(string);
    lines.add(style);
    lineList.addItem(style);
  }

  private void addString(String string) {
    if (string != null) {
      strings.add(string);
    }
    else {
      strings.add("");
    }
  }

  private void clearStrings() {
    strings.clear();
  }

  public Iterator<LineStyle> styles() {
    return lines.iterator();
  }

  /*
   * Despite its use of EmptyBorder, this panel makes a fine content
   * pane because the empty border just increases the panel's size
   * and is "painted" on top of the panel's normal background.  In
   * other words, the JPanel fills its entire background if it's
   * opaque (which it is by default); adding a border doesn't change
   * that.
   */

  public JGraphLineComboBox() {
    super(new BorderLayout());
    // setup the renderer
    setRenderer(lineList.getPreferredSize());

    //Lay out the demo.
    add(lineList, BorderLayout.PAGE_START);
  }

  /**
   * createGraph
   *
   * @param pattern float[]
   * @return JGraph
   */
  public JGraph createGraph(LineStyle style, String string) {
    // Construct Graph
    DGraph graph = new DGraph();
    //graph.getGraphLayoutCache().setFactory(new LTLCellViewFactory());

    // set disabled. otherwise there are placeholders around lines
    graph.setSelectionEnabled(false);

    // make the start end the end point for the line
    Dimension dimension = lineList.getSize();
    double height = dimension.getHeight();
    double width = dimension.getWidth();
    Double leftX = new Double(10);
    Double leftY = new Double(height / 2);
    Double rightX = new Double(width - 50);
    Double rightY = new Double(height / 2);
    Point left = new Point(leftX.intValue(), leftY.intValue());
    Point right = new Point(rightX.intValue(), rightY.intValue());

    // Create Edge
//    DefaultEdge edge = CellFactory.createEdge(style, left, right);
    DefaultGraphCell source = new DefaultGraphCell(); // invisible source cell
    source.addPort();
    GraphConstants.setBounds(source.getAttributes(),
                             new Rectangle2D.Double(left.getX(), left.getY(), 1,
        1));

    DefaultGraphCell target = new DefaultGraphCell(); // invivisble target cell
    target.addPort();
    GraphConstants.setBounds(target.getAttributes(),
                             new Rectangle2D.Double(right.getX(), right.getY(),
        1, 1));

    DefaultEdge edge = new DEdge(style);
    edge.setSource(source);
    edge.setTarget(target);
    // set display
    edge.setUserObject(string);

    // insert edge
    graph.getGraphLayoutCache().insert(source);
    graph.getGraphLayoutCache().insert(target);
    graph.getGraphLayoutCache().insert(edge);
    graph.clearSelection();
    return graph;
  }

  /**
   * setRenderer
   */
  public void setRenderer(Dimension dim) {
    ComboBoxRenderer renderer = new ComboBoxRenderer();
    renderer.setPreferredSize(dim);
    lineList.setRenderer(renderer);
    lineList.setMaximumRowCount(5);
  }

  /**
   * getSelectedStyle
   */
  public LineStyle getSelectedStyle() {
    LineStyle style = null;
    Object selected = this.lineList.getSelectedItem();
    if (selected != null) {
      if (selected instanceof LineStyle) {
        style = (LineStyle) selected;
      }
    }
    return style;
  }

  /**
   * setSelected
   */
  public void setSelected(LineStyle style) {
    if (style != null) {
      this.lineList.setSelectedItem(style);
    }
  }

  /**
   * getSelectedIndex
   */
  public int getSelectedIndex() {
    return lineList.getSelectedIndex();
  }

  /**
   * clear
   */
  public void clear() {
    this.clearStrings();
    lines.clear();
    lineList.removeAllItems();
  }

  class ComboBoxRenderer
      extends JPanel implements ListCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4490526872362280854L;
	private JGraph current = null;

    public ComboBoxRenderer() {
      setOpaque(true);
      setLayout(new BorderLayout());
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
      //Get the selected index. (The index param isn't
      //always valid, so just use the value.)
      int selectedIndex = -1;
      if (value != null) {
        if (isSelected) {
          setBackground(list.getSelectionBackground());
          setForeground(list.getSelectionForeground());
        }
        else {
          setBackground(list.getBackground());
          setForeground(list.getForeground());
        }

        // select which line to print at which position of the combo box
        LineStyle lineStyle = (LineStyle) value;

        // create a new gpath for every item
        if (current != null) {
          this.remove(current);
        }
        String string = null; // used to write special descrition (if exists) of the arc
        if (index >= 0) {
          string = strings.get(index);
        }
        else {
          selectedIndex = list.getSelectedIndex();
          string = strings.get(selectedIndex);
        }
        current = createGraph(lineStyle, string);
        this.add(current);
      }
      else {
        if (current != null) {
          this.remove(current);
        }
      }
      return this;
    }
  }
}
