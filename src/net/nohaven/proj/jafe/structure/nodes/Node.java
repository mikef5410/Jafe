package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;
import java.util.*;
import net.nohaven.proj.jafe.structure.Tree;

public abstract class Node {
   public static final int NULL_UID = -1;

   private byte level;

   private List sons;

   private Node father;

   private String title;

   private Tree tree;

   public Node(Tree t) {
      super();
      tree = t;
   }

   private final List getSons() {
      if (sons == null)
         sons = new ArrayList();
      return sons;
   }

   public final int getNOfSons() {
      if (sons == null)
         return 0;
      return getSons().size();
   }

   public final Node getSon(int pos) {
      if (pos >= getNOfSons())
         return null;
      return (Node) getSons().get(pos);
   }

   public final void addSon(Node son) {
      getSons().add(son);
   }

   public final void addSonAtPosition(Node son, int position) {
      getSons().add(position, son);
   }

   public final void removeSon(Node son) {
      if (sons != null)
         getSons().remove(son);
   }

   public final int getIdxOfSon(Node son) {
      if (sons == null)
         return -1;
      return getSons().indexOf(son);
   }

   public final void setSon(int pos, Node son) {
      getSons().set(pos, son);
   }

   public final Node getFather() {
      return father;
   }

   public final void setFather(Node father) {
      this.father = father;
   }

   public byte getLevel() {
      return level;
   }

   public void setLevel(byte level) {
      this.level = level;
   }

   public final String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * Get an unique identifier for this node type. Ideally it should return a
    * constant, see for example DirectoryNode.
    */
   public abstract byte getTypeID();

   public boolean hasSons() {
      return sons != null && !sons.isEmpty();
   }

   public String toString() {
      return getTitle();
   }

   public Tree getTree() {
      return tree;
   }

   public boolean isFirstSibling() {
      return level == 0 || father.getIdxOfSon(this) == 0;
   }

   public boolean isLastSibling() {
      return level == 0 || father.getIdxOfSon(this) == father.getNOfSons() - 1;
   }

   protected static final byte[] NULL_CONTENT = new byte[0];

   /**
    * Returns the size of the "serialized" content, i.e. the space it will
    * occupy on the (uncompressed) output stream written in writeContentToStream
    */
   protected abstract int getContentSize();

   /**
    * Serialize the content of the node and write it to the bare output stream. 
    * The data will be encrypted and compressed by the stream manager.
    */
   protected abstract void writeContentToStream(DataOutputStream os)
         throws IOException;

   /**
    * Read the serialized content from the bare input stream, and restore the
    * node state. The data read from the stream are already decompressed and 
    * decyphered.
    * 
    * You *must* read *exactly* <length> bytes, they are the same quantity
    * originally returned by getContentSize.
    */
   protected abstract void readContentFromStream(DataInputStream is, int length)
         throws IOException;

   public final void writeContent(DataOutputStream os) throws IOException {
      int size = getContentSize();
      os.writeInt(size);
      if (size > 0)
         writeContentToStream(os);
   }

   public final void readContent(DataInputStream is) throws IOException {
      int size = is.readInt();
      if (size > 0)
         readContentFromStream(is, size);
   }
}
