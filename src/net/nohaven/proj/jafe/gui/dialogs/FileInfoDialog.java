package net.nohaven.proj.jafe.gui.dialogs;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.security.enums.*;
import net.nohaven.proj.jafe.structure.*;

import com.atticlabs.zonelayout.swing.*;

public class FileInfoDialog extends CustomOkDialog {
	private static final long serialVersionUID = 4413226871310730022L;

	private JLabel jLabel1;

	private JLabel jLabel10;

	private JLabel jLabel11;

	private JLabel jLabel12;

	private JLabel jLabel2;

	private JLabel jLabel5;

	private JLabel jLabel6;

	private JLabel jLabel7;

	private JLabel jLabel9;
	
	public FileInfoDialog(java.awt.Frame parent, Tree t, CipherAlgorithm ca,
			CompressionMethod cm, String fileName) {
		super(parent);
		
		jLabel9.setText(Integer.toString(t.size()));

		if (fileName == null)
			jLabel10.setText("--");
		else
			jLabel10.setText(fileName);

		if (ca != null) {
			jLabel11.setText(ca.getName());
		} else {
			jLabel11.setText("--");
		}

		if (cm != null) {
			jLabel12.setText(cm.getName());
		} else {
			jLabel12.setText("--");
		}
		
		render();
	}

	protected void initI18n() {
		setTitle(J18n._("File informations"));
		jLabel1.setText("<html><h2>" + J18n._("File informations")
				+ "</h2></html>");
		jLabel2.setText(J18n._("Num. of nodes") + "...");
		jLabel5.setText(J18n._("File name") + "...");
		jLabel6.setText(J18n._("Cipher") + "...");
		jLabel7.setText(J18n._("Compression") + "...");
	}

	protected void initComponents(JPanel content) {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel5 = new JLabel();
		jLabel6 = new JLabel();
		jLabel7 = new JLabel();
		jLabel9 = new JLabel();
		jLabel10 = new JLabel();
		jLabel11 = new JLabel();
		jLabel12 = new JLabel();
		
		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow("a...!-....a");
		layout.addRow(".....5.....");
		layout.addRow("t----~----t");
		layout.addRow(".....6.....");
		layout.addRow("b<b3c-----c");
		layout.addRow(".....6.....");
		layout.addRow("d<d3e-----e");
		layout.addRow(".....6.....");
		layout.addRow("f<f3g-----g");
		layout.addRow(".....6.....");
		layout.addRow("h<h3i-----i");
		layout.addRow(".....6.....");
		layout.addRow("j<j3k-----k");
		
		content.setLayout(layout);
		
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		content.add(jLabel1, "a");

		content.add(jLabel2, "b");

		content.add(jLabel9, "c");

		content.add(jLabel5, "d");
		content.add(jLabel10, "e");

		content.add(jLabel6, "f");
		content.add(jLabel11, "g");

		content.add(jLabel7, "h");
		content.add(jLabel12, "i");

		content.add(CustomSeparator.getHorizontalSeparator(), "t");
	}

	protected void btOkPressed() {
		dispose();
	}
}
