package net.nohaven.proj.jafe.security.enums;

import java.lang.reflect.*;
import java.util.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.security.exceptions.*;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.*;

public class CipherAlgorithm {
	public static List values;
	
    public static CipherAlgorithm AES = new CipherAlgorithm(
            AESFastEngine.class, (byte) 0x00, "AES");

    public static CipherAlgorithm TWOFISH = new CipherAlgorithm(
            TwofishEngine.class, (byte) 0x01, "Twofish");

    public static CipherAlgorithm SERPENT = new CipherAlgorithm(
            SerpentEngine.class, (byte) 0x02, "Serpent");

    public static CipherAlgorithm CAMELLIA = new CipherAlgorithm(
            CamelliaEngine.class, (byte) 0x03, "Camellia");

    private Class algo;

    private byte id;
    
    private String name;

    private int blockLength = -1;
    
    private static void registerValue (Object o) {
    	if (values == null)
    		values = new ArrayList();
    	values.add(o);	
    }

    private CipherAlgorithm(Class algo, byte id, String name) {
        this.algo = algo;
        this.id = id;
        this.name = name;
        
        registerValue(this);
    }

    private static final Class[] NO_CLASS = new Class[0];

    private static final Object[] NO_OBJ = new Object[0];

    public BlockCipher getEngineInstance() {
        try {
            Constructor c = algo.getConstructor(NO_CLASS);
            return (BlockCipher) c.newInstance(NO_OBJ);
        } catch (Exception e) {
            return null;
        }
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CipherAlgorithm fromId(byte id)
            throws UnsupportedCharacteristicException {
    	for (int i=0; i < values.size(); i++){
    		CipherAlgorithm ca = (CipherAlgorithm) values.get(i);
    		if (ca.id == id)
    			return ca;
    	}

        throw new UnsupportedCharacteristicException(
        		J18n._("Unknown encryption algorithm"));
    }

    public int getBlockSize() {
        if (blockLength == -1)
            blockLength = getEngineInstance().getBlockSize();
        return blockLength;
    }
    
    public List getValues() {
    	return values;
    }
    
    public static String[] getNames() {
    	String[] ret = new String[values.size()];
    	for (int i=0; i < values.size(); i++)
    		ret[i] = ((CipherAlgorithm) values.get(i)).getName(); 
    	return ret;
    }
}
