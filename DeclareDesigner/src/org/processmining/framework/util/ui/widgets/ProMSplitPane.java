package org.processmining.framework.util.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.fluxicon.slickerbox.components.RoundedPanel;

public class ProMSplitPane extends BorderPanel {
	private static class ImprovedSplitPaneDivider extends BasicSplitPaneDivider {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ImprovedSplitPaneDivider(final BasicSplitPaneUI ui) {
			super(ui);
		}

		@Override
		public void paint(final Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			paintComponents(g);
		}

		@Override
		protected JButton createLeftOneTouchButton() {
			final JButton b = new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// Don't want the button to participate in focus traversable.
				@Override
				public boolean isFocusTraversable() {
					return false;
				}

				@Override
				public void paint(final Graphics g) {
					if (splitPane != null) {
						final int[] xs = new int[3];
						final int[] ys = new int[3];
						int blockSize;

						// Fill the background first ...
						g.setColor(ImprovedSplitPaneDivider.this.getBackground());
						g.fillRect(0, 0, getWidth(), getHeight());

						// ... then draw the arrow.
						g.setColor(ImprovedSplitPaneDivider.this.getForeground());
						if (orientation == JSplitPane.VERTICAL_SPLIT) {
							blockSize = Math.min(getHeight(), BasicSplitPaneDivider.ONE_TOUCH_SIZE);
							xs[0] = blockSize;
							xs[1] = 0;
							xs[2] = blockSize << 1;
							ys[0] = 0;
							ys[1] = ys[2] = blockSize;
							g.drawPolygon(xs, ys, 3); // Little trick to make the
							// arrows of equal size
						} else {
							blockSize = Math.min(getWidth(), BasicSplitPaneDivider.ONE_TOUCH_SIZE);
							xs[0] = xs[2] = blockSize;
							xs[1] = 0;
							ys[0] = 0;
							ys[1] = blockSize;
							ys[2] = blockSize << 1;
						}
						g.fillPolygon(xs, ys, 3);
					}
				}

				@Override
				public void setBorder(final Border b) {
				}
			};
			b.setMinimumSize(new Dimension(BasicSplitPaneDivider.ONE_TOUCH_SIZE, BasicSplitPaneDivider.ONE_TOUCH_SIZE));
			b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			b.setFocusPainted(false);
			b.setBorderPainted(false);
			b.setRequestFocusEnabled(false);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					leftClicked();
				}
			});
			return b;
		}

		/**
		 * Creates and return an instance of JButton that can be used to
		 * collapse the right component in the split pane.
		 */
		@Override
		protected JButton createRightOneTouchButton() {
			final JButton b = new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// Don't want the button to participate in focus traversable.
				@Override
				public boolean isFocusTraversable() {
					return false;
				}

				@Override
				public void paint(final Graphics g) {
					if (splitPane != null) {
						final int[] xs = new int[3];
						final int[] ys = new int[3];
						int blockSize;

						// Fill the background first ...
						g.setColor(ImprovedSplitPaneDivider.this.getBackground());
						g.fillRect(0, 0, getWidth(), getHeight());

						// ... then draw the arrow.
						if (orientation == JSplitPane.VERTICAL_SPLIT) {
							blockSize = Math.min(getHeight(), BasicSplitPaneDivider.ONE_TOUCH_SIZE);
							xs[0] = blockSize;
							xs[1] = blockSize << 1;
							xs[2] = 0;
							ys[0] = blockSize;
							ys[1] = ys[2] = 0;
						} else {
							blockSize = Math.min(getWidth(), BasicSplitPaneDivider.ONE_TOUCH_SIZE);
							xs[0] = xs[2] = 0;
							xs[1] = blockSize;
							ys[0] = 0;
							ys[1] = blockSize;
							ys[2] = blockSize << 1;
						}
						g.setColor(ImprovedSplitPaneDivider.this.getForeground());
						g.fillPolygon(xs, ys, 3);
					}
				}

				@Override
				public void setBorder(final Border border) {
				}
			};
			b.setMinimumSize(new Dimension(BasicSplitPaneDivider.ONE_TOUCH_SIZE, BasicSplitPaneDivider.ONE_TOUCH_SIZE));
			b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			b.setFocusPainted(false);
			b.setBorderPainted(false);
			b.setRequestFocusEnabled(false);
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					rightClicked();
				}
			});
			return b;
		}

		protected void leftClicked() {

		}

		protected void rightClicked() {

		}
	}

	public static final int HORIZONTAL_SPLIT = JSplitPane.HORIZONTAL_SPLIT;
	public static final int VERTICAL_SPLIT = JSplitPane.VERTICAL_SPLIT;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel top, bottom;
	JSplitPane split;

	public ProMSplitPane() {
		this(ProMSplitPane.HORIZONTAL_SPLIT);
	}

	public ProMSplitPane(final int split) {
		super(5, 5);
		setLayout(new BorderLayout());
		setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		setForeground(WidgetColors.COLOR_ENCLOSURE_BG);
		setOpaque(true);
		top = new RoundedPanel(10);
		top.setLayout(new BorderLayout());
		top.setBackground(Color.LIGHT_GRAY);
		top.setOpaque(true);
		bottom = new RoundedPanel(10);
		bottom.setLayout(new BorderLayout());
		bottom.setBackground(Color.LIGHT_GRAY);
		bottom.setOpaque(true);
		this.split = new JSplitPane(split, true);
		this.split.setLeftComponent(top);
		this.split.setRightComponent(bottom);
		this.split.setBorder(BorderFactory.createEmptyBorder());
		this.split.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		this.split.setOpaque(true);
		this.split.setUI(new BasicSplitPaneUI() {
			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				final BasicSplitPaneDivider divider = new ImprovedSplitPaneDivider(this) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void leftClicked() {
						ProMSplitPane.this.leftClicked();
					}

					@Override
					public void rightClicked() {
						ProMSplitPane.this.rightClicked();
					}
				};
				divider.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
				divider.setForeground(Color.LIGHT_GRAY);
				return divider;
			}
		});
		add(this.split);
	}

	public void setBottomComponent(final Component c) {
		bottom.removeAll();
		bottom.add(c);
		validate();
	}

	public void setDividerLocation(final int location) {
		split.setDividerLocation(location);
	}

	public void setDividerSize(final int size) {
		split.setDividerSize(size);
	}

	public void setLeftComponent(final Component c) {
		setTopComponent(c);
	}

	public void setOneTouchExpandable(final boolean expandable) {
		split.setOneTouchExpandable(expandable);
	}

	public void setResizeWeight(final double d) {
		split.setResizeWeight(d);
	}

	public void setRightComponent(final Component c) {
		setBottomComponent(c);
	}

	public void setTopComponent(final Component c) {
		top.removeAll();
		top.add(c);
		validate();
	}

	protected void leftClicked() {

	}

	protected void rightClicked() {

	}
}
