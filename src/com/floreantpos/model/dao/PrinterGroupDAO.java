package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.dao.BasePrinterGroupDAO;


public class PrinterGroupDAO extends BasePrinterGroupDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public PrinterGroupDAO () {}

	public PrinterGroup getReceiptPrinterGroup() {
		Session session = getSession();
		
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(PrinterGroup.PROP_NAME, PrinterGroup.GROUP_RECEIPT_PRINTER));
		
		PrinterGroup printerGroup = (PrinterGroup) criteria.uniqueResult();
		
		if(printerGroup == null) {
			printerGroup = new PrinterGroup();
			printerGroup.setName(PrinterGroup.GROUP_RECEIPT_PRINTER);
			
			save(printerGroup);
		}
		
		return printerGroup;
	}
	
	public PrinterGroup getKitchenPrinterGroup() {
		Session session = getSession();
		
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(PrinterGroup.PROP_NAME, PrinterGroup.GROUP_KITCHEN_PRINTER));
		
		PrinterGroup printerGroup = (PrinterGroup) criteria.uniqueResult();
		
		if(printerGroup == null) {
			printerGroup = new PrinterGroup();
			printerGroup.setName(PrinterGroup.GROUP_KITCHEN_PRINTER);
			
			save(printerGroup);
		}
		
		return printerGroup;
	}
}