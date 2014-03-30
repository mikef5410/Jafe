package net.nohaven.proj.jafe.gui.components;

import java.awt.event.*;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;

import com.atticlabs.zonelayout.swing.*;

public abstract class OkCancelButtonPanel extends JPanel {
	private javax.swing.JButton jbCancel;

	private javax.swing.JButton jbOk;

	public OkCancelButtonPanel() {
		super();

		ZoneLayout flayout = ZoneLayoutFactory.newZoneLayout();
		flayout.addRow(".a>a2b>b");

		jbOk = new javax.swing.JButton();
		jbCancel = new javax.swing.JButton();

		setLayout(flayout);
		add(jbOk, "a");
		add(jbCancel, "b");

		jbOk.setText(J18n._("Ok"));
		jbCancel.setText(J18n._("Cancel"));

		jbOk.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() != MouseEvent.BUTTON1
						|| !evt.getComponent().isEnabled())
					return;

				jbOkMouseClickedHdlr(evt);
			}

		});

		jbCancel.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() != MouseEvent.BUTTON1
						|| !evt.getComponent().isEnabled())
					return;

				jbCancelMouseClickedHdlr(evt);
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

	public abstract void jbCancelMouseClickedHdlr(java.awt.event.MouseEvent evt);
}
