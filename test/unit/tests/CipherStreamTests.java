package unit.tests;

import java.io.*;
import java.util.*;

import junit.framework.*;
import net.nohaven.proj.jafe.security.*;
import net.nohaven.proj.jafe.security.enums.*;

public class CipherStreamTests extends TestCase {
   public static void testSingleByte() throws Exception {
      CipherAlgorithm ca = CipherAlgorithm.AES;
      CompressionMethod cm = CompressionMethod.ZLIB;
      
      byte b = (byte)(new Random().nextInt() % 128);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JafeCipherOutputStream jcos = new JafeCipherOutputStream(baos,
            ca, cm);
      jcos.initialize("passwordOfSomeSort");
      jcos.write(b);
      jcos.flush();
      jcos.close();
      
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      JafeCipherInputStream jcis = new JafeCipherInputStream(bais);
      assertTrue(jcis.initialize("passwordOfSomeSort"));
      int a = (byte)(jcis.read());
      
      assertEquals(b, a);
      assertEquals(jcis.getHeader().getCipher(), ca);
      assertEquals(jcis.getHeader().getCompressionMethod(), cm);
   }
   
   public static void testMultiByte() throws Exception {
      CipherAlgorithm ca = CipherAlgorithm.AES;
      CompressionMethod cm = CompressionMethod.ZLIB;
      
      byte[] b = new byte[50505];
      new Random().nextBytes(b);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JafeCipherOutputStream jcos = new JafeCipherOutputStream(baos,
            ca, cm);
      jcos.initialize("passwordOfSomeSort");
      for (int i=0; i < b.length; i++)
         jcos.write(b[i]);
      jcos.flush();
      jcos.close();
      
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      JafeCipherInputStream jcis = new JafeCipherInputStream(bais);
      assertTrue(jcis.initialize("passwordOfSomeSort"));
      byte[] a = new byte[b.length];
      byte[] one = new byte[1];
      for (int i=0; i < b.length; i++){
         jcis.read(one);
         a[i] = one[0];
      }
      
      assertEquals(jcis.getHeader().getCipher(), ca);
      assertEquals(jcis.getHeader().getCompressionMethod(), cm);
      
      for (int i=0; i < b.length; i++){
         System.out.println(i);
         assertEquals(b[i], a[i]);
      }
   }
}
