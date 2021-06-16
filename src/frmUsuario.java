/**
* Este archivo contiene el codigo para la ventana de usuarios
* para ingregar, modificar y eliminar un usuario de la base de datos
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.digest.DigestUtils;

public class frmUsuario extends javax.swing.JFrame {

    // Declaracion de variable
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String usuarioLogeado;

    public frmUsuario() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        txtId.setEnabled(false);
        txtContraseña.setEnabled(false);
        txtNombre.setEnabled(false);
        cmbTipo.setEnabled(false);
        txtEmail.setEnabled(false);
        txtFono.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnContraseña.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }
    public frmUsuario(String id) {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        this.usuarioLogeado = id;
        llenarTabla();
        txtId.setEnabled(false);
        txtContraseña.setEnabled(false);
        txtNombre.setEnabled(false);
        cmbTipo.setEnabled(false);
        txtEmail.setEnabled(false);
        txtFono.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnContraseña.setEnabled(false);
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
        String[] fila = new String[5];
        DefaultTableModel model = (DefaultTableModel) tblUsuario.getModel();

        // Logica para poblar la tabla con datos
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM usuario WHERE delete_at = False and idusuario != '" + usuarioLogeado + "'");
            while(lista.next()){
                fila[0] = lista.getString("idusuario");
                fila[1] = lista.getString("nombre");
                if (lista.getString("tipo").equals("A")){
                    fila[2] = "Admin";
                }else{
                    fila[2] = "Vendedor";
                }
                fila[3] = lista.getString("email");
                fila[4] = lista.getString("fono");
                model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para volver al menu anterior
    */
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        frmMenuAdmin seleccion = new frmMenuAdmin(usuarioLogeado);
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para guardar un usuario
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String id, contra, nom, tipo, email, fono, insert, update;

        // Logica para agregar un usuario nuevo
        if(btnAgregar.isEnabled()){
            id = txtId.getText();
            contra = txtContraseña.getText();
            contra = DigestUtils.sha1Hex(contra);
            nom = txtNombre.getText();
            tipo = (String)cmbTipo.getSelectedItem();
            if(tipo.equals("Admin")){
                tipo = "A";
            }else{
                tipo="V";
            }
            email  = txtEmail.getText();
            fono   = txtFono.getText();
            insert = "INSERT INTO usuario(idusuario,contraseña,nombre,tipo,email,fono,delete_at) VALUES('"+id+"','" +contra+ "','" +nom+ "','" +tipo+ "','" +email+ "','" +fono+ "', False)";
            try{
                sentencia.executeUpdate(insert);
                JOptionPane.showMessageDialog(null,"Usuario guardado con exito","Guardar Usuario", 2);

                // Reinicio de los campos
                resetTable();
                llenarTabla();
                txtId.setEnabled(false);
                txtId.setText("");
                txtContraseña.setEnabled(false);
                txtContraseña.setText("");
                txtNombre.setEnabled(false);
                txtNombre.setText("");
                cmbTipo.setEnabled(false);
                cmbTipo.setSelectedIndex(0);
                txtEmail.setEnabled(false);
                txtEmail.setText("");
                txtFono.setEnabled(false);
                txtFono.setText("");
                btnGuardar.setEnabled(false);
                btnCancelar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnSalir.setEnabled(true);
                btnContraseña.setEnabled(false);
                txtBusqueda.setEnabled(true);
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error insertar"+ee.getMessage(),"Guardar", 2);
            }
        }else{

            // Logica para modificar un usuario
            if(btnModificar.isEnabled()){
                id = txtId.getText();
                contra = txtContraseña.getText();
                contra = DigestUtils.sha1Hex(contra);
                nom = txtNombre.getText();
                tipo   = (String)cmbTipo.getSelectedItem();
                if(tipo.equals("Admin")){
                    tipo = "A";
                }else{
                    tipo="V";
                }
                email  = txtEmail.getText();
                fono   = txtFono.getText();
                update = "UPDATE usuario SET nombre = '" +nom+ "', tipo = '" +tipo+ "', email = '" +email+ "', fono = '" +fono+ "' WHERE idusuario = '"+ id +"'";
                try{
                    sentencia.executeUpdate(update);
                    JOptionPane.showMessageDialog(null,"Usuario actualizado con exito","Modificar Usuario", 2);

                    //reinicio de los campos
                    resetTable();
                    llenarTabla();
                    txtId.setEnabled(false);
                    txtId.setText("");
                    txtContraseña.setEnabled(false);
                    txtContraseña.setText("");
                    txtNombre.setEnabled(false);
                    txtNombre.setText("");
                    cmbTipo.setEnabled(false);
                    cmbTipo.setSelectedIndex(0);
                    txtEmail.setEnabled(false);
                    txtEmail.setText("");
                    txtFono.setEnabled(false);
                    txtFono.setText("");
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);
                    btnAgregar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    btnSalir.setEnabled(true);
                    btnContraseña.setEnabled(false);
                    txtBusqueda.setEnabled(true);
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                }
            }
        }
    }

    /**
    * Metodo para activar campos para agregar
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        txtContraseña.setEnabled(true);
        txtNombre.setEnabled(true);
        cmbTipo.setEnabled(true);
        txtEmail.setEnabled(true);
        txtFono.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnSalir.setEnabled(false);
        btnContraseña.setEnabled(false);
        txtBusqueda.setEnabled(false);
    }

    /**
    * Metodo para limpiar campos
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        if(btnAgregar.isEnabled()){
            txtId.setEnabled(false);
            txtContraseña.setEnabled(false);
            txtNombre.setEnabled(false);
            cmbTipo.setEnabled(false);
            txtEmail.setEnabled(false);
            txtFono.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnSalir.setEnabled(true);
            txtBusqueda.setEnabled(true);
            btnContraseña.setEnabled(false);
        }
        if(btnModificar.isEnabled()){
            txtId.setEnabled(false);
            txtId.setText("");
            txtContraseña.setEnabled(false);
            txtContraseña.setText("");
            txtNombre.setEnabled(false);
            txtNombre.setText("");
            cmbTipo.setEnabled(false);
            cmbTipo.setSelectedIndex(0);
            txtEmail.setEnabled(false);
            txtEmail.setText("");
            txtFono.setEnabled(false);
            txtFono.setText("");
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnAgregar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnSalir.setEnabled(true);
            txtBusqueda.setEnabled(true);
            tblUsuario.clearSelection();
            btnContraseña.setEnabled(false);
        }
    }

    /**
    * Metodo para activar campos para modificar
    */
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblUsuario.getModel();
        String id, nom, tipo, email, fono;

        // Logica para activar campos
        if(tblUsuario.getSelectedRow()>=0){
            txtId.setEnabled(true);
            txtContraseña.setEnabled(true);
            txtNombre.setEnabled(true);
            cmbTipo.setEnabled(true);
            txtEmail.setEnabled(true);
            txtFono.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnSalir.setEnabled(false);
            txtBusqueda.setEnabled(false);
            btnContraseña.setEnabled(true);

            // Logica para obtener datos de la tabla y insertar en campos
            id = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),0));
            txtId.setText(id);
            txtId.setEnabled(false);
            txtContraseña.setEnabled(false);
            nom = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),1));
            txtNombre.setText(nom);
            tipo = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),2));
            cmbTipo.setSelectedItem(tipo);
            email = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),3));
            txtEmail.setText(email);
            fono = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),4));
            txtFono.setText(fono);
        }
    }

    /**
    * Metodo para eliminar un usuario
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String id = "";
        String nom = "";
        String eliminar = "";

        // Logica para eliminar usuario
        if(tblUsuario.getSelectedRow()>=0){
            DefaultTableModel tm = (DefaultTableModel) tblUsuario.getModel();
            id = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),0));
            nom = String.valueOf(tm.getValueAt(tblUsuario.getSelectedRow(),1));
            eliminar = "UPDATE usuario SET delete_at = True WHERE idusuario = '" + id + "'";
            try{
                byte opt = (byte) JOptionPane.showConfirmDialog(null, "¿Desea eliminar al usuario " + nom + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opt == 0){
                    sentencia.executeUpdate(eliminar);
                    resetTable();
                    llenarTabla();
                    btnEliminar.setFocusable(false);
                }else{
                    tblUsuario.clearSelection();
                    btnEliminar.setFocusable(false);
                }
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error en eliminar "+ee.getMessage(),"eliminar", 2);
            }
        }
    }

    /**
    * Metodo para cambiar contraseña
    */
    private void btnContraseñaActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String contraseña1, contraseña2, contraseñaEncriptada, id, sql;

        // Logica para restablecer contraseña
        contraseña1 = (String)JOptionPane.showInputDialog(null,"Ingrese la nueva contraseña","Restablecer Contraseña",JOptionPane.WARNING_MESSAGE);
        contraseña2 = (String) JOptionPane.showInputDialog(null,"Ingrese nuevamente la nueva contraseña","Confirmar nueva contraseña",JOptionPane.WARNING_MESSAGE);
        while (!contraseña1.equals(contraseña2)){
            JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden, Reingrese","Error en la nueva contraseña", 2);
            contraseña1 = (String) JOptionPane.showInputDialog(null,"Ingrese la nueva contraseña","Restablecer Contraseña",JOptionPane.WARNING_MESSAGE);
            contraseña2 = (String) JOptionPane.showInputDialog(null,"Ingrese nuevamente la nueva contraseña","Confirmar nueva contraseña",JOptionPane.WARNING_MESSAGE);
        }
        if (contraseña1.equals(contraseña2)) {
            id = txtId.getText();
            contraseñaEncriptada = DigestUtils.sha1Hex(contraseña1);
            sql = "UPDATE usuario SET contraseña = '"+contraseñaEncriptada+"' WHERE idusuario = '"+ id +"'";
            try{
                sentencia.executeUpdate(sql);
                JOptionPane.showMessageDialog(null,"Contraseña Actualizada","Restablecer Contraseña",JOptionPane.INFORMATION_MESSAGE);
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
            }
        }
    }


    private void txtContraseñaFocusGained(java.awt.event.FocusEvent evt) {
        if(txtContraseña.getText().equals("**********")){
            txtContraseña.setText("");
        }
    }

    private void txtContraseñaFocusLost(java.awt.event.FocusEvent evt) {
        if(txtContraseña.getText().equals("")){
            txtContraseña.setText("**********");
        }
    }

    /**
    * Metodo para realizar una busqueda
    */
    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {

        // Declaracion de variables
        String filtro = txtBusqueda.getText();
        String dato = (String)cmbBusqueda.getSelectedItem();
        String[] fila = new String[5];
        ResultSet lista = null;
        DefaultTableModel model = (DefaultTableModel) tblUsuario.getModel();
        String sql = null;

        // Logica para cuando el campo de busqeuda esta vacio
        if(filtro.equals("")){
            resetTable();
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery("SELECT * FROM usuario WHERE delete_at = False and idusuario <> '" + usuarioLogeado + "'");
                while(lista.next()){
                    fila[0] = lista.getString("idusuario");
                    fila[1] = lista.getString("nombre");
                    if (lista.getString("tipo").equals("A")){
                        fila[2] = "Admin";
                    }else{
                        fila[2] = "Vendedor";
                    }
                    fila[3] = lista.getString("email");
                    fila[4] = lista.getString("fono");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4]});
                }
            }catch(SQLException ee){
            }
        }else{

            // Logica para realizar una busqueda
            resetTable();
            if(dato.equals("Id-usuario")){
                sql = "SELECT * FROM usuario WHERE idusuario LIKE '" +filtro+"%' AND delete_at = False and idusuario <> '" + usuarioLogeado + "'";
            }
            if(dato.equals("Nombre")){
                sql = "SELECT * FROM usuario WHERE nombre LIKE '" +filtro+"%' AND delete_at = False and idusuario <> '" + usuarioLogeado + "'";
            }
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery(sql);
                while(lista.next()){
                    fila[0] = lista.getString("idusuario");
                    fila[1] = lista.getString("nombre");
                    if (lista.getString("tipo").equals("A")){
                        fila[2] = "Admin";
                    }else{
                        fila[2] = "Vendedor";
                    }
                    fila[3] = lista.getString("email");
                    fila[4] = lista.getString("fono");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4]});
                }
            }catch(SQLException ee){
            }
        }
    }

    /**
    * Metodo para actualizar tabla
    */
    public void resetTable(){
        DefaultTableModel tb = (DefaultTableModel) tblUsuario.getModel();
        int a = tblUsuario.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
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

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuario = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        txtBusqueda = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtFono = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnContraseña = new javax.swing.JButton();
        txtContraseña = new javax.swing.JPasswordField();
        cmbBusqueda = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Usuario", "Nombre", "Tipo", "Email", "Fono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsuario.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblUsuario);
        if (tblUsuario.getColumnModel().getColumnCount() > 0) {
            tblUsuario.getColumnModel().getColumn(0).setResizable(false);
            tblUsuario.getColumnModel().getColumn(1).setResizable(false);
            tblUsuario.getColumnModel().getColumn(2).setResizable(false);
            tblUsuario.getColumnModel().getColumn(3).setResizable(false);
            tblUsuario.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Usuarios");

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnSalir.setText("Volver");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda.png"))); // NOI18N
        jLabel14.setText("Busqueda por:");

        jLabel8.setText("Id-Usuario: ");

        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdKeyTyped(evt);
            }
        });

        jLabel9.setText("Contraseña:");

        jLabel10.setText("Nombre:");

        jLabel11.setText("Tipo: ");

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Vendedor" }));

        jLabel12.setText("Email: ");

        jLabel13.setText("Fono:");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnContraseña.setText("Restablecer Contraseña");
        btnContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContraseñaActionPerformed(evt);
            }
        });

        txtContraseña.setText("**********");
        txtContraseña.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContraseñaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContraseñaFocusLost(evt);
            }
        });

        cmbBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id-usuario", "Nombre" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSalir)
                                .addGap(8, 8, 8))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(267, 267, 267)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbBusqueda, 0, 84, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(287, 287, 287)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnAgregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar)
                                .addGap(147, 147, 147))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtId)
                                                    .addComponent(txtContraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(cmbTipo, javax.swing.GroupLayout.Alignment.TRAILING, 0, 134, Short.MAX_VALUE)
                                                            .addComponent(txtNombre)))
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(txtFono, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnContraseña))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnGuardar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnCancelar)))
                                .addGap(42, 42, 42))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnAgregar)
                    .addComponent(btnModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(btnContraseña)
                    .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtFono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            java.util.logging.Logger.getLogger(frmUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUsuario().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnContraseña;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbBusqueda;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuario;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFono;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
}