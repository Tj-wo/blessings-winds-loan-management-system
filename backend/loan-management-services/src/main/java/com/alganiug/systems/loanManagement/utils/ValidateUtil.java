package com.alganiug.systems.loanManagement.utils;

import com.alganiug.systems.loanManagement.core.services.ServiceValidationException;
import java.math.BigDecimal;

public final class ValidateUtil {

    private ValidateUtil() {
    }

    public static void notNull(Object value, String fieldName) {
        if (value == null) {
            throw new ServiceValidationException(fieldName + " is required");
        }
    }

    public static void notBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ServiceValidationException(fieldName + " is required");
        }
    }

    public static void positive(BigDecimal value, String fieldName) {
        notNull(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceValidationException(fieldName + " must be greater than zero");
        }
    }

    public static void nonNegative(BigDecimal value, String fieldName) {
        notNull(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceValidationException(fieldName + " cannot be negative");
        }
    }

    public static void state(boolean valid, String message) {
        if (!valid) {
            throw new ServiceValidationException(message);
        }
    }
}
