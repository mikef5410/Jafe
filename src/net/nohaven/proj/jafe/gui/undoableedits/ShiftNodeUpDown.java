package net.nohaven.proj.jafe.gui.undoableedits;

import javax.swing.undo.*;

import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

public class ShiftNodeUpDown extends AbstractUndoableEdit {
	private static final long serialVersionUID = 6639985573987570964L;

	private boolean isUp;

	private Node node;

	public ShiftNodeUpDown(Node n, boolean isUp) {
		super();
		this.isUp = isUp;
		node = n;
	}

	public boolean canRedo() {
		return true;
	}

	public boolean canUndo() {
		return true;
	}

	public void redo() throws CannotRedoException {
		if (isUp)
			shiftUp();
		else
			shiftDown();
		update ();
	}

	public void undo() throws CannotUndoException {
		if (isUp)
			shiftDown();
		else
			shiftUp();
		update ();
	}

	private void shiftDown() {
		node.getTree().shiftDown(node);	
	};

	private void shiftUp() {
		node.getTree().shiftUp(node);
	};
	
	private void update() {
		Context.getSidePanel().updateTree();
		Context.getMainPanel().markUnsavedState();
	}

}
