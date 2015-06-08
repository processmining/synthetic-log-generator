package nl.tue.declare.graph;

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

import org.jgraph.graph.*;

public class DEdge
    extends DefaultEdge implements Cloneable {

  private static final long serialVersionUID = -693583188250090210L;
  private static final float LINE_WIDTH = 2;
  private static final int SYMBOL_SIZE = 10;
  private LineStyle lineStyle;

  public DEdge() {
    super();
    setLineStyle(new LineStyle());
    prepare(false);
  }

  public DEdge(Object object) {
    super(object);
    setLineStyle(new LineStyle());
    prepare(false);
  }

  public DEdge(Object object, AttributeMap attributeMap) {
    super(object, attributeMap);
    setLineStyle(new LineStyle());
    prepare(false);
  }

  public DEdge(Object object, LineStyle anLineStyle) {
    super(object);
    setLineStyle(anLineStyle);
    prepare(false);
  }

  public DEdge(LineStyle anLineStyle) {
    super();
    setLineStyle(anLineStyle);
    prepare(false);
  }

  public DEdge(LineStyle anLineStyle, ArrayList<Point> points) {
    super();
    setLineStyle(anLineStyle);
    GraphConstants.setPoints(getAttributes(), points);
    prepare(false);
  }

  protected float getLineWidth() {
    return LINE_WIDTH;
  }

  public LineStyle getLineStyle() {
    return lineStyle;
  }

  private void setLineStyle(LineStyle anStyle) {
    lineStyle = anStyle;

    this.resetLineDash();
    this.resetLineNumber();
    this.resetLineBegin();
    this.resetLineMiddle();
    this.resetLineEnd();
  }

  protected void resetLineStyle() {
    this.setLineStyle(this.lineStyle);
  }

  private int getSymbolSize(int symbol) {
    int size = SYMBOL_SIZE;
    if (symbol == GraphConstants.ARROW_DOUBLELINE) {
      size = size * (new Float(LINE_WIDTH)).intValue();
    }
    return size;
  }

  protected void resetLineBegin() {
    // Set Arrow and Fill Style for edge begin
	  AttributeMap map = getAttributes();    
    GraphConstants.setLineBegin(map, lineStyle.getBegin());
    GraphConstants.setBeginFill(map, lineStyle.isBeginFill());
    GraphConstants.setBeginSize(map, getSymbolSize(lineStyle.getBegin()));
  }

  protected void resetLineEnd() {
    // Set Arrow and Fill Style for edge end
	AttributeMap map = getAttributes();  
    GraphConstants.setLineEnd(map, lineStyle.getEnd());
    GraphConstants.setEndFill(map, lineStyle.isEndFill());
    GraphConstants.setEndSize(map, getSymbolSize(lineStyle.getEnd()));
  }

@SuppressWarnings("unchecked")
protected void resetLineMiddle() {
    // Set Arrow and Fill Style for edge end
  
	AttributeMap map = getAttributes();
    DGraphConstants.setLineMiddle(map, lineStyle.getMiddle());
    DGraphConstants.setMiddleFill(map, lineStyle.isMiddleFill());
    DGraphConstants.setMiddleSize(map, getSymbolSize(lineStyle.getMiddle()));
  }

  protected void resetLineDash() {
    // Set Dash Style for edge
    this.setLineDash(lineStyle.getLine());
  }

  protected void setLineDash(float[] dash) {
    if (dash != null) {
      GraphConstants.setDashPattern(getAttributes(), dash);
    }
    else {
      this.getAttributes().remove(DGraphConstants.DASHPATTERN);
    }

  }

@SuppressWarnings("unchecked")
protected void resetLineNumber() {
    // Set Dash Style for edge
    DGraphConstants.setLineNumber(getAttributes(), lineStyle.getNumber());
  }

  /**
   * setUp
   *
   * @param editable boolean
   */
  private void prepare(boolean editable) {
    GraphConstants.setLabelAlongEdge(getAttributes(), true);
    GraphConstants.setEditable(getAttributes(), editable);
    GraphConstants.setDisconnectable(getAttributes(), false);
    GraphConstants.setLineWidth(getAttributes(), this.getLineWidth());
  }

  /**
   * setEditable
   *
   * @param editable boolean
   */
  protected void setEditable(boolean editable) {
    GraphConstants.setEditable(getAttributes(), editable);
  }

  /**
   * setColor
   *
   * @param color Color
   */
  public void setLineColor(Color color) {
    GraphConstants.setLineColor(getAttributes(), color);
  }

  /**
   *
   * @return Object
   */
  public Object clone() {
    DEdge myClone = null;
    Object lineClone = this.lineStyle.clone();
    if (lineClone != null) {
      if (lineClone instanceof LineStyle) {
        myClone = new DEdge( (LineStyle) lineClone);
      }
    }
    return myClone;
  }

  /**
   * clearLineBegin
   */
  protected void clearLineBegin() {
    // Set Arrow Style for edge
    GraphConstants.setLineBegin(getAttributes(), GraphConstants.ARROW_NONE);

    // Set Arrow Style for edge
    GraphConstants.setBeginFill(getAttributes(), false);
  }

  /**
   * clearLineEnd
   */
  protected void clearLineEnd() {
    // Set Arrow Style for edge
    GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_NONE);

    // Set Arrow Style for edge
    GraphConstants.setEndFill(getAttributes(), false);
  }

  /**
   * clearLineMiddle
   */

@SuppressWarnings("unchecked")
protected void clearLineMiddle() {
    // Set Arrow Style for edge
    DGraphConstants.setLineMiddle(getAttributes(), GraphConstants.ARROW_NONE);

    // Set Arrow Style for edge
    DGraphConstants.setMiddleFill(getAttributes(), false);
  }

  public void refresh() {

  }
}
