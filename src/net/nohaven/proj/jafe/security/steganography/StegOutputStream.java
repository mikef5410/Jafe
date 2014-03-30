package net.nohaven.proj.jafe.security.steganography;

import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import net.nohaven.proj.gettext4j.*;

public class StegOutputStream extends OutputStream {
	private BufferedImage img;

	private WritableRaster wr;

	private int index;

	private int wid;

	private int hei;

	private int dim;
	
	private int channelsPerPixel;
	
	public StegOutputStream(BufferedImage bi) throws IOException {
		super();
		
		img = bi;
		
		int pixSize = bi.getColorModel().getPixelSize();
		if (pixSize != 24 && pixSize != 32)
			throw new IOException(J18n._("A true color (24/32 bit) image is required"));
		
		wr = bi.getRaster();
		channelsPerPixel = pixSize / 8;
		index = 32; //length is written at the end

		wid = bi.getWidth();
		hei = wr.getHeight();
		dim = wid * hei;
		
		iArray = new int[channelsPerPixel];
	}

	private int[] iArray;

	private int iPos = -1;

	//used to set a bit to 1
	private static final byte[] MASKS_OR = new byte[] { 0x1, 0x2, 0x4, 0x8,
			0x10, 0x20, 0x40, (byte) 0x80 };

	//used to set a bit to 0
	private static final byte[] MASKS_AND = new byte[] { (byte) 0xFE,
			(byte) 0xFD, (byte) 0xFB, (byte) 0xF7, (byte) 0xEF, (byte) 0xDF,
			(byte) 0xBF, (byte) 0x7F };

	//calculates f(bit, zorder, value) = 
	//               (BITS[bit] & MASKS[zorder]) | (value & !(MASKS)))
	private static final byte[][][] TRUTH_TABLE = new byte[2][8][256];

	static {
		for (int j = 0; j < 8; j++)
			for (int k = 0; k < 256; k++) {
				TRUTH_TABLE[1][j][k] = (byte) (k | MASKS_OR[j]);
				TRUTH_TABLE[0][j][k] = (byte) (k & MASKS_AND[j]);
			}
	}

	private void writeOneBit(int bit) throws IOException {
		int zorder = (index / (dim * channelsPerPixel));
		if (zorder >= 8)
			throw new IOException("Too many data written");
		int idx = (index % (dim * channelsPerPixel));
		int pos = idx / channelsPerPixel;
		int colour = idx % channelsPerPixel;
		int x = pos % wid;
		int y = pos / wid;
		
		if (pos != iPos) {
			if (iPos > -1)
				wr.setPixel(iPos % wid, iPos / wid, iArray);
			wr.getPixel(x, y, iArray);
			iPos = pos;
		}
		iArray[colour] = TRUTH_TABLE[bit][zorder][iArray[colour]];
		
		index++;			
	}

	private void writeOneByte(byte b) throws IOException {
		for (int i = 0; i < 8; i++) {
			writeOneBit((b & 0x1));

			b >>= 1;
		}
	}

	public void close() throws IOException {
		int len = index;

		//writes the length of the data written, "obfuscated" adding the max
		//length of data writable to this image a random number of times to the
		//actual length. This way, it's not possible to easily know that this 
		//image has data concealed inside it by simply looking at the first bits
		int maxlen = dim * 8 * channelsPerPixel + 1;
		int maxtimes = (Integer.MAX_VALUE - len) / maxlen;
		int times = new Random().nextInt(maxtimes);
		int lenToWrite = len + times * maxlen;

		index = 0;
		DataOutputStream dos = new DataOutputStream(this);
		dos.writeInt(lenToWrite);
		
		//final commit
		wr.setPixel(iPos % wid, iPos / wid, iArray);
		super.close();
	}

	public void flush() throws IOException {
		super.flush();
	}

	public void saveImage(String path) throws IOException {
		ImageIO.write(img, "PNG", new File(path));
	}

	public BufferedImage getResultingImage() {
		return img;
	}

	public void write(byte[] bytes, int off, int len) throws IOException {
		for (int i = off; i < off + len; i++)
			writeOneByte(bytes[i]);
	}

	public void write(byte[] bytes) throws IOException {
		write(bytes, 0, bytes.length);
	}

	private byte[] oneByte = new byte[1];

	public void write(int b) throws IOException {
		oneByte[0] = (byte) (b & 0xFF);
		this.write(oneByte, 0, 1);
	}

	public static void main(String[] argv) throws Exception {
		//BufferedImage bi = ImageIO.read(new File("C:\\testAlpha.png"));
		BufferedImage bi = ImageIO.read(new File("C:\\test.jpg"));
		StegOutputStream ios = new StegOutputStream(bi);

		byte[] content = new byte[(ios.dim * ios.channelsPerPixel) - 4];
		byte[] content2 = new byte[content.length];
		new java.util.Random().nextBytes(content);

		long l = System.currentTimeMillis();
		ios.write(content);
		System.out.println(System.currentTimeMillis() - l);

		ios.close();
		//ios.saveImage("C:\\DSCN0557A.png");

		StegInputStream iis = new StegInputStream(ios.getResultingImage());
		l = System.currentTimeMillis();
		iis.read(content2);
		System.out.println(System.currentTimeMillis() - l);

		for (int i = 0; i < content.length; i++)
			if (content[i] != content2[i]){
				System.out.println("error at index " + i);
				return;
			}
	}

}
