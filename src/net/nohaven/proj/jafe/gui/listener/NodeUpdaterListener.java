package net.nohaven.proj.jafe.gui.listener;

import javax.swing.event.*;

import net.nohaven.proj.jafe.gui.nodepanels.*;

public class NodeUpdaterListener extends Object implements DocumentListener {
	private NodePanel node;

	private boolean enabled = true;

	public NodeUpdaterListener(NodePanel node) {
		super();
		this.node = node;
	}

	public void insertUpdate(DocumentEvent arg0) {
		if (enabled)
			node.syncContent();
	}

	public void removeUpdate(DocumentEvent arg0) {
		if (enabled)
			node.syncContent();
	}

	public void changedUpdate(DocumentEvent arg0) {
		if (enabled)
			node.syncContent();
	}

	public void disable() {
		enabled = false;
	}

	public void enable() {
		enabled = true;
	}
}
