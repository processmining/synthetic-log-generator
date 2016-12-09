package nl.tue.declare.appl.util;

import java.util.List;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

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

public class FrameUtil {

  /**
   * fillList
   *
   * @param anList List
   * @param anJList JList
   */
  public static void fillList(Object[] elements, JList list) {
		setListModel(generateListModel(elements),list);
  }
  
  /**
   * fillList
   *
   * @param anList List
   * @param anJList JList
   */
  public static void fillList(Iterable<?> elements, JList list) {
	  setListModel(generateListModel(elements),list);
  }
  
  /**
   * fillList
   *
   * @param anList List
   * @param anJList JList
   */
  public static void clearList(JList list) {
	  setListModel(generateListModel(),list);
  }
  
  private static void setListModel(ListModel model, JList list){
	    list.setModel(model);
	    if (model.getSize() > 0) {
	      list.setSelectedIndex(0);
	    }	  
  }
  
  private static ListModel generateListModel(Object[] elements){
	    DefaultListModel listModel = new DefaultListModel();
	    if (elements != null) {
	       for (Object o: elements) {
	         listModel.addElement(o);
	      }
	    }
	    return listModel;
  }
  
  private static DefaultListModel generateListModel(Iterable<?> elements){
	    DefaultListModel listModel = generateListModel();
	    if (elements != null) {
	       for (Object o: elements) {
	         listModel.addElement(o);
	      }
	    }
	    return listModel;
 }
  
  private static DefaultListModel generateListModel(){
	    return new DefaultListModel();
   }


  public static void iniList(JList anJList) {
    anJList.setModel(new DefaultListModel());
  }

  public static void addToList(JList list, Object object) {
    ListModel model = list.getModel();
    if (model == null) {
      model = new DefaultListModel();
      list.setModel(model);
    }
    ;
    if (model instanceof DefaultListModel) {
      DefaultListModel listModel = (DefaultListModel) model;
      listModel.addElement(object);
    }
  }

  public static void removeFromList(JList list, Object object) {
    ListModel model = list.getModel();
    if (model == null) {
      model = new DefaultListModel();
      list.setModel(model);
    }
    ;
    if (model instanceof DefaultListModel) {
      DefaultListModel listModel = (DefaultListModel) model;
      listModel.removeElement(object);
    }
  }

  public static void removeFromList(JList list, int index) {
    ListModel model = list.getModel();
    if (model == null) {
      model = new DefaultListModel();
      list.setModel(model);
    }
    ;
    if (model instanceof DefaultListModel) {
      DefaultListModel listModel = (DefaultListModel) model;
      listModel.removeElementAt(index);
    }
  }

  /**
   * getSelecetdList
   *
   * @param anJList JList
   * @return Object
   */
  public static Object getSelecetdList(JList anJList) {
    return anJList.getSelectedValue();
  }

  /**
   * getSelecetdList
   *
   * @param anJList JList
   * @return Object
   */
  public static Object[] getSelecetdAllList(JList anJList) {
    return anJList.getSelectedValues();
  }

  public static int indexList(JList list, Object object) {
    ListModel model = list.getModel();
    if (model == null) {
      model = new DefaultListModel();
      list.setModel(model);
    }
    ;
    if (model instanceof DefaultListModel) {
      DefaultListModel listModel = (DefaultListModel) model;
      return listModel.indexOf(object);
    }
    return -1;
  }

  public static void readOnly(JTextComponent c, Component parent) {
    c.setEditable(false);
    c.setForeground(parent.getForeground());
    c.setBackground(parent.getBackground());
  }

  /**
   * getSelecetdList
   *
   * @param anJList JList
   * @return Object
   */
  public static Object getItemList(JList anJList, int index) {
    return anJList.getModel().getElementAt(index);
  }

  /**
   *
   * @param list JList
   * @param selected Object
   */
  public static void setSelectedList(JList list, Object selected) {
    if (list == null) {
      return;
    }
    if (list.getModel().getSize() == 0) {
      return;
    }
    if (selected == null) {
      list.setSelectedIndex(0);
    }
    else {
      list.setSelectedValue(selected, true);
    }
  }

  /**
   * fillList
   *
   * @param anJList JList
   * @return Object
   */
  protected static Object[] getSelectedMultipleList(JList anJList) {
    Object[] el = null;
    el = anJList.getSelectedValues();
    return el;
  }

  public static void addToComboBox(JComboBox list, Object object) {
    ComboBoxModel model = list.getModel();
    if (model == null) {
      model = new DefaultComboBoxModel();
      list.setModel(model);
    }
    ;
    if (model instanceof DefaultComboBoxModel) {
      DefaultComboBoxModel listModel = (DefaultComboBoxModel) model;
      listModel.addElement(object);
    }
  }

  public static void fillComboBox(JComboBox combo, List<Object> list) {
    ComboBoxModel model = combo.getModel();
    if (model == null) {
      model = new DefaultComboBoxModel();
      combo.setModel(model);
    }
    ;
    if (model instanceof DefaultComboBoxModel) {
      DefaultComboBoxModel listModel = (DefaultComboBoxModel) model;
      listModel.removeAllElements();
      for (int i = 0; i < list.size(); i++) {
        listModel.addElement(list.get(i));
      }
    }
  }

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
 /* public static void setButtonOk(JButton anButton) {
    anButton.setText(BUTTON_OK);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
  /*public static void setButtonCancel(JButton anButton) {
    anButton.setText(BUTTON_CANCEL);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
  /*public static void setButtonYes(JButton anButton) {
    anButton.setText(BUTTON_YES);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
 /* public static void setButtonNo(JButton anButton) {
    anButton.setText(BUTTON_NO);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
 /* public static void setButtonAdd(JButton anButton) {
    anButton.setText(BUTTON_ADD);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
  /*public static void setButtonEdit(JButton anButton) {
    anButton.setText(BUTTON_EDIT);
  }*/

  /**
   * setButtonOk
   *
   * @param anButton JButton
   */
  /*public static void setButtonDelete(JButton anButton) {
    anButton.setText(BUTTON_DELETE);
  }*/

  public static void setSize(JComponent componenet, Dimension dimension) {
    componenet.setMinimumSize(dimension);
    componenet.setMaximumSize(dimension);
    componenet.setPreferredSize(dimension);
  }

}
