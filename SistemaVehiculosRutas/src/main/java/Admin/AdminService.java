/*
 * Nombre del Archivo: AdminService.java
 * 
 * Descripcion: Servicio principal de administración que coordina todos los servicios
 *              de gestión del sistema. Actúa como punto central para acceder a los
 *              servicios de ciudades, estaciones y usuarios, además de verificar
 *              privilegios de administrador.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Admin;

import Services.AuthenticationService;

/**
 * Servicio principal de administración del sistema
 * 
 * Esta clase actúa como coordinador central para todos los servicios de administración,
 * proporcionando acceso unificado a los servicios de gestión de ciudades, estaciones
 * y usuarios, además de funcionalidades de verificación de privilegios.
 */
public class AdminService {
    
    private final CityManagementService cityManager;
    private final StationManagementService stationManager;
    private final UserManagementService userManager;
    private final AuthenticationService authService;
    
    /**
     * Constructor del servicio de administración
     * 
     * @param cityManager Servicio de gestión de ciudades y conexiones
     * @param stationManager Servicio de gestión de estaciones y tipos de energía
     * @param userManager Servicio de gestión de usuarios
     * @param authService Servicio de autenticación para verificar privilegios
     */
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
     * Verifica si el usuario actual tiene privilegios de administrador
     * 
     * @return true si el usuario actual es administrador, false en caso contrario
     * 
     * Notas:
     * - Utiliza el servicio de autenticación para verificar el rol del usuario
     * - Es necesario para controlar el acceso a funcionalidades administrativas
     */
    public boolean hasAdminPrivileges() {
        return authService.isCurrentUserAdmin();
    }
    
    /**
     * Obtiene el servicio de gestión de ciudades
     * 
     * @return El servicio de gestión de ciudades y conexiones
     */
    public CityManagementService getCityManager() {
        return cityManager;
    }
    
    /**
     * Obtiene el servicio de gestión de estaciones
     * 
     * @return El servicio de gestión de estaciones y tipos de energía
     */
    public StationManagementService getStationManager() {
        return stationManager;
    }
    
    /**
     * Obtiene el servicio de gestión de usuarios
     * 
     * @return El servicio de gestión de usuarios del sistema
     */
    public UserManagementService getUserManager() {
        return userManager;
    }
}
