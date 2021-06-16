/**
* Este archivo contiene el codigo para la ventana de ventas
* para guardar una nueva venta a la base de datos
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmVenta extends javax.swing.JFrame {

    // Declaracion de variables
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String usuarioLogeado;

    public frmVenta() {
        initComponents();
        conectar();
        llenarTabla();
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    public frmVenta(String usuario) {
        initComponents();
        conectar();
        llenarTabla();
        this.setLocationRelativeTo(null);
        this.usuarioLogeado = usuario;
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    /**
    * Metodo para conectarse a la BD
    */
    public void conectar(){

        // Declaracion de variables
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

        // Declaracion de variables
        ResultSet lista = null;
        String[] fila = new String[8];
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

        // Logica para poblar tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m, categoria c WHERE p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto");
            while(lista.next()){
                fila[0] = lista.getString("idproducto");
                fila[1] = lista.getString("nombre");
                fila[2] = lista.getString("cantexistencia");
                fila[3] = lista.getString("precioactual");
                fila[4] = lista.getString("unidad");
                fila[5] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                fila[6] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6]});
            }
        }catch(SQLException ee){
        }
    }

    /**
    * Metodo para cerrar session y guardar datos historicos
    */
    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        Calendar date = Calendar.getInstance();
        int año, mes, dia;
        String fecha = "";
        String inicio = "";
        String fin = "";
        String totalR = "";
        String insert = "";
        String update = "";
        String total = null;
        String min = null;
        String max = null;
        ResultSet lista = null, lista2 = null;

        // Obtencion de fecha actual
        año = date.get(Calendar.YEAR);
        mes = date.get(Calendar.MONTH) + 1;
        dia = date.get(Calendar.DAY_OF_MONTH);
        fecha = año + "-" + formato(mes) + "-" + formato(dia);

        // Logica para obtener datos de ventas realizas durante el dia
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM totaldiario WHERE fecha = '"+fecha+"'");
            while(lista.next()){
                inicio = lista.getString("inicionroboleta");
                fin = lista.getString("finnroboleta");
                totalR = lista.getString("total");
            }
        }catch(SQLException ee){
        }

        try{
            sentencia = conex.createStatement();
            lista2  = sentencia.executeQuery("SELECT SUM(total), MAX(nroboleta), MIN(nroboleta) FROM venta WHERE fecha LIKE '"+fecha+"%'");
            while(lista2.next()){
                total = lista2.getString("SUM(total)");
                min = lista2.getString("MIN(nroboleta)");
                max = lista2.getString("MAX(nroboleta)");
            }
        }catch(SQLException ee){
        }

        // Logica para guardar datos historicos de ventas
        if (total != null && min != null && max != null && inicio.equals("") && fin.equals("") && totalR.equals("")) {
            insert = "INSERT INTO totaldiario(fecha, inicionroboleta, finnroboleta, total) VALUES('"+fecha+"',"+min+","+max+","+total+")";
            try{
                sentencia.executeUpdate(insert);
                JOptionPane.showMessageDialog(null,"Ventas del día almacenadas con exito","Guardar Ventas", 2);
            }catch(SQLException ee){
            }
        }

        if (total != null && min != null && max != null && !inicio.equals("") && !fin.equals("") && !totalR.equals("")) {
            System.out.println("entre al update");
            update = "UPDATE totaldiario SET inicionroboleta = "+min+", finnroboleta = "+max+", total = "+total+" WHERE fecha = '"+ fecha +"'";
            try{
                sentencia.executeUpdate(update);
                JOptionPane.showMessageDialog(null,"Ventas del día actualizadas con exito","Guardar Ventas", 2);
            }catch(SQLException ee){
            }
        }

        if (total == null && min == null && max == null) {
            JOptionPane.showMessageDialog(null, "Hasta ahora, hoy no se han realizado ventas", "Ventas", JOptionPane.WARNING_MESSAGE);
        }

        System.exit(0);
    }

    /**
    * Metodo para agregar un producto al detalle de la venta
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblProductos.getModel();
        DefaultTableModel model = (DefaultTableModel) tblContenidoVenta.getModel();
        int aux = 0;
        String cantidad = "";
        String[] fila = new String[8];

        // Pregunta por cantidad de productos a vender
        if(tblProductos.getSelectedRow()>=0){
            cantidad = JOptionPane.showInputDialog(null,"Ingrese cantidad solicitada","Cantidad",JOptionPane.WARNING_MESSAGE);

            // Validaciones de cantidad
            while(aux == 0){
                if(cantidad == null || cantidad.equals("0") || cantidad.equals("")){
                    JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                    cantidad = JOptionPane.showInputDialog(null,"Ingrese cantidad solicitada","Cantidad",JOptionPane.WARNING_MESSAGE);
                    aux = 0;
                }else if(Integer.parseInt(cantidad) > Integer.parseInt(String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),2))) ){
                    JOptionPane.showMessageDialog(null, "La cantidad supera la existencia actual", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                    cantidad = JOptionPane.showInputDialog(null,"Ingrese cantidad solicitada","Cantidad",JOptionPane.WARNING_MESSAGE);
                    aux = 0;
                }else{
                    aux=1;
                }
            }

            // Logica para agregar un producto al detalle venta
            fila[0] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),0));
            fila[1] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),1));
            fila[2] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),5));
            fila[3] = cantidad;
            fila[4] = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),3));
            fila[5] = String.valueOf(Integer.parseInt(cantidad) * Integer.parseInt(fila[4]));
            model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]});
            tblProductos.clearSelection();
            totalVenta();
        }
    }

    /**
    * Metodo para cancelar venta y limpiar ventana
    */
    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {
        byte opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea cancelar la venta?", "Confirmar cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == 0){
            lblTotalVenta.setText("0");
            resetTableContenido();
        }
    }

    /**
    * Metodo para modificar cantidad de producto en detalle venta
    */
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String cantidad = "";
        String precio = "";
        int fila;
        DefaultTableModel tm = (DefaultTableModel) tblContenidoVenta.getModel();

        // Validacion de nueva cantidad
        if(tblContenidoVenta.getSelectedRow()>=0){
            cantidad = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad","Modificar Cantidad",JOptionPane.WARNING_MESSAGE);
            while(cantidad == null || cantidad.equals("0")){
                JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad valida", "Error en el ingreso de cantidad", JOptionPane.ERROR_MESSAGE);
                cantidad = JOptionPane.showInputDialog(null,"Ingrese la nueva cantidad","Modificar Cantidad",JOptionPane.WARNING_MESSAGE);
            }

            // Actualizar detalle venta
            fila= tblContenidoVenta.getSelectedRow();
            tm.setValueAt(cantidad, fila, 3);
            precio = String.valueOf(tm.getValueAt(tblContenidoVenta.getSelectedRow(),4));
            tm.setValueAt(String.valueOf(Integer.parseInt(cantidad) * Integer.parseInt(precio)), fila, 5);
            totalVenta();
        }
    }

    /**
    * Metodo para eliminar producto de detalle venta
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        DefaultTableModel tm = (DefaultTableModel) tblContenidoVenta.getModel();
        String nom = "";
        byte opt;

        //Logica para eliminar producto de detalle venta
        nom = String.valueOf(tm.getValueAt(tblContenidoVenta.getSelectedRow(),1));
        opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea eliminar el producto " +nom+ " de la venta?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == 0){
            tm.removeRow(tblContenidoVenta.getSelectedRow());
            totalVenta();
        }
    }

    /**
    * Metodo para guardar nueva venta en la BD
    */
    private void btnRealizarVentaActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        byte opt;
        int total=0;
        int nroboleta=0;
        String fecha, id,sql, sqlUpdate,idproducto, cantidad, preciounitario;
        ResultSet lista = null;

        // Pregunta de confirmacion y validacion
        opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea finalizar la venta?", "Confirmar venta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(opt == 0){
            total = Integer.parseInt(lblTotalVenta.getText());
            if (total == 0){
                JOptionPane.showMessageDialog(null, "La venta se encuentra vacia", "Venta vacia", JOptionPane.ERROR_MESSAGE);
            }else{
                try{

                    // Obtener id de nueva boleta
                    sentencia = conex.createStatement();
                    lista = sentencia.executeQuery("SELECT max(nroboleta) FROM venta");
                    while(lista.next()){
                        nroboleta = lista.getInt("max(nroboleta)") + 1;
                    }
                }catch (SQLException ee){
                }
                fecha = fechayhora();
                id = usuarioLogeado;

                // Guardar venta menor
                if (total <= 300) {
                    sql = "INSERT INTO ventamenor(fecha, monto) VALUES('" +fecha+ "'," +total+ ")";
                    try{
                        sentencia.executeUpdate(sql);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }else{

                    // Guardar venta normal
                    sql = "INSERT INTO venta(nroboleta, fecha, total, idusuario) VALUES("+nroboleta+",'" +fecha+ "'," +total+ ",'" +id+ "')";
                    try{
                        sentencia.executeUpdate(sql);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                    for (int i = 0; i < tblContenidoVenta.getRowCount(); i++) {
                        idproducto = (String) tblContenidoVenta.getValueAt(i, 0);
                        cantidad = (String) tblContenidoVenta.getValueAt(i, 3);
                        preciounitario = (String) tblContenidoVenta.getValueAt(i, 4);
                        sql = "INSERT INTO detalleventa(nroboleta, idproducto, cantidad, preciounitario) VALUES("+nroboleta+"," +idproducto+ "," +cantidad+ "," +preciounitario+ ")";
                        sqlUpdate = "UPDATE producto SET cantexistencia = cantexistencia - "+cantidad+" WHERE idproducto = "+ idproducto;
                        System.out.println(sql);
                        System.out.println(sqlUpdate);
                        try{
                           sentencia.executeUpdate(sql);
                        }catch(SQLException ee){
                        }
                        try{
                           sentencia.executeUpdate(sqlUpdate);
                        }catch(SQLException ee){
                        }
                    }
                    resetTableProductos();
                    llenarTabla();
                    lblTotalVenta.setText("0");
                    resetTableContenido();
                }
            }
        }
    }

    /**
    * Metodo para realizar busqueda
    */
    private void txtBusquedaKeyReleased(java.awt.event.KeyEvent evt) {
        filtro();
    }

    private void txtBusquedaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /**
    * Metodo para realizar busqueda
    */
    public void filtro(){

        //Declaracion de variable
        String filtro = txtBusqueda.getText();
        String dato = (String)cmbBusqueda.getSelectedItem();
        String sql = null;
        ResultSet lista = null;
        String[] fila = new String[8];
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

        // Logica para cuando el campo de busqueda esta vacio
        if(filtro.equals("")){
            resetTableProductos();
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m, categoria c WHERE p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto");
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("cantexistencia");
                    fila[3] = lista.getString("precioactual");
                    fila[4] = lista.getString("unidad");
                    fila[5] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    fila[6] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6]});
                }
            }catch(SQLException ee){
            }
        }else{

            // Realizar busqueda dependiendo del filtro seleccionado
            resetTableProductos();
            if(dato.equals("Id-Producto")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE p.idproducto LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto";
            }
            if(dato.equals("Nombre")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE p.nombre LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto";
            }
            if(dato.equals("Marca")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE m.nombremar LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto";
            }
            if(dato.equals("Categoria")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE c.nombrecat LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.cantexistencia > 0 ORDER BY p.idproducto";
            }

            // Poblar tabla en base a busqueda
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery(sql);
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("cantexistencia");
                    fila[3] = lista.getString("precioactual");
                    fila[4] = lista.getString("unidad");
                    fila[5] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    fila[6] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6]});
                }
            }catch(SQLException ee){
            }
        }
    }

    /**
    * Metodo para calcular el total de venta
    */
    public void totalVenta(){
        int total = 0;
        for (int i = 0; i < tblContenidoVenta.getRowCount(); i++) {
            total = total + Integer.parseInt((String) tblContenidoVenta.getValueAt(i, 5));
        }
        lblTotalVenta.setText(Integer.toString(total));
    }

    /**
    * Metodo para limpiar tabla
    */
    public void resetTableProductos(){
        DefaultTableModel tb = (DefaultTableModel) tblProductos.getModel();
        int a = tblProductos.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Metodo para limpiar tabla
    */
    public void resetTableContenido(){
        DefaultTableModel tb = (DefaultTableModel) tblContenidoVenta.getModel();
        int a = tblContenidoVenta.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Metodo para obtener fecha y hora actual
    */
    public String fechayhora(){

        // Declaracion de variable
        Calendar date = Calendar.getInstance();
        int año, mes, dia, hora, minuto, segundo;
        String mesF, diaF, horaF, minutoF, segundoF, fechayhora;

        // Obtener fecha y hora actual
        año = date.get(Calendar.YEAR);
        mes = date.get(Calendar.MONTH) + 1;
        dia = date.get(Calendar.DAY_OF_MONTH);
        hora = date.get(Calendar.HOUR_OF_DAY);
        minuto = date.get(Calendar.MINUTE);
        segundo = date.get(Calendar.SECOND);

        // Formatear fecha y hora
        mesF = formato(mes);
        diaF = formato(dia);
        horaF = formato(hora);
        minutoF = formato(minuto);
        segundoF = formato(segundo);
        fechayhora = año + "-" + mesF + "-" + diaF + " " + horaF + ":" + minutoF + ":" + segundoF;
        return fechayhora;
    }

    /**
    * Metodo para formatear fecha y hora
    */
    public String formato(int dato){
        String formateado;
        if(Integer.toString(dato).length()<2){
            formateado = "0"+dato;
        }else{
            formateado = Integer.toString(dato);
        }
        return formateado;
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbBusqueda = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblContenidoVenta = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        lblVenta = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnRealizarVenta = new javax.swing.JButton();
        btnCancelarVenta = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        lblTotalVenta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTitulo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtTitulo.setText("Ventas");

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Producto", "Nombre", "Cant-Existencia", "Precio", "Unidad", "Marca", "Categoria"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblProductos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblProductos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblProductos);
        if (tblProductos.getColumnModel().getColumnCount() > 0) {
            tblProductos.getColumnModel().getColumn(0).setResizable(false);
            tblProductos.getColumnModel().getColumn(1).setResizable(false);
            tblProductos.getColumnModel().getColumn(2).setResizable(false);
            tblProductos.getColumnModel().getColumn(3).setResizable(false);
            tblProductos.getColumnModel().getColumn(4).setResizable(false);
            tblProductos.getColumnModel().getColumn(5).setResizable(false);
            tblProductos.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel1.setText("Productos Disponibles");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda.png"))); // NOI18N
        jLabel2.setText("Busqueda por:");

        cmbBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id-Producto", "Nombre", "Marca", "Categoria" }));

        jLabel3.setText(":");

        txtBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaActionPerformed(evt);
            }
        });
        txtBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusquedaKeyReleased(evt);
            }
        });

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnAgregar.setText("Agregar Producto");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        tblContenidoVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-Producto", "Nombre", "Marca", "Cantidad Solicitada", "Precio Unitario", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblContenidoVenta.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblContenidoVenta.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblContenidoVenta);
        if (tblContenidoVenta.getColumnModel().getColumnCount() > 0) {
            tblContenidoVenta.getColumnModel().getColumn(0).setResizable(false);
            tblContenidoVenta.getColumnModel().getColumn(1).setResizable(false);
            tblContenidoVenta.getColumnModel().getColumn(2).setResizable(false);
            tblContenidoVenta.getColumnModel().getColumn(3).setResizable(false);
            tblContenidoVenta.getColumnModel().getColumn(4).setResizable(false);
            tblContenidoVenta.getColumnModel().getColumn(5).setResizable(false);
        }

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        lblVenta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblVenta.setText("Total Venta: $");

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnRealizarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnRealizarVenta.setText("Realizar Venta");
        btnRealizarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarVentaActionPerformed(evt);
            }
        });

        btnCancelarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/minus.png"))); // NOI18N
        btnCancelarVenta.setText("Cancelar Venta");
        btnCancelarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVentaActionPerformed(evt);
            }
        });

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/salida.png"))); // NOI18N
        btnCerrar.setText("Cerrar Sesión");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        lblTotalVenta.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblTotalVenta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalVenta.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(205, 205, 205)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(173, 173, 173)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRealizarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(lblVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancelarVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCerrar)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(401, 401, 401)
                        .addComponent(txtTitulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGap(188, 419, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVenta)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnRealizarVenta)
                    .addComponent(lblTotalVenta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelarVenta)
                    .addComponent(btnCerrar))
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
                .addGap(0, 11, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(frmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVenta().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRealizarVenta;
    private javax.swing.JComboBox<String> cmbBusqueda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotalVenta;
    private javax.swing.JLabel lblVenta;
    private javax.swing.JTable tblContenidoVenta;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JLabel txtTitulo;
}