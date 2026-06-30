/**
 * 
 */
package com.pms.email.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pms.email.service.IEmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

/**
 * 
 */
@Service
@Profile("prod") 
public class SendGridEmailServiceImpl implements IEmailService {
	
	public static final Logger logger = LoggerFactory.getLogger(SendGridEmailServiceImpl.class);
	
	@Autowired
	private  JavaMailSender mailSender;
	
	public void sendEmail(
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
//    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
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
}

}
