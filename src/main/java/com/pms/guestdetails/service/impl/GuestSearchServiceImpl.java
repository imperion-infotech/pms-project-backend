/**
 * 
 */
package com.pms.guestdetails.service.impl;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.booking.Booking;
import com.pms.document.dao.DocumentDetailsRepository;
import com.pms.document.dao.DocumentTypeRepository;
import com.pms.document.entity.DocumentDetails;
import com.pms.document.entity.DocumentType;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.guestdetails.dto.GuestSearchRequestDTO;
import com.pms.guestdetails.dto.GuestSearchResponseDTO;
import com.pms.guestdetails.service.IGuestSearchService;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.repository.PaymentDetailsRepository;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.impl.RentDetailsRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.search.specification.GuestSpecification;
import com.pms.security.repository.BookingRepository;
import com.pms.stay.dao.StayDetailsRepository;
import com.pms.stay.entity.StayDetails;

/**
 * 
 */
@Service
public class GuestSearchServiceImpl implements IGuestSearchService {

    @Autowired
    private GuestDetailsRepository repository;

    /*public List<GuestSearchResponseDTO> search(
            GuestSearchRequestDTO request) {

        return repository.searchGuests(

                request.getFirstName(),

                request.getLastName(),

                request.getDocumentNumber(),

                request.getRoomName(),

                request.getCheckInFromDate(),

                request.getCheckInToDate(),

                request.getCheckOutFromDate(),

                request.getCheckOutToDate(),

                request.getTotalRental()
        );
    }*/
    
    @Autowired
    private GuestSpecification guestSpecification;
    
    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;
    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;   // NEW
    @Autowired
    private StayDetailsRepository stayDetailsRepository;
    @Autowired
    private RoomMasterRepository roomMasterRepository;
    @Autowired
    private RentDetailsRepository rentDetailsRepository;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private BookingRepository bookingRepository;
    
    
    
    

   /* @Override
    public List<GuestSearchResponseDTO> searchGuests(GuestSearchRequestDTO request) {

        // Step 1: Filter by date using Specification
        Specification<GuestDetails> spec = guestSpecification.search(
                request.getCheckInFromDate(),
                request.getCheckInToDate(),
                request.getCheckOutFromDate(),
                request.getCheckOutToDate(),
                request.getHotelId()
        );

        List<GuestDetails> guests = repository.findAll(spec);

        // Step 2: Map and filter
        return guests.stream().map(g -> {

            PersonalDetails personal = (g.getPersonalDetailsId() != null)
                    ? personalDetailsRepository.findById(g.getPersonalDetailsId().longValue()).orElse(null)
                    : null;

            DocumentDetails document = (g.getDocumentDetailsId() != null)
                    ? documentDetailsRepository.findById(g.getDocumentDetailsId().longValue()).orElse(null)
                    : null;

            StayDetails stay = (g.getStayDetailsId() != null)
                    ? stayDetailsRepository.findById(g.getStayDetailsId().longValue()).orElse(null)
                    : null;

            RoomMaster room = (stay != null && stay.getRoomMasterId() != null)
                    ? roomMasterRepository.findById(stay.getRoomMasterId().longValue()).orElse(null)
                    : null;

            RentDetails rent = (g.getRentDetailsId() != null)
                    ? rentDetailsRepository.findById(g.getRentDetailsId().longValue()).orElse(null)
                    : null;

            String fName   = personal != null ? personal.getFirstName()       : null;
            String lName   = personal != null ? personal.getLastName()        : null;
            String docNum  = document != null ? document.getDocumentNumber()  : null;
            String rName   = room     != null ? room.getRoomName()            : null;
            Double rental  = rent     != null ? rent.getTotalRental()         : null;

            // Apply string/numeric filters in Java
            if (request.getFirstName() != null && (fName == null ||
                    !fName.toLowerCase().contains(request.getFirstName().toLowerCase()))) return null;

            if (request.getLastName() != null && (lName == null ||
                    !lName.toLowerCase().contains(request.getLastName().toLowerCase()))) return null;

            if (request.getDocumentNumber() != null && (docNum == null ||
                    !docNum.toLowerCase().contains(request.getDocumentNumber().toLowerCase()))) return null;

            if (request.getRoomName() != null && (rName == null ||
                    !rName.toLowerCase().contains(request.getRoomName().toLowerCase()))) return null;

            if (request.getTotalRental() != null && (rental == null ||
                    !rental.equals(request.getTotalRental()))) return null;
            
           

            return new GuestSearchResponseDTO(
                    g.getId(),
                    fName,
                    lName,
                    docNum,
                    rName,
                    g.getCheckInDate(),
                    g.getCheckOutDate(),
                    rental,
                    g.getHotelId()
            );

        })
        .filter(dto -> dto != null)
        .collect(Collectors.toList());
    }*/
    
    //TODO:previously working searchguest api method but it is not included document_category wise document number
    @Override
    public List<GuestSearchResponseDTO> searchGuests(GuestSearchRequestDTO request) {

    	Specification<GuestDetails> spec = guestSpecification.search(
                request.getCreatedFromDate(),
                request.getCreatedToDate(),
                request.getHotelId()
        );

        List<GuestDetails> guests = repository.findAll(spec);

        return guests.stream().map(g -> {

            PersonalDetails personal = (g.getPersonalDetailsId() != null)
                    ? personalDetailsRepository.findById(g.getPersonalDetailsId().longValue()).orElse(null)
                    : null;

            DocumentDetails document = (g.getDocumentDetailsId() != null)
                    ? documentDetailsRepository.findById(g.getDocumentDetailsId().longValue()).orElse(null)
                    : null;

            StayDetails stay = (g.getStayDetailsId() != null)
                    ? stayDetailsRepository.findById(g.getStayDetailsId().longValue()).orElse(null)
                    : null;

            RoomMaster room = (stay != null && stay.getRoomMasterId() != null)
                    ? roomMasterRepository.findById(stay.getRoomMasterId().longValue()).orElse(null)
                    : null;

            RentDetails rent = (g.getRentDetailsId() != null)
                    ? rentDetailsRepository.findById(g.getRentDetailsId().longValue()).orElse(null)
                    : null;

            // Payment: reverse lookup (guest_details_id FK lives on payment_details)
            List<PaymentDetails> payments = paymentDetailsRepository.findByGuestDetailsId(g.getId());
            PaymentDetails payment = (payments != null && !payments.isEmpty())
                    ? payments.stream()
                            .max(Comparator.comparing(PaymentDetails::getPaymentDate,
                                    Comparator.nullsFirst(Comparator.naturalOrder())))
                            .orElse(null)
                    : null;

            // Booking: reverse lookup (guestDetailsId is a plain field on Booking, no JPA relation)
            // Returned as a List defensively — a guest may have zero or, in edge cases, more than one
            // Booking row, and we must not blow up the whole search when that happens.
            List<Booking> bookings = bookingRepository.findByGuestDetailsId(g.getId());
            Booking booking = (bookings != null && !bookings.isEmpty())
                    ? bookings.stream()
                            .max(Comparator.comparing(Booking::getCheckIn,
                                    Comparator.nullsFirst(Comparator.naturalOrder())))
                            .orElse(null)
                    : null;

            String fName   = personal != null ? personal.getFirstName()       : null;
            String lName   = personal != null ? personal.getLastName()        : null;
            String docNum  = document != null ? document.getDocumentNumber()  : null;
            String rName   = room     != null ? room.getRoomName()            : null;
            Double rental  = rent     != null ? rent.getTotalRental()         : null;

            String paymentTypeName = (payment != null && payment.getPaymentType() != null)
                    ? payment.getPaymentType().getPaymentTypeName() : null;
            String transactionNo   = payment != null ? payment.getTransactionId() : null;
            Double amountPaid      = rent != null ? rent.getPayments() : null;
            Double amountRemain   = rent != null ? rent.getBalance() : null;

            String documentId    = document != null && document.getId() != null
                                        ? String.valueOf(document.getId()) : null;

//            String documentId    = document != null ? document.getDocumentNumber() : null;1

            
            
            String bookingType   = booking != null ? booking.getSource()       : null;
            String bookingRefNo  = booking != null ? booking.getBookingRefNo() : null;

         // Guest Name Search (First Name + Last Name)
            if (request.getGuestName() != null && !request.getGuestName().trim().isEmpty()) {

                String searchText = request.getGuestName().trim().toLowerCase();

                String fullName = ((fName != null ? fName : "") + " " +
                                   (lName != null ? lName : ""))
                                   .trim()
                                   .toLowerCase();

                boolean matched =
                        fullName.contains(searchText) ||
                        (fName != null && fName.toLowerCase().contains(searchText)) ||
                        (lName != null && lName.toLowerCase().contains(searchText));

                if (!matched) {
                    return null;
                }
            }

            if (request.getDocumentNumber() != null && (docNum == null ||
                    !docNum.toLowerCase().contains(request.getDocumentNumber().toLowerCase()))) return null;

            if (request.getRoomName() != null && (rName == null ||
                    !rName.toLowerCase().contains(request.getRoomName().toLowerCase()))) return null;

            if (request.getTotalRental() != null && (rental == null ||
                    !rental.equals(request.getTotalRental()))) return null;

            return new GuestSearchResponseDTO(
                    g.getId(),
                    fName,
                    lName,
                    docNum,
                    rName,
                    g.getCheckInDate(),
                    g.getCheckOutDate(),
                    rental,
                    g.getHotelId(),
                    paymentTypeName,
                    transactionNo,
                    amountRemain,
                    amountPaid,
                    documentId,
                    bookingType,
                    bookingRefNo
            );

        })
        .filter(dto -> dto != null)
        .collect(Collectors.toList());
    }
    
    
    //DOCUMENT CATEGORY FROM DOCUMENT_TYPE TABLE ADDED
    /*@Override
    public List<GuestSearchResponseDTO> searchGuests(GuestSearchRequestDTO request) {

        Specification<GuestDetails> spec = guestSpecification.search(
                request.getCheckInFromDate(),
                request.getCheckInToDate(),
                request.getCheckOutFromDate(),
                request.getCheckOutToDate(),
                request.getHotelId()
        );

        List<GuestDetails> guests = repository.findAll(spec);

        return guests.stream().map(g -> {

            PersonalDetails personal = (g.getPersonalDetailsId() != null)
                    ? personalDetailsRepository.findById(g.getPersonalDetailsId().longValue()).orElse(null)
                    : null;

            DocumentDetails document = (g.getDocumentDetailsId() != null)
                    ? documentDetailsRepository.findById(g.getDocumentDetailsId().longValue()).orElse(null)
                    : null;

            // NEW: resolve the document's category (Credit Card / Debit Card / Passport / etc.)
            DocumentType documentType = (document != null && document.getDocumentType().getId() != null)
                    ? documentTypeRepository.findById( document.getDocumentType().getId().longValue()).orElse(null)
                    : null;
            String documentTypeCategory = documentType != null ? documentType.getDocumentTypeCategory() : null;

            StayDetails stay = (g.getStayDetailsId() != null)
                    ? stayDetailsRepository.findById(g.getStayDetailsId().longValue()).orElse(null)
                    : null;

            RoomMaster room = (stay != null && stay.getRoomMasterId() != null)
                    ? roomMasterRepository.findById(stay.getRoomMasterId().longValue()).orElse(null)
                    : null;

            RentDetails rent = (g.getRentDetailsId() != null)
                    ? rentDetailsRepository.findById(g.getRentDetailsId().longValue()).orElse(null)
                    : null;

            List<PaymentDetails> payments = paymentDetailsRepository.findByGuestDetailsId(g.getId());
            PaymentDetails payment = (payments != null && !payments.isEmpty())
                    ? payments.stream()
                            .max(Comparator.comparing(PaymentDetails::getPaymentDate,
                                    Comparator.nullsFirst(Comparator.naturalOrder())))
                            .orElse(null)
                    : null;

            List<Booking> bookings = bookingRepository.findByGuestDetailsId(g.getId());
            Booking booking = (bookings != null && !bookings.isEmpty())
                    ? bookings.stream()
                            .max(Comparator.comparing(Booking::getCheckIn,
                                    Comparator.nullsFirst(Comparator.naturalOrder())))
                            .orElse(null)
                    : null;

            String fName   = personal != null ? personal.getFirstName()       : null;
            String lName   = personal != null ? personal.getLastName()        : null;
            String docNum  = document != null ? document.getDocumentNumber()  : null;
            String rName   = room     != null ? room.getRoomName()            : null;
            Double rental  = rent     != null ? rent.getTotalRental()         : null;

            String paymentTypeName = (payment != null && payment.getPaymentType() != null)
                    ? payment.getPaymentType().getPaymentTypeName() : null;
            String transactionNo   = payment != null ? payment.getTransactionId() : null;
            Double amountPaid      = payment != null ? payment.getAmount()        : null;
            Double amountRemain    = (payment != null
                                        && payment.getTotalAmount() != null
                                        && payment.getAmount() != null)
                    ? payment.getTotalAmount() - payment.getAmount() : null;

            String documentId    = document != null && document.getId() != null
                                        ? String.valueOf(document.getId()) : null;

            String bookingType   = booking != null ? booking.getSource()       : null;
            String bookingRefNo  = booking != null ? booking.getBookingRefNo() : null;

            // Guest Name Search (First Name + Last Name)
            if (request.getGuestName() != null && !request.getGuestName().trim().isEmpty()) {

                String searchText = request.getGuestName().trim().toLowerCase();

                String fullName = ((fName != null ? fName : "") + " " +
                                   (lName != null ? lName : ""))
                                   .trim()
                                   .toLowerCase();

                boolean matched =
                        fullName.contains(searchText) ||
                        (fName != null && fName.toLowerCase().contains(searchText)) ||
                        (lName != null && lName.toLowerCase().contains(searchText));

                if (!matched) {
                    return null;
                }
            }

            if (request.getDocumentNumber() != null && (docNum == null ||
                    !docNum.toLowerCase().contains(request.getDocumentNumber().toLowerCase()))) return null;

            if (request.getRoomName() != null && (rName == null ||
                    !rName.toLowerCase().contains(request.getRoomName().toLowerCase()))) return null;

            if (request.getTotalRental() != null && (rental == null ||
                    !rental.equals(request.getTotalRental()))) return null;

            return new GuestSearchResponseDTO(
                    g.getId(),
                    fName,
                    lName,
                    docNum,
                    rName,
                    g.getCheckInDate(),
                    g.getCheckOutDate(),
                    rental,
                    g.getHotelId(),
                    paymentTypeName,
                    transactionNo,
                    amountRemain,
                    amountPaid,
                    documentId,
                    bookingType,
                    bookingRefNo,
                    documentTypeCategory
            );

        })
        .filter(dto -> dto != null)
        .collect(Collectors.toList());
    }*/
}
    

