package nl.tue.declare.appl.worklist.gui;

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
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.worklist.DataComponentFactory;
import nl.tue.declare.execution.*;

public class DataPanel
    extends TPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4291971741055408371L;
JPanel panel;
  WorkItem workItem;
  Collection<IDataComponenet> componenets;

  DataPanel(WorkItem workItem) {
    super(new BorderLayout(), "data fields");
    this.workItem = workItem;
    panel = new JPanel();
    this.componenets = new ArrayList<IDataComponenet>();
    this.ini();
    if (workItem.dataCount() > 0) {
      this.add(new JScrollPane(panel), BorderLayout.NORTH);
    }
  }

  private void ini() {
    int count = workItem.dataCount();
    panel.setLayout(new GridLayout(count, 1));
    for (int i = 0; i < count; i++) {
      JComponent component = DataComponentFactory.createDataComponent(workItem.
          getDataAt(i));
      this.componenets.add( (IDataComponenet) component);
      panel.add(component);
    }
  }

  public boolean read() {
    Iterator<IDataComponenet> iterator = this.componenets.iterator();
    boolean ok = true;
    while (iterator.hasNext() && ok) {
      ok = iterator.next().readValue();
    }
    return ok;
  }
}
