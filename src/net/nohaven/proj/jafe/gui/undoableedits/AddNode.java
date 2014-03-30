package net.nohaven.proj.jafe.gui.undoableedits;

import javax.swing.undo.*;

import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

public class AddNode extends AbstractUndoableEdit {
    private static final long serialVersionUID = -8947514548013891696L;
	
	private Node son;
	private Node father;
	private int pos;

	public AddNode(Node son, Node father, int pos) {
		super();
		this.son = son;
		this.father = father;
		this.pos = pos;
	}

	public boolean canRedo() {
		return true;
	}

	public boolean canUndo() {
		return true;
	}

	public void undo() throws CannotRedoException {
		father.getTree().delete(son);
		Context.getSidePanel().selectNode(father);
		Context.getSidePanel().updateTree();
		Context.getMainPanel().markUnsavedState();
	}

	public void redo() throws CannotUndoException {
		father.addSonAtPosition(son, pos);
		Context.getSidePanel().selectNode(son);
		Context.getSidePanel().updateTree();
		Context.getMainPanel().markUnsavedState();
	}
}
