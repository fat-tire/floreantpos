package com.floreantpos.model;

import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class TipsCashoutReportTableModel extends ListTableModel {
	public TipsCashoutReportTableModel(List<TipsCashoutReportData> datas) {
		super(new String[] {"Ref#", "CD Type", "Total", "Tips"}, datas);
	}
	
	public TipsCashoutReportTableModel(List<TipsCashoutReportData> datas, String[] columnNames) {
		super(columnNames, datas);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		TipsCashoutReportData data = (TipsCashoutReportData) rows.get(rowIndex);
		
		switch(columnIndex) {
		case 0:
			return data.getTicketId();
			
		case 1:
			return data.getSaleType();
			
		case 2:
			return Application.formatNumber(data.getTicketTotal());
			
		case 3:
			return Application.formatNumber(data.getTips());
		}
		
		return null;
	}
}