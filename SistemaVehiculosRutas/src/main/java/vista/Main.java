/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import control.ControladorUsuarios;
import modelo.Admin;
import modelo.Regular;

/**
 *
 * @author javie
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //InicioSesion.main(args);
        
        ControladorUsuarios c = new ControladorUsuarios();
        c.agregar(new Admin("a", "b", "c"));
        c.agregar(new Regular("a", "b", "c"));
        c.guardarLista();
        c.recuperarLista();
        System.out.println(c);
    }
    
}
