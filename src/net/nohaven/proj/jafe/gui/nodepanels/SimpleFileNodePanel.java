package net.nohaven.proj.jafe.gui.nodepanels;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.nohaven.proj.gettext4j.J18n;
import net.nohaven.proj.jafe.gui.components.CustomButton;
import net.nohaven.proj.jafe.gui.components.CustomFileDialog;
import net.nohaven.proj.jafe.gui.components.CustomSeparator;
import net.nohaven.proj.jafe.gui.listener.DefaultUndoableEditListener;
import net.nohaven.proj.jafe.structure.nodes.Node;
import net.nohaven.proj.jafe.structure.nodes.SimpleFileNode;
import net.nohaven.proj.jafe.utils.Context;
import net.nohaven.proj.jafe.utils.Utils;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

public class SimpleFileNodePanel extends NodePanel {
	private static final long serialVersionUID = 1097740657865666879L;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JLabel jLabel4;

	private JScrollPane jScrollPane1;

	private JButton jbReset;

	private JButton jbSaveFile;

	private JButton jbSet;

	private JLabel jlFileName;

	private JLabel jlFileSize;

	private JTextArea jtaContents;

	public SimpleFileNodePanel() {
		super();

		jtaContents.getDocument().addUndoableEditListener(
				DefaultUndoableEditListener.instance);
		jtaContents.getDocument().addDocumentListener(nlu);
	}

	protected void initI18n() {
		jLabel2.setText("<html><b>" + J18n._("File name") + "</b></html>");
		jLabel4.setText("<html><b>" + J18n._("File size") + "</b></html>");
		jbSet.setToolTipText(J18n._("Load file"));
		jbReset.setToolTipText(J18n._("Reset"));
		jbSaveFile.setToolTipText(J18n._("Save file"));
		jLabel3.setText(J18n._("Notes"));
	}

	protected void initComponents(JPanel contents) {
		jScrollPane1 = new JScrollPane();
		jtaContents = new JTextArea();
		jlFileName = new JLabel();
		jLabel2 = new JLabel();
		jLabel4 = new JLabel();
		jlFileSize = new JLabel();
		jLabel3 = new JLabel();
		
		//buttons
		jbSet = new CustomButton("/22/attach.png"){
			private static final long serialVersionUID = -6731621049201977712L;

			public void mouseClickedHandler(MouseEvent evt) {
				jbSetMouseClicked(evt);
			}			
		};
		
		jbSaveFile = new CustomButton("/22/filesave.png"){
			private static final long serialVersionUID = -6731622049201977712L;

			public void mouseClickedHandler(MouseEvent evt) {
				jbSaveFileMouseClicked(evt);
			}			
		};
		
		jbReset = new CustomButton("/22/eraser.png"){
			private static final long serialVersionUID = -6731623049201977712L;

			public void mouseClickedHandler(MouseEvent evt) {
				jbResetMouseClicked(evt);
			}			
		};

		jtaContents.setColumns(20);
		jtaContents.setRows(5);

		jScrollPane1.setViewportView(jtaContents);

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

		layout.addRow("t------~------t");
		layout.addRow("5..............");
		layout.addRow("a<a3b<--------b");
		layout.addRow(".......6.......");
		layout.addRow("c<c3d<--------d");
		layout.addRow("5..............");
		layout.addRow("u------~------u");
		layout.addRow("6..............");
		layout.addRow("s----~~----sefg");
		layout.addRow("h<h............");
		layout.addRow("5..............");
		layout.addRow("i+*...........i");

		contents.setLayout(layout);

		contents.add(jLabel2, "a");
		contents.add(jlFileName, "b");
		contents.add(jLabel4, "c");
		contents.add(jlFileSize, "d");
		contents.add(jbSet, "e");
		contents.add(jbSaveFile, "f");
		contents.add(jbReset, "g");

		contents.add(jLabel3, "h");
		contents.add(jScrollPane1, "i");

		contents.add(CustomSeparator.getHorizontalSeparator(), "t");
		contents.add(CustomSeparator.getHorizontalSeparator(), "u");
	}

	private void jbSaveFileMouseClicked(java.awt.event.MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		SimpleFileNode sfn = (SimpleFileNode) getNode();

		File fileToSave = CustomFileDialog.showSaveFileDialog(Context
				.getMainPanel(), new File(Context.getLastSetDir(), sfn
				.getFileName()));

		if (fileToSave == null)
			return;

		if (fileToSave.exists()) {
			int res = JOptionPane.showConfirmDialog(Context.getMainPanel(),
					J18n._("Do you wish to overwrite the existing file?"), J18n
							._("Confirmation needed"),
					JOptionPane.YES_NO_OPTION);

			if (res == JOptionPane.NO_OPTION)
				return;
		}

		try {
			Utils.backupFile(fileToSave);
			sfn.saveFile(fileToSave);
			Context.setLastSetDir(fileToSave.getParentFile());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, J18n
				._("Your file has been saved correctly"), J18n._("Ok"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void jbResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbResetMouseClicked
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		SimpleFileNode sfn = (SimpleFileNode) getNode();

		if (sfn.getFileName() != null) {
			int res = JOptionPane
					.showConfirmDialog(
							Context.getMainPanel(),
							J18n
									._("This action can't be undone. Do you want to continue?"),
							J18n._("Confirmation needed"),
							JOptionPane.YES_NO_OPTION);

			if (res == JOptionPane.NO_OPTION)
				return;
		}

		sfn.resetFile();

		jlFileName.setText("--");
		jlFileSize.setText("0 " + J18n._("bytes"));

		Context.getMainPanel().markUnsavedState();
	}

	private void jbSetMouseClicked(java.awt.event.MouseEvent evt) {
		if (!evt.getComponent().isEnabled()
				|| evt.getButton() != MouseEvent.BUTTON1)
			return;

		SimpleFileNode sfn = (SimpleFileNode) getNode();

		File fileToLoad = CustomFileDialog.showOpenFileDialog(Context
				.getMainPanel(), Context.getLastSetDir());

		if (fileToLoad == null)
			return;

		if (sfn.getFileName() != null) {
			int res = JOptionPane
					.showConfirmDialog(
							Context.getMainPanel(),
							J18n
									._("This action can't be undone. Do you want to continue?"),
							J18n._("Confirmation needed"),
							JOptionPane.YES_NO_OPTION);

			if (res == JOptionPane.NO_OPTION)
				return;
		}

		try {
			sfn.loadFile(fileToLoad);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		jlFileName.setText(sfn.getFileName());
		jlFileSize.setText(sfn.getFileSize() + " " + J18n._("bytes"));

		Context.setLastSetDir(fileToLoad.getParentFile());

		Context.getMainPanel().markUnsavedState();

		int res = JOptionPane.showConfirmDialog(Context.getMainPanel(), J18n
				._("Do you want to delete the original file?")
				+ "\n"
				+ J18n._("(It will be actually deleted when saving the file)"),
				J18n._("Option"), JOptionPane.YES_NO_OPTION);

		if (res == JOptionPane.YES_OPTION)
			Context.getMainPanel().addFileToDelete(fileToLoad);
	}

	protected void setNodeContentsToControl(Node n) {
		SimpleFileNode sfn = (SimpleFileNode) n;

		jtaContents.setText(sfn.getNotes());
		if (sfn.getFileName() == null) {
			jlFileName.setText("--");
			jlFileSize.setText("0 " + J18n._("bytes"));
		} else {
			jlFileName.setText(sfn.getFileName());
			jlFileSize.setText(sfn.getFileSize() + " " + J18n._("bytes"));
		}
	}

	protected void syncPanelContent() {
		SimpleFileNode sfn = (SimpleFileNode) getNode();

		if (!Utils.areStringsEquivalent(sfn.getNotes(), jtaContents.getText())) {
			Context.getMainPanel().markUnsavedState();
			sfn.setNotes(jtaContents.getText());
		}
	}
}
