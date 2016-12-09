package nl.tue.declare.appl.util;


import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;

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
public class FrmSelectFromList
    extends OkCancelDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = 6955164708422742965L;


  private JList list = new JList();
  private JScrollPane scrollPane = new JScrollPane(list);


  public FrmSelectFromList(Frame parent, String title,
                           Container aMonitorFrame) {
    super(parent, title, aMonitorFrame);
    
  }


  public FrmSelectFromList(Dialog parent, String title,
                           Container aMonitorFrame) {
    super(parent, title, aMonitorFrame);
  }

  public void fillList(Iterable<?> anList) {
    this.fillList(anList, list);
  }

  public Object getSelected() {
    return this.getSelecetdList(list);
  }

  public Object[] getSelectedMultiple() {
    return this.getSelectedMultipleList(list);
  }

  public void setMultipleSelectionMode() {
    this.list.getSelectionModel().setSelectionMode(ListSelectionModel.
        MULTIPLE_INTERVAL_SELECTION);
  }

  public void setSingleSelectionMode() {
    this.list.getSelectionModel().setSelectionMode(ListSelectionModel.
        SINGLE_SELECTION);
  }

  public void setSelected(Iterable<?> sel) {
    for (Object o: sel) {
      list.setSelectedValue(o, true);
    }
  }

  /**
   * setBounds
   */
  public void setMyBounds(int width, int height) {
    Rectangle bounds = getBounds();
    setBounds(bounds.x, bounds.y, width, height);
  }

@Override
protected Component getContent() {
    JPanel content = new TPanel(new BorderLayout(2, 2));
    content.add(new JLabel("select one item:"), BorderLayout.NORTH);
    content.add(scrollPane, BorderLayout.CENTER);
    // *** set the dimension - otherwize whole frame too small
    //content.setPreferredSize(new Dimension(175, 250));
    return content;
}
}
