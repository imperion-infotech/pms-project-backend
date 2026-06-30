/**
 * 
 */
package com.pms.email.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.pms.email.service.IEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * 
 */
@Service
@Profile("dev")
public class GmailEmailServiceImpl implements IEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
    @Override
    public void sendEmail(
	        String to,
	        List<String> cc,
	        List<String> bcc,
	        String subject,
	        String text,
	        List<File> attachments) {

	    try {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	        helper.setTo(to);

	        if (cc != null && !cc.isEmpty()) {
	            helper.setCc(cc.toArray(new String[0]));
	        }

	        if (bcc != null && !bcc.isEmpty()) {
	            helper.setBcc(bcc.toArray(new String[0]));
	        }

	        helper.setSubject(subject);
	        helper.setText(text, true);

	        if (attachments != null) {
	            for (File file : attachments) {
	                if (file != null && file.exists()) {
	                    helper.addAttachment(file.getName(), file);
	                }
	            }
	        }

	        mailSender.send(message);

	    } catch (MessagingException e) {
	        throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
	    }
	}
    
}
