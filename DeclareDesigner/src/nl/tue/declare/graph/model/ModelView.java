package nl.tue.declare.graph.model;

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
import java.util.List;

import java.awt.*;
import javax.swing.tree.*;

import org.jgraph.graph.*;

import nl.tue.declare.graph.*;

public abstract class ModelView {

  private static final Color BACKGROUND = new Color(235, 235, 235);
  protected List<DefaultGraphCell> cells;
  protected DGraph graph;

  public ModelView() {
    super();
    graph = new DGraph();
    cells = new ArrayList<DefaultGraphCell>();
    graph.setBackground(BACKGROUND);
  }

  public ModelView(ModelView view) {
    super();
    cells = new ArrayList<DefaultGraphCell>(view.cells);
    graph = new DGraph();
    graph.setBackground(BACKGROUND);
  }

  /**
   * addVertex
   *
   * @param cell DefaultGraphCell
   */
  public void addVertex(DefaultGraphCell cell) {
    addCell(cell);
  }

  /**
   * addEdge
   *
   * @param cell DefaultGraphCell
   * @param source DefaultGraphCell
   * @param target DefaultGraphCell
   */
  public void addEdge(DefaultGraphCell cell, DefaultGraphCell source,
                      DefaultGraphCell target) {
    if (cell instanceof DefaultEdge) {
      if (cells.contains(source) && cells.contains(target)) {
        if (source.getChildCount() == 0) {
          source.addPort();
        }
        if (target.getChildCount() == 0) {
          target.addPort();
        }
        DefaultEdge edge = (DefaultEdge) cell;
        edge.setSource(source.getChildAt(0));
        edge.setTarget(target.getChildAt(0));
        addCell(edge);
      }
    }
  }

  /**
   * addEdge
   *
   * @param cell DefaultGraphCell
   * @param source DefaultGraphCell
   * @param target DefaultGraphCell
   */
  public void addEdge(DefaultGraphCell cell, TreeNode source,
                      TreeNode target) {
    if (cell instanceof DefaultEdge) {
      DefaultEdge edge = (DefaultEdge) cell;
      edge.setSource(source);
      edge.setTarget(target);
      addCell(edge);
    }
  }

  /**
   * addCell
   *
   * @param cell DefaultGraphCell
   */
  protected void addCell(DefaultGraphCell cell) {
    if (!cells.contains(cell)) {
      cells.add(cell);
      graph.getGraphLayoutCache().insert(cell);
    }
  }

  /**
   * getGraph
   *
   * @return DGraph
   */
  public DGraph getGraph() {
    return this.graph;
  }

  /**
   * getModel
   *
   * @return DGraphModel
   */
  public GraphModel getModel() {
    //return model;
    return graph.getModel();
  }

  /**
   * removeCell
   *
   * @param cells Object
   */
  protected void removeCells(Object[] cells) {
    cells = graph.getDescendants(cells);
    getModel().remove(cells);
  }

  /**
   * getCell
   *
   * @param object Object
   * @return DefaultGraphCell
   */
  protected List<DefaultGraphCell> getCells(Object object) {
    List<DefaultGraphCell> list = new ArrayList<DefaultGraphCell>();
    DefaultGraphCell cell = null;
    for (int i = 0; i < getModel().getRootCount(); i++) {
      Object cellObject = getModel().getRootAt(i);
      if (cellObject instanceof DefaultGraphCell) {
        cell = (DefaultGraphCell) cellObject;
        Object userObject = cell.getUserObject();
        if (userObject.equals(object)) {
          list.add(cell);
        }
      }
    }
    return list;
  }

  /**
   * getCells
   *
   * @return DefaultGraphCell
   */
  protected List<DefaultGraphCell> getCells() {
    List<DefaultGraphCell> list = new ArrayList<DefaultGraphCell>();
    DefaultGraphCell cell = null;
    for (int i = 0; i < getModel().getRootCount(); i++) {
      Object cellObject = getModel().getRootAt(i);
      if (cellObject instanceof DefaultGraphCell) {
        cell = (DefaultGraphCell) cellObject;
        list.add(cell);
      }
    }
    return list;
  }

  /**
   * clear
   */
  protected void clear() {
    getModel().remove(getCells().toArray());
  }

  /**
   * updateUI
   */
  public void updateUI() {
    graph.refresh();
  }

  /**
   * updateUI
   *
   * @param cell DefaultGraphCell
   */
  public void updateUI(DefaultGraphCell cell) {
    CellView view = graph.getGraphLayoutCache().getMapping(cell, true);
    graph.getGraphLayoutCache().refresh(view, true);
    graph.repaint();
  }

  /**
   * getVertex
   *
   * @param port Port
   * @return DVertex
   */
  protected DVertex getVertex(Port port) {
    DVertex vertex = null;
    Object vertexObject = getModel().getParent(port);
    if (vertexObject != null) {
      if (vertexObject instanceof DVertex) {
        vertex = (DVertex) vertexObject;
      }
    }
    return vertex;
  }

  /**
   * getVertexObject
   *
   * @param port Port
   * @return ActivityDefinition
   */
  public Object getVertexObject(Port port) {
    Object object = null;
    DVertex vertex = this.getVertex(port);
    if (vertex != null) {
      object = vertex.getUserObject();
    }
    return object;
  }

  /**
   * vertexCells
   *
   * @return List
   */
  protected List<DVertex> vertexCells() {
    List<DVertex> cells = new ArrayList<DVertex> ();
    for (int i = 0; i < getModel().getRootCount(); i++) {
      Object cell = getModel().getRootAt(i);
      if (cell instanceof DVertex) {
        cells.add( (DVertex) cell);
      }
    }
    return cells;
  }

  /**
   *
   */
  public void clearSelection() {
    graph.getSelectionModel().clearSelection();
  }
}
