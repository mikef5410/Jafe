package net.nohaven.proj.jafe.gui.listener;

import javax.swing.event.*;

import net.nohaven.proj.jafe.utils.*;

public class TreeRefresherListener implements DocumentListener {
	public static final TreeRefresherListener instance = new TreeRefresherListener();

	private TreeRefresherListener() {
		super();
	}

	public void insertUpdate(DocumentEvent arg0) {
		Context.getSidePanel().refreshTree();
	}

	public void removeUpdate(DocumentEvent arg0) {
		Context.getSidePanel().refreshTree();
	}

	public void changedUpdate(DocumentEvent arg0) {
		Context.getSidePanel().refreshTree();
	}

}
