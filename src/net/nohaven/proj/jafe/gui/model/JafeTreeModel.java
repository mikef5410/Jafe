package net.nohaven.proj.jafe.gui.model;

import javax.swing.event.*;
import javax.swing.tree.*;

import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.structure.nodes.*;

public class JafeTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = 2436468584445832798L;

	Tree tree;

	TreeModelListener tml;

	public JafeTreeModel(Tree tree) {
		super(new DefaultMutableTreeNode(tree.getRoot()));
		this.tree = tree;
	}

	public Object getRoot() {
		return tree.getRoot();
	}

	public Object getChild(Object father, int idx) {
		return ((Node) father).getSon(idx);
	}

	public int getChildCount(Object father) {
		return ((Node) father).getNOfSons();
	}

	public boolean isLeaf(Object node) {
		return !((Node) node).hasSons();
	}

	public int getIndexOfChild(Object father, Object node) {
		return ((Node) father).getIdxOfSon((Node) node);
	}
	
	public TreePath getTreePath (Node n) {
		Node[] nodes = new Node[n.getLevel() + 1];
		while (n.getLevel() > 0) {
			nodes[n.getLevel()] = n;
			n = n.getFather();
		}
                nodes[0] = n;
		return new TreePath(nodes);
	}
}
