/*
 * PayOutView.java
 *
 * Created on August 25, 2006, 8:15 PM
 */

package com.floreantpos.ui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.dao.PayoutReasonDAO;
import com.floreantpos.model.dao.PayoutRecepientDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.NotesDialog;

/**
 *
 * @author  MShahriar
 */
public class PayOutView extends TransparentPanel {
    
    /** Creates new form PayOutView */
    public PayOutView() {
        initComponents();
    }
    
    public void initialize() {
    	PayoutReasonDAO reasonDAO = new PayoutReasonDAO();
    	List<PayoutReason> reasons = reasonDAO.findAll();
    	cbReason.setModel(new DefaultComboBoxModel(reasons.toArray()));
    	
    	PayoutRecepientDAO recepientDAO = new PayoutRecepientDAO();
    	List<PayoutRecepient> recepients = recepientDAO.findAll();
    	cbRecepient.setModel(new DefaultComboBoxModel(new Vector(recepients)));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        numberSelectionView = new com.floreantpos.ui.views.NumberSelectionView();
        numberSelectionView.setDecimalAllowed(true);
        jLabel1 = new javax.swing.JLabel();
        cbReason = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbRecepient = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        btnAddNote = new com.floreantpos.swing.PosButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfNote = new javax.swing.JTextArea();

        numberSelectionView.setTitle(com.floreantpos.POSConstants.AMOUNT_PAID_OUT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText(com.floreantpos.POSConstants.PAY_OUT_REASON);

        cbReason.setFont(new java.awt.Font("Tahoma", 1, 18));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText(com.floreantpos.POSConstants.SELECT_PAY_OUT_RECEPIENT);

        cbRecepient.setFont(new java.awt.Font("Tahoma", 1, 18));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText("NOTE");

        btnAddNote.setText("...");
        btnAddNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNoteActionPerformed(evt);
            }
        });

        tfNote.setColumns(20);
        tfNote.setEditable(false);
        tfNote.setLineWrap(true);
        tfNote.setRows(4);
        tfNote.setWrapStyleWord(true);
        jScrollPane1.setViewportView(tfNote);
        
        btnNewReason = new PosButton();
        btnNewReason.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doNewReason();
        	}
        });
        btnNewReason.setText("...");
        
        btnNewRecepient = new PosButton();
        btnNewRecepient.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doNewRecepient();
        	}
        });
        btnNewRecepient.setText("...");

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(numberSelectionView, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(7)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(cbRecepient, 0, 349, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnNewRecepient, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(cbReason, 0, 334, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnNewReason, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnAddNote, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(numberSelectionView, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(layout.createSequentialGroup()
        							.addGap(4)
        							.addComponent(cbReason, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        						.addComponent(btnNewReason, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(33)
        					.addComponent(jLabel2)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(cbRecepient, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        							.addGap(4))
        						.addComponent(btnNewRecepient, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(31)
        					.addComponent(jLabel3)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnAddNote, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))))
        			.addContainerGap())
        );
        this.setLayout(layout);

    }// </editor-fold>//GEN-END:initComponents

    protected void doNewRecepient() {
    	NotesDialog dialog = new NotesDialog();
    	dialog.setTitle("Enter pay out recepient");
    	dialog.pack();
    	dialog.open();
    	
    	if(dialog.isCanceled()) {
    		return;
    	}
    	
    	PayoutRecepient recepient = new PayoutRecepient();
    	recepient.setName(dialog.getNote());
    	
    	PayoutRecepientDAO.getInstance().saveOrUpdate(recepient);
    	DefaultComboBoxModel<PayoutRecepient> model = (DefaultComboBoxModel<PayoutRecepient>) cbRecepient.getModel();
    	model.addElement(recepient);
	}

	protected void doNewReason() {
		NotesDialog dialog = new NotesDialog();
    	dialog.setTitle("Enter pay out reason");
    	dialog.pack();
    	dialog.open();
    	
    	if(dialog.isCanceled()) {
    		return;
    	}
    	
    	PayoutReason reason = new PayoutReason();
    	reason.setReason(dialog.getNote());
    	
    	PayoutReasonDAO.getInstance().saveOrUpdate(reason);
    	DefaultComboBoxModel<PayoutReason> model = (DefaultComboBoxModel<PayoutReason>) cbReason.getModel();
    	model.addElement(reason);
	}

	private void btnAddNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNoteActionPerformed
    	NotesDialog dialog = new NotesDialog();
    	dialog.setTitle(com.floreantpos.POSConstants.ENTER_PAYOUT_NOTE);
    	dialog.pack();
    	dialog.open();
    	
    	if(!dialog.isCanceled()) {
    		tfNote.setText(dialog.getNote());
    	}
    }//GEN-LAST:event_btnAddNoteActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.floreantpos.swing.PosButton btnAddNote;
    private javax.swing.JComboBox cbReason;
    private javax.swing.JComboBox cbRecepient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.floreantpos.ui.views.NumberSelectionView numberSelectionView;
    private javax.swing.JTextArea tfNote;
    private PosButton btnNewRecepient;
    private PosButton btnNewReason;
    // End of variables declaration//GEN-END:variables
    
    public double getPayoutAmount() {
    	return numberSelectionView.getValue();
    }
    
    public String getNote() {
    	return tfNote.getText();
    }
    
    public PayoutReason getReason() {
    	return (PayoutReason) cbReason.getSelectedItem();
    }
    
    public PayoutRecepient getRecepient() {
    	return (PayoutRecepient) cbRecepient.getSelectedItem();
    }
}