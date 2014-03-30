package net.nohaven.proj.jafe.security.enums;

import java.util.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.security.exceptions.*;

public class CompressionMethod {
	public static List values;
	
    public static CompressionMethod NO_COMPRESSION = new CompressionMethod(
            (byte) 0x00, "");

    public static CompressionMethod ZLIB = new CompressionMethod((byte) 0x01, "Deflate (ZLib/ZIP)");

    public static CompressionMethod BZIP2 = new CompressionMethod((byte) 0x02, "BWCA (BZip2)");

    private byte id;
    
    private String name;

    private static void registerValue (Object o) {
    	if (values == null)
    		values = new ArrayList();
    	values.add(o);
    }

    private CompressionMethod(byte id, String name) {
        this.id = id;
        this.name = name;
        
        registerValue(this);
    }

    public byte getId() {
        return id;
    }

    public String getName() {
    	//dirty
    	if (id == NO_COMPRESSION.id)
    		return J18n._("No compression");
        return name;
    }

    public static CompressionMethod fromId(byte id)
            throws UnsupportedCharacteristicException {
    	for (int i=0; i < values.size(); i++){
    		CompressionMethod cm = (CompressionMethod) values.get(i);
    		if (cm.id == id)
    			return cm;
    	}
    	
        throw new UnsupportedCharacteristicException(
        		J18n._("Unknown compression method"));
    }
    
    public static String[] getNames() {
    	String[] ret = new String[values.size()];
    	for (int i=0; i < values.size(); i++)
    		ret[i] = ((CompressionMethod) values.get(i)).getName(); 
    	return ret;
    }
}
