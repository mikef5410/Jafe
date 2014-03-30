package net.nohaven.proj.jafe.security;

import java.io.*;
import java.util.zip.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.security.enums.*;

import org.apache.tools.bzip2.*;

public class JafeCipherOutputStream extends OutputStream {
   private CipherAlgorithm algorithm;

   private CompressionMethod compression;

   private boolean isInitialized = false;

   private OutputStream destinationStream;

   private OutputStream codedStream;

   private OutputStream compressedStream;

   private OutputStream topLevelStream;

   public JafeCipherOutputStream(OutputStream out, CipherAlgorithm ca,
         CompressionMethod cm) {
      super();
      destinationStream = out;
      algorithm = ca;
      compression = cm;
   }

   public void initialize(String password) throws IOException {
      prvInitialize(CiphSecretKey.getKeyMaterialFromPassword(password));
   }

   public void initialize(byte[] rawData) throws IOException {
      prvInitialize(CiphSecretKey.getKeyMaterialFromRawData(rawData));
   }

   private void prvInitialize(byte[] keyMaterial) throws IOException {
      CiphRandomBits rnd = new CiphRandomBits();

      destinationStream.write(rnd.getByteArrayRepr());

      CiphSecretKey key = new CiphSecretKey(keyMaterial, rnd);

      CiphHeader header = new CiphHeader(algorithm, compression);

      JafeCipherStreamGenerator jcsg = new JafeCipherStreamGenerator(header,
            key, rnd);
      JafeCipherObfuscator jco = new JafeCipherObfuscator(key, rnd);

      byte[] headerBytes = header.getByteArrayRepr();
      jco.process(headerBytes);

      destinationStream.write(headerBytes);

      codedStream = new CipherOutputStream(destinationStream, jcsg);
      
      if (compression == CompressionMethod.NO_COMPRESSION)
         compressedStream = codedStream;
      else if (compression == CompressionMethod.BZIP2)
         compressedStream = new CBZip2OutputStream(codedStream, 9);
      else if (compression == CompressionMethod.ZLIB)
         compressedStream = new DeflaterOutputStream(codedStream);

      topLevelStream = compressedStream;

      isInitialized = true;
   }

   public void write(byte[] bytes) throws IOException {
      if (!isInitialized)
         throw new IllegalStateException(J18n._("Not initialized"));

      topLevelStream.write(bytes);
   }

   public void write(byte[] bytes, int off, int len) throws IOException {
      if (!isInitialized)
         throw new IllegalStateException(J18n._("Not initialized"));

      topLevelStream.write(bytes, off, len);
   }

   public void write(int b) throws IOException {
      if (!isInitialized)
         throw new IllegalStateException(J18n._("Not initialized"));

      topLevelStream.write(b);
   }

   public void close() throws IOException {
	  //this, in cascade, will close all the stacked streams. In the case of the
	  //compression streams, it will also commit the unsaved data.
      topLevelStream.close();
   }

   private class CipherOutputStream extends FilterOutputStream {
      private JafeCipherStreamGenerator jcsg;

      public CipherOutputStream(OutputStream arg0,
            JafeCipherStreamGenerator jcsg) {
         super(arg0);
         this.jcsg = jcsg;
      }

      public void write(byte[] bytes, int off, int len) throws IOException {
         for (int i = off; i < off+len; i++)
            out.write((byte) ((bytes[i] ^ jcsg.getNextByte()) & 0xFF));
      }

      public void write(byte[] buf) throws IOException {
         this.write(buf, 0, buf.length);
      }

      private byte[] oneByte = new byte[1];

      public void write(int val) throws IOException {
         oneByte[0] = (byte) (val & 0xFF);
         this.write(oneByte, 0, 1);
      }

      public void close() throws IOException {
         super.close();
      }
   }
}
