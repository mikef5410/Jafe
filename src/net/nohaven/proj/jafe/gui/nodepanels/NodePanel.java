package net.nohaven.proj.jafe.gui.nodepanels;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.listener.*;
import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public abstract class NodePanel extends JPanel {
	private Node node;

	protected NodeUpdaterListener nlu;

	private JLabel jLabel1;

	private JTextField jtfTitle;

	protected NodePanel() {
		jLabel1 = new JLabel(J18n._("Title") + "...");
		jtfTitle = new JTextField();
		
		ZoneLayout alayout = ZoneLayoutFactory.newZoneLayout();
		
		alayout.addRow("a<a3b<~-b");
		
		JPanel jp = new JPanel(alayout);
		
		jp.add(jLabel1, "a");
		jp.add(jtfTitle, "b");

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

		layout.addRow("a---~---a");
		layout.addRow("....6....");
		layout.addRow("c...+....");
		layout.addRow("....*...c");

		setLayout(layout);

		add(jp, "a");

		JPanel content = new JPanel();
		initComponents(content);
		add(content, "c");

		initI18n();

		nlu = new NodeUpdaterListener(this);
		jtfTitle.getDocument().addUndoableEditListener(
				DefaultUndoableEditListener.instance);
		jtfTitle.getDocument().addDocumentListener(nlu);
		jtfTitle.getDocument().addDocumentListener(
				TreeRefresherListener.instance);
	}

	protected abstract void initComponents(JPanel contents);

	protected abstract void initI18n();

	public final Node getNode() {
		return node;
	}

	public final void setNode(Node n) {
		node = n;
		nlu.disable();
		setNodeContentsToControl();
		nlu.enable();
	}

	private final void setNodeContentsToControl() {
		jtfTitle.setText(node.getTitle());
		setNodeContentsToControl(node);
	}

	public final void syncContent() {
		if (!Utils.areStringsEquivalent(getNode().getTitle(), jtfTitle
				.getText())) {
			Context.getMainPanel().markUnsavedState();
			getNode().setTitle(jtfTitle.getText());
		}
		syncPanelContent();
	}

	protected abstract void setNodeContentsToControl(Node n);

	protected abstract void syncPanelContent();
}
