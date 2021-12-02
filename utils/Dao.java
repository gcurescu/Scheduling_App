package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Gentillo_Curescu
 * Dao Class for database connection 
 */
public class Dao {
	
	
	private String dbURL = "jdbc:mysql://localhost:3306/client_schedule";
	private String dbName = "sqlUser";
    private String dbPassword= "Passw0rd!";
	private String dbDriver = "com.mysql.cj.jdbc.Driver";
	
	

	public Dao() {
		super();
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	/**This method is used for establishing a connection to the DB.
	 * @return connection object
	 */
	public Connection getConnection() {

		Connection con = null;

		try {

			con = DriverManager.getConnection(dbURL, dbName, dbPassword);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;

	}

	

}
