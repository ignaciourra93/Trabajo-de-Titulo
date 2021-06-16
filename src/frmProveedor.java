/**
* Este archivo contiene el codigo para la ventana de proveedores
* para ingregar, modificar y eliminar un proveedor de la base de datos
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

public class frmProveedor extends javax.swing.JFrame {

    // Declaracion de variables
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";

    // Variable para instancia
    private static frmProveedor f = null;

    public frmProveedor() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        txtId.setEnabled(false);
        txtNom.setEnabled(false);
        txtFono.setEnabled(false);
        txtEmail.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    /**
    * Metodo para instancia
    */
    public static synchronized frmProveedor getInstance(){
        try {
            if (f == null) {
                f = (frmProveedor) Class.forName("frmProveedor").newInstance();
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

        // Declaracion de variable
        String url="";

        // Logica para conectarse a BD
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

    // Metodo para poblar la tabla con datos de BD
    public void llenarTabla(){

        // Declaracion de variable
        ResultSet lista = null;
        String[] fila = new String[4];
        DefaultTableModel model = (DefaultTableModel) tblProveedores.getModel();

        // Logica para poblar tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM proveedor WHERE delete_at = False");
            while(lista.next()){
                fila[0] = lista.getString("idproveedor");
                fila[1] = lista.getString("nombre");
                fila[2] = lista.getString("fono");
                fila[3] = lista.getString("email");
                model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para volver al menu anterior
    */
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        frmFactura.getInstance().setVisible(true);
        this.dispose();
    }

    /**
    * Metodo para limpiar campos
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {

        txtId.setEnabled(false);
        txtId.setText("");
        txtNom.setEnabled(false);
        txtNom.setText("");
        txtFono.setEnabled(false);
        txtFono.setText("");
        txtEmail.setEnabled(false);
        txtEmail.setText("");
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnMod.setEnabled(true);
    }

    /**
    * Metodo para activar campos para agregar
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {

        txtId.setEnabled(true);
        txtNom.setEnabled(true);
        txtFono.setEnabled(true);
        txtEmail.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnMod.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    /**
    * Metodo para activar campos para modificar y obtener datos de tabla
    */
    private void btnModActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblProveedores.getModel();
        String cod = "";
        String nom = "";
        String fono = "";
        String email = "";

        // Logica para activar campos y obtener datos de tabla
        if(tblProveedores.getSelectedRow()>=0){
            cod = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),0));
            txtId.setText(cod);
            txtNom.setEnabled(true);
            txtFono.setEnabled(true);
            txtEmail.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            nom = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),1));
            fono = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),2));
            email = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),3));
            txtNom.setText(nom);
            txtFono.setText(fono);
            txtEmail.setText(email);
        }
    }

    /**
    * Metodo para guardar en la BD
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String nom,cod,fono,email,insert,update;

        // Validaciones de campo
        if (txtId.getText().equals("") || txtNom.getText().equals("") || txtFono.getText().equals("") || txtEmail.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Faltan datos", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            if (txtNom.getText().length() > 40){
                JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 40 caracteres", "Error en el ingreso del nombre", JOptionPane.ERROR_MESSAGE);
            }else{

                // Logica para guardar nuevo proveedor
                if(btnAgregar.isEnabled()){
                cod = txtId.getText();
                nom = txtNom.getText();
                fono = txtFono.getText();
                email = txtEmail.getText();
                insert = "INSERT INTO proveedor(idproveedor,nombre,fono,email,delete_at) VALUES('"+cod+"','" +nom+ "','"+fono+"','"+email+"',False)";
                try{
                    sentencia.executeUpdate(insert);
                    JOptionPane.showMessageDialog(null,"Proveedor guardado con exito","Guardar proveedor", 2);
                    resetTable();
                    llenarTabla();
                    txtId.setEnabled(false);
                    txtId.setText("");
                    txtNom.setEnabled(false);
                    txtNom.setText("");
                    txtFono.setEnabled(false);
                    txtFono.setText("");
                    txtEmail.setEnabled(false);
                    txtEmail.setText("");
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                }
                }

                // Logica para modificar un proveedor
                if(btnMod.isEnabled()){
                    cod = txtId.getText();
                    nom = txtNom.getText();
                    fono = txtFono.getText();
                    email = txtEmail.getText();
                    update = "UPDATE proveedor SET nombre = '" +nom+ "', fono = '"+fono+"', email = '"+email+"' WHERE idproveedor = '"+ cod+"'";
                    try{
                        sentencia.executeUpdate(update);
                        JOptionPane.showMessageDialog(null,"Proveedor actualizado con exito","Modificar Proveedor", 2);
                        resetTable();
                        llenarTabla();
                        txtId.setEnabled(false);
                        txtId.setText("");
                        txtNom.setEnabled(false);
                        txtNom.setText("");
                        txtFono.setEnabled(false);
                        txtFono.setText("");
                        txtEmail.setEnabled(false);
                        txtEmail.setText("");
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
    * Metodo para eliminar
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String cod = "";
        String nom = "";
        String eliminar = "";
        DefaultTableModel tm = (DefaultTableModel) tblProveedores.getModel();
        byte opt;

        // Logica para eliminar
        if(tblProveedores.getSelectedRow()>=0){
            cod = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),0));
            nom = String.valueOf(tm.getValueAt(tblProveedores.getSelectedRow(),1));
            eliminar = "UPDATE proveedor SET delete_at = True WHERE idproveedor = '" + cod+"'";
            try{
                opt = (byte) JOptionPane.showConfirmDialog(null, "¿Desea eliminar " + nom + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opt == 0){
                    sentencia.executeUpdate(eliminar);
                    resetTable();
                    llenarTabla();
                    btnEliminar.setFocusable(false);
                }else{
                    tblProveedores.clearSelection();
                    btnEliminar.setFocusable(false);
                }
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error en eliminar "+ee.getMessage(),"eliminar", 2);
            }
        }
    }

    /**
    * Metodo para actualizar tabla
    */
    public void resetTable(){
        DefaultTableModel tb = (DefaultTableModel) tblProveedores.getModel();
        int a = tblProveedores.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Validacion de campo
    */
    private void txtNomKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtNom.getText().length() > 40) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 40 caracteres", "Error en el ingreso de nombre", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * Validacion de campo
    */
    private void txtFonoKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtFono.getText().length() > 12) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un telefono de mas de 12 caracteres", "Error en el ingreso de telefono", JOptionPane.ERROR_MESSAGE);
            txtFono.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtEmail.getText().length() > 30) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un email de mas de 30 caracteres", "Error en el ingreso de email", JOptionPane.ERROR_MESSAGE);
            txtEmail.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtIdKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtId.getText().length() > 12) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un id-proveedor de mas de 12 caracteres", "Error en el ingreso de id-proveedor", JOptionPane.ERROR_MESSAGE);
            txtId.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtIdKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(!isNumber(c) && !isValidSignal(c)) evt.consume();
    }

    /**
    * Validacion de campo
    */
    private boolean isNumber(char ch){
        return ch >= '0' && ch <= '9';
    }

    /**
    * Validacion de campo
    */
    private boolean isValidSignal(char ch){
        if(ch == '-' || ch == 'k' || ch == 'K'){
            return true;
        }
        return false;
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnVolver = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnMod = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProveedores = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFono = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel3.setText("Nombre:");

        txtNom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomKeyReleased(evt);
            }
        });

        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdKeyTyped(evt);
            }
        });

        jLabel2.setText("Id-Proveedor:");

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

        tblProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Proveedor", "Nombre", "Fono", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProveedores.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblProveedores);
        if (tblProveedores.getColumnModel().getColumnCount() > 0) {
            tblProveedores.getColumnModel().getColumn(0).setResizable(false);
            tblProveedores.getColumnModel().getColumn(1).setResizable(false);
            tblProveedores.getColumnModel().getColumn(2).setResizable(false);
            tblProveedores.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Proveedor");

        jLabel4.setText("Fono:");

        jLabel5.setText("Email:");

        txtFono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFonoKeyReleased(evt);
            }
        });

        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancelar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(6, 6, 6))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmail)
                                    .addComponent(txtFono)
                                    .addComponent(txtNom)
                                    .addComponent(txtId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(144, 144, 144))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnAgregar)
                        .addGap(27, 27, 27)
                        .addComponent(btnMod)
                        .addGap(28, 28, 28)
                        .addComponent(btnEliminar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVolver)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnMod)
                    .addComponent(btnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVolver)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(frmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProveedor().setVisible(true);
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProveedores;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFono;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNom;
}
