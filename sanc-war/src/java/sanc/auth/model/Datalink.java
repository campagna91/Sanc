/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.model;
// Required packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author champ
 */
public class Datalink {
    
    // JDBC driver name and database URL
    protected final static String DRIVER = "com.mysql.jdbc.Driver";
    protected String DB = "jdbc:mysql://localhost/sanc";
    
    // Database credentials
    protected static final String USER = "root";
    protected static final String PWD = "";

    public Datalink() {}
    
    /**
     * Establish a connection with database
     * 
     * @return Connection - Link to database
     */
    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
        } catch(ClassNotFoundException c) {
            System.out.println("ECCEZIONE: " + c.getMessage());
        }
        try {
            conn = DriverManager.getConnection(DB, USER, PWD);
            return conn;
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        }
        return conn;
    }
    
    /**
     * Perform a query to check if requested data result exist
     * 
     * @param query - Query to perform
     * @return - true if exist one record at least
     */
    public final boolean performExistanceQuery(String query)
    {
        System.out.println(query);
        Connection conn = null;
        Statement statement = null;
        int rows = 0;
        try {
            
            // establish connection
            conn = this.getConnection();
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            // Row number recover
            rs.last();
            rows = rs.getRow();
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch(SQLException s) {
                System.out.println("ECCEZIONE: " + s.getMessage());
            }
            try {
                if (conn != null)
                    conn.close();
            } catch(SQLException s) {
                System.out.println("ECCEZIONE: " + s.getMessage());
            }
        }
        return rows > 0;
    }
    
    /**
     * Perform a delete / insertion or update query passed by parameter
     * 
     * @param query - Query to execute
     */
    public final void performUpdateQuery(String query)
    {
        System.out.println(query);
        Connection conn = null;
        Statement statement = null;
        try {
            // establish connection
            conn = getConnection();
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch(SQLException s) {
                System.out.println("ECCEZIONE: " + s.getMessage());
            }
            try {
                if (conn != null)
                    conn.close();
            } catch(SQLException s) {
                System.out.println("ECCEZIONE: " + s.getMessage());
            }
        }
    }
    
    /**
     * Perform a selection query and return set of results
     * 
     * @param query - Query to execute
     * @return ResultSet - Set of results
     */
    public final ResultSet performSelectionQuery(String query)
    {
        System.out.println(query);
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            
            // establish connection
            conn = getConnection();
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        } 
        
        // Possible memory leak
        return rs;
    }
}
