package net.nohaven.proj.jafe.security.steganography;

import java.awt.image.*;
import java.io.*;

import net.nohaven.proj.gettext4j.*;

public class StegInputStream extends InputStream {
	private WritableRaster wr;

	private int index;

	private int limit;

	private int wid;

	private int hei;

	private int dim;
	
	private int channelsPerPixel;
	
	public StegInputStream(BufferedImage bi) throws IOException {
		super();
		int pixSize = bi.getColorModel().getPixelSize();
		if (pixSize != 24 && pixSize != 32)
			throw new IOException(J18n._("A true color (24/32 bit) image is required"));

		wr = bi.getRaster();
		channelsPerPixel = pixSize / 8;
		index = 0;

		wid = bi.getWidth();
		hei = wr.getHeight();
		dim = wid * hei;
		
		iArray = new int[channelsPerPixel];

		limit = 32;
		DataInputStream dis = new DataInputStream(this);
		int len = dis.readInt();
		len %= (dim * 8 * channelsPerPixel) + 1;
		limit = len;
	}

	private byte[] oneByte = new byte[1];

	public int read() throws IOException {
		int read = this.read(oneByte, 0, 1);

		if (read != 1)
			return -1;

		return (oneByte[0] & 0xFF);
	}

	public int read(byte[] bytes, int off, int len) throws IOException {
		int newLen = Math.min(len, limit - index);
		if (newLen <= 0)
			return -1;
		for (int i = off; i < off + newLen; i++)
			bytes[i] = readOneByte();
		return newLen;
	}

	public int read(byte[] bytes) throws IOException {
		return this.read(bytes, 0, bytes.length);
	}

	private int[] iArray;

	private static final byte[][] TRUTH_TABLE = new byte[256][8];

	static {
		for (int j = 0; j < 256; j++)
			for (int k = 0; k < 8; k++)
				TRUTH_TABLE[j][k] = (byte) ((j >> k) & 0x1);
	}

	private byte readOneBit() {
		int zorder = (index / (dim * channelsPerPixel));

		int idx = (index % (dim * channelsPerPixel));
		int pos = idx / channelsPerPixel;
		int colour = idx % channelsPerPixel;
		int x = pos % wid;
		int y = pos / wid;

		wr.getPixel(x, y, iArray);

		index++;

		return TRUTH_TABLE[iArray[colour]][zorder];
	}

	private byte readOneByte() {
		int ret = readOneBit() | (readOneBit() << 1) | (readOneBit() << 2)
				| (readOneBit() << 3) | (readOneBit() << 4)
				| (readOneBit() << 5) | (readOneBit() << 6)
				| (readOneBit() << 7);

		return (byte) ret;
	}
}
