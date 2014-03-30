package net.nohaven.proj.jafe.security;

import net.nohaven.proj.jafe.security.enums.*;
import net.nohaven.proj.jafe.security.exceptions.*;

public class CiphHeader {
   public static final byte[] MAGIC_NUMBER = new byte[] { (byte) 0x46,
         (byte) 0x31, (byte) 0x8C, (byte) 0x62 };

   public static final int HEADER_BLOCK_LEN_BITS = 128;

   public static final int HEADER_BLOCK_LEN_BYTES = HEADER_BLOCK_LEN_BITS / 8;

   protected CiphHeader(CipherAlgorithm cipher, CompressionMethod compression) {
      super();
      fileFormat = FileFormatVersion.getCurrent();
      this.cipher = cipher;
      this.compression = compression;
   }

   private CiphHeader() {
      super();
   }

   private FileFormatVersion fileFormat;

   private CipherAlgorithm cipher;

   private CompressionMethod compression;

   public CipherAlgorithm getCipher() {
      return cipher;
   }

   public CompressionMethod getCompressionMethod() {
      return compression;
   }

   public FileFormatVersion getFileFormat() {
      return fileFormat;
   }

   protected byte[] getByteArrayRepr() {
      byte[] ret = new byte[HEADER_BLOCK_LEN_BYTES];

      ret[0] = MAGIC_NUMBER[0];
      ret[1] = MAGIC_NUMBER[1];
      ret[2] = MAGIC_NUMBER[2];
      ret[3] = MAGIC_NUMBER[3];
      ret[4] = fileFormat.getId();
      ret[5] = cipher.getId();
      ret[6] = compression.getId();

      SecRandom.getInstance().nextBytes(ret, 7);
      return ret;
   }

   protected static CiphHeader getFromBytes(byte[] repr)
         throws WrongMagicNumberException, UnsupportedCharacteristicException {
      if (repr[0] != MAGIC_NUMBER[0] || repr[1] != MAGIC_NUMBER[1]
            || repr[2] != MAGIC_NUMBER[2] || repr[3] != MAGIC_NUMBER[3])
         throw new WrongMagicNumberException();

      CiphHeader ch = new CiphHeader();
      ch.fileFormat = FileFormatVersion.fromId(repr[4]);
      ch.cipher = CipherAlgorithm.fromId(repr[5]);
      ch.compression = CompressionMethod.fromId(repr[6]);
      return ch;
   }
}
