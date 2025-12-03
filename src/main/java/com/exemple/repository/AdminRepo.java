package com.exemple.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.exemple.HibernateUtils;
import com.exemple.models.Admin;

import java.util.List;



public class AdminRepo {

    // CRUD for Admin
    public static void save(Admin admin) {

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(admin);
            tx.commit();
        }
    }

    public static Admin findById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Admin.class, id);
        }
    }

    public static List<Admin> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Admin", Admin.class).list();
        }
    }

    public Admin update(Admin admin) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Admin clientManaged = (Admin) session.merge(admin);
            tx.commit();
            return clientManaged;
        }
    }

    public static void deleteById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Admin admin = session.get(Admin.class, id);
            if (admin != null) {
                session.remove(admin);
            }
            tx.commit();
        }
    }
}
