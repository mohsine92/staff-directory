package com.exemple.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.exemple.models.Admin;
import com.exemple.repository.AdminRepo;

public class AdminService {

    // CRUD Operations
    public static void createAdmin(String firstName, String lastName, String phone, 
                                  String cell, String email, String password) {
        String passwordHash = hashPassword(password);
        Admin admin = new Admin(firstName, lastName, phone, cell, email, passwordHash);
        AdminRepo.save(admin);
    }

    public static Admin getAdminById(Integer id) {
        return AdminRepo.findById(id);
    }

    public static List<Admin> getAllAdmins() {
        return AdminRepo.findAll();
    }

    public static void updateAdmin(Admin admin) {
        AdminRepo.update(admin);
    }

    public static void deleteAdmin(Integer id) {
        AdminRepo.deleteById(id);
    }

    // Authentification
    public static Admin authenticate(String email, String password) {
        List<Admin> admins = getAllAdmins();
        String passwordHash = hashPassword(password);
        
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPASSWORD_HASH().equals(passwordHash)) {
                return admin;
            }
        }
        return null; // Authentification échouée
    }

    // Hash du mot de passe (SHA-256)
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }
}

