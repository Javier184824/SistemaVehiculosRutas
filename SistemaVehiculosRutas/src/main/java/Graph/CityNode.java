/*
 * Nombre del Archivo: CityNode.java
 * 
 * Descripcion: Nodo de ciudad que implementa la interfaz Node para representar
 *              ciudades en el grafo del sistema de rutas. Encapsula una instancia
 *              de City y proporciona métodos para acceder a sus propiedades
 *              como ID, nombre y ciudad subyacente. Implementa métodos de
 *              comparación y representación en cadena delegando a la ciudad
 *              encapsulada.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Graph;

import java.util.ArrayList;
import java.util.List;

import Interfaces.Node;
import Models.City;

/**
 * Nodo de ciudad para representación en el grafo del sistema
 * 
 * Esta clase implementa la interfaz Node y encapsula una instancia de City
 * para representar ciudades en el grafo de rutas. Proporciona acceso a las
 * propiedades de la ciudad y delega las operaciones de comparación y
 * representación a la ciudad subyacente.
 * 
 * Características principales:
 * - Encapsula una instancia de City
 * - Implementa la interfaz Node para compatibilidad con el grafo
 * - Delega operaciones a la ciudad subyacente
 * - No mantiene sus propias conexiones (responsabilidad del grafo)
 * - Proporciona métodos de comparación y representación
 * 
 * Notas:
 * - Los nodos no mantienen sus propias conexiones
 * - Las conexiones son gestionadas por el grafo
 * - La ciudad encapsulada es inmutable (final)
 */
public class CityNode implements Node {
    private final City city;
    
    /**
     * Constructor del nodo de ciudad
     * 
     * @param city Ciudad a encapsular en el nodo
     * 
     * Notas:
     * - La ciudad se almacena como referencia final
     * - No se permite ciudad null (debería validarse en el constructor)
     * - El nodo delega todas las operaciones a la ciudad
     */
    public CityNode(City city) {
        this.city = city;
    }
    
    /**
     * Obtiene la ciudad encapsulada en el nodo
     * 
     * @return Instancia de City encapsulada
     * 
     * Notas:
     * - Retorna la referencia directa a la ciudad
     * - Permite acceso completo a las propiedades de la ciudad
     * - Útil para operaciones específicas de ciudad
     */
    public City getCity() {
        return city;
    }
    
    /**
     * Obtiene el identificador único de la ciudad
     * 
     * @return ID de la ciudad como String
     * 
     * Proceso:
     * - Delega la operación al método getId() de la ciudad
     * 
     * Notas:
     * - Implementa el método de la interfaz Node
     * - El ID es único para cada ciudad
     * - Utilizado por el grafo para identificar nodos
     */
    @Override
    public String getId() {
        return city.getId();
    }
    
    /**
     * Obtiene el nombre de la ciudad
     * 
     * @return Nombre de la ciudad como String
     * 
     * Proceso:
     * - Delega la operación al método getName() de la ciudad
     * 
     * Notas:
     * - Implementa el método de la interfaz Node
     * - El nombre es legible para el usuario
     * - Utilizado para mostrar información al usuario
     */
    @Override
    public String getName() {
        return city.getName();
    }
    
    /**
     * Obtiene los nodos conectados (implementado por el grafo)
     * 
     * @return Lista vacía ya que los nodos no mantienen sus propias conexiones
     * 
     * Notas:
     * - Este método es implementado por el grafo, no por el nodo
     * - Los nodos no mantienen sus propias conexiones
     * - Retorna lista vacía para indicar que no hay conexiones directas
     * - Las conexiones se gestionan a nivel del grafo
     * 
     * Responsabilidad:
     * - La gestión de conexiones es responsabilidad del grafo
     * - Los nodos solo representan entidades (ciudades)
     */
    public List<Node> getConnectedNodes() {
        return new ArrayList<>();
    }
    
    /**
     * Compara este nodo con otro objeto para igualdad
     * 
     * @param obj Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     * 
     * Proceso:
     * - Verifica si es la misma referencia
     * - Verifica si el objeto es null o de clase diferente
     * - Compara las ciudades encapsuladas
     * 
     * Criterios de igualdad:
     * - Misma referencia de objeto
     * - Mismo tipo de clase (CityNode)
     * - Ciudades encapsuladas iguales
     * 
     * Notas:
     * - Delega la comparación a la ciudad encapsulada
     * - Utiliza el método equals de City
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityNode cityNode = (CityNode) obj;
        return city.equals(cityNode.city);
    }
    
    /**
     * Genera el código hash del nodo
     * 
     * @return Código hash basado en la ciudad encapsulada
     * 
     * Notas:
     * - Delega la operación a la ciudad encapsulada
     * - Utiliza el método hashCode de City
     * - Consistente con el método equals
     * - Útil para estructuras de datos basadas en hash
     */
    @Override
    public int hashCode() {
        return city.hashCode();
    }
    
    /**
     * Genera una representación en cadena del nodo
     * 
     * @return Representación en cadena de la ciudad encapsulada
     * 
     * Notas:
     * - Delega la operación a la ciudad encapsulada
     * - Utiliza el método toString de City
     * - Útil para debugging y logging
     * - Muestra información legible de la ciudad
     */
    @Override
    public String toString() {
        return city.toString();
    }
}