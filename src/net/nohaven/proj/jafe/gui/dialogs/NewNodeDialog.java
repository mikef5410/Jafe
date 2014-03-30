package net.nohaven.proj.jafe.gui.dialogs;

import javax.swing.*;

import com.atticlabs.zonelayout.swing.*;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.gui.components.*;
import net.nohaven.proj.jafe.structure.nodes.*;
import net.nohaven.proj.jafe.utils.*;

public class NewNodeDialog extends CustomOkCancelDialog {
    private static final long serialVersionUID = -4076208062947225537L;
	
    private Node relative;
    
    private ReferenceContainer newNode;
    
    public NewNodeDialog(java.awt.Frame parent, Node relative, ReferenceContainer newNode) {
        super(parent);
        
        this.relative = relative;
        this.newNode = newNode;
        
        jrbSibling.setEnabled(relative.getLevel() > 0);
        
        jrbSon.setSelected(true);
        jrbFreeText.setSelected(true);
        
        render();
    }
    
    protected void initI18n() {
    	setTitle(J18n._("New node"));
    	jrbSon.setText(J18n._("Son of selected node"));
    	jrbSibling.setText(J18n._("Sibling of selected node"));
    	jLabel1.setText(J18n._("Title"));
    	jtfTitle.setText(J18n._("Untitled node"));
    	jLabel2.setText(J18n._("Type"));
    	jrbDir.setText(J18n._("Directory"));
    	jrbFreeText.setText(J18n._("Free text"));
    	jrbAccount.setText(J18n._("Account"));
    	jrbFile.setText(J18n._("File"));
    	jLabel3.setText(J18n._("Create as"));
	}

    protected void initComponents(JPanel contents) {
        jbgType = new javax.swing.ButtonGroup();
        jbgPosition = new javax.swing.ButtonGroup();
        
        jrbSon = new javax.swing.JRadioButton();
        jrbSibling = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jtfTitle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jrbDir = new javax.swing.JRadioButton();
        jrbFreeText = new javax.swing.JRadioButton();
        jrbAccount = new javax.swing.JRadioButton();
        jrbFile = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();

        jbgPosition.add(jrbSon);
        jbgPosition.add(jrbSibling);
        
        jbgType.add(jrbDir);
        jbgType.add(jrbFreeText);
        jbgType.add(jrbFile);
        jbgType.add(jrbAccount);
        
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        
        layout.addRow("a<.ab---~------b");
        layout.addRow("........6.......");
        layout.addRow("s-------~------s");
        layout.addRow("........6.......");
        layout.addRow("c!<c2d!<~d2e!<~e");
        layout.addRow(".....f!<~f.g!<~g");
        layout.addRow("........6.......");
        layout.addRow("t-------~------t");
        layout.addRow("........6.......");
        layout.addRow("h<!h2i~!<......i");
        layout.addRow(".....j~!<......j");
        
        contents.setLayout(layout);
        contents.add(jLabel1, "a");
        contents.add(jtfTitle, "b");
        contents.add(jLabel2, "c");
        contents.add(jrbDir, "d");
        contents.add(jrbFreeText, "e");
        contents.add(jrbAccount, "f");
        contents.add(jrbFile, "g");
        contents.add(jLabel3, "h");
        contents.add(jrbSon, "i");
        contents.add(jrbSibling, "j");
        
        contents.add(CustomSeparator.getHorizontalSeparator(), "s");
        contents.add(CustomSeparator.getHorizontalSeparator(), "t");
               
        /*
        jrbSon.setSelected(true);
        jrbSon.setText("Son of selected node");
        jrbSon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbSon.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jrbSibling.setText("Sibling of selected node");
        jrbSibling.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbSibling.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jrbSon)
                    .add(jrbSibling))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jrbSon)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jrbSibling))
        );

        jLabel1.setText("Title");

        jtfTitle.setText("Untitled node");

        jLabel2.setText("Type");

        jbgType.add(jrbDir);
        jrbDir.setSelected(true);
        jrbDir.setText("Directory");
        jrbDir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbDir.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jbgType.add(jrbFreeText);
        jrbFreeText.setText("Free text");
        jrbFreeText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbFreeText.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jbgType.add(jRadioButton1);
        jRadioButton1.setText("Account");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jbgType.add(jrbFile);
        jrbFile.setText("File");
        jrbFile.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbFile.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabel2)
                .add(39, 39, 39)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jrbDir)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jRadioButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 51, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jrbFreeText)
                    .add(jrbFile))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jrbFreeText)
                    .add(jrbDir))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButton1)
                    .add(jrbFile)))
        );

        jbOk.setText("Ok");
        jbOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbOkMouseClicked(evt);
            }
        });

        jbCancel.setText("Cancel");
        jbCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbCancelMouseClicked(evt);
            }
        });

        jLabel3.setText("Create as");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jtfTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jbOk)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jbCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jtfTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jbCancel)
                    .add(jbOk))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();*/
    }// </editor-fold>//GEN-END:initComponents

    
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JRadioButton jrbAccount;
    private javax.swing.ButtonGroup jbgPosition;
    private javax.swing.ButtonGroup jbgType;
    private javax.swing.JRadioButton jrbDir;
    private javax.swing.JRadioButton jrbFile;
    private javax.swing.JRadioButton jrbFreeText;
    private javax.swing.JRadioButton jrbSibling;
    private javax.swing.JRadioButton jrbSon;
    private javax.swing.JTextField jtfTitle;

	protected void btOkPressed() {
		Node toAdd;

	      if (jrbDir.isSelected())
	         toAdd = new DirectoryNode(relative.getTree());
	      else if (jrbFreeText.isSelected())
	         toAdd = new TextNode(relative.getTree());
	      else if (jrbFile.isSelected())
	         toAdd = new SimpleFileNode(relative.getTree());
	      else
	         toAdd = new AccountNode(relative.getTree());

	      toAdd.setTitle(jtfTitle.getText());

	      if (jrbSon.isSelected())
	         relative.getTree().add(relative, toAdd);
	      else
	         relative.getTree().addAtPosition(relative.getFather(), toAdd,
	               relative.getFather().getIdxOfSon(relative) + 1);

	      newNode.setContent(toAdd);
	      
	      dispose();
	}

	protected void btCancelPressed() {
		dispose();
	}
    
}
