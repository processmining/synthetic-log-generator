package nl.tue.declare.graph;

import java.util.*;

import java.awt.*;

import org.jgraph.graph.AttributeMap;


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
public class DGraphConstants
    extends org.jgraph.graph.GraphConstants {

  /**
   * Key for the <code>lineBegin</code> attribute. Use instances of Integer
   * as values for this key. Constants defined in this class.
   */
  public final static String LINEMIDDLE = "lineMiddle";
  public final static String MIDDLE_SIZE = "middleSize";
  public static final String MIDDLECOLOR = "middleColor";
  public static final String MIDDLE_FILL = "middleFill";

  public static final String LINE_NUMBER = "lineNumber";

  public static final int DEFAULT_LINE_NUMBER = 1;

  public static final int ARROW_TECHNICAL_CIRCLE = 10;

  private static final int TRUE_RED = 0;
  private static final int TRUE_GREEN = 170;
  private static final int TRUE_BLUE = 0;

  private static final int FALSE_TEMPORARY_RED = 255;
  private static final int FALSE_TEMPORARY_GREEN = 139;
  private static final int FALSE_TEMPORARY_BLUE = 0;

  private static final int FALSE_PERMANENET_RED = 255;
  private static final int FALSE_PERMANENET_GREEN = 0;
  private static final int FALSE_PERMANENET_BLUE = 0;

  public static final float[] DASH_NONE = null; //{10, 0};
  public static final float[] DASH_1 = {
      5, 5};
  public static final float[] DASH_2 = {
      10, 10};
  public static final float[] DASH_3 = {
      10, 2, 2, 2};

  public static final int NUMBER_ONE = 1;
  public static final int NUMBER_TWO = 2;
  public static final int NUMBER_THREE = 3;

  /**
   * Returns the LINEBEGIN attribute from the specified map. This attribute
   * indicates what sort of decoration should be applied to the beginning of
   * edges when they are rendered.
   *
   * @see #ARROW_NONE
   * @see #ARROW_CLASSIC
   * @see #ARROW_TECHNICAL
   * @see #ARROW_SIMPLE
   * @see #ARROW_CIRCLE
   * @see #ARROW_LINE
   * @see #ARROW_DOUBLELINE
   * @see #ARROW_DIAMOND
   * @see #ARROW_TECHNICAL_CIRCLE
   */
  public static final int getLineMiddle(AttributeMap map) {
    Integer intObj = (Integer) map.get(LINEMIDDLE);
    if (intObj != null) {
      return intObj.intValue();
    }
    return ARROW_NONE;
  }

  /**
   * Sets the LINEBEGIN attribute in the specified Map<Object,Object> to the specified value.
   * This attribute indicates what sort of decoration should be applied to the
   * beginning of edges when they are rendered.
   *
   * @see #ARROW_NONE
   * @see #ARROW_CLASSIC
   * @see #ARROW_TECHNICAL
   * @see #ARROW_SIMPLE
   * @see #ARROW_CIRCLE
   * @see #ARROW_LINE
   * @see #ARROW_DOUBLELINE
   * @see #ARROW_DIAMOND
   * @see #ARROW_TECHNICAL_CIRCLE
   */
  public static final void setLineMiddle(Map<Object,Object> map, int style) {
    map.put(LINEMIDDLE, new Integer(style));
  }

  /**
   * Sets the beginsize attribute in the specified Map<Object,Object> to the specified value.
   */
  public static final void setMiddleColor(Map<Object,Object> map, Color color) {
    map.put(MIDDLECOLOR, color);
  }

  /**
   * Returns the beginsize attribute from the specified map.
   */
  public static final Color getMiddleColor(Map<Object,Object> map) {
    return (Color) map.get(MIDDLECOLOR);
  }

  /**
   *
   * @return Color
   */
  public static final Color trueTemporaryColor() {
    Color color = new Color(TRUE_RED, TRUE_GREEN, TRUE_BLUE);
    return color.darker();
  }

  /**
   *
   * @return Color
   */
  public static final Color truePermamentColor() {
    return Color.BLACK;
  }

  /**
   *
   * @return Color
   */
  public static final Color falseTemporaryColor() {
    Color color = new Color(FALSE_TEMPORARY_RED, FALSE_TEMPORARY_GREEN,
                            FALSE_TEMPORARY_BLUE);
    return color;
  }

  /**
   *
   * @return Color
   */
  public static final Color falsePermanentColor() {
    Color color = new Color(FALSE_PERMANENET_RED, FALSE_PERMANENET_GREEN,
                            FALSE_PERMANENET_BLUE);
    return color;
  }

  /**
   *
   * @return Color
   */
  public static final Color unvalidColor() {
    return Color.LIGHT_GRAY;
  }

  /**
   * Sets the opaque level attribute in the specified Map<Object,Object> to the specified value.
   */
  public static final void setLineNumber(Map<Object,Object> map, int level) {
    map.put(LINE_NUMBER, new Integer(level));
  }

  /**
   * Returns the opaque level attribute from the specified map.
   */
  public static final int getLineNumber(AttributeMap map) {
    Integer intObj = (Integer) map.get(LINE_NUMBER);
    if (intObj != null) {
      return intObj.intValue();
    }
    return DEFAULT_LINE_NUMBER;
  }

  /**
   * Sets the opaque level attribute in the specified Map<Object,Object> to the specified value.
   */
  public static final void setMiddleSize(Map<Object,Object> map, int size) {
    map.put(MIDDLE_SIZE, new Integer(size));
  }

  /**
   * Returns the opaque level attribute from the specified map.
   */
  public static final int getMiddleSize(Map<Object,Object> map) {
    Integer intObj = (Integer) map.get(MIDDLE_SIZE);
    if (intObj != null) {
      return intObj.intValue();
    }
    return 10;
  }

  /**
   * Sets the middlefill attribute in the specified Map<Object,Object> to the specified value.
   */
  public static final void setMiddleFill(Map<Object,Object> map, boolean flag) {
    map.put(MIDDLE_FILL, new Boolean(flag));
  }

  /**
   * Returns the beginfill attribute from the specified map.
   */
  public static final boolean isMiddleFill(Map<Object,Object> map) {
    Boolean bool = (Boolean) map.get(MIDDLE_FILL);
    if (bool != null) {
      return bool.booleanValue();
    }
    return false;
  }
}
