package net.nohaven.proj.jafe.gui.undoableedits;

import javax.swing.undo.*;

import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

public class ChangeNode extends AbstractUndoableEdit {
	private static final long serialVersionUID = -8947514548013891696L;
	
	private Node nFrom;
	private Node nTo;

	public ChangeNode(Node nFrom, Node nTo) {
		super();
		this.nFrom = nFrom;
		this.nTo = nTo;
	}

	public boolean canRedo() {
		return true;
	}

	public boolean canUndo() {
		return true;
	}

	public void redo() throws CannotRedoException {
		Context.getSidePanel().selectNode(nTo);
	}

	public void undo() throws CannotUndoException {
		Context.getSidePanel().selectNode(nFrom);
	}
}
