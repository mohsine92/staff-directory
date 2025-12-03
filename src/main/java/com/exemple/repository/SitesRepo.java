package com.exemple.repository;

import java.util.List;

import org.hibernate.Session;

import com.exemple.HibernateUtils;
import com.exemple.models.Sites;


public class SitesRepo {

    // CRUD for Sites
     public static void save(Sites sites) {

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.persist(sites);
            tx.commit();
        }
    }

    public static Sites findById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Sites.class, id);
        }
    }

    public static List<Sites> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Sites", Sites.class).list();
        }
    }

    public static Sites update(Sites sites) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            Sites sitesManaged = session.merge(sites);
            tx.commit();
            return sitesManaged;
        }
    }

    public static void deleteById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            Sites sites = session.get(Sites.class, id);
            if (sites != null) {
                session.remove(sites);
            }
            tx.commit();
        }
    }
}
