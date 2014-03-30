package net.nohaven.proj.jafe.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.gui.dialogs.*;
import net.nohaven.proj.jafe.gui.model.*;
import net.nohaven.proj.jafe.gui.nodepanels.*;
import net.nohaven.proj.jafe.gui.undoableedits.*;
import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class SidePanel extends javax.swing.JDialog {
	private static final long serialVersionUID = 2531370502297968573L;

	private Tree tree;

	private JButton btAdd;

	private JButton btDel;

	private JButton btDown;

	private JButton btUp;

	private JPanel jPanel2;

	private JPanel jPanel1;

	private JScrollPane jScrollPane1;

	private JTree jTree1;

	public SidePanel(java.awt.Frame parent, Tree tree) {
		super(parent, false);
		initComponents();

		initI18n();

		Context.setSidePanel(this);

		setNewTree(tree);

		jTree1.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		jTree1.setCellRenderer(new TreeCellIconProvider());
	}

	private void initI18n() {
		btUp.setToolTipText(J18n._("Shift up item"));
		btDown.setToolTipText(J18n._("Shift down item"));
		btAdd.setToolTipText(J18n._("Add new item") + "...");
		btDel.setToolTipText(J18n._("Delete item"));
	}

	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTree1 = new javax.swing.JTree();

		//buttons
		btUp = new CustomButton("/22/1uparrow.png") {
			private static final long serialVersionUID = 154370857345032475L;

			public void mouseClickedHandler(MouseEvent evt) {
				btUpMouseClicked(evt);
			}

		};

		btDown = new CustomButton("/22/1downarrow.png") {
			private static final long serialVersionUID = 254370857345032475L;

			public void mouseClickedHandler(MouseEvent evt) {
				btDownMouseClicked(evt);
			}

		};

		btAdd = new CustomButton("/22/edit_add.png") {
			private static final long serialVersionUID = 354370857345032475L;

			public void mouseClickedHandler(MouseEvent evt) {
				btAddMouseClicked(evt);
			}

		};

		btDel = new CustomButton("/22/editdelete.png") {
			private static final long serialVersionUID = 454370857345032475L;

			public void mouseClickedHandler(MouseEvent evt) {
				btDelMouseClicked(evt);
			}

		};
		
		jPanel2.add(btUp);
		jPanel2.add(btDown);
		jPanel2.add(btAdd);
		jPanel2.add(btDel);

		jScrollPane1.setViewportView(jTree1);

		setUndecorated(true);

		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent evt) {
				reposition();
			}
		});

		jTree1.setLargeModel(true);
		jTree1
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							javax.swing.event.TreeSelectionEvent evt) {
						jTree1ValueChanged(evt);
					}
				});

		jPanel1.setBorder(javax.swing.BorderFactory
				.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

		jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1,
				1));

		int bdim = btUp.getSize().height;
		int margin = bdim / 4;

		btUp.setBounds(0, 0, bdim, bdim);
		btDown.setBounds(bdim + margin, 0, bdim, bdim);
		btUp.setBounds(2 * (bdim + margin), 0, bdim, bdim);
		btUp.setBounds(3 * (bdim + margin), 0, bdim, bdim);

		jPanel2.setSize(new Dimension(3 * (bdim + margin) + bdim, bdim));

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow("a*+...");
		layout.addRow("......");
		layout.addRow("......");
		layout.addRow("......");
		layout.addRow("......");
		layout.addRow(".....a");
		layout.addRow("b.....");
		layout.addRow(".....b");
		
		jPanel1.setLayout(layout);

		jPanel1.add(jScrollPane1, "a");
		jPanel1.add(jPanel2, "b");

		add(jPanel1);

		setResizable(false);
		
		pack();
	}

	private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
		Node from = null;
		if (UndoDirector.ud.isUndoRecEnabled()
				&& Context.getMainPanel().getNodePanel() != null)
			from = Context.getMainPanel().getNodePanel().getNode();

		Node to = (Node) evt.getPath().getLastPathComponent();
		NodePanel newPanel = NodePanelFactory.getPanel(to);

		//registers to the undo	        
		UndoDirector.ud.register(new ChangeNode(from, to));

		Context.getMainPanel().setNodePanel(newPanel);

		//update the buttons abilitation
		updateButtonAbilitation(to);
	}

	private void btUpMouseClicked(MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		Node selected = (Node) jTree1.getSelectionPath().getLastPathComponent();

		if ((selected == null) || (selected.getLevel() == 0))
			return;

		tree.shiftUp(selected);

		jTree1.updateUI();
		selectNode(selected);

		UndoDirector.ud.register(new ShiftNodeUpDown(selected, true));

		Context.getMainPanel().markUnsavedState();
	}

	private void btDownMouseClicked(MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		Node selected = (Node) jTree1.getSelectionPath().getLastPathComponent();

		if ((selected == null) || (selected.getLevel() == 0))
			return;

		tree.shiftDown(selected);

		jTree1.updateUI();
		selectNode(selected);

		UndoDirector.ud.register(new ShiftNodeUpDown(selected, false));

		Context.getMainPanel().markUnsavedState();
	}

	private void btDelMouseClicked(MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		Node selected = (Node) jTree1.getSelectionPath().getLastPathComponent();

		if (selected == null) {
			JOptionPane.showMessageDialog(Context.getMainPanel(), J18n
					._("You must select a node to do this."), J18n._("Error"),
					JOptionPane.ERROR_MESSAGE);

		} else if (selected.getLevel() == 0) {
			JOptionPane.showMessageDialog(Context.getMainPanel(), J18n
					._("You can't do this on the root node."), J18n._("Error"),
					JOptionPane.ERROR_MESSAGE);

		} else {
			int n = JOptionPane.showConfirmDialog(Context.getMainPanel(),
					J18n._("Do you really want to delete this node?"), J18n
							._("Confirmation needed"),
					JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.NO_OPTION)
				return;

			Node ftr = selected.getFather();

			UndoDirector.ud.register(new DeleteNode(selected, ftr, ftr
					.getIdxOfSon(selected)));

			tree.delete(selected);
			updateTree();

			UndoDirector.ud.disableUndoRec();
			selectNode(ftr);
			UndoDirector.ud.enableUndoRec();

			Context.getMainPanel().markUnsavedState();
		}
	}

	private void btAddMouseClicked(MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		Node selected = (Node) jTree1.getSelectionPath().getLastPathComponent();

		if (selected == null) {
			JOptionPane.showMessageDialog(Context.getMainPanel(), J18n
					._("You must select a node to do this."), J18n._("Error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ReferenceContainer createdNode = new ReferenceContainer();
		new NewNodeDialog(Context.getMainPanel(), selected, createdNode);

		if (!createdNode.isEmpty()) {
			Node created = (Node) createdNode.getContent();

			UndoDirector.ud.register(new AddNode(created, created.getFather(),
					created.getFather().getIdxOfSon(created)));

			updateTree();
			Context.getMainPanel().markUnsavedState();

			UndoDirector.ud.disableUndoRec();
			selectNode(created);
			UndoDirector.ud.enableUndoRec();
		}
	}

	public void reposition() {
		MainPanel mainPanel = Context.getMainPanel();
		int top = mainPanel.getInsets().top;
		int bottom = mainPanel.getInsets().bottom
				+ mainPanel.getBottomAdditionalInset();

		int y = mainPanel.getLocation().y + top;
		int x = mainPanel.getLocation().x - this.getSize().width - 5;
		
		setBounds(x, y , getWidth(), mainPanel.getHeight()
				- top - bottom);
	}

	public void selectRoot() {
		selectNode(tree.getRoot());
	}

	public void selectNode(Node n) {
		jTree1.setSelectionPath(((JafeTreeModel) jTree1.getModel())
				.getTreePath(n));

		updateButtonAbilitation(n);
	}

	private void updateButtonAbilitation(Node n) {
		btDel.setEnabled(n.getLevel() != 0);
		btUp.setEnabled(!n.isFirstSibling());
		btDown.setEnabled(!n.isLastSibling());
	}

	public void setNewTree(Tree t) {
		this.tree = t;
		jTree1.setModel(new JafeTreeModel(t));
		jTree1.updateUI();
	}

	public void updateTree() {
		jTree1.updateUI();
	}

	public void refreshTree() {
		jTree1.repaint();
	}

	protected void syncLanguageModification() {
		initI18n();
	}
}
