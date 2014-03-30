package net.nohaven.proj.jafe.security;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.digests.*;
import org.bouncycastle.crypto.params.*;

public class JafeCipherObfuscator {
	private byte[] obfuscator;

	private int cursor;

	public JafeCipherObfuscator(CiphSecretKey key, CiphRandomBits crb) {
		// the obfuscator is used to "obfuscate" (not strictly encrypt, only
		// make random-alike) the header and the checksum. Hash of a fixed
		// value (the magic number), an user-provided value (the key) and a
		// random value (the nonce).
		Digest dg = new SHA256Digest();

		dg.update(CiphHeader.MAGIC_NUMBER, 0, 4);
		dg.update(((KeyParameter) key.getKey()).getKey(), 0,
				CiphSecretKey.KEY_LEN_BYTES);
		dg.update(crb.getNonce(), 0, crb.getNonce().length);

		obfuscator = new byte[dg.getDigestSize()];
		dg.doFinal(obfuscator, 0);
		
		

		cursor = 0;
	}

	protected byte[] getObfuscator() {
		return obfuscator;
	}

	protected void process(byte[] material, int off, int len) {
		for (int i = off; i < len; i++) {
			cursor = (cursor + 1) % obfuscator.length;
			material[i] = (byte) ((material[i] ^ obfuscator[cursor]) & 0xFF);
		}
	}
	
	protected void process(byte[] material) {
		process (material, 0, material.length);
	}
	
	protected void reset() {
		cursor = 0;
	}
}
