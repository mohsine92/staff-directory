package com.exemple.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.exemple.HibernateUtils;
import com.exemple.models.Services;

public class ServiceRepo {

    // CRUD for Services
    public static void save(Services service) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(service);
            tx.commit();
        }
    }

    public static Services findById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Services.class, id);
        }
    }

    public static List<Services> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Services", Services.class).list();
        }
    }

    public static Services update(Services service) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Services serviceManaged = session.merge(service);
            tx.commit();
            return serviceManaged;
        }
    }

    public static void deleteById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Services service = session.get(Services.class, id);
            if (service != null) {
                session.remove(service);
            }
            tx.commit();
        }
    }
}
