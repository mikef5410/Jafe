package benchmarks;


import java.io.*;

import net.nohaven.proj.jafe.security.*;
import net.nohaven.proj.jafe.security.enums.*;

public class Benchmarks {
    private static final int NUM_REP = 20;

    private static final int DIM_MB = 10;

    private static class NullOutputStream extends OutputStream {

        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
        }

        public void write(byte[] arg0) throws IOException {
        }

        public void write(int arg0) throws IOException {
        }

    }

    private static byte[] b = new byte[1048576];

    public static void main(String argv[]) throws Exception {

        long runtimes = 0;
        for (int i = 0; i < NUM_REP; i++) {
            runtimes += test(CipherAlgorithm.AES);
            System.gc();
            Thread.sleep(2000);
        }
        System.out.println("AES: "
                + (1000f * NUM_REP * 1024 / runtimes * DIM_MB) + " kb/sec");

        runtimes = 0;
        
        runtimes = 0;
        for (int i = 0; i < NUM_REP; i++) {
            runtimes += test(CipherAlgorithm.SERPENT);
            System.gc();
            Thread.sleep(2000);
        }
        System.out.println("SERPENT: "
                + (1000f * NUM_REP * 1024 / runtimes * DIM_MB) + " kb/sec");

        runtimes = 0;
        for (int i = 0; i < NUM_REP; i++) {
            runtimes += test(CipherAlgorithm.TWOFISH);
            System.gc();
            Thread.sleep(2000);
        }
        System.out.println("TWOFISH: "
                + (1000f * NUM_REP * 1024 / runtimes * DIM_MB) + " kb/sec");

    }

    private static long test(CipherAlgorithm ca) throws IOException {
        JafeCipherOutputStream jcos = new JafeCipherOutputStream(
                new NullOutputStream(), ca, CompressionMethod.NO_COMPRESSION);

        jcos.initialize("password");
        long t1 = System.currentTimeMillis();
        for (int i=0; i < NUM_REP; i++)
            jcos.write(b);
        jcos.close();
        long t2 = System.currentTimeMillis();

        return t2 - t1;
    }
}
