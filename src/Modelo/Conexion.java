/**
* Este archivo contiene el codigo para conectarse a la BD y
* solo se utiliza para la creacion de reportes.
* @autor Ignacio Urra & Gabriel Gomez
*/
package Modelo;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    // Declaracion de variables
    private final String base = "almacen";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost:3306/" + base;
    private Connection con = null;

    /**
    * Metodo para conectarse a la BD
    */
    public Connection getConexion(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(this.url, this.user, this.password);

        } catch(SQLException e)
        {
            System.err.println(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
      return con;
    }
}