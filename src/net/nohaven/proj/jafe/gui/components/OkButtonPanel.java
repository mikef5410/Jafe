package net.nohaven.proj.jafe.gui.components;

import java.awt.event.*;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;

import com.atticlabs.zonelayout.swing.*;

public abstract class OkButtonPanel extends JPanel {

	private javax.swing.JButton jbOk;

	public OkButtonPanel() {
		super();

		ZoneLayout flayout = ZoneLayoutFactory.newZoneLayout();
		flayout.addRow("a>a");

		jbOk = new javax.swing.JButton();

		setLayout(flayout);
		add(jbOk, "a");

		jbOk.setText(J18n._("Ok"));

		jbOk.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() != MouseEvent.BUTTON1
						|| !evt.getComponent().isEnabled())
					return;
				
				jbOkMouseClickedHdlr(evt);
			}

		});
	}

	public void enableOkButton() {
		jbOk.setEnabled(true);
	}

	public void disableOkButton() {
		jbOk.setEnabled(false);
	}

	public abstract void jbOkMouseClickedHdlr(java.awt.event.MouseEvent evt);

}
