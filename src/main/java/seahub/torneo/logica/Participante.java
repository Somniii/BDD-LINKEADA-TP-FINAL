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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author tinov
 */
@Entity
@Table(name = "participante")
@NamedQueries({
    @NamedQuery(name = "Participante.findAll", query = "SELECT p FROM Participante p"),
    @NamedQuery(name = "Participante.findByIdPersona", query = "SELECT p FROM Participante p WHERE p.idPersona = :idPersona"),
    @NamedQuery(name = "Participante.findByIdParticipante", query = "SELECT p FROM Participante p WHERE p.idParticipante = :idParticipante")})
public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "idPersona")
    private int idPersona;
    @Id
    @Basic(optional = false)
    @Column(name = "idParticipante")
    private Integer idParticipante;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Lob
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Lob
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Lob
    @Column(name = "contrasena")
    private String contrasena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idParticipante")
    private List<Administrador> administradorList;
    @JoinColumn(name = "idTorneo", referencedColumnName = "idTorneo")
    @ManyToOne(optional = false)
    private Torneo idTorneo;

    public Participante() {
    }

    public Participante(Integer idParticipante) {
        this.idParticipante = idParticipante;
    }

    public Participante(Integer idParticipante, int idPersona, String nombre, String email, String usuario, String contrasena) {
        this.idParticipante = idParticipante;
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.email = email;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public Integer getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Integer idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Administrador> getAdministradorList() {
        return administradorList;
    }

    public void setAdministradorList(List<Administrador> administradorList) {
        this.administradorList = administradorList;
    }

    public Torneo getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Torneo idTorneo) {
        this.idTorneo = idTorneo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParticipante != null ? idParticipante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participante)) {
            return false;
        }
        Participante other = (Participante) object;
        if ((this.idParticipante == null && other.idParticipante != null) || (this.idParticipante != null && !this.idParticipante.equals(other.idParticipante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "seahub.torneo.logica.Participante[ idParticipante=" + idParticipante + " ]";
    }
    
}
