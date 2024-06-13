/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author tinov
 */
public class EtapaJpaController implements Serializable {
    public EtapaJpaController(EntityManagerFactory emf){
        this.emf = emf;
    }
}
