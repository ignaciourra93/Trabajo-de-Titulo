/**
* Este archivo contiene el codigo para autenticarse en el sistema
* @autor Ignacio Urra & Gabriel Gomez
*/

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;

public class frmLogin extends javax.swing.JFrame {

    // Declaracion de variable
    Statement sentencia;
    Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";

    /**
    * Metodo para conectarse a la BD
    */
    public void conectar(){

        // Declaracion de variable
        String url="";

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

    public frmLogin() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(270,350);
        conectar();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    /**
    * Metodo para validar estructura de RUT
    */
    public int verificacion(String rut){

        // Declaracion de variables
        int dato = 0;
        int busqGuion = rut.indexOf("-");
        int busqPunto = rut.indexOf(".");

        // Logica para validar estructura de rut
        if (busqGuion != -1 && busqPunto != -1) {
            JOptionPane.showMessageDialog(null,"El rut ingresado debe tener GUION pero sin Puntos","Error en el formato del rut",JOptionPane.ERROR_MESSAGE);
            dato = 1;
            return dato;
        }
        if (busqGuion == -1 && busqPunto != -1) {
            JOptionPane.showMessageDialog(null,"El rut ingresado no debe tener Puntos","Error en el formato del rut",JOptionPane.ERROR_MESSAGE);
            dato = 1;
            return dato;
        }
        return dato;
    }

    /**
    * Metodo para autenticarse
    */
    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        String id, contraseña, tipoVerificacion="", idVerificacion="", contraVerificacion="";
        ResultSet lista = null;

        // Logica para autenticarse
        id = txtId.getText();
        int resultado = verificacion(id);
        contraseña = txtContraseña.getText();
        contraseña = DigestUtils.sha1Hex(contraseña);
        try{
            sentencia = conex.createStatement();
            lista = sentencia.executeQuery("SELECT * FROM usuario WHERE idusuario = '" +id+ "'");
            while(lista.next()){
                idVerificacion = lista.getString("idusuario");
                contraVerificacion = lista.getString("contraseña");
                tipoVerificacion = lista.getString("tipo");
            }
        }catch(SQLException ee){
        }

        // Mensaje de error
        if(idVerificacion.equals("") && contraVerificacion.equals("") && resultado == 0){
            JOptionPane.showMessageDialog(null,"Usuario Inexistente","Error en el Ingreso",JOptionPane.ERROR_MESSAGE);
        }

        // Logica de autenticacion lograda
        if(id.equals(idVerificacion)){
            if(contraseña.equals(contraVerificacion)){
                if (tipoVerificacion.equals("A")) {
                    frmMenuAdmin seleccion = new frmMenuAdmin(id);
                    this.dispose();
                    seleccion.pack();
                    seleccion.setVisible(true);
                }
                if (tipoVerificacion.equals("V")) {
                    frmVenta seleccion = new frmVenta(id);
                    this.dispose();
                    seleccion.pack();
                    seleccion.setVisible(true);
                }
            }else{
                JOptionPane.showMessageDialog(null,"Contraseña ingresada incorrecta","Error de Contraseña",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void txtIdFocusGained(java.awt.event.FocusEvent evt) {
        if(txtId.getText().equals("Ingrese su RUT")){
            txtId.setText("");
        }
    }

    private void txtIdFocusLost(java.awt.event.FocusEvent evt) {
        if(txtId.getText().equals("")){
            txtId.setText("Ingrese su RUT");
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
    * Metodo para cerrar software
    */
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
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

        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        txtContraseña = new javax.swing.JPasswordField();
        txtId = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(240, 310));

        jPanel1.setMaximumSize(new java.awt.Dimension(300, 300));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 300));

        lblTitulo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTitulo.setText("Almacen \"Donde Cochato\"");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Inicio de Sesión");

        jLabel2.setText("Usuario:");

        jLabel3.setText("Contraseña:");

        btnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ok.png"))); // NOI18N
        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
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

        txtId.setText("Ingrese su RUT");
        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdFocusLost(evt);
            }
        });
        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdKeyTyped(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/salida.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/usuario.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblTitulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)
                        .addGap(38, 38, 38)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitulo)
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIniciar)
                    .addComponent(btnCancelar))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmLogin().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtId;
}
