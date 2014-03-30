package net.nohaven.proj.jafe.utils;

import java.io.*;

public class CounterOutputStream extends OutputStream {
   private int counter;

   public CounterOutputStream() {
      reset();
   }
   
   public void reset(){
      counter = 0;
   }

   public void write(int arg0) throws IOException {
      counter++;
   }

   public void close() throws IOException {
   }

   public void flush() throws IOException {
   }

   public void write(byte[] arg0, int arg1, int arg2) throws IOException {
      counter += arg2;
   }

   public void write(byte[] arg0) throws IOException {
      counter += arg0.length;
   }

   public int getCount () {
      return counter;
   }
}
