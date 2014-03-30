package net.nohaven.proj.jafe.gui.dialogs;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class PasswordDialog extends CustomOkCancelDialog {
	private static final long serialVersionUID = -4648463494263181294L;

	private ReferenceContainer rc;

	/** Creates new form PasswordDialog */
	public PasswordDialog(java.awt.Frame parent, ReferenceContainer rc) {
		super(parent);
		this.rc = rc;
		render();
	}

	protected void initI18n() {
		setTitle(J18n._("Enter password"));
		jLabel1.setText(J18n._("Password") + "...");
	}

	protected void initComponents(JPanel content) {
		jLabel1 = new JLabel();
		jPasswordField1 = new JPasswordField(32);

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow("a!..p.....");
		layout.addRow("...3.!~-..");
		layout.addRow("..a......p");
		
		

		content.setLayout(layout);
		content.add(jLabel1, "a");
		content.add(jPasswordField1, "p");

		jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jPasswordField1KeyReleased(evt);
			}
		});
	}

	private void jPasswordField1KeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() != 10) //return key
			return;
		
		btOkPressed();
	}

	protected void btCancelPressed () {
		dispose();
	}

	protected void btOkPressed() {
		rc.setContent(new String(jPasswordField1.getPassword()));
		dispose();
	}

	private JLabel jLabel1;

	private JPasswordField jPasswordField1;
}
