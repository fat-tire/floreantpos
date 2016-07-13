
package com.floreantpos.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.model.InventoryItem;
import com.floreantpos.swing.ListTableModel;

public class InventoryOnHandReportModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM-dd-yyyy hh:mm a"); //$NON-NLS-1$

	DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

	public InventoryOnHandReportModel() {
		super(new String[] { "itemgroup", "items", "barcode", "onHand", "cost", "onhandvalue" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		InventoryItem data = (InventoryItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return data.getItemGroup().getName();

			case 1:
				return data.getName();

			case 2:
				return data.getPackageBarcode();

			case 3:
				return String.valueOf(data.getTotalPackages());

			case 4:
				return String.valueOf(data.getUnitPurchasePrice());

			case 5:
				double totalOnHandValue = data.getTotalPackages() * data.getAveragePackagePrice();
				return String.valueOf(totalOnHandValue);

		}
		return null;
	}

}
