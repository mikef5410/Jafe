package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;

import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.utils.*;

public class TextNode extends Node {
   public static final byte TYPE_ID = 0x02;

   public TextNode(Tree t) {
      super(t);
   }

   private String text;

   public final String getText() {
      return text;
   }

   public final void setText(String text) {
      this.text = text;
   }

   public byte getTypeID() {
      return TYPE_ID;
   }

   protected int getContentSize() {
      CounterOutputStream cos = new CounterOutputStream();
      DataOutputStream dos = new DataOutputStream(cos);
      try {
         writeContentToStream(dos);
      } catch (IOException e) {
         return 0; //cannot be
      }
      return cos.getCount();
   }

   protected void writeContentToStream(DataOutputStream dos) throws IOException {
      Utils.putStringToDataStream(text, dos);
   }

   protected void readContentFromStream(DataInputStream dis, int length)
         throws IOException {
      text = Utils.getStringFromDataStream(dis);
   }

}
