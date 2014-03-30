package net.nohaven.proj.jafe.gui.nodepanels;

import java.awt.*;

import javax.swing.*;

import com.atticlabs.zonelayout.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.structure.nodes.*;

public class UnknownNodePanel extends NodePanel {
	private static final long serialVersionUID = 5310469111063655821L;

	private static final Color RED_TEXT = new java.awt.Color(204, 0, 51);

	private static final Color RED_BORDER_1 = new java.awt.Color(255, 51, 0);

	private static final Color RED_BORDER_2 = new java.awt.Color(153, 0, 0);

	private JLabel jLabel2;

	private JScrollPane jScrollPane1;

	private JTextPane jTextPane1;

	public UnknownNodePanel() {
		super();
	}

	protected void initI18n() {
		jLabel2.setText(J18n._("Unknown node type"));
		jTextPane1
				.setText(J18n
						._("This node has been probably created with a later version of Jafe. \n\nThe content will be preserved when saving, but you won't be able to view it. \n\nPlease open this file with the appropriated version of Jafe."));
	}

	protected void initComponents(JPanel contents) {
		jLabel2 = new JLabel();
		jScrollPane1 = new JScrollPane();
		jTextPane1 = new JTextPane();

		jScrollPane1.setBorder(BorderFactory.createEtchedBorder(RED_BORDER_1,
				RED_BORDER_2));
		jLabel2.setForeground(RED_TEXT);

		jTextPane1.setEnabled(false);
		jScrollPane1.setViewportView(jTextPane1);

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

		layout.addRow("a<a");
		layout.addRow("b*.");
		layout.addRow(".+b");

		contents.setLayout(layout);

		contents.add(jLabel2, "a");
		contents.add(jScrollPane1, "b");
	}

	protected void setNodeContentsToControl(Node n) {
	}

	protected void syncPanelContent() {
	}
}
