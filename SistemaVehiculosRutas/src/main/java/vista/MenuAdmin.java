/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Admin.AdminService;
import Admin.UserManagementService;
import Main.RouteSystemContext;
import User.User;
import User.UserRole;
import User.UserService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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
        cargarUsuarios();
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        mapaPanel = new javax.swing.JPanel();
        matrizLabel = new javax.swing.JLabel();
        nombreCiudadLabel = new javax.swing.JLabel();
        nombreCiudadTField = new javax.swing.JTextField();
        estacionCiudadButton = new javax.swing.JButton();
        agregarCiudadButton = new javax.swing.JButton();
        eliminarCiudadButton = new javax.swing.JButton();
        editarConexionesLabel = new javax.swing.JLabel();
        origenCBox = new javax.swing.JComboBox<>();
        destinoCBox = new javax.swing.JComboBox<>();
        minutosSpinner = new javax.swing.JSpinner();
        kilometrosSpinner = new javax.swing.JSpinner();
        costoSpinner = new javax.swing.JSpinner();
        agregarConexionButton = new javax.swing.JButton();
        eliminarConexionButton = new javax.swing.JButton();
        verMapaButton = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();

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

        javax.swing.GroupLayout usuariosPanelLayout = new javax.swing.GroupLayout(usuariosPanel);
        usuariosPanel.setLayout(usuariosPanelLayout);
        usuariosPanelLayout.setHorizontalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usuariosPanelLayout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addGroup(usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eliminarUsuarioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usuariosScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(219, Short.MAX_VALUE))
        );
        usuariosPanelLayout.setVerticalGroup(
            usuariosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usuariosPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(usuariosScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(eliminarUsuarioButton)
                .addContainerGap(105, Short.MAX_VALUE))
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
                    .addComponent(listaEstacionesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
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
                        .addComponent(listaEstacionesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
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

        matrizLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        matrizLabel.setText("Matriz");

        nombreCiudadLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nombreCiudadLabel.setText("Nombre de ciudad:");

        nombreCiudadTField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        estacionCiudadButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        estacionCiudadButton.setText("Estación en ciudad");

        agregarCiudadButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        agregarCiudadButton.setText("Agregar");

        eliminarCiudadButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        eliminarCiudadButton.setText("Eliminar");

        editarConexionesLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        editarConexionesLabel.setText("Editar conexiones:");

        origenCBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        origenCBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        origenCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                origenCBoxActionPerformed(evt);
            }
        });

        destinoCBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        destinoCBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        minutosSpinner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        kilometrosSpinner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        costoSpinner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        agregarConexionButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        agregarConexionButton.setText("Agregar");

        eliminarConexionButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        eliminarConexionButton.setText("Eliminar");

        verMapaButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        verMapaButton.setText("Ver mapa");

        javax.swing.GroupLayout mapaPanelLayout = new javax.swing.GroupLayout(mapaPanel);
        mapaPanel.setLayout(mapaPanelLayout);
        mapaPanelLayout.setHorizontalGroup(
            mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapaPanelLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mapaPanelLayout.createSequentialGroup()
                        .addComponent(verMapaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(358, Short.MAX_VALUE))
                    .addGroup(mapaPanelLayout.createSequentialGroup()
                        .addComponent(matrizLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nombreCiudadTField)
                            .addComponent(editarConexionesLabel)
                            .addComponent(nombreCiudadLabel)
                            .addComponent(estacionCiudadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(mapaPanelLayout.createSequentialGroup()
                                .addComponent(agregarCiudadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eliminarCiudadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(mapaPanelLayout.createSequentialGroup()
                                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(costoSpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(minutosSpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(agregarConexionButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(origenCBox, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(eliminarConexionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(kilometrosSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(destinoCBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSpinner1)))
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86))))
        );
        mapaPanelLayout.setVerticalGroup(
            mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapaPanelLayout.createSequentialGroup()
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mapaPanelLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(matrizLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapaPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreCiudadLabel)))
                .addGap(18, 18, 18)
                .addComponent(nombreCiudadTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(estacionCiudadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agregarCiudadButton)
                    .addComponent(eliminarCiudadButton))
                .addGap(30, 30, 30)
                .addComponent(editarConexionesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(origenCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(destinoCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minutosSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kilometrosSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(costoSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mapaPanelLayout.createSequentialGroup()
                        .addComponent(jSpinner1)
                        .addGap(1, 1, 1)))
                .addGap(18, 18, 18)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mapaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agregarConexionButton)
                    .addComponent(eliminarConexionButton))
                .addGap(18, 18, 18)
                .addComponent(verMapaButton)
                .addGap(76, 76, 76))
        );

        tabs.addTab("Mapa", mapaPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void seleccionarCargadorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarCargadorButtonActionPerformed
        this.dispose();
        SeleccionarCargador seleccionarCargador = new SeleccionarCargador(context);
        seleccionarCargador.setVisible(true);
    }//GEN-LAST:event_seleccionarCargadorButtonActionPerformed

    private void regularRButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regularRButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regularRButtonActionPerformed

    private void origenCBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_origenCBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_origenCBoxActionPerformed

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
    private javax.swing.JButton agregarCiudadButton;
    private javax.swing.JButton agregarConexionButton;
    private javax.swing.JButton agregarEstacionButton;
    private javax.swing.JLabel combustibleLabel;
    private javax.swing.JSpinner costoSpinner;
    private javax.swing.JComboBox<String> destinoCBox;
    private javax.swing.JRadioButton dieselRButton;
    private javax.swing.JButton editarCargadoresButton;
    private javax.swing.JLabel editarConexionesLabel;
    private javax.swing.JButton eliminarCiudadButton;
    private javax.swing.JButton eliminarConexionButton;
    private javax.swing.JButton eliminarEstacionButton;
    private javax.swing.JButton eliminarUsuarioButton;
    private javax.swing.JButton estacionCiudadButton;
    private javax.swing.JPanel estacionesPanel;
    private javax.swing.JRadioButton gasLPRButton;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner kilometrosSpinner;
    private javax.swing.JScrollPane listaEstacionesScrollPane;
    private javax.swing.JTable listaEstacionesTable;
    private javax.swing.JPanel mapaPanel;
    private javax.swing.JLabel matrizLabel;
    private javax.swing.JSpinner minutosSpinner;
    private javax.swing.JLabel nombreCiudadLabel;
    private javax.swing.JTextField nombreCiudadTField;
    private javax.swing.JLabel nombreEstacionLabel;
    private javax.swing.JTextField nombreEstacionTField;
    private javax.swing.JComboBox<String> origenCBox;
    private javax.swing.JRadioButton plusRButton;
    private javax.swing.JRadioButton regularRButton;
    private javax.swing.JButton seleccionarCargadorButton;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JLabel tiposCargadorLabel;
    private javax.swing.JPanel usuariosPanel;
    private javax.swing.JScrollPane usuariosScrollPane;
    private javax.swing.JTable usuariosTable;
    private javax.swing.JButton verMapaButton;
    // End of variables declaration//GEN-END:variables
}
