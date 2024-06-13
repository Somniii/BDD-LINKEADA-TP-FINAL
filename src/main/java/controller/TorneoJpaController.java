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
import seahub.torneo.logica.Administrador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import seahub.torneo.logica.Etapa;
import seahub.torneo.logica.Participante;
import seahub.torneo.logica.Torneo;

/**
 *
 * @author Alumno
 */
public class TorneoJpaController implements Serializable {

    public TorneoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TorneoJpaController){
        emf =  Persistence.createEntityManagerFactory("torneoBDDJPAPU");
    }
        
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Torneo torneo) throws PreexistingEntityException, Exception {
        if (torneo.getAdministradorList() == null) {
            torneo.setAdministradorList(new ArrayList<Administrador>());
        }
        if (torneo.getEtapaList() == null) {
            torneo.setEtapaList(new ArrayList<Etapa>());
        }
        if (torneo.getParticipanteList() == null) {
            torneo.setParticipanteList(new ArrayList<Participante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Administrador> attachedAdministradorList = new ArrayList<Administrador>();
            for (Administrador administradorListAdministradorToAttach : torneo.getAdministradorList()) {
                administradorListAdministradorToAttach = em.getReference(administradorListAdministradorToAttach.getClass(), administradorListAdministradorToAttach.getIdAdministrador());
                attachedAdministradorList.add(administradorListAdministradorToAttach);
            }
            torneo.setAdministradorList(attachedAdministradorList);
            List<Etapa> attachedEtapaList = new ArrayList<Etapa>();
            for (Etapa etapaListEtapaToAttach : torneo.getEtapaList()) {
                etapaListEtapaToAttach = em.getReference(etapaListEtapaToAttach.getClass(), etapaListEtapaToAttach.getIdEtapa());
                attachedEtapaList.add(etapaListEtapaToAttach);
            }
            torneo.setEtapaList(attachedEtapaList);
            List<Participante> attachedParticipanteList = new ArrayList<Participante>();
            for (Participante participanteListParticipanteToAttach : torneo.getParticipanteList()) {
                participanteListParticipanteToAttach = em.getReference(participanteListParticipanteToAttach.getClass(), participanteListParticipanteToAttach.getIdParticipante());
                attachedParticipanteList.add(participanteListParticipanteToAttach);
            }
            torneo.setParticipanteList(attachedParticipanteList);
            em.persist(torneo);
            for (Administrador administradorListAdministrador : torneo.getAdministradorList()) {
                Torneo oldIdTorneoOfAdministradorListAdministrador = administradorListAdministrador.getIdTorneo();
                administradorListAdministrador.setIdTorneo(torneo);
                administradorListAdministrador = em.merge(administradorListAdministrador);
                if (oldIdTorneoOfAdministradorListAdministrador != null) {
                    oldIdTorneoOfAdministradorListAdministrador.getAdministradorList().remove(administradorListAdministrador);
                    oldIdTorneoOfAdministradorListAdministrador = em.merge(oldIdTorneoOfAdministradorListAdministrador);
                }
            }
            for (Etapa etapaListEtapa : torneo.getEtapaList()) {
                Torneo oldIdTorneoOfEtapaListEtapa = etapaListEtapa.getIdTorneo();
                etapaListEtapa.setIdTorneo(torneo);
                etapaListEtapa = em.merge(etapaListEtapa);
                if (oldIdTorneoOfEtapaListEtapa != null) {
                    oldIdTorneoOfEtapaListEtapa.getEtapaList().remove(etapaListEtapa);
                    oldIdTorneoOfEtapaListEtapa = em.merge(oldIdTorneoOfEtapaListEtapa);
                }
            }
            for (Participante participanteListParticipante : torneo.getParticipanteList()) {
                Torneo oldIdTorneoOfParticipanteListParticipante = participanteListParticipante.getIdTorneo();
                participanteListParticipante.setIdTorneo(torneo);
                participanteListParticipante = em.merge(participanteListParticipante);
                if (oldIdTorneoOfParticipanteListParticipante != null) {
                    oldIdTorneoOfParticipanteListParticipante.getParticipanteList().remove(participanteListParticipante);
                    oldIdTorneoOfParticipanteListParticipante = em.merge(oldIdTorneoOfParticipanteListParticipante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTorneo(torneo.getIdTorneo()) != null) {
                throw new PreexistingEntityException("Torneo " + torneo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Torneo torneo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Torneo persistentTorneo = em.find(Torneo.class, torneo.getIdTorneo());
            List<Administrador> administradorListOld = persistentTorneo.getAdministradorList();
            List<Administrador> administradorListNew = torneo.getAdministradorList();
            List<Etapa> etapaListOld = persistentTorneo.getEtapaList();
            List<Etapa> etapaListNew = torneo.getEtapaList();
            List<Participante> participanteListOld = persistentTorneo.getParticipanteList();
            List<Participante> participanteListNew = torneo.getParticipanteList();
            List<String> illegalOrphanMessages = null;
            for (Administrador administradorListOldAdministrador : administradorListOld) {
                if (!administradorListNew.contains(administradorListOldAdministrador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Administrador " + administradorListOldAdministrador + " since its idTorneo field is not nullable.");
                }
            }
            for (Etapa etapaListOldEtapa : etapaListOld) {
                if (!etapaListNew.contains(etapaListOldEtapa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Etapa " + etapaListOldEtapa + " since its idTorneo field is not nullable.");
                }
            }
            for (Participante participanteListOldParticipante : participanteListOld) {
                if (!participanteListNew.contains(participanteListOldParticipante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participante " + participanteListOldParticipante + " since its idTorneo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Administrador> attachedAdministradorListNew = new ArrayList<Administrador>();
            for (Administrador administradorListNewAdministradorToAttach : administradorListNew) {
                administradorListNewAdministradorToAttach = em.getReference(administradorListNewAdministradorToAttach.getClass(), administradorListNewAdministradorToAttach.getIdAdministrador());
                attachedAdministradorListNew.add(administradorListNewAdministradorToAttach);
            }
            administradorListNew = attachedAdministradorListNew;
            torneo.setAdministradorList(administradorListNew);
            List<Etapa> attachedEtapaListNew = new ArrayList<Etapa>();
            for (Etapa etapaListNewEtapaToAttach : etapaListNew) {
                etapaListNewEtapaToAttach = em.getReference(etapaListNewEtapaToAttach.getClass(), etapaListNewEtapaToAttach.getIdEtapa());
                attachedEtapaListNew.add(etapaListNewEtapaToAttach);
            }
            etapaListNew = attachedEtapaListNew;
            torneo.setEtapaList(etapaListNew);
            List<Participante> attachedParticipanteListNew = new ArrayList<Participante>();
            for (Participante participanteListNewParticipanteToAttach : participanteListNew) {
                participanteListNewParticipanteToAttach = em.getReference(participanteListNewParticipanteToAttach.getClass(), participanteListNewParticipanteToAttach.getIdParticipante());
                attachedParticipanteListNew.add(participanteListNewParticipanteToAttach);
            }
            participanteListNew = attachedParticipanteListNew;
            torneo.setParticipanteList(participanteListNew);
            torneo = em.merge(torneo);
            for (Administrador administradorListNewAdministrador : administradorListNew) {
                if (!administradorListOld.contains(administradorListNewAdministrador)) {
                    Torneo oldIdTorneoOfAdministradorListNewAdministrador = administradorListNewAdministrador.getIdTorneo();
                    administradorListNewAdministrador.setIdTorneo(torneo);
                    administradorListNewAdministrador = em.merge(administradorListNewAdministrador);
                    if (oldIdTorneoOfAdministradorListNewAdministrador != null && !oldIdTorneoOfAdministradorListNewAdministrador.equals(torneo)) {
                        oldIdTorneoOfAdministradorListNewAdministrador.getAdministradorList().remove(administradorListNewAdministrador);
                        oldIdTorneoOfAdministradorListNewAdministrador = em.merge(oldIdTorneoOfAdministradorListNewAdministrador);
                    }
                }
            }
            for (Etapa etapaListNewEtapa : etapaListNew) {
                if (!etapaListOld.contains(etapaListNewEtapa)) {
                    Torneo oldIdTorneoOfEtapaListNewEtapa = etapaListNewEtapa.getIdTorneo();
                    etapaListNewEtapa.setIdTorneo(torneo);
                    etapaListNewEtapa = em.merge(etapaListNewEtapa);
                    if (oldIdTorneoOfEtapaListNewEtapa != null && !oldIdTorneoOfEtapaListNewEtapa.equals(torneo)) {
                        oldIdTorneoOfEtapaListNewEtapa.getEtapaList().remove(etapaListNewEtapa);
                        oldIdTorneoOfEtapaListNewEtapa = em.merge(oldIdTorneoOfEtapaListNewEtapa);
                    }
                }
            }
            for (Participante participanteListNewParticipante : participanteListNew) {
                if (!participanteListOld.contains(participanteListNewParticipante)) {
                    Torneo oldIdTorneoOfParticipanteListNewParticipante = participanteListNewParticipante.getIdTorneo();
                    participanteListNewParticipante.setIdTorneo(torneo);
                    participanteListNewParticipante = em.merge(participanteListNewParticipante);
                    if (oldIdTorneoOfParticipanteListNewParticipante != null && !oldIdTorneoOfParticipanteListNewParticipante.equals(torneo)) {
                        oldIdTorneoOfParticipanteListNewParticipante.getParticipanteList().remove(participanteListNewParticipante);
                        oldIdTorneoOfParticipanteListNewParticipante = em.merge(oldIdTorneoOfParticipanteListNewParticipante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = torneo.getIdTorneo();
                if (findTorneo(id) == null) {
                    throw new NonexistentEntityException("The torneo with id " + id + " no longer exists.");
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
            Torneo torneo;
            try {
                torneo = em.getReference(Torneo.class, id);
                torneo.getIdTorneo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The torneo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Administrador> administradorListOrphanCheck = torneo.getAdministradorList();
            for (Administrador administradorListOrphanCheckAdministrador : administradorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Torneo (" + torneo + ") cannot be destroyed since the Administrador " + administradorListOrphanCheckAdministrador + " in its administradorList field has a non-nullable idTorneo field.");
            }
            List<Etapa> etapaListOrphanCheck = torneo.getEtapaList();
            for (Etapa etapaListOrphanCheckEtapa : etapaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Torneo (" + torneo + ") cannot be destroyed since the Etapa " + etapaListOrphanCheckEtapa + " in its etapaList field has a non-nullable idTorneo field.");
            }
            List<Participante> participanteListOrphanCheck = torneo.getParticipanteList();
            for (Participante participanteListOrphanCheckParticipante : participanteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Torneo (" + torneo + ") cannot be destroyed since the Participante " + participanteListOrphanCheckParticipante + " in its participanteList field has a non-nullable idTorneo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(torneo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Torneo> findTorneoEntities() {
        return findTorneoEntities(true, -1, -1);
    }

    public List<Torneo> findTorneoEntities(int maxResults, int firstResult) {
        return findTorneoEntities(false, maxResults, firstResult);
    }

    private List<Torneo> findTorneoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Torneo.class));
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

    public Torneo findTorneo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Torneo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTorneoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Torneo> rt = cq.from(Torneo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
