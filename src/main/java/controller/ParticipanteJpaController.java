/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import seahub.torneo.logica.Torneo;
import seahub.torneo.logica.Administrador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import seahub.torneo.logica.Participante;

/**
 *
 * @author Alumno
 */
public class ParticipanteJpaController implements Serializable {

    public ParticipanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ParticipanteJpaController(){
        emf =  Persistence.createEntityManagerFactory("torneoBDDJPAPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Participante participante) throws PreexistingEntityException, Exception {
        if (participante.getAdministradorList() == null) {
            participante.setAdministradorList(new ArrayList<Administrador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Torneo idTorneo = participante.getIdTorneo();
            if (idTorneo != null) {
                idTorneo = em.getReference(idTorneo.getClass(), idTorneo.getIdTorneo());
                participante.setIdTorneo(idTorneo);
            }
            List<Administrador> attachedAdministradorList = new ArrayList<Administrador>();
            for (Administrador administradorListAdministradorToAttach : participante.getAdministradorList()) {
                administradorListAdministradorToAttach = em.getReference(administradorListAdministradorToAttach.getClass(), administradorListAdministradorToAttach.getIdAdministrador());
                attachedAdministradorList.add(administradorListAdministradorToAttach);
            }
            participante.setAdministradorList(attachedAdministradorList);
            em.persist(participante);
            if (idTorneo != null) {
                idTorneo.getParticipanteList().add(participante);
                idTorneo = em.merge(idTorneo);
            }
            for (Administrador administradorListAdministrador : participante.getAdministradorList()) {
                Participante oldIdParticipanteOfAdministradorListAdministrador = administradorListAdministrador.getIdParticipante();
                administradorListAdministrador.setIdParticipante(participante);
                administradorListAdministrador = em.merge(administradorListAdministrador);
                if (oldIdParticipanteOfAdministradorListAdministrador != null) {
                    oldIdParticipanteOfAdministradorListAdministrador.getAdministradorList().remove(administradorListAdministrador);
                    oldIdParticipanteOfAdministradorListAdministrador = em.merge(oldIdParticipanteOfAdministradorListAdministrador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParticipante(participante.getIdParticipante()) != null) {
                throw new PreexistingEntityException("Participante " + participante + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Participante participante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participante persistentParticipante = em.find(Participante.class, participante.getIdParticipante());
            Torneo idTorneoOld = persistentParticipante.getIdTorneo();
            Torneo idTorneoNew = participante.getIdTorneo();
            List<Administrador> administradorListOld = persistentParticipante.getAdministradorList();
            List<Administrador> administradorListNew = participante.getAdministradorList();
            List<String> illegalOrphanMessages = null;
            for (Administrador administradorListOldAdministrador : administradorListOld) {
                if (!administradorListNew.contains(administradorListOldAdministrador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Administrador " + administradorListOldAdministrador + " since its idParticipante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTorneoNew != null) {
                idTorneoNew = em.getReference(idTorneoNew.getClass(), idTorneoNew.getIdTorneo());
                participante.setIdTorneo(idTorneoNew);
            }
            List<Administrador> attachedAdministradorListNew = new ArrayList<Administrador>();
            for (Administrador administradorListNewAdministradorToAttach : administradorListNew) {
                administradorListNewAdministradorToAttach = em.getReference(administradorListNewAdministradorToAttach.getClass(), administradorListNewAdministradorToAttach.getIdAdministrador());
                attachedAdministradorListNew.add(administradorListNewAdministradorToAttach);
            }
            administradorListNew = attachedAdministradorListNew;
            participante.setAdministradorList(administradorListNew);
            participante = em.merge(participante);
            if (idTorneoOld != null && !idTorneoOld.equals(idTorneoNew)) {
                idTorneoOld.getParticipanteList().remove(participante);
                idTorneoOld = em.merge(idTorneoOld);
            }
            if (idTorneoNew != null && !idTorneoNew.equals(idTorneoOld)) {
                idTorneoNew.getParticipanteList().add(participante);
                idTorneoNew = em.merge(idTorneoNew);
            }
            for (Administrador administradorListNewAdministrador : administradorListNew) {
                if (!administradorListOld.contains(administradorListNewAdministrador)) {
                    Participante oldIdParticipanteOfAdministradorListNewAdministrador = administradorListNewAdministrador.getIdParticipante();
                    administradorListNewAdministrador.setIdParticipante(participante);
                    administradorListNewAdministrador = em.merge(administradorListNewAdministrador);
                    if (oldIdParticipanteOfAdministradorListNewAdministrador != null && !oldIdParticipanteOfAdministradorListNewAdministrador.equals(participante)) {
                        oldIdParticipanteOfAdministradorListNewAdministrador.getAdministradorList().remove(administradorListNewAdministrador);
                        oldIdParticipanteOfAdministradorListNewAdministrador = em.merge(oldIdParticipanteOfAdministradorListNewAdministrador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = participante.getIdParticipante();
                if (findParticipante(id) == null) {
                    throw new NonexistentEntityException("The participante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participante participante;
            try {
                participante = em.getReference(Participante.class, id);
                participante.getIdParticipante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Administrador> administradorListOrphanCheck = participante.getAdministradorList();
            for (Administrador administradorListOrphanCheckAdministrador : administradorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Participante (" + participante + ") cannot be destroyed since the Administrador " + administradorListOrphanCheckAdministrador + " in its administradorList field has a non-nullable idParticipante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Torneo idTorneo = participante.getIdTorneo();
            if (idTorneo != null) {
                idTorneo.getParticipanteList().remove(participante);
                idTorneo = em.merge(idTorneo);
            }
            em.remove(participante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Participante> findParticipanteEntities() {
        return findParticipanteEntities(true, -1, -1);
    }

    public List<Participante> findParticipanteEntities(int maxResults, int firstResult) {
        return findParticipanteEntities(false, maxResults, firstResult);
    }

    private List<Participante> findParticipanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Participante.class));
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

    public Participante findParticipante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Participante.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Participante> rt = cq.from(Participante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
