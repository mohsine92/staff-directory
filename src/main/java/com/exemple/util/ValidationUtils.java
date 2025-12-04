package com.exemple.util;

import java.util.regex.Pattern;

/**
 * Utilitaires de validation
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );

    /**
     * Valide un email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valide un numéro de téléphone (10 chiffres)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Nettoyer le numéro (enlever espaces, tirets, etc.)
        String cleaned = phone.replaceAll("[^0-9]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    /**
     * Nettoie un numéro de téléphone (garde uniquement les chiffres)
     */
    public static String cleanPhoneNumber(String phone) {
        if (phone == null) {
            return "";
        }
        return phone.replaceAll("[^0-9]", "");
    }

    /**
     * Valide qu'une chaîne n'est pas vide
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}

