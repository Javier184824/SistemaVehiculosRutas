/*
 * Nombre del Archivo: AdjacencyMatrixGraph.java
 * 
 * Descripcion: Implementación de grafo usando matriz de adyacencia que proporciona
 *              funcionalidades completas para la gestión de rutas entre ciudades.
 *              Implementa algoritmos de búsqueda de rutas como Dijkstra y DFS,
 *              manejo de nodos y aristas, y análisis de conectividad del grafo.
 *              Utiliza una matriz de adyacencia para representar conexiones
 *              y un mapa de índices para acceso eficiente a nodos.
 * 
 * Nombre de los Integrantes:
 * Javier Lee Liang
 * Paulo César Herrera Arias
 * José Emilio Alvarado Mendez
 * Josué Santiago Hidalgo Sandoval
 */
package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import Interfaces.Edge;
import Interfaces.Graph;
import Interfaces.Node;

/**
 * Grafo implementado con matriz de adyacencia para sistema de rutas
 * 
 * Esta clase implementa la interfaz Graph usando una matriz de adyacencia
 * para representar conexiones entre nodos. Proporciona algoritmos de
 * búsqueda de rutas, gestión de nodos y aristas, y análisis de conectividad.
 * 
 * Características principales:
 * - Matriz de adyacencia para representar conexiones
 * - Mapa de índices para acceso eficiente a nodos
 * - Algoritmo de Dijkstra para rutas más cortas
 * - Búsqueda DFS para todas las rutas posibles
 * - Gestión completa de nodos y aristas
 * - Análisis de conectividad del grafo
 * 
 * Estructura de datos:
 * - Lista de nodos para mantener orden
 * - Lista de aristas para acceso directo
 * - Matriz de adyacencia para conexiones
 * - Mapa de ID de nodo a índice para búsqueda rápida
 * 
 * Algoritmos implementados:
 * - Dijkstra para ruta más corta
 * - DFS para todas las rutas posibles
 * - DFS para verificación de conectividad
 */
public class AdjacencyMatrixGraph<T extends Node> implements Graph<T> {
    private final List<T> nodes;
    private final List<Edge> edges;
    private double[][] adjacencyMatrix;
    private final Map<String, Integer> nodeIndexMap;
    private static final double INFINITY = Double.MAX_VALUE;
    
    /**
     * Constructor por defecto del grafo
     * 
     * Inicializa todas las estructuras de datos necesarias:
     * - Lista vacía de nodos
     * - Lista vacía de aristas
     * - Mapa vacío de índices de nodos
     * - Matriz de adyacencia vacía
     * 
     * Notas:
     * - El grafo comienza completamente vacío
     * - Las estructuras se redimensionan automáticamente
     * - El mapa de índices se mantiene sincronizado
     */
    public AdjacencyMatrixGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.nodeIndexMap = new HashMap<>();
        this.adjacencyMatrix = new double[0][0];
    }
    
    /**
     * Agrega un nodo al grafo
     * 
     * @param node Nodo a agregar al grafo
     * @return true si el nodo fue agregado exitosamente, false en caso contrario
     * 
     * Proceso:
     * - Valida que el nodo no sea null
     * - Verifica que el nodo no exista ya en el grafo
     * - Agrega el nodo a la lista de nodos
     * - Actualiza el mapa de índices
     * - Redimensiona la matriz de adyacencia
     * 
     * Validaciones:
     * - Nodo no puede ser null
     * - ID del nodo debe ser único
     * 
     * Notas:
     * - El índice del nodo será su posición en la lista
     * - La matriz se redimensiona automáticamente
     * - El mapa de índices se actualiza inmediatamente
     */
    @Override
    public boolean addNode(T node) {
        if (node == null || nodeIndexMap.containsKey(node.getId())) {
            return false;
        }
        
        int newIndex = nodes.size();
        nodes.add(node);
        nodeIndexMap.put(node.getId(), newIndex);
        
        // Redimensiona matriz de adyacencia
        resizeMatrix(nodes.size());
        
        return true;
    }
    
    /**
     * Agrega una arista al grafo
     * 
     * @param edge Arista a agregar al grafo
     * @return true si la arista fue agregada exitosamente, false en caso contrario
     * 
     * Notas:
     * - El peso se almacena en la matriz de adyacencia
     * - La arista se agrega a la lista para acceso directo
     * - Se verifica la existencia de ambos nodos antes de agregar
     */
    @Override
    public boolean addEdge(Edge edge) {
        if (edge == null || edges.contains(edge)) {
            return false;
        }
        
        Integer fromIndex = nodeIndexMap.get(edge.getFrom().getId());
        Integer toIndex = nodeIndexMap.get(edge.getTo().getId());
        
        if (fromIndex == null || toIndex == null) {
            return false; // Uno o ambos nodos no están en el grafo
        }
        
        edges.add(edge);
        adjacencyMatrix[fromIndex][toIndex] = edge.getWeight();
        
        return true;
    }
    
    /**
     * Obtiene todos los nodos del grafo
     * 
     * @return Lista copiada de todos los nodos en el grafo
     * 
     * Notas:
     * - Retorna una copia defensiva de la lista de nodos
     * - Los cambios en la lista retornada no afectan el grafo
     * - El orden de los nodos corresponde al orden de inserción
     */
    @Override
    public List<T> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    /**
     * Obtiene todas las aristas del grafo
     * 
     * @return Lista copiada de todas las aristas en el grafo
     * 
     * Notas:
     * - Retorna una copia defensiva de la lista de aristas
     * - Los cambios en la lista retornada no afectan el grafo
     * - El orden de las aristas corresponde al orden de inserción
     */
    @Override
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    /**
     * Obtiene un nodo por su ID
     * 
     * @param id ID del nodo a buscar
     * @return Nodo con el ID especificado, o null si no se encuentra
     * 
     * Proceso:
     * - Busca el índice del nodo en el mapa de índices
     * - Retorna el nodo en la posición correspondiente
     * 
     * Notas:
     * - Utiliza el mapa de índices para búsqueda O(1)
     * - Retorna null si el ID no existe en el grafo
     * - Útil para validar existencia de nodos
     */
    @Override
    public T getNode(String id) {
        Integer index = nodeIndexMap.get(id);
        return index != null ? nodes.get(index) : null;
    }
    
    /**
     * Encuentra la ruta más corta entre dos nodos usando el algoritmo de Dijkstra
     * 
     * @param from Nodo de origen
     * @param to Nodo de destino
     * @return Lista de nodos que forman la ruta más corta, o lista vacía si no hay ruta
     * 
     * Notas:
     * - Utiliza el algoritmo de Dijkstra para ruta más corta
     * - El peso de las aristas determina la "distancia"
     * - Retorna la ruta completa desde origen hasta destino
     */
    @Override
    public List<T> findPath(T from, T to) {
        if (from == null || to == null) {
            return new ArrayList<>();
        }
        
        Integer fromIndex = nodeIndexMap.get(from.getId());
        Integer toIndex = nodeIndexMap.get(to.getId());
        
        if (fromIndex == null || toIndex == null) {
            return new ArrayList<>();
        }
        
        return dijkstra(fromIndex, toIndex);
    }
    
    /**
     * Implementación del algoritmo de Dijkstra para ruta más corta
     * 
     * @param fromIndex Índice del nodo de origen
     * @param toIndex Índice del nodo de destino
     * @return Lista de nodos que forman la ruta más corta
     * 
     * Notas:
     * - Usa cola de prioridad para eficiencia
     * - Termina cuando encuentra el destino o explora todos los nodos
     * - Reconstruye la ruta usando el array de nodos anteriores
     */
    private List<T> dijkstra(int fromIndex, int toIndex) {
        int n = nodes.size();
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        
        // Inicialización
        Arrays.fill(distances, INFINITY);
        Arrays.fill(previous, -1);
        distances[fromIndex] = 0;
        
        // Cola de prioridad para obtener nodo con menor distancia
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            Comparator.comparingDouble(i -> distances[i])
        );
        pq.offer(fromIndex);
        
        while (!pq.isEmpty()) {
            int current = pq.poll();
            
            if (visited[current]) continue;
            visited[current] = true;
            
            if (current == toIndex) break; // Encontrado el destino
            
            // Revisa todos los vecinos
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (!visited[neighbor] && adjacencyMatrix[current][neighbor] != 0) {
                    double newDistance = distances[current] + adjacencyMatrix[current][neighbor];
                    
                    if (newDistance < distances[neighbor]) {
                        distances[neighbor] = newDistance;
                        previous[neighbor] = current;
                        pq.offer(neighbor);
                    }
                }
            }
        }
        
        // Reconstruye la ruta
        return reconstructPath(previous, fromIndex, toIndex);
    }
    
    /**
     * Reconstruye la ruta desde los resultados del algoritmo de Dijkstra
     * 
     * @param previous Array con nodos anteriores en la ruta
     * @param fromIndex Índice del nodo de origen
     * @param toIndex Índice del nodo de destino
     * @return Lista de nodos que forman la ruta
     * 
     * Proceso:
     * - Verifica si existe una ruta válida
     * - Reconstruye la ruta desde el destino hacia el origen
     * - Invierte la lista para obtener el orden correcto
     * - Convierte índices a nodos
     * 
     * Validaciones:
     * - Si no hay ruta (previous[toIndex] == -1), retorna lista vacía
     * - Si origen y destino son iguales, retorna lista con un nodo
     * 
     * Notas:
     * - La ruta se reconstruye en orden inverso
     * - Se invierte al final para obtener el orden correcto
     * - Convierte índices de la matriz a objetos Node
     */
    private List<T> reconstructPath(int[] previous, int fromIndex, int toIndex) {
        List<T> path = new ArrayList<>();
        
        if (previous[toIndex] == -1 && fromIndex != toIndex) {
            return path; // No se encontró ruta
        }
        
        List<Integer> indices = new ArrayList<>();
        int current = toIndex;
        
        while (current != -1) {
            indices.add(current);
            current = previous[current];
        }
        
        Collections.reverse(indices);
        
        for (int index : indices) {
            path.add(nodes.get(index));
        }
        
        return path;
    }
    
    /**
     * Encuentra todas las rutas posibles entre dos nodos usando DFS
     * 
     * @param from Nodo de origen
     * @param to Nodo de destino
     * @return Lista de todas las rutas posibles entre los nodos
     * 
     * Proceso:
     * - Obtiene los índices de los nodos en el grafo
     * - Verifica que ambos nodos existan en el grafo
     * - Ejecuta búsqueda DFS para encontrar todas las rutas
     * - Retorna todas las rutas encontradas
     * 
     * Validaciones:
     * - Ambos nodos deben existir en el grafo
     * - Si no hay rutas, retorna lista vacía
     * 
     * Notas:
     * - Usa DFS para explorar todas las rutas posibles
     * - Puede encontrar múltiples rutas entre los mismos nodos
     * - Útil para análisis de alternativas de ruta
     * - Complejidad exponencial en el peor caso
     */
    public List<List<T>> findAllPaths(T from, T to) {
        Integer fromIndex = nodeIndexMap.get(from.getId());
        Integer toIndex = nodeIndexMap.get(to.getId());
        
        if (fromIndex == null || toIndex == null) {
            return new ArrayList<>();
        }
        
        List<List<T>> allPaths = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[nodes.size()];
        
        dfsAllPaths(fromIndex, toIndex, visited, currentPath, allPaths);
        
        return allPaths;
    }
    
    /**
     * DFS para encontrar todas las rutas posibles
     * 
     * @param current Índice del nodo actual
     * @param target Índice del nodo objetivo
     * @param visited Array de nodos visitados
     * @param currentPath Ruta actual siendo explorada
     * @param allPaths Lista donde se almacenan todas las rutas encontradas
     * 
     * Algoritmo:
     * 1. Marca el nodo actual como visitado
     * 2. Agrega el nodo actual a la ruta
     * 3. Si llegó al objetivo, guarda la ruta encontrada
     * 4. Explora todos los vecinos no visitados
     * 5. Hace backtrack removiendo el nodo actual
     * 6. Desmarca el nodo como visitado para permitir otras rutas
     * 
     * Notas:
     * - Usa backtracking para explorar todas las posibilidades
     * - Permite visitar el mismo nodo en diferentes rutas
     * - La ruta se reconstruye al encontrar el objetivo
     * - Complejidad exponencial en el peor caso
     */
    private void dfsAllPaths(int current, int target, boolean[] visited, 
                           List<Integer> currentPath, List<List<T>> allPaths) {
        visited[current] = true;
        currentPath.add(current);
        
        if (current == target) {
            // Encontrada una ruta, agregarla a los resultados
            List<T> path = new ArrayList<>();
            for (int index : currentPath) {
                path.add(nodes.get(index));
            }
            allPaths.add(path);
        } else {
            // Continuar buscando
            for (int neighbor = 0; neighbor < nodes.size(); neighbor++) {
                if (!visited[neighbor] && adjacencyMatrix[current][neighbor] != 0) {
                    dfsAllPaths(neighbor, target, visited, currentPath, allPaths);
                }
            }
        }
        
        // Backtrack
        currentPath.remove(currentPath.size() - 1);
        visited[current] = false;
    }
    
    /**
     * Obtiene las conexiones directas desde un nodo
     * 
     * @param node Nodo del cual obtener las conexiones directas
     * @return Lista de nodos directamente conectados al nodo especificado
     * 
     * Proceso:
     * - Obtiene el índice del nodo en el grafo
     * - Revisa la fila correspondiente en la matriz de adyacencia
     * - Agrega todos los nodos con conexión no nula
     * 
     * Validaciones:
     * - Si el nodo no existe en el grafo, retorna lista vacía
     * 
     * Notas:
     * - Solo considera conexiones directas (un salto)
     * - Usa la matriz de adyacencia para eficiencia
     * - Retorna nodos, no aristas
     */
    public List<T> getDirectConnections(T node) {
        Integer index = nodeIndexMap.get(node.getId());
        if (index == null) {
            return new ArrayList<>();
        }
        
        List<T> connections = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (adjacencyMatrix[index][i] != 0) {
                connections.add(nodes.get(i));
            }
        }
        
        return connections;
    }
    
    /**
     * Obtiene la arista entre dos nodos
     * 
     * @param from Nodo de origen
     * @param to Nodo de destino
     * @return Arista entre los nodos, o null si no existe
     * 
     * Proceso:
     * - Busca en la lista de aristas
     * - Compara nodos origen y destino
     * - Retorna la primera arista que coincida
     * 
     * Notas:
     * - Busca en la lista completa de aristas
     * - Retorna null si no existe la conexión
     * - Útil para obtener información específica de la conexión
     */
    public Edge getEdge(T from, T to) {
        for (Edge edge : edges) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to)) {
                return edge;
            }
        }
        return null;
    }
    
    /**
     * Elimina un nodo y todas sus conexiones del grafo
     * 
     * @param node Nodo a eliminar del grafo
     * @return true si el nodo fue eliminado exitosamente, false en caso contrario
     * 
     * Proceso:
     * - Verifica que el nodo exista en el grafo
     * - Elimina todas las aristas que involucren al nodo
     * - Remueve el nodo de la lista de nodos
     * - Actualiza el mapa de índices
     * - Redimensiona la matriz de adyacencia
     * - Reconstruye la matriz con las aristas restantes
     * 
     * Validaciones:
     * - El nodo debe existir en el grafo
     * 
     * Notas:
     * - Elimina todas las conexiones entrantes y salientes
     * - Reconstruye completamente las estructuras de datos
     * - Mantiene la consistencia del grafo
     */
    public boolean removeNode(T node) {
        Integer index = nodeIndexMap.get(node.getId());
        if (index == null) {
            return false;
        }
        
        // Elimina todas las aristas que involucren este nodo
        edges.removeIf(edge -> 
            edge.getFrom().equals(node) || edge.getTo().equals(node)
        );
        
        // Remueve de la lista de nodos y reconstruye matriz
        nodes.remove(node);
        nodeIndexMap.remove(node.getId());
        rebuildNodeIndexMap();
        resizeMatrix(nodes.size());
        rebuildAdjacencyMatrix();
        
        return true;
    }
    
    /**
     * Elimina una arista del grafo
     * 
     * @param edge Arista a eliminar del grafo
     * @return true si la arista fue eliminada exitosamente, false en caso contrario
     * 
     * Proceso:
     * - Remueve la arista de la lista de aristas
     * - Obtiene los índices de los nodos origen y destino
     * - Actualiza la matriz de adyacencia (pone 0 en la posición)
     * 
     * Validaciones:
     * - La arista debe existir en el grafo
     * 
     * Notas:
     * - Solo elimina la arista específica
     * - Actualiza tanto la lista como la matriz
     * - Mantiene la consistencia de las estructuras
     */
    public boolean removeEdge(Edge edge) {
        if (!edges.remove(edge)) {
            return false;
        }
        
        Integer fromIndex = nodeIndexMap.get(edge.getFrom().getId());
        Integer toIndex = nodeIndexMap.get(edge.getTo().getId());
        
        if (fromIndex != null && toIndex != null) {
            adjacencyMatrix[fromIndex][toIndex] = 0;
        }
        
        return true;
    }
    
    /**
     * Redimensiona la matriz de adyacencia
     * 
     * @param newSize Nuevo tamaño de la matriz
     * 
     * Proceso:
     * - Crea una nueva matriz con el tamaño especificado
     * - Copia los valores existentes de la matriz anterior
     * - Reemplaza la matriz anterior con la nueva
     * 
     * Notas:
     * - Preserva los valores existentes durante el redimensionamiento
     * - La nueva matriz se inicializa con ceros
     * - Útil para agregar o remover nodos
     */
    private void resizeMatrix(int newSize) {
        double[][] newMatrix = new double[newSize][newSize];
        
        // Copia valores existentes
        for (int i = 0; i < Math.min(adjacencyMatrix.length, newSize); i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, 
                           Math.min(adjacencyMatrix[i].length, newSize));
        }
        
        this.adjacencyMatrix = newMatrix;
    }
    
    /**
     * Reconstruye el mapa de índices de nodos después de una eliminación
     * 
     * Proceso:
     * - Limpia el mapa actual
     * - Recorre la lista de nodos
     * - Asigna el índice correspondiente a cada nodo
     * 
     * Notas:
     * - Se ejecuta después de eliminar un nodo
     * - Mantiene la sincronización entre lista y mapa
     * - Los índices corresponden a las posiciones en la lista
     */
    private void rebuildNodeIndexMap() {
        nodeIndexMap.clear();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i).getId(), i);
        }
    }
    
    /**
     * Reconstruye la matriz de adyacencia después de eliminar un nodo
     * 
     * Proceso:
     * - Limpia toda la matriz (pone ceros)
     * - Recorre todas las aristas restantes
     * - Actualiza la matriz con los pesos de las aristas
     * 
     * Notas:
     * - Se ejecuta después de eliminar un nodo
     * - Reconstruye la matriz desde las aristas restantes
     * - Mantiene la consistencia de la representación
     */
    private void rebuildAdjacencyMatrix() {
        // Limpia matriz
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            Arrays.fill(adjacencyMatrix[i], 0);
        }
        
        // Reconstruye desde aristas
        for (Edge edge : edges) {
            Integer fromIndex = nodeIndexMap.get(edge.getFrom().getId());
            Integer toIndex = nodeIndexMap.get(edge.getTo().getId());
            
            if (fromIndex != null && toIndex != null) {
                adjacencyMatrix[fromIndex][toIndex] = edge.getWeight();
            }
        }
    }
    
    /**
     * Verifica si el grafo está conectado
     * 
     * @return true si el grafo está conectado, false en caso contrario
     * 
     * Proceso:
     * - Si el grafo está vacío, se considera conectado
     * - Ejecuta DFS desde el primer nodo
     * - Verifica que todos los nodos hayan sido visitados
     * 
     * Algoritmo:
     * - Usa DFS para explorar el grafo desde un nodo inicial
     * - Si todos los nodos son alcanzables, el grafo está conectado
     * 
     * Notas:
     * - Un grafo vacío se considera conectado
     * - Considera conexiones bidireccionales
     * - Útil para validar la integridad del grafo
     */
    public boolean isConnected() {
        if (nodes.isEmpty()) return true;
        
        boolean[] visited = new boolean[nodes.size()];
        dfsVisit(0, visited);
        
        for (boolean v : visited) {
            if (!v) return false;
        }
        
        return true;
    }
    
    /**
     * DFS para verificar conectividad del grafo
     * 
     * @param nodeIndex Índice del nodo actual
     * @param visited Array de nodos visitados
     * 
     * Proceso:
     * - Marca el nodo actual como visitado
     * - Explora todos los vecinos no visitados
     * - Considera conexiones bidireccionales
     * 
     * Notas:
     * - Usado para verificar conectividad
     * - Considera tanto conexiones salientes como entrantes
     * - Marca todos los nodos alcanzables
     */
    private void dfsVisit(int nodeIndex, boolean[] visited) {
        visited[nodeIndex] = true;
        
        for (int i = 0; i < nodes.size(); i++) {
            if (!visited[i] && (adjacencyMatrix[nodeIndex][i] != 0 || adjacencyMatrix[i][nodeIndex] != 0)) {
                dfsVisit(i, visited);
            }
        }
    }
    
    /**
     * Obtiene estadísticas del grafo
     * 
     * @return Cadena con información estadística del grafo
     * 
     * Información incluida:
     * - Número total de nodos
     * - Número total de aristas
     * - Estado de conectividad del grafo
     * 
     * Formato:
     * "Graph Statistics: X nodes, Y edges, Connected: true/false"
     * 
     * Notas:
     * - Útil para debugging y análisis del grafo
     * - Proporciona información resumida del estado
     */
    public String getStatistics() {
        return String.format("Graph Statistics: %d nodes, %d edges, Connected: %s", 
                           nodes.size(), edges.size(), isConnected());
    }
    
    /**
     * Genera una representación en cadena del grafo
     * 
     * @return Cadena con información detallada del grafo
     * 
     * Información incluida:
     * - Nombre de la clase
     * - Número de nodos y aristas
     * - Lista completa de nodos (si existen)
     * 
     * Formato:
     * - Encabezado con información básica
     * - Lista de nodos si el grafo no está vacío
     * - Estructura jerárquica clara
     * 
     * Notas:
     * - Útil para debugging y logging
     * - Muestra información completa del grafo
     * - Formato legible y estructurado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdjacencyMatrixGraph {\n");
        sb.append("  Nodes: ").append(nodes.size()).append("\n");
        sb.append("  Edges: ").append(edges.size()).append("\n");
        
        if (!nodes.isEmpty()) {
            sb.append("  Node list:\n");
            for (T node : nodes) {
                sb.append("    ").append(node.toString()).append("\n");
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
}