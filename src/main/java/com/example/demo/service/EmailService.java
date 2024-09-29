package com.example.demo.service;

import com.example.demo.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(EmailDetail emailDetail) {
        try {
            // Prepare the email template
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getEmail());
            context.setVariable("button", "Go to home page");
            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("welcome-template", context);

            // Create a MimeMessage for HTML content
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            // Set email details
            mimeMessageHelper.setFrom("nghialncse170125@fpt.edu.vn");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mimeMessageHelper.setText(template, true);

            // Send the email
            javaMailSender.send(mimeMessage);

        } catch (MailAuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Messaging error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
        }
    }
}
