package net.nohaven.proj.jafe.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import net.nohaven.proj.gettext4j.J18n;
import net.nohaven.proj.jafe.gui.components.CustomButton;
import net.nohaven.proj.jafe.gui.components.CustomFileDialog;
import net.nohaven.proj.jafe.gui.components.CustomSeparator;
import net.nohaven.proj.jafe.gui.components.CustomToggleButton;
import net.nohaven.proj.jafe.gui.dialogs.AboutDialog;
import net.nohaven.proj.jafe.gui.dialogs.DefinePasswordDialog;
import net.nohaven.proj.jafe.gui.dialogs.EmbedIntoImageDialog;
import net.nohaven.proj.jafe.gui.dialogs.FileInfoDialog;
import net.nohaven.proj.jafe.gui.dialogs.PasswordDialog;
import net.nohaven.proj.jafe.gui.nodepanels.NodePanel;
import net.nohaven.proj.jafe.security.JafeCipherInputStream;
import net.nohaven.proj.jafe.security.JafeCipherOutputStream;
import net.nohaven.proj.jafe.security.enums.CipherAlgorithm;
import net.nohaven.proj.jafe.security.enums.CompressionMethod;
import net.nohaven.proj.jafe.security.exceptions.UnsupportedCharacteristicException;
import net.nohaven.proj.jafe.security.steganography.StegInputStream;
import net.nohaven.proj.jafe.structure.Tree;
import net.nohaven.proj.jafe.structure.TreeFactory;
import net.nohaven.proj.jafe.tasks.CheckVersion;
import net.nohaven.proj.jafe.utils.Action;
import net.nohaven.proj.jafe.utils.Constants;
import net.nohaven.proj.jafe.utils.Context;
import net.nohaven.proj.jafe.utils.ReferenceContainer;
import net.nohaven.proj.jafe.utils.Utils;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

public class MainPanel extends JFrame {
	private static final long serialVersionUID = -529427963955995765L;

	private static final String WINDOW_TITLE = "Jafe " + Constants.VERSION;

	private File currentFile;

	private String currentPassword;

	private CipherAlgorithm currentCiph;

	private CompressionMethod currentComp;

	private Tree tree;

	private Set filesToDelete;

	private String[] langCodes;

	private JRadioButtonMenuItem[] langMenus;

	public MainPanel(Tree tree, File toLoad) {
		initComponents();
		initI18n();
		setSize(450, 450);

		filesToDelete = new HashSet();

		Context.setMainPanel(this);

		this.tree = tree;
		new SidePanel(this, tree);
		Context.getSidePanel().selectRoot();

		setTitle(WINDOW_TITLE);

		try {
			setIconImage(ImageIO.read(getClass().getResourceAsStream(
					"/icons/48/jafe.png")));
		} catch (IOException e) {
			//impossible. Even if it fails, irrilevant
			e.printStackTrace();
		}

		setLocationRelativeTo(null); //center on the screen

		boolean checkUpgrades = Context.getPropStore().getPropertyAsBoolean(
				Constants.PROP_KEY_CHECK_UPGR, Boolean.TRUE).booleanValue();

		jcbmiCheckUpdatesAtStartup.setSelected(checkUpgrades);

		//if set so, checks for updates
		if (checkUpgrades) {
			doCheckUpdatesCommand(false);
		}

		jcbmiMakeBackup.setSelected(Context.getPropStore()
				.getPropertyAsBoolean(Constants.PROP_KEY_MAKE_BACKUPS,
						Boolean.TRUE).booleanValue());

		//selection of the right item in the language menu
		for (int i = 0; i < langCodes.length; i++)
			if (langCodes[i].equals(J18n.getLocale())) {
				langMenus[i].setSelected(true);
				break;
			}

		setVisible(true);
		Context.getSidePanel().reposition();
		Context.getSidePanel().setVisible(true);

		//undo management
		UndoDirector.ud.reset();
		
		if (toLoad != null)
			load(toLoad);
	}

	private void initI18n() {
		jbNew.setToolTipText(J18n._("New") + "...");
		jbLoad.setToolTipText(J18n._("Open") + "...");
		jbSave.setToolTipText(J18n._("Save") + "...");
		jbSaveAs.setToolTipText(J18n._("Save as") + "...");
		jbQuit.setToolTipText(J18n._("Quit"));
		jbInfo.setToolTipText(J18n._("File info") + "...");
		jToggleButton1.setToolTipText(J18n._("Hide/show side panel"));
		jbUndo.setToolTipText(J18n._("Undo"));
		jbRedo.setToolTipText(J18n._("Redo"));
		jmiNew.setText(J18n._("New") + "...");
		jmFileMenu.setText(J18n._("File"));
		jmiOpen.setText(J18n._("Open") + "...");
		jmiSave.setText(J18n._("Save") + "...");
		jmiSaveAs.setText(J18n._("Save as") + "...");
		jmiEmbed.setText(J18n._("Embed into an image") + "...");
		jmiDebed.setText(J18n._("Read from an image"));
		jmiQuit.setText(J18n._("Quit"));
		jmEdit.setText(J18n._("Edit"));
		jmiUndo.setText(J18n._("Undo"));
		jmiRedo.setText(J18n._("Redo"));
		jmPreferences.setText(J18n._("Preferences"));
		jcbmiCheckUpdatesAtStartup.setText(J18n
				._("Check for updates at startup"));
		jcbmiMakeBackup.setText(J18n._("Make backups if overwriting"));
		jmiFileInfo.setText(J18n._("File info") + "...");
		jmHelpMenu.setText(J18n._("?"));
		jmiUpdates.setText(J18n._("Check for updates") + "...");
		jmiAbout.setText(J18n._("About") + "...");
		jmLanguages.setText(J18n._("Language"));
	}

	private void initComponents() {
		bgLanguages = new ButtonGroup();
		jProgressBar1 = new JProgressBar();
		JMenuBar jMenuBar1 = new JMenuBar();
		jmFileMenu = new JMenu();
		jmiNew = new JMenuItem();
		jmiOpen = new JMenuItem();
		jmiSave = new JMenuItem();
		jmiSaveAs = new JMenuItem();
		jmiEmbed = new JMenuItem();
		jmiDebed = new JMenuItem();
		jmiQuit = new JMenuItem();
		jmEdit = new JMenu();
		jmiUndo = new JMenuItem();
		jmiRedo = new JMenuItem();
		jmPreferences = new JMenu();
		jcbmiCheckUpdatesAtStartup = new JCheckBoxMenuItem();
		jcbmiMakeBackup = new JCheckBoxMenuItem();
		jmLanguages = new JMenu();
		jmHelpMenu = new JMenu();
		jmiFileInfo = new JMenuItem();
		jmiUpdates = new JMenuItem();
		jmiAbout = new JMenuItem();

		//buttons
		jbNew = new CustomButton("/32/filenew.png") {
			private static final long serialVersionUID = 4518156189L;

			public void mouseClickedHandler(MouseEvent evt) {
				doNewCommand();
			}
		};

		jbLoad = new CustomButton("/32/fileopen.png") {
			private static final long serialVersionUID = 451451981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				doOpenCommand();
			}
		};

		jbSave = new CustomButton("/32/filesave.png") {
			private static final long serialVersionUID = 451451981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				doSaveCommand();
			}
		};

		jbSaveAs = new CustomButton("/32/filesaveas.png") {
			private static final long serialVersionUID = 451457981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				doSaveAsCommand();
			}
		};

		jbQuit = new CustomButton("/32/logout.png") {
			private static final long serialVersionUID = 451857981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				doQuitCommand();
			}
		};

		jbInfo = new CustomButton("/32/info.png") {
			private static final long serialVersionUID = 411457981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				doInfoCommand();
			}
		};

		jbUndo = new CustomButton("/32/undo.png") {
			private static final long serialVersionUID = 411457981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				UndoDirector.ud.undo();
			}
		};

		jbRedo = new CustomButton("/32/redo.png") {
			private static final long serialVersionUID = 411457981589L;

			public void mouseClickedHandler(MouseEvent evt) {
				UndoDirector.ud.redo();
			}
		};

		jToggleButton1 = new CustomToggleButton("/32/left_panel.png", true) {
			private static final long serialVersionUID = 17L;

			public void toggledHandler(ItemEvent evt) {
				Context.getSidePanel().setVisible(jToggleButton1.isSelected());
				jToggleButton1.repaint();
			}
		};

		//button bars
		ZoneLayout alayout = ZoneLayoutFactory.newZoneLayout();
		alayout.addRow("q1t1a1b1c1d1s1e1fug1h");
		alayout.addRow("..|.........|........");
		alayout.addRow("q.t.a.b.c.d.s.e.fug.h");
		alayout.getZone("u").setTake(100, 0);

		JPanel aPanel = new JPanel(alayout);
		aPanel.add(jToggleButton1, "q");

		aPanel.add(CustomSeparator.getVerticalSeparator(), "t");

		aPanel.add(jbNew, "a");
		aPanel.add(jbLoad, "b");
		aPanel.add(jbSave, "c");
		aPanel.add(jbSaveAs, "d");

		aPanel.add(CustomSeparator.getVerticalSeparator(), "s");

		aPanel.add(jbUndo, "e");
		aPanel.add(jbRedo, "f");

		aPanel.add(jbInfo, "g");
		aPanel.add(jbQuit, "h");

		ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
		layout.addRow(".5...........");
		layout.addRow("2a----~----a2");//button bars
		layout.addRow("......5......");
		layout.addRow("s-----~-----s");//separator
		layout.addRow("......6......");
		layout.addRow(".c...........");
		layout.addRow(".....+*......");//content pane
		layout.addRow("...........c.");
		layout.addRow("......6......");
		layout.addRow("t-----~-----t");//separator
		layout.addRow("......5......");
		layout.addRow("2f----~----f2");//progress bar
		layout.addRow("......5......");

		setLayout(layout);

		add(aPanel, "a");
		add(CustomSeparator.getHorizontalSeparator(), "s");
		add(CustomSeparator.getHorizontalSeparator(), "t");
		add(jProgressBar1, "f");

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Jafe");
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				formComponentResized(evt);
			}

			public void componentMoved(java.awt.event.ComponentEvent evt) {
				formComponentMoved(evt);
			}

			public void componentShown(java.awt.event.ComponentEvent evt) {
				formComponentShown(evt);
			}

			public void componentHidden(java.awt.event.ComponentEvent evt) {
				formComponentHidden(evt);
			}
		});

		jmiNew.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doNewCommand();
			}
		});

		jmFileMenu.add(jmiNew);

		jmFileMenu.add(CustomSeparator.getHorizontalSeparator());

		jmiOpen.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doOpenCommand();
			}
		});

		jmFileMenu.add(jmiOpen);

		jmFileMenu.add(CustomSeparator.getHorizontalSeparator());

		jmiSave.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doSaveCommand();
			}
		});

		jmFileMenu.add(jmiSave);

		jmiSaveAs.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		jmiSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doSaveAsCommand();
			}
		});

		jmFileMenu.add(jmiSaveAs);

		jmFileMenu.add(CustomSeparator.getHorizontalSeparator());

		jmiEmbed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jmiEmbedActionPerformed(evt);
			}
		});

		jmFileMenu.add(jmiEmbed);

		jmiDebed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDebedCommand();
			}
		});

		jmFileMenu.add(jmiDebed);

		jmFileMenu.add(CustomSeparator.getHorizontalSeparator());

		jmiQuit.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Q,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doQuitCommand();
			}
		});

		jmFileMenu.add(jmiQuit);

		jMenuBar1.add(jmFileMenu);

		jmiUndo.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiUndo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				UndoDirector.ud.undo();
			}
		});

		jmEdit.add(jmiUndo);

		jmiRedo.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Y,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiRedo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				UndoDirector.ud.redo();
			}
		});

		jmEdit.add(jmiRedo);

		jMenuBar1.add(jmEdit);

		jcbmiCheckUpdatesAtStartup.setSelected(true);
		jcbmiCheckUpdatesAtStartup
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jcbmiCheckUpdatesAtStartupActionPerformed(evt);
					}
				});

		jmPreferences.add(jcbmiCheckUpdatesAtStartup);

		jcbmiMakeBackup.setSelected(true);
		jcbmiMakeBackup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jcbmiMakeBackupActionPerformed(evt);
			}
		});

		jmPreferences.add(jcbmiMakeBackup);

		Map langs = Context.getLanguages();
		SortedSet sLangs = new TreeSet(langs.keySet());
		langCodes = new String[sLangs.size()];
		langMenus = new JRadioButtonMenuItem[sLangs.size()];

		Iterator langIt = sLangs.iterator();
		int i = 0;

		while (langIt.hasNext()) {
			String iso = (String) langIt.next();
			langCodes[i] = iso;
			JRadioButtonMenuItem jrbmi = new JRadioButtonMenuItem();
			langMenus[i] = jrbmi;
			bgLanguages.add(jrbmi);
			jrbmi.setText((String) langs.get(iso));
			jmLanguages.add(jrbmi);
			jrbmi.addActionListener(new LangMenuActionListener(iso));
			i++;
		}

		jmLanguages.add(CustomSeparator.getHorizontalSeparator());

		JMenuItem jmiInfoLanguage = new JMenuItem();
		jmiInfoLanguage.setText("Translate Jafe in your language!");
		jmiInfoLanguage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showTranslationMessage();
			}
		});

		jmLanguages.add(jmiInfoLanguage);

		jmPreferences.add(jmLanguages);

		jMenuBar1.add(jmPreferences);

		jmiFileInfo.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_I,
				java.awt.event.InputEvent.CTRL_MASK));
		jmiFileInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doInfoCommand();
			}
		});

		jmHelpMenu.add(jmiFileInfo);

		jmHelpMenu.add(CustomSeparator.getHorizontalSeparator());

		jmiUpdates.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCheckUpdatesCommand(true);
			}
		});

		jmHelpMenu.add(jmiUpdates);

		jmiAbout.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_I,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		jmiAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new AboutDialog(Context.getMainPanel());
			}
		});
		jmHelpMenu.add(jmiAbout);

		jMenuBar1.add(jmHelpMenu);

		setJMenuBar(jMenuBar1);
	}

	private void setLocale(String loc) {
		J18n.setLocale(loc);
		initI18n();

		Context.getPropStore().setProperty(Constants.PROP_KEY_LANGUAGE, loc);
		Context.getPropStore().save();

		Context.getSidePanel().syncLanguageModification();
	}

	private void jcbmiMakeBackupActionPerformed(java.awt.event.ActionEvent evt) {
		Context.getPropStore().setProperty(Constants.PROP_KEY_MAKE_BACKUPS,
				new Boolean(jcbmiMakeBackup.isSelected()));
		Context.getPropStore().save();
	}

	private void jmiEmbedActionPerformed(java.awt.event.ActionEvent evt) {
		new EmbedIntoImageDialog(tree);
	}

	private void jcbmiCheckUpdatesAtStartupActionPerformed(
			java.awt.event.ActionEvent evt) {
		Context.getPropStore().setProperty(Constants.PROP_KEY_CHECK_UPGR,
				new Boolean(jcbmiCheckUpdatesAtStartup.isSelected()));
		Context.getPropStore().save();
	}

	private void formComponentShown(java.awt.event.ComponentEvent evt) {
		if (Context.getSidePanel() != null)
			Context.getSidePanel().setVisible(true);
	}

	private void formComponentHidden(java.awt.event.ComponentEvent evt) {
		if (Context.getSidePanel() != null)
			Context.getSidePanel().setVisible(false);
	}

	private void formComponentResized(java.awt.event.ComponentEvent evt) {
		if (Context.getSidePanel() != null)
			Context.getSidePanel().reposition();
	}

	private void formComponentMoved(java.awt.event.ComponentEvent evt) {
		if (Context.getSidePanel() != null)
			Context.getSidePanel().reposition();
	}

	private void doInfoCommand() {
		new FileInfoDialog(this, tree, currentCiph, currentComp,
				currentFile == null ? null : currentFile.getName());
	}

	private void doOpenCommand() {

		if (unsaved) {
			boolean canContinue = askForSave();
			if (!canContinue)
				return;
		}

		File preset = null;
		if (currentFile != null)
			preset = currentFile.getParentFile();
		else
			preset = Context.getLastSetDir();

		File fileToLoad = CustomFileDialog.showOpenFileDialog(this, preset);
		
		load(fileToLoad);
	}

	public void load(File fileToLoad) {
		if (fileToLoad != null && fileToLoad.isFile() && fileToLoad.exists()
				&& fileToLoad.canRead()) {
			try {
				FileInputStream fis = new FileInputStream(fileToLoad);
				boolean result = askPassForLoad(fis);
				fis.close();
				if (result) {
					currentFile = fileToLoad;
					Context.setLastSetDir(currentFile.getParentFile());
				}
			} catch (IOException e) {
				// not possible
				e.printStackTrace();
			}
		}

	}

	private void doNewCommand() {
		if (unsaved) {
			boolean canContinue = askForSave();
			if (!canContinue)
				return;
		}

		Context.getSidePanel().setNewTree(new Tree(J18n._("New document")));
		Context.getSidePanel().selectRoot();

		currentFile = null;
		currentPassword = null;
		currentCiph = null;
		currentComp = null;

		filesToDelete.clear();

		UndoDirector.ud.reset();
		unmarkUnsavedState();
	}

	private void doSaveAsCommand() {
		File tmpFile = currentFile;
		currentFile = null;

		String tmpPassword = currentPassword;
		currentFile = null;

		CipherAlgorithm tmpCiph = currentCiph;
		currentCiph = null;

		CompressionMethod tmpComp = currentComp;
		currentComp = null;

		if (tmpFile == null)
			doSaveCommand();
		else
			doSaveCommand(tmpFile.getParentFile());

		if (currentFile == null || currentPassword == null
				|| currentCiph == null || currentComp == null) {
			currentFile = tmpFile;
			currentPassword = tmpPassword;
			currentCiph = tmpCiph;
			currentComp = tmpComp;
		}
	}

	private void doSaveCommand() {
		doSaveCommand(null);
	}

	private void doSaveCommand(File curDir) {
		//show "ok, saved" dialog if and only if you're choosing the file
		boolean showConfirmation = currentFile == null;
		File origCurrentFile = currentFile;

		if (currentFile == null) {
			File preset = null;
			
			if (curDir == null)
				preset = Context.getLastSetDir();
			else
				preset = curDir;

			currentFile = CustomFileDialog.showSaveFileDialog(this, preset);
		}

		if (currentFile == null)
			return;

		if (currentPassword == null || currentCiph == null
				|| currentComp == null) {
			ReferenceContainer rcPwd = new ReferenceContainer();
			ReferenceContainer rcAlgo = new ReferenceContainer();
			ReferenceContainer rcComp = new ReferenceContainer();
			new DefinePasswordDialog(this, rcPwd, rcAlgo, rcComp);

			if (rcPwd.getContent() != null)
				currentPassword = (String) rcPwd.getContent();

			currentCiph = (CipherAlgorithm) rcAlgo.getContent();
			currentComp = (CompressionMethod) rcComp.getContent();
		}

		if (currentFile == null || currentPassword == null
				|| currentCiph == null || currentComp == null) {
			currentFile = origCurrentFile;
			return;
		}

		try {
			//backup the old file
			Utils.backupFile(currentFile);

			//write the new file
			OutputStream os = new FileOutputStream(currentFile, false);

			JafeCipherOutputStream jc = new JafeCipherOutputStream(os,
					currentCiph, currentComp);

			jc.initialize(currentPassword);

			TreeFactory.writeTree(tree, jc);

			jc.flush();

			jc.close();

			os.close();

			if (showConfirmation)
				JOptionPane.showMessageDialog(this, J18n
						._("Your file has been saved correctly"), J18n._("Ok"),
						JOptionPane.INFORMATION_MESSAGE);

			Context.setLastSetDir(currentFile.getParentFile());

			deleteFilesToDelete();

			unmarkUnsavedState();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showTranslationMessage() {
		JOptionPane
				.showMessageDialog(
						this,
						"If you can speak a foreign language not included in the\n"
								+ "language list, please help us to translate Jafe!\n\n"
								+ "It's really easy, not much work, and it will be very, very\n"
								+ "useful for the people who also speak your language!\n\n"
								+ "Just contact us at projects@nohaven.net. Thanks!",
						"Help Jafe", JOptionPane.NO_OPTION);
	}

	private boolean askPassForLoad(InputStream toLoad) {
		if (toLoad == null)
			return false;

		String pwd = null;

		ReferenceContainer rc = new ReferenceContainer();
		new PasswordDialog(this, rc);
		if (rc.getContent() != null)
			pwd = (String) rc.getContent();

		if (pwd == null)
			return false;

		return load(toLoad, pwd);
	}

	private boolean load(InputStream toLoad, String pwd) {
		if (toLoad == null || pwd == null)
			return false;

		try {
			JafeCipherInputStream jcis = new JafeCipherInputStream(toLoad);

			boolean pwdOk = jcis.initialize(pwd);

			if (!pwdOk) {
				JOptionPane.showMessageDialog(this, J18n._("Wrong password"),
						J18n._("Failure"), JOptionPane.ERROR_MESSAGE);

				return false;
			}

			Tree t = null;

			boolean allOk = true;

			try {
				t = TreeFactory.loadTree(jcis);
			} catch (ZipException e) {
				allOk = false;
			}

			jcis.close();

			if (!allOk) {
				JOptionPane.showMessageDialog(this, J18n
						._("The file is corrupted. Cannot load the data."),
						J18n._("Failure"), JOptionPane.ERROR_MESSAGE);

				return false;
			}

			tree = t;

			Context.getSidePanel().setNewTree(t);
			Context.getSidePanel().selectRoot();

			currentPassword = pwd;
			currentCiph = jcis.getHeader().getCipher();
			currentComp = jcis.getHeader().getCompressionMethod();

			filesToDelete.clear();

			UndoDirector.ud.reset();
			unmarkUnsavedState();

			return true;
		} catch (UnsupportedCharacteristicException e) {
			JOptionPane
					.showMessageDialog(
							this,
							J18n
									._("The file has been created with a later version of Jafe"),
							J18n._("Failure"), JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
		}
		return false; //you can reach here only through a catch block
	}

	private void doQuitCommand() {
		if (unsaved) {
			boolean canContinue = askForSave();
			if (!canContinue)
				return;
		}

		filesToDelete.clear();

		dispose();
	}

	private boolean unsaved;

	public void markUnsavedState() {
		this.unsaved = true;
		setTitle(WINDOW_TITLE + " [" + J18n._("Unsaved") + "]");
	}

	public void unmarkUnsavedState() {
		this.unsaved = false;
		setTitle(WINDOW_TITLE);
	}

	//returns true if it will be possible to go on, false otherwise
	public boolean askForSave() {
		int n = JOptionPane
				.showConfirmDialog(
						this,
						J18n
								._("The application contains unsaved data; do you want to save them?"),
						J18n._("Confirmation needed"),
						JOptionPane.YES_NO_CANCEL_OPTION);

		switch (n) {
		case JOptionPane.YES_OPTION:
			doSaveCommand();

		case JOptionPane.NO_OPTION:
			return true;

		case JOptionPane.CANCEL_OPTION:
		default:
			return false;
		}
	}

	private NodePanel set = null;

	public NodePanel getNodePanel() {
		return set;
	}

	public void setNodePanel(NodePanel np) {
		if (set != null)
			getContentPane().remove(set);

		getContentPane().add(np, "c");

		set = np;
		np.updateUI();
		getContentPane().repaint();
	}

	public int getBottomAdditionalInset() {
		//8 = ZoneLayout margin + separator
		return jProgressBar1.getSize().height + 8;
	}

	public void addFileToDelete(File f) {
		filesToDelete.add(f);
	}

	private void deleteFilesToDelete() {
		Iterator it = filesToDelete.iterator();

		while (it.hasNext())
			((File) it.next()).delete();

		filesToDelete.clear();
	}

	private void doCheckUpdatesCommand(boolean verbose) {
		CheckVersion cv = new CheckVersion(new Action() {

			public void execute() {
				JOptionPane.showMessageDialog(Context.getMainPanel(), J18n
						._("You already have the latest version."), J18n
						._("Ok"), JOptionPane.INFORMATION_MESSAGE);
			}

			public void execute(String str) {
			}
		}, new Action() {

			public void execute() {
			}

			public void execute(String str) {
				String msg = Utils
						.substTokenInString(
								J18n
										._("An updated version (%s) is available; please upgrade\nto enjoy the latest features and fixes."),
								str);

				JOptionPane.showMessageDialog(Context.getMainPanel(), msg, J18n
						._("New version available"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}, new Action() {

			public void execute() {
				JOptionPane
						.showMessageDialog(
								Context.getMainPanel(),
								J18n
										._("Cannot reach the server. Check your net configuration or try again later."),
								J18n._("Failure"), JOptionPane.ERROR_MESSAGE);
			}

			public void execute(String str) {
			}
		}, verbose);
		cv.start();
	}

	private void doDebedCommand() {
		File fileToLoad = CustomFileDialog.showOpenFileDialog(this, Context
				.getLastSetDir());

		if (fileToLoad == null || !fileToLoad.isFile() || !fileToLoad.exists())
			return;

		BufferedImage bi = null;

		try {
			bi = ImageIO.read(fileToLoad);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, J18n
					._("The file doesn't seem to contain an image."), J18n
					._("Failure"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		StegInputStream sis;
		try {
			sis = new StegInputStream(bi);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, J18n
					._("A problem has been encountered")
					+ ": " + e.getMessage(), J18n._("Failure"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean result = askPassForLoad(sis);

		if (result) {
			//FIXME
			currentFile = null;
			Context.setLastSetDir(fileToLoad.getParentFile());
		}
	}

	private ButtonGroup bgLanguages;

	private JProgressBar jProgressBar1;

	private JToggleButton jToggleButton1;

	private JButton jbInfo;

	private JButton jbLoad;

	private JButton jbNew;

	private JButton jbQuit;

	private JButton jbRedo;

	private JButton jbSave;

	private JButton jbSaveAs;

	private JButton jbUndo;

	private JCheckBoxMenuItem jcbmiCheckUpdatesAtStartup;

	private JCheckBoxMenuItem jcbmiMakeBackup;

	private JMenu jmEdit;

	private JMenu jmFileMenu;

	private JMenu jmHelpMenu;

	private JMenu jmLanguages;

	private JMenu jmPreferences;

	private JMenuItem jmiAbout;

	private JMenuItem jmiDebed;

	private JMenuItem jmiEmbed;

	private JMenuItem jmiFileInfo;

	private JMenuItem jmiNew;

	private JMenuItem jmiOpen;

	private JMenuItem jmiQuit;

	private JMenuItem jmiRedo;

	private JMenuItem jmiSave;

	private JMenuItem jmiSaveAs;

	private JMenuItem jmiUndo;

	private JMenuItem jmiUpdates;

	private class LangMenuActionListener implements ActionListener {
		private String lang;

		private LangMenuActionListener(String lang) {
			this.lang = lang;
		}

		public void actionPerformed(ActionEvent arg0) {
			setLocale(lang);
		}
	}
}
