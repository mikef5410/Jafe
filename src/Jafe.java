import java.io.*;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.*;
import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.utils.*;

/**
 * This is here because it's the only way to display the correct name in OSX's
 * menu bar. Setting com.apple.mrj.application.apple.menu.about.name doesn't seem
 * to work...
 */

public class Jafe {

	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		if (OSDetector.isMac())
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		Context.setStartDir(Utils.getExecutableDir());

		PropertyStore props = new PropertyStore();
		Context.setPropStore(props);

		J18n.setLocale(props.getPropertyAsString(Constants.PROP_KEY_LANGUAGE,
				"en"));

		Tree t = new Tree(J18n._("New document"));

		Context.setLastSetDir(new File("."));

		File toLoad = null;
		if (args.length > 0) {
			toLoad = new File(args[0]);
		}
		
		new MainPanel(t, toLoad);		
	}

}
