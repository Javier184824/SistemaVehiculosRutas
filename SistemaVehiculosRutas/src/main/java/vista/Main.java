/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author javie
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1) Install FlatLaf *before* any Swing code runs:
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println("FlatLaf failed to initialize");
            ex.printStackTrace();
        }

        // 2) (Optional) Tweak some global UI defaults:
        UIManager.put( "Button.arc", 16 );                           // rounder buttons
        UIManager.put( "Button.background", new java.awt.Color(32,24,121) );
        UIManager.put( "Button.foreground", java.awt.Color.WHITE );
        UIManager.put( "TextField.background", new java.awt.Color(240,240,240) );
        UIManager.put( "PasswordField.background", new java.awt.Color(240,240,240) );
        // …any other global tweaks you like…

        // 3) Now launch your login frame
        SwingUtilities.invokeLater(() -> {
            new vista.InicioSesion().setVisible(true);
        });
    }
    
}
