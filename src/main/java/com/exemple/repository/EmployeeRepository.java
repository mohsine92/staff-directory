package com.exemple.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.exemple.HibernateUtils;
import com.exemple.models.Employee;
import com.exemple.models.Services;
import com.exemple.models.Sites;

public class EmployeeRepository {

    // CRUD for Employee
    public static void save(Employee employee) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        }
    }

    public static Employee findById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public static List<Employee> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class).list();
        }
    }

    public static Employee update(Employee employee) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employeeManaged = session.merge(employee);
            tx.commit();
            return employeeManaged;
        }
    }

    public static void deleteById(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
            }
            tx.commit();
        }
    }

    // Méthodes de recherche
    // Recherche par nom (FirstName et LastName)
    public static List<Employee> searchByName(String namePattern) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String query = "from Employee e where lower(e.FirstName) like lower(:pattern) " +
                          "or lower(e.LastName) like lower(:pattern)";
            return session.createQuery(query, Employee.class)
                    .setParameter("pattern", "%" + namePattern + "%")
                    .list();
        }
    }

    // Recherche par site
    public static List<Employee> findBySite(Sites site) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String query = "from Employee e where e.site = :site";
            return session.createQuery(query, Employee.class)
                    .setParameter("site", site)
                    .list();
        }
    }

    // Recherche par service
    public static List<Employee> findByService(Services service) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String query = "from Employee e where e.service = :service";
            return session.createQuery(query, Employee.class)
                    .setParameter("service", service)
                    .list();
        }
    }

    // Recherche (nom + site + service)
    public static List<Employee> search(String namePattern, Sites site, Services service) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            StringBuilder queryBuilder = new StringBuilder("from Employee e where 1=1");
            
            if (namePattern != null && !namePattern.trim().isEmpty()) {
                queryBuilder.append(" and (lower(e.FirstName) like lower(:pattern) " +
                                   "or lower(e.LastName) like lower(:pattern))");
            }
            if (site != null) {
                queryBuilder.append(" and e.site = :site");
            }
            if (service != null) {
                queryBuilder.append(" and e.service = :service");
            }
            
            var query = session.createQuery(queryBuilder.toString(), Employee.class);
            
            if (namePattern != null && !namePattern.trim().isEmpty()) {
                query.setParameter("pattern", "%" + namePattern + "%");
            }
            if (site != null) {
                query.setParameter("site", site);
            }
            if (service != null) {
                query.setParameter("service", service);
            }
            
            return query.list();
        }
    }
}   

