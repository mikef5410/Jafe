package net.nohaven.proj.jafe.security;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.digests.*;
import org.bouncycastle.crypto.generators.*;

public class CiphSecretKey {
   private static final int ITERATION_COUNT = 2048;

   public static final int KEY_LEN_BITS = 128;

   public static final int KEY_LEN_BYTES = KEY_LEN_BITS/8;

   private CipherParameters key;

   public static byte[] getKeyMaterialFromPassword (String password) {
      return PKCS5S2ParametersGenerator.PKCS5PasswordToBytes(password
            .toCharArray());
   }

   public static byte[] getKeyMaterialFromRawData (byte[] rawData) {
      Digest dg = new SHA256Digest();
      dg.update(rawData,0, rawData.length);
      byte[] material = new byte[dg.getDigestSize()];
      dg.doFinal(material, 0);
      return material;
   }

   public CiphSecretKey(byte[] rawMaterial, CiphRandomBits crb) {
      PBEParametersGenerator kgen = new PKCS5S2ParametersGenerator();

      kgen.init(rawMaterial, crb.getSalt(), ITERATION_COUNT);

      key = kgen.generateDerivedParameters(KEY_LEN_BITS);
   }

   public CipherParameters getKey() {
      return key;
   }
}
