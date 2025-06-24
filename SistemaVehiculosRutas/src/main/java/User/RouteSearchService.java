/*
 * Nombre del Archivo: RouteSearchService.java
 * 
 * Descripcion: Servicio especializado en búsqueda y análisis de rutas para usuarios
 *              regulares. Proporciona funcionalidades para encontrar rutas entre
 *              ciudades, obtener estaciones compatibles con vehículos específicos,
 *              y generar información detallada de rutas. Integra servicios de
 *              rutas y vehículos para ofrecer una experiencia completa de
 *              planificación de viajes.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package User;

import java.util.List;

import Models.City;
import Models.Route;
import Models.Station;
import Services.RouteService;
import Services.VehicleService;
import Vehicle.Vehicle;

/**
 * Servicio de búsqueda y análisis de rutas para usuarios
 * 
 * Esta clase proporciona funcionalidades especializadas para la búsqueda
 * y análisis de rutas, incluyendo:
 * - Búsqueda de rutas entre ciudades
 * - Identificación de estaciones compatibles con vehículos
 * - Generación de información detallada de rutas
 * - Gestión de vehículos del usuario actual
 * 
 * Actúa como intermediario entre los servicios de rutas y vehículos.
 */
public class RouteSearchService {
    
    private final RouteService routeService;
    private final VehicleService vehicleService;
    
    /**
     * Constructor del servicio de búsqueda de rutas
     * 
     * @param routeService Servicio de rutas para operaciones de búsqueda
     * @param vehicleService Servicio de vehículos para compatibilidad
     * 
     * Notas:
     * - Establece las dependencias necesarias para búsqueda de rutas
     * - Permite integración entre servicios de rutas y vehículos
     */
    public RouteSearchService(RouteService routeService, VehicleService vehicleService) {
        this.routeService = routeService;
        this.vehicleService = vehicleService;
    }
    
    /**
     * Encuentra rutas disponibles entre dos ciudades
     * 
     * @param fromCityId ID de la ciudad de origen
     * @param toCityId ID de la ciudad de destino
     * @return Lista de rutas disponibles entre las ciudades
     * 
     * Notas:
     * - Utiliza el servicio de rutas para la búsqueda
     * - Retorna todas las rutas disponibles entre las ciudades especificadas
     */
    public List<Route> findRoutes(String fromCityId, String toCityId) {
        return routeService.findRoutes(fromCityId, toCityId);
    }
    
    /**
     * Obtiene todas las ciudades disponibles para planificación de rutas
     * 
     * @return Lista de todas las ciudades del sistema
     * 
     * Notas:
     * - Proporciona acceso a todas las ciudades para selección de origen/destino
     * - Utilizado para mostrar opciones en interfaces de usuario
     */
    public List<City> getAllCities() {
        return routeService.getAllCities();
    }
    
    /**
     * Obtiene estaciones compatibles en una ruta para el vehículo favorito del usuario
     * 
     * @param route La ruta a analizar
     * @return Lista de estaciones compatibles, o lista vacía si no hay vehículo favorito
     * 
     * Notas:
     * - Utiliza automáticamente el vehículo favorito del usuario actual
     * - Retorna lista vacía si el usuario no tiene vehículo favorito configurado
     * - Filtra estaciones según la compatibilidad con el vehículo
     */
    public List<Station> getCompatibleStationsOnRoute(Route route) {
        Vehicle favoriteVehicle = vehicleService.getCurrentUserFavoriteVehicle();
        if (favoriteVehicle == null) {
            return List.of();
        }
        
        return routeService.getCompatibleStationsOnRoute(route, favoriteVehicle);
    }
    
    /**
     * Obtiene estaciones compatibles en una ruta para un vehículo específico
     * 
     * @param route La ruta a analizar
     * @param vehicle El vehículo para verificar compatibilidad
     * @return Lista de estaciones compatibles con el vehículo especificado
     * 
     * Notas:
     * - Permite especificar un vehículo particular para el análisis
     * - Filtra estaciones según el tipo y características del vehículo
     * - Útil para comparar diferentes vehículos en la misma ruta
     */
    public List<Station> getCompatibleStationsOnRoute(Route route, Vehicle vehicle) {
        return routeService.getCompatibleStationsOnRoute(route, vehicle);
    }
    
    /**
     * Obtiene todas las estaciones en una ciudad específica
     * 
     * @param cityId ID de la ciudad
     * @return Lista de estaciones en la ciudad especificada
     * 
     * Notas:
     * - Proporciona información sobre infraestructura de la ciudad
     * - Útil para planificación de paradas intermedias
     */
    public List<Station> getStationsInCity(String cityId) {
        return routeService.getStationsInCity(cityId);
    }
    
    /**
     * Obtiene los vehículos del usuario actual para planificación de rutas
     * 
     * @return Lista de vehículos del usuario actual
     * 
     * Notas:
     * - Proporciona acceso a la flota personal del usuario
     * - Utilizado para selección de vehículo en planificación de rutas
     */
    public List<Vehicle> getCurrentUserVehicles() {
        return vehicleService.getCurrentUserVehicles();
    }
    
    /**
     * Obtiene el vehículo favorito del usuario actual
     * 
     * @return El vehículo favorito, o null si no está configurado
     * 
     * Notas:
     * - Utilizado como vehículo por defecto en búsquedas de rutas
     * - Facilita la experiencia del usuario al evitar selección repetitiva
     */
    public Vehicle getCurrentUserFavoriteVehicle() {
        return vehicleService.getCurrentUserFavoriteVehicle();
    }
    
    /**
     * Busca una ciudad por nombre (insensible a mayúsculas/minúsculas)
     * 
     * @param cityName El nombre de la ciudad a buscar
     * @return La ciudad encontrada, o null si no existe
     * 
     * Notas:
     * - Búsqueda insensible a mayúsculas y minúsculas
     * - Utiliza stream para búsqueda eficiente
     * - Útil para búsquedas por nombre en interfaces de usuario
     */
    public City findCityByName(String cityName) {
        return getAllCities().stream()
            .filter(city -> city.getName().equalsIgnoreCase(cityName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Obtiene información detallada de una ruta incluyendo estaciones compatibles
     * 
     * @param route La ruta para obtener información detallada
     * @param vehicle El vehículo para análisis de compatibilidad (opcional, usa favorito si es null)
     * @return Información formateada de la ruta con detalles completos
     * 
     * Información incluida:
     * - Detalles básicos de la ruta (origen, destino, distancia, tiempo, costo)
     * - Ruta paso a paso con ciudades intermedias
     * - Estaciones compatibles con el vehículo seleccionado
     * - Formato visual mejorado para lectura
     */
    public String getDetailedRouteInfo(Route route, Vehicle vehicle) {
        if (route == null) {
            return "No route information available.";
        }
        
        Vehicle selectedVehicle = vehicle != null ? vehicle : getCurrentUserFavoriteVehicle();
        
        StringBuilder info = new StringBuilder();
        info.append("🛣️ Route Details:\n");
        info.append("═".repeat(60)).append("\n");
        info.append(String.format("From: %s → To: %s\n", 
            route.getOrigin().getName(), route.getDestination().getName()));
        info.append(String.format("Distance: %.1f km\n", route.getTotalDistance()));
        info.append(String.format("Time: %s\n", route.getFormattedTotalTime()));
        info.append(String.format("Cost: $%.2f\n", route.getTotalCost()));
        
        // Mostrar ruta paso a paso
        info.append("\n📍 Route Path:\n");
        List<City> cities = route.getCities();
        for (int i = 0; i < cities.size(); i++) {
            info.append(String.format("%d. %s", i + 1, cities.get(i).getName()));
            if (i < cities.size() - 1) {
                info.append(" → ");
            }
            info.append("\n");
        }
        
        // Mostrar estaciones compatibles si hay vehículo seleccionado
        if (selectedVehicle != null) {
            List<Station> compatibleStations = getCompatibleStationsOnRoute(route, selectedVehicle);
            
            info.append(String.format("\n⛽ Compatible Stations for %s:\n", selectedVehicle.toString()));
            if (compatibleStations.isEmpty()) {
                info.append("No compatible stations found on this route.\n");
            } else {
                for (Station station : compatibleStations) {
                    info.append(String.format("• %s in %s (%s)\n", 
                        station.getName(), station.getCity().getName(), station.getStationType()));
                }
            }
        }
        
        return info.toString();
    }
}
