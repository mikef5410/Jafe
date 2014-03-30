package net.nohaven.proj.jafe.gui.components;

import java.awt.*;

import javax.swing.*;

public class CustomSeparator extends JSeparator {
	private static final long serialVersionUID = -1721642773724268369L;
	
	private static final Dimension DIM = new Dimension(3,3);
	
	private CustomSeparator(int orientation) {
		setOrientation(orientation);
		setPreferredSize(DIM);
	}
	
	public static final JSeparator getHorizontalSeparator () {
		return new CustomSeparator(CustomSeparator.HORIZONTAL);
	}
	
	public static final JSeparator getVerticalSeparator () {
		return new CustomSeparator(CustomSeparator.VERTICAL);
	}
}
