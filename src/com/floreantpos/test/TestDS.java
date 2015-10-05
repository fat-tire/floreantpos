package com.floreantpos.test;

import com.floreantpos.Messages;
import com.floreantpos.report.AbstractReportDataSource;

public class TestDS extends AbstractReportDataSource {
	
	

	public TestDS() {
		super(new String[] {Messages.getString("TestDS.0"),Messages.getString("TestDS.1")}); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public int getRowCount() {
		return 2;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return Messages.getString("TestDS.2"); //$NON-NLS-1$
	}

}
