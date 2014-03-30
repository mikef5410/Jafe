package net.nohaven.proj.jafe.gui.nodepanels;

import javax.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.listener.*;
import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

import com.atticlabs.zonelayout.swing.*;

public class AccountNodePanel extends NodePanel {
    private static final long serialVersionUID = 1097740657865666879L;
    
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JScrollPane jScrollPane1;
    private JTextArea jtaNotes;
    private JTextField jtfPwd;
    private JTextField jtfUid;
    private JTextField jtfUrl;
    
    public AccountNodePanel() {
    	super();
    	
        jtaNotes.getDocument().addUndoableEditListener(DefaultUndoableEditListener.instance);
        jtaNotes.getDocument().addDocumentListener(nlu);
        
        jtfUrl.getDocument().addUndoableEditListener(DefaultUndoableEditListener.instance);
        jtfUrl.getDocument().addDocumentListener(nlu);
        
        jtfUid.getDocument().addUndoableEditListener(DefaultUndoableEditListener.instance);
        jtfUid.getDocument().addDocumentListener(nlu);
        
        jtfPwd.getDocument().addUndoableEditListener(DefaultUndoableEditListener.instance);
        jtfPwd.getDocument().addDocumentListener(nlu);
    }
    
    protected void initI18n() {
    	jLabel2.setText(J18n._("URL"));
    	jLabel3.setText(J18n._("UserID"));
    	jLabel4.setText(J18n._("Password"));
	}

	protected void initComponents(JPanel contents) {
        jScrollPane1 = new JScrollPane();
        jtaNotes = new JTextArea();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jtfPwd = new JTextField();
        jtfUid = new JTextField();
        jtfUrl = new JTextField();
        
        jtaNotes.setColumns(20);
        jtaNotes.setRows(5);

        jScrollPane1.setViewportView(jtaNotes);

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        
        layout.addRow("a<a3b-~b");
        layout.addRow("...6....");
        layout.addRow("c<c3d-~d");
        layout.addRow("...6....");
        layout.addRow("e<e3f-~f");
        layout.addRow("...6....");
        layout.addRow("g.......");
        layout.addRow("...+*...");
        layout.addRow(".......g");
        
        contents.setLayout(layout);
        
        contents.add(jLabel2,"a");
        contents.add(jtfUrl,"b");
        contents.add(jLabel3,"c");
        contents.add(jtfUid,"d");
        contents.add(jLabel4,"e");
        contents.add(jtfPwd,"f");
        contents.add(jScrollPane1,"g");
    }

    protected void setNodeContentsToControl(Node n) {
    	AccountNode an = (AccountNode) n;
    	
        jtfUrl.setText(an.getUrl());
        jtfUid.setText(an.getUserId());
        jtfPwd.setText(an.getPassword());
        jtaNotes.setText(an.getNotes());
    }
    
    protected void syncPanelContent() {
       AccountNode an = (AccountNode) getNode();	
    	
       if (!Utils.areStringsEquivalent(an.getUrl(), jtfUrl.getText()) ||
             !Utils.areStringsEquivalent(an.getUserId(), jtfUid.getText()) ||
             !Utils.areStringsEquivalent(an.getPassword(), jtfPwd.getText()) ||
             !Utils.areStringsEquivalent(an.getNotes(), jtaNotes.getText())){
          Context.getMainPanel().markUnsavedState();
       
          an.setUrl(jtfUrl.getText());
          an.setUserId(jtfUid.getText());
          an.setPassword(jtfPwd.getText());
          an.setNotes(jtaNotes.getText());
       }
    }
}
