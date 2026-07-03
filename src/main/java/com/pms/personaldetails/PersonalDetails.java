/**
 * 
 */
package com.pms.personaldetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pms.baseentity.BaseEntity;
import com.pms.document.entity.DocumentDetails;
import com.pms.personaldetails.controller.ContactInformationTypeEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="personal_details")
@SQLRestriction("is_deleted = false")
public class PersonalDetails extends BaseEntity implements Serializable {
	
static final Logger logger = LoggerFactory.getLogger(PersonalDetails.class);
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "firstname is required")
    @Column(nullable = false)
    private String firstName;
    
    @NotBlank(message = "lastname is required")
    @Column(nullable = false)
    private String lastName;
    
//    @NotBlank(message = "company name is required")
     private String companyName;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;
  
    @Email(message = "Invalid email format")
//    @Column(unique = false, nullable = false)
    private String email;
    
    private String address;

    @Column(unique = true, nullable = true)
    private String profilePhoto;
    
    @Column(unique = true, nullable = true)
    private String signature;
    
//    // ✅ Correct OneToOne (inverse side)
//    @OneToOne(mappedBy = "personalDetails", cascade = CascadeType.ALL)
//    private GuestDetails guest;

    // ✅ NEW: One person → many documents
    @OneToMany(mappedBy = "personalDetails", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DocumentDetails> documents;
    
    private String folioNo;
    
    private String crsFolioNo;
    
    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_information_type_enum")
    private ContactInformationTypeEnum contactInformationTypeEnum =ContactInformationTypeEnum.HOME ;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "nationality")
    private String nationality;
    
    
    
}