package nl.tue.declare.appl.framework;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import nl.tue.declare.appl.framework.gui.*;
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

import nl.tue.declare.appl.framework.yawl.*;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.instance.*;

public class MappingCoordinator
    implements TableModelListener, ILoadListener {
  private FrmMapping frmLoad;
  private JFrame parent;
  private FrmSelectFromList frmSelect;

  private DataMapTableModel dataTableModel = new DataMapTableModel();
  private DataMapTable dataTable = new DataMapTable(dataTableModel);

  private TeamCoordinator teamCoordinator;
  private InstanceHandler manager;

  public MappingCoordinator(JFrame parent, InstanceHandler manager) {
    super();
    this.manager = manager;
    this.parent = parent;
    teamCoordinator = new TeamCoordinator(this.parent, this.parent);

    frmLoad = new FrmMapping(this.parent, this.parent, dataTable,
                             teamCoordinator.teamContainer());
    frmLoad.addLoadListener(this);

    frmSelect = new FrmSelectFromList(this.parent, "", this.parent);
    frmSelect.setMyBounds(300, 400);
    frmSelect.setMultipleSelectionMode();

    dataTableModel.addTableModelListener(this);
    setDataTableSelectionListener();
  }

  private void setDataTableSelectionListener() {
    ListSelectionModel rowSM = this.dataTable.getSelectionModel();
    rowSM.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting()) {
          return;
        }

        ListSelectionModel lsm =
            (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
          //no rows are selected
        }
        else { //selectedRow is selected
          fillLocalCombo();
        }
      }
    });
  }

  /**
   *
   * Load the assignment by seecting an assignment model, mapping local to external data
   * elements, and seelction team members.
   *
   * @return true if the user succesfully finished loading; false if the user cancelled the lloading procedure.
   */
  boolean load() {
    if (manager != null) {
      String path = null;
      if (!manager.isEmpty()) {
        path = this.getAssignmentPath();
      }
      if (path != null) {
        manager.setAssignment(path);
      }
      else {
        path = this.openModel(manager);
      }
      if (path != null) {
        this.refresh();
        if (this.emptyMapping()) {
          this.teamCoordinator.assignAdministartor(manager.getAssignment());
          return true;
        }
        else {
          if ( mapped() ){
           return true;
          } else {
            return frmLoad.showCentered();
          }
        }
      }
    }
    return false;
  }

  boolean automaticLoad() {
    if (manager != null) {
      String path = null;
      if (!manager.isEmpty()) {
        path = this.getAssignmentPath();
      }
      if (path != null) {
        manager.setAssignment(path);
      }
      else {
        //path = this.openModel(manager);
    	  return false;
      }
      if (path != null) {
        this.refresh();
        if (this.emptyMapping()) {
          this.teamCoordinator.assignAdministartor(manager.getAssignment());
          return true;
        }
        else {
          if ( mapped() ){
           return true;
          } else {
            return false;
          }
        }
      }
    }
    return false;
  }

  /**
   *  fill the load form with data elements and team structure
   */
  private void fillForm() {
    this.frmLoad.setModel(manager.getPath());
    this.fillData();
  }

  /**
   * fill data info on the loading form
   */
  private void fillData() {
    this.dataTableModel.clear();
    ExternalDataMapping mapping = manager.getMapping();
    for (int i = 0; i < mapping.getSize(); i++) {
      DataMap map = mapping.getMapAt(i);
      this.dataTableModel.addRow(map);
    }
  }

  /**
   * fill combo box from which users can seelct local data fields
   */
  private void fillLocalCombo() {
    ExternalDataMapping mapping = manager.getMapping();
    for (int j = 0; j < mapping.getSize(); j++) {
      DataMap map = mapping.getMapAt(j);
      List<Object> item = new ArrayList<Object>();
      Assignment assignment = manager.getAssignment();
      for (int i = 0; i < assignment.dataCount(); i++) {
        DataField localField = assignment.dataAt(i);
        if (mapping.canMap(map, localField)) {
          item.add(localField);
        }
      }
      this.dataTable.addLocal(item.toArray());
    }
  }

  void complete() {

  }

  /**
   * Called when the data table is changed, i.e., when a data mapping is changed.
   * @param e TableModelEvent
   */
  public void tableChanged(TableModelEvent e) {
    // get the table row and column
    int row = e.getFirstRow();
    int column = e.getColumn();
    // get the table model
    TableModel model = (TableModel) e.getSource();
    // if not a valid selection return
    if (row < 0 || column < 0) {
      return;
    }
    // if a valid selection precede
    if (row < model.getRowCount() && column < model.getColumnCount()) {
      if (model instanceof DataMapTableModel) { // if the table model is a DataMapTableModel
        // get changed value for the table cell
        Object data = model.getValueAt(row, column);
        if (this.dataTableModel.isLocalColumn(column)) { // if the column is for the local data field
          if (data instanceof DataField) { // selected value is a DataField value
            DataField field = (DataField) data;
            Object selected = this.dataTable.getSelected(); // get selected object
            if (selected != null) {
              if (selected instanceof DataMap) { // get the selected DataMap object
                DataMap map = (DataMap) selected;
                map.setLocal(field); // set the local field of the selected data map
              }
            }
          }
        }
      }
    }
  }

  /**
   *
   * @return boolean
   * @throws Exception
   */
  public boolean onOk() throws Exception {
    ExternalDataMapping data = manager.getMapping();
    String message = "";

    boolean dataOk = data.ok(); // check if data mappings are ok
    if (!dataOk) {
      message = "Some external data is not mapped to local data!";
    }

    boolean teamOk = false;
    try {
      teamOk = teamCoordinator.onOk();
    }
    catch (Exception e) {
      if (!message.equals("")) {
        message += "\n";
      }
      message += "Some model roles do not have assigned participants!";
    }

    if ( (!dataOk) || (!teamOk)) {
      throw (new Exception(message));
    }
    return true;
  }

  public boolean mapped() {
    boolean ok = false;
    try {
      ok = teamCoordinator.onOk();
      ok = manager.getMapping().ok();
    }
    catch (Exception e) {
      ok = false;
    }
    return ok;
  }


  private boolean emptyMapping() {
    return manager.isEmptyData() && teamCoordinator.isEmpty();
  }

  /**
   *
   * @param manager CaseManager
   * @return String
   */
  private String openModel(InstanceHandler manager) {
    XMLFileDialog dialog = new XMLFileDialog();
    String path = dialog.openFile(this.parent);
    if (path != null) {
      manager.setAssignment(path);
      return path;
    }
    return null;
  }

  /**
   *
   * @return String
   */
  private String getAssignmentPath() {
      return manager.getMapping().getModel();
  }


  /**
   *
   */
  private void iniDataMapping() {
    if (manager != null) {
      ExternalDataMapping mapping = manager.getMapping();
      for (int i = 0; i < mapping.getSize(); i++) {
        DataMap map = mapping.getMapAt(i);
        String name = map.getExternal().getName();
        String type = map.getExternal().getType();
        DataField local = manager.getAssignment().getDataFied(name, type);
        map.setLocal(local);
      }
    }
  }

  public void reload() {
    if (manager != null) {
      String path = this.openModel(manager);
      if (path != null) {
        this.refresh();
        frmLoad.showCentered();
      }
    }
  }

  private void refresh() {
    teamCoordinator.setAssignment(manager.getAssignment());
    iniDataMapping();
    fillForm();
    frmLoad.setTitle("Data mappings and participants for instance \"" +
                     manager.toString() + "\"");
  }
}
