package org.processmining.framework.util.ui.widgets;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Images useful for tabbed panes
 * 
 * @author mwesterg
 * 
 */
public interface WidgetImages {
	final static Image inspectorIcon = new ImageIcon(WidgetImages.class.getResource("resources/inspector48.png"))
			.getImage();
	final static Image summaryIcon = new ImageIcon(WidgetImages.class.getResource("resources/summary48-2.png"))
			.getImage();
	final static Image dashboardIcon = new ImageIcon(WidgetImages.class.getResource("resources/dashboard48.png"))
			.getImage();

}
