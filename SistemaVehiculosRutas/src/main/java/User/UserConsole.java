/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import Console.MenuUtil;
import Models.City;
import Models.Route;
import Models.Station;
import Vehicle.ChargerType;
import Vehicle.ElectricVehicle;
import Vehicle.FuelType;
import Vehicle.FuelVehicle;
import Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class UserConsole {
    
    private final UserService userService;
    
    public UserConsole(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Shows the main user menu
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
     * Shows the vehicle management menu
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
     * Shows the route planning menu
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
     * Shows the browsing menu
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
     * Shows the profile menu
     */
    private void showProfileMenu() {
        System.out.println("\n👤 My Profile");
        
        List<Vehicle> vehicles = userService.getVehicleService().getCurrentUserVehicles();
        Vehicle favorite = userService.getVehicleService().getCurrentUserFavoriteVehicle();
        
        System.out.println("Vehicles: " + vehicles.size());
        System.out.println("Favorite Vehicle: " + (favorite != null ? favorite.toString() : "None set"));
        
        MenuUtil.pause("Profile information displayed.");
    }
    
    // ========== VEHICLE MANAGEMENT IMPLEMENTATIONS ==========
    
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
    
    private FuelVehicle createFuelVehicle(String make, String model, int year, String licensePlate) {
        // For simplicity, we'll use a basic fuel type
        // In a full implementation, you'd let the user choose from available fuel types
        FuelType regularFuel = new FuelType("regular", "Regular Gasoline", "Octane 87");
        double tankCapacity = MenuUtil.getDoubleInput("Tank Capacity (L): ");
        
        return new FuelVehicle(make, model, year, licensePlate, regularFuel, tankCapacity);
    }
    
    private ElectricVehicle createElectricVehicle(String make, String model, int year, String licensePlate) {
        double batteryCapacity = MenuUtil.getDoubleInput("Battery Capacity (kWh): ");
        
        // For simplicity, we'll add a basic charger type
        // In a full implementation, you'd let the user choose from available charger types
        List<ChargerType> chargers = new ArrayList<>();
        chargers.add(new ChargerType("type2", "Type 2", "IEC 62196-2", 22));
        
        return new ElectricVehicle(make, model, year, licensePlate, chargers, batteryCapacity);
    }
    
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
    
    // ========== ROUTE PLANNING IMPLEMENTATIONS ==========
    
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
    
    // ========== BROWSING IMPLEMENTATIONS ==========
    
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
