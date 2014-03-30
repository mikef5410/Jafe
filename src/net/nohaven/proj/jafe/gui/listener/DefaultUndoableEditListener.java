package net.nohaven.proj.jafe.gui.listener;

import javax.swing.event.*;

import net.nohaven.proj.jafe.gui.*;

public class DefaultUndoableEditListener implements UndoableEditListener {
	public static final DefaultUndoableEditListener instance = new DefaultUndoableEditListener();

	private DefaultUndoableEditListener() {
		super();
	}

	public void undoableEditHappened(UndoableEditEvent evt) {
		UndoDirector.ud.register(evt.getEdit());
	}

}
