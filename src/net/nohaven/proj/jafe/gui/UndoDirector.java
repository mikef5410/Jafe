package net.nohaven.proj.jafe.gui;

import javax.swing.undo.*;

//Facade for the undo manager
public class UndoDirector {
	public static final UndoDirector ud = new UndoDirector();
	
    private UndoManager undoManager;
    
    private int undoDisabilitationLevel = 0;

    private UndoDirector() {
		super();
		undoManager = new UndoManager();
		undoManager.setLimit(128);
	}

    public boolean isUndoRecEnabled() {
		return undoDisabilitationLevel == 0;
	}

	public void register (UndoableEdit ue) {
		if (isUndoRecEnabled())
			undoManager.addEdit(ue);
	}
	
	public void reset () {
		undoManager.discardAllEdits();
		undoDisabilitationLevel = 0;
	}
	
	public void undo () {
		if (undoManager.canUndo()){
			disableUndoRec();
            undoManager.undo();
            enableUndoRec();
		}
	}
	
	public void redo () {
		if (undoManager.canRedo()){
			disableUndoRec();
            undoManager.redo();
            enableUndoRec();
		}
	}
	
	public void enableUndoRec() {
		if (undoDisabilitationLevel > 0)
			undoDisabilitationLevel--;
	}

	public void disableUndoRec() {
		undoDisabilitationLevel++;
	}
}
