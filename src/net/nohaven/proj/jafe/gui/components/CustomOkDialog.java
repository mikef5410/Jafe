package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.awt.event.*;

public abstract class CustomOkDialog extends CustomDialog {
	private OkButtonPanel buttons;
	
	public CustomOkDialog(Frame parent)
			throws HeadlessException {
		super(parent);

		buttons = new OkButtonPanel() {
			private static final long serialVersionUID = -7651997789577642239L;

			public void jbOkMouseClickedHdlr(MouseEvent evt) {
				btOkPressed();
			}
		};
		addButtons(buttons);
	}

	protected abstract void btOkPressed();
	
	public final void enableOkButton() {
		buttons.enableOkButton();
	}

	public final void disableOkButton() {
		buttons.disableOkButton();
	}
}
