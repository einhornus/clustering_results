package sqlTestingDec27;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import projClasses.DbDataObject;
import simplemysql.SocketMySql;
//import sync.system.SyncUtils;

public class MySqlMachine {

	DbDataObject	dbData;
	String			query;
	

	// public static void main(String[] args) throws SQLException {

	public MySqlMachine() throws SQLException {
		try {
			this.query = "SELECT * FROM DataLive LIMIT 10";

			SocketMySql sql = new SocketMySql(query);
			dbData = sql.getDbData();
			// dbData.printData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[ERROR - Feb 2, 2015 4:11:02 PM] \n\t [MySqlMachine::MySqlMachine] \n\t TYPE = SQLException | VAR = e");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( SyncUtils.getDateBox() + " -> [MySqlMachine::MySqlMachine] Exception");
		}

	}
	

	public MySqlMachine(String query) throws SQLException {


		try {
			if (query.length() < 5) {
				System.out.println("ERROR: EMPTY EMPTY QUERY");
				throw new SQLException();
			}
			this.query = query; // "SELECT * FROM DataLive";

			SocketMySql sql = new SocketMySql(this.query);
			dbData = sql.getDbData();
			// dbData.printData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::MySqlMachine] SQLException");
		} catch (Exception e) {
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::MySqlMachine] Exception");

		}
	}

	/**
	 * @return the dbData
	 */
	public DbDataObject getDbData() {
		return dbData;
	}
	
	public synchronized void insertQuery( String query) {
		try {
			SocketMySql sql = new SocketMySql();
			sql.insertQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::insertQuery]");
		}
	}
}

--------------------

package edu.umich.robustopt.microsoft;

import java.sql.Connection;
import java.sql.DriverManager;

import edu.umich.robustopt.dblogin.DatabaseLoginConfiguration;

public class MicrosoftConnection {

	public static Connection createConnection(DatabaseLoginConfiguration databaseLogin) {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			conn = DriverManager.getConnection(
					String.format("jdbc:sqlserver://%s:%s;databasename=%s", 
							databaseLogin.getDBhost(), databaseLogin.getDBport(), databaseLogin.getDBname()), 
							databaseLogin.getDBuser(), databaseLogin.getDBpasswd());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}

--------------------

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class Logger {

public static void LogEvent(String user, String mode, String Event){

		try{
    		//HARD CODED DATABASE NAME:
    		Connection database = DriverManager.getConnection("jdbc:sqlite:Project2.data");
    	       //create a statement object which will be used to relay a
    	       //sql query to the database
    		PreparedStatement prep = database.prepareStatement(
		            "Insert into Logs (Username, Mode, Event, Timestamp) values (?, ?, ?, ?);");

    		prep.setString(1, user);
    		prep.setString(2, mode);
    		prep.setString(3, Event);
				
		    // Add a time stamp that is the current time this function was executed
    		prep.setString(4, (new Timestamp(System.currentTimeMillis())).toString());

    		prep.execute();

    		database.close();


    		}catch(SQLException ex){
    			ex.printStackTrace();

    		}

	}

}

--------------------

