package com.exemple.services;

import java.util.List;

import com.exemple.models.Sites;
import com.exemple.repository.SitesRepo;

public class SiteService {

    // CRUD Operations
    public static void createSite(String city) {
        Sites site = new Sites(city);
        SitesRepo.save(site);
    }

    public static Sites getSiteById(Integer id) {
        return SitesRepo.findById(id);
    }

    public static List<Sites> getAllSites() {
        return SitesRepo.findAll();
    }

    public static void updateSite(Sites site) {
        SitesRepo.update(site);
    }

    public static void deleteSite(Integer id) {
        SitesRepo.deleteById(id);
    }
}

