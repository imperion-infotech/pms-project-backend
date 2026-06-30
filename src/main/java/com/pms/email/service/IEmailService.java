/**
 * 
 */
package com.pms.email.service;

import java.io.File;
import java.util.List;

/**
 * 
 */
public interface IEmailService {

    void sendEmail(
            String to,
            List<String> cc,
            List<String> bcc,
            String subject,
            String text,
            List<File> attachments);
}
