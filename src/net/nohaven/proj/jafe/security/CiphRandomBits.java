package net.nohaven.proj.jafe.security;

import java.nio.*;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.digests.*;

public class CiphRandomBits {
    public static final int BLOCK_LEN_BITS = 128;

    public static final int BLOCK_LEN_BYTES = BLOCK_LEN_BITS / 8;

    protected CiphRandomBits() {
        super();

        // salt: random data
        salt = new byte[BLOCK_LEN_BYTES / 2];
        SecRandom.getInstance().nextBytes(salt);

        // nonce: a 32-byte buffer with current timestamp (8 byte), plus
        // random data till full, then SHA-256 it to get simil-rnd
        // data
        ByteBuffer tmpNonce = ByteBuffer.allocate(32);
        tmpNonce.putLong(System.currentTimeMillis());
        SecRandom.getInstance().nextBytes(tmpNonce);

        Digest digest = new SHA256Digest();

        digest.update(tmpNonce.array(), 0, 32);

        byte[] bigNonce = new byte[digest.getDigestSize()];

        digest.doFinal(bigNonce, 0);

        nonce = new byte[BLOCK_LEN_BYTES / 2];

        // limits digest.getDigestSize >= 128. That's quite assured.
        System.arraycopy(bigNonce, 0, nonce, 0, BLOCK_LEN_BYTES / 2);
    }

    private byte[] salt;

    private byte[] nonce;

    public byte[] getNonce() {
        return nonce;
    }

    public byte[] getSalt() {
        return salt;
    }

    protected byte[] getByteArrayRepr() {
        byte[] ret = new byte[BLOCK_LEN_BYTES];

        System.arraycopy(salt, 0, ret, 0, salt.length);

        System.arraycopy(nonce, 0, ret, salt.length, nonce.length);

        return ret;
    }

    protected static CiphRandomBits getFromBytes(byte[] repr) {
        CiphRandomBits crb = new CiphRandomBits();

        crb.salt = new byte[BLOCK_LEN_BYTES / 2];
        System.arraycopy(repr, 0, crb.salt, 0, crb.salt.length);

        crb.nonce = new byte[BLOCK_LEN_BYTES / 2];
        System.arraycopy(repr, crb.salt.length, crb.nonce, 0, crb.nonce.length);

        return crb;
    }
}
