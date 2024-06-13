/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seahub.torneo.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author tinov
 */
@Entity
@Table(name = "torneo")
@NamedQueries({
    @NamedQuery(name = "Torneo.findAll", query = "SELECT t FROM Torneo t"),
    @NamedQuery(name = "Torneo.findByIdTorneo", query = "SELECT t FROM Torneo t WHERE t.idTorneo = :idTorneo"),
    @NamedQuery(name = "Torneo.findByIdEtapa", query = "SELECT t FROM Torneo t WHERE t.idEtapa = :idEtapa"),
    @NamedQuery(name = "Torneo.findByPisos", query = "SELECT t FROM Torneo t WHERE t.pisos = :pisos"),
    @NamedQuery(name = "Torneo.findByPisosTotales", query = "SELECT t FROM Torneo t WHERE t.pisosTotales = :pisosTotales"),
    @NamedQuery(name = "Torneo.findByVigente", query = "SELECT t FROM Torneo t WHERE t.vigente = :vigente")})
public class Torneo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTorneo")
    private Integer idTorneo;
    @Basic(optional = false)
    @Column(name = "idEtapa")
    private int idEtapa;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "pisos")
    private int pisos;
    @Basic(optional = false)
    @Column(name = "pisosTotales")
    private int pisosTotales;
    @Basic(optional = false)
    @Column(name = "vigente")
    private boolean vigente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTorneo")
    private List<Administrador> administradorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTorneo")
    private List<Etapa> etapaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTorneo")
    private List<Participante> participanteList;

    public Torneo() {
    }

    public Torneo(Integer idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Torneo(Integer idTorneo, int idEtapa, String nombre, int pisos, int pisosTotales, boolean vigente) {
        this.idTorneo = idTorneo;
        this.idEtapa = idEtapa;
        this.nombre = nombre;
        this.pisos = pisos;
        this.pisosTotales = pisosTotales;
        this.vigente = vigente;
    }

    public Integer getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Integer idTorneo) {
        this.idTorneo = idTorneo;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(int idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPisos() {
        return pisos;
    }

    public void setPisos(int pisos) {
        this.pisos = pisos;
    }

    public int getPisosTotales() {
        return pisosTotales;
    }

    public void setPisosTotales(int pisosTotales) {
        this.pisosTotales = pisosTotales;
    }

    public boolean getVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public List<Administrador> getAdministradorList() {
        return administradorList;
    }

    public void setAdministradorList(List<Administrador> administradorList) {
        this.administradorList = administradorList;
    }

    public List<Etapa> getEtapaList() {
        return etapaList;
    }

    public void setEtapaList(List<Etapa> etapaList) {
        this.etapaList = etapaList;
    }

    public List<Participante> getParticipanteList() {
        return participanteList;
    }

    public void setParticipanteList(List<Participante> participanteList) {
        this.participanteList = participanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTorneo != null ? idTorneo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Torneo)) {
            return false;
        }
        Torneo other = (Torneo) object;
        if ((this.idTorneo == null && other.idTorneo != null) || (this.idTorneo != null && !this.idTorneo.equals(other.idTorneo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "seahub.torneo.logica.Torneo[ idTorneo=" + idTorneo + " ]";
    }
    
}
