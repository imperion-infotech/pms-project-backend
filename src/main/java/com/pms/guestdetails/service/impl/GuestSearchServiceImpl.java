/**
 * 
 */
package com.pms.guestdetails.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.document.dao.DocumentDetailsRepository;
import com.pms.document.entity.DocumentDetails;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.guestdetails.dto.GuestSearchRequestDTO;
import com.pms.guestdetails.dto.GuestSearchResponseDTO;
import com.pms.guestdetails.service.IGuestSearchService;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.impl.RentDetailsRepository;
import com.pms.room.dao.impl.RoomMasterRepository;
import com.pms.room.entity.RoomMaster;
import com.pms.search.specification.GuestSpecification;
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
    private StayDetailsRepository stayDetailsRepository;
    @Autowired
    private RoomMasterRepository roomMasterRepository;
    @Autowired
    private RentDetailsRepository rentDetailsRepository;
    
    
    
    

    @Override
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
    }



	


}