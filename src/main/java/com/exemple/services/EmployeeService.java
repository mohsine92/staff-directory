package com.exemple.services;

import java.util.List;

import com.exemple.models.Employee;
import com.exemple.models.Services;
import com.exemple.models.Sites;
import com.exemple.repository.EmployeeRepository;

public class EmployeeService {

    // CRUD Operations
    public static void createEmployee(String firstName, String lastName, String phone, 
                                     String cell, String email, Services service, Sites site) {
        // Créer un employé sans mot de passe (pour les visiteurs, pas d'authentification)
        Employee employee = new Employee(firstName, lastName, phone, cell, email, "", service, site);
        EmployeeRepository.save(employee);
    }

    public static Employee getEmployeeById(Integer id) {
        return EmployeeRepository.findById(id);
    }

    public static List<Employee> getAllEmployees() {
        return EmployeeRepository.findAll();
    }

    public static void updateEmployee(Employee employee) {
        EmployeeRepository.update(employee);
    }

    public static void deleteEmployee(Integer id) {
        EmployeeRepository.deleteById(id);
    }

    // Recherche Operations
    public static List<Employee> searchEmployeesByName(String namePattern) {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return getAllEmployees();
        }
        return EmployeeRepository.searchByName(namePattern);
    }

    public static List<Employee> searchEmployeesBySite(Sites site) {
        if (site == null) {
            return getAllEmployees();
        }
        return EmployeeRepository.findBySite(site);
    }

    public static List<Employee> searchEmployeesByService(Services service) {
        if (service == null) {
            return getAllEmployees();
        }
        return EmployeeRepository.findByService(service);
    }

    public static List<Employee> searchEmployees(String namePattern, Sites site, Services service) {
        return EmployeeRepository.search(namePattern, site, service);
    }
}