/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Admin.AdminService;
import Admin.CityManagementService;
import Admin.StationManagementService;
import Admin.UserManagementService;
import Binary.BinaryDataManager;
import Binary.DataInitializationService;
import Console.ConsoleUI;
import Interfaces.DataManager;
import Services.AuthenticationService;
import Services.RouteService;
import Services.StationService;
import Services.VehicleService;
import User.UserService;

/**
 *
 * @author JE
 */
public class Entry {
    
    private static final String DATA_DIRECTORY = "data";
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Route Planning System...");
        
        try {
            // Initialize system components
            RouteSystemContext context = initializeSystem();
            
            // Start the console UI
            ConsoleUI consoleUI = new ConsoleUI(
                context.getAuthenticationService(),
                context.getAdminService(),
                context.getUserService()
            );
            
            consoleUI.start();
            
        } catch (Exception e) {
            System.err.println("❌ Failed to start application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("👋 Route Planning System terminated.");
    }
    
    /**
     * Initializes the system with all required dependencies
     * @return configured system context
     */
    private static RouteSystemContext initializeSystem() {
        System.out.println("🔧 Initializing system components...");
        
        // 1. Initialize data manager
        DataManager dataManager = (DataManager) new BinaryDataManager(DATA_DIRECTORY);
        System.out.println("✓ Data manager initialized");
        
        // 2. Initialize default data if needed
        DataInitializationService initService = new DataInitializationService(dataManager);
        initService.initializeDefaultData();
        System.out.println("✓ Default data verified");
        
        // 3. Initialize core services
        AuthenticationService authService = new AuthenticationService(dataManager);
        RouteService routeService = new RouteService(dataManager);
        StationService stationService = new StationService(dataManager);
        VehicleService vehicleService = new VehicleService(dataManager, authService);
        System.out.println("✓ Core services initialized");
        
        // 4. Initialize admin services
        CityManagementService cityManager = new CityManagementService(dataManager);
        StationManagementService stationManager = new StationManagementService(dataManager);
        UserManagementService userManager = new UserManagementService(dataManager);
        AdminService adminService = new AdminService(cityManager, stationManager, userManager, authService);
        System.out.println("✓ Admin services initialized");
        
        // 5. Initialize user services
        UserService userService = new UserService(vehicleService, routeService);
        System.out.println("✓ User services initialized");
        
        // 6. Create and return system context
        RouteSystemContext context = new RouteSystemContext(
            dataManager, authService, routeService, stationService,
            vehicleService, adminService, userService
        );
        
        System.out.println("✅ System initialization complete!");
        System.out.println("📊 " + routeService.getGraphStatistics());
        
        return context;
    }
    
    /**
     * Handles command line arguments for special operations
     * @param args command line arguments
     */
    private static void handleCommandLineArgs(String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "--reset-data":
                    resetSystemData();
                    System.exit(0);
                    break;
                case "--backup-data":
                    backupSystemData();
                    System.exit(0);
                    break;
                case "--help":
                    showHelp();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown argument: " + args[0]);
                    showHelp();
                    System.exit(1);
                    break;
            }
        }
    }
    
    /**
     * Resets all system data to defaults
     */
    private static void resetSystemData() {
        System.out.println("🔄 Resetting system data to defaults...");
        
        DataManager dataManager = new BinaryDataManager(DATA_DIRECTORY);
        DataInitializationService initService = new DataInitializationService(dataManager);
        initService.resetToDefaults();
        
        System.out.println("✅ System data reset completed!");
    }
    
    /**
     * Creates backup of all system data
     */
    private static void backupSystemData() {
        System.out.println("💾 Creating system data backup...");
        
        BinaryDataManager dataManager = new BinaryDataManager(DATA_DIRECTORY);
        
        String[] files = {
            "users.dat", "cities.dat", "connections.dat", 
            "fuel_types.dat", "charger_types.dat"
        };
        
        int backupCount = 0;
        for (String file : files) {
            if (dataManager.backupFile(file)) {
                backupCount++;
                System.out.println("✓ Backed up " + file);
            }
        }
        
        System.out.println("✅ Backup completed! " + backupCount + " files backed up.");
    }
    
    /**
     * Shows command line help
     */
    private static void showHelp() {
        System.out.println("🚗 Route Planning System - Help");
        System.out.println("================================");
        System.out.println("Usage: java -jar route-planning-system.jar [option]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --help         Show this help message");
        System.out.println("  --reset-data   Reset all data to factory defaults");
        System.out.println("  --backup-data  Create backup of all data files");
        System.out.println();
        System.out.println("Default login credentials:");
        System.out.println("  Admin:  admin / admin123");
        System.out.println("  User:   user / user123");
    }
}
