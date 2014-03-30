package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;

import net.nohaven.proj.jafe.structure.Tree;

public class RootNode extends Node {
   public static final byte TYPE_ID = 0x00;

   public RootNode(Tree t) {
      super(t);
   }

   public byte getLevel() {
      return (byte) 0;
   }

   public void setLevel(byte level) {

   }

   public byte getTypeID() {
      return TYPE_ID;
   }

   protected int getContentSize() {
      return 0;
   }

   protected void writeContentToStream(DataOutputStream os) {
      //do nothing
   }

   protected void readContentFromStream(DataInputStream is, int length) {
      //do nothing
   }
}
