/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import java.util.List;

import Admin.AdminService;
import Models.City;
import Models.Connection;
import User.User;
import User.UserRole;
import Vehicle.ChargerType;
import Vehicle.FuelType;

/**
 *
 * @author JE
 */
public class AdminConsole {
    
    private final AdminService adminService;
    
    public AdminConsole(AdminService adminService) {
        this.adminService = adminService;
    }
    
    /**
     * Shows the admin menu
     */
    public void showAdminMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🔧 Admin Panel",
                "Manage Cities & Routes",
                "Manage Stations & Energy Types",
                "Manage Users",
                "System Information"
            );
            
            switch (choice) {
                case 0 -> showCityManagementMenu();
                case 1 -> showStationManagementMenu();
                case 2 -> showUserManagementMenu();
                case 3 -> showSystemInformation();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Shows the city management menu
     */
    private void showCityManagementMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🏙️ City & Route Management",
                "List All Cities",
                "Create New City",
                "Update City",
                "Delete City",
                "List All Connections",
                "Create New Connection",
                "Delete Connection",
                "Fix Corrupted Connections"
            );
            
            switch (choice) {
                case 0 -> listAllCities();
                case 1 -> createNewCity();
                case 2 -> updateCity();
                case 3 -> deleteCity();
                case 4 -> listAllConnections();
                case 5 -> createNewConnection();
                case 6 -> deleteConnection();
                case 7 -> fixCorruptedConnections();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Shows the station management menu
     */
    private void showStationManagementMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "⛽ Station & Energy Type Management",
                "List Fuel Types",
                "Create Fuel Type",
                "Delete Fuel Type",
                "List Charger Types",
                "Create Charger Type",
                "Delete Charger Type"
            );
            
            switch (choice) {
                case 0 -> listFuelTypes();
                case 1 -> createFuelType();
                case 2 -> deleteFuelType();
                case 3 -> listChargerTypes();
                case 4 -> createChargerType();
                case 5 -> deleteChargerType();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Shows the user management menu
     */
    private void showUserManagementMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "👥 User Management",
                "List All Users",
                "Create New User",
                "Change User Role",
                "Delete User",
                "User Statistics"
            );
            
            switch (choice) {
                case 0 -> listAllUsers();
                case 1 -> createNewUser();
                case 2 -> changeUserRole();
                case 3 -> deleteUser();
                case 4 -> showUserStatistics();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    // ========== CITY MANAGEMENT IMPLEMENTATIONS ==========
    
    private void listAllCities() {
        List<City> cities = adminService.getCityManager().getAllCities();
        
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities found.");
            return;
        }
        
        System.out.println("\n📍 All Cities:");
        System.out.println("-".repeat(80));
        System.out.printf("%-30s %-15s %-15s %-10s%n", "Name", "Latitude", "Longitude", "Stations");
        System.out.println("-".repeat(80));
        
        for (City city : cities) {
            System.out.printf("%-30s %-15.6f %-15.6f %-10d%n", 
                city.getName(), city.getLatitude(), city.getLongitude(), city.getStations().size());
        }
        
        MenuUtil.pause("City list displayed.");
    }
    
    private void createNewCity() {
        System.out.println("\n🏗️ Create New City");
        
        String name = MenuUtil.getNonEmptyStringInput("City Name: ");
        double latitude = MenuUtil.getDoubleInput("Latitude: ");
        double longitude = MenuUtil.getDoubleInput("Longitude: ");
        
        City newCity = new City(name, latitude, longitude);
        
        if (adminService.getCityManager().createCity(newCity)) {
            MenuUtil.showSuccess("City '" + name + "' created successfully!");
        } else {
            MenuUtil.showError("Failed to create city. Name may already exist.");
        }
    }
    
    private void updateCity() {
        List<City> cities = adminService.getCityManager().getAllCities();
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities available to update.");
            return;
        }
        
        // Display cities with indices
        System.out.println("\n📝 Select City to Update:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cities.get(i).getName());
        }
        
        int index = MenuUtil.getIntInput("Select city (number): ") - 1;
        if (index < 0 || index >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        City cityToUpdate = cities.get(index);
        String newName = MenuUtil.getStringInput("New name (current: " + cityToUpdate.getName() + "): ");
        if (!newName.isEmpty()) {
            cityToUpdate.setName(newName);
        }
        
        // Update coordinates if needed
        if (MenuUtil.getConfirmation("Update coordinates?")) {
            double newLat = MenuUtil.getDoubleInput("New latitude: ");
            double newLng = MenuUtil.getDoubleInput("New longitude: ");
            cityToUpdate.setLatitude(newLat);
            cityToUpdate.setLongitude(newLng);
        }
        
        if (adminService.getCityManager().updateCity(cityToUpdate.getId(), cityToUpdate)) {
            MenuUtil.showSuccess("City updated successfully!");
        } else {
            MenuUtil.showError("Failed to update city.");
        }
    }
    
    private void deleteCity() {
        List<City> cities = adminService.getCityManager().getAllCities();
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ Select City to Delete:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cities.get(i).getName());
        }
        
        int index = MenuUtil.getIntInput("Select city (number): ") - 1;
        if (index < 0 || index >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        City cityToDelete = cities.get(index);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to delete '" + cityToDelete.getName() + "'?")) {
            return;
        }
        
        if (adminService.getCityManager().deleteCity(cityToDelete.getId())) {
            MenuUtil.showSuccess("City deleted successfully!");
        } else {
            MenuUtil.showError("Failed to delete city. It may have existing connections.");
        }
    }
    
    private void listAllConnections() {
        List<Connection> connections = adminService.getCityManager().getAllConnections();
        
        if (connections.isEmpty()) {
            MenuUtil.showInfo("No connections found.");
            return;
        }
        
        System.out.println("\n🛣️ All Connections:");
        System.out.println("-".repeat(100));
        System.out.printf("%-20s %-20s %-10s %-10s %-10s%n", "From", "To", "Distance", "Time", "Cost");
        System.out.println("-".repeat(100));
        
        for (Connection conn : connections) {
            System.out.printf("%-20s %-20s %-10.1f %-10d %-10.2f%n",
                conn.getFromCity() != null ? conn.getFromCity().getName() : "Unknown",
                conn.getToCity() != null ? conn.getToCity().getName() : "Unknown",
                conn.getDistance(), conn.getTimeMinutes(), conn.getCost());
        }
        
        MenuUtil.pause("Connection list displayed.");
    }
    
    private void createNewConnection() {
        List<City> cities = adminService.getCityManager().getAllCities();
        if (cities.size() < 2) {
            MenuUtil.showError("Need at least 2 cities to create connections.");
            return;
        }
        
        System.out.println("\n🔗 Create New Connection");
        
        // Select from city
        System.out.println("From City:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cities.get(i).getName());
        }
        int fromIndex = MenuUtil.getIntInput("Select from city: ") - 1;
        if (fromIndex < 0 || fromIndex >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        // Select to city
        System.out.println("To City:");
        for (int i = 0; i < cities.size(); i++) {
            if (i != fromIndex) {
                System.out.printf("%d. %s%n", i + 1, cities.get(i).getName());
            }
        }
        int toIndex = MenuUtil.getIntInput("Select to city: ") - 1;
        if (toIndex < 0 || toIndex >= cities.size() || toIndex == fromIndex) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        double distance = MenuUtil.getDoubleInput("Distance (km): ");
        int time = MenuUtil.getIntInput("Travel time (minutes): ");
        double cost = MenuUtil.getDoubleInput("Cost: ");
        
        Connection newConnection = new Connection(cities.get(fromIndex), cities.get(toIndex), distance, time, cost);
        
        if (adminService.getCityManager().createConnection(newConnection)) {
            MenuUtil.showSuccess("Connection created successfully!");
        } else {
            MenuUtil.showError("Failed to create connection. It may already exist.");
        }
    }
    
    private void deleteConnection() {
        List<Connection> connections = adminService.getCityManager().getAllConnections();
        if (connections.isEmpty()) {
            MenuUtil.showInfo("No connections available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ Select Connection to Delete:");
        for (int i = 0; i < connections.size(); i++) {
            Connection conn = connections.get(i);
            System.out.printf("%d. %s → %s%n", i + 1, 
                conn.getFromCity() != null ? conn.getFromCity().getName() : "Unknown",
                conn.getToCity() != null ? conn.getToCity().getName() : "Unknown");
        }
        
        int index = MenuUtil.getIntInput("Select connection (number): ") - 1;
        if (index < 0 || index >= connections.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        Connection connToDelete = connections.get(index);
        
        // Add detailed debugging
        // System.out.println("Debug: Connection details:");
        // System.out.println("  From City: " + (connToDelete.getFromCity() != null ? connToDelete.getFromCity().getName() : "null"));
        // System.out.println("  From City ID: " + connToDelete.getFromCityId());
        // System.out.println("  To City: " + (connToDelete.getToCity() != null ? connToDelete.getToCity().getName() : "null"));
        // System.out.println("  To City ID: " + connToDelete.getToCityId());
        
        String fromId = connToDelete.getFromCity() != null ? connToDelete.getFromCity().getId() : connToDelete.getFromCityId();
        String toId = connToDelete.getToCity() != null ? connToDelete.getToCity().getId() : connToDelete.getToCityId();
        
        // System.out.println("Debug: Attempting to delete connection with IDs: " + fromId + " -> " + toId);
        // System.out.println("Debug: Connection object: " + connToDelete);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to delete this connection?")) {
            return;
        }
        
        if (adminService.getCityManager().deleteConnection(fromId, toId)) {
            MenuUtil.showSuccess("Connection deleted successfully!");
        } else {
            MenuUtil.showError("Failed to delete connection.");
        }
    }
    
    private void fixCorruptedConnections() {
        System.out.println("\n🔄 Fix Corrupted Connections");
        
        if (!MenuUtil.getConfirmation("This will delete and recreate all connections. Are you sure?")) {
            return;
        }
        
        if (adminService.getCityManager().fixCorruptedConnections()) {
            MenuUtil.showSuccess("Connections fixed successfully!");
        } else {
            MenuUtil.showError("Failed to fix connections.");
        }
    }
    
    // ========== STATION MANAGEMENT IMPLEMENTATIONS ==========
    
    private void listFuelTypes() {
        List<FuelType> fuelTypes = adminService.getStationManager().getAllFuelTypes();
        
        if (fuelTypes.isEmpty()) {
            MenuUtil.showInfo("No fuel types found.");
            return;
        }
        
        System.out.println("\n⛽ Fuel Types:");
        System.out.println("-".repeat(60));
        for (FuelType ft : fuelTypes) {
            System.out.printf("• %s - %s%n", ft.getName(), ft.getDescription());
        }
        
        MenuUtil.pause("Fuel types displayed.");
    }
    
    private void createFuelType() {
        System.out.println("\n🆕 Create Fuel Type");
        
        String id = MenuUtil.getNonEmptyStringInput("Fuel Type ID: ");
        String name = MenuUtil.getNonEmptyStringInput("Name: ");
        String description = MenuUtil.getStringInput("Description: ");
        
        FuelType newFuelType = new FuelType(id, name, description);
        
        if (adminService.getStationManager().createFuelType(newFuelType)) {
            MenuUtil.showSuccess("Fuel type '" + name + "' created successfully!");
        } else {
            MenuUtil.showError("Failed to create fuel type. ID may already exist.");
        }
    }
    
    private void deleteFuelType() {
        List<FuelType> fuelTypes = adminService.getStationManager().getAllFuelTypes();
        if (fuelTypes.isEmpty()) {
            MenuUtil.showInfo("No fuel types available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ Select Fuel Type to Delete:");
        for (int i = 0; i < fuelTypes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, fuelTypes.get(i).getName());
        }
        
        int index = MenuUtil.getIntInput("Select fuel type (number): ") - 1;
        if (index < 0 || index >= fuelTypes.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        FuelType fuelTypeToDelete = fuelTypes.get(index);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to delete '" + fuelTypeToDelete.getName() + "'?")) {
            return;
        }
        
        if (adminService.getStationManager().deleteFuelType(fuelTypeToDelete.getId())) {
            MenuUtil.showSuccess("Fuel type deleted successfully!");
        } else {
            MenuUtil.showError("Failed to delete fuel type.");
        }
    }
    
    private void listChargerTypes() {
        List<ChargerType> chargerTypes = adminService.getStationManager().getAllChargerTypes();
        
        if (chargerTypes.isEmpty()) {
            MenuUtil.showInfo("No charger types found.");
            return;
        }
        
        System.out.println("\n🔌 Charger Types:");
        System.out.println("-".repeat(80));
        System.out.printf("%-20s %-20s %-10s%n", "Name", "Standard", "Max Power");
        System.out.println("-".repeat(80));
        
        for (ChargerType ct : chargerTypes) {
            System.out.printf("%-20s %-20s %-10d kW%n", 
                ct.getName(), ct.getStandard(), ct.getMaxPowerKW());
        }
        
        MenuUtil.pause("Charger types displayed.");
    }
    
    private void createChargerType() {
        System.out.println("\n🆕 Create Charger Type");
        
        String id = MenuUtil.getNonEmptyStringInput("Charger Type ID: ");
        String name = MenuUtil.getNonEmptyStringInput("Name: ");
        String standard = MenuUtil.getNonEmptyStringInput("Standard: ");
        int maxPower = MenuUtil.getIntInput("Max Power (kW): ");
        
        ChargerType newChargerType = new ChargerType(id, name, standard, maxPower);
        
        if (adminService.getStationManager().createChargerType(newChargerType)) {
            MenuUtil.showSuccess("Charger type '" + name + "' created successfully!");
        } else {
            MenuUtil.showError("Failed to create charger type. ID may already exist.");
        }
    }
    
    private void deleteChargerType() {
        List<ChargerType> chargerTypes = adminService.getStationManager().getAllChargerTypes();
        if (chargerTypes.isEmpty()) {
            MenuUtil.showInfo("No charger types available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ Select Charger Type to Delete:");
        for (int i = 0; i < chargerTypes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, chargerTypes.get(i).getName());
        }
        
        int index = MenuUtil.getIntInput("Select charger type (number): ") - 1;
        if (index < 0 || index >= chargerTypes.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        ChargerType chargerTypeToDelete = chargerTypes.get(index);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to delete '" + chargerTypeToDelete.getName() + "'?")) {
            return;
        }
        
        if (adminService.getStationManager().deleteChargerType(chargerTypeToDelete.getId())) {
            MenuUtil.showSuccess("Charger type deleted successfully!");
        } else {
            MenuUtil.showError("Failed to delete charger type.");
        }
    }
    
    // ========== USER MANAGEMENT IMPLEMENTATIONS ==========
    
    private void listAllUsers() {
        List<User> users = adminService.getUserManager().getAllUsers();
        
        if (users.isEmpty()) {
            MenuUtil.showInfo("No users found.");
            return;
        }
        
        System.out.println("\n👥 All Users:");
        System.out.println("-".repeat(60));
        System.out.printf("%-20s %-15s %-10s%n", "Username", "Role", "Vehicles");
        System.out.println("-".repeat(60));
        
        for (User user : users) {
            System.out.printf("%-20s %-15s %-10d%n", 
                user.getUsername(), user.getRole().getDisplayName(), user.getVehicles().size());
        }
        
        MenuUtil.pause("User list displayed.");
    }
    
    private void createNewUser() {
        System.out.println("\n👤 Create New User");
        
        String username = MenuUtil.getNonEmptyStringInput("Username: ");
        String password = MenuUtil.getNonEmptyStringInput("Password: ");
        
        System.out.println("User Role:");
        System.out.println("1. Regular User");
        System.out.println("2. Administrator");
        int roleChoice = MenuUtil.getIntInput("Select role: ");
        
        UserRole role = (roleChoice == 2) ? UserRole.ADMIN : UserRole.USER;
        
        User newUser = new User(username, password, role);
        
        if (adminService.getUserManager().createUser(newUser)) {
            MenuUtil.showSuccess("User '" + username + "' created successfully!");
        } else {
            MenuUtil.showError("Failed to create user. Username may already exist.");
        }
    }
    
    private void changeUserRole() {
        List<User> users = adminService.getUserManager().getAllUsers();
        if (users.isEmpty()) {
            MenuUtil.showInfo("No users available.");
            return;
        }
        
        System.out.println("\n🔄 Change User Role:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, user.getUsername(), user.getRole().getDisplayName());
        }
        
        int index = MenuUtil.getIntInput("Select user (number): ") - 1;
        if (index < 0 || index >= users.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        User userToUpdate = users.get(index);
        
        System.out.println("New Role:");
        System.out.println("1. Regular User");
        System.out.println("2. Administrator");
        int roleChoice = MenuUtil.getIntInput("Select new role: ");
        
        UserRole newRole = (roleChoice == 2) ? UserRole.ADMIN : UserRole.USER;
        
        if (adminService.getUserManager().changeUserRole(userToUpdate.getId(), newRole)) {
            MenuUtil.showSuccess("User role changed successfully!");
        } else {
            MenuUtil.showError("Failed to change user role.");
        }
    }
    
    private void deleteUser() {
        List<User> users = adminService.getUserManager().getAllUsers();
        if (users.isEmpty()) {
            MenuUtil.showInfo("No users available to delete.");
            return;
        }
        
        System.out.println("\n🗑️ Select User to Delete:");
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, users.get(i).getUsername());
        }
        
        int index = MenuUtil.getIntInput("Select user (number): ") - 1;
        if (index < 0 || index >= users.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        User userToDelete = users.get(index);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to delete '" + userToDelete.getUsername() + "'?")) {
            return;
        }
        
        if (adminService.getUserManager().deleteUser(userToDelete.getId())) {
            MenuUtil.showSuccess("User deleted successfully!");
        } else {
            MenuUtil.showError("Failed to delete user.");
        }
    }
    
    private void showUserStatistics() {
        String stats = adminService.getUserManager().getUserStatistics();
        System.out.println("\n📊 User Statistics:");
        System.out.println("-".repeat(30));
        System.out.println(stats);
        MenuUtil.pause("Statistics displayed.");
    }
    
    private void showSystemInformation() {
        System.out.println("\n🖥️ System Information:");
        System.out.println("-".repeat(50));
        
        // Display various system stats
        List<City> cities = adminService.getCityManager().getAllCities();
        List<Connection> connections = adminService.getCityManager().getAllConnections();
        List<FuelType> fuelTypes = adminService.getStationManager().getAllFuelTypes();
        List<ChargerType> chargerTypes = adminService.getStationManager().getAllChargerTypes();
        List<User> users = adminService.getUserManager().getAllUsers();
        
        System.out.println("Cities: " + cities.size());
        System.out.println("Connections: " + connections.size());
        System.out.println("Fuel Types: " + fuelTypes.size());
        System.out.println("Charger Types: " + chargerTypes.size());
        System.out.println("Total Users: " + users.size());
        
        MenuUtil.pause("System information displayed.");
    }
}
