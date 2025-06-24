/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author javie
 */
public abstract class Usuario implements Serializable {
    private String nombre;
    private String correo;
    private String contrasenia;
    
    public Usuario() {
        nombre = "";
        correo = "";
        contrasenia = "";
    }
    
    public Usuario(String nombre) {
        this.nombre = nombre;
        correo = "";
        contrasenia = "";
    }
    
    public Usuario(String nombre, String correo, String contrasenia) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.nombre, other.nombre);
    }

    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre + ", correo=" + correo + ", contrasenia=" + contrasenia + '}';
    }
}
