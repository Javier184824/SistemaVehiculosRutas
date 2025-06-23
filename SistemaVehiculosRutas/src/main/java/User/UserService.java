/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import Services.RouteService;
import Services.VehicleService;

/**
 *
 * @author JE
 */
public class UserService {
    
    private final VehicleService vehicleService;
    private final RouteSearchService routeSearchService;
    
    public UserService(VehicleService vehicleService, RouteService routeService) {
        this.vehicleService = vehicleService;
        this.routeSearchService = new RouteSearchService(routeService, vehicleService);
    }
    
    public VehicleService getVehicleService() {
        return vehicleService;
    }
    
    public RouteSearchService getRouteSearchService() {
        return routeSearchService;
    }
}
