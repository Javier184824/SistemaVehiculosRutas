package vista;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import Models.City;
import Models.Connection;

public class MapaCiudadesFrame extends JFrame {
    public MapaCiudadesFrame(List<City> cities, List<Connection> connections) {
        setTitle("Mapa de Ciudades");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configuración para GraphStream
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Mapa de Ciudades");

        // Estilo visual atractivo para el grafo
        graph.setAttribute("ui.stylesheet",
            "node { " +
            "   fill-color: #3498db; " +
            "   size: 30px, 30px; " +
            "   text-size: 18; " +
            "   text-alignment: under; " +
            "   text-color: #222; " +
            "   stroke-mode: plain; " +
            "   stroke-color: #217dbb; " +
            "   shadow-mode: gradient-radial; " +
            "   shadow-color: #000, #0000; " +
            "   shadow-width: 4px; " +
            "   shadow-offset: 2px, 2px; " +
            "} " +
            "edge { " +
            "   fill-color: #2ecc71; " +
            "   size: 3px; " +
            "   text-size: 14; " +
            "   text-background-mode: rounded-box; " +
            "   text-background-color: #fff; " +
            "   text-color: #222; " +
            "   arrow-shape: arrow; " +
            "   arrow-size: 12px, 8px; " +
            "}");

        // Agregar nodos (ciudades)
        for (City city : cities) {
            graph.addNode(city.getId()).setAttribute("ui.label", city.getName());
        }

        // Agregar aristas (conexiones)
        for (Connection conn : connections) {
            City from = conn.getFromCity();
            City to = conn.getToCity();
            if (from != null && to != null) {
                String fromId = from.getId();
                String toId = to.getId();
                if (!fromId.equals(toId) && graph.getNode(fromId) != null && graph.getNode(toId) != null) {
                    // Normalizar el ID para evitar duplicados bidireccionales
                    String edgeId = fromId.compareTo(toId) < 0 ? fromId + "-" + toId : toId + "-" + fromId;
                    if (graph.getEdge(edgeId) == null) {
                        try {
                            graph.addEdge(edgeId, fromId, toId, false)
                                .setAttribute("ui.label", conn.getDistance() + " km");
                        } catch (org.graphstream.graph.EdgeRejectedException e) {
                            // Ignorar si la arista es rechazada
                        }
                    }
                }
            }
        }

        // Mostrar el grafo en el JFrame usando SwingViewer y ViewPanel
        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);
        this.add(viewPanel, BorderLayout.CENTER);
    }
} 