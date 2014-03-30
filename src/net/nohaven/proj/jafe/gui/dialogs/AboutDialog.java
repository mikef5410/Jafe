package net.nohaven.proj.jafe.gui.dialogs;

import java.awt.*;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class AboutDialog extends CustomOkDialog {
	private static final long serialVersionUID = 6861077240421817042L;

	public AboutDialog(Frame parent) throws HeadlessException {
		super(parent);
		render();
	}

	protected void initComponents(JPanel content) {
		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow(".a***.g2b<<");
		layout.addRow("4****4|.<<<");
		layout.addRow(".****.|.<<<");
		layout.addRow(".***a.g.<<b");

		ZoneLayout blayout = ZoneLayoutFactory.newZoneLayout();
		blayout.addRow("b<b");
		blayout.addRow(".5.");
		blayout.addRow("c<c");
		blayout.addRow(".5.");
		blayout.addRow("s-s");
		blayout.addRow(".5.");
		blayout.addRow("d<d");
		blayout.addRow(".5.");
		blayout.addRow("e<e");
		blayout.addRow(".5.");
		blayout.addRow("t-t");
		blayout.addRow(".5.");
		blayout.addRow("f<f");
		blayout.addRow(".5.");
		blayout.addRow("g<g");
		blayout.addRow(".5.");
		blayout.addRow("h<h");

		JPanel p = new JPanel(blayout);

		JLabel l = new JLabel();
		l.setIcon(new ImageIcon(getClass().getResource("/icons/48/jafe.png")));
		l.setBounds(0, 00, 90, 90);

		content.setLayout(layout);
		content.add(l, "a");

		p.add(new JLabel("Jafe " + Constants.VERSION), "b");
		p.add(new JLabel(J18n._("A secure strongbox for your data")), "c");
		p.add(CustomSeparator.getHorizontalSeparator(), "s");
		p
				.add(new JLabel(
						"(c) 2006, Germano Rizzo <projects@nohaven.net>"), "d");
		p.add(new JLabel(J18n._("Licensed under the BSD License")), "e");
		p.add(CustomSeparator.getHorizontalSeparator(), "t");
		p.add(new JLabel(J18n._("Translators")+":"), "f");
		p.add(
				new JLabel(
						"Germano Rizzo <projects@nohaven.net> (it)"),
				"g");
		p.add(
				new JLabel(
						"Marian-Nicolae V. Ion <marian_ion@noos.fr> (fr, ro)"),
				"h");

		content.add(CustomSeparator.getVerticalSeparator(), "g");

		content.add(p, "b");
	}

	protected void initI18n() {
		setTitle(J18n._("About"));
	}

	protected void btOkPressed() {
		dispose();
	}
}
