package com.pms.auditlog.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pms.security.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class AuditUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditUtil.class);
	
	@Autowired
	 private JavaMailSender mailSender;   // no @Autowired here
	 
	
	private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
        	  e.printStackTrace(); //
        	  return "{}";
        }
    }

    public static String getUsername() {
    	
    	User user =(User) AuditUtil.getSessionAttribute("user");
    	if(user != null) {
    	return user.getUsername();
    	} else
    	{
    		return "";
    	}
    
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }
    
 // Get session attribute by name
    public static Object getSessionAttribute(String name) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession(false); // false = don't create new session
            if (session != null) {
                return session.getAttribute(name);
            }
        }
        return null;
    }
    
 // Remove session attribute by name
    public static void removeSessionAttribute(String name) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession(false); // false = don't create new session
            if (session != null) {
                session.removeAttribute(name);
            }
        }
    }
    
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}