/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Admin.AdminService;
import Admin.CityManagementService;
import Admin.UserManagementService;
import Main.RouteSystemContext;
import Models.City;
import Models.Connection;
import User.User;
import User.UserRole;
import User.UserService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author javie
 */
public class MenuAdmin extends javax.swing.JFrame {
    
    private RouteSystemContext context;
    
    /**
     * Creates new form MenuAdmin
     */
    public MenuAdmin(RouteSystemContext context) {
        this.context = context;
        initComponents();
        configurarTablaUsuarios();
        configurarTablaCiudades();
        configurarComboBoxesCiudades();
        cargarUsuarios();
        cargarCiudades();
    }
    
    /**
     * Configura la tabla de usuarios con las columnas apropiadas
     */
    private void configurarTablaUsuarios() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Usuario");
        model.addColumn("Rol");
        model.addColumn("Vehículos");
        usuariosTable.setModel(model);
        
        // Hacer que la tabla no sea editable
        usuariosTable.setDefaultEditor(Object.class, null);
    }
    
    /**
     * Configura la tabla de ciudades con las columnas apropiadas
     */
    private void configurarTablaCiudades() {
        // La tabla se configura dinámicamente según lo que se quiera mostrar
        // (ciudades o conexiones), por lo que no necesitamos configuración inicial
    }
    
    /**
     * Configura los combo boxes de ciudades para origen y destino
     */
    private void configurarComboBoxesCiudades() {
        jComboBox1.setModel(new DefaultComboBoxModel<>());
        jComboBox2.setModel(new DefaultComboBoxModel<>());
    }
    
    /**
     * Carga todas las ciudades en la tabla y combo boxes
     */
    public void cargarCiudades() {
        try {
            CityManagementService cityManager = context.getAdminService().getCityManager();
            List<City> ciudades = cityManager.getAllCities();
            
            // Configurar tabla para mostrar ciudades
            DefaultTableModel tablaContenido = new DefaultTableModel();
            tablaContenido.addColumn("Ciudad");
            tablaContenido.addColumn("Latitud");
            tablaContenido.addColumn("Longitud");
            tablaContenido.addColumn("Estaciones");
            
            if (ciudades.isEmpty()) {
                tablaContenido.addRow(new Object[] {
                    "No hay ciudades registradas",
                    "",
                    "",
                    ""
                });
            } else {
                for (City ciudad : ciudades) {
                    tablaContenido.addRow(new Object[] {
                        ciudad.getName(),
                        String.format("%.4f", ciudad.getLatitude()),
                        String.format("%.4f", ciudad.getLongitude()),
                        ciudad.getStations().size()
                    });
                }
            }
            
            // Actualizar tabla
            jTable1.setModel(tablaContenido);
            jTable1.setDefaultEditor(Object.class, null); // Hacer no editable
            
            // Cargar combo boxes
            cargarComboBoxesCiudades(ciudades);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar ciudades: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga las ciudades en los combo boxes de origen y destino
     */
    private void cargarComboBoxesCiudades(List<City> ciudades) {
        DefaultComboBoxModel<String> modeloOrigen = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modeloDestino = new DefaultComboBoxModel<>();
        
        // Agregar opción por defecto
        modeloOrigen.addElement("Seleccionar ciudad origen");
        modeloDestino.addElement("Seleccionar ciudad destino");
        
        // Agregar todas las ciudades
        for (City ciudad : ciudades) {
            modeloOrigen.addElement(ciudad.getName());
            modeloDestino.addElement(ciudad.getName());
        }
        
        jComboBox1.setModel(modeloOrigen);
        jComboBox2.setModel(modeloDestino);
    }
    
    /**
     * Carga todos los usuarios en la tabla
     */
    public void cargarUsuarios() {
        try {
            UserManagementService servicioManejoUsuarios = context.getAdminService().getUserManager();
            List<User> usuarios = servicioManejoUsuarios.getAllUsers();
            
            DefaultTableModel tablaContenido = (DefaultTableModel) usuariosTable.getModel();
            tablaContenido.setRowCount(0); // Limpiar tabla
            
            for (User usuario : usuarios) {
                tablaContenido.addRow(new Object[] {
                    usuario.getUsername(),
                    usuario.getRole().getDisplayName(),
                    usuario.getVehicles().size()
                });
            }
            
            // Actualizar el estado del botón eliminar
            actualizarEstadoBotonEliminar();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar usuarios: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza el estado del botón eliminar según si hay usuarios seleccionados
     */
    private void actualizarEstadoBotonEliminar() {
        int filaSeleccionada = usuariosTable.getSelectedRow();
        eliminarUsuarioButton.setEnabled(filaSeleccionada >= 0);
    }
    
    /**
     * Elimina el usuario seleccionado
     */
    private void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = usuariosTable.getSelectedRow();
        
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un usuario para eliminar", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el nombre del usuario seleccionado
        String nombreUsuario = (String) usuariosTable.getValueAt(filaSeleccionada, 0);
        
        // Buscar el usuario por nombre para obtener su ID
        UserManagementService servicioManejoUsuarios = context.getAdminService().getUserManager();
        List<User> usuarios = servicioManejoUsuarios.getAllUsers();
        User usuarioAEliminar = null;
        
        for (User usuario : usuarios) {
            if (usuario.getUsername().equals(nombreUsuario)) {
                usuarioAEliminar = usuario;
                break;
            }
        }
        
        if (usuarioAEliminar == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo encontrar el usuario seleccionado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al usuario '" + nombreUsuario + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (servicioManejoUsuarios.deleteUser(usuarioAEliminar.getId())) {
                    JOptionPane.showMessageDialog(this,
                        "Usuario '" + nombreUsuario + "' eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Recargar la tabla
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el usuario '" + nombreUsuario + "'",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Método obsoleto - mantener por compatibilidad
     */
    public void iniciarTablaUsuarios() {
        cargarUsuarios();
    }
    
    // ========== MÉTODOS PARA GESTIÓN DE CIUDADES ==========
    
    /**
     * Agrega una nueva ciudad al sistema
     */
    private void agregarCiudad() {
        String nombreCiudad = jTextField1.getText().trim();
        
        if (nombreCiudad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese el nombre de la ciudad",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtener valores de latitud y longitud
            double latitud = ((Number) jSpinner5.getValue()).doubleValue();
            double longitud = ((Number) jSpinner6.getValue()).doubleValue();
            
            // Crear nueva ciudad
            City nuevaCiudad = new City(nombreCiudad, latitud, longitud);
            
            CityManagementService cityManager = context.getAdminService().getCityManager();
            
            if (cityManager.createCity(nuevaCiudad)) {
                JOptionPane.showMessageDialog(this,
                    "Ciudad '" + nombreCiudad + "' agregada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                jTextField1.setText("");
                jSpinner5.setValue(0.0);
                jSpinner6.setValue(0.0);
                
                // Recargar ciudades
                cargarCiudades();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo agregar la ciudad. Verifique que el nombre no esté duplicado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar ciudad: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina la ciudad seleccionada
     */
    private void eliminarCiudad() {
        int filaSeleccionada = jTable1.getSelectedRow();
        
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una ciudad para eliminar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreCiudad = (String) jTable1.getValueAt(filaSeleccionada, 0);
        
        // Buscar la ciudad por nombre
        CityManagementService cityManager = context.getAdminService().getCityManager();
        List<City> ciudades = cityManager.getAllCities();
        City ciudadAEliminar = null;
        
        for (City ciudad : ciudades) {
            if (ciudad.getName().equals(nombreCiudad)) {
                ciudadAEliminar = ciudad;
                break;
            }
        }
        
        if (ciudadAEliminar == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo encontrar la ciudad seleccionada",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar la ciudad '" + nombreCiudad + "'?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (cityManager.deleteCity(ciudadAEliminar.getId())) {
                    JOptionPane.showMessageDialog(this,
                        "Ciudad '" + nombreCiudad + "' eliminada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Recargar ciudades
                    cargarCiudades();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la ciudad '" + nombreCiudad + "'.\n" +
                        "Verifique que no tenga conexiones existentes.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar ciudad: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Agrega una nueva conexión entre ciudades
     */
    private void agregarConexion() {
        String ciudadOrigen = (String) jComboBox1.getSelectedItem();
        String ciudadDestino = (String) jComboBox2.getSelectedItem();
        
        if ("Seleccionar ciudad origen".equals(ciudadOrigen) || 
            "Seleccionar ciudad destino".equals(ciudadDestino)) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione ciudades de origen y destino",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (ciudadOrigen.equals(ciudadDestino)) {
            JOptionPane.showMessageDialog(this,
                "La ciudad de origen y destino no pueden ser la misma",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Obtener valores de tiempo, distancia y costo
            int minutos = ((Number) jSpinner3.getValue()).intValue();
            double kilometros = ((Number) jSpinner4.getValue()).doubleValue();
            double costo = ((Number) jSpinner7.getValue()).doubleValue();
            
            // Buscar las ciudades por nombre
            CityManagementService cityManager = context.getAdminService().getCityManager();
            List<City> ciudades = cityManager.getAllCities();
            
            City origen = null;
            City destino = null;
            
            for (City ciudad : ciudades) {
                if (ciudad.getName().equals(ciudadOrigen)) {
                    origen = ciudad;
                }
                if (ciudad.getName().equals(ciudadDestino)) {
                    destino = ciudad;
                }
            }
            
            if (origen == null || destino == null) {
                JOptionPane.showMessageDialog(this,
                    "No se pudieron encontrar las ciudades seleccionadas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear nueva conexión
            Connection nuevaConexion = new Connection(origen, destino, kilometros, minutos, costo);
            
            if (cityManager.createConnection(nuevaConexion)) {
                JOptionPane.showMessageDialog(this,
                    "Conexión agregada exitosamente entre '" + ciudadOrigen + "' y '" + ciudadDestino + "'",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                jComboBox1.setSelectedIndex(0);
                jComboBox2.setSelectedIndex(0);
                jSpinner3.setValue(0);
                jSpinner4.setValue(0.0);
                jSpinner7.setValue(0.0);
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo agregar la conexión. Verifique que no exista ya una conexión entre estas ciudades.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar conexión: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina la conexión seleccionada
     */
    private void eliminarConexion() {
        String ciudadOrigen = (String) jComboBox1.getSelectedItem();
        String ciudadDestino = (String) jComboBox2.getSelectedItem();
        
        if ("Seleccionar ciudad origen".equals(ciudadOrigen) || 
            "Seleccionar ciudad destino".equals(ciudadDestino)) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione ciudades de origen y destino",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Buscar las ciudades por nombre
            CityManagementService cityManager = context.getAdminService().getCityManager();
            List<City> ciudades = cityManager.getAllCities();
            
            City origen = null;
            City destino = null;
            
            for (City ciudad : ciudades) {
                if (ciudad.getName().equals(ciudadOrigen)) {
                    origen = ciudad;
                }
                if (ciudad.getName().equals(ciudadDestino)) {
                    destino = ciudad;
                }
            }
            
            if (origen == null || destino == null) {
                JOptionPane.showMessageDialog(this,
                    "No se pudieron encontrar las ciudades seleccionadas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la conexión entre '" + ciudadOrigen + "' y '" + ciudadDestino + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (cityManager.deleteConnection(origen.getId(), destino.getId())) {
                    JOptionPane.showMessageDialog(this,
                        "Conexión eliminada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Limpiar campos
                    jComboBox1.setSelectedIndex(0);
                    jComboBox2.setSelectedIndex(0);
                    
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la conexión. Verifique que exista.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar conexión: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Lista todas las conexiones en la tabla existente
     */
    private void listarConexiones() {
        try {
            CityManagementService cityManager = context.getAdminService().getCityManager();
            List<Connection> conexiones = cityManager.getAllConnections();
            
            // Configurar la tabla para mostrar conexiones
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Desde");
            modelo.addColumn("Hacia");
            modelo.addColumn("Distancia (km)");
            modelo.addColumn("Tiempo (min)");
            modelo.addColumn("Costo");
            
            if (conexiones.isEmpty()) {
                // Si no hay conexiones, mostrar mensaje en la tabla
                modelo.addRow(new Object[] {
                    "No hay conexiones registradas",
                    "",
                    "",
                    "",
                    ""
                });
            } else {
                // Agregar todas las conexiones a la tabla
                for (Connection conexion : conexiones) {
                    String desde = conexion.getFromCity() != null ? conexion.getFromCity().getName() : "N/A";
                    String hacia = conexion.getToCity() != null ? conexion.getToCity().getName() : "N/A";
                    
                    modelo.addRow(new Object[] {
                        desde,
                        hacia,
                        String.format("%.1f", conexion.getDistance()),
                        conexion.getTimeMinutes(),
                        String.format("₡%.0f", conexion.getCost())
                    });
                }
            }
            
            // Actualizar la tabla
            jTable1.setModel(modelo);
            jTable1.setDefaultEditor(Object.class, null); // Hacer no editable
            
            // Mostrar mensaje informativo
            if (conexiones.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay conexiones registradas en el sistema",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Se muestran " + conexiones.size() + " conexiones en la tabla",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al listar conexiones: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Alterna entre mostrar ciudades y conexiones en la tabla
     */
    private void alternarVistaTabla() {
        // Obtener el modelo actual de la tabla
        DefaultTableModel modeloActual = (DefaultTableModel) jTable1.getModel();
        
        // Verificar qué está mostrando actualmente la tabla
        if (modeloActual.getColumnCount() == 4 && 
            modeloActual.getColumnName(0).equals("Ciudad")) {
            // Actualmente muestra ciudades, cambiar a conexiones
            listarConexiones();
        } else {
            // Actualmente muestra conexiones, cambiar a ciudades
            cargarCiudades();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jFileChooser1 = new javax.swing.JFileChooser();
        jComboBox3 = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        usuariosPanel = new javax.swing.JPanel();
        usuariosScrollPane = new javax.swing.JScrollPane();
        usuariosTable = new javax.swing.JTable();
        eliminarUsuarioButton = new javax.swing.JButton();
        estacionesPanel = new javax.swing.JPanel();
        listaEstacionesScrollPane = new javax.swing.JScrollPane();
        listaEstacionesTable = new javax.swing.JTable();
        nombreEstacionLabel = new javax.swing.JLabel();
        nombreEstacionTField = new javax.swing.JTextField();
        combustibleLabel = new javax.swing.JLabel();
        regularRButton = new javax.swing.JRadioButton();
        plusRButton = new javax.swing.JRadioButton();
        dieselRButton = new javax.swing.JRadioButton();
        gasLPRButton = new javax.swing.JRadioButton();
        tiposCargadorLabel = new javax.swing.JLabel();
        seleccionarCargadorButton = new javax.swing.JButton();
        agregarEstacionButton = new javax.swing.JButton();
        eliminarEstacionButton = new javax.swing.JButton();
        editarCargadoresButton = new javax.swing.JButton();
        ciudadesPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jSpinner6 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        usuariosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lista de usuarios"
            }
        ));
        usuariosScrollPane.setViewportView(usuariosTable);

        eliminarUsuarioButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        eliminarUsuarioButton.setText("Eliminar");
        eliminarUsuarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarUsuarioButtonActionPerformed(evt);
            }
        });

        usuariosTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                usuariosTableValueChanged(evt);
            }
        });

        javax.swing.GroupLayout usuariosPanelLayout = new javax.swing.GroupLayout(usuariosPanel);
        usuariosPanel.setLayout(usuariosPanelLayout);
        usuariosPanelLayout.setHorizontalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usuariosPanelLayout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usuariosScrollPane)
                    .addComponent(eliminarUsuarioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );
        usuariosPanelLayout.setVerticalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, usuariosPanelLayout.createSequentialGroup()
                .addContainerGap(129, Short.MAX_VALUE)
                .addComponent(usuariosScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(eliminarUsuarioButton)
                .addGap(83, 83, 83))
        );

        tabs.addTab("Usuarios", usuariosPanel);

        listaEstacionesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Lista de estaciones"
            }
        ));
        listaEstacionesScrollPane.setViewportView(listaEstacionesTable);

        nombreEstacionLabel.setText("Nombre de estación:");

        combustibleLabel.setText("Combustible:");

        regularRButton.setText("Regular");
        regularRButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regularRButtonActionPerformed(evt);
            }
        });

        plusRButton.setText("Plus");

        dieselRButton.setText("Diesel");

        gasLPRButton.setText("Gas LP");

        tiposCargadorLabel.setText("Tipos de cargador:");

        seleccionarCargadorButton.setText("Seleccionar");
        seleccionarCargadorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarCargadorButtonActionPerformed(evt);
            }
        });

        agregarEstacionButton.setText("Agregar");

        eliminarEstacionButton.setText("Eliminar");

        editarCargadoresButton.setText("Editar cargadores");

        javax.swing.GroupLayout estacionesPanelLayout = new javax.swing.GroupLayout(estacionesPanel);
        estacionesPanel.setLayout(estacionesPanelLayout);
        estacionesPanelLayout.setHorizontalGroup(
            estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(estacionesPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editarCargadoresButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(listaEstacionesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nombreEstacionLabel)
                    .addComponent(nombreEstacionTField)
                    .addComponent(combustibleLabel)
                    .addComponent(tiposCargadorLabel)
                    .addComponent(seleccionarCargadorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agregarEstacionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(eliminarEstacionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(estacionesPanelLayout.createSequentialGroup()
                        .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(regularRButton)
                            .addComponent(dieselRButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(gasLPRButton)
                            .addComponent(plusRButton))))
                .addGap(37, 37, 37))
        );
        estacionesPanelLayout.setVerticalGroup(
            estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(estacionesPanelLayout.createSequentialGroup()
                .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(estacionesPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(listaEstacionesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, estacionesPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nombreEstacionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreEstacionTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(combustibleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(regularRButton)
                            .addComponent(plusRButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(estacionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dieselRButton)
                            .addComponent(gasLPRButton))
                        .addGap(18, 18, 18)
                        .addComponent(tiposCargadorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seleccionarCargadorButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(agregarEstacionButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eliminarEstacionButton)
                        .addGap(102, 102, 102)))
                .addComponent(editarCargadoresButton)
                .addGap(28, 28, 28))
        );

        tabs.addTab("Estaciones", estacionesPanel);

        jLabel5.setText("Ciudad:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Latitud:");

        jLabel7.setText("Longitud:");

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton3.setText("Listar Ciudades");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Conexiones:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Origen:");

        jLabel8.setText("Destino");

        jLabel9.setText("Minutos:");

        jLabel10.setText("Km:");

        jLabel11.setText("Costo:");

        jButton4.setText("Agregar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Eliminar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Listar Conexiones");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(102, 102, 255));
        jButton7.setText("Mostrar Mapa");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ciudadesPanelLayout = new javax.swing.GroupLayout(ciudadesPanel);
        ciudadesPanel.setLayout(ciudadesPanelLayout);
        ciudadesPanelLayout.setHorizontalGroup(
            ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ciudadesPanelLayout.createSequentialGroup()
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9)
                            .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSpinner4)
                            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                                .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSpinner7)
                    .addGroup(ciudadesPanelLayout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(ciudadesPanelLayout.createSequentialGroup()
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                                .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6)
                                    .addComponent(jSpinner5)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7)
                                    .addComponent(jSpinner6)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1)
                            .addComponent(jLabel11))
                        .addGap(0, 78, Short.MAX_VALUE))
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        ciudadesPanelLayout.setVerticalGroup(
            ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ciudadesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ciudadesPanelLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(14, 14, 14)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ciudadesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5))
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Ciudades", ciudadesPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 869, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        eliminarConexion();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        eliminarCiudad();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        listarConexiones();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        agregarConexion();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // No necesitamos hacer nada aquí por ahora
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cargarCiudades();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarCiudad();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // No necesitamos hacer nada aquí por ahora
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void seleccionarCargadorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarCargadorButtonActionPerformed
        this.dispose();
        SeleccionarCargador seleccionarCargador = new SeleccionarCargador(context);
        seleccionarCargador.setVisible(true);
    }//GEN-LAST:event_seleccionarCargadorButtonActionPerformed

    private void regularRButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regularRButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regularRButtonActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * Maneja el evento del botón eliminar usuario
     */
    private void eliminarUsuarioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarUsuarioSeleccionado();
    }

    /**
     * Maneja el evento de cambio de selección en la tabla de usuarios
     */
    private void usuariosTableValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            actualizarEstadoBotonEliminar();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarEstacionButton;
    private javax.swing.JPanel ciudadesPanel;
    private javax.swing.JLabel combustibleLabel;
    private javax.swing.JRadioButton dieselRButton;
    private javax.swing.JButton editarCargadoresButton;
    private javax.swing.JButton eliminarEstacionButton;
    private javax.swing.JButton eliminarUsuarioButton;
    private javax.swing.JPanel estacionesPanel;
    private javax.swing.JRadioButton gasLPRButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JScrollPane listaEstacionesScrollPane;
    private javax.swing.JTable listaEstacionesTable;
    private javax.swing.JLabel nombreEstacionLabel;
    private javax.swing.JTextField nombreEstacionTField;
    private javax.swing.JRadioButton plusRButton;
    private javax.swing.JRadioButton regularRButton;
    private javax.swing.JButton seleccionarCargadorButton;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JLabel tiposCargadorLabel;
    private javax.swing.JPanel usuariosPanel;
    private javax.swing.JScrollPane usuariosScrollPane;
    private javax.swing.JTable usuariosTable;
    // End of variables declaration//GEN-END:variables
}
