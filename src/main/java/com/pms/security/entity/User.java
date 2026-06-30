/**
 * 
 */
package com.pms.security.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
	    name = "users",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "username")
	    }
	)
public class User {

    static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private Boolean enabled;

//    @Column(unique = true)
    @Column(name="email")
    private String email;
    
    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;
    
    @Column(name = "deleted_by")
    private Long deletedBy;
    
    @Column(name = "hotel_id")
    private Long hotelId; 


    // ✅ Multiple roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore   // or keep ManagedReference if properly paired
    private Set<UserHotelMapping> mappings = new HashSet<>();
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @Column(name = "approval_pin")
    private String approvalPin;
    
//    @Lob // Large Object - maps to TEXT in PostgreSQL for String
    @Column(columnDefinition = "TEXT",name = "user_permission") // Explicitly tell Hibernate to use TEXT
    private String userPermissionArray;
    
//    private Set<UserPermissionArrayDTO> userPermission = new HashSet<>();
    
  
    
 // Getters & Setters
    
    public void addMapping(UserHotelMapping mapping) {
        mappings.add(mapping);
        mapping.setUser(this);
    }

    public void removeMapping(UserHotelMapping mapping) {
        mappings.remove(mapping);
        mapping.setUser(null);
    }
    
 // ✅ ADD THIS METHOD HERE
    public boolean hasRole(String roleName) {
        return this.roles != null && this.roles.stream()
                .anyMatch(role -> roleName.equals(role.getName()));
    }

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserPermission> userPermissions = new HashSet<>();

}