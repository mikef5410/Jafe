package net.nohaven.proj.jafe.security;

import java.nio.*;

import org.bouncycastle.crypto.*;

public class JafeCipherStreamGenerator {
    private BlockCipher cipher;

    private long msgNum;

    private ByteBuffer ctrBlock;

    private byte[] encBlock;

    private int cursor;

    protected JafeCipherStreamGenerator(CiphHeader header, CiphSecretKey key,
            CiphRandomBits crb) {
        cipher = header.getCipher().getEngineInstance();

        cipher.init(true, key.getKey());

        msgNum = 0;

        // the block to encode to perform the CTR block mode. The first part is
        // the nonce, ev. repeated, the second will be filled with the message number.
        ctrBlock = ByteBuffer.allocate(cipher.getBlockSize());
        ctrBlock.order(ByteOrder.BIG_ENDIAN);
        
        int pos = 0;
        int limit = cipher.getBlockSize();
        while (pos < limit) {
            int toWrite = Math.min(crb.getNonce().length, limit - pos);
            System.arraycopy(crb.getNonce(), 0, ctrBlock.array(), pos, toWrite);
            pos += toWrite;
        }

        // the ctrBlock, encoded with the key, to XOR with the message material
        // to perform the CTR
        encBlock = new byte[cipher.getBlockSize()];

        getNewBlock();
    }

    private void getNewBlock() {
        ctrBlock.putLong(cipher.getBlockSize() - 8, msgNum++);

        cipher.processBlock(ctrBlock.array(), 0, encBlock, 0);
        cipher.reset();

        cursor = 0;
    }

    public byte getNextByte() {
        if (cursor == encBlock.length)
            getNewBlock();

        return encBlock[cursor++];
    }

    public void processBytes(byte[] bytes, int off, int len) {
        for (int i = off; i < off + len; i++)
            bytes[i] = (byte) ((bytes[i] ^ getNextByte()) & 0xFF);
    }
}
