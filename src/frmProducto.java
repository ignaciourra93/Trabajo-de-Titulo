/**
* Este archivo contiene el codigo para la ventana de productos
* para ingregar, modificar y eliminar un Producto de la base de datos
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmProducto extends javax.swing.JFrame {

    // Declaraciones de variables
    Statement sentencia;
    Connection conex;
    String nombre = "almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String usuario;

    public frmProducto() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        llenarComboMarca();
        llenarComboCategoria();
        txtId.setEnabled(false);
        txtNombre.setEnabled(false);
        txtStock.setEnabled(false);
        txtPrecio.setEnabled(false);
        cmbUnidad.setEnabled(false);
        txtStockCritico.setEnabled(false);
        cmbMarca.setEnabled(false);
        cmbCategoria.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        cbProductoSinCodigo.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    public frmProducto(String usuario) {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        llenarTabla();
        llenarComboMarca();
        llenarComboCategoria();
        txtId.setEnabled(false);
        txtNombre.setEnabled(false);
        txtStock.setEnabled(false);
        txtPrecio.setEnabled(false);
        cmbUnidad.setEnabled(false);
        txtStockCritico.setEnabled(false);
        cmbMarca.setEnabled(false);
        cmbCategoria.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
        cbProductoSinCodigo.setEnabled(false);
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
        this.usuario = usuario;
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
        String[] fila = new String[8];
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

        // Logica para poblar la tabla
        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m, categoria c WHERE p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto");
            while(lista.next()){
                fila[0] = lista.getString("idproducto");
                fila[1] = lista.getString("nombre");
                fila[2] = lista.getString("cantexistencia");
                fila[3] = lista.getString("precioactual");
                fila[4] = lista.getString("unidad");
                fila[5] = lista.getString("stockcritico");
                fila[6] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                fila[7] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7]});
            }
        }catch(SQLException ee){

        }
    }

    /**
    * Metodo para rellenar combobox
    */
    public void llenarComboMarca(){

        // Declaracion de variable
        ResultSet lista = null;

        // Logica para rellenar combobox
        try{
            sentencia = conex.createStatement();
            lista = sentencia.executeQuery("SELECT * FROM marca WHERE delete_at = False");
            while(lista.next()){
                cmbMarca.addItem(lista.getString("codmarca")+ "- " + lista.getString("nombremar"));
            }
        }catch (SQLException ee){
        }
    }

    /**
    * Metodo para rellenar combobox
    */
    public void llenarComboCategoria(){

        // Declaracion de variable
        ResultSet lista = null;

        // Logica para rellenar combobox
        try{
            sentencia = conex.createStatement();
            lista = sentencia.executeQuery("SELECT * FROM categoria WHERE delete_at = False");
            while(lista.next()){
                cmbCategoria.addItem(lista.getString("codcategoria") + "- " + lista.getString("nombrecat"));
            }
        }catch (SQLException ee){
        }
    }

    /**
    * Metodo para abrir ventana de categorias
    */
    private void btnCategoriaActionPerformed(java.awt.event.ActionEvent evt) {
        frmCategoria seleccion = new frmCategoria();
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para habilitar campos para agregar un producto
    */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        txtId.setEnabled(true);
        txtNombre.setEnabled(true);
        txtStock.setEnabled(true);
        txtPrecio.setEnabled(true);
        cmbUnidad.setEnabled(true);
        txtStockCritico.setEnabled(true);
        cmbMarca.setEnabled(true);
        cmbCategoria.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnMarca.setEnabled(false);
        btnCategoria.setEnabled(false);
        btnAjuste.setEnabled(false);
        cbProductoSinCodigo.setEnabled(true);
    }

    /**
    * Metodo para habilitar campos para modificar un producto
    */
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        DefaultTableModel tm = (DefaultTableModel) tblProductos.getModel();
        String nom,cod, stockcritico, precio, stock, aux, aux2, aux3;

        if(tblProductos.getSelectedRow()>=0){

            // Logica para activar campos
            txtId.setEnabled(true);
            txtNombre.setEnabled(true);
            txtStock.setEnabled(false);
            txtPrecio.setEnabled(true);
            cmbUnidad.setEnabled(true);
            txtStockCritico.setEnabled(true);
            cmbMarca.setEnabled(true);
            cmbCategoria.setEnabled(true);
            btnGuardar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnAgregar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnMarca.setEnabled(false);
            btnCategoria.setEnabled(false);
            btnAjuste.setEnabled(false);
            cbProductoSinCodigo.setEnabled(true);

            // Logica para obtener datos de tabla y rellenar campos
            cod = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),0));
            txtId.setText(cod);
            txtId.setEnabled(false);
            cbProductoSinCodigo.setEnabled(false);
            nom = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),1));
            txtNombre.setText(nom);
            stock = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),2));
            txtStock.setText(stock);
            precio = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),3));
            txtPrecio.setText(precio);
            aux = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),4));
            cmbUnidad.setSelectedItem(aux);
            stockcritico = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),5));
            txtStockCritico.setText(stockcritico);
            aux2 = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),6));
            cmbMarca.setSelectedItem(aux2);
            aux3 = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),7));
            cmbCategoria.setSelectedItem(aux3);
        }
    }

    /**
    * Metodo para eliminar un producto
    */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String cod = "";
        String nom = "";
        String eliminar = "";
        DefaultTableModel tm = (DefaultTableModel) tblProductos.getModel();

        // Logica para eliminar un producto
        if(tblProductos.getSelectedRow()>=0){
            cod = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),0));
            nom = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),1));
            eliminar = "UPDATE producto SET delete_at = True WHERE idproducto = " + cod;
            try{
                byte opt = (byte) JOptionPane.showConfirmDialog(null, "¿Desea eliminar " + nom + "?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(opt == 0){
                    sentencia.executeUpdate(eliminar);
                    resetTable();
                    llenarTabla();
                    btnEliminar.setFocusable(false);
                }else{
                    tblProductos.clearSelection();
                    btnEliminar.setFocusable(false);
                }
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,"error en eliminar "+ee.getMessage(),"eliminar", 2);
            }
        }
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
    * Metodo para abrir ventana de marcas
    */
    private void btnMarcaActionPerformed(java.awt.event.ActionEvent evt) {
        frmMarca seleccion = new frmMarca();
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para limpiar los campos
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {

        if(btnAgregar.isEnabled()){
            txtId.setEnabled(false);
            txtId.setText("");
            txtNombre.setEnabled(false);
            txtNombre.setText("");
            txtStock.setEnabled(false);
            txtStock.setText("");
            txtPrecio.setEnabled(false);
            txtPrecio.setText("");
            cmbUnidad.setEnabled(false);
            cmbUnidad.setSelectedIndex(0);
            txtStockCritico.setEnabled(false);
            txtStockCritico.setText("");
            cmbMarca.setEnabled(false);
            cmbMarca.setSelectedIndex(0);
            cmbCategoria.setEnabled(false);
            cmbCategoria.setSelectedIndex(0);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnMarca.setEnabled(true);
            btnCategoria.setEnabled(true);
            btnAjuste.setEnabled(true);
            cbProductoSinCodigo.setSelected(false);
            cbProductoSinCodigo.setEnabled(false);
        }

        if(btnModificar.isEnabled()){
            txtId.setEnabled(false);
            txtId.setText("");
            txtNombre.setEnabled(false);
            txtNombre.setText("");
            txtStock.setEnabled(false);
            txtStock.setText("");
            txtPrecio.setEnabled(false);
            txtPrecio.setText("");
            cmbUnidad.setEnabled(false);
            cmbUnidad.setSelectedIndex(0);
            txtStockCritico.setEnabled(false);
            txtStockCritico.setText("");
            cmbMarca.setEnabled(false);
            cmbMarca.setSelectedIndex(0);
            cmbCategoria.setEnabled(false);
            cmbCategoria.setSelectedIndex(0);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnGuardar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnAgregar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnMarca.setEnabled(true);
            btnCategoria.setEnabled(true);
            btnAjuste.setEnabled(true);
            cbProductoSinCodigo.setSelected(false);
            cbProductoSinCodigo.setEnabled(false);
            tblProductos.clearSelection();
        }
    }

    /**
    * Metodo para guardar un producto
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String nom,unidad,cod, stockcritico, precio, stock, codcat, codmar, aux, aux2, insert, update;

        // Validaciones de campos
        if (txtNombre.getText().equals("") || txtStock.getText().equals("") || txtPrecio.getText().equals("") || txtStockCritico.getText().equals("") || txtId.getText().equals("") ){
            JOptionPane.showMessageDialog(null, "Faltan datos", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            if (txtNombre.getText().length() > 20   ){
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso del nombre", JOptionPane.ERROR_MESSAGE);
            }else{
                if (txtNombre.getText().length() > 20){
                JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso del nombre", JOptionPane.ERROR_MESSAGE);
                }else{

                    // Logica para agregar un producto
                    if(btnAgregar.isEnabled()){
                    cod = txtId.getText();
                    nom = txtNombre.getText();
                    stock = txtStock.getText();
                    precio = txtPrecio.getText();
                    unidad = (String)cmbUnidad.getSelectedItem();
                    stockcritico = txtStockCritico.getText();
                    aux = (String)cmbCategoria.getSelectedItem();
                    String[] separador = aux.split("-");
                    codcat = separador[0];
                    aux2 = (String)cmbMarca.getSelectedItem();
                    String[] separador2 = aux2.split("-");
                    codmar = separador2[0];
                    insert = "INSERT INTO producto(idproducto,nombre,cantexistencia,precioactual,unidad,stockcritico,codcategoria,codmarca,delete_at) VALUES("+cod+",'" +nom+ "'," +stock+ "," +precio+ ",'" +unidad+ "'," +stockcritico+ "," +codcat+","+codmar+ ",False)";
                    try{
                        sentencia.executeUpdate(insert);
                        JOptionPane.showMessageDialog(null,"Producto guardado con exito","Guardar producto", 2);
                        resetTable();
                        llenarTabla();
                        txtId.setEnabled(false);
                        txtId.setText("");
                        txtNombre.setEnabled(false);
                        txtNombre.setText("");
                        txtStock.setEnabled(false);
                        txtStock.setText("");
                        txtPrecio.setEnabled(false);
                        txtPrecio.setText("");
                        cmbUnidad.setEnabled(false);
                        cmbUnidad.setSelectedIndex(0);
                        txtStockCritico.setEnabled(false);
                        txtStockCritico.setText("");
                        cmbMarca.setEnabled(false);
                        cmbMarca.setSelectedIndex(0);
                        cmbCategoria.setEnabled(false);
                        cmbCategoria.setSelectedIndex(0);
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        btnAjuste.setEnabled(true);
                        btnEliminar.setEnabled(true);
                        btnMarca.setEnabled(true);
                        btnCategoria.setEnabled(true);
                        cbProductoSinCodigo.setSelected(false);
                        cbProductoSinCodigo.setEnabled(false);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }

                // Metodo para modificar un producto
                if(btnModificar.isEnabled()){
                    cod = txtId.getText();
                    nom = txtNombre.getText();
                    stock = txtStock.getText();
                    precio = txtPrecio.getText();
                    unidad = (String)cmbUnidad.getSelectedItem();
                    stockcritico = txtStockCritico.getText();
                    aux = (String)cmbCategoria.getSelectedItem();
                    String[] separador = aux.split("-");
                    codcat = separador[0];
                    aux2 = (String)cmbMarca.getSelectedItem();
                    String[] separador2 = aux2.split("-");
                    codmar = separador2[0];
                    update = "UPDATE producto SET nombre = '" +nom+ "', cantexistencia = " +stock+ ", precioactual = " +precio+ ", unidad = '" +unidad+ "', stockcritico = " +stockcritico+ ", codmarca = " +codmar+ " , codcategoria = " +codcat+ " WHERE idproducto = "+ cod;
                    try{
                        sentencia.executeUpdate(update);
                        JOptionPane.showMessageDialog(null,"Producto actualizado con exito","Modificar producto", 2);
                        resetTable();
                        llenarTabla();
                        txtId.setEnabled(false);
                        txtId.setText("");
                        txtNombre.setEnabled(false);
                        txtNombre.setText("");
                        txtStock.setEnabled(false);
                        txtStock.setText("");
                        txtPrecio.setEnabled(false);
                        txtPrecio.setText("");
                        cmbUnidad.setEnabled(false);
                        cmbUnidad.setSelectedIndex(0);
                        txtStockCritico.setEnabled(false);
                        txtStockCritico.setText("");
                        cmbMarca.setEnabled(false);
                        cmbMarca.setSelectedIndex(0);
                        cmbCategoria.setEnabled(false);
                        cmbCategoria.setSelectedIndex(0);
                        btnGuardar.setEnabled(false);
                        btnCancelar.setEnabled(false);
                        btnAgregar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                        btnMarca.setEnabled(true);
                        btnAjuste.setEnabled(true);
                        btnCategoria.setEnabled(true);
                        cbProductoSinCodigo.setSelected(false);
                        cbProductoSinCodigo.setEnabled(false);
                    }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,"error insertar"                                 +ee.getMessage(),"Guardar", 2);
                    }
                }
                btnModificar.setEnabled(true);
                }
            }
        }
    }

    /**
    * Metodo para activar logica de producto sin codigo de barra
    */
    private void cbProductoSinCodigoActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        ResultSet lista = null;

        // Logica para obtener id de producto sin codigo de barra
        if(txtId.isEnabled()){
            txtId.setEnabled(false);
            try{
                sentencia = conex.createStatement();
                lista = sentencia.executeQuery("SELECT max(idproducto)+1 FROM producto WHERE idproducto >= 1 AND idproducto <= 5000");
                while(lista.next()){
                    txtId.setText(lista.getString("max(idproducto)+1"));
                }
            }catch (SQLException ee){
            }
        }else{
            txtId.setEnabled(true);
            txtId.setText("");
        }
    }

    /**
    * Metodo para realizar un ajuste al stock de los productos
    */
    private void btnAjusteActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String cod;
        DefaultTableModel tm = (DefaultTableModel) tblProductos.getModel();

        // Logica para abrir ventana de ajuste
        if (tblProductos.getSelectedRow()>=0) {
            cod = String.valueOf(tm.getValueAt(tblProductos.getSelectedRow(),0));
            frmAjuste seleccion = new frmAjuste(cod);
            this.dispose();
            seleccion.pack();
            seleccion.setVisible(true);
        }
    }

    /**
    * Metodo para realizar una busqueda
    */
    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {

        // Declaracion de variable
        String dato = (String)cmbBusqueda.getSelectedItem();
        String filtro = txtBuscar.getText();
        String sql = null;
        String[] fila = new String[8];
        ResultSet lista = null;
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

        // Logica para cuando el campo de busqueda esta vacio
        if(filtro.equals("")){
            resetTable();
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery("SELECT * FROM producto p , marca m, categoria c WHERE p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto");
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("cantexistencia");
                    fila[3] = lista.getString("precioactual");
                    fila[4] = lista.getString("unidad");
                    fila[5] = lista.getString("stockcritico");
                    fila[6] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    fila[7] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7]});
                }
            }catch(SQLException ee){
            }

        // Logica para realizar una busqueda
        }else{
            resetTable();
            if(dato.equals("Id-Producto")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE p.idproducto LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto";
            }
            if(dato.equals("Nombre")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE p.nombre LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto";
            }
            if(dato.equals("Marca")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE m.nombremar LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto";
            }
            if(dato.equals("Categoria")){
                sql = "SELECT * FROM producto p , marca m, categoria c WHERE c.nombrecat LIKE '" +filtro+"%' and p.codmarca = m.codmarca and p.codcategoria = c.codcategoria and p.delete_at = False ORDER BY p.idproducto";
            }
            try{
                sentencia = conex.createStatement();
                lista  = sentencia.executeQuery(sql);
                while(lista.next()){
                    fila[0] = lista.getString("idproducto");
                    fila[1] = lista.getString("nombre");
                    fila[2] = lista.getString("cantexistencia");
                    fila[3] = lista.getString("precioactual");
                    fila[4] = lista.getString("unidad");
                    fila[5] = lista.getString("stockcritico");
                    fila[6] = lista.getString("codmarca") + "- " + lista.getString("nombremar");
                    fila[7] = lista.getString("codcategoria") + "- " + lista.getString("nombrecat");
                    model.addRow(new Object[] {fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7]});
                }
            }catch(SQLException ee){
            }
        }
    }

    /**
    * Metodo para actualizar la tabla
    */
    public void resetTable(){
        DefaultTableModel tb = (DefaultTableModel) tblProductos.getModel();
        int a = tblProductos.getRowCount()-1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount()-1);
        }
    }

    /**
    * Validacion de campo
    */
    private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {

        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtPrecioKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtStockCriticoKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtIdKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(c<'0' || c>'9') evt.consume();
    }

    /**
    * Validacion de campo
    */
    private void txtIdKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtId.getText().length() > 12) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un id-producto de mas de 12 digitos", "Error en el ingreso de id-producto", JOptionPane.ERROR_MESSAGE);
            txtId.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtNombre.getText().length() > 20) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un nombre de mas de 20 caracteres", "Error en el ingreso de nombre", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
    * Validacion de campo
    */
    private void txtStockKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtStock.getText().length() > 5) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un stock de mas de 5 digitos", "Error en el ingreso de Stock", JOptionPane.ERROR_MESSAGE);
            txtStock.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtPrecioKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtPrecio.getText().length() > 5) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un precio de mas de 5 digitos", "Error en el ingreso de precio", JOptionPane.ERROR_MESSAGE);
            txtPrecio.setText("");
        }
    }

    /**
    * Validacion de campo
    */
    private void txtStockCriticoKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtStockCritico.getText().length() > 3) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un stock critico de mas de 5 digitos", "Error en el ingreso de Stock critico", JOptionPane.ERROR_MESSAGE);
            txtStockCritico.setText("");
        }
    }

    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        txtStockCritico = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnCategoria = new javax.swing.JButton();
        btnMarca = new javax.swing.JButton();
        cmbMarca = new javax.swing.JComboBox<>();
        cmbCategoria = new javax.swing.JComboBox<>();
        cmbUnidad = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cbProductoSinCodigo = new javax.swing.JCheckBox();
        btnAjuste = new javax.swing.JButton();
        cmbBusqueda = new javax.swing.JComboBox<>();

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
        jScrollPane2.setViewportView(jTable1);

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusTraversalPolicyProvider(true);
        setMinimumSize(new java.awt.Dimension(600, 400));

        jPanel1.setPreferredSize(new java.awt.Dimension(600, 400));

        lblTitulo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTitulo.setText("Productos");

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/delette.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
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

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id-producto", "Nombre", "Stock", "Precio", "Unidad de Medida", "Stock Critico", "Marca", "Categoria"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProductos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblProductos.setShowHorizontalLines(false);
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
            tblProductos.getColumnModel().getColumn(7).setResizable(false);
        }

        lblBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/busqueda.png"))); // NOI18N
        lblBuscar.setText("Busqueda por:");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel1.setText("Id- Producto: ");

        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdKeyTyped(evt);
            }
        });

        jLabel2.setText("Nombre: ");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });

        jLabel3.setText("Stock:");

        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });

        jLabel4.setText("Precio:");

        txtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioKeyTyped(evt);
            }
        });

        txtStockCritico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockCriticoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockCriticoKeyTyped(evt);
            }
        });

        jLabel5.setText("Unidad:");

        jLabel6.setText("Stock Critico:");

        jLabel7.setText("Marca:");

        jLabel8.setText("Categoria:");

        btnCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnCategoria.setText("Agregar Categoria");
        btnCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriaActionPerformed(evt);
            }
        });

        btnMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/add.png"))); // NOI18N
        btnMarca.setText("Agregar Marca");
        btnMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarcaActionPerformed(evt);
            }
        });

        cmbUnidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gramo", "Kilógramo", "Militritro", "Centímetro Cúbico", "Litro", "Unidad" }));

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

        cbProductoSinCodigo.setText("Producto Sin Codigo");
        cbProductoSinCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProductoSinCodigoActionPerformed(evt);
            }
        });

        btnAjuste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/settings.png"))); // NOI18N
        btnAjuste.setText("Ajuste");
        btnAjuste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjusteActionPerformed(evt);
            }
        });

        cmbBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id-Producto", "Nombre", "Marca", "Categoria" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnMarca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCategoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAjuste, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(224, 224, 224)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(lblBuscar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(303, 303, 303)
                            .addComponent(btnGuardar)
                            .addGap(18, 18, 18)
                            .addComponent(btnCancelar))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGap(304, 304, 304)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtStockCritico, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(18, 18, 18)
                            .addComponent(cbProductoSinCodigo))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(316, 316, 316)
                            .addComponent(lblTitulo))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEliminar)
                    .addComponent(btnModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbProductoSinCodigo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStockCritico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMarca)
                    .addComponent(btnCategoria)
                    .addComponent(btnVolver)
                    .addComponent(btnAjuste))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                .addContainerGap())
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
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProducto().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAjuste;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCategoria;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMarca;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnVolver;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbProductoSinCodigo;
    private javax.swing.JComboBox<String> cmbBusqueda;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbMarca;
    private javax.swing.JComboBox<String> cmbUnidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtStockCritico;
}