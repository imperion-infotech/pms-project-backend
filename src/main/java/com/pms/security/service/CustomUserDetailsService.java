package com.pms.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pms.security.entity.User;
import com.pms.security.repository.PermissionRepository;
import com.pms.security.repository.UserRepository;
import com.pms.security.util.PermissionResolver;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
    private UserRepository userRepository;
    private final Map<String, UserDetails> users = new HashMap<>();
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private PermissionResolver permissionResolver;
    
 
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
		// Admin user
        users.put("admin", org.springframework.security.core.userdetails.User.withUsername("admin")
                .password("{noop}password") // {noop} means no encoding (for demo only)
                .roles("ADMIN") // becomes ROLE_ADMIN internally
                .build());

        // Normal user
        users.put("user", org.springframework.security.core.userdetails.User.withUsername("user")
                .password("{noop}12345")
                .roles("USER") // becomes ROLE_USER internally
                .build());
    }

    

    
   /* @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsernameWithRolesAndIsDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("DEBUG ROLES: " + user.getRoles()); // 🔥 check

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }*/
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"));

        Set<GrantedAuthority> authorities =
                permissionResolver
                        .resolveAuthorities(user)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        logger.info(
                "User [{}] loaded with authorities : {}",
                username,
                authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    
}
