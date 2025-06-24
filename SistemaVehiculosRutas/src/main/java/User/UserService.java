/*
 * Nombre del Archivo: UserService.java
 * 
 * Descripcion: Servicio principal para usuarios regulares del sistema. Coordina
 *              los servicios de vehículos y búsqueda de rutas, proporcionando
 *              acceso unificado a las funcionalidades específicas para usuarios
 *              no administradores. Actúa como punto central para todas las
 *              operaciones de usuario regular.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package User;

import Services.RouteService;
import Services.VehicleService;

/**
 * Servicio principal para usuarios regulares del sistema
 * 
 * Esta clase actúa como coordinador central para todas las funcionalidades
 * disponibles para usuarios regulares:
 * - Gestión de vehículos personales
 * - Búsqueda y planificación de rutas
 * - Acceso a servicios especializados por rol
 * 
 * Proporciona una interfaz unificada para las operaciones de usuario regular.
 */
public class UserService {
    
    private final VehicleService vehicleService;
    private final RouteSearchService routeSearchService;
    
    /**
     * Constructor del servicio de usuario regular
     * 
     * @param vehicleService Servicio de gestión de vehículos
     * @param routeService Servicio de rutas para búsqueda y planificación
     * 
     * Notas:
     * - Inicializa automáticamente el servicio de búsqueda de rutas
     * - Establece las dependencias necesarias para funcionalidades de usuario
     */
    public UserService(VehicleService vehicleService, RouteService routeService) {
        this.vehicleService = vehicleService;
        this.routeSearchService = new RouteSearchService(routeService, vehicleService);
    }
    
    /**
     * Obtiene el servicio de gestión de vehículos
     * 
     * @return El servicio de vehículos para operaciones de gestión
     */
    public VehicleService getVehicleService() {
        return vehicleService;
    }
    
    /**
     * Obtiene el servicio de búsqueda de rutas
     * 
     * @return El servicio especializado en búsqueda y planificación de rutas
     */
    public RouteSearchService getRouteSearchService() {
        return routeSearchService;
    }
}
