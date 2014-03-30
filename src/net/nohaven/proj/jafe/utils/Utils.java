package net.nohaven.proj.jafe.utils;

import java.io.*;

public class Utils {

	private static final String NULL_STRING = "\0\0\0";

	private Utils() {
		//not instantiable
	}

	public static String getStringFromDataStream(DataInputStream dis)
			throws IOException {
		String s = dis.readUTF();
		if (NULL_STRING.equals(s))
			return null;
		return s;
	}

	public static void putStringToDataStream(String s, DataOutputStream dos)
			throws IOException {
		if (s == null)
			dos.writeUTF(NULL_STRING);
		else
			dos.writeUTF(s);
	}

	//(null == "") is TRUE
	public static boolean areStringsEquivalent(String a, String b) {
		if (a == null)
			return b == null || b.trim().length() == 0;
		if (b == null)
			return a.trim().length() == 0;
		return a.equals(b);
	}

	public static void backupFile(File file) throws IOException {
		if (!Context.getPropStore().getPropertyAsBoolean(
				Constants.PROP_KEY_MAKE_BACKUPS, Boolean.TRUE).booleanValue())
			return;
		if (!file.exists())
			return;

		File backupFile = new File(file.getCanonicalPath() + ".bak");
		if (backupFile.exists())
			backupFile.delete();
		file.renameTo(backupFile);
	}

	//returns the directory in wich has been started the JAR file
	public static File getExecutableDir() {
		String classpath = System.getProperty("java.class.path");

		int colonPos = classpath.indexOf(OSDetector.isWindows() ? ";" : ":");

		if (colonPos > -1)
			classpath = classpath.substring(OSDetector.isWindows() ? 0 : 1,
					colonPos);

		File file = new File((OSDetector.isWindows() ? "" : "/") + classpath);
		if (file.isDirectory())
			return file;
		if (file.isFile())
			return file.getParentFile();
		return null;
	}

	//substitutes a single "%s" with a given string
	public static String substTokenInString(String string, String piece) {
		if (string == null)
			return null;
		int pos = string.indexOf("%s");
		if (pos == -1)
			return string;
		return string.substring(0, pos) + piece + string.substring(pos + 2);
	}
}
