/**
* Este archivo contiene el codigo para la ventana de informes
* para crear un informe diario o mensual
* @autor Ignacio Urra & Gabriel Gomez
*/

import com.mysql.jdbc.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import Modelo.Conexion;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class frmInforme extends javax.swing.JFrame {

    // Declaracion de variables
    Statement sentencia;
    java.sql.Connection conex;
    String nombre="almacen";
    String usu = "root";
    String pass = "";
    String msj = "";
    String usuario;

    public frmInforme() {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
    }

    public frmInforme(String usuario) {
        initComponents();
        this.setLocationRelativeTo(null);
        conectar();
        this.setIconImage(new ImageIcon(getClass().getResource("Iconos/Icon Principal.png")).getImage());
        this.usuario = usuario;
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
    * Metodo para volver al menu anterior
    */
    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {
        frmMenuAdmin seleccion = new frmMenuAdmin(usuario);
        this.dispose();
        seleccion.pack();
        seleccion.setVisible(true);
    }

    /**
    * Metodo para realizar un reporte mensual
    */
    private void btnReporteMensualActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variables
        Object objetoMes = JOptionPane.showInputDialog(null,"Seleccione un MES","Ingreso MES",JOptionPane.QUESTION_MESSAGE,null, new Object[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},"");
        String mes = objetoMes.toString();
        String año, inicioJ,finJ, sql, sql2;
        double total = 0, ivaMes = 0, ivaFactura = 0, ivaPagar = 0;
        ResultSet lista = null;
        ResultSet lista2 = null;
        DecimalFormat formatea = new DecimalFormat("###,###.##");

        // Switch para establecer mes
        switch(mes) {
            case "Enero":
              mes = "01";
              break;
            case "Febrero":
              mes = "02";
              break;
            case "Marzo":
              mes = "03";
              break;
            case "Abril":
              mes = "04";
              break;
            case "Mayo":
              mes = "05";
              break;
            case "Junio":
              mes = "06";
              break;
            case "Julio":
              mes = "07";
              break;
            case "Agosto":
              mes = "08";
              break;
            case "Septiembre":
              mes = "09";
              break;
            case "Octubre":
              mes = "10";
              break;
            case "Noviembre":
              mes = "11";
              break;
            case "Dicimienbre":
              mes = "12";
              break;
        }

        // Pregunta y validacion de año
        año = JOptionPane.showInputDialog(null,"ingrese año del informe","Ingreso AÑO",JOptionPane.QUESTION_MESSAGE);
        while (año.length() < 4){
            año = JOptionPane.showInputDialog(null,"ingrese año valido","Error Ingreso año",JOptionPane.QUESTION_MESSAGE);
        }
        inicioJ = año + "/" + mes + "/01";
        finJ = año + "/" +mes + "/31";

        // Obtencion de datos relativos a la fecha
        sql = "SELECT SUM(total) FROM totaldiario WHERE fecha BETWEEN '" + inicioJ + "' AND '" + finJ + "'";
        sql2 = "SELECT SUM(iva) FROM factura WHERE fecha BETWEEN '" + inicioJ + "' AND '" + finJ + "'";

        try{
            sentencia = conex.createStatement();
            lista  = sentencia.executeQuery(sql);
            while(lista.next()){
                total = lista.getDouble("SUM(total)");
            }
        }catch(SQLException ee){
        }

        try{
            sentencia = conex.createStatement();
            lista2  = sentencia.executeQuery(sql2);
            while(lista2.next()){
                ivaFactura = lista2.getDouble("SUM(iva)");
            }
        }catch(SQLException ee){
        }

        // Calculo del iva
        ivaMes = total * 0.19;
        ivaPagar = ivaMes - ivaFactura;

        // Construccion de reporte
        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();

            Map parametro = new HashMap();
            parametro.put("inicio", inicioJ);
            parametro.put("fin", finJ);
            parametro.put("total", formatea.format((int) Math.round(total)));
            parametro.put("ivaMes", formatea.format((int) Math.round(ivaMes)));
            parametro.put("ivaFactura", formatea.format((int) Math.round(ivaFactura)));
            parametro.put("ivaEstimado", formatea.format((int) Math.round(ivaPagar)));

            JasperReport reporte = null;
            String path = "src\\Reportes\\ReporteMensual.jasper";

            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, conn);

            JasperViewer view = new JasperViewer(jprint, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(frmInforme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
    * Metodo para crear un reporte diario
    */
    private void btnReporteDiarioActionPerformed(java.awt.event.ActionEvent evt) {

        // Declaracion de variable
        String fecha = fecha();
        String path = "";

        // Construccion de reporte
        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();

            Map parametro = new HashMap();
            parametro.put("fecha", fecha);

            JasperReport reporte = null;
            path = "src\\Reportes\\ReporteDiario.jasper";

            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

            JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, conn);

            JasperViewer view = new JasperViewer(jprint, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(frmInforme.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
    * Metodo para obtener fecha y hora actual formateada
    */
    public String fecha(){

        // Declaracion de variable
        Calendar date = Calendar.getInstance();
        int año, mes, dia;
        String mesF, diaF,fechayhora;

        // Obtencion de fecha y formateo
        año = date.get(Calendar.YEAR);
        mes = date.get(Calendar.MONTH) + 1;
        dia = date.get(Calendar.DAY_OF_MONTH);

        mesF = formato(mes);
        diaF = formato(dia);
        fechayhora = año + "-" + mesF + "-" + diaF;
        return fechayhora;
    }

    /**
    * Metodo para formatear dato
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
        jLabel1 = new javax.swing.JLabel();
        btnReporteDiario = new javax.swing.JButton();
        btnReporteMensual = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Informe");

        btnReporteDiario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/folder.png"))); // NOI18N
        btnReporteDiario.setText("Generar Informe Diario");
        btnReporteDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteDiarioActionPerformed(evt);
            }
        });

        btnReporteMensual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/folder.png"))); // NOI18N
        btnReporteMensual.setText("Generar Informe Mensual");
        btnReporteMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteMensualActionPerformed(evt);
            }
        });

        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/back.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
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
                        .addComponent(btnReporteDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnReporteMensual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReporteDiario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReporteMensual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
            java.util.logging.Logger.getLogger(frmInforme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmInforme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmInforme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmInforme.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmInforme().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnReporteDiario;
    private javax.swing.JButton btnReporteMensual;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
}