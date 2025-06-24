/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Admin.AdminService;
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
public class RouteSystemContext {
    
    private final DataManager dataManager;
    private final AuthenticationService authenticationService;
    private final RouteService routeService;
    private final StationService stationService;
    private final VehicleService vehicleService;
    private final AdminService adminService;
    private final UserService userService;
    
    public RouteSystemContext(DataManager dataManager,
                             AuthenticationService authenticationService,
                             RouteService routeService,
                             StationService stationService,
                             VehicleService vehicleService,
                             AdminService adminService,
                             UserService userService) {
        this.dataManager = dataManager;
        this.authenticationService = authenticationService;
        this.routeService = routeService;
        this.stationService = stationService;
        this.vehicleService = vehicleService;
        this.adminService = adminService;
        this.userService = userService;
    }
    
    // Getters for all services
    public DataManager getDataManager() { return dataManager; }
    public AuthenticationService getAuthenticationService() { return authenticationService; }
    public RouteService getRouteService() { return routeService; }
    public StationService getStationService() { return stationService; }
    public VehicleService getVehicleService() { return vehicleService; }
    public AdminService getAdminService() { return adminService; }
    public UserService getUserService() { return userService; }
    
    /**
     * Gets system information
     * @return formatted system info
     */
    public String getSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("🖥️ Route Planning System Status\n");
        info.append("================================\n");
        info.append("Data Directory: ").append(dataManager.toString()).append("\n");
        info.append("Authentication: ").append(authenticationService.isAuthenticated() ? "Active" : "None").append("\n");
        info.append("Graph Status: ").append(routeService.getGraphStatistics()).append("\n");
        
        return info.toString();
    }
}
