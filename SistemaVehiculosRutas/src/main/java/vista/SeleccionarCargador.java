/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Main.RouteSystemContext;

/**
 *
 * @author javie
 */
public class SeleccionarCargador extends javax.swing.JFrame {
    
    private RouteSystemContext context;
    private javax.swing.JFrame menuAdmin;
    /**
     * Creates new form SeleccionarCargador
     */
    public SeleccionarCargador(RouteSystemContext context, javax.swing.JFrame menuAdmin) {
        this.context = context;
        this.menuAdmin = menuAdmin;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cargadoresScrollPane = new javax.swing.JScrollPane();
        cargadoresTable = new javax.swing.JTable();
        listoButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                SeleccionarCargador.this.windowClosing(evt);
            }
        });

        cargadoresTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo de cargador", "Está"
            }
        ));
        cargadoresScrollPane.setViewportView(cargadoresTable);

        listoButton.setText("Listo");
        listoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(cargadoresScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(listoButton)))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(cargadoresScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(listoButton)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
        menuAdmin.setEnabled(true);
    }//GEN-LAST:event_windowClosing

    private void listoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listoButtonActionPerformed
        this.dispose();
        menuAdmin.setEnabled(true);
    }//GEN-LAST:event_listoButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane cargadoresScrollPane;
    private javax.swing.JTable cargadoresTable;
    private javax.swing.JButton listoButton;
    // End of variables declaration//GEN-END:variables
}
