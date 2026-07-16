package com.alganiug.systems.loanManagement.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class GeneralSearchUtil {

    private GeneralSearchUtil() {
    }

    public static String normalize(String searchTerm) {
        return searchTerm == null ? "" : searchTerm.trim().toLowerCase(Locale.ROOT);
    }

    public static String toLikePattern(String searchTerm) {
        String normalized = normalize(searchTerm).replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + normalized + "%";
    }

    public static List<String> tokenize(String searchTerm) {
        String normalized = normalize(searchTerm);
        if (normalized.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(normalized.split("\\s+")).filter(token -> !token.isEmpty()).collect(Collectors.toList());
    }
}
