package net.nohaven.proj.gettext4j;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class J18n {
	//key for the hashmap that holds the translations
	private static class Key {
		private String language;

		private String id;

		private Key(String language, String id) {
			this.language = language;
			this.id = id;
		}

		private Key() {
		}

		private static boolean isEqual(Object o1, Object o2) {
			if (o1 == null ^ o2 == null)
				return false;
			if (o1 == null)
				return true;
			return o1.equals(o2);
		}

		public boolean equals(Object o) {
			if (!(o instanceof Key))
				return false;
			Key k = (Key) o;
			return isEqual(id, k.id) && isEqual(language, k.language);
		}

		public int hashCode() {
			int ret = 0;
			if (id != null)
				ret += id.hashCode();
			if (language != null)
				ret += language.hashCode();
			return ret;
		}
	}

	private static Map translations = new HashMap();

	private static Key k = new Key();

	public static void setLocale(String language) {
		k.language = language;
		try {
			load(language);
		} catch (IOException e) {
			e.printStackTrace();
			translations.clear();
		}
	}

	public static String getLocale() {
		return k.language;
	}

	public static String _(String toTranslate) {
		k.id = toTranslate;
		String trans = (String) translations.get(k);
		if (trans == null)
			return toTranslate;
		return trans;
	}

	private static String cropAtQuotes(String str) {
		int pos1 = str.indexOf("\"");
		int pos2 = str.lastIndexOf("\"");
		if (pos2 <= pos1 || pos1 < 0 || pos2 < 0)
			return null;
		return str.substring(pos1 + 1, pos2);
	}

	private static void substReturns(StringBuffer sb) {
		int pos;
		while ((pos = sb.lastIndexOf("\\n")) > -1)
			sb.replace(pos, pos + 2, "\n");
	}

	protected static final String MSGID = "msgid";

	protected static final String MSGSTR = "msgstr";

	//(re)imports the resource files 
	private static void load(String lang) throws IOException {
		translations.clear();

		int state = 0;//0 => nothing; 1=> reading original; 2=> reading translated  

		StringBuffer original = new StringBuffer();
		StringBuffer translated = new StringBuffer();

		InputStream is = J18n.class.getResourceAsStream("/po/" + lang + ".po");
		if (is == null)
			return;
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));
		String line = br.readLine();
		while (line != null)
			try {
				line = line.trim();
				if (line.startsWith("#") || line.length() == 0)
					continue;

				if (line.startsWith(MSGID)) {
					if (state == 2 && translated.length() > 0) {
						substReturns(translated);
						translations.put(new Key(lang, original.toString()),
								translated.toString());
					}

					state = 1;
					original = new StringBuffer();
					translated = new StringBuffer();
				}

				if (line.startsWith(MSGSTR)) {
					state = 2;
				}

				if (state == 1)
					original.append(cropAtQuotes(line));
				else if (state == 2)
					translated.append(cropAtQuotes(line));
			} finally {
				line = br.readLine();
			}

		if (state == 2)
			translations.put(new Key(lang, original.toString()), translated
					.toString());
	}
}
