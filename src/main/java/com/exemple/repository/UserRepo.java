package com.exemple.repository;


import org.hibernate.Session;
import org.hibernate.Transaction;

import com.exemple.HibernateUtils;
import com.exemple.models.User;

import java.util.List;
import java.util.Optional;

public class UserRepo {

    public static void save(User user){

        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        }
    }

    public static User findById(Integer id){
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public static List<User> findAll(){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from User", User.class).list();
        }
    }

    public User update(User user){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            User userManaged = (User) session.merge(user);
            tx.commit();
            return userManaged;
        }
    }

    public void deleteById(Integer id){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if(user != null){
                session.remove(user);
            }
            tx.commit();
        }
    }
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            User user = session.createQuery("from User where USERNAME = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }
}
