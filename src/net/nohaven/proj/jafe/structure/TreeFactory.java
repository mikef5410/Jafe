package net.nohaven.proj.jafe.structure;

import java.io.*;

import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

public class TreeFactory {
   private static final byte VERSION_1 = 0x00;

   private static final byte CURRENT_VERSION = VERSION_1;

   public static Tree loadTree(InputStream is) throws IOException {
      byte[] version = new byte[1];

      is.read(version);

      switch (version[0]) {
      case VERSION_1:
         return loadTree_V1(is);
      default:
         return null;
      }
   }

   private static Tree loadTree_V1(InputStream is)
         throws IOException {
      DataInputStream dis = new DataInputStream(is);

      Tree ret = new Tree();

      ret.getRoot().setTitle(Utils.getStringFromDataStream(dis));
      dis.readInt(); //dimension of the content of the root node

      Node current = ret.getRoot();

      try {
         while (true) {
            // compose the node
            byte level = dis.readByte();
            byte typeId = dis.readByte();

            Node nn = null;

            switch (typeId) {
            case DirectoryNode.TYPE_ID:
               nn = new DirectoryNode(ret);
               break;
            case TextNode.TYPE_ID:
               nn = new TextNode(ret);
               break;
            case AccountNode.TYPE_ID:
               nn = new AccountNode(ret);
               break;
            case SimpleFileNode.TYPE_ID:
               nn = new SimpleFileNode(ret);
               break;

            default:
               nn = new UnknownNode(ret);
               ((UnknownNode) nn).setTypeID(typeId);
            }

            nn.setTitle(Utils.getStringFromDataStream(dis));

            nn.setLevel(level);

            nn.readContent(dis);

            // adds it to the tree, following the depth-first assumption
            while (level <= current.getLevel())
               current = current.getFather();
            ret.add(current, nn);
            current = nn;
         }
      } catch (EOFException e) {
      }

      return ret;
   }

   private static void walkTheTree(Node current, DataOutputStream dos)
         throws IOException {
      if (current.getLevel() != 0) {// for the root node, I put only the
         // title infos
         dos.write(current.getLevel());
         dos.write(current.getTypeID());
      }

      // length of title, and title
      Utils.putStringToDataStream(current.getTitle(), dos);

      current.writeContent(dos);

      // recurse
      for (int i = 0; i < current.getNOfSons(); i++)
         walkTheTree(current.getSon(i), dos);
   }

   public static void writeTree(Tree t, OutputStream os) throws IOException {
      os.write(CURRENT_VERSION);

      DataOutputStream dos = new DataOutputStream(os);

      // recursively writes the depth-first walk of the tree, saving data as:
      // level [1b] + node type ID [1b] + title length [1b] + title [varies] + 
      // length of content [4b] + content bytes [varies]

      walkTheTree(t.getRoot(), dos);
   }
}
