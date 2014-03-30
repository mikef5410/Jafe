package net.nohaven.proj.jafe.gui.dialogs;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.nohaven.proj.gettext4j.J18n;
import net.nohaven.proj.jafe.gui.components.CustomFileDialog;
import net.nohaven.proj.jafe.gui.components.CustomOkCancelDialog;
import net.nohaven.proj.jafe.gui.components.CustomSeparator;
import net.nohaven.proj.jafe.security.JafeCipherOutputStream;
import net.nohaven.proj.jafe.security.enums.CipherAlgorithm;
import net.nohaven.proj.jafe.security.enums.CompressionMethod;
import net.nohaven.proj.jafe.security.steganography.StegOutputStream;
import net.nohaven.proj.jafe.structure.Tree;
import net.nohaven.proj.jafe.structure.TreeFactory;
import net.nohaven.proj.jafe.utils.Context;
import net.nohaven.proj.jafe.utils.Utils;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

public class EmbedIntoImageDialog extends CustomOkCancelDialog {
	private static final long serialVersionUID = 1841159752603356085L;

	private File src;

	private File dest;

	private Tree tree;

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JLabel jLabel4;

	private JLabel jLabel5;

	private JLabel jLabel6;

	private JButton jbChooseDest;

	private JButton jbChooseImg;

	private JComboBox jcbCompress;

	private JComboBox jcbEncrypt;

	private JPasswordField jpfPwd1;

	private JPasswordField jpfPwd2;

	private JTextField jtfDest;

	private JTextField jtfImg;

	/** Creates new form EmbedIntoImageDialog */
	public EmbedIntoImageDialog(Tree t) {
		super(Context.getMainPanel());

		tree = t;

		jcbEncrypt
				.setModel(new DefaultComboBoxModel(CipherAlgorithm.getNames()));
		jcbEncrypt.setSelectedIndex(0);

		jcbCompress.setModel(new DefaultComboBoxModel(CompressionMethod
				.getNames()));
		jcbCompress.setSelectedIndex(1);

		render();
	}

	protected void initI18n() {
		setTitle(J18n._("Embedding parameters"));
		jLabel1.setText(J18n._("Original image") + ":");
		jbChooseImg.setText(J18n._("Choose") + "...");
		jLabel2.setText(J18n._("Password") + ":");
		jLabel3.setText(J18n._("Repeat it") + ":");
		jLabel4.setText(J18n._("Encryption") + ":");
		jLabel5.setText(J18n._("Compression") + ":");
		jbChooseDest.setText(J18n._("Choose") + "...");
		jLabel6.setText(J18n._("File to save the image into (PNG format)")
				+ ":");
	}

	protected void initComponents(JPanel content) {
		jLabel1 = new JLabel();
		jtfImg = new JTextField(32);
		jbChooseImg = new JButton();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		jpfPwd1 = new JPasswordField();
		jcbCompress = new JComboBox();
		jcbEncrypt = new JComboBox();
		jpfPwd2 = new JPasswordField();
		jtfDest = new JTextField(32);
		jbChooseDest = new JButton();
		jLabel6 = new JLabel();

		jtfImg.setEditable(false);
		jtfDest.setEditable(false);

		jbChooseImg.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jbChooseImgMouseClicked(evt);
			}
		});

		jpfPwd1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				checkAbilitationOkButton();
			}
		});

		jpfPwd2.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				checkAbilitationOkButton();
			}
		});

		jbChooseDest.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jbChooseDestMouseClicked(evt);
			}
		});

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

		layout.addRow("a<...-~...a");
		layout.addRow(".....5.....");
		layout.addRow("b-~...b1c-c");
		layout.addRow(".....6.....");
		layout.addRow("s...-~-...s");
		layout.addRow(".....6.....");
		layout.addRow("d....+.....");
		layout.addRow(".....*.....");
		layout.addRow("..........d");
		layout.addRow(".....6.....");
		layout.addRow("t...-~-...t");
		layout.addRow(".....6.....");
		layout.addRow("h....+.....");
		layout.addRow(".....*.....");
		layout.addRow("..........h");
		layout.addRow(".....6.....");
		layout.addRow("w...-~-...w");
		layout.addRow(".....6.....");
		layout.addRow("e<...-~...e");
		layout.addRow(".....5.....");
		layout.addRow("f-~...f1g-g");

		ZoneLayout dlayout = ZoneLayoutFactory.newZoneLayout();

		dlayout.addRow("d<!d2e!-~e");
		dlayout.addRow("....6.....");
		dlayout.addRow("f<!f2g!-~g");

		JPanel jp = new JPanel(dlayout);

		jp.add(jLabel4, "d");
		jp.add(jcbEncrypt, "e");
		jp.add(jLabel5, "f");
		jp.add(jcbCompress, "g");
		
		ZoneLayout hlayout = ZoneLayoutFactory.newZoneLayout();

		hlayout.addRow("d<!d2e!-~e");
		hlayout.addRow("....6.....");
		hlayout.addRow("f<!f2g!-~g");

		JPanel jp2 = new JPanel(hlayout);

		jp2.add(jLabel2, "d");
		jp2.add(jpfPwd1, "e");
		jp2.add(jLabel3, "f");
		jp2.add(jpfPwd2, "g");

		content.setLayout(layout);

		content.add(jLabel1, "a");
		content.add(jtfImg, "b");
		content.add(jbChooseImg, "c");
		content.add(jp, "d");
		content.add(jLabel6, "e");
		content.add(jtfDest, "f");
		content.add(jbChooseDest, "g");
		content.add(jp2, "h");
		
		content.add(CustomSeparator.getHorizontalSeparator(), "s");
		content.add(CustomSeparator.getHorizontalSeparator(), "t");
		content.add(CustomSeparator.getHorizontalSeparator(), "w");
	}

	private void jbChooseDestMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() != MouseEvent.BUTTON1 || !jbChooseDest.isEnabled())
			return;

		File fileToLoad = CustomFileDialog.showOpenFileDialog(this, Context
				.getLastSetDir());

		if (fileToLoad == null)
			return;

		try {
			Context.setLastSetDir(fileToLoad.getParentFile());
			String name = fileToLoad.getCanonicalPath();
			if (!name.endsWith(".png"))
				name += ".png";
			dest = new File(name);
			jtfDest.setText(name);
		} catch (IOException e) {
			e.printStackTrace();
			jtfDest.setText("");
			dest = null;
		}

		checkAbilitationOkButton();
	}

	private void jbChooseImgMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() != MouseEvent.BUTTON1 || !jbChooseImg.isEnabled())
			return;

		File fileToLoad = CustomFileDialog.showOpenFileDialog(this, Context
				.getLastSetDir());

		if (fileToLoad != null) {
			src = fileToLoad;
			Context.setLastSetDir(fileToLoad.getParentFile());
		}

		try {
			jtfImg.setText(src == null ? "" : src.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
			src = null;
			jtfImg.setText("");
		}

		checkAbilitationOkButton();
	}

	private void checkAbilitationOkButton() {
		char[] pwd1 = jpfPwd1.getPassword();
		char[] pwd2 = jpfPwd2.getPassword();

		if (pwd1.length != pwd2.length) {
			disableOkButton();
			return;
		}

		for (int i = 0; i < pwd1.length; i++)
			if (pwd1[i] != pwd2[i]) {
				disableOkButton();
				return;
			}

		if (src == null || !src.isFile() || !src.exists()) {
			disableOkButton();
			return;
		}

		if (dest == null || (src.exists() && !src.isFile())) {
			disableOkButton();
			return;
		}

		enableOkButton();
	}

	protected void btCancelPressed() {
		dispose();
	}

	protected void btOkPressed() {
		String password = new String(jpfPwd1.getPassword());

		CipherAlgorithm ca = (CipherAlgorithm) CipherAlgorithm.values
				.get(jcbEncrypt.getSelectedIndex());
		CompressionMethod cm = (CompressionMethod) CompressionMethod.values
				.get(jcbCompress.getSelectedIndex());

		BufferedImage bi = null;

		try {
			bi = ImageIO.read(src);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, J18n
					._("The source file doesn't seem to contain an image."),
					J18n._("Failure"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		StegOutputStream sos;
		try {
			sos = new StegOutputStream(bi);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, J18n
					._("The image is not valid (must be 24/32 bit)."), J18n
					._("Failure"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		JafeCipherOutputStream jcos = new JafeCipherOutputStream(sos, ca, cm);

		try {
			jcos.initialize(password);
			TreeFactory.writeTree(tree, jcos);
			jcos.close();

			// backup the old file
			Utils.backupFile(dest);

			ImageIO.write(bi, "PNG", dest);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		dispose();
	}

}
