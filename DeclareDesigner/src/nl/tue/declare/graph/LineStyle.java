package nl.tue.declare.graph;

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

import org.jgraph.graph.*;

public class LineStyle
    implements Cloneable {

  // default line dash --> full line
  public static final float[] DEFAULT_LINE = {
      1, 0};
  // default begin --> no arrow
  public static final int DEFAULT_BEGIN = GraphConstants.ARROW_NONE;
  // default middle --> no arrow
  public static final int DEFAULT_MIDDLE = GraphConstants.ARROW_NONE;
  // default end --> no arrow
  public static final int DEFAULT_END = GraphConstants.ARROW_NONE;
  // line number
  public static final int DEFAULT_NUMBER = 1;
  // default begin fill --> no fill
  public static final boolean DEFAULT_END_FILL = false;
  // default middle fill --> no fill
  public static final boolean DEFAULT_MIDDLE_FILL = false;
  // default end fill --> no fill
  public static final boolean DEFAULT_BEGIN_FILL = false;

  private float[] line;
  private int begin;
  private int end;
  private int middle;
  private boolean beginFill;
  private boolean endFill;
  private boolean middleFill;
  private int number;

  public float[] getLine() {
    return line;
  }

  public int getBegin() {
    return begin;
  }

  public int getMiddle() {
    return middle;
  }

  public int getEnd() {
    return end;
  }

  public boolean isBeginFill() {
    return beginFill;
  }

  public boolean isMiddleFill() {
    return middleFill;
  }

  public boolean isEndFill() {
    return endFill;
  }

  public int getNumber() {
    return number;
  }

  public void setLine(float[] line) {
    this.line = line;
  }

  public void setBegin(int begin) {
    this.begin = begin;
  }

  public void setMiddle(int middle) {
    this.middle = middle;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public void setBeginFill(boolean beginFill) {
    this.beginFill = beginFill;
  }

  public void setMiddleFill(boolean middleFill) {
    this.middleFill = middleFill;
  }

  public void setEndFill(boolean endFill) {
    this.endFill = endFill;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * LineStyle
   *
   * @param anLine float[] --> dash patter for line
   * @param anBegin int    --> begin arrow style for line
   * @param anEnd int      --> end arrow style for line
   */
  public LineStyle(float[] anLine, int number, int anBegin, int anMiddle,
                   int anEnd,
                   boolean anBeginFill, boolean anMiddleFill, boolean anEndFill) {
    this.setLine(anLine);
    this.setNumber(number);
    this.setBegin(anBegin);
    this.setMiddle(anMiddle);
    this.setEnd(anEnd);
    this.setBeginFill(anBeginFill);
    this.setMiddleFill(anMiddleFill);
    this.setEndFill(anEndFill);
  }

  public void setStyle(LineStyle style) {
    this.setLine(style.getLine());
    this.setNumber(style.getNumber());
    this.setBegin(style.getBegin());
    this.setMiddle(style.getMiddle());
    this.setEnd(style.getEnd());
    this.setBeginFill(style.isBeginFill());
    this.setMiddleFill(style.isMiddleFill());
    this.setEndFill(style.isEndFill());
  }

  /**
   * LineStyle
   *
   * @param anLine float[] --> dash patter for line
   * @param anBegin int    --> begin arrow style for line
   * @param anEnd int      --> end arrow style for line
   */
  public LineStyle(float[] anLine, int anBegin, int anMiddle, int anEnd) {
    this(anLine, DEFAULT_NUMBER, anBegin, anMiddle, anEnd, DEFAULT_BEGIN_FILL,
         DEFAULT_MIDDLE_FILL, DEFAULT_END_FILL);
  }

  /**
   * LineStyle
   *
   * @param anLine float[] --> dash patter for line
   * @param anBegin int    --> begin arrow style for line
   * @param anEnd int      --> end arrow style for line
   */
  public LineStyle(float[] anLine, int number, int anBegin, int anMiddle,
                   int anEnd) {
    this(anLine, number, anBegin, anMiddle, anEnd, DEFAULT_BEGIN_FILL,
         DEFAULT_MIDDLE_FILL, DEFAULT_END_FILL);
  }

  /**
   * LineStyle
   *
   * crate a default line style
   */
  public LineStyle() {
    this(null, DEFAULT_NUMBER, DEFAULT_BEGIN, DEFAULT_MIDDLE, DEFAULT_END,
         DEFAULT_BEGIN_FILL, DEFAULT_MIDDLE_FILL, DEFAULT_END_FILL);
  }

  /**
   *
   * @return Object
   */
  public Object clone() {
    return new LineStyle(this.line, this.number, this.begin, this.middle,
                         this.end, this.beginFill, this.middleFill,
                         this.endFill);
  }

}
