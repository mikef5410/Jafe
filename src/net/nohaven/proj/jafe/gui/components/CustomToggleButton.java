package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public abstract class CustomToggleButton extends JToggleButton {
	private static final int DIM = 36;

	public CustomToggleButton(String icon, boolean selected) {
		super();

		icon = "/icons" + icon;
		setSelected(selected);

		setIcon(new javax.swing.ImageIcon(getClass().getResource(icon)));
		setText("");

		setMinimumSize(new Dimension(DIM, DIM));
		setMaximumSize(new Dimension(DIM, DIM));
		setPreferredSize(new Dimension(DIM, DIM));

		setBorder();

		setContentAreaFilled(false);

		addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				toggledHandler(evt);
				setBorder();
			}
		});
	}

	private void setBorder() {
		setBorder(isSelected() ? CustomButton.getPressedBorder() : CustomButton
				.getNormalBorder());
	}

	public abstract void toggledHandler(ItemEvent evt);

}
