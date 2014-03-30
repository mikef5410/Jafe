package net.nohaven.proj.jafe.structure;

import java.util.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.exceptions.*;
import net.nohaven.proj.jafe.structure.nodes.*;

public class Tree {
   private final Node root;

   private final Set nodes = new HashSet();

   public Tree(String title) {
      this();
      root.setTitle(title);
   }

   public Tree() {
      root = new RootNode(this);
   }
   
   public Node getRoot() {
      return root;
   }

   public Iterator getNodesIterator() {
      return Collections.unmodifiableSet(nodes).iterator();
   }

   public void add(Node father, Node n) {
      if (!(father instanceof RootNode) && !nodes.contains(father))
         throw new ParamNotValidException(
        		 J18n._("The father doesn't belong to this tree"));

      father.addSon(n);
      n.setFather(father);

      nodes.add(n);

      n.setLevel((byte) (father.getLevel() + 1));
   }

   public void delete(Node toDel) {
      toDel.getFather().removeSon(toDel);
   }

   public void shiftDown(Node toShift) {
      Node father = toShift.getFather();
      int idx = father.getIdxOfSon(toShift);
      if (idx < father.getNOfSons() - 1) {
         Node tmp = father.getSon(idx + 1);
         father.setSon(idx + 1, father.getSon(idx));
         father.setSon(idx, tmp);
      }
   }

   public void shiftUp(Node toShift) {
      Node father = toShift.getFather();
      int idx = father.getIdxOfSon(toShift);
      if (idx > 0) {
         Node tmp = father.getSon(idx - 1);
         father.setSon(idx - 1, father.getSon(idx));
         father.setSon(idx, tmp);
      }
   }

   public int size() {
      return nodes.size() + 1;//+1 => the root
   }

   public void addAtPosition(Node father, Node toAdd, int idxOfSon) {
      if (!(father instanceof RootNode) && !nodes.contains(father))
         throw new ParamNotValidException(
        		 J18n._("The father doesn't belong to this tree"));

      if (father.getLevel() == Byte.MAX_VALUE)
         throw new ParamNotValidException(J18n._("Too many levels of nidification"));

      father.addSonAtPosition(toAdd, idxOfSon);
      toAdd.setFather(father);

      nodes.add(toAdd);

      toAdd.setLevel((byte) (father.getLevel() + 1));
   }
}
