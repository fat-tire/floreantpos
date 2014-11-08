/*
 * FoodItemEditor.java
 *
 * Created on August 2, 2006, 10:34 PM
 */

package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.extension.InventoryPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemShift;
import com.floreantpos.model.Tax;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.DoubleDocument;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.IUpdatebleView;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

/**
 *
 * @author  MShahriar
 */
public class MenuItemForm extends BeanEditor implements ActionListener, ChangeListener {
	ShiftTableModel shiftTableModel;
	
	/** Creates new form FoodItemEditor */
	public MenuItemForm() throws Exception {
		this(new MenuItem());
	}

	protected void doSelectImageFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int option = fileChooser.showOpenDialog(null);
		
		if(option == JFileChooser.APPROVE_OPTION) {
			File imageFile = fileChooser.getSelectedFile();
			try {
				byte[] itemImage = FileUtils.readFileToByteArray(imageFile);
				int imageSize = itemImage.length / 1024;
				
				if(imageSize > 20) {
					POSMessageDialog.showMessage("The image is too large. Please select an image within 20 KB in size");
					itemImage = null;
					return;
				}
				
				ImageIcon imageIcon = new ImageIcon(new ImageIcon(itemImage).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
				lblImagePreview.setIcon(imageIcon);
				
				MenuItem menuItem = (MenuItem) getBean();
				menuItem.setImage(itemImage);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void doClearImage() {
		MenuItem menuItem = (MenuItem) getBean();
		menuItem.setImage(null);
		lblImagePreview.setIcon(null);
	}

	public MenuItemForm(MenuItem menuItem) throws Exception {
		initComponents();
		
		tfName.setDocument(new FixedLengthDocument(30));
		
		MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
		List<MenuGroup> foodGroups = foodGroupDAO.findAll();
		cbGroup.setModel(new ComboBoxModel(foodGroups));
		
		TaxDAO taxDAO = new TaxDAO();
		List<Tax> taxes = taxDAO.findAll();
		cbTax.setModel(new ComboBoxModel(taxes));
		
		menuItemModifierGroups = menuItem.getMenuItemModiferGroups();
		menuItemMGListModel = new MenuItemMGListModel();
		tableTicketItemModifierGroups.setModel(menuItemMGListModel);
		shiftTable.setModel(shiftTableModel = new ShiftTableModel(menuItem.getShifts()));
		
		btnNewModifierGroup.addActionListener(this);
		btnEditModifierGroup.addActionListener(this);
		btnDeleteModifierGroup.addActionListener(this);
		btnAddShift.addActionListener(this);
		btnDeleteShift.addActionListener(this);
		
		tfDiscountRate.setDocument(new DoubleDocument());
        jPanel1.setLayout(new MigLayout("", "[104px][100px,grow][][49px]", "[19px][25px][][19px][19px][][][25px][][15px]"));
        
        lblBuyPrice = new JLabel(Messages.getString("LABEL_BUY_PRICE"));
        jPanel1.add(lblBuyPrice, "cell 0 2");
        
        tfBuyPrice = new DoubleTextField();
        tfBuyPrice.setHorizontalAlignment(SwingConstants.TRAILING);
        jPanel1.add(tfBuyPrice, "cell 1 2,growx");
        jPanel1.add(jLabel3, "cell 0 3,alignx left,aligny center");
        jPanel1.add(jLabel4, "cell 0 1,alignx left,aligny center");
        
        JLabel lblImage = new JLabel("Image:");
        lblImage.setHorizontalAlignment(SwingConstants.TRAILING);
        jPanel1.add(lblImage, "cell 0 5,aligny center");
        setLayout(new BorderLayout(0, 0));
        
        lblImagePreview = new JLabel("");
        lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
        lblImagePreview.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        lblImagePreview.setPreferredSize(new Dimension(60, 120));
        jPanel1.add(lblImagePreview, "cell 1 5,grow");
        
        JButton btnSelectImage = new JButton("...");
        btnSelectImage.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doSelectImageFile();
        	}
        });
        jPanel1.add(btnSelectImage, "cell 2 5");
        
        btnClearImage = new JButton("Clear");
        btnClearImage.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doClearImage();
        	}
        });
        jPanel1.add(btnClearImage, "cell 3 5");
        
        cbShowTextWithImage = new JCheckBox("Show image only");
        cbShowTextWithImage.setActionCommand("Show Text with Image");
        jPanel1.add(cbShowTextWithImage, "cell 1 6 3 1"); //$NON-NLS-1$
        jPanel1.add(jLabel6, "cell 0 7,alignx left,aligny center"); //$NON-NLS-1$
        jPanel1.add(jLabel2, "cell 0 4,alignx left,aligny center"); //$NON-NLS-1$
        jPanel1.add(jLabel1, "cell 0 0,alignx left,aligny center"); //$NON-NLS-1$
        jPanel1.add(tfName, "cell 1 0 3 1,growx,aligny top"); //$NON-NLS-1$
        jPanel1.add(cbGroup, "cell 1 1,growx,aligny top"); //$NON-NLS-1$
        jPanel1.add(btnNewGroup, "cell 3 1,growx,aligny top"); //$NON-NLS-1$
        jPanel1.add(tfDiscountRate, "cell 1 4,growx,aligny top"); //$NON-NLS-1$
        jPanel1.add(cbTax, "cell 1 7,growx,aligny top"); //$NON-NLS-1$
        jPanel1.add(tfPrice, "cell 1 3,growx,aligny top"); //$NON-NLS-1$
        
        lblKitchenPrinter = new JLabel("Kitchen Printer");
        jPanel1.add(lblKitchenPrinter, "cell 0 8"); //$NON-NLS-1$
        
        cbPrinter = new JComboBox<VirtualPrinter>(new DefaultComboBoxModel<VirtualPrinter>(VirtualPrinterDAO.getInstance().findAll().toArray(new VirtualPrinter[0])));
        jPanel1.add(cbPrinter, "cell 1 8,growx"); //$NON-NLS-1$
        jPanel1.add(chkVisible, "cell 1 9,alignx left,aligny top"); //$NON-NLS-1$
        jPanel1.add(btnNewTax, "cell 2 7,alignx left,aligny top"); //$NON-NLS-1$
        jPanel1.add(jLabel5, "cell 2 4"); //$NON-NLS-1$
        add(tabbedPane);
        
		setBean(menuItem);
		
		addRecepieExtension();
	}

	public void addRecepieExtension() {
		InventoryPlugin plugin = Application.getPluginManager().getPlugin(InventoryPlugin.class);
		if (plugin == null) {
			return;
		}
		
		plugin.addRecepieView(tabbedPane);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
        tfName = new com.floreantpos.swing.FixedLengthTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
        cbGroup = new javax.swing.JComboBox();
        btnNewGroup = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
        tfPrice = new DoubleTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setHorizontalAlignment(SwingConstants.TRAILING);
        cbTax = new javax.swing.JComboBox();
        btnNewTax = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabel5 = new javax.swing.JLabel();
        tfDiscountRate = new DoubleTextField();
        tfDiscountRate.setHorizontalAlignment(SwingConstants.TRAILING);
        chkVisible = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnNewModifierGroup = new javax.swing.JButton();
        btnDeleteModifierGroup = new javax.swing.JButton();
        btnEditModifierGroup = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTicketItemModifierGroups = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnDeleteShift = new javax.swing.JButton();
        btnAddShift = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        shiftTable = new javax.swing.JTable();

        jLabel1.setText(Messages.getString("LABEL_NAME"));
        jLabel4.setText(Messages.getString("LABEL_GROUP"));

        btnNewGroup.setText("...");
        btnNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doCreateNewGroup(evt);
            }
        });

        if(Application.getInstance().isPriceIncludesTax()) {
        	jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_INCLUDING_TAX"));
        }
        else {
        	jLabel3.setText(Messages.getString("LABEL_SALES_PRICE_EXCLUDING_TAX"));
        }

        tfPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel6.setText(Messages.getString("LABEL_TAX"));

        btnNewTax.setText("...");
        btnNewTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTaxdoCreateNewTax(evt);
            }
        });

        jLabel2.setText(com.floreantpos.POSConstants.DISCOUNT_RATE + ":");

        jLabel5.setText("%");

        chkVisible.setText(com.floreantpos.POSConstants.VISIBLE);
        chkVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkVisible.setMargin(new java.awt.Insets(0, 0, 0, 0));

        tabbedPane.addTab(com.floreantpos.POSConstants.GENERAL, jPanel1);

        btnNewModifierGroup.setText(com.floreantpos.POSConstants.ADD);
        btnNewModifierGroup.setActionCommand("AddModifierGroup");
        btnNewModifierGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewModifierGroupActionPerformed(evt);
            }
        });

        btnDeleteModifierGroup.setText(com.floreantpos.POSConstants.DELETE);
        btnDeleteModifierGroup.setActionCommand("DeleteModifierGroup");

        btnEditModifierGroup.setText(com.floreantpos.POSConstants.EDIT);
        btnEditModifierGroup.setActionCommand("EditModifierGroup");

        tableTicketItemModifierGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableTicketItemModifierGroups);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .add(btnNewModifierGroup)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnEditModifierGroup)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnDeleteModifierGroup)
                .addContainerGap())
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 377, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnDeleteModifierGroup)
                    .add(btnEditModifierGroup)
                    .add(btnNewModifierGroup))
                .addContainerGap())
        );

        tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUPS, jPanel2);

        btnDeleteShift.setText(com.floreantpos.POSConstants.DELETE_SHIFT);

        btnAddShift.setText(com.floreantpos.POSConstants.ADD_SHIFT);

        shiftTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(shiftTable);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 387, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(btnAddShift)
                        .add(5, 5, 5)
                        .add(btnDeleteShift)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 281, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnAddShift)
                    .add(btnDeleteShift))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab(com.floreantpos.POSConstants.SHIFTS, jPanel3);
        
        tabbedPane.addChangeListener(this);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewTaxdoCreateNewTax(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTaxdoCreateNewTax
    	BeanEditorDialog dialog = new BeanEditorDialog(new TaxForm(), BackOfficeWindow.getInstance(), true);
		dialog.open();
    }//GEN-LAST:event_btnNewTaxdoCreateNewTax

    private void btnNewModifierGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewModifierGroupActionPerformed
    	
    }//GEN-LAST:event_btnNewModifierGroupActionPerformed

	private void doCreateNewGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doCreateNewGroup
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(editor, getParentFrame(), true);
		dialog.open();
		if (!dialog.isCanceled()) {
			MenuGroup foodGroup = (MenuGroup) editor.getBean();
			ComboBoxModel model = (ComboBoxModel) cbGroup.getModel();
			model.addElement(foodGroup);
			model.setSelectedItem(foodGroup);
		}
	}//GEN-LAST:event_doCreateNewGroup

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddShift;
    private javax.swing.JButton btnDeleteModifierGroup;
    private javax.swing.JButton btnDeleteShift;
    private javax.swing.JButton btnEditModifierGroup;
    private javax.swing.JButton btnNewGroup;
    private javax.swing.JButton btnNewModifierGroup;
    private javax.swing.JButton btnNewTax;
    private javax.swing.JComboBox cbGroup;
    private javax.swing.JComboBox cbTax;
    private javax.swing.JCheckBox chkVisible;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable shiftTable;
    private javax.swing.JTable tableTicketItemModifierGroups;
    private DoubleTextField tfDiscountRate;
    private com.floreantpos.swing.FixedLengthTextField tfName;
    private DoubleTextField tfPrice;
    // End of variables declaration//GEN-END:variables
    private List<MenuItemModifierGroup> menuItemModifierGroups;
	private MenuItemMGListModel menuItemMGListModel;
	private JLabel lblImagePreview;
	private JButton btnClearImage;
	private JCheckBox cbShowTextWithImage;
	private JLabel lblBuyPrice;
	private DoubleTextField tfBuyPrice;
	private JLabel lblKitchenPrinter;
	private JComboBox<VirtualPrinter> cbPrinter;
    
    private void addMenuItemModifierGroup() {
    	try {
			MenuItemModifierGroupForm form = new MenuItemModifierGroupForm();
			BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(), true);
			dialog.open();
			if (!dialog.isCanceled()) {
				MenuItemModifierGroup modifier = (MenuItemModifierGroup) form.getBean();
				//modifier.setParentMenuItem((MenuItem) this.getBean());
				menuItemMGListModel.add(modifier);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
    }
    
    private void editMenuItemModifierGroup() {
    	try {
    		int index = tableTicketItemModifierGroups.getSelectedRow();
    		if(index < 0) return;
    		
    		MenuItemModifierGroup menuItemModifierGroup = menuItemMGListModel.get(index);
    		
    		MenuItemModifierGroupForm form = new MenuItemModifierGroupForm(menuItemModifierGroup);
    		BeanEditorDialog dialog = new BeanEditorDialog(form, getParentFrame(), true);
    		dialog.open();
    		if (!dialog.isCanceled()) {
    			//menuItemModifierGroup.setParentMenuItem((MenuItem) this.getBean());
    			menuItemMGListModel.fireTableDataChanged();
    		}
    	} catch (Exception x) {
    		MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
    	}
    }
    private void deleteMenuItemModifierGroup() {
    	try {
    		int index = tableTicketItemModifierGroups.getSelectedRow();
    		if(index < 0) return;
    		
    		if(ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.CONFIRM) == ConfirmDeleteDialog.YES){
    			menuItemMGListModel.remove(index);
    		}
    	} catch (Exception x) {
    		MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
    	}
    }
    
	@Override
	public boolean save() {
		try {
			if(!updateModel()) return false;
			
			MenuItem menuItem = (MenuItem) getBean();
			MenuItemDAO menuItemDAO = new MenuItemDAO();
			menuItemDAO.saveOrUpdate(menuItem);
		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, e);
			return false;
		}
		return true;
	}

	@Override
	protected void updateView() {
		MenuItem menuItem = (MenuItem) getBean();
		
		if(menuItem.getId() != null && !Hibernate.isInitialized(menuItem.getMenuItemModiferGroups())) {
			//initialize food item modifer groups.
			MenuItemDAO dao = new MenuItemDAO();
			Session session = dao.getSession();
			menuItem = (MenuItem) session.merge(menuItem);
			Hibernate.initialize(menuItem.getMenuItemModiferGroups());
			session.close();
		}
		
		tfName.setText(menuItem.getName());
		tfPrice.setText(String.valueOf(menuItem.getPrice()));
		tfDiscountRate.setText(String.valueOf(menuItem.getDiscountRate()));
		chkVisible.setSelected(menuItem.isVisible());
		cbShowTextWithImage.setSelected(menuItem.isShowImageOnly());
		if(menuItem.getImage() != null) {
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(menuItem.getImage()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
			lblImagePreview.setIcon(imageIcon);
		}
		
		if(menuItem.getId() == null) {
//			cbGroup.setSelectedIndex(0);
//			cbTax.setSelectedIndex(0);
		}
		else {
			cbGroup.setSelectedItem(menuItem.getParent());
			cbTax.setSelectedItem(menuItem.getTax());
		}
		
		cbPrinter.setSelectedItem(menuItem.getVirtualPrinter());
	}

	@Override
	protected boolean updateModel() {
		String itemName = tfName.getText();
		if(POSUtil.isBlankOrNull(itemName)) {
			MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}
		
		MenuItem menuItem = (MenuItem) getBean();
		menuItem.setName(itemName);
		menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
		menuItem.setPrice(Double.valueOf(tfPrice.getText()));
		menuItem.setTax((Tax) cbTax.getSelectedItem());
		menuItem.setVisible(chkVisible.isSelected());
		menuItem.setShowImageOnly(cbShowTextWithImage.isSelected());
		
        try {
            menuItem.setDiscountRate(Double.parseDouble(tfDiscountRate.getText()));
        } catch (Exception x){}
		menuItem.setMenuItemModiferGroups(menuItemModifierGroups);
		menuItem.setShifts(shiftTableModel.getShifts());
		
		int tabCount = tabbedPane.getTabCount();
		for(int i = 0; i< tabCount; i++) {
			Component componentAt = tabbedPane.getComponent(i);
			if(componentAt instanceof IUpdatebleView) {
				IUpdatebleView view = (IUpdatebleView) componentAt;
				if(!view.updateModel(menuItem)) {
					return false;
				}
			}
		}
		
		menuItem.setVirtualPrinter((VirtualPrinter) cbPrinter.getSelectedItem());
		
		return true;
	}
	
	public String getDisplayText() {
    	MenuItem foodItem = (MenuItem) getBean();
    	if(foodItem.getId() == null) {
    		return com.floreantpos.POSConstants.NEW_MENU_ITEM;
    	}
    	return com.floreantpos.POSConstants.EDIT_MENU_ITEM;
    }
	
	class MenuItemMGListModel extends AbstractTableModel {
		String[] cn = {com.floreantpos.POSConstants.GROUP_NAME, com.floreantpos.POSConstants.MIN_QUANTITY, com.floreantpos.POSConstants.MAX_QUANTITY};
		
		MenuItemMGListModel(){
		}
		
		public MenuItemModifierGroup get(int index) {
			return menuItemModifierGroups.get(index);
		}
		
		public void add(MenuItemModifierGroup group) {
			if(menuItemModifierGroups == null) {
				menuItemModifierGroups = new ArrayList<MenuItemModifierGroup>();
			}
			menuItemModifierGroups.add(group);
			fireTableDataChanged();
		}
		
		public void remove(int index) {
			if(menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(index);
			fireTableDataChanged();
		}
		
		public void remove(MenuItemModifierGroup group) {
			if(menuItemModifierGroups == null) {
				return;
			}
			menuItemModifierGroups.remove(group);
			fireTableDataChanged();
		}

		public int getRowCount() {
			if(menuItemModifierGroups == null) return 0;
			
			return menuItemModifierGroups.size();

		}

		public int getColumnCount() {
			return cn.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return cn[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemModifierGroup menuItemModifierGroup = menuItemModifierGroups.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return menuItemModifierGroup.getModifierGroup().getName();
					
				case 1:
					return Integer.valueOf(menuItemModifierGroup.getMinQuantity());
					
				case 2:
					return Integer.valueOf(menuItemModifierGroup.getMaxQuantity());
			}
			return null;
		}
	}
	
	class ShiftTableModel extends AbstractTableModel {
		List<MenuItemShift> shifts;
		String[] cn = {com.floreantpos.POSConstants.START_TIME, com.floreantpos.POSConstants.END_TIME, com.floreantpos.POSConstants.PRICE};
		Calendar calendar = Calendar.getInstance();
		
		ShiftTableModel(List<MenuItemShift> shifts){
			if(shifts == null) {
				this.shifts = new ArrayList<MenuItemShift>();
			}
			else {
				this.shifts = new ArrayList<MenuItemShift>(shifts);
			}
		}
		
		public MenuItemShift get(int index) {
			return shifts.get(index);
		}
		
		public void add(MenuItemShift group) {
			if(shifts == null) {
				shifts = new ArrayList<MenuItemShift>();
			}
			shifts.add(group);
			fireTableDataChanged();
		}
		
		public void remove(int index) {
			if(shifts == null) {
				return;
			}
			shifts.remove(index);
			fireTableDataChanged();
		}
		
		public void remove(MenuItemShift group) {
			if(shifts == null) {
				return;
			}
			shifts.remove(group);
			fireTableDataChanged();
		}

		public int getRowCount() {
			if(shifts == null) return 0;
			
			return shifts.size();

		}

		public int getColumnCount() {
			return cn.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return cn[column];
		}
		
		public List<MenuItemShift> getShifts() {
			return shifts;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItemShift shift = shifts.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getStartTime());
					
				case 1:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getShift().getEndTime());
					
				case 2:
					return String.valueOf(shift.getShiftPrice());
			}
			return null;
		}
	}
	
	private void addShift() {
		//TODO: ???
		MenuItemShiftDialog dialog = new MenuItemShiftDialog((Dialog) this.getTopLevelAncestor());
		dialog.setSize(350, 220);
        dialog.open();
        
        if(!dialog.isCanceled()) {
        	MenuItemShift menuItemShift = dialog.getMenuItemShift();
        	shiftTableModel.add(menuItemShift);
        }
	}
	
	private void deleteShift() {
		int selectedRow = shiftTable.getSelectedRow();
		if(selectedRow >= 0) {
			shiftTableModel.remove(selectedRow);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("AddModifierGroup")) {
			addMenuItemModifierGroup();
		}
		else if(actionCommand.equals("EditModifierGroup")) {
			editMenuItemModifierGroup();
		}
		else if(actionCommand.equals("DeleteModifierGroup")) {
			deleteMenuItemModifierGroup();
		}
		else if(actionCommand.equals(com.floreantpos.POSConstants.ADD_SHIFT)) {
			addShift();
		}
		else if(actionCommand.equals(com.floreantpos.POSConstants.DELETE_SHIFT)) {
			deleteShift();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Component selectedComponent = tabbedPane.getSelectedComponent();
		if(!(selectedComponent instanceof IUpdatebleView)){
			return;
		}
		
		IUpdatebleView view = (IUpdatebleView) selectedComponent;
		
		MenuItem menuItem = (MenuItem) getBean();
		view.initView(menuItem);
	}
}
