package com.example.jpa_covoiturage.Repository;

import com.example.jpa_covoiturage.Model.Vehicule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class VehiculeRepository {

    private static EntityManagerFactory emf;

    public VehiculeRepository() {
        if (emf == null) {
            synchronized (VehiculeRepository.class) {
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory("PERSISTENCE");
                }
            }
        }
    }

    public void addVehicule(Vehicule vehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehicule);
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

    public void updateVehicule(Vehicule vehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehicule);
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

    public void removeVehicule(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Vehicule vehicule = em.find(Vehicule.class, id);
            if (vehicule != null) {
                em.remove(vehicule);
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

    public Vehicule getVehiculeById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Vehicule.class, id);
        } finally {
            em.close();
        }
    }

    public List<Vehicule> getAllVehicules() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Vehicule> query = em.createQuery("SELECT v FROM Vehicule v", Vehicule.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vehicule> findVehiculesByMarqueOrModele(String recherche) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Vehicule> query = em.createQuery(
                    "SELECT v FROM Vehicule v WHERE LOWER(v.marque) LIKE LOWER(:recherche) OR LOWER(v.modele) LIKE LOWER(:recherche)", Vehicule.class);
            query.setParameter("recherche", "%" + recherche + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
