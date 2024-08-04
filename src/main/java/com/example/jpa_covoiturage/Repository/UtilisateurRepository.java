package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.JPAUtil;


import com.example.jpa_covoiturage.Model.Utilisateur;
import com.example.jpa_covoiturage.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UtilisateurRepository {
    private final EntityManagerFactory entityManagerFactory;

    public UtilisateurRepository() {
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }

    public void addUser(Utilisateur utilisateur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(utilisateur);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void deleteUser(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Utilisateur utilisateur = entityManager.find(Utilisateur.class, id);
            if (utilisateur != null) {
                entityManager.remove(utilisateur);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<Utilisateur> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Utilisateur> query = entityManager.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Utilisateur> searchUsers(String searchTerm) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT u FROM Utilisateur u WHERE LOWER(u.nom) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(u.prenom) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(u.email) LIKE LOWER(:searchTerm)";
            TypedQuery<Utilisateur> query = entityManager.createQuery(jpql, Utilisateur.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Utilisateur findByEmail(String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT u FROM Utilisateur u WHERE u.email = :email";
            TypedQuery<Utilisateur> query = entityManager.createQuery(jpql, Utilisateur.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void save(Utilisateur utilisateur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (utilisateur.getId() == null) {
                entityManager.persist(utilisateur);
            } else {
                entityManager.merge(utilisateur);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}