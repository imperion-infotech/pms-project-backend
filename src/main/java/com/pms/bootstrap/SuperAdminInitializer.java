/**
 * 
 */
package com.pms.bootstrap;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pms.security.entity.Role;
import com.pms.security.entity.User;
import com.pms.security.repository.RoleRepository;
import com.pms.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Override
    public void run(String... args) {
    	
    	 log.info("=== SuperAdminInitializer Started ===");

        Role superAdminRole = roleRepository
                .findByName("ROLE_SUPER_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_SUPER_ADMIN");
                    return roleRepository.save(role);
                });

        boolean exists = userRepository
                .existsByRoles_NameAndEnabledTrue("ROLE_SUPER_ADMIN");

        log.info("Creating Super Admin User...");
        
        if (!exists) {

            User user = new User();
            user.setUsername("super_admin");
            
//        	String encoded = passwordEncoder.encode("Admin@123");
 	      
 	    	   user.setPassword(bcryptPasswordEncoder.encode("Admin@123"));
            
//            user.setPassword("Admin@123");
            user.setEnabled(true);
//            user.setEmail("test2@gmail.com");

            System.out.println("Role ID = " + superAdminRole.getId());
            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);
            user.setRoles(roles);
            
            superAdminRole = roleRepository.save(superAdminRole);
            
            user.getRoles().add(superAdminRole);
            

            userRepository.save(user);

            log.info("Default Super Admin created successfully");
        }
        log.info("=== SuperAdminInitializer class Ended ===");
    }
}