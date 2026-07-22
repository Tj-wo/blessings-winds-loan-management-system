package com.alganiug.systems.loanManagement.views.dialogs;

import java.util.Locale;

public final class UserMessageResolver {
    private UserMessageResolver() { }

    public static String resolve(Throwable throwable) {
        Throwable current = throwable;
        String fallback = null;
        while (current != null) {
            String message = current.getMessage();
            String type = current.getClass().getName();
            if (type.endsWith("ServiceValidationException") || type.endsWith("ServiceOperationException")) {
                return useful(message, "The requested operation is not allowed.");
            }
            String text = message == null ? "" : message.toLowerCase(Locale.ROOT);
            if (text.contains("duplicate entry") || text.contains("unique constraint")
                    || text.contains("constraint [uk_") || text.contains("constraint [uc_")) {
                return "An active record with the same name or reference already exists. Please use a different value.";
            }
            if (text.contains("foreign key constraint") || text.contains("referential integrity")) {
                return "This record is already in use and cannot be deleted.";
            }
            if (text.contains("cannot be null") || text.contains("not-null property")
                    || text.contains("null value was assigned")) {
                return "Please complete all required fields before saving.";
            }
            if (type.contains("DateTimeParseException") || text.contains("could not be parsed at index")) {
                return "Enter the date or payroll period in the required format.";
            }
            if (type.contains("NumberFormatException")) {
                return "Enter a valid number.";
            }
            if (type.contains("LazyInitializationException")) {
                return "Some information could not be loaded. Refresh the page and try again.";
            }
            if (text.contains("max_allowed_packet") || text.contains("query is too large")) {
                return "The selected file is too large to upload. Choose a smaller file and try again.";
            }
            if (type.contains("OptimisticLock") || type.contains("StaleObjectState")) {
                return "This record was changed by another user. Refresh the page and try again.";
            }
            if (message != null && !message.trim().isEmpty()) fallback = message;
            current = current.getCause() == current ? null : current.getCause();
        }
        if (fallback != null && !looksTechnical(fallback)) return fallback;
        return "We could not complete that action. Check the entered information and try again.";
    }

    private static String useful(String message, String fallback) {
        return message == null || message.trim().isEmpty() ? fallback : message;
    }

    private static boolean looksTechnical(String message) {
        String text = message.toLowerCase(Locale.ROOT);
        return text.contains("hibernate") || text.contains("sql") || text.contains("jdbc")
                || text.contains("exception") || text.contains("com.alganiug") || text.contains("proxy");
    }
}