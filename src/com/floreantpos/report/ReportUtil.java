/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class ReportUtil {
	private static Log logger = LogFactory.getLog(ReportUtil.class);
	
	private final static String USER_REPORT_DIR = "/printerlayouts/"; //$NON-NLS-1$
	private final static String DEFAULT_REPORT_DIR = "/com/floreantpos/report/template/"; //$NON-NLS-1$
	
	public static void populateRestaurantProperties(Map map) {
		JasperReport reportHeader = ReportUtil.getReport("report_header"); //$NON-NLS-1$
		
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		map.put("restaurantName", restaurant.getName()); //$NON-NLS-1$
		map.put("addressLine1", restaurant.getAddressLine1()); //$NON-NLS-1$
		map.put("addressLine2", restaurant.getAddressLine2()); //$NON-NLS-1$
		map.put("addressLine3", restaurant.getAddressLine3()); //$NON-NLS-1$
		map.put("phone", restaurant.getTelephone()); //$NON-NLS-1$
		map.put("reportHeader", reportHeader); //$NON-NLS-1$
	}
	
	public static JasperReport getReport(String reportName) {
		InputStream resource = null;

		try {
			resource = ReceiptPrintService.class.getResourceAsStream(USER_REPORT_DIR + reportName + ".jasper"); //$NON-NLS-1$
			if (resource == null) {
				return compileReport(reportName);
			}
			else {
				return (JasperReport) JRLoader.loadObject(resource);
			}
		} catch (Exception e) {
			return getDefaultReport(reportName);
			
		} finally {
			IOUtils.closeQuietly(resource);
		}
		
	}
	
	private static JasperReport compileReport(String reportName) throws Exception {
		InputStream in = null;
		InputStream in2 = null;
		FileOutputStream out = null;
		File jasperFile = null;
		
		try {
			File jrxmlFile = new File(ReceiptPrintService.class.getResource(USER_REPORT_DIR + reportName + ".jrxml").getFile()); //$NON-NLS-1$
			File dir = jrxmlFile.getParentFile();
			jasperFile = new File(dir, reportName + ".jasper"); //$NON-NLS-1$

			in = ReceiptPrintService.class.getResourceAsStream(USER_REPORT_DIR + reportName + ".jrxml"); //$NON-NLS-1$
			out = new FileOutputStream(jasperFile);
			JasperCompileManager.compileReportToStream(in, out);
			
			in2 = ReceiptPrintService.class.getResourceAsStream(USER_REPORT_DIR + reportName + ".jasper"); //$NON-NLS-1$
			return (JasperReport) JRLoader.loadObject(in2);
			
		} catch (Exception e) {
			IOUtils.closeQuietly(out);
			if(jasperFile != null) {
				jasperFile.delete();
			}
			
			throw e;
		}
		
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(in2);
			IOUtils.closeQuietly(out);
		}
	}
	
	private static JasperReport getDefaultReport(String reportName) {
		InputStream resource = null;

		try {
			
			resource = ReceiptPrintService.class.getResourceAsStream(DEFAULT_REPORT_DIR + reportName + ".jasper"); //$NON-NLS-1$
			return (JasperReport) JRLoader.loadObject(resource);
			
		} catch (Exception e) {
			logger.error(e);
			return null;
			
		} finally {
			IOUtils.closeQuietly(resource);
		}
	}
	
	public static void main(String[] args) {
		URL resource = ReceiptPrintService.class.getResource("/printerlayouts/ticket-receipt.jrxml"); //$NON-NLS-1$
		String externalForm = resource.getFile();
		PosLog.info(ReportUtil.class, resource.getProtocol());
		PosLog.info(ReportUtil.class, externalForm);
	}
}
