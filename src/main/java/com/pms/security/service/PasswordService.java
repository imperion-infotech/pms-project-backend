/**
 * 
 */
package com.pms.security.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pms.auditlog.util.AuditUtil;
import com.pms.exception.ResourceNotFoundException;
import com.pms.security.entity.PasswordResetToken;
import com.pms.security.entity.Role;
import com.pms.security.entity.User;
import com.pms.security.repository.PasswordResetTokenRepository;
import com.pms.security.repository.UserRepository;

/**
 * 
 */
@Service
public class PasswordService {
	
	static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    @Autowired 
    private UserRepository userRepo;
    @Autowired 
    private PasswordResetTokenRepository tokenRepo;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Environment environment;
    
    @Autowired
    private AuditUtil auditUtil;
    
    public PasswordService(UserRepository userRepo, PasswordResetTokenRepository tokenRepo, JavaMailSender mailSender,
			PasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
//		this.mailSender = mailSender;
		this.passwordEncoder = passwordEncoder;
	}

	public void createPasswordResetToken(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(60));
        tokenRepo.save(resetToken);
        
        String port = environment.getProperty("server.port");
        String ip=  environment.getProperty("password_reset_ip"); 
        
        
        StringBuilder resetLink=new StringBuilder("http://"+ip+":"+port);
        resetLink = new StringBuilder();
        
        resetLink.append(environment.getProperty("password_reset_url"));
        
        logger.info("resetLink::"+resetLink);

        StringBuilder resetFullLink = resetLink.append("?token=" + token);
        auditUtil.sendEmail(email, "Password Reset", "Click here to reset: " + resetFullLink);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Token expired");
        }

        User user = userRepo.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

//        user.setPassword(passwordEncoder.encode(newPassword));
        
        Set<Role> roles =  user.getRoles();
        
        boolean isSuperAdmin = roles.stream()
                .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getName()));
        
        boolean isHotelOwner = roles.stream()
                .anyMatch(role -> "ROLE_HOTEL_OWNER".equals(role.getName()));
        
       if(isSuperAdmin || isHotelOwner)
       {
    	   user.setPassword(passwordEncoder.encode(newPassword));
       } else
       {
    	   user.setPassword(newPassword);
       }
        
        userRepo.save(user);
        tokenRepo.delete(resetToken);
    }

}

