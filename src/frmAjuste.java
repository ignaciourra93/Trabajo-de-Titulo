/**
* Este archivo contiene el codigo para la ventana de ajustes
* para ingregar un ajuste a la base de datos y modificar stock de productos
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class frmAjuste extends javax.swing.JFrame {

   // Declaracion de variables
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String cod;

    public frmAjuste() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(300,350);
        conectar();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    public frmAjuste(String cod) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(300,350);
        conectar();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
        this.cod = cod;
        txtId.setText(cod);
        txtId.setEnabled(false);
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
    * Metodo para volver al menu anterior
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        frmProducto seleccion = new frmProducto();
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para obtener fecha y hora actual
    */
    public String fechayhora(){

        // Declaracion de variables
        Calendar date = Calendar.getInstance();
        int año, mes, dia, hora, minuto, segundo;
        String mesF, diaF, horaF, minutoF, segundoF, fechayhora;

        // Logica para obtener fecha y hora actual
        año = date.get(Calendar.YEAR);
        mes = date.get(Calendar.MONTH) + 1;
        dia = date.get(Calendar.DAY_OF_MONTH);
        hora = date.get(Calendar.HOUR_OF_DAY);
        minuto = date.get(Calendar.MINUTE);
        segundo = date.get(Calendar.SECOND);

        mesF = formato(mes);
        diaF = formato(dia);
        horaF = formato(hora);
        minutoF = formato(minuto);
        segundoF = formato(segundo);
        fechayhora = año + "-" + mesF + "-" + diaF + " " + horaF + ":" + minutoF + ":" + segundoF;

        return fechayhora;
    }

    /**
    * Metodo para formatear la fecha
    */
    public String formato(int dato){

        // Declaracion de variable
        String formateado;

        // Logica de formateo
        if(Integer.toString(dato).length()<2){
            formateado = "0"+dato;
        }else{
            formateado = Integer.toString(dato);
        }
        return formateado;
    }

    /**
    * Metodo para guardar un ajuste
    */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String fecha, idproducto, stock, motivo;
        String sql, sqlUpdate;
        ResultSet lista = null;

        // Validacion de campos
        if (txtId.getText().equals("") || txtStock.getText().equals("") || txtMotivo.getText().equals("") ){
            JOptionPane.showMessageDialog(null, "Faltan datos", "Error", JOptionPane.ERROR_MESSAGE);
        }else{

            // Logica para guardar nuevo ajuste
            byte opt = (byte) JOptionPane.showConfirmDialog(null, "¿Esta seguro de que desea finalizar el ajuste?", "Confirmar ajuste", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(opt == 0){
                fecha = fechayhora();
                idproducto = txtId.getText();
                stock = txtStock.getText();
                motivo = txtMotivo.getText();
                sql = "INSERT INTO ajusteproducto(idproducto, stocknuevo, fecha, motivo) VALUES("+idproducto+"," +stock+ ",'" +fecha+ "','" +motivo+ "')";
                try{
                    sentencia.executeUpdate(sql);
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,"error insertar"+ee.getMessage(),"Guardar", 2);
                }

                // Logica para actualizar stock
                sqlUpdate = "UPDATE producto SET cantexistencia = cantexistencia + "+stock+" WHERE idproducto = "+ idproducto;
                try{
                   sentencia.executeUpdate(sqlUpdate);
                }catch(SQLException ee){
                }

                // Volver al menu anterior
                frmProducto seleccion = new frmProducto();
                this.dispose();
                seleccion.pack();
                seleccion.setVisible(true);
            }
        }
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
    private void txtStockKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if(!isNumber(c) && !isValidSignal(c)) evt.consume();
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
    private void txtStockKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtStock.getText().length() > 3) {
            int stock;
            stock = Integer.parseInt(txtStock.getText());
            if (stock > 0){
                JOptionPane.showMessageDialog(null, "No puede ingresar un stock de mas de 3 digitos", "Error en el ingreso de stock", JOptionPane.ERROR_MESSAGE);
                txtStock.setText("");
            }else{
                if (txtStock.getText().length() > 4) {
                JOptionPane.showMessageDialog(null, "No puede ingresar un stock de mas de 3 digitos", "Error en el ingreso de stock", JOptionPane.ERROR_MESSAGE);
                txtStock.setText("");
                }
            }
        }
    }

    /**
    * Validacion de campo
    */
    private void txtMotivoKeyReleased(java.awt.event.KeyEvent evt) {
        if (txtMotivo.getText().length() > 40) {
            JOptionPane.showMessageDialog(null, "No puede ingresar un motivo de mas de 40 caracteres", "Error en el ingreso del motivo", JOptionPane.ERROR_MESSAGE);
            txtMotivo.setText("");
        }
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
        if(ch == '-'){
            return true;
        }
        return false;
    }


    /**
    * Codigo autogenerado
    */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Ajuste Producto");

        jLabel2.setText("Id-Producto:");

        jLabel3.setText("Ajuste:");

        jLabel5.setText("Motivo:");

        txtMotivo.setColumns(20);
        txtMotivo.setRows(5);
        txtMotivo.setMaximumSize(new java.awt.Dimension(164, 94));
        txtMotivo.setMinimumSize(new java.awt.Dimension(164, 94));
        txtMotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMotivoKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtMotivo);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
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

        txtStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnGuardar)
                        .addGap(38, 38, 38)
                        .addComponent(btnCancelar)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(frmAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAjuste().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextArea txtMotivo;
    private javax.swing.JTextField txtStock;
}