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
import java.sql.PreparedStatement;

/**
 *
 * @author trbrenn
 */
public class DataBaseConnection {
    private Connection          connect   = null;
    private Statement           statement = null;
    private ResultSet           resultSet = null;
    private DataBaseConnData    DBCD      = null;
    private PreparedStatement   preparedStatement = null;
    
    public DataBaseConnection(DataBaseConnData db) {
        DBCD = db;
        DBCD = new DataBaseConnData();
        DBCD.setURL("jdbc:mysql://192.168.21.2:3306/xbeedata");
	DBCD.setUserName("XbeeUser");
	DBCD.setPassWord("GateData!");
	DBCD.setCL("com.mysql.jdbc.Driver");
    }
    
    public void connect() throws Exception {
        Class.forName(DBCD.getCL());
        String conn = DBCD.getURL()+"?user="+DBCD.getUserName()+"&password="+DBCD.getPassWord();
        //System.err.println(conn);
        connect = DriverManager.getConnection(conn);
        statement = connect.createStatement();
    }
    
    public ResultSet executeStatement(String query) throws SQLException {
        return statement.executeQuery(query);
    }
    
    public int executeInsert(String query) throws SQLException {
        preparedStatement = connect.prepareStatement(query);
        return(preparedStatement.executeUpdate());
    }
    
    public void close() throws SQLException {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
      if (connect != null) {
        connect.close();
      }
   }
}
