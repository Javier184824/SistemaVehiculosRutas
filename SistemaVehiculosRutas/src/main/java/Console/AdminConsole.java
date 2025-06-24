/*
 * Nombre del Archivo: AdminConsole.java
 * 
 * Descripcion: Consola de administración que proporciona una interfaz de usuario para
 *              gestionar ciudades, conexiones, estaciones, tipos de combustible y usuarios.
 *              Permite realizar operaciones CRUD completas a través de menús interactivos.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
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
 * Consola de administración del sistema
 * 
 * Esta clase proporciona una interfaz de usuario completa para administradores,
 * permitiendo gestionar todos los aspectos del sistema de rutas y vehículos.
 */
public class AdminConsole {
    
    private final AdminService adminService;
    
    /**
     * Constructor de la consola de administración
     * 
     * @param adminService El servicio de administración que maneja la lógica de negocio
     */
    public AdminConsole(AdminService adminService) {
        this.adminService = adminService;
    }
    
    /**
     * Muestra el menú principal de administración
     * 
     * Permite navegar entre las diferentes secciones del sistema:
     * - Gestión de ciudades y rutas
     * - Gestión de estaciones y tipos de energía
     * - Gestión de usuarios
     * - Información del sistema
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
     * Muestra el menú de gestión de ciudades y rutas
     * 
     * Permite realizar operaciones CRUD completas sobre ciudades y conexiones:
     * - Listar ciudades
     * - Crear ciudades
     * - Actualizar ciudades
     * - Eliminar ciudades
     * - Listar conexiones
     * - Crear conexiones
     * - Eliminar conexiones
     * - Reparar conexiones corruptas
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
     * Muestra el menú de gestión de estaciones y tipos de energía
     * 
     * Permite gestionar tipos de combustible y tipos de cargadores:
     * - Listar tipos de combustible
     * - Crear tipos de combustible
     * - Eliminar tipos de combustible
     * - Listar tipos de cargadores
     * - Crear tipos de cargadores
     * - Eliminar tipos de cargadores
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
     * Muestra el menú de gestión de usuarios
     * 
     * Permite gestionar usuarios del sistema:
     * - Listar usuarios
     * - Crear usuarios
     * - Cambiar rol de usuario
     * - Eliminar usuarios
     * - Ver estadísticas de usuarios
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
    
    // ========== IMPLEMENTACIONES DE GESTIÓN DE CIUDADES ==========
    
    /**
     * Lista todas las ciudades del sistema
     * 
     * Muestra una tabla formateada con todas las ciudades disponibles,
     * incluyendo nombre, coordenadas y número de estaciones.
     */
    private void listAllCities() {
        List<City> cities = adminService.getCityManager().getAllCities();
        
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities found.");
            return;
        }
        
        System.out.println("\n🏙️ All Cities:");
        System.out.println("-".repeat(80));
        System.out.printf("%-20s %-15s %-15s %-10s%n", "Name", "Latitude", "Longitude", "Stations");
        System.out.println("-".repeat(80));
        
        for (City city : cities) {
            System.out.printf("%-20s %-15.4f %-15.4f %-10d%n", 
                city.getName(), city.getLatitude(), city.getLongitude(), city.getStations().size());
        }
        
        MenuUtil.pause("City list displayed.");
    }
    
    /**
     * Crea una nueva ciudad en el sistema
     * 
     * Solicita al usuario los datos de la nueva ciudad:
     * - Nombre de la ciudad
     * - Latitud
     * - Longitud
     * 
     * Restricciones:
     * - El nombre no puede estar vacío
     * - No puede existir otra ciudad con el mismo nombre
     */
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
    
    /**
     * Actualiza una ciudad existente en el sistema
     * 
     * Permite al usuario seleccionar una ciudad y modificar:
     * - Nombre de la ciudad
     * - Coordenadas (latitud y longitud)
     * 
     * Restricciones:
     * - Debe existir al menos una ciudad para actualizar
     * - El nuevo nombre no puede estar vacío
     */
    private void updateCity() {
        List<City> cities = adminService.getCityManager().getAllCities();
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities available to update.");
            return;
        }
        
        // Mostrar ciudades con índices
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
        
        // Actualizar coordenadas si es necesario
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
    
    /**
     * Elimina una ciudad del sistema
     * 
     * Permite al usuario seleccionar una ciudad para eliminar.
     * Solo se puede eliminar si la ciudad no tiene conexiones existentes.
     * 
     * Restricciones:
     * - Debe existir al menos una ciudad para eliminar
     * - La ciudad no puede tener conexiones existentes
     */
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
    
    /**
     * Lista todas las conexiones del sistema
     * 
     * Muestra una tabla formateada con todas las conexiones disponibles,
     * incluyendo ciudades de origen y destino, distancia, tiempo y costo.
     */
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
    
    /**
     * Crea una nueva conexión entre ciudades
     * 
     * Permite al usuario seleccionar dos ciudades y definir:
     * - Distancia en kilómetros
     * - Tiempo de viaje en minutos
     * - Costo del viaje
     * 
     * Restricciones:
     * - Deben existir al menos 2 ciudades
     * - Las ciudades de origen y destino deben ser diferentes
     * - No puede existir una conexión idéntica entre las mismas ciudades
     */
    private void createNewConnection() {
        List<City> cities = adminService.getCityManager().getAllCities();
        if (cities.size() < 2) {
            MenuUtil.showError("Need at least 2 cities to create connections.");
            return;
        }
        
        System.out.println("\n🔗 Create New Connection");
        
        // Seleccionar ciudad de origen
        System.out.println("From City:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, cities.get(i).getName());
        }
        int fromIndex = MenuUtil.getIntInput("Select from city: ") - 1;
        if (fromIndex < 0 || fromIndex >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        // Seleccionar ciudad de destino
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
    
    /**
     * Elimina una conexión del sistema
     * 
     * Permite al usuario seleccionar una conexión para eliminar.
     * Muestra información detallada de la conexión antes de confirmar la eliminación.
     * 
     * Restricciones:
     * - Debe existir al menos una conexión para eliminar
     * - La conexión debe existir en el sistema
     */
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
    
    /**
     * Repara conexiones corruptas en el sistema
     * 
     * Elimina todas las conexiones existentes y recrea las conexiones por defecto
     * entre las principales ciudades del sistema.
     * 
     * Restricciones:
     * - Debe haber al menos 6 ciudades en el sistema
     * - Elimina y recrea todas las conexiones existentes
     */
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
    
    // ========== IMPLEMENTACIONES DE GESTIÓN DE ESTACIONES ==========
    
    /**
     * Lista todos los tipos de combustible disponibles
     * 
     * Muestra una lista formateada de todos los tipos de combustible
     * con su nombre y descripción.
     */
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
    
    /**
     * Crea un nuevo tipo de combustible
     * 
     * Solicita al usuario los datos del nuevo tipo de combustible:
     * - ID del tipo de combustible
     * - Nombre
     * - Descripción
     * 
     * Restricciones:
     * - El ID no puede estar vacío
     * - No puede existir otro tipo de combustible con el mismo ID
     */
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
    
    /**
     * Elimina un tipo de combustible del sistema
     * 
     * Permite al usuario seleccionar un tipo de combustible para eliminar.
     * 
     * Restricciones:
     * - Debe existir al menos un tipo de combustible para eliminar
     */
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
    
    /**
     * Lista todos los tipos de cargadores disponibles
     * 
     * Muestra una tabla formateada de todos los tipos de cargadores
     * con su nombre, estándar y potencia máxima.
     */
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
    
    /**
     * Crea un nuevo tipo de cargador
     * 
     * Solicita al usuario los datos del nuevo tipo de cargador:
     * - ID del tipo de cargador
     * - Nombre
     * - Estándar
     * - Potencia máxima en kW
     * 
     * Restricciones:
     * - El ID no puede estar vacío
     * - No puede existir otro tipo de cargador con el mismo ID
     * - La potencia máxima debe ser un número positivo
     */
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
    
    /**
     * Elimina un tipo de cargador del sistema
     * 
     * Permite al usuario seleccionar un tipo de cargador para eliminar.
     * 
     * Restricciones:
     * - Debe existir al menos un tipo de cargador para eliminar
     */
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
    
    // ========== IMPLEMENTACIONES DE GESTIÓN DE USUARIOS ==========
    
    /**
     * Lista todos los usuarios del sistema
     * 
     * Muestra una tabla formateada de todos los usuarios
     * con su nombre de usuario, rol y número de vehículos.
     */
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
    
    /**
     * Crea un nuevo usuario en el sistema
     * 
     * Solicita al usuario los datos del nuevo usuario:
     * - Nombre de usuario
     * - Contraseña
     * - Rol (Admin o User)
     * 
     * Restricciones:
     * - El nombre de usuario no puede estar vacío
     * - La contraseña no puede estar vacía
     * - No puede existir otro usuario con el mismo nombre de usuario
     */
    private void createNewUser() {
        System.out.println("\n👤 Create New User");
        
        String username = MenuUtil.getNonEmptyStringInput("Username: ");
        String password = MenuUtil.getNonEmptyStringInput("Password: ");
        
        System.out.println("Role:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        int roleChoice = MenuUtil.getIntInput("Select role: ");
        
        UserRole role;
        switch (roleChoice) {
            case 1 -> role = UserRole.ADMIN;
            case 2 -> role = UserRole.USER;
            default -> {
                MenuUtil.showError("Invalid role selection.");
                return;
            }
        }
        
        User newUser = new User(username, password, role);
        
        if (adminService.getUserManager().createUser(newUser)) {
            MenuUtil.showSuccess("User '" + username + "' created successfully!");
        } else {
            MenuUtil.showError("Failed to create user. Username may already exist.");
        }
    }
    
    /**
     * Cambia el rol de un usuario existente
     * 
     * Permite al usuario seleccionar un usuario y cambiar su rol
     * entre Admin y User.
     * 
     * Restricciones:
     * - Debe existir al menos un usuario para modificar
     * - El usuario debe existir en el sistema
     */
    private void changeUserRole() {
        List<User> users = adminService.getUserManager().getAllUsers();
        if (users.isEmpty()) {
            MenuUtil.showInfo("No users available to modify.");
            return;
        }
        
        System.out.println("\n🔄 Select User to Change Role:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, user.getUsername(), user.getRole().getDisplayName());
        }
        
        int index = MenuUtil.getIntInput("Select user (number): ") - 1;
        if (index < 0 || index >= users.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        User userToModify = users.get(index);
        
        System.out.println("New Role:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        int roleChoice = MenuUtil.getIntInput("Select new role: ");
        
        UserRole newRole;
        switch (roleChoice) {
            case 1 -> newRole = UserRole.ADMIN;
            case 2 -> newRole = UserRole.USER;
            default -> {
                MenuUtil.showError("Invalid role selection.");
                return;
            }
        }
        
        userToModify.setRole(newRole);
        
        if (adminService.getUserManager().updateUser(userToModify.getId(), userToModify)) {
            MenuUtil.showSuccess("User role changed successfully!");
        } else {
            MenuUtil.showError("Failed to change user role.");
        }
    }
    
    /**
     * Elimina un usuario del sistema
     * 
     * Permite al usuario seleccionar un usuario para eliminar.
     * 
     * Restricciones:
     * - Debe existir al menos un usuario para eliminar
     * - El usuario debe existir en el sistema
     */
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
    
    /**
     * Muestra estadísticas de usuarios del sistema
     * 
     * Presenta información resumida sobre los usuarios:
     * - Total de usuarios
     * - Distribución por roles
     * - Otros datos relevantes
     */
    private void showUserStatistics() {
        String stats = adminService.getUserManager().getUserStatistics();
        System.out.println("\n📊 User Statistics:");
        System.out.println("-".repeat(30));
        System.out.println(stats);
        MenuUtil.pause("Statistics displayed.");
    }
    
    /**
     * Muestra información general del sistema
     * 
     * Presenta estadísticas del sistema incluyendo:
     * - Número de ciudades
     * - Número de conexiones
     * - Número de tipos de combustible
     * - Número de tipos de cargadores
     * - Número total de usuarios
     */
    private void showSystemInformation() {
        System.out.println("\n🖥️ System Information:");
        System.out.println("-".repeat(50));
        
        // Mostrar varias estadísticas del sistema
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
