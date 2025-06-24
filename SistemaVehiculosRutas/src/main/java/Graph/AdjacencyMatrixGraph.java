/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Graph;

import Interfaces.Edge;
import Interfaces.Graph;
import Interfaces.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author JE
 */
public class AdjacencyMatrixGraph<T extends Node> implements Graph<T> {
    private final List<T> nodes;
    private final List<Edge> edges;
    private double[][] adjacencyMatrix;
    private final Map<String, Integer> nodeIndexMap;
    private static final double INFINITY = Double.MAX_VALUE;
    
    public AdjacencyMatrixGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.nodeIndexMap = new HashMap<>();
        this.adjacencyMatrix = new double[0][0];
    }
    
    @Override
    public boolean addNode(T node) {
        if (node == null || nodeIndexMap.containsKey(node.getId())) {
            return false;
        }
        
        int newIndex = nodes.size();
        nodes.add(node);
        nodeIndexMap.put(node.getId(), newIndex);
        
        // Resize adjacency matrix
        resizeMatrix(nodes.size());
        
        return true;
    }
    
    @Override
    public boolean addEdge(Edge edge) {
        if (edge == null || edges.contains(edge)) {
            return false;
        }
        
        Integer fromIndex = nodeIndexMap.get(edge.getFrom().getId());
        Integer toIndex = nodeIndexMap.get(edge.getTo().getId());
        
        if (fromIndex == null || toIndex == null) {
            return false; // One or both nodes not in graph
        }
        
        edges.add(edge);
        adjacencyMatrix[fromIndex][toIndex] = edge.getWeight();
        
        return true;
    }
    
    @Override
    public List<T> getNodes() {
        return new ArrayList<>(nodes);
    }
    
    @Override
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    @Override
    public T getNode(String id) {
        Integer index = nodeIndexMap.get(id);
        return index != null ? nodes.get(index) : null;
    }
    
    /**
     * Finds shortest path using Dijkstra's algorithm
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
     * Dijkstra's algorithm implementation
     */
    private List<T> dijkstra(int fromIndex, int toIndex) {
        int n = nodes.size();
        double[] distances = new double[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];
        
        // Initialize
        Arrays.fill(distances, INFINITY);
        Arrays.fill(previous, -1);
        distances[fromIndex] = 0;
        
        // Priority queue to get minimum distance node
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            Comparator.comparingDouble(i -> distances[i])
        );
        pq.offer(fromIndex);
        
        while (!pq.isEmpty()) {
            int current = pq.poll();
            
            if (visited[current]) continue;
            visited[current] = true;
            
            if (current == toIndex) break; // Found destination
            
            // Check all neighbors
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
        
        // Reconstruct path
        return reconstructPath(previous, fromIndex, toIndex);
    }
    
    /**
     * Reconstructs the path from Dijkstra's algorithm results
     */
    private List<T> reconstructPath(int[] previous, int fromIndex, int toIndex) {
        List<T> path = new ArrayList<>();
        
        if (previous[toIndex] == -1 && fromIndex != toIndex) {
            return path; // No path found
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
     * Finds all possible paths between two nodes (using DFS)
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
     * DFS to find all paths
     */
    private void dfsAllPaths(int current, int target, boolean[] visited, 
                           List<Integer> currentPath, List<List<T>> allPaths) {
        visited[current] = true;
        currentPath.add(current);
        
        if (current == target) {
            // Found a path, add it to results
            List<T> path = new ArrayList<>();
            for (int index : currentPath) {
                path.add(nodes.get(index));
            }
            allPaths.add(path);
        } else {
            // Continue searching
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
     * Gets direct connections from a node
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
     * Gets the edge between two nodes
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
     * Removes a node and all its connections
     */
    public boolean removeNode(T node) {
        Integer index = nodeIndexMap.get(node.getId());
        if (index == null) {
            return false;
        }
        
        // Remove all edges involving this node
        edges.removeIf(edge -> 
            edge.getFrom().equals(node) || edge.getTo().equals(node)
        );
        
        // Remove from nodes list and rebuild matrix
        nodes.remove(node);
        nodeIndexMap.remove(node.getId());
        rebuildNodeIndexMap();
        resizeMatrix(nodes.size());
        rebuildAdjacencyMatrix();
        
        return true;
    }
    
    /**
     * Removes an edge
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
     * Resizes the adjacency matrix
     */
    private void resizeMatrix(int newSize) {
        double[][] newMatrix = new double[newSize][newSize];
        
        // Copy existing values
        for (int i = 0; i < Math.min(adjacencyMatrix.length, newSize); i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, 
                           Math.min(adjacencyMatrix[i].length, newSize));
        }
        
        this.adjacencyMatrix = newMatrix;
    }
    
    /**
     * Rebuilds the node index map after removal
     */
    private void rebuildNodeIndexMap() {
        nodeIndexMap.clear();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i).getId(), i);
        }
    }
    
    /**
     * Rebuilds the adjacency matrix after node removal
     */
    private void rebuildAdjacencyMatrix() {
        // Clear matrix
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            Arrays.fill(adjacencyMatrix[i], 0);
        }
        
        // Rebuild from edges
        for (Edge edge : edges) {
            Integer fromIndex = nodeIndexMap.get(edge.getFrom().getId());
            Integer toIndex = nodeIndexMap.get(edge.getTo().getId());
            
            if (fromIndex != null && toIndex != null) {
                adjacencyMatrix[fromIndex][toIndex] = edge.getWeight();
            }
        }
    }
    
    /**
     * Checks if the graph is connected
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
     * DFS visit for connectivity check
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
     * Gets statistics about the graph
     */
    public String getStatistics() {
        return String.format("Graph Statistics: %d nodes, %d edges, Connected: %s", 
                           nodes.size(), edges.size(), isConnected());
    }
    
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