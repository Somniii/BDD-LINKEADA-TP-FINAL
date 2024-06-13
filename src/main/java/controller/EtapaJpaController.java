/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import seahub.torneo.logica.Etapa;
import seahub.torneo.logica.Torneo;

/**
 *
 * @author Alumno
 */
public class EtapaJpaController implements Serializable {

    public EtapaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EtapaJpaController(){
        emf =  Persistence.createEntityManagerFactory("torneoBDDJPAPU");
    }
    
    
    private EntityManagerFactory emf = null;
            
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Etapa etapa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Torneo idTorneo = etapa.getIdTorneo();
            if (idTorneo != null) {
                idTorneo = em.getReference(idTorneo.getClass(), idTorneo.getIdTorneo());
                etapa.setIdTorneo(idTorneo);
            }
            em.persist(etapa);
            if (idTorneo != null) {
                idTorneo.getEtapaList().add(etapa);
                idTorneo = em.merge(idTorneo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEtapa(etapa.getIdEtapa()) != null) {
                throw new PreexistingEntityException("Etapa " + etapa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Etapa etapa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Etapa persistentEtapa = em.find(Etapa.class, etapa.getIdEtapa());
            Torneo idTorneoOld = persistentEtapa.getIdTorneo();
            Torneo idTorneoNew = etapa.getIdTorneo();
            if (idTorneoNew != null) {
                idTorneoNew = em.getReference(idTorneoNew.getClass(), idTorneoNew.getIdTorneo());
                etapa.setIdTorneo(idTorneoNew);
            }
            etapa = em.merge(etapa);
            if (idTorneoOld != null && !idTorneoOld.equals(idTorneoNew)) {
                idTorneoOld.getEtapaList().remove(etapa);
                idTorneoOld = em.merge(idTorneoOld);
            }
            if (idTorneoNew != null && !idTorneoNew.equals(idTorneoOld)) {
                idTorneoNew.getEtapaList().add(etapa);
                idTorneoNew = em.merge(idTorneoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = etapa.getIdEtapa();
                if (findEtapa(id) == null) {
                    throw new NonexistentEntityException("The etapa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Etapa etapa;
            try {
                etapa = em.getReference(Etapa.class, id);
                etapa.getIdEtapa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The etapa with id " + id + " no longer exists.", enfe);
            }
            Torneo idTorneo = etapa.getIdTorneo();
            if (idTorneo != null) {
                idTorneo.getEtapaList().remove(etapa);
                idTorneo = em.merge(idTorneo);
            }
            em.remove(etapa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Etapa> findEtapaEntities() {
        return findEtapaEntities(true, -1, -1);
    }

    public List<Etapa> findEtapaEntities(int maxResults, int firstResult) {
        return findEtapaEntities(false, maxResults, firstResult);
    }

    private List<Etapa> findEtapaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Etapa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Etapa findEtapa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Etapa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEtapaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Etapa> rt = cq.from(Etapa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
