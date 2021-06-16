/**
* Este archivo contiene el codigo para la ventana de facturas
* para ingregar una factura a la base de datos
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmFactura extends javax.swing.JFrame {

    // Declaracion de variable
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String usuario;

    // Variable para instancia
    private static frmFactura f = null;


    public frmFactura() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        llenarComboProv();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    public frmFactura(String usuario) {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        llenarComboProv();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
        this.usuario = usuario;
    }

    /**
    * Metodo para instancia
    */
    public static synchronized frmFactura getInstance(){
        try {
            if (f == null) {
                f = (frmFactura) Class.forName("frmFactura").newInstance();
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println(e.toString());
        }
        return f;
    }

    /**
    * Metodo para conectarse a BD
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

    /**
    * Metodo para poblar tabla con datos de BD
    */
    public void llenarTabla(){

        // Declaracion de variable
        ResultSet lista = null;
        String[] fila = new String[3];
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

        // Logica para poblar tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m WHERE p.codmarca = m.codmarca ORDER BY p.idproducto");
            while(lista.next()){
                fila[0] = lista.getString("idproducto");
                fila[1] = lista.getString("nombre");
                fila[2] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                model.addRow(new Object[] {fila[0],fila[1],fila[2]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para abrir ventana de producto
    */
    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        frmProductoC.getInstance().setVisible(true);
        this.dispose();
    }

    /**
    * Metodo para abrir ventana de proveedor
    */
    private void btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {
          frmProveedor.getInstance().setVisible(true);
          this.dispose();
    }

    /**
    * Metodo para volver al menu anterior
    */
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        frmMenuAdmin seleccion = new frmMenuAdmin(usuario);
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para agregar un producto al detalle factura
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String cantidad,precio;
        String[] fila = new String[8];
        DefaultTableModel tm = (DefaultTableModel) tblProductos.getModel();
        DefaultTableModel model = (DefaultTableModel) tblContenidoFactura.getModel();

        // Validacion de cantidad y precio
        if(tblProductos.getSelectedRow()>=0){
            cantidad = JOptionPane.showInputDialog(null,"Ingrese cantidad solicitada","Cantidad",JOptionPane.WARNING_MESSAGE);
            while(cantidad == null || cantidad.equals("0")){
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                cantidad = JOptionPane.showInputDialog(null,"Ingrese cantidad solicitada","Cantidad",JOptionPane.WARNING_MESSAGE);
            }
            precio = JOptionPane.showInputDialog(null,"Ingrese precio del producto","Precio",JOptionPane.WARNING_MESSAGE);
            while(precio == null || precio.equals("0")){
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                precio = JOptionPane.showInputDialog(null,"Ingrese precio del producto","Precio",JOptionPane.WARNING_MESSAGE);
            }

            // Logica para agregar producto a detalle factura
            fila[0] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),0));
            fila[1] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),1));
            fila[2] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),2));
            fila[3] = cantidad;
            fila[4] = precio;
            model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4]});
            tblProductos.clearSelection();
        }
    }

    /**
    * Metodo para modificar cantidad y precio ingresado
    */
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String cantidad, precio;
        DefaultTableModel tm = (DefaultTableModel) tblContenidoFactura.getModel();
        int fila;

        // Validacion de campos
        if(tblContenidoFactura.getSelectedRow()>=0){
            cantidad = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad","Modificar Cantidad",JOptionPane.WARNING_MESSAGE);
            while(cantidad == null || cantidad.equals("0")){
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                cantidad = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad","Modificar Cantidad",JOptionPane.WARNING_MESSAGE);
            }
            precio = JOptionPane.showInputDialog(null,"Ingrese precio del producto","Precio",JOptionPane.WARNING_MESSAGE);
            while(precio == null || precio.equals("0")){
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                precio = JOptionPane.showInputDialog(null,"Ingrese precio del producto","Precio",JOptionPane.WARNING_MESSAGE);
            }

            // Actualizacion de cantidad y precio
            fila = tblContenidoFactura.getSelectedRow();
            tm.setValueAt(cantidad, fila, 3);
            tm.setValueAt(precio, fila, 4);
        }
    }

    /**
    * Metodo para eliminar producto de detalle factura
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        DefaultTableModel tm = (DefaultTableModel) tblContenidoFactura.getModel();
        String nom;
        byte opt;

        // Logica para eliminar de detalle factura
        nom = String.valueOf(tm.getValueAt(tblContenidoFactura.getSelectedRow(),1));
        opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea eliminar el producto " +nom+ " de la factura?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == 0){
            tm.removeRow(tblContenidoFactura.getSelectedRow());
        }
    }

    /**
    * Metodo para guardar en BD
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        byte opt;
        String fecha, folio, monto, iva, prov, aux;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet lista = null;
        String sql, sqlUpdate;
        String idproducto, cantidad, preciounitario;

        // Validaciones
        if (txtFolio.getText().equals("") || txtMonto.getText().equals("") || txtIva.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Faltan datos", "Error", JOptionPane.ERROR_MESSAGE);
        }else{

            // Mensaje de confirmacion
            opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea finalizar la factura?", "Confirmar factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // Logica para guardar factura y actualizar stock
            if(opt == 0){
                folio = txtFolio.getText();
                monto = txtMonto.getText();
                iva = txtIva.getText();
                aux = (String)cbProv.getSelectedItem();
                String[] separador = aux.split("-");
                prov = separador[0] +"-"+ separador[1];
                fecha = sdf.format(jDateChooser.getDate());
                sql = "INSERT INTO factura(folio, idproveedor, montoneto, iva, fecha) VALUES("+folio+",'" +prov+ "'," +monto+ "," +iva+ ",'" +fecha+ "')";
                try{
                    sentencia.executeUpdate(sql);
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                }
                for (int i = 0; i < tblContenidoFactura.getRowCount(); i++) {
                    idproducto = (String) tblContenidoFactura.getValueAt(i, 0);
                    cantidad = (String) tblContenidoFactura.getValueAt(i, 3);
                    preciounitario = (String) tblContenidoFactura.getValueAt(i, 4);
                    sql = "INSERT INTO detallefactura(folio, idproveedor, idproducto, cantidad, precio) VALUES("+folio+",'"+prov+"'," +idproducto+ "," +cantidad+ "," +preciounitario+ ")";
                    sqlUpdate = "UPDATE producto SET cantexistencia = cantexistencia + "+cantidad+" WHERE idproducto = "+ idproducto;
                    try{
                       sentencia.executeUpdate(sql);
                       sentencia.executeUpdate(sqlUpdate);
                    }catch(SQLException ee){
                    }
                }
                resetTableFactura();
                resetTable();
                llenarTabla();
                txtFolio.setText("");
                txtMonto.setText("");
                txtIva.setText("");
                jDateChooser.setCalendar(null);
                cbProv.setSelectedIndex(0);
            }
        }
    }

    /**
    * Metodo para actualizar tabla
    */
    public void resetTable(){
        DefaultTableModel tb = (DefaultTableModel) tblProductos.getModel();
        int a = tblProductos.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Metodo para realizar busqueda
    */
    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {

        // Declaracion de variables
        String dato = (String)cmbBusqueda.getSelectedItem();
        String filtro = txtBuscar.getText();
        ResultSet lista = null;
        String[] fila = new String[3];
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();
        String sql = null;

        // Logica para cuando campo de busqueda esta vacio
        if(filtro.equals("")){
            resetTable();
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m WHERE p.codmarca = m.codmarca ORDER BY p.idproducto");
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2]});
                }
            }catch(SQLException ee){
            }
        }else{

            // Logica para realizar busqueda dependiendo del filtro seleccionado
            resetTable();
            if(dato.equals("Id-Producto")){
                sql = "SELECT * FROM producto p , marca m WHERE p.idproducto LIKE '" +filtro+"%' and p.codmarca = m.codmarca ORDER BY p.idproducto";
            }
            if(dato.equals("Nombre")){
                sql = "SELECT * FROM producto p , marca m WHERE p.nombre LIKE '" +filtro+"%' and p.codmarca = m.codmarca ORDER BY p.idproducto";
            }
            if(dato.equals("Marca")){
                sql = "SELECT * FROM producto p , marca m WHERE m.nombremar LIKE '" +filtro+"%' and p.codmarca = m.codmarca ORDER BY p.idproducto";
            }
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery(sql);
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2]});
                }
            }catch(SQLException ee){
            }
        }
    }

    /**
    * Metodo para limpiar tabla
    */
    public void resetTableFactura(){
        DefaultTableModel tb = (DefaultTableModel) tblContenidoFactura.getModel();
        int a = tblContenidoFactura.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Metodo para rellenar combobox
    */
    public void llenarComboProv(){
        ResultSet lista = null;
        try{
            sentencia = conex.createStatement();
            lista = sentencia.executeQuery("SELECT * FROM proveedor");
            while(lista.next()){
                cbProv.addItem(lista.getString("idproveedor") + "- " +lista.getString("nombre"));
            }
        }catch (SQLException ee){
        }
    }

    /**
    * Validacion de campo
    */
    private void txtFolioKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtFolio.getText().length() > 9) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un Folio de mas de 9 digitos", "Error en el ingreso de Folio", JOptionPane.ERROR_MESSAGE);
            txtFolio.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtMontoKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtMonto.getText().length() > 8) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un Monto neto de mas de 8 digitos", "Error en el ingreso de Monto neto", JOptionPane.ERROR_MESSAGE);
            txtMonto.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtIvaKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtIva.getText().length() > 5) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un Iva de mas de 5 digitos", "Error en el ingreso de Iva", JOptionPane.ERROR_MESSAGE);
            txtIva.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtMontoKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtIvaKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtFolioKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        txtMonto = new javax.swing.JTextField();
        txtIva = new javax.swing.JTextField();
        cbProv = new javax.swing.JComboBox<>();
        btnVolver = new javax.swing.JButton();
        btnAgregarProveedor = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblContenidoFactura = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        cmbBusqueda = new javax.swing.JComboBox<>();
        txtBuscar = new javax.swing.JTextField();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Factura");

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Producto", "Nombre", "Marca"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblProductos);
        if (tblProductos.getColumnModel().getColumnCount() > 0) {
            tblProductos.getColumnModel().getColumn(0).setResizable(false);
            tblProductos.getColumnModel().getColumn(1).setResizable(false);
            tblProductos.getColumnModel().getColumn(2).setResizable(false);
        }

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda.png"))); // NOI18N
        jLabel3.setText("Busqueda por:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Detalle Factura:");

        jLabel4.setText("Folio:");

        jLabel5.setText("Proveedor:");

        jLabel6.setText("Fecha:");

        jLabel7.setText("Monto Neto:");

        jLabel8.setText("Iva:");

        txtFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFolioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFolioKeyTyped(evt);
            }
        });

        txtMonto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMontoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMontoKeyTyped(evt);
            }
        });

        txtIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIvaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIvaKeyTyped(evt);
            }
        });

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregarProveedor.setText("Agregar Proveedor");
        btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedorActionPerformed(evt);
            }
        });

        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregarProducto.setText("Agregar Producto");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        tblContenidoFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Producto", "Nombre", "Marca", "Cantidad", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblContenidoFactura.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblContenidoFactura);
        if (tblContenidoFactura.getColumnModel().getColumnCount() > 0) {
            tblContenidoFactura.getColumnModel().getColumn(0).setResizable(false);
            tblContenidoFactura.getColumnModel().getColumn(1).setResizable(false);
            tblContenidoFactura.getColumnModel().getColumn(2).setResizable(false);
            tblContenidoFactura.getColumnModel().getColumn(3).setResizable(false);
            tblContenidoFactura.getColumnModel().getColumn(4).setResizable(false);
        }

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Almacenar Factura");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        cmbBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id-Producto", "Nombre", "Marca" }));

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
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
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(235, 235, 235)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtFolio)
                                                .addComponent(txtMonto)
                                                .addComponent(txtIva)
                                                .addComponent(cbProv, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel2))
                                    .addGap(72, 72, 72)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(315, 315, 315)
                                .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 276, Short.MAX_VALUE)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(202, 202, 202))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(204, 204, 204))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btnAgregarProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVolver)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(373, 373, 373)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 26, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregar)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnModificar)
                        .addComponent(btnEliminar)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto)
                    .addComponent(btnAgregarProveedor)
                    .addComponent(btnVolver))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            java.util.logging.Logger.getLogger(frmFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frmFactura.getInstance().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAgregarProveedor;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cbProv;
    private javax.swing.JComboBox<String> cmbBusqueda;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblContenidoFactura;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtIva;
    private javax.swing.JTextField txtMonto;
}