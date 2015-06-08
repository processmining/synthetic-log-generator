package nl.tue.declare.appl.framework.yawl;

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

import yawlservice.*;
import nl.tue.declare.domain.instance.*;

public class ExternalDataMapping {

  private static final String MODEL = "declaremodel";

  private ExternalWorkItem external;
  private List<DataMap> mappings;
  private ModelDataMap modelDataMap = null;

  /**
   *
   * @param extrenal ExternalWorkItem
   */
  public ExternalDataMapping(ExternalWorkItem extrenal) {
    super();
    this.external = extrenal;
    this.mappings = new ArrayList<DataMap>();
    this.createMaps();
  }

  private void createMaps() {
    if (external != null) {
      for (int i = 0; i < external.dataSize(); i++) {
        ExternalDataElement ext = external.getData(i);
        if (isModel(ext)) {
          this.modelDataMap = new ModelDataMap(ext);
        }
        else {
          this.mappings.add(new DataMap(ext));
        }
      }
    }
  }

  /**
   *
   * @param ext ExternalDataElement
   * @param local DataField
   * @return DataMap
   */
  public DataMap set(ExternalDataElement ext, DataField local) {
    DataMap map = this.getMap(ext);
    map.setLocal(local);
    return map;
  }

  /**
   *
   */
  public void readInputFromExternal() {
    for (int i = 0; i < mappings.size(); i++) {
      mappings.get(i).readFromExternal();
    }
  }

  /**
   *
   */
  public void writeOutputToExternal() {
   for (int i = 0; i < mappings.size(); i++) {
     mappings.get(i).writeToExternal();
    }
  }

 /**
   *
   * @param ext ExternalDataElement
   * @return DataMap
   */
  private DataMap getMap(ExternalDataElement ext) {
    boolean found = false;
    int i = 0;
    DataMap map = null;
    while (!found && i < mappings.size()) {
      map = mappings.get(i++);
      found = map.getExternal() == ext;
    }
    return found ? map : null;
  }

  /**
   *
   * @return boolean
   */
  public boolean ok() {
    boolean ok = true;
    for (int i = 0; i < mappings.size() && ok; i++) {
      DataMap map = this.getMapAt(i);
      ok = map.ok();
    }
    return ok;
  }

  public int getSize() {
    return this.mappings.size();
  }

  public DataMap getMapAt(int index) {
    DataMap map = null;
    if (0 <= index && index < this.mappings.size()) {
      map = mappings.get(index);
    }
    return map;
  }

  /**
   * Checks is it is possible to map a local data element to a extrenal data element.
   * The rule is that (1) the external data element and the local data element have to have the same DATA TYPE, and
   * (2) it one local data element can be mapped only to one extrenal data element.
   * @param map data mapping of a external data element
   * @param local local data element
   * @return true if the types of the external and local data elemets are the same and local dada element is not
   * mapped to another external element.
   */
  public boolean canMap(DataMap map, DataField local) {
    return local.getType().maps(map.getExternal().getType());
  }

  private boolean isModel(ExternalDataElement ext) {
    return ext.getName().equals(MODEL);
  }

  public String getModel() {
	if (modelDataMap != null) {  
		return this.modelDataMap.getModel();
	} else {
		return null;
	}
  }

  public void setModel(String model) {
    this.modelDataMap.setModel(model);
  }

  /**
   *
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
  private class ModelDataMap
      extends DataMap {

    private String model = null;

    private ModelDataMap(ExternalDataElement external) {
      super(external);
      readFromExternal();
    }

    public boolean ok() {
      return true;
    }

    public void setModel(String model) {
      this.model = model;
      writeToExternal();
    }

    public String getModel() {
      return model;
    }

    protected String getValueToWrite() {
      return (model == null) ? "UNKNOWN" : model;
    }

    protected void setValueRead(String value) {
      model = value;
    }

    public String toString() {
      return this.model;
    }
  }

}
