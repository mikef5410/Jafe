package net.nohaven.proj.jafe.gui.nodepanels;

import net.nohaven.proj.jafe.gui.*;
import net.nohaven.proj.jafe.structure.nodes.*;

public class NodePanelFactory {
	private static NodePanel TEXT_NODE_PANEL;

	private static NodePanel ACCOUNT_NODE_PANEL;

	private static NodePanel DIR_NODE_PANEL;

	private static NodePanel UNKNOWN_NODE_PANEL;

	private static NodePanel SIMPLE_FILE_NODE_PANEL;

	public static NodePanel getPanel(Node n) {
		UndoDirector.ud.disableUndoRec();
		NodePanel panel = null;

		switch (n.getTypeID()) {
		case DirectoryNode.TYPE_ID:
		case RootNode.TYPE_ID: {
			if (DIR_NODE_PANEL == null)
				DIR_NODE_PANEL = new DirNodePanel();

			panel = DIR_NODE_PANEL;
			break;
		}

		case TextNode.TYPE_ID: {
			if (TEXT_NODE_PANEL == null)
				TEXT_NODE_PANEL = new TextNodePanel();

			panel = TEXT_NODE_PANEL;
			break;
		}

		case AccountNode.TYPE_ID: {
			if (ACCOUNT_NODE_PANEL == null)
				ACCOUNT_NODE_PANEL = new AccountNodePanel();

			panel = ACCOUNT_NODE_PANEL;
			break;
		}

		case SimpleFileNode.TYPE_ID: {
			if (SIMPLE_FILE_NODE_PANEL == null)
				SIMPLE_FILE_NODE_PANEL = new SimpleFileNodePanel();

			panel = SIMPLE_FILE_NODE_PANEL;
			break;
		}

		default: {
			if (UNKNOWN_NODE_PANEL == null)
				UNKNOWN_NODE_PANEL = new UnknownNodePanel();

			panel = UNKNOWN_NODE_PANEL;
		}
		}

		panel.setNode(n);

		UndoDirector.ud.enableUndoRec();

		return panel;
	}
}
