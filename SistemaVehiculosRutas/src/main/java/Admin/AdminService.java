/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Services.AuthenticationService;

/**
 *
 * @author JE
 */
public class AdminService {
    
    private final CityManagementService cityManager;
    private final StationManagementService stationManager;
    private final UserManagementService userManager;
    private final AuthenticationService authService;
    
    public AdminService(CityManagementService cityManager, 
                       StationManagementService stationManager,
                       UserManagementService userManager,
                       AuthenticationService authService) {
        this.cityManager = cityManager;
        this.stationManager = stationManager;
        this.userManager = userManager;
        this.authService = authService;
    }
    
    /**
     * Checks if the current user has admin privileges
     * @return true if current user is admin
     */
    public boolean hasAdminPrivileges() {
        return authService.isCurrentUserAdmin();
    }
    
    public CityManagementService getCityManager() {
        return cityManager;
    }
    
    public StationManagementService getStationManager() {
        return stationManager;
    }
    
    public UserManagementService getUserManager() {
        return userManager;
    }
}
