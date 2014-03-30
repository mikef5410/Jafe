package net.nohaven.proj.gettext4j;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
   private static Pattern GETTEXT_MATCHER = Pattern.compile(
         "_\\([ \\t\\n]*(\\\".*?\\\")\\)", Pattern.MULTILINE);

   private String id;

   private File srcDir;

   private File poDir;

   private BufferedWriter poWriter;

   private static class ParsedItem {
      String text;

      HashSet origin = new HashSet();
   }

   public Parser(String id, File srcDir, File poDir) {
      super();
      this.id = id;
      this.srcDir = srcDir;
      this.poDir = poDir;
   }

   private void writeLine(String line) throws IOException {
      poWriter.write(line);
      poWriter.write('\n');
   }

//   private void write(String chars) throws IOException {
//      poWriter.write(chars);
//   }
//
//   private void write(char c) throws IOException {
//      poWriter.write(c);
//   }

   public void parse() throws IOException {
      OutputStream os = new FileOutputStream(poDir.getAbsolutePath() + "/" + id
            + ".pot");
      poWriter = new BufferedWriter(new OutputStreamWriter(os));

      writeLine("# SOME DESCRIPTIVE TITLE.");
      writeLine("# Copyright (C) YEAR Free Software Foundation, Inc.");

      Map occurs = new TreeMap();
      recurseDir(srcDir, occurs);

      Iterator it = occurs.values().iterator();
      while (it.hasNext()) {
         writeLine("");

         ParsedItem pi = (ParsedItem) it.next();
         Iterator it2 = pi.origin.iterator();
         while (it2.hasNext())
            writeLine("# " + (String) it2.next());
         writeLine(J18n.MSGID + " " + pi.text);
         writeLine(J18n.MSGSTR + " \"\"");

      }

      poWriter.flush();
      poWriter.close();
   }

   private void recurseDir(File dir, Map occurs) throws IOException {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++) {
         File f = files[i];
         if (f.isDirectory())
            recurseDir(f, occurs);
         if (f.getName().endsWith(".java")) {
            System.out.println("Parsing " + f.getName() + "...");
            parseFile(f, occurs);
         }
      }
   }

   private void parseFile(File f, Map occurs) throws IOException {
      InputStream is = new FileInputStream(f);
      byte[] cnt = new byte[is.available()];
      is.read(cnt);
      is.close();
      parseFile(new String(cnt), f.getName(), occurs);
   }

   private void parseFile(String cnt, String fname, Map occurs) {
      Matcher m = GETTEXT_MATCHER.matcher(cnt);
      while (m.find()) {
         String str = m.group(1);
         ParsedItem pi = (ParsedItem) occurs.get(str);
         if (pi == null) {
            pi = new ParsedItem();
            pi.text = str;
            occurs.put(str, pi);
         }
         pi.origin.add(fname);
      }

   }

   public static void main(String[] args) throws Exception {
      Parser p = new Parser("jafe", new File("src"),
            new File("src/po"));
      p.parse();
   }
}
