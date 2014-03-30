package net.nohaven.proj.jafe.utils;

public class OSDetector {
	private static final int LINUX = 0;
	private static final int MAC = 1;
	private static final int WINDOWS = 2;
	private static final int OTHER = 3;
	
	private static final int os;
	
	static {
		String sos = System.getProperty("os.name").toUpperCase();
		
		if (sos.indexOf("MAC") > -1)
			os = MAC;
		else if (sos.indexOf("WINDOWS") > -1)
			os = WINDOWS;
		else if (sos.indexOf("LINUX") > -1)
			os = LINUX;
		else 
			os = OTHER;
	}
	
	public static boolean isWindows () {
		return os == WINDOWS;
	}
	
	public static boolean isMac () {
		return os == MAC;
	}
	
	public static boolean isLinux () {
		return os == LINUX;
	}
}
