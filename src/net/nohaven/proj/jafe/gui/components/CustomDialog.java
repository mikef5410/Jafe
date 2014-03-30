package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.atticlabs.zonelayout.swing.*;

public abstract class CustomDialog extends JDialog {
	protected CustomDialog(Frame parent) throws HeadlessException {
		super(parent, true);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow(".5...........");
		layout.addRow("2a...+*......");
		layout.addRow("...........a.");
		layout.addRow("......6......");
		layout.addRow(".s----~----s.");
		layout.addRow("......5......");
		layout.addRow(".b-------->b2");
		layout.addRow("...........6.");

		setLayout(layout);

		JPanel content = new JPanel();
		initComponents(content);
		add(content, "a");

		add(CustomSeparator.getHorizontalSeparator(), "s");

		initI18n();
	}

	protected final void addButtons(JPanel buttons) {
		add(buttons, "b");
	}

	private int minW, minH;

	protected void render() {
		pack();

		setLocationRelativeTo(getParent());

		//a simplicistic way to enforce the minimum size of a dialog
		//FIXME does it really work?
		minW = getWidth();
		minH = getHeight();
		addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent arg0) {
				if (getWidth() < minW || getHeight() < minH)
					setSize(Math.max(getWidth(), minW), Math.max(getHeight(),
							minH));
			}
		});

		setVisible(true);
	}

	protected abstract void initComponents(JPanel content);

	protected abstract void initI18n();
}
