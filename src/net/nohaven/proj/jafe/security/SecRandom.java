package net.nohaven.proj.jafe.security;

import java.nio.*;
import java.security.*;
import java.util.*;

public class SecRandom {
	private static SecRandom instance;

	private Random rnd;

	private SecRandom() {
		rnd = new SecureRandom();
	}

	public static final SecRandom getInstance() {
		if (instance == null)
			instance = new SecRandom();
		return instance;
	}

	public boolean nextBoolean() {
		return rnd.nextBoolean();
	}

	public void nextBytes(byte[] arg0) {
		rnd.nextBytes(arg0);
	}

	public void nextBytes(byte[] arg0, int offset) {
		nextBytes(arg0, offset, arg0.length - offset);
	}

	public void nextBytes(byte[] arg0, int offset, int len) {
		byte[] buf = new byte[len];
		rnd.nextBytes(buf);
      System.arraycopy(arg0, offset, buf, 0, len);
	}
   
   public void nextBytes(ByteBuffer bb) {
      byte[] toPut = new byte[bb.capacity() - bb.position()];
      rnd.nextBytes(toPut);
      bb.put(toPut);
   }

	public double nextDouble() {
		return rnd.nextDouble();
	}

	public float nextFloat() {
		return rnd.nextFloat();
	}

	public int nextInt() {
		return rnd.nextInt();
	}

	public int nextInt(int arg0) {
		return rnd.nextInt(arg0);
	}

	public long nextLong() {
		return rnd.nextLong();
	}

	public String nextString(int len, char[] chars) {
		char[] b = new char[len];
		for (int i = 0; i < len; i++)
			b[i] = chars[nextInt(chars.length)];
		return new String(b);
	}
}
