package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public abstract class CustomButton extends JButton {
	private static final int MARGIN = 6; //pixel of margin

	private Dimension dim;

	protected static final Border getNormalBorder() {
		return BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	}

	protected static final Border getPressedBorder() {
		return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	}

	protected CustomButton(String icon) {
		super();
		
		icon = "/icons" + icon;

		ImageIcon ico = new ImageIcon(getClass().getResource(icon));

		int dim = Math.max(ico.getIconWidth(), ico.getIconHeight()) + MARGIN;
		
		doIt(ico, dim);
	}

	protected CustomButton(String icon, int dim) {
		super();
		
		ImageIcon ico = new ImageIcon(getClass().getResource(icon));

		doIt(ico, dim);
	}

	private void doIt (ImageIcon icon, int dim) {
		

		setIcon(icon);
		setText("");

		this.dim = new Dimension(dim, dim);

		setBorder(getNormalBorder());

		setContentAreaFilled(false);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (!evt.getComponent().isEnabled()
						|| evt.getButton() != MouseEvent.BUTTON1)
					return;
				mouseClickedHandler(evt);
			}

			public void mousePressed(MouseEvent evt) {
				if (!evt.getComponent().isEnabled()
						|| evt.getButton() != MouseEvent.BUTTON1)
					return;
				setBorder(getPressedBorder());
			}

			public void mouseReleased(MouseEvent evt) {
				setBorder(getNormalBorder());
			}

		});

	}

	public Dimension getMaximumSize() {
		return dim;
	}

	public Dimension getMinimumSize() {
		return dim;
	}

	public Dimension getPreferredSize() {
		return dim;
	}

	public abstract void mouseClickedHandler(MouseEvent evt);

}
