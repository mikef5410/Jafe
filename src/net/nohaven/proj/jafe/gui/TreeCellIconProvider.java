package net.nohaven.proj.jafe.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import net.nohaven.proj.jafe.structure.nodes.*;

public class TreeCellIconProvider extends DefaultTreeCellRenderer {
   private static final long serialVersionUID = 890924814168502023L;

   public Component getTreeCellRendererComponent(JTree tree, Object value,
         boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
            hasFocus);

      Node n = (Node) value;

      if (n instanceof RootNode) {
         setIcon(new javax.swing.ImageIcon(getClass().getResource(
               "/icons/16/root.png")));
      } else if (n instanceof DirectoryNode) {
         if (expanded)
            setIcon(new javax.swing.ImageIcon(getClass().getResource(
                  "/icons/16/folder_open.png")));
         else
            setIcon(new javax.swing.ImageIcon(getClass().getResource(
                  "/icons/16/folder.png")));
      } else if (n instanceof TextNode) {
         setIcon(new javax.swing.ImageIcon(getClass().getResource(
               "/icons/16/text.png")));
      } else if (n instanceof AccountNode) {
         setIcon(new javax.swing.ImageIcon(getClass().getResource(
               "/icons/16/html.png")));
      } else if (n instanceof SimpleFileNode) {
         setIcon(new javax.swing.ImageIcon(getClass().getResource(
               "/icons/16/file.png")));
      } else if (n instanceof UnknownNode) {
         setIcon(new javax.swing.ImageIcon(getClass().getResource(
               "/icons/16/unknown.png")));
      }

      return this;
   }
}
