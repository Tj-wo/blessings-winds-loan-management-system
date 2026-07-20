package com.alganiug.systems.loanManagement.views.dialogs;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public final class MessageComposer {

    private MessageComposer() {
    }

    public static void info(String title, String description) {
        addMessage(FacesMessage.SEVERITY_INFO, title, description);
    }

    public static void warn(String title, String description) {
        addMessage(FacesMessage.SEVERITY_WARN, title, description);
    }

    public static void error(String title, String description) {
        addMessage(FacesMessage.SEVERITY_ERROR, title, description);
    }

    private static void addMessage(FacesMessage.Severity severity, String title, String description) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, description));
    }
}