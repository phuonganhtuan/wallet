package model;

import java.sql.*;

/**
 *
 * @author tungnguyen
 */
public class MySQL {
    // JDBC driver name
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connnect state       
    static Connection connection = null;
    
    /**
     * Connect to MySQL
     * @param url the database url
     * @param database the database name
     * @param user username
     * @param password password
     * @return true if connect successful, false if not
     */
    public static boolean connect(String url, String database, String user, String password) {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER).newInstance();
            
            String dbUrl = "jdbc:mysql://" + url + "/" + database + "?useUnicode=yes&characterEncoding=UTF-8";
            
            try {
                connection = DriverManager.getConnection(dbUrl, user, password); 
                return true;
            } catch (Exception e) {
                connection = null;
                System.err.println(e);
                return false;
            }
        } catch (Exception e) {
            connection = null;
            System.err.println(e);
            return false;
        }
        
    }
    
    /**
     * Disconnect from database
     * @return 
     */
    public static boolean disconnect() {
        if (connection != null) {
            try {
                connection.close();  
                connection = null;
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Verify if database is connected
     * @return true if connected, false if not
     */
    public static boolean isConnected() {
        if (connection == null) {
            return false;
        } else {
            return true;
        }
    }
    
    /** 
     * execute a query statement and return result of the query
     * @param sqlQueryStatement the statement
     * @return a result is type of ResultSet
     */
    public static ResultSet executeQuery(String sqlQueryStatement) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQueryStatement);
        } catch (Exception e) {
            resultSet = null;
            System.err.println(e);
        }
        
        return resultSet;
    } 
    
    
    /**
     * execute an update statement (update, delete, insert), return an integer
     * @param sqlUpdateStatement
     * @return -1 if fail, greater than -1 if successful
     */
    public static int executeUpdate(String sqlUpdateStatement) {
        Statement statement = null;
        int result;
        try {
            statement = connection.createStatement();
            result = statement.executeUpdate(sqlUpdateStatement);
        } catch (Exception e) {
            result = -1;
            System.err.println(e);
        }
        return result;
    } 
    
}

