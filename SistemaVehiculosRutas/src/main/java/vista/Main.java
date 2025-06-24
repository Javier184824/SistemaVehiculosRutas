/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import Admin.AdminService;
import Admin.CityManagementService;
import Admin.StationManagementService;
import Admin.UserManagementService;
import Binary.BinaryDataManager;
import Binary.DataInitializationService;
import Interfaces.DataManager;
import Main.RouteSystemContext;
import Services.AuthenticationService;
import Services.RouteService;
import Services.StationService;
import Services.VehicleService;
import User.UserService;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author javie
 */
public class Main {
    
    private static final String DATA_DIRECTORY = "data";
    private static final RouteSystemContext context = initializeSystem();
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
            new vista.InicioSesion(context).setVisible(true);
        });
    }
    
    /**
     * Initializes the system with all required dependencies
     * @return configured system context
     */
    private static RouteSystemContext initializeSystem() {
        // 1. Initialize data manager
        DataManager dataManager = (DataManager) new BinaryDataManager(DATA_DIRECTORY);
        
        // 2. Initialize default data if needed
        DataInitializationService initService = new DataInitializationService(dataManager);
        initService.initializeDefaultData();
        
        // 3. Initialize core services
        AuthenticationService authService = new AuthenticationService(dataManager);
        RouteService routeService = new RouteService(dataManager);
        StationService stationService = new StationService(dataManager);
        VehicleService vehicleService = new VehicleService(dataManager, authService);
        
        // 4. Initialize admin services
        CityManagementService cityManager = new CityManagementService(dataManager);
        StationManagementService stationManager = new StationManagementService(dataManager);
        UserManagementService userManager = new UserManagementService(dataManager);
        AdminService adminService = new AdminService(cityManager, stationManager, userManager, authService);
        
        // 5. Initialize user services
        UserService userService = new UserService(vehicleService, routeService);
        
        // 6. Create and return system context
        RouteSystemContext context = new RouteSystemContext(
            dataManager, authService, routeService, stationService,
            vehicleService, adminService, userService
        );
        
        return context;
    }
    
}
