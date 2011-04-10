package com.floreantpos.ui.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.main.Application;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.HourlyLaborReportModel;
import com.floreantpos.report.services.ReportService;
import com.floreantpos.swing.ListComboBoxModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

/**
 * Created by IntelliJ IDEA.
 * User: mshahriar
 * Date: Feb 28, 2007
 * Time: 12:25:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class HourlyLaborReportView extends TransparentPanel {
    private JButton btnGo;
    private JComboBox cbTerminal;
    private JXDatePicker fromDatePicker;
    private JXDatePicker toDatePicker;
    private JPanel reportPanel;
    private JPanel contentPane;
    private JComboBox cbUserType;

    public HourlyLaborReportView() {
        cbUserType.setModel(new DefaultComboBoxModel(new String[]{com.floreantpos.POSConstants.ALL, com.floreantpos.POSConstants.SERVER, com.floreantpos.POSConstants.CASHIER, com.floreantpos.POSConstants.MANAGER}));

        TerminalDAO terminalDAO = new TerminalDAO();
        List terminals = terminalDAO.findAll();
        terminals.add(0, com.floreantpos.POSConstants.ALL);
        cbTerminal.setModel(new ListComboBoxModel(terminals));


        setLayout(new BorderLayout());
        add(contentPane);

        btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewReport();
            }
        });
    }

    private void viewReport() {
        Date fromDate = fromDatePicker.getDate();
        Date toDate = toDatePicker.getDate();

        if (fromDate.after(toDate)) {
            POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
            return;
        }

        String userType = (String) cbUserType.getSelectedItem();
        if (userType.equalsIgnoreCase(com.floreantpos.POSConstants.ALL)) {
            userType = null;
        }
        Terminal terminal = null;
        if (cbTerminal.getSelectedItem() instanceof Terminal) {
            terminal = (Terminal) cbTerminal.getSelectedItem();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(fromDate);

        calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fromDate = calendar.getTime();

        calendar.clear();
        calendar2.setTime(toDate);
        calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        toDate = calendar.getTime();


        TicketDAO ticketDAO = TicketDAO.getInstance();
        AttendenceHistoryDAO attendenceHistoryDAO = new AttendenceHistoryDAO();
        ArrayList<LaborReportData> rows = new ArrayList<LaborReportData>();

        DecimalFormat formatter = new DecimalFormat("00");

        int grandTotalChecks = 0;
        int grandTotalGuests = 0;
        double grandTotalSales = 0;
        double grandTotalMHr = 0;
        double grandTotalLabor = 0;
        double grandTotalSalesPerMHr = 0;
        double grandTotalGuestsPerMHr = 0;
        double grandTotalCheckPerMHr = 0;
        double grandTotalLaborCost = 0;

        for (int i = 0; i < 24; i++) {
            List<Ticket> tickets = ticketDAO.findTicketsForLaborHour(fromDate, toDate, i, userType, terminal);
            List<User> users = attendenceHistoryDAO.findNumberOfClockedInUserAtHour(fromDate, toDate, i, userType, terminal);

            int manHour = users.size();
            int totalChecks = 0;
            int totalGuests = 0;
            double totalSales = 0;
            double labor = 0;
            double salesPerMHr = 0;
            double guestsPerMHr = 0;
            double checksPerMHr = 0;
            //double laborCost = 0;

            for (Ticket ticket : tickets) {
                ++totalChecks;
                totalGuests += ticket.getNumberOfGuests();
                totalSales += ticket.getTotalAmount();
            }

            for (User user : users) {
                labor += (user.getCostPerHour() == null ? 0 : user.getCostPerHour());
            }
            if (manHour > 0) {
                labor = labor / manHour;
                salesPerMHr = totalSales / manHour;
                guestsPerMHr = (double) totalGuests / manHour;
                checksPerMHr = totalChecks / manHour;
                //laborCost =
            }

            LaborReportData reportData = new LaborReportData();
            reportData.setPeriod(formatter.format(i) + ":00 - " + formatter.format(i) + ":59");
            reportData.setManHour(manHour);
            reportData.setNoOfChecks(totalChecks);
            reportData.setSales(totalSales);
            reportData.setNoOfGuests(totalGuests);
            reportData.setLabor(labor);
            reportData.setSalesPerMHr(salesPerMHr);
            reportData.setGuestsPerMHr(guestsPerMHr);
            reportData.setCheckPerMHr(checksPerMHr);

            rows.add(reportData);

            grandTotalChecks += totalChecks;
            grandTotalGuests += totalGuests;
            grandTotalSales += totalSales;
            grandTotalMHr += manHour;
            grandTotalLabor += labor;
            grandTotalSalesPerMHr += salesPerMHr;
            grandTotalCheckPerMHr += checksPerMHr;
            grandTotalGuestsPerMHr += guestsPerMHr;
            //grandTotalLaborCost +=

        }

        ArrayList<LaborReportData> shiftReportRows = new ArrayList<LaborReportData>();
        ShiftDAO shiftDAO = new ShiftDAO();
        List<Shift> shifts = shiftDAO.findAll();
        for (Shift shift : shifts) {
            List<Ticket> tickets = ticketDAO.findTicketsForShift(fromDate, toDate, shift, userType, terminal);
            List<User> users = attendenceHistoryDAO.findNumberOfClockedInUserAtShift(fromDate, toDate, shift, userType, terminal);

            int manHour = users.size();
            int totalChecks = 0;
            int totalGuests = 0;
            double totalSales = 0;
            double labor = 0;
            double salesPerMHr = 0;
            double guestsPerMHr = 0;
            double checksPerMHr = 0;
            //double laborCost = 0;

            for (Ticket ticket : tickets) {
                ++totalChecks;
                totalGuests += ticket.getNumberOfGuests();
                totalSales += ticket.getTotalAmount();
            }

            for (User user : users) {
                labor += (user.getCostPerHour() == null ? 0 : user.getCostPerHour());
            }
            if (manHour > 0) {
                labor = labor / manHour;
                salesPerMHr = totalSales / manHour;
                guestsPerMHr = (double) totalGuests / manHour;
                checksPerMHr = totalChecks / manHour;
                //laborCost =
            }

            LaborReportData reportData = new LaborReportData();
            reportData.setPeriod(shift.getName());
            reportData.setManHour(manHour);
            reportData.setNoOfChecks(totalChecks);
            reportData.setSales(totalSales);
            reportData.setNoOfGuests(totalGuests);
            reportData.setLabor(labor);
            reportData.setSalesPerMHr(salesPerMHr);
            reportData.setGuestsPerMHr(guestsPerMHr);
            reportData.setCheckPerMHr(checksPerMHr);

            shiftReportRows.add(reportData);
        }

        try {
            JasperReport hourlyReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/ui/report/hourly_labor_subreport.jasper"));
            JasperReport shiftReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/ui/report/hourly_labor_shift_subreport.jasper"));

            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/ui/report/hourly_labor_report.jasper"));

            HashMap properties = new HashMap();
            ReportUtil.populateRestaurantProperties(properties);
            properties.put("reportTitle", com.floreantpos.POSConstants.HOURLY_LABOR_REPORT);
            properties.put("reportTime", ReportService.formatFullDate(new Date()));
            properties.put("fromDay", ReportService.formatShortDate(fromDate));
            properties.put("toDay", ReportService.formatShortDate(toDate));
            properties.put(com.floreantpos.POSConstants.TYPE, com.floreantpos.POSConstants.BY_RANGE_ACTUAL);
            properties.put("dept", userType == null ? com.floreantpos.POSConstants.ALL : userType);
            properties.put("incr", "60 Minute");
            properties.put("cntr", terminal == null ? com.floreantpos.POSConstants.ALL : terminal.getName());

            properties.put("totalChecks", String.valueOf(grandTotalChecks));
            properties.put("totalGuests", String.valueOf(grandTotalGuests));
            properties.put("totalSales", Application.formatNumber(grandTotalSales));
            properties.put("totalMHr", Application.formatNumber(grandTotalMHr));
            properties.put("totalLabor", Application.formatNumber(grandTotalLabor));
            properties.put("totalSalesPerMhr", Application.formatNumber(grandTotalSalesPerMHr));
            properties.put("totalGuestsPerMhr", Application.formatNumber(grandTotalCheckPerMHr));
            properties.put("totalCheckPerMHr", Application.formatNumber(grandTotalGuestsPerMHr));
            properties.put("totalLaborCost", Application.formatNumber(grandTotalLaborCost));

            properties.put("hourlyReport", hourlyReport);
            properties.put("hourlyReportDatasource", new JRTableModelDataSource(new HourlyLaborReportModel(rows)));
            properties.put("shiftReport", shiftReport);
            properties.put("shiftReportDatasource", new JRTableModelDataSource(new HourlyLaborReportModel(shiftReportRows)));

            JasperPrint print = JasperFillManager.fillReport(report, properties, new JREmptyDataSource());

            JRViewer viewer = new JRViewer(print);
            reportPanel.removeAll();
            reportPanel.add(viewer);
            reportPanel.revalidate();
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText(com.floreantpos.POSConstants.FROM + ":");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText(com.floreantpos.POSConstants.TO + ":");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText(com.floreantpos.POSConstants.TERMINAL);
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fromDatePicker = new JXDatePicker();
        panel1.add(fromDatePicker, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));
        toDatePicker = new JXDatePicker();
        panel1.add(toDatePicker, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cbTerminal = new JComboBox();
        panel1.add(cbTerminal, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(147, 22), null, 0, false));
        btnGo = new JButton();
        btnGo.setText(com.floreantpos.POSConstants.GO);
        panel1.add(btnGo, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(147, 23), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText(com.floreantpos.POSConstants.USER_TYPE + ":");
        panel1.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbUserType = new JComboBox();
        panel1.add(cbUserType, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(147, 22), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(reportPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public static class LaborReportData {
        private String period;
        private int noOfChecks;
        private int noOfGuests;
        private double sales;
        private double manHour;
        private double labor;
        private double salesPerMHr;
        private double guestsPerMHr;
        private double checkPerMHr;
        private double laborCost;

        public double getCheckPerMHr() {
            return checkPerMHr;
        }

        public void setCheckPerMHr(double checkPerMHr) {
            this.checkPerMHr = checkPerMHr;
        }

        public double getGuestsPerMHr() {
            return guestsPerMHr;
        }

        public void setGuestsPerMHr(double guestsPerMHr) {
            this.guestsPerMHr = guestsPerMHr;
        }

        public double getLabor() {
            return labor;
        }

        public void setLabor(double labor) {
            this.labor = labor;
        }

        public double getLaborCost() {
            return laborCost;
        }

        public void setLaborCost(double laborCost) {
            this.laborCost = laborCost;
        }

        public double getManHour() {
            return manHour;
        }

        public void setManHour(double manHour) {
            this.manHour = manHour;
        }

        public int getNoOfChecks() {
            return noOfChecks;
        }

        public void setNoOfChecks(int noOfChecks) {
            this.noOfChecks = noOfChecks;
        }

        public int getNoOfGuests() {
            return noOfGuests;
        }

        public void setNoOfGuests(int noOfGuests) {
            this.noOfGuests = noOfGuests;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public double getSales() {
            return sales;
        }

        public void setSales(double sales) {
            this.sales = sales;
        }

        public double getSalesPerMHr() {
            return salesPerMHr;
        }

        public void setSalesPerMHr(double salesPerMHr) {
            this.salesPerMHr = salesPerMHr;
        }

    }
}
