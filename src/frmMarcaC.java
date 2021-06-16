/**
* Este archivo se creó para evitar la perdida de datos ingresados en los campos
* de la ventana anterior, contiene el mismo codigo de "frmMarca.java" y el
* codigo necesario para evitar la perdida de datos.
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

public class frmMarcaC extends javax.swing.JFrame {
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";

    // Variable para instancia
    private static frmMarcaC f = null;

    public frmMarcaC() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        txtCodMar.setEnabled(false);
        txtNomMar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    /**
    * Metodo para instancia
    */
     public static synchronized frmMarcaC getInstance(){
        try {
            if (f == null) {
                f = (frmMarcaC) Class.forName("frmMarcaC").newInstance();
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println(e.toString());
        }
        return f;
    }

    /**
    * Metodo para conectarse a la BD
    */
    public void conectar(){

        // Declaracion de variables
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
    * Metodo para poblar la tabla
    */
    public void llenarTabla(){

        // Declaracion de variables
        ResultSet lista = null;
        String[] fila = new String[2];
        DefaultTableModel model = (DefaultTableModel) tblMarcas.getModel();

        // Logica para poblar la tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM marca where delete_at = False");
            while(lista.next()){
                fila[0] = lista.getString("codmarca");
                fila[1] = lista.getString("nombremar");
                model.addRow(new Object[] {fila[0],fila[1]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para volver al menu anterior
    */
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        frmProductoC.getInstance().setVisible(true);
        this.dispose();
    }

    /**
    * Metodo para activar campos y obtener id de nueva marca
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        // Declaracion de variables
        ResultSet lista = null;

        // Activacion de los campos
        txtNomMar.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnMod.setEnabled(false);
        btnEliminar.setEnabled(false);

            // Logica para obtener id de nueva marca
            try{
                sentencia = conex.createStatement();
                lista = sentencia.executeQuery("SELECT max(codmarca)+1 FROM marca");
                while(lista.next()){
                    txtCodMar.setText(lista.getString("max(codmarca)+1"));
                }
            }catch (SQLException ee){
            }
    }

    /**
    * Metodo para modificar una marca
    */
    private void btnModActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblMarcas.getModel();
        String cod= "";
        String nom = "";

        // Logica para activar campos y obtener datos de la marca a modificar
        if(tblMarcas.getSelectedRow()>=0){
            cod = String.valueOf(tm.getValueAt(tblMarcas.getSelectedRow(),0));
            txtCodMar.setText(cod);
            txtNomMar.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            nom = String.valueOf(tm.getValueAt(tblMarcas.getSelectedRow(),1));
            txtNomMar.setText(nom);
        }
    }

    /**
    * Metodo para guardar en la BD
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String nom,cod,insert,update;

        // Validaciones de campos
        if (txtNomMar.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            if (txtNomMar.getText().length() > 20){
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso del nombre", JOptionPane.ERROR_MESSAGE);
            }else{

                // Logicar para guardar una nueva marca
                if(btnAgregar.isEnabled()){
                    cod = txtCodMar.getText();
                    nom = txtNomMar.getText();
                    insert = "INSERT INTO marca(codmarca,nombremar,delete_at) VALUES("+cod+",'" +nom+ "',False)";
                    try{
                        sentencia.executeUpdate(insert);
                        JOptionPane.showMessageDialog(null,"marca guardada con exito","Guardar marca", 2);
                        resetTable();
                        llenarTabla();
                        txtCodMar.setEnabled(false);
                        txtCodMar.setText("");
                        txtNomMar.setEnabled(false);
                        txtNomMar.setText("");
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }

                // Logica para modificar una marca
                if(btnMod.isEnabled()){
                    cod = txtCodMar.getText();
                    nom = txtNomMar.getText();
                    update = "UPDATE marca SET nombremar = '" +nom+ "' WHERE codmarca = "+ cod;
                    try{
                        sentencia.executeUpdate(update);
                        JOptionPane.showMessageDialog(null,"marca actualizada con exito","Modificar marca", 2);
                        resetTable();
                        llenarTabla();
                        txtCodMar.setEnabled(false);
                        txtCodMar.setText("");
                        txtNomMar.setEnabled(false);
                        txtNomMar.setText("");
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        btnAgregar.setEnabled(true);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error al actualizar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }
                btnMod.setEnabled(true);
                btnEliminar.setEnabled(true);
            }
        }
    }

    /**
    * Metodo para limpiar los campos
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {

        txtCodMar.setEnabled(false);
        txtCodMar.setText("");
        txtNomMar.setEnabled(false);
        txtNomMar.setText("");
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnMod.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    /**
    * Metodo para eliminar una marca
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblMarcas.getModel();
        String cod = "";
        String nom = "";
        String eliminar = "";
        byte opt;

        // Logica para eliminar una marca
        if(tblMarcas.getSelectedRow()>=0){
            cod = String.valueOf(tm.getValueAt(tblMarcas.getSelectedRow(),0));
            nom = String.valueOf(tm.getValueAt(tblMarcas.getSelectedRow(),1));
            eliminar = "update marca set delete_at = True WHERE codmarca = " + cod;
            try{
                opt = (byte) JOptionPane.showConfirmDialog(null, "¿Desea eliminar " + nom + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opt == 0){
                    sentencia.executeUpdate(eliminar);
                    resetTable();
                    llenarTabla();
                    btnEliminar.setFocusable(false);
                }else{
                    tblMarcas.clearSelection();
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
        DefaultTableModel tb = (DefaultTableModel) tblMarcas.getModel();
        int a = tblMarcas.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Validacion de campo
    */
    private void txtNomMarKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtNomMar.getText().length() > 20) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso de nombre", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMarcas = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnMod = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCodMar = new javax.swing.JTextField();
        txtNomMar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Marca");

        tblMarcas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod-Marca", "Nombre"
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
        tblMarcas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblMarcas);
        if (tblMarcas.getColumnModel().getColumnCount() > 0) {
            tblMarcas.getColumnModel().getColumn(0).setResizable(false);
            tblMarcas.getColumnModel().getColumn(1).setResizable(false);
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

        jLabel2.setText("Cod-Marca:");

        jLabel3.setText("Nombre:");

        txtNomMar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomMarKeyReleased(evt);
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(btnAgregar)
                                .addGap(18, 18, 18)
                                .addComponent(btnMod)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnGuardar)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addComponent(btnCancelar))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtNomMar, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(txtCodMar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(53, 53, 53)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnVolver)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnMod)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodMar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomMar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addComponent(btnVolver)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMarca().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMod;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMarcas;
    private javax.swing.JTextField txtCodMar;
    private javax.swing.JTextField txtNomMar;
}