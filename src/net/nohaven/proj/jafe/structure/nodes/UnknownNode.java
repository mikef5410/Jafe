package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;

import net.nohaven.proj.jafe.structure.*;

public class UnknownNode extends Node {
    private byte typeId;
    private byte[] content;

    public UnknownNode(Tree t) {
        super(t);
    }

    public byte getTypeID() {
        return typeId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setTypeID(byte typeId) {
        this.typeId = typeId;
    }

   protected int getContentSize() {
      if (content != null)
         return content.length;
      return 0;
   }

   protected void writeContentToStream(DataOutputStream os) throws IOException {
      os.write(content);
   }

   protected void readContentFromStream(DataInputStream is, int length) throws IOException {
      content = new byte[length];
      is.read(content);
   }
}
