package com.alganiug.systems.loanManagement.core.services.notification;
public interface EmailService {
    boolean isConfigured();
    void send(String recipient, String subject, String body);
}