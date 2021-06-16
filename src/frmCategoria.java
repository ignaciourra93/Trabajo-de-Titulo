/**
* Este archivo contiene el codigo para la ventana de categorias
* para ingregar, modificar y eliminar una categoria de la base de datos.
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmCategoria extends javax.swing.JFrame {

    // Declaracion de variable
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";

    public frmCategoria() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar(); // conexion a la base de datos
        llenarTabla(); // poblamiento de tabla
        txtCodCat.setEnabled(false);
        txtNomCat.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    /**
    * Metodo para conectarse a la BD
    */
    public void conectar(){

        // Declaracion de variable
        String url="";

        // Logica para conectarse a la BD
        try{
            Class.forName("com.mysql.jdbc.Driver");
            url="jdbc:mysql://localhost:3306/" + nombre;
            conex=DriverManager.getConnection(url,usu,pass);
            sentencia = conex.createStatement();
            msj="Conexion exitosa";
        }catch(Exception e){
            msj="error en coneccion";
        }
    }

    /**
    * Metodo para llenar la tabla con datos
    */
    public void llenarTabla(){

        // Declaracion de variable
        ResultSet lista = null;
        String[] fila = new String[2];
        DefaultTableModel model = (DefaultTableModel) tblCategorias.getModel();

        // Logica para obtener datos de BD y poblar la tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM categoria where delete_at = False");
            while(lista.next()){
                fila[0] = lista.getString("codcategoria");
                fila[1] = lista.getString("nombrecat");
                model.addRow(new Object[] {fila[0],fila[1]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para cerrar la ventana y volver a vista anterior
    */
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        frmProducto seleccion = new frmProducto();
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para limpiar campos
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        txtCodCat.setEnabled(false);
        txtCodCat.setText("");
        txtNomCat.setEnabled(false);
        txtNomCat.setText("");
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnMod.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    /**
    * Metodo para activar campos para ingresar un nuevo registro
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        ResultSet lista = null;

        // Logica activar campos
        txtNomCat.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnMod.setEnabled(false);
        btnEliminar.setEnabled(false);
            try{
                sentencia = conex.createStatement();
                lista = sentencia.executeQuery("SELECT max(codcategoria)+1 FROM categoria");
                while(lista.next()){
                    txtCodCat.setText(lista.getString("max(codcategoria)+1"));
                }
            }catch (SQLException ee){
            }
    }

    /**
    * Metodo para activar campos para modificar un registro
    */
    private void btnModActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        DefaultTableModel tm = (DefaultTableModel) tblCategorias.getModel();
        String cod = String.valueOf(tm.getValueAt(tblCategorias.getSelectedRow(),0));
        String nom = String.valueOf(tm.getValueAt(tblCategorias.getSelectedRow(),1));

        // Logica para activar campos
        if(tblCategorias.getSelectedRow()>=0){
            txtCodCat.setText(cod);
            txtNomCat.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            txtNomCat.setText(nom);
        }
    }

    /**
    * Metodo para guardar en la BD
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String nom = "";
        String cod = "";
        String insert = "";
        String update = "";

        // Validaciones de campos
        if (txtNomCat.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            if (txtNomCat.getText().length() > 20){
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso del nombre", JOptionPane.ERROR_MESSAGE);
            }else{

                // Logica para agregar una categoria
                if(btnAgregar.isEnabled()){
                    cod = txtCodCat.getText();
                    nom = txtNomCat.getText();
                    insert = "INSERT INTO categoria(codcategoria,nombrecat,delete_at) VALUES("+cod+",'" +nom+ "',False)";
                    try{
                        sentencia.executeUpdate(insert);
                        JOptionPane.showMessageDialog(null,"Categoria guardada con exito","Guardar categoria", 2);
                        resetTable();
                        llenarTabla();
                        txtCodCat.setEnabled(false);
                        txtCodCat.setText("");
                        txtNomCat.setEnabled(false);
                        txtNomCat.setText("");
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        btnEliminar.setEnabled(true);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error al insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }

                // Logica para modificar una categoria
                if(btnMod.isEnabled()){
                    cod = txtCodCat.getText();
                    nom = txtNomCat.getText();
                    update = "UPDATE categoria SET nombrecat = '" +nom+ "' WHERE codcategoria = "+ cod;
                    try{
                        sentencia.executeUpdate(update);
                        JOptionPane.showMessageDialog(null,"Categoria actualizada con exito","Modificar categoria", 2);
                        resetTable();
                        llenarTabla();
                        txtCodCat.setEnabled(false);
                        txtCodCat.setText("");
                        txtNomCat.setEnabled(false);
                        txtNomCat.setText("");
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        btnAgregar.setEnabled(true);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error al actualizar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }
                btnMod.setEnabled(true);
            }
        }
    }

    /**
    * Metodo para eliminar una categoria
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String nom = "";
        String cod = "";
        String eliminar = "";

        // Logica para eliminar
        if(tblCategorias.getSelectedRow()>=0){
            DefaultTableModel tm = (DefaultTableModel) tblCategorias.getModel();
            cod = String.valueOf(tm.getValueAt(tblCategorias.getSelectedRow(),0));
            nom = String.valueOf(tm.getValueAt(tblCategorias.getSelectedRow(),1));
            eliminar = "update categoria set delete_at = True WHERE codcategoria = " + cod;
            try{
                byte opt = (byte) JOptionPane.showConfirmDialog(null, "¿Desea eliminar " + nom + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opt == 0){
                    sentencia.executeUpdate(eliminar);
                    resetTable();
                    llenarTabla();
                    btnEliminar.setFocusable(false);
                }else{
                    tblCategorias.clearSelection();
                    btnEliminar.setFocusable(false);
                }
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error en eliminar "+ee.getMessage(),"eliminar", 2);
            }
        }
    }

    /**
    * Metodo para actualizar la tabla
    */
    public void resetTable(){
        DefaultTableModel tb = (DefaultTableModel) tblCategorias.getModel();
        int a = tblCategorias.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Validacion de campo nombre
    */
    private void txtNomCatKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtNomCat.getText().length() > 20) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso de nombre", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnMod = new javax.swing.JButton();
        txtCodCat = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNomCat = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 300));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Categoria");

        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod-Categoria", "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCategorias.setGridColor(new java.awt.Color(255, 255, 255));
        tblCategorias.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblCategorias);
        if (tblCategorias.getColumnModel().getColumnCount() > 0) {
            tblCategorias.getColumnModel().getColumn(0).setResizable(false);
            tblCategorias.getColumnModel().getColumn(1).setResizable(false);
        }

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnMod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnMod.setText("Modificar");
        btnMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModActionPerformed(evt);
            }
        });

        jLabel2.setText("Cod-Categoria:");

        jLabel3.setText("Nombre:");

        txtNomCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomCatKeyReleased(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnGuardar)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(btnCancelar))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNomCat, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                            .addComponent(txtCodCat))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(btnAgregar)
                                .addGap(36, 36, 36)
                                .addComponent(btnMod)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminar)))
                        .addGap(43, 43, 43))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolver)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnMod)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addComponent(btnVolver)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmCategoria().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMod;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JTextField txtCodCat;
    private javax.swing.JTextField txtNomCat;
}