/*
 * Nombre del Archivo: VehicleService.java
 * 
 * Descripcion: Servicio de gestión de vehículos para usuarios del sistema.
 *              Proporciona funcionalidades para agregar, eliminar, actualizar
 *              y gestionar vehículos personales de usuarios, incluyendo la
 *              configuración de vehículos favoritos. Integra con el servicio
 *              de autenticación para operaciones seguras por usuario.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Services;

import java.util.List;

import Interfaces.DataManager;
import User.User;
import Vehicle.Vehicle;

/**
 * Servicio de gestión de vehículos para usuarios
 * 
 * Esta clase proporciona funcionalidades completas para la gestión de vehículos:
 * - Operaciones CRUD para vehículos personales
 * - Gestión de vehículos favoritos
 * - Búsqueda de vehículos por ID
 * - Integración con autenticación de usuarios
 * 
 * Todas las operaciones están asociadas al usuario autenticado actual.
 */
public class VehicleService {
    
    private final DataManager dataManager;
    private final AuthenticationService authService;
    
    /**
     * Constructor del servicio de vehículos
     * 
     * @param dataManager Gestor de datos para operaciones de persistencia
     * @param authService Servicio de autenticación para gestión de usuarios
     * 
     * Notas:
     * - Establece las dependencias necesarias para gestión de vehículos
     * - Permite integración con sistema de autenticación
     */
    public VehicleService(DataManager dataManager, AuthenticationService authService) {
        this.dataManager = dataManager;
        this.authService = authService;
    }
    
    /**
     * Agrega un vehículo al usuario actual
     * 
     * @param vehicle El vehículo a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     * 
     * Validaciones:
     * - Verifica que el usuario actual no sea null
     * - Verifica que el vehículo no sea null
     * - Actualiza automáticamente la información del usuario
     */
    public boolean addVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicle == null) {
            return false;
        }
        
        currentUser.addVehicle(vehicle);
        return authService.updateCurrentUser(currentUser);
    }
    
    /**
     * Elimina un vehículo del usuario actual
     * 
     * @param vehicle El vehículo a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     * 
     * Validaciones:
     * - Verifica que el usuario actual no sea null
     * - Verifica que el vehículo no sea null
     * - Actualiza automáticamente la información del usuario
     * - Maneja la eliminación del vehículo favorito si es necesario
     */
    public boolean removeVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicle == null) {
            return false;
        }
        
        boolean removed = currentUser.removeVehicle(vehicle);
        if (removed) {
            return authService.updateCurrentUser(currentUser);
        }
        
        return false;
    }
    
    /**
     * Actualiza un vehículo del usuario actual
     * 
     * @param vehicleId El ID del vehículo a actualizar
     * @param updatedVehicle La información actualizada del vehículo
     * @return true si se actualizó exitosamente, false en caso contrario
     * 
     * Proceso:
     * - Busca el vehículo por ID en la lista del usuario
     * - Reemplaza la información del vehículo
     * - Actualiza automáticamente la información del usuario
     */
    public boolean updateVehicle(String vehicleId, Vehicle updatedVehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null || vehicleId == null || updatedVehicle == null) {
            return false;
        }
        
        List<Vehicle> vehicles = currentUser.getVehicles();
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicleId.equals(vehicles.get(i).getId())) {
                vehicles.set(i, updatedVehicle);
                return authService.updateCurrentUser(currentUser);
            }
        }
        
        return false;
    }
    
    /**
     * Establece un vehículo como favorito para el usuario actual
     * 
     * @param vehicle El vehículo a establecer como favorito
     * @return true si se estableció exitosamente, false en caso contrario
     * 
     * Validaciones:
     * - Verifica que el usuario actual no sea null
     * - Verifica que el vehículo pertenezca al usuario
     * - Permite establecer null para quitar el favorito
     * - Actualiza automáticamente la configuración del usuario
     */
    public boolean setFavoriteVehicle(Vehicle vehicle) {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        // Verificar que el vehículo pertenezca al usuario
        if (vehicle != null && !currentUser.getVehicles().contains(vehicle)) {
            return false;
        }
        
        currentUser.setFavoriteVehicle(vehicle);
        return authService.updateCurrentUser(currentUser);
    }
    
    /**
     * Obtiene todos los vehículos del usuario actual
     * 
     * @return Lista de vehículos del usuario, o lista vacía si no hay usuario
     * 
     * Notas:
     * - Retorna lista vacía si no hay usuario autenticado
     * - Proporciona acceso directo a la flota personal del usuario
     */
    public List<Vehicle> getCurrentUserVehicles() {
        User currentUser = authService.getCurrentUser();
        return currentUser != null ? currentUser.getVehicles() : List.of();
    }
    
    /**
     * Obtiene el vehículo favorito del usuario actual
     * 
     * @return El vehículo favorito, o null si no está configurado
     * 
     * Notas:
     * - Retorna null si no hay usuario autenticado
     * - Retorna null si no hay vehículo favorito configurado
     * - Útil para operaciones por defecto en planificación de rutas
     */
    public Vehicle getCurrentUserFavoriteVehicle() {
        User currentUser = authService.getCurrentUser();
        return currentUser != null ? currentUser.getFavoriteVehicle() : null;
    }
    
    /**
     * Busca un vehículo por ID para el usuario actual
     * 
     * @param vehicleId El ID del vehículo a buscar
     * @return El vehículo encontrado, o null si no existe
     * 
     * Notas:
     * - Busca solo en los vehículos del usuario actual
     * - Utiliza stream para búsqueda eficiente
     * - Retorna null si no se encuentra el vehículo
     */
    public Vehicle findVehicleById(String vehicleId) {
        List<Vehicle> vehicles = getCurrentUserVehicles();
        return vehicles.stream()
            .filter(v -> vehicleId.equals(v.getId()))
            .findFirst()
            .orElse(null);
    }
}
