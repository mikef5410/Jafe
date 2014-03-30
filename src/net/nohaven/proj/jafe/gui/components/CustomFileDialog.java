package net.nohaven.proj.jafe.gui.components;

import java.awt.*;
import java.io.*;

import javax.swing.JFileChooser;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.utils.OSDetector;

public class CustomFileDialog {
	private static final long serialVersionUID = -3608091574656536647L;

	private static final int TYPE_OPEN = 0;

	private static final int TYPE_SAVE = 1;

	private static File showFileDialog(Frame parent, int type, File preset) {
		if (OSDetector.isMac())
			return handleFileDialog(parent, type, preset);
		return handleJFileChooser(parent, type, preset);
	}

	public static File showSaveFileDialog(Frame parent) {
		return showFileDialog(parent, TYPE_SAVE, null);
	}

	public static File showOpenFileDialog(Frame parent) {
		return showFileDialog(parent, TYPE_OPEN, null);
	}

	public static File showSaveFileDialog(Frame parent, File preset) {
		return showFileDialog(parent, TYPE_SAVE, preset);
	}

	public static File showOpenFileDialog(Frame parent, File preset) {
		return showFileDialog(parent, TYPE_OPEN, preset);
	}

	private static File showFileDialog(Dialog parent, int type, File preset) {
		if (OSDetector.isMac())
			return handleFileDialog(parent, type, preset);
		return handleJFileChooser(parent, type, preset);
	}

	public static File showSaveFileDialog(Dialog parent) {
		return showFileDialog(parent, TYPE_SAVE, null);
	}

	public static File showOpenFileDialog(Dialog parent) {
		return showFileDialog(parent, TYPE_OPEN, null);
	}

	public static File showSaveFileDialog(Dialog parent, File preset) {
		return showFileDialog(parent, TYPE_SAVE, preset);
	}

	public static File showOpenFileDialog(Dialog parent, File preset) {
		return showFileDialog(parent, TYPE_OPEN, preset);
	}

	private static File handleFileDialog(Dialog parent, int type, File preset) {
		FileDialog d = new FileDialog(parent);
		d.setLocationRelativeTo(parent);
		return handleFileDialog(d, type, preset);
	}

	private static File handleFileDialog(Frame parent, int type, File preset) {
		FileDialog d = new FileDialog(parent);
		d.setLocationRelativeTo(parent);
		return handleFileDialog(d, type, preset);
	}

	private static File handleFileDialog(FileDialog d, int type, File preset) {

		switch (type) {
		case TYPE_OPEN: {
			d.setMode(FileDialog.LOAD);
			d.setTitle(J18n._("Open"));
		}
			break;
		case TYPE_SAVE: {
			d.setMode(FileDialog.SAVE);
			d.setTitle(J18n._("Save"));
		}
			break;
		}

		if (preset != null) {
			if (preset.isDirectory()) {
				d.setDirectory(preset.getAbsolutePath());
			} else {
				d.setDirectory(preset.getParent());
				d.setFile(preset.getName());
			}
		}

		d.setVisible(true);

		if (d.getFile() == null)
			return null;
		return new File(d.getDirectory(), d.getFile());
	}

	private static File handleJFileChooser(Component parent, int type,
			File preset) {
		JFileChooser d = new JFileChooser() {
			private static final long serialVersionUID = -1294304212267515843L;

			public void cancelSelection() {
				setSelectedFile(null);
				super.cancelSelection();
			}
		};

		if (preset != null)
			d.setSelectedFile(preset);

		switch (type) {
		case TYPE_OPEN:
			d.showOpenDialog(parent);
			break;
		case TYPE_SAVE:
			d.showSaveDialog(parent);
			break;
		}

		return d.getSelectedFile();
	}
}
