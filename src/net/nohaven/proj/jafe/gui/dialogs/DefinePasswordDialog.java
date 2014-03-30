package net.nohaven.proj.jafe.gui.dialogs;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.security.enums.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class DefinePasswordDialog extends CustomOkCancelDialog {
	private static final long serialVersionUID = -4648463494263181294L;

	private ReferenceContainer rcPwd;

	private ReferenceContainer rcAlgo;

	private ReferenceContainer rcComp;

	private JComboBox jComboBox1;

	private JComboBox jComboBox2;

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JLabel jLabel4;

	private JPasswordField jPasswordField1;

	private JPasswordField jPasswordField2;

	/** Creates new form PasswordDialog */
	public DefinePasswordDialog(java.awt.Frame parent,
			ReferenceContainer rcPwd, ReferenceContainer rcAlgo,
			ReferenceContainer rcComp) {
		super(parent);
		
		this.rcPwd = rcPwd;
		this.rcAlgo = rcAlgo;
		this.rcComp = rcComp;
	
		disableOkButton();
		render();
	}

	protected void initI18n() {
		setTitle(J18n._("Define encoding parameters"));
		jLabel1.setText(J18n._("Password"));
		jLabel2.setText(J18n._("Repeat it"));
		jLabel3.setText(J18n._("Encryption"));
		jLabel4.setText(J18n._("Compression"));
	}

	protected void initComponents(JPanel content) {
		jLabel1 = new JLabel();
		jPasswordField1 = new JPasswordField(32);
		jPasswordField2 = new JPasswordField(32);
		jLabel2 = new JLabel();
		jComboBox1 = new JComboBox();
		jLabel3 = new JLabel();
		jComboBox2 = new JComboBox();
		jLabel4 = new JLabel();

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow("a!<a3c!~-.c");
		layout.addRow("b!<b.d!~-.d");
		layout.addRow(".....5.....");
		layout.addRow("t----~----t");
		layout.addRow(".....5.....");
		layout.addRow("e!<e3h!~-.h");
		layout.addRow("g!<g.i!~-.i");

		content.setLayout(layout);
		content.add(jLabel1, "a");
		content.add(jLabel2, "b");
		content.add(jPasswordField1, "c");
		content.add(jPasswordField2, "d");
		content.add(jLabel3, "e");
		content.add(jLabel4, "g");
		content.add(jComboBox1, "h");
		content.add(jComboBox2, "i");

		content.add(CustomSeparator.getHorizontalSeparator(), "t");

		jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jPasswordField1KeyReleased(evt);
			}
		});

		jPasswordField2.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jPasswordField1KeyReleased(evt);
			}
		});

		jComboBox1.setModel(new DefaultComboBoxModel(
				CipherAlgorithm.getNames()));
		jComboBox1.setSelectedIndex(0);
		jComboBox2.setModel(new DefaultComboBoxModel(
				CompressionMethod.getNames()));
		jComboBox2.setSelectedIndex(1);
	}

	private void jPasswordField1KeyReleased(java.awt.event.KeyEvent evt) {
		if (jPasswordField1.getPassword().length == 0
				|| jPasswordField2.getPassword().length != jPasswordField1
						.getPassword().length) {
			disableOkButton();
			return;
		}
		for (int i = 0; i < jPasswordField1.getPassword().length; i++)
			if (jPasswordField2.getPassword()[i] != jPasswordField1
					.getPassword()[i]) {
				disableOkButton();
				return;
			}
		enableOkButton();
	}

	protected void btOkPressed() {
		rcPwd.setContent(new String(jPasswordField1.getPassword()));

		rcAlgo.setContent(CipherAlgorithm.values.get(jComboBox1
				.getSelectedIndex()));
		rcComp.setContent(CompressionMethod.values.get(jComboBox2
				.getSelectedIndex()));

		dispose();
	}

	protected void btCancelPressed() {
		dispose();
	}
}
