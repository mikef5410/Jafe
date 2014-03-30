package net.nohaven.proj.jafe.gui.undoableedits;

import javax.swing.undo.*;

import net.nohaven.proj.jafe.structure.nodes.*;

//it's the same as AddNode, but undo and redo are inverted
public class DeleteNode extends AddNode {
    private static final long serialVersionUID = -8947514548013891696L;
	
	public DeleteNode(Node son, Node father, int pos) {
		super(son, father, pos);
	}

	public boolean canRedo() {
		return super.canUndo();
	}

	public boolean canUndo() {
		return super.canRedo();
	}

	public void redo() throws CannotRedoException {
		super.undo();
	}

	public void undo() throws CannotUndoException {
		super.redo();
	}
}
