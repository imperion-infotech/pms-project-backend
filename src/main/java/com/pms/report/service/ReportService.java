/**
 * 
 */

package com.pms.report.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.booking.service.BookingServiceImpl;
import com.pms.building.dao.BuildingsRepository;
import com.pms.building.entity.Building;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.security.service.AuthService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ReportService {

    @Autowired
    private BuildingsRepository buildingRepository;
    
    @Autowired
    private HotelRepository hotelRepository;
    
    @Autowired
    private DataSource dataSource;
    
    private AuthService authService;
    
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    
    
    @Auditable(action = "CREATE", entity = "BUILDINGREPORT")
    public byte[] generateBuildingReport() throws Exception {

        // 1. Fetch data from DB
        List<Building> buildings = buildingRepository.findAll();

        // 2. Load jrxml template
        InputStream template = getClass()
                .getResourceAsStream("/reports/building_report.jrxml");

        // 3. Compile template
        JasperReport jasperReport = JasperCompileManager.compileReport(template);

        // 4. Set parameters
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Building Report");
        params.put("hotelName", "My Hotel");

        // 5. Set data source
        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(buildings);

        // 6. Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, params, dataSource);

        // 7. Export to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    @Autowired                        // ← inject ReportCompiler here
    private ReportCompiler reportCompiler;

    
    @Auditable(action = "CREATE", entity = "BOOKINGREPORT")
    public byte[] generateBookingReport(Long hotelId, String format) throws Exception {

        // Use pre-compiled report instead of compiling every time
        JasperReport jasperReport = reportCompiler.getBookingReport();  // ← use it here

        // Pass parameters
        Map<String, Object> params = new HashMap<>();
        params.put("HOTEL_ID", hotelId);

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport,
            params,
            dataSource.getConnection()
        );

        // Export
        if ("xlsx".equalsIgnoreCase(format)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();
            return out.toByteArray();
        } else {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
    
    @Auditable(action = "CREATE", entity = "ROOMSTATUSREPORT")    
//    @Async
    public byte[] generateRoomStatusReport(Long hotelId, String format, LocalDate fromDate, LocalDate toDate,Long buildingId,Long floorId,String buildingName,String floorName) 
    		throws Exception {

        // Fetch hotel details from DB
        Hotel hotel = hotelRepository.findById(hotelId)
                        .orElseThrow(() -> new Exception("Hotel not found"));

        // Get logged in username
        String username = SecurityContextHolder.getContext()
                            .getAuthentication().getName();
        
        Map<String, Object> params = new HashMap<>();
        params.put("HOTEL_ID",      hotelId);
        params.put("HOTEL_NAME",    hotel.getHotelName());
        params.put("HOTEL_ADDRESS", hotel.getAddress());
        params.put("HOTEL_CITY",    hotel.getCity());
        params.put("HOTEL_STATE",   hotel.getState());
        params.put("HOTEL_COUNTRY", hotel.getCountry());
        params.put("HOTEL_ZIPCODE", hotel.getZipCode());
        params.put("HOTEL_PHONE",   hotel.getContactNumber());
        params.put("HOTEL_EMAIL",   hotel.getEmail());
        params.put("PRINTED_BY",    username);
        params.put("REPORT_DATE",
            new SimpleDateFormat("M/d/yyyy").format(new Date()));
        params.put("FROM_DATE", java.sql.Date.valueOf(fromDate));
        params.put("TO_DATE", java.sql.Date.valueOf(toDate));
        /*
        params.put("BUILDING_ID",buildingId );
        params.put("FLOOR_ID",floorId );
        */
        
        params.put("BUILDING_ID",
                buildingId != null ? buildingId.toString() : null);
        params.put("FLOOR_ID",
                floorId != null ? floorId.toString() : null);
        
        params.put("BUILDING_NAME",buildingName );
        params.put("FLOOR_NAME",floorName );
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            reportCompiler.getRoomStatusReport(),
            params,
            dataSource.getConnection()
        );

        // export PDF or Excel...
     // Export
        if ("xlsx".equalsIgnoreCase(format)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();
            return out.toByteArray();
        } else {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
    
 
    @Auditable(action = "CREATE", entity = "DAILYCOLLECTIONREPORT")
    public byte[] generateDailyCollectionReport(Long hotelId, String format, LocalDateTime fromDate, LocalDateTime toDate) {
    	   // Fetch hotel details from DB
        Hotel hotel = hotelRepository.findById(hotelId)
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));

        // Get logged in username
        String username = SecurityContextHolder.getContext()
                            .getAuthentication().getName();
        
        Map<String, Object> params = new HashMap<>();
        params.put("HOTEL_ID",      hotelId);
        params.put("HOTEL_NAME",    hotel.getHotelName());
        params.put("HOTEL_ADDRESS", hotel.getAddress());
        params.put("HOTEL_CITY",    hotel.getCity());
        params.put("HOTEL_STATE",   hotel.getState());
        params.put("HOTEL_COUNTRY", hotel.getCountry());
        params.put("HOTEL_ZIPCODE", hotel.getZipCode());
        params.put("HOTEL_PHONE",   hotel.getContactNumber());
        params.put("HOTEL_EMAIL",   hotel.getEmail());
        params.put("PRINTED_BY",    username);
        params.put("REPORT_DATE",
            new SimpleDateFormat("M/d/yyyy").format(new Date()));
        params.put("FROM_DATE", java.sql.Timestamp.valueOf(fromDate));
        params.put("TO_DATE", java.sql.Timestamp.valueOf(toDate));
        JasperPrint jasperPrint = null;
        try {
             jasperPrint = JasperFillManager.fillReport(
                    reportCompiler.getDailyCollectionReport(),
                    params,
                    dataSource.getConnection()
            );

       

        // export PDF or Excel...
     // Export
        if ("xlsx".equalsIgnoreCase(format)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();
            return out.toByteArray();
        } else {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        
        } catch (JRException | SQLException e) {
            throw new RuntimeException("Error generating Daily Collection Report", e);
        }
    }
    
    @Auditable(action = "CREATE", entity = "GUESTLISTREPORT")
    public byte[] generateGuestListReport(Long hotelId, String format, LocalDate fromDate, LocalDate toDate,String guestStatus) {
 	   // Fetch hotel details from DB
     Hotel hotel = hotelRepository.findById(hotelId)
                     .orElseThrow(() -> new RuntimeException("Hotel not found"));

     // Get logged in username
     String username = SecurityContextHolder.getContext()
                         .getAuthentication().getName();
     
     Map<String, Object> params = new HashMap<>();
     params.put("HOTEL_ID",      hotelId);
     params.put("HOTEL_NAME",    hotel.getHotelName());
     params.put("HOTEL_ADDRESS", hotel.getAddress());
     params.put("HOTEL_CITY",    hotel.getCity());
     params.put("HOTEL_STATE",   hotel.getState());
     params.put("HOTEL_COUNTRY", hotel.getCountry());
     params.put("HOTEL_ZIPCODE", hotel.getZipCode());
     params.put("HOTEL_PHONE",   hotel.getContactNumber());
     params.put("HOTEL_EMAIL",   hotel.getEmail());
     params.put("PRINTED_BY",    username);
     params.put("REPORT_DATE",
         new SimpleDateFormat("M/d/yyyy").format(new Date()));
         
     params.put("FROM_DATE", java.sql.Date.valueOf(fromDate));
     params.put("TO_DATE", java.sql.Date.valueOf(toDate));
     
     if(guestStatus.equals(""))
     {
     params.put("GUEST_STATUS",    "ALL");
     } else {
    	 params.put("GUEST_STATUS",  guestStatus );
     }
     
     JasperPrint jasperPrint = null;
     try {
          jasperPrint = JasperFillManager.fillReport(
                 reportCompiler.getGuestListReport(),
                 params,
                 dataSource.getConnection()
         );

    

     // export PDF or Excel...
  // Export
     if ("xlsx".equalsIgnoreCase(format)) {
         JRXlsxExporter exporter = new JRXlsxExporter();
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
         exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
         exporter.exportReport();
         return out.toByteArray();
     } else {
         return JasperExportManager.exportReportToPdf(jasperPrint);
     }
     
     } catch (JRException | SQLException e) {
         throw new RuntimeException("Error generating Guest List Report", e);
     }
 }
    
    
    @Auditable(action = "CREATE", entity = "HOUSEKEEPINGREPORT")
    public byte[] generateHouseKeepingReport(Long hotelId, String format, LocalDate fromDate, LocalDate toDate,Long buildingId,Long floorId,
    		String buildingName,String floorName) 
    		throws Exception {

    	// Fetch hotel info
        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new Exception("Hotel not found"));

        // Get logged in user
        String username = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        // Convert String dates to java.sql.Date
        java.sql.Date sqlFromDate = java.sql.Date.valueOf(fromDate); // "2026-06-15"
        java.sql.Date sqlToDate   = java.sql.Date.valueOf(toDate);

        Map<String, Object> params = new HashMap<>();
        params.put("hotelId",      hotelId);
        params.put("fromDate",     sqlFromDate);   // ← must be java.sql.Date
        params.put("toDate",       sqlToDate);     // ← must be java.sql.Date
        params.put("buildingId",   buildingId);    // null = all buildings
        params.put("floorId",      floorId);       // null = all floors

        // Hotel info for title
        params.put("hotelName",    hotel.getHotelName());
        params.put("hotelAddress", hotel.getAddress());
        params.put("hotelEmail",   hotel.getEmail() != null ? hotel.getEmail() : "");

        // Building/floor name for header display
        params.put("buildingName", buildingName != null ? buildingName : "All Buildings");
        params.put("floorName",    floorName    != null ? floorName    : "All Floors");

        // Print info
        params.put("printedBy",    username);
        params.put("printedOn",    new SimpleDateFormat("MM/dd/yyyy hh:mm a")
                                       .format(new Date()));
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            reportCompiler.getHouseKeepingReport(),
            params,
            dataSource.getConnection()
        );

        // export PDF or Excel...
     // Export
        if ("xlsx".equalsIgnoreCase(format)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();
            return out.toByteArray();
        } else {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
    
    @Auditable(action = "CREATE", entity = "SHIFTREPORT")
    public byte[] generateShiftReport(Long hotelId, String format, LocalDate fromDate, LocalDate toDate) {
  	   // Fetch hotel details from DB
      Hotel hotel = hotelRepository.findById(hotelId)
                      .orElseThrow(() -> new RuntimeException("Hotel not found"));

      // Get logged in username
      String username = SecurityContextHolder.getContext()
                          .getAuthentication().getName();
      
      Map<String, Object> params = new HashMap<>();
      params.put("hotelId",      hotelId);
      params.put("hotelName",    hotel.getHotelName());
      params.put("hotelAddress", hotel.getAddress());
      
      params.put("hotelPhone",   hotel.getContactNumber());
      params.put("hotelEmail",   hotel.getEmail());
      params.put("printedBy",    username);
//      params.put("printedOn",    new Date());
      params.put("printedOn",
    	        LocalDateTime.now()
    	                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
      params.put("REPORT_DATE",
          new SimpleDateFormat("M/d/yyyy").format(new Date()));
          
      params.put("fromDate", java.sql.Date.valueOf(fromDate));
      params.put("toDate", java.sql.Date.valueOf(toDate));
      
      
      JasperPrint jasperPrint = null;
      try {
           jasperPrint = JasperFillManager.fillReport(
                  reportCompiler.getShiftReport(),
                  params,
                  dataSource.getConnection()
          );

     

      // export PDF or Excel...
   // Export
      if ("xlsx".equalsIgnoreCase(format)) {
          JRXlsxExporter exporter = new JRXlsxExporter();
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
          exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
          exporter.exportReport();
          return out.toByteArray();
      } else {
          return JasperExportManager.exportReportToPdf(jasperPrint);
      }
      
      } catch (JRException | SQLException e) {
    	  logger.error("Error generating Shift Report", e);
    	  e.printStackTrace();
          throw new RuntimeException("Error generating Shift Report", e);
      }
}
    @Auditable(action = "CREATE", entity = "MONTHLY_COLLECTION_PAYMENTTYPE_REPORT")
    public byte[] generateMonthlyCollectionPaymentTypeReport(Long hotelId, String format, LocalDate fromDate, LocalDate toDate) {
   	   // Fetch hotel details from DB
       Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
                       .orElseThrow(() -> new RuntimeException("Hotel not found"));

       // Get logged in username
       String username = SecurityContextHolder.getContext()
                           .getAuthentication().getName();
       
       Map<String, Object> params = new HashMap<>();
       params.put("hotelId",      hotelId);
       params.put("hotelName",    hotel.getHotelName());
       params.put("hotelAddress", hotel.getAddress());
       
       params.put("hotelPhone",   hotel.getContactNumber());
       params.put("hotelEmail",   hotel.getEmail());
       params.put("printedBy",    username);
//       params.put("printedOn",    new Date());
       params.put("printedOn",
     	        LocalDateTime.now()
     	                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
       params.put("REPORT_DATE",
           new SimpleDateFormat("M/d/yyyy").format(new Date()));
           
       params.put("fromDate", java.sql.Date.valueOf(fromDate));
       params.put("toDate", java.sql.Date.valueOf(toDate));
       
       
       JasperPrint jasperPrint = null;
       try {
            jasperPrint = JasperFillManager.fillReport(
                   reportCompiler.getMonthlyCollectionPaymentTypeReport(),
                   params,
                   dataSource.getConnection()
           );
       // export PDF or Excel...
    // Export
       if ("xlsx".equalsIgnoreCase(format)) {
           JRXlsxExporter exporter = new JRXlsxExporter();
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
           exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
           exporter.exportReport();
           return out.toByteArray();
       } else {
           return JasperExportManager.exportReportToPdf(jasperPrint);
       }
       
       } catch (JRException | SQLException e) {
     	  logger.error("Error generating Monthly Collection Payment Type Report", e);
     	  e.printStackTrace();
           throw new RuntimeException("Error generating Monthly Collection Payment Type Report", e);
       }
 }
    
}