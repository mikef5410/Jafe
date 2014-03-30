package net.nohaven.proj.jafe.security;

import java.io.*;
import java.util.zip.*;

import net.nohaven.proj.jafe.security.enums.*;
import net.nohaven.proj.jafe.security.exceptions.*;

import org.apache.tools.bzip2.*;

public class JafeCipherInputStream extends InputStream {
   private InputStream destinationStream;

   private InputStream codedStream;

   private InputStream compressedStream;

   private InputStream topLevelStream;

   private CiphHeader header;

   public JafeCipherInputStream(InputStream in) {
      super();
      destinationStream = in;
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      return topLevelStream.read();
   }

   public int read(byte[] bytes) throws IOException {
      return topLevelStream.read(bytes);
   }

   public int read(byte[] bytes, int off, int len) throws IOException {
      return topLevelStream.read(bytes, off, len);
   }

   // TODO implement this better
   public long skip(long pos) throws IOException {
	  for (long i=0; i < pos; i++)
      	if (read() == -1)
      		return i;
	  return pos;
   }

   public boolean initialize(String password) throws IOException,
         UnsupportedCharacteristicException {
      return prvInitialize(CiphSecretKey.getKeyMaterialFromPassword(password));
   }

   public boolean initialize(byte[] rawData) throws IOException,
         UnsupportedCharacteristicException {
      return prvInitialize(CiphSecretKey.getKeyMaterialFromRawData(rawData));
   }

   // returns if the password is correct
   public boolean prvInitialize(byte[] keyMaterial) throws IOException,
         UnsupportedCharacteristicException {
      byte[] buf = new byte[CiphRandomBits.BLOCK_LEN_BYTES];
      destinationStream.read(buf);
      CiphRandomBits rnd = CiphRandomBits.getFromBytes(buf);

      // gets the key
      CiphSecretKey key = new CiphSecretKey(keyMaterial, rnd);

      // builds the obfuscator
      JafeCipherObfuscator jco = new JafeCipherObfuscator(key, rnd);

      // decodes the header
      buf = new byte[CiphHeader.HEADER_BLOCK_LEN_BYTES];
      destinationStream.read(buf);
      jco.process(buf);
      header = null;
      try {
         header = CiphHeader.getFromBytes(buf);
      } catch (WrongMagicNumberException e) {
         return false;
      }

      // builds the cipher stream generator
      JafeCipherStreamGenerator jcsg = new JafeCipherStreamGenerator(header,
            key, rnd);

      codedStream = new CipherInputStream(destinationStream, jcsg);

      if (header.getCompressionMethod() == CompressionMethod.NO_COMPRESSION)
         compressedStream = codedStream;
      else if (header.getCompressionMethod() == CompressionMethod.BZIP2)
         compressedStream = new CBZip2InputStream(codedStream);
      else if (header.getCompressionMethod() == CompressionMethod.ZLIB)
         compressedStream = new BufferedInputStream(new InflaterInputStream(
               codedStream));

      topLevelStream = compressedStream;

      return true;
   }

   public final CiphHeader getHeader() {
      return header;
   }

   private class CipherInputStream extends FilterInputStream {
      private JafeCipherStreamGenerator jcsg;

      public CipherInputStream(InputStream arg0, JafeCipherStreamGenerator jcsg) {
         super(arg0);
         this.jcsg = jcsg;
      }

      public boolean markSupported() {
         return false;
      }

      private byte[] oneByte = new byte[1];

      public int read() throws IOException {
         int read = this.read(oneByte, 0, 1);

         if (read != 1)
            return -1;

         return (oneByte[0] & 0xFF);
      }

      public int read(byte[] bytes, int off, int len) throws IOException {
         int read = in.read(bytes, off, len);
         jcsg.processBytes(bytes, off, read);
         return read;
      }

      public int read(byte[] bytes) throws IOException {
         return this.read(bytes, 0, bytes.length);
      }
   }
}
