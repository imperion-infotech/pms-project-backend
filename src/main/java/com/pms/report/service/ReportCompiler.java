/**
 * 
 */
package com.pms.report.service;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ReportCompiler {

    private JasperReport compiledBookingReport;
    
    private JasperReport compiledRoomStatusReport;
    
    private JasperReport compiledDailyCollectionReport;
    
    private JasperReport compiledGuestListReport;
    
    private JasperReport compiledHouseKeepingReport;
    
    private JasperReport compiledShiftReport;
    
    private JasperReport compiledMonthlyCollectionPaymentTypeReport;

    @PostConstruct
    public void compileReports() throws Exception {

        // Spring's ClassPathResource is most reliable
        ClassPathResource resource = new ClassPathResource("reports/booking.jasper");

        InputStream stream = resource.getInputStream();

        compiledBookingReport = (JasperReport) JRLoader.loadObject(stream);
        
     // Room Status Report
        ClassPathResource roomStatusResource =
                new ClassPathResource("reports/room_status_report_v11.jasper");
        
        ClassPathResource dailyCollectionResource =
                new ClassPathResource("reports/daily_collection_v10.jasper");
        
        ClassPathResource guestListResource =
                new ClassPathResource("reports/guest_list_report_v12.jasper");
        
        ClassPathResource houseKeepingResource =
                new ClassPathResource("reports/house_keeping_report_v9.jasper");
        
        ClassPathResource shiftReportResource =
                new ClassPathResource("reports/shift_report_fixed_v2.jasper");
        
        ClassPathResource monthlyCollectionPaymentTypeReportResource =
                new ClassPathResource("reports/monthly_collection_payment_type_report_v2.jasper");

        compiledRoomStatusReport =
                (JasperReport) JRLoader.loadObject(
                        roomStatusResource.getInputStream());
        
        compiledDailyCollectionReport  =
                (JasperReport) JRLoader.loadObject(
                        dailyCollectionResource.getInputStream());
        compiledGuestListReport =
        		(JasperReport) JRLoader.loadObject(
        				guestListResource.getInputStream());
        
        compiledHouseKeepingReport =
        		(JasperReport) JRLoader.loadObject(
        				houseKeepingResource.getInputStream());
        
        compiledShiftReport =
        		(JasperReport) JRLoader.loadObject(
        				shiftReportResource.getInputStream());
        
        compiledMonthlyCollectionPaymentTypeReport =
        		(JasperReport) JRLoader.loadObject(
        monthlyCollectionPaymentTypeReportResource.getInputStream());
        
    }

    public JasperReport getRoomStatusReport()
   {
        return compiledRoomStatusReport;
    }
    
    public JasperReport getBookingReport() {
        return compiledBookingReport;
    }
    
    public JasperReport getDailyCollectionReport()
    {
         return compiledDailyCollectionReport;
     }
    
    public JasperReport getGuestListReport()
    {
         return compiledGuestListReport;
     }
    
    public JasperReport getHouseKeepingReport()
    {
         return compiledHouseKeepingReport;
     }
    
    public JasperReport getShiftReport()
    {
         return compiledShiftReport;
     }
    
    public JasperReport getMonthlyCollectionPaymentTypeReport()
    {
         return compiledMonthlyCollectionPaymentTypeReport;
     }
}