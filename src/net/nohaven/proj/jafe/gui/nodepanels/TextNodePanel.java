package net.nohaven.proj.jafe.gui.nodepanels;

import javax.swing.*;

import net.nohaven.proj.jafe.gui.listener.*;
import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class TextNodePanel extends NodePanel {
    private static final long serialVersionUID = 1097740657865666879L;
    
    private JScrollPane jScrollPane1;
    private JTextArea jtaContents;
    
    public TextNodePanel() {
    	super();
        
        jtaContents.getDocument().addUndoableEditListener(DefaultUndoableEditListener.instance);
        jtaContents.getDocument().addDocumentListener(nlu);
    }
    
    protected void initI18n() {
    }

    protected void initComponents(JPanel contents) {
        jScrollPane1 = new JScrollPane();
        jtaContents = new JTextArea();

        jtaContents.setColumns(20);
        jtaContents.setRows(5);
        jScrollPane1.setViewportView(jtaContents);

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        
        layout.addRow("c.......");
        layout.addRow("...*....");
        layout.addRow("...+....");
        layout.addRow(".......c");
        	
        contents.setLayout(layout);
        
        contents.add(jScrollPane1, "c");
    }
                    
	protected void setNodeContentsToControl(Node n) {
    	jtaContents.setText(((TextNode) n).getText());
    }
    
    protected void syncPanelContent() {
    	TextNode tn = (TextNode) getNode();
        if (!Utils.areStringsEquivalent(tn.getText(), jtaContents.getText())){
           Context.getMainPanel().markUnsavedState();
           tn.setText(jtaContents.getText());
        }
    }
}
