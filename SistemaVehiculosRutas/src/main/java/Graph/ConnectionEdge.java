/*
 * Nombre del Archivo: ConnectionEdge.java
 * 
 * Descripcion: Arista de conexión que implementa la interfaz Edge para representar
 *              conexiones entre ciudades en el grafo del sistema de rutas.
 *              Encapsula una instancia de Connection y proporciona métodos para
 *              acceder a sus propiedades como nodos origen y destino, peso,
 *              distancia, tiempo y costo. Implementa métodos de comparación
 *              y representación en cadena delegando a la conexión encapsulada.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Graph;

import Interfaces.Edge;
import Interfaces.Node;
import Models.Connection;

/**
 * Arista de conexión para representación en el grafo del sistema
 * 
 * Esta clase implementa la interfaz Edge y encapsula una instancia de Connection
 * para representar conexiones entre ciudades en el grafo de rutas. Proporciona
 * acceso a las propiedades de la conexión y crea nodos de ciudad dinámicamente
 * para los nodos origen y destino.
 * 
 * Características principales:
 * - Encapsula una instancia de Connection
 * - Implementa la interfaz Edge para compatibilidad con el grafo
 * - Crea CityNode dinámicamente para nodos origen y destino
 * - Delega operaciones a la conexión subyacente
 * - Proporciona acceso a peso, distancia, tiempo y costo
 * 
 * Notas:
 * - La conexión encapsulada es inmutable (final)
 * - Los nodos se crean dinámicamente a partir de las ciudades
 * - Delega comparaciones y representación a la conexión
 */
public class ConnectionEdge implements Edge {
    private final Connection connection;
    
    /**
     * Constructor de la arista de conexión
     * 
     * @param connection Conexión a encapsular en la arista
     * 
     * Notas:
     * - La conexión se almacena como referencia final
     * - No se permite conexión null (debería validarse en el constructor)
     * - La arista delega todas las operaciones a la conexión
     */
    public ConnectionEdge(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Obtiene la conexión encapsulada en la arista
     * 
     * @return Instancia de Connection encapsulada
     * 
     * Notas:
     * - Retorna la referencia directa a la conexión
     * - Permite acceso completo a las propiedades de la conexión
     * - Útil para operaciones específicas de conexión
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Obtiene el nodo de origen de la conexión
     * 
     * @return CityNode creado a partir de la ciudad de origen
     * 
     * Proceso:
     * - Obtiene la ciudad de origen de la conexión
     * - Crea un nuevo CityNode con esa ciudad
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - Crea un nuevo CityNode cada vez que se llama
     * - El nodo representa la ciudad de origen
     */
    @Override
    public Node getFrom() {
        return new CityNode(connection.getFromCity());
    }
    
    /**
     * Obtiene el nodo de destino de la conexión
     * 
     * @return CityNode creado a partir de la ciudad de destino
     * 
     * Proceso:
     * - Obtiene la ciudad de destino de la conexión
     * - Crea un nuevo CityNode con esa ciudad
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - Crea un nuevo CityNode cada vez que se llama
     * - El nodo representa la ciudad de destino
     */
    @Override
    public Node getTo() {
        return new CityNode(connection.getToCity());
    }
    
    /**
     * Obtiene el peso de la conexión para algoritmos de grafo
     * 
     * @return Peso de la conexión como double
     * 
     * Proceso:
     * - Delega la operación al método getWeight() de la conexión
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - El peso se utiliza en algoritmos de ruta más corta
     * - Generalmente representa la distancia o costo
     */
    @Override
    public double getWeight() {
        return connection.getWeight();
    }
    
    /**
     * Obtiene la distancia física de la conexión
     * 
     * @return Distancia en kilómetros como double
     * 
     * Proceso:
     * - Delega la operación al método getDistance() de la conexión
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - La distancia representa kilómetros entre ciudades
     * - Utilizada para cálculos de ruta y estadísticas
     */
    @Override
    public double getDistance() {
        return connection.getDistance();
    }
    
    /**
     * Obtiene el tiempo estimado de viaje
     * 
     * @return Tiempo en minutos como int
     * 
     * Proceso:
     * - Delega la operación al método getTimeMinutes() de la conexión
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - El tiempo representa minutos estimados de viaje
     * - Considera condiciones de tráfico y tipo de ruta
     */
    @Override
    public int getTimeMinutes() {
        return connection.getTimeMinutes();
    }
    
    /**
     * Obtiene el costo estimado del viaje
     * 
     * @return Costo en unidades monetarias como double
     * 
     * Proceso:
     * - Delega la operación al método getCost() de la conexión
     * 
     * Notas:
     * - Implementa el método de la interfaz Edge
     * - El costo representa el precio estimado del viaje
     * - Incluye combustible, peajes y otros gastos
     */
    @Override
    public double getCost() {
        return connection.getCost();
    }
    
    /**
     * Compara esta arista con otro objeto para igualdad
     * 
     * @param obj Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     * 
     * Proceso:
     * - Verifica si es la misma referencia
     * - Verifica si el objeto es null o de clase diferente
     * - Compara las conexiones encapsuladas
     * 
     * Criterios de igualdad:
     * - Misma referencia de objeto
     * - Mismo tipo de clase (ConnectionEdge)
     * - Conexiones encapsuladas iguales
     * 
     * Notas:
     * - Delega la comparación a la conexión encapsulada
     * - Utiliza el método equals de Connection
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConnectionEdge that = (ConnectionEdge) obj;
        return connection.equals(that.connection);
    }
    
    /**
     * Genera el código hash de la arista
     * 
     * @return Código hash basado en la conexión encapsulada
     * 
     * Notas:
     * - Delega la operación a la conexión encapsulada
     * - Utiliza el método hashCode de Connection
     * - Consistente con el método equals
     * - Útil para estructuras de datos basadas en hash
     */
    @Override
    public int hashCode() {
        return connection.hashCode();
    }
    
    /**
     * Genera una representación en cadena de la arista
     * 
     * @return Representación en cadena de la conexión encapsulada
     * 
     * Notas:
     * - Delega la operación a la conexión encapsulada
     * - Utiliza el método toString de Connection
     * - Útil para debugging y logging
     * - Muestra información legible de la conexión
     */
    @Override
    public String toString() {
        return connection.toString();
    }
}