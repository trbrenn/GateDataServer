/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author trbrenn
 */
public class DatabaseConnectorBak {

    private Connection Conn; 	
    private Statement Stmt;		
    private ResultSet RS;
    private DataBaseConnData DBCD;
    
    public DatabaseConnectorBak() {
        DBCD = new DataBaseConnData();        
    }
    
    public DatabaseConnectorBak(String url, String cl){
        DBCD = new DataBaseConnData(url, cl);
    }
    
    public DatabaseConnectorBak(DataBaseConnData dbcd){
        DBCD = dbcd;
    }
    
    public void connect() throws Exception {
        Class.forName(DBCD.getCL());
 	Conn = DriverManager.getConnection(DBCD.getURL(), DBCD.getUserName(), DBCD.getPassWord());
        Stmt = Conn.createStatement();
    }

    public void connect(String url, String usr, String passwd, String cl) throws Exception {
	Class.forName(cl);
        Conn = DriverManager.getConnection(url, usr, passwd);
	Stmt = Conn.createStatement();
    }

    public void connect(DataBaseConnData dbc) throws Exception {
        Class.forName(dbc.getCL());
   	Conn = DriverManager.getConnection(dbc.getURL(), dbc.getUserName(), dbc.getPassWord());
        Stmt = Conn.createStatement();
    }

    public Connection getConnection(){
        return Conn;
    }

    public void setConnection(Connection c){
        Conn=c;
    }

    public Statement getStatement(){
        return Stmt;
    }

    public ResultSet getResultSet(){
        return RS;
    }

    // Close the connection to the database.
    public void close() throws SQLException {
        //System.out.println("I'm being closed!!!!");
        RS.close();
        Stmt.close();
        Conn.close();
    }

    public ResultSet executeStatement(String query) throws Exception {
        //System.out.println("Query = "+query+"\n");
        RS = Stmt.executeQuery(query);
        return RS;
    }
    // runs query and retruns the result set.
    public ResultSet runQuery(String query) throws Exception {
        //System.out.println("Query = "+query+"\n");
        RS = Stmt.executeQuery(query);
        return RS;
    }
	
    public int runUpdate(String update) throws Exception {
        //System.out.println("Update = "+update+"\n");
        return Stmt.executeUpdate(update);
    } 
}
