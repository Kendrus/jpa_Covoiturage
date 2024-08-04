package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.Model.Trajet;
import jakarta.persistence.*;

import java.util.List;

public class TrajetRepository {
    private EntityManagerFactory emf;
    private EntityManager em;

    private TrajetRepository() {
        emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        em = emf.createEntityManager();
    }

    public static TrajetRepository create() {
        return new TrajetRepository();
    }

    public void save(Trajet trajet) {
        executeInTransaction(() -> em.persist(trajet));
    }

    public void update(Trajet trajet) {
        executeInTransaction(() -> em.merge(trajet));
    }

    public void delete(Trajet trajet) {
        executeInTransaction(() -> {
            Trajet toDelete = em.merge(trajet);
            em.remove(toDelete);
        });
    }

    public List<Trajet> getAll() {
        TypedQuery<Trajet> query = em.createQuery("SELECT t FROM Trajet t", Trajet.class);
        return query.getResultList();
    }

    public List<Trajet> searchTrajets(String searchTerm) {
        TypedQuery<Trajet> query = em.createQuery("SELECT t FROM Trajet t WHERE t.lieuDepart LIKE :searchTerm OR t.lieuArrivee LIKE :searchTerm", Trajet.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        return query.getResultList();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    private void executeInTransaction(Runnable action) {
        EntityTransaction transaction = em.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            action.run();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}