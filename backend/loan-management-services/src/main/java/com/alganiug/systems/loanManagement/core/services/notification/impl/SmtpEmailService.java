package com.alganiug.systems.loanManagement.core.services.notification.impl;

import com.alganiug.systems.loanManagement.core.services.ServiceOperationException;
import com.alganiug.systems.loanManagement.core.services.notification.EmailService;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SmtpEmailService implements EmailService {
    private static final String DEFAULT_SENDER = "systems@alganiug.com";
    public boolean isConfigured() { return value("SMTP_HOST") != null && value("SMTP_USERNAME") != null && value("SMTP_PASSWORD") != null; }
    public void send(String recipient, String subject, String body) {
        if (!isConfigured()) throw new ServiceOperationException("Email is not configured. Set SMTP_HOST, SMTP_USERNAME and SMTP_PASSWORD.");
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", value("SMTP_HOST"));
            properties.put("mail.smtp.port", defaultValue("SMTP_PORT", "587"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", defaultValue("SMTP_STARTTLS", "true"));
            properties.put("mail.smtp.ssl.enable", defaultValue("SMTP_SSL", "false"));
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(value("SMTP_USERNAME"), value("SMTP_PASSWORD")); }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(defaultValue("SMTP_FROM", DEFAULT_SENDER), "Blessed Winds Loans"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            Transport.send(message);
        } catch (Exception exception) { throw new ServiceOperationException("Email could not be sent: " + exception.getMessage()); }
    }
    private String defaultValue(String name, String fallback) { String configured=value(name); return configured == null ? fallback : configured; }
    private String value(String name) { String value=System.getenv(name); return value == null || value.trim().isEmpty() ? null : value.trim(); }
}