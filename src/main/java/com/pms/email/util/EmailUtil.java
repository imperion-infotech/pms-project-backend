/**
 * 
 */
package com.pms.email.util;


import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	private static JavaMailSender mailSender;

	@Autowired
	public EmailUtil(JavaMailSender mailSender) {
		EmailUtil.mailSender = mailSender;
	}

//	public static void sendEmail(String to, String subject, String text) {
//
//		SimpleMailMessage message = new SimpleMailMessage();
//
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(text);
//
//		mailSender.send(message);
//	}
	
	
	public static void sendEmail(
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
	//Following send email method for render uploaded project about sendgrid//
//	=========================================================================//
	/*public static void sendEmail(
	        String to,
	        List<String> cc,
	        List<String> bcc,
	        String subject,
	        String text,
	        List<File> attachments) {

	    try {
	        // ── TO ──
	        Mail mail = new Mail();
	        mail.setFrom(new Email("imperioninfotech@gmail.com")); // ← your SendGrid verified sender
	        mail.setSubject(subject);

	        Personalization personalization = new Personalization();
	        personalization.addTo(new Email(to));

	        // ── CC ──
	        if (cc != null && !cc.isEmpty()) {
	            for (String ccEmail : cc) {
	                personalization.addCc(new Email(ccEmail));
	            }
	        }

	        // ── BCC ──
	        if (bcc != null && !bcc.isEmpty()) {
	            for (String bccEmail : bcc) {
	                personalization.addBcc(new Email(bccEmail));
	            }
	        }

	        mail.addPersonalization(personalization);

	        // ── BODY ──
	        mail.addContent(new Content("text/html", text));

	        // ── ATTACHMENTS ──
	        if (attachments != null) {
	            for (File file : attachments) {
	                if (file != null && file.exists()) {
	                    byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());
	                    String encoded = java.util.Base64.getEncoder().encodeToString(fileBytes);

	                    Attachments attachment = new Attachments();
	                    attachment.setFilename(file.getName());
	                    attachment.setContent(encoded);
	                    attachment.setDisposition("attachment");
	                    mail.addAttachments(attachment);
	                }
	            }
	        }

	        // ── SEND ──
//	        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	        String apiKey = System.getenv("SENDGRID_API_KEY");
	        
	        logger.info("SendGrid key present: " + (apiKey != null ? "YES" : "NULL"));
	        
	        SendGrid sg = new SendGrid(apiKey);
	        Request request = new Request();
	        request.setMethod(Method.POST);
	        request.setEndpoint("mail/send");
	        request.setBody(mail.build());

	        Response response = sg.api(request);

	        if (response.getStatusCode() >= 400) {
	            throw new RuntimeException("SendGrid error: " + response.getStatusCode() + " " + response.getBody());
	        }

	        logger.info("Email sent successfully to: " + to + " status: " + response.getStatusCode());

	    } catch (IOException e) {
	        throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
	    }
	}*/
	
}