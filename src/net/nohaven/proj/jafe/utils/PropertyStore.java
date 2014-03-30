package net.nohaven.proj.jafe.utils;

import java.io.*;
import java.util.*;

//Facade for the properties.
public class PropertyStore {
   private static final String PROP_FILE_NAME = ".jafe-conf.xml";

   private File propFile;

   private Properties props;

   public PropertyStore() {
      props = new Properties();
      propFile = new File(Context.getStartDir(), PROP_FILE_NAME);
      
      if (!propFile.exists())
    	  return;
      
      FileInputStream fis = null;
      try {
         fis = new FileInputStream(propFile);
         props.loadFromXML(fis);
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (fis != null)
            try {
               fis.close();
            } catch (Exception e2) {
               e2.printStackTrace();
            }
      }
   }

   public boolean save() {
      try {
         OutputStream os = new FileOutputStream(propFile);
         props.storeToXML(os, "Jafe configuration file");
         os.close();
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }

   public void setProperty(String key, String value) {
      if (value == null)
         props.remove(key);
      else
         props.setProperty(key, value);
   }

   public String getPropertyAsString(String key) {
      return props.getProperty(key);
   }

   public String getPropertyAsString(String key, String defaultVal) {
      String ret = props.getProperty(key);
      if (ret == null)
         return defaultVal;
      return ret;
   }

   public void setProperty(String key, Boolean value) {
      if (value == null)
         setProperty(key, (String) null);
      else
         setProperty(key, value.booleanValue() ? "1" : "0");
   }

   public Boolean getPropertyAsBoolean(String key) {
      String val = getPropertyAsString(key);
      if (val == null)
         return null;
      return new Boolean("1".equals(val));
   }

   public Boolean getPropertyAsBoolean(String key, Boolean defaultVal) {
      Boolean val = getPropertyAsBoolean(key);
      if (val == null)
         return defaultVal;
      return val;
   }

}
