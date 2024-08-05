package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.Model.Reservation;
import jakarta.persistence.*;

import java.util.List;

public class ReservationRepository {
    private EntityManagerFactory emf;
    private EntityManager em;

    private ReservationRepository() {
        emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        em = emf.createEntityManager();
    }

    public static ReservationRepository create() {
        return new ReservationRepository();
    }

    public void save(Reservation reservation) {
        executeInTransaction(() -> em.persist(reservation));
    }

    public void update(Reservation reservation) {
        executeInTransaction(() -> em.merge(reservation));
    }

    public void delete(Reservation reservation) {
        executeInTransaction(() -> {
            Reservation toDelete = em.merge(reservation);
            em.remove(toDelete);
        });
    }

    public List<Reservation> getAll() {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r", Reservation.class);
        return query.getResultList();
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r WHERE r.user.id = :userId", Reservation.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Reservation> getReservationsByTrajet(Long trajetId) {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r WHERE r.trajet.id = :trajetId", Reservation.class);
        query.setParameter("trajetId", trajetId);
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