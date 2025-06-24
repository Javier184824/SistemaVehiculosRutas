/*
 * Nombre del Archivo: UserConsole.java
 * 
 * Descripcion: Consola de usuario regular del sistema de planificación de rutas.
 *              Proporciona una interfaz completa de consola para usuarios no
 *              administradores, incluyendo gestión de vehículos personales,
 *              planificación de rutas, exploración de ciudades y estaciones,
 *              y gestión de perfil personal. Ofrece una experiencia de usuario
 *              intuitiva y completa para todas las funcionalidades de usuario regular.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package User;

import java.util.ArrayList;
import java.util.List;

import Console.MenuUtil;
import Models.City;
import Models.Route;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.ElectricVehicle;
import Vehicle.FuelType;
import Vehicle.FuelVehicle;
import Vehicle.Vehicle;

/**
 * Consola de usuario regular del sistema
 * 
 * Esta clase proporciona una interfaz completa de consola para usuarios regulares,
 * incluyendo:
 * - Gestión completa de vehículos personales
 * - Planificación y búsqueda de rutas
 * - Exploración de ciudades y estaciones
 * - Gestión de perfil personal
 * - Interfaz intuitiva con menús organizados
 * 
 * Proporciona acceso a todas las funcionalidades disponibles para usuarios no administradores.
 */
public class UserConsole {
    
    private final UserService userService;
    
    /**
     * Constructor de la consola de usuario regular
     * 
     * @param userService Servicio de usuario para acceder a funcionalidades
     * 
     * Notas:
     * - Establece la dependencia del servicio de usuario
     * - Permite acceso a todas las funcionalidades de usuario regular
     */
    public UserConsole(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Muestra el menú principal del usuario
     * 
     * Características:
     * - Bucle principal de navegación para usuarios regulares
     * - Menú con opciones organizadas por categorías
     * - Navegación fluida entre diferentes funcionalidades
     * - Opción de salida integrada
     */
    public void showUserMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🚗 User Dashboard",
                "Manage My Vehicles",
                "Plan a Route",
                "Browse Cities & Stations",
                "My Profile"
            );
            
            switch (choice) {
                case 0 -> showVehicleManagementMenu();
                case 1 -> showRoutePlanningMenu();
                case 2 -> showBrowsingMenu();
                case 3 -> showProfileMenu();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Muestra el menú de gestión de vehículos
     * 
     * Funcionalidades disponibles:
     * - Listar vehículos personales
     * - Agregar nuevos vehículos
     * - Eliminar vehículos existentes
     * - Establecer vehículo favorito
     * - Ver información del vehículo favorito
     */
    private void showVehicleManagementMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🚙 Vehicle Management",
                "List My Vehicles",
                "Add New Vehicle",
                "Remove Vehicle",
                "Set Favorite Vehicle",
                "View Favorite Vehicle"
            );
            
            switch (choice) {
                case 0 -> listMyVehicles();
                case 1 -> addNewVehicle();
                case 2 -> removeVehicle();
                case 3 -> setFavoriteVehicle();
                case 4 -> viewFavoriteVehicle();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Muestra el menú de planificación de rutas
     * 
     * Funcionalidades disponibles:
     * - Buscar rutas con vehículo favorito
     * - Buscar rutas con vehículo específico
     * - Ver todas las ciudades disponibles
     */
    private void showRoutePlanningMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🗺️ Route Planning",
                "Find Routes",
                "Find Routes with Specific Vehicle",
                "View All Cities"
            );
            
            switch (choice) {
                case 0 -> findRoutes(null);
                case 1 -> findRoutesWithVehicle();
                case 2 -> viewAllCities();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Muestra el menú de exploración de ciudades y estaciones
     * 
     * Funcionalidades disponibles:
     * - Ver todas las ciudades del sistema
     * - Ver estaciones en una ciudad específica
     * - Buscar estaciones compatibles con vehículos
     */
    private void showBrowsingMenu() {
        boolean running = true;
        while (running) {
            int choice = MenuUtil.displayMenu(
                "🏙️ Browse Cities & Stations",
                "View All Cities",
                "View Stations in City",
                "Search Compatible Stations"
            );
            
            switch (choice) {
                case 0 -> viewAllCities();
                case 1 -> viewStationsInCity();
                case 2 -> searchCompatibleStations();
                case -1 -> running = false;
                default -> MenuUtil.showError("Invalid option.");
            }
        }
    }
    
    /**
     * Muestra el menú de perfil personal
     * 
     * Información mostrada:
     * - Número total de vehículos del usuario
     * - Vehículo favorito actual (si está configurado)
     * - Resumen de la flota personal
     */
    private void showProfileMenu() {
        System.out.println("\n👤 My Profile");
        
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        Vehicle favorite = userService.getVehicleService().getCurrentUserFavoriteVehicle();
        
        System.out.println("Vehicles: " + vehicles.size());
        System.out.println("Favorite Vehicle: " + (favorite != null ? favorite.toString() : "None set"));
        
        MenuUtil.pause("Profile information displayed.");
    }
    
    // ========== IMPLEMENTACIONES DE GESTIÓN DE VEHÍCULOS ==========
    
    /**
     * Lista todos los vehículos del usuario actual
     * 
     * Características:
     * - Muestra información detallada de cada vehículo
     * - Indica el vehículo favorito con icono especial
     * - Incluye información específica por tipo de vehículo
     * - Maneja casos de usuario sin vehículos
     */
    private void listMyVehicles() {
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        
        if (vehicles.isEmpty()) {
            MenuUtil.showInfo("You don't have any vehicles yet. Add one to get started!");
            return;
        }
        
        System.out.println("\n🚗 Your Vehicles:");
        System.out.println("-".repeat(80));
        
        Vehicle favorite = userService.getVehicleService().getCurrentUserFavoriteVehicle();
        
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            String favoriteMarker = vehicle.equals(favorite) ? " ⭐" : "";
            System.out.printf("%d. %s%s\n", i + 1, vehicle.toString(), favoriteMarker);
            
            if (vehicle instanceof FuelVehicle) {
                FuelVehicle fuelVehicle = (FuelVehicle) vehicle;
                System.out.printf("   Tank: %.1f L, Fuel: %s\n", 
                    fuelVehicle.getTankCapacity(),
                    fuelVehicle.getFuelType() != null ? fuelVehicle.getFuelType().getName() : "Unknown");
            } else if (vehicle instanceof ElectricVehicle) {
                ElectricVehicle electricVehicle = (ElectricVehicle) vehicle;
                System.out.printf("   Battery: %.1f kWh, Chargers: %d types\n",
                    electricVehicle.getBatteryCapacity(),
                    electricVehicle.getSupportedChargers().size());
            }
        }
        
        MenuUtil.pause("Vehicle list displayed.");
    }
    
    /**
     * Permite al usuario agregar un nuevo vehículo
     * 
     * Proceso:
     * - Solicita información básica del vehículo
     * - Permite seleccionar tipo (combustible o eléctrico)
     * - Solicita información específica según el tipo
     * - Agrega el vehículo a la flota personal
     * - Maneja errores de entrada
     */
    private void addNewVehicle() {
        System.out.println("\n➕ Add New Vehicle");
        
        String make = MenuUtil.getNonEmptyStringInput("Make: ");
        String model = MenuUtil.getNonEmptyStringInput("Model: ");
        int year = MenuUtil.getIntInput("Year: ");
        String licensePlate = MenuUtil.getStringInput("License Plate: ");
        
        System.out.println("Vehicle Type:");
        System.out.println("1. Fuel Vehicle");
        System.out.println("2. Electric Vehicle");
        int typeChoice = MenuUtil.getIntInput("Select type: ");
        
        Vehicle newVehicle;
        
        if (typeChoice == 1) {
            newVehicle = createFuelVehicle(make, model, year, licensePlate);
        } else if (typeChoice == 2) {
            newVehicle = createElectricVehicle(make, model, year, licensePlate);
        } else {
            MenuUtil.showError("Invalid vehicle type.");
            return;
        }
        
        if (newVehicle != null && userService.getVehicleService().addVehicle(newVehicle)) {
            MenuUtil.showSuccess("Vehicle added successfully!");
            
            if (MenuUtil.getConfirmation("Set this as your favorite vehicle?")) {
                userService.getVehicleService().setFavoriteVehicle(newVehicle);
            }
        } else {
            MenuUtil.showError("Failed to add vehicle.");
        }
    }
    
    /**
     * Crea un vehículo de combustible con información específica
     * 
     * @param make Marca del vehículo
     * @param model Modelo del vehículo
     * @param year Año del vehículo
     * @param licensePlate Placa del vehículo
     * @return El vehículo de combustible creado, o null si hay error
     * 
     * Proceso:
     * - Solicita capacidad del tanque
     * - Permite seleccionar tipo de combustible
     * - Crea y retorna el vehículo configurado
     */
    private FuelVehicle createFuelVehicle(String make, String model, int year, String licensePlate) {
        // For simplicity, we'll use a basic fuel type
        // In a full implementation, you'd let the user choose from available fuel types
        FuelType regularFuel = new FuelType("regular", "Regular Gasoline", "Octane 87");
        double tankCapacity = MenuUtil.getDoubleInput("Tank Capacity (L): ");
        
        return new FuelVehicle(make, model, year, licensePlate, regularFuel, tankCapacity);
    }
    
    /**
     * Crea un vehículo eléctrico con información específica
     * 
     * @param make Marca del vehículo
     * @param model Modelo del vehículo
     * @param year Año del vehículo
     * @param licensePlate Placa del vehículo
     * @return El vehículo eléctrico creado, o null si hay error
     * 
     * Proceso:
     * - Solicita capacidad de batería
     * - Permite seleccionar tipos de cargadores compatibles
     * - Crea y retorna el vehículo configurado
     */
    private ElectricVehicle createElectricVehicle(String make, String model, int year, String licensePlate) {
        double batteryCapacity = MenuUtil.getDoubleInput("Battery Capacity (kWh): ");
        
        // For simplicity, we'll add a basic charger type
        // In a full implementation, you'd let the user choose from available charger types
        List<ChargerType> chargers = new ArrayList<>();
        chargers.add(new ChargerType("type2", "Type 2", "IEC 62196-2", 22));
        
        return new ElectricVehicle(make, model, year, licensePlate, chargers, batteryCapacity);
    }
    
    /**
     * Permite al usuario eliminar un vehículo de su flota
     * 
     * Proceso:
     * - Lista los vehículos disponibles
     * - Permite seleccionar el vehículo a eliminar
     * - Solicita confirmación antes de eliminar
     * - Maneja la eliminación del vehículo favorito si es necesario
     */
    private void removeVehicle() {
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        
        if (vehicles.isEmpty()) {
            MenuUtil.showInfo("You don't have any vehicles to remove.");
            return;
        }
        
        System.out.println("\n🗑️ Select Vehicle to Remove:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, vehicles.get(i).toString());
        }
        
        int index = MenuUtil.getIntInput("Select vehicle (number): ") - 1;
        if (index < 0 || index >= vehicles.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        Vehicle vehicleToRemove = vehicles.get(index);
        
        if (!MenuUtil.getConfirmation("Are you sure you want to remove this vehicle?")) {
            return;
        }
        
        if (userService.getVehicleService().removeVehicle(vehicleToRemove)) {
            MenuUtil.showSuccess("Vehicle removed successfully!");
        } else {
            MenuUtil.showError("Failed to remove vehicle.");
        }
    }
    
    /**
     * Permite al usuario establecer un vehículo favorito
     * 
     * Proceso:
     * - Lista los vehículos disponibles
     * - Permite seleccionar el nuevo vehículo favorito
     * - Actualiza la configuración del usuario
     * - Confirma el cambio exitoso
     */
    private void setFavoriteVehicle() {
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        
        if (vehicles.isEmpty()) {
            MenuUtil.showInfo("You don't have any vehicles. Add one first!");
            return;
        }
        
        System.out.println("\n⭐ Select Favorite Vehicle:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, vehicles.get(i).toString());
        }
        System.out.println("0. Clear favorite (no favorite vehicle)");
        
        int index = MenuUtil.getIntInput("Select vehicle (number): ");
        
        if (index == 0) {
            if (userService.getVehicleService().setFavoriteVehicle(null)) {
                MenuUtil.showSuccess("Favorite vehicle cleared!");
            } else {
                MenuUtil.showError("Failed to clear favorite vehicle.");
            }
        } else if (index > 0 && index <= vehicles.size()) {
            Vehicle selectedVehicle = vehicles.get(index - 1);
            
            if (userService.getVehicleService().setFavoriteVehicle(selectedVehicle)) {
                MenuUtil.showSuccess("Favorite vehicle set to: " + selectedVehicle.toString());
            } else {
                MenuUtil.showError("Failed to set favorite vehicle.");
            }
        } else {
            MenuUtil.showError("Invalid selection.");
        }
    }
    
    /**
     * Muestra información detallada del vehículo favorito actual
     * 
     * Información mostrada:
     * - Detalles completos del vehículo favorito
     * - Información específica según el tipo de vehículo
     * - Mensaje informativo si no hay vehículo favorito configurado
     */
    private void viewFavoriteVehicle() {
        Vehicle favorite = userService.getVehicleService().getCurrentUserFavoriteVehicle();
        
        if (favorite == null) {
            MenuUtil.showInfo("You haven't set a favorite vehicle yet.");
            return;
        }
        
        System.out.println("\n⭐ Your Favorite Vehicle:");
        System.out.println("-".repeat(40));
        System.out.println("Vehicle: " + favorite.toString());
        System.out.println("Energy Type: " + favorite.getEnergyType());
        
        if (favorite instanceof FuelVehicle) {
            FuelVehicle fuelVehicle = (FuelVehicle) favorite;
            System.out.println("Tank Capacity: " + fuelVehicle.getTankCapacity() + " L");
            if (fuelVehicle.getFuelType() != null) {
                System.out.println("Fuel Type: " + fuelVehicle.getFuelType().getName());
            }
        } else if (favorite instanceof ElectricVehicle) {
            ElectricVehicle electricVehicle = (ElectricVehicle) favorite;
            System.out.println("Battery Capacity: " + electricVehicle.getBatteryCapacity() + " kWh");
            System.out.println("Supported Chargers: " + electricVehicle.getSupportedChargers().size());
        }
        
        MenuUtil.pause("Favorite vehicle information displayed.");
    }
    
    // ========== IMPLEMENTACIONES DE PLANIFICACIÓN DE RUTAS ==========
    
    /**
     * Busca rutas entre ciudades con un vehículo específico
     * 
     * @param specificVehicle Vehículo específico para la búsqueda, o null para usar favorito
     * 
     * Proceso:
     * - Solicita ciudades de origen y destino
     * - Busca rutas disponibles
     * - Muestra información detallada de cada ruta
     * - Incluye estaciones compatibles si hay vehículo seleccionado
     */
    private void findRoutes(Vehicle specificVehicle) {
        List<City> cities = userService.getRouteSearchService().getAllCities();
        
        if (cities.size() < 2) {
            MenuUtil.showError("Need at least 2 cities for route planning.");
            return;
        }
        
        System.out.println("\n🗺️ Route Planning");
        
        // Select origin city
        System.out.println("Origin City:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, cities.get(i).getName());
        }
        int fromIndex = MenuUtil.getIntInput("Select origin: ") - 1;
        if (fromIndex < 0 || fromIndex >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        // Select destination city
        System.out.println("Destination City:");
        for (int i = 0; i < cities.size(); i++) {
            if (i != fromIndex) {
                System.out.printf("%d. %s\n", i + 1, cities.get(i).getName());
            }
        }
        int toIndex = MenuUtil.getIntInput("Select destination: ") - 1;
        if (toIndex < 0 || toIndex >= cities.size() || toIndex == fromIndex) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        City fromCity = cities.get(fromIndex);
        City toCity = cities.get(toIndex);
        
        List<Route> routes = userService.getRouteSearchService().findRoutes(fromCity.getId(), toCity.getId());
        
        if (routes.isEmpty()) {
            MenuUtil.showError("No routes found between " + fromCity.getName() + " and " + toCity.getName());
            return;
        }
        
        System.out.println("\n📍 Available Routes:");
        System.out.println("═".repeat(80));
        
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            System.out.printf("%d. %s\n", i + 1, route.toString());
        }
        
        int routeChoice = MenuUtil.getIntInput("Select route for details (0 to skip): ");
        if (routeChoice > 0 && routeChoice <= routes.size()) {
            Route selectedRoute = routes.get(routeChoice - 1);
            String routeDetails = userService.getRouteSearchService().getDetailedRouteInfo(selectedRoute, specificVehicle);
            System.out.println("\n" + routeDetails);
        }
        
        MenuUtil.pause("Route planning completed.");
    }
    
    /**
     * Busca rutas con un vehículo específico seleccionado por el usuario
     * 
     * Proceso:
     * - Permite seleccionar un vehículo específico de la flota
     * - Busca rutas con el vehículo seleccionado
     * - Muestra información detallada incluyendo estaciones compatibles
     */
    private void findRoutesWithVehicle() {
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        
        if (vehicles.isEmpty()) {
            MenuUtil.showError("You need to add vehicles first to plan routes with specific vehicles.");
            return;
        }
        
        System.out.println("\n🚗 Select Vehicle for Route Planning:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, vehicles.get(i).toString());
        }
        
        int vehicleIndex = MenuUtil.getIntInput("Select vehicle: ") - 1;
        if (vehicleIndex < 0 || vehicleIndex >= vehicles.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        Vehicle selectedVehicle = vehicles.get(vehicleIndex);
        findRoutes(selectedVehicle);
    }
    
    /**
     * Muestra todas las ciudades disponibles en el sistema
     * 
     * Información mostrada:
     * - Lista completa de ciudades
     * - Número total de ciudades
     * - Información básica de cada ciudad
     */
    private void viewAllCities() {
        List<City> cities = userService.getRouteSearchService().getAllCities();
        
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities found.");
            return;
        }
        
        System.out.println("\n🏙️ All Cities:");
        System.out.println("-".repeat(60));
        System.out.printf("%-30s %-10s%n", "City Name", "Stations");
        System.out.println("-".repeat(60));
        
        for (City city : cities) {
            System.out.printf("%-30s %-10d%n", city.getName(), city.getStations().size());
        }
        
        MenuUtil.pause("City list displayed.");
    }
    
    // ========== IMPLEMENTACIONES DE EXPLORACIÓN ==========
    
    /**
     * Muestra las estaciones en una ciudad específica
     * 
     * Proceso:
     * - Permite seleccionar una ciudad
     * - Muestra todas las estaciones en esa ciudad
     * - Incluye información detallada de cada estación
     * - Maneja casos de ciudades sin estaciones
     */
    private void viewStationsInCity() {
        List<City> cities = userService.getRouteSearchService().getAllCities();
        
        if (cities.isEmpty()) {
            MenuUtil.showInfo("No cities found.");
            return;
        }
        
        System.out.println("\n🏙️ Select City to View Stations:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.printf("%d. %s (%d stations)\n", i + 1, 
                cities.get(i).getName(), cities.get(i).getStations().size());
        }
        
        int index = MenuUtil.getIntInput("Select city: ") - 1;
        if (index < 0 || index >= cities.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        City selectedCity = cities.get(index);
        List<Station> stations = selectedCity.getStations();
        
        if (stations.isEmpty()) {
            MenuUtil.showInfo("No stations found in " + selectedCity.getName());
            return;
        }
        
        System.out.println("\n⛽ Stations in " + selectedCity.getName() + ":");
        System.out.println("-".repeat(60));
        
        for (Station station : stations) {
            System.out.println("• " + station.getName() + " (" + station.getStationType() + ")");
            System.out.println("  Address: " + station.getAddress());
        }
        
        MenuUtil.pause("Station list displayed.");
    }
    
    /**
     * Busca estaciones compatibles con los vehículos del usuario
     * 
     * Proceso:
     * - Permite seleccionar un vehículo de la flota
     * - Busca estaciones compatibles en todo el sistema
     * - Muestra información detallada de estaciones compatibles
     * - Maneja casos de vehículos sin estaciones compatibles
     */
    private void searchCompatibleStations() {
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        
        if (vehicles.isEmpty()) {
            MenuUtil.showError("You need to add vehicles first to search for compatible stations.");
            return;
        }
        
        System.out.println("\n🔍 Select Vehicle to Find Compatible Stations:");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, vehicles.get(i).toString());
        }
        
        int vehicleIndex = MenuUtil.getIntInput("Select vehicle: ") - 1;
        if (vehicleIndex < 0 || vehicleIndex >= vehicles.size()) {
            MenuUtil.showError("Invalid selection.");
            return;
        }
        
        Vehicle selectedVehicle = vehicles.get(vehicleIndex);
        List<City> cities = userService.getRouteSearchService().getAllCities();
        
        System.out.println("\n⛽ Compatible Stations for " + selectedVehicle.toString() + ":");
        System.out.println("-".repeat(80));
        
        boolean foundCompatible = false;
        
        for (City city : cities) {
            List<Station> compatibleStations = city.getStations().stream().filter(selectedVehicle::isCompatibleWith).toList();
            
            if (!compatibleStations.isEmpty()) {
                foundCompatible = true;
                System.out.println("\n📍 " + city.getName() + ":");
                for (Station station : compatibleStations) {
                    System.out.println("  • " + station.getName() + " (" + station.getStationType() + ")");
                }
            }
        }
        
        if (!foundCompatible) {
            MenuUtil.showInfo("No compatible stations found for this vehicle type.");
        }
        
        MenuUtil.pause("Compatible stations search completed.");
    }
}
