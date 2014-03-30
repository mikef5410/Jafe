package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.awt.event.*;

public abstract class CustomOkCancelDialog extends CustomDialog {
	private OkCancelButtonPanel buttons;
	
	public CustomOkCancelDialog(Frame parent)
			throws HeadlessException {
		super(parent);

		buttons = new OkCancelButtonPanel() {
			private static final long serialVersionUID = -7651997789587642239L;

			public void jbOkMouseClickedHdlr(MouseEvent evt) {
				btOkPressed();
			}

			public void jbCancelMouseClickedHdlr(MouseEvent evt) {
				btCancelPressed();
			}
		};
		
		addButtons(buttons);
	}

	protected abstract void btOkPressed();
	
	protected abstract void btCancelPressed();

	public final void enableOkButton() {
		buttons.enableOkButton();
	}

	public final void disableOkButton() {
		buttons.disableOkButton();
	}
}
