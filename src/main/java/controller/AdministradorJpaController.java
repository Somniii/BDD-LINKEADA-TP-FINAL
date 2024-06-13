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
import seahub.torneo.logica.Administrador;
import seahub.torneo.logica.Participante;
import seahub.torneo.logica.Torneo;

/**
 *
 * @author Alumno
 */
public class AdministradorJpaController implements Serializable {

    public AdministradorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
   
    public AdministradorJpaController(){
        emf =  Persistence.createEntityManagerFactory("torneoBDDJPAPU");
    }
        
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrador administrador) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participante idParticipante = administrador.getIdParticipante();
            if (idParticipante != null) {
                idParticipante = em.getReference(idParticipante.getClass(), idParticipante.getIdParticipante());
                administrador.setIdParticipante(idParticipante);
            }
            Torneo idTorneo = administrador.getIdTorneo();
            if (idTorneo != null) {
                idTorneo = em.getReference(idTorneo.getClass(), idTorneo.getIdTorneo());
                administrador.setIdTorneo(idTorneo);
            }
            em.persist(administrador);
            if (idParticipante != null) {
                idParticipante.getAdministradorList().add(administrador);
                idParticipante = em.merge(idParticipante);
            }
            if (idTorneo != null) {
                idTorneo.getAdministradorList().add(administrador);
                idTorneo = em.merge(idTorneo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdministrador(administrador.getIdAdministrador()) != null) {
                throw new PreexistingEntityException("Administrador " + administrador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Administrador administrador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrador persistentAdministrador = em.find(Administrador.class, administrador.getIdAdministrador());
            Participante idParticipanteOld = persistentAdministrador.getIdParticipante();
            Participante idParticipanteNew = administrador.getIdParticipante();
            Torneo idTorneoOld = persistentAdministrador.getIdTorneo();
            Torneo idTorneoNew = administrador.getIdTorneo();
            if (idParticipanteNew != null) {
                idParticipanteNew = em.getReference(idParticipanteNew.getClass(), idParticipanteNew.getIdParticipante());
                administrador.setIdParticipante(idParticipanteNew);
            }
            if (idTorneoNew != null) {
                idTorneoNew = em.getReference(idTorneoNew.getClass(), idTorneoNew.getIdTorneo());
                administrador.setIdTorneo(idTorneoNew);
            }
            administrador = em.merge(administrador);
            if (idParticipanteOld != null && !idParticipanteOld.equals(idParticipanteNew)) {
                idParticipanteOld.getAdministradorList().remove(administrador);
                idParticipanteOld = em.merge(idParticipanteOld);
            }
            if (idParticipanteNew != null && !idParticipanteNew.equals(idParticipanteOld)) {
                idParticipanteNew.getAdministradorList().add(administrador);
                idParticipanteNew = em.merge(idParticipanteNew);
            }
            if (idTorneoOld != null && !idTorneoOld.equals(idTorneoNew)) {
                idTorneoOld.getAdministradorList().remove(administrador);
                idTorneoOld = em.merge(idTorneoOld);
            }
            if (idTorneoNew != null && !idTorneoNew.equals(idTorneoOld)) {
                idTorneoNew.getAdministradorList().add(administrador);
                idTorneoNew = em.merge(idTorneoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = administrador.getIdAdministrador();
                if (findAdministrador(id) == null) {
                    throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.");
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
            Administrador administrador;
            try {
                administrador = em.getReference(Administrador.class, id);
                administrador.getIdAdministrador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.", enfe);
            }
            Participante idParticipante = administrador.getIdParticipante();
            if (idParticipante != null) {
                idParticipante.getAdministradorList().remove(administrador);
                idParticipante = em.merge(idParticipante);
            }
            Torneo idTorneo = administrador.getIdTorneo();
            if (idTorneo != null) {
                idTorneo.getAdministradorList().remove(administrador);
                idTorneo = em.merge(idTorneo);
            }
            em.remove(administrador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrador> findAdministradorEntities() {
        return findAdministradorEntities(true, -1, -1);
    }

    public List<Administrador> findAdministradorEntities(int maxResults, int firstResult) {
        return findAdministradorEntities(false, maxResults, firstResult);
    }

    private List<Administrador> findAdministradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Administrador.class));
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

    public Administrador findAdministrador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrador.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdministradorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Administrador> rt = cq.from(Administrador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
