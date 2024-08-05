package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UserRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PERSISTENCE");
    private final EntityManagerFactory factory;

    public UserRepository(EntityManagerFactory factory) {
        this.factory = factory;
    }


    public static UserRepository create() {
        return new UserRepository(emf);
    }

    public void save(User user) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public User findById(Long id) {
        EntityManager em = factory.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = factory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<User> findByRole(User.Role role) {
        EntityManager em = factory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.role = :role", User.class);
            query.setParameter("role", role);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<User> searchUsers(String searchTerm) {
        EntityManager em = factory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.nom) LIKE LOWER(:term) OR LOWER(u.prenom) LIKE LOWER(:term) OR LOWER(u.email) LIKE LOWER(:term)",
                    User.class
            );
            query.setParameter("term", "%" + searchTerm + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void close() {
        emf.close();
    }
}
