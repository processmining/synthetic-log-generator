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

import yawlservice.*;
import nl.tue.declare.domain.instance.*;

public class DataMap {

  protected ExternalDataElement external;
  private DataField local;

  DataMap(ExternalDataElement external) {
    super();
    this.external = external;
    this.local = null;
  }

  public ExternalDataElement getExternal() {
    return external;
  }

  public DataField getLocal() {
    return local;
  }

  public void setLocal(DataField local) {
    this.local = local;
  }

  public boolean ok() {
    return local != null && external != null;
  }

  public String toString() {
    String type = "unknown";
    boolean in = external.isInput();
    boolean out = external.isOutput();
    if (in && !out){
      type = "input";
    }
    if (!in && out){
      type = "output";
    }

    if (in && out){
      type = "input/output";
    }
    return type;
  }

  protected String getValueToWrite(){
      if (local != null){
        return local.getValue();
      }
      return "";
  }

  /**
   * write
   */
  public void writeToExternal() {
    if (external != null) {
      if (external.isOutput()){
        external.setValue(getValueToWrite());
      }
    }
  }


  protected void setValueRead(String value){
     if (local != null){
       local.push(value);
     }
  }

  /**
   * read
   */
  public void readFromExternal() {
    if (external != null) {
      if (external.isInput()){
        this.setValueRead(external.getValue());
      }
    }
  }
}
