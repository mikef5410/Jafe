package net.nohaven.proj.jafe.utils;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import net.nohaven.proj.jafe.gui.*;

//contains the various singletons used in the app
//sacrifice thread-safety for ease of use and readability
public class Context {
	private static MainPanel mainPanel;

	private static SidePanel sidePanel;

	private static File startDir;

	private static PropertyStore propStore;

	private static File lastSetDir;

	//enabled languages, with descriptions. Read from languages.properties.
	private static Map languages;

	public static final MainPanel getMainPanel() {
		return mainPanel;
	}

	public static final void setMainPanel(MainPanel mainPanel) {
		Context.mainPanel = mainPanel;
	}

	public static final SidePanel getSidePanel() {
		return sidePanel;
	}

	public static final void setSidePanel(SidePanel sidePanel) {
		Context.sidePanel = sidePanel;
	}

	public static final File getStartDir() {
		return startDir;
	}

	public static final void setStartDir(File startDir) {
		Context.startDir = startDir;
	}

	public static final PropertyStore getPropStore() {
		return propStore;
	}

	public static final void setPropStore(PropertyStore propStore) {
		Context.propStore = propStore;
	}

	public static final Map getLanguages() {
		if (languages == null) {
			languages = new HashMap();
			//default row
			languages.put("en", "English");
			//reads from the file
			BufferedReader br = new BufferedReader(new InputStreamReader(
					Context.class
							.getResourceAsStream("/po/languages.properties"),
					Charset.forName("UTF-8")));

			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					int pos = line.indexOf("=");
					if (pos > -1) {
						languages.put(line.substring(0, pos), line
								.substring(pos + 1));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return languages;
	}

	public static final File getLastSetDir() {
		return lastSetDir;
	}

	public static final void setLastSetDir(File lastSetDir) {
		Context.lastSetDir = lastSetDir;
	}

	private Context() {

	}

}
