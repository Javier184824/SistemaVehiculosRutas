/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author javie
 */
public class Regular extends Usuario implements Serializable {
    
    public Regular() {
        super();
    }
    
    public Regular(String nombre) {
        super(nombre);
    }
    
    public Regular(String nombre, String correo, String contrasenia) {
        super(nombre, correo, contrasenia);
    }
    
    @Override
    public String toString() {
        return "Regular{" + super.toString() + '}';
    }
}
