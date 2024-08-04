package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.Model.Voiture;
import com.example.jpa_covoiturage.Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VoitureRepository {
    private final EntityManagerFactory emf;

    public VoitureRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Méthodes save, delete, findById, findAll restent les mêmes

    public List<Voiture> findByMarqueOrModeleOrImmatriculation(String searchTerm) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voiture> query = em.createQuery(
                    "SELECT v FROM Voiture v WHERE LOWER(v.marque) LIKE LOWER(:term) OR LOWER(v.modele) LIKE LOWER(:term) OR LOWER(v.immatriculation) LIKE LOWER(:term)",
                    Voiture.class
            );
            query.setParameter("term", "%" + searchTerm + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Voiture> findByConducteur(User conducteur) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voiture> query = em.createQuery(
                    "SELECT v FROM Voiture v WHERE v.conducteur = :conducteur",
                    Voiture.class
            );
            query.setParameter("conducteur", conducteur);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Voiture> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Voiture> query = em.createQuery("SELECT v FROM Voiture v", Voiture.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Voiture newVoiture) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (newVoiture.getId() == null) {
                em.persist(newVoiture);
            } else {
                em.merge(newVoiture);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Voiture voiture = em.find(Voiture.class, id);
            if (voiture != null) {
                em.remove(voiture);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }



}