package com.exemple.services;

import java.util.List;

import com.exemple.models.Services;
import com.exemple.repository.ServiceRepo;

public class ServiceService {

    // CRUD Operations
    public static void createService(String name) {
        Services service = new Services(name);
        ServiceRepo.save(service);
    }

    public static Services getServiceById(Integer id) {
        return ServiceRepo.findById(id);
    }

    public static List<Services> getAllServices() {
        return ServiceRepo.findAll();
    }

    public static void updateService(Services service) {
        ServiceRepo.update(service);
    }

    public static void deleteService(Integer id) {
        ServiceRepo.deleteById(id);
    }
}

