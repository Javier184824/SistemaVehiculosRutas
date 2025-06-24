/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import modelo.Usuario;

/**
 *
 * @author javie
 */
public class ControladorUsuarios {
    private LinkedList<Usuario> lista;
    
    public ControladorUsuarios() {
        lista = new LinkedList<>();
    }
    
    /**
     * Agrega un nuevo usuario.
     * @param nuevo Usuario a agregar.
     */
    public void agregar(Usuario nuevo) {
        for (Usuario otro : lista) {
            if (nuevo.equals(otro)) {
                return;
            }
        }
        lista.add(nuevo);
    }
    
    /**
     * Busca un usuario en la lista tomando como referencia 
     * otra instancia de usuario regular.
     * @param referencia Usuario de referencia para buscar la instancia
     * que se desea buscar en la lista.
     * @return Usuario regular que se deseaba buscar.
     */
    public Usuario buscar(Usuario referencia) {
        for (Usuario otro : lista) {
            if (referencia.equals(otro)) {
                return otro;
            }
        }
        return null;
    }
    
    /**
     * Busca un usuario regular en la lista tomando como referencia un nombre.
     * @param nombre String con el nombre del usuario que se quiere
     * buscar.
     * @return Usuario que se deseaba buscar.
     */
    public Usuario buscar(String nombre) {
        for (Usuario otro : lista) {
            if (otro.getNombre().equals(nombre)) {
                return otro;
            }
        }
        return null;
    }
    
    /**
     * Elimina un usuario en la lista tomando como referencia otra 
     * instancia de usuario.
     * @param referencia Usuario de refencia para buscar la instancia
     * que se desea eliminar en la lista.
     */
    public void eliminar(Usuario referencia) {
        lista.remove(referencia);
    }
    
    /**
     * Elimina un usuario en la lista tomando como referencia un nombre.
     * @param nombre String con el nombre del usuario que se quiere 
     * eliminar.
     */
    public void eliminar(String nombre) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equals(nombre)) {
                lista.remove(i);
            }
        }
    }
    
    /**
     * Guarda la lista de usuarios en un archivo llamado 
     * "cuentasUsuariosRegulares.dat".
     */
    public void guardarLista() {
        try {
            FileOutputStream archivo = new FileOutputStream("cuentasUsarios.dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(archivo);
            objectStream.writeObject(lista);
            objectStream.close();
            archivo.close();
        }
        catch (IOException ex) {
            System.out.println("WARNING: IOException was caught");
        }
    }
    
    /**
     * Recupera la lista de usuarios guardados en el archivo 
     * "cuentasUsariosRegulares.dat".
     */
    public void recuperarLista() {
        try {
            FileInputStream archivo = new FileInputStream("cuentasUsarios.dat");
            ObjectInputStream objectStream = new ObjectInputStream(archivo);
            lista = (LinkedList<Usuario>) objectStream.readObject();
            objectStream.close();
            archivo.close();
        }
        catch (IOException ex) {
            System.out.println("WARNING: IOException was caught");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("WARNING: ClassNotFoundException was caught");
        }
    }

    @Override
    public String toString() {
        return "ControladorUsuariosRegulares{" + "lista=" + lista + '}';
    }
}
