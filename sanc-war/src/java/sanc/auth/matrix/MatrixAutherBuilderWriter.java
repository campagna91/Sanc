/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.matrix;
// Request packages
import sanc.auth.model.Datalink;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author champ
 */
class MatrixAutherBuilderWriter {
    
    // Actual username
    private String username;
    
    // Databese link
    private Datalink link = new Datalink();
    
    /**
     * One parameter constructor
     * 
     * @param username - Actual username
     */
    MatrixAutherBuilderWriter(String username)
    {
        this.username = username;
    }
    
    /**
     * Return actual username
     * 
     * @return String - Actual username
     */
    String getUsername() 
    {
        return username;
    }
    
    /**
     * Say if user exist into users database
     * 
     * @return boolean - True if exist otherwise false
     */
    boolean userExist() 
    {
        return this.link.performExistanceQuery(
                "select * from users where username = '" + username + "'"); 
    }
    
    /**
     * Say if password passed as parameter match with user password
     * 
     * @param passwordHash - Hash password sent
     * @return boolean - True if match, otherwise false
     */
    boolean userPasswordMatch(String passwordHash)
    {
        return this.link.performExistanceQuery(
                "select username from users where username = '" + 
                this.username + "' and password = '" + passwordHash + "'");
    }
    
    /**
     * Decreases user throttling counter to 0
     */
    void resetThrottlingCounter()
    {
        this.link.performUpdateQuery("update users set throttling = 0 "
            + "where username = '" + this.username + "'");
    }
    
    /**
     * Say if user throttling counter is greater of 3
     * 
     * @return boolean - True if counter is > or = 3, otherwise false
     */
    boolean isThreatThrottling() 
    {
        return this.link.performExistanceQuery("select username from users " +
                "where username = '" + this.username + "' and throttling > 3");
    }
    
    /**
     * Increment of one unit user throttling counter
     */
    void increaseThrottlingCounter()
    {
        try {
            ResultSet rs = this.link.performSelectionQuery(
                    "select throttling from users where username = '" + 
                    username + "'");
            if(rs.next()) {   
                int up = rs.getInt("throttling");
                up++;
                this.link.performUpdateQuery(
                    "update users set throttling = " + up + " where username = '" +
                    this.username + "'"
                );
            }
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        }
    }
    
    /**
     * Save a signup request into users database
     * 
     * @param passwordHash - 128 bits hash password sent
     * @param matrix - secret matrix
     */
    void saveSignupRequest(String passwordHash, String matrix) 
    {
        this.link.performUpdateQuery(
                "insert into users (username, password) values " +
                "('" + this.username + "', '" + passwordHash + "')");
        this.link.performUpdateQuery(
                "insert into matrixusercredentials values ('" + this.username + 
                "', '" + matrix + "', 99)");
    }
    
    /**
     * Say if sequence sent match with saved user sequence
     * 
     * @param sequenceSent - Received sequence
     * @param sequenceRequested - Requested and saved sequence
     * @return boolean - True if sequence match, otherwise false
     */
    boolean userSequenceMatch(String sequenceSent, String sequenceRequested)
    {
        boolean matched = true;
        try {
            ResultSet rs = this.link.performSelectionQuery(
                "select matrix from matrixusercredentials where username = '" + 
                this.username + "'");
            if(rs.next()) {
                String matrix = rs.getString("matrix");
                String[] sent = sequenceSent.split("-");
                String[] req = sequenceRequested.split("-");
                
                // Check if character at req[i] position match with charcter sent
                for(int i = 0; i < sent.length - 1; ++i) {
                    int start, end; 
                    start = Integer.parseInt(req[i]);
                    end = start + 1;
                    if(sent[i].equals(matrix.substring(start, end))) {
                        matched &= true;
                    } else matched &= false;
                }
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        System.out.println("Sequence match ? " + matched);
        return matched;
    }
    
    /**
     * Say if user access for first time
     * Note: 99 is a flag. Matrix length is 49 so index value can ben max 48.
     * 
     * @return boolean - True if is first access user time, otherwise false
     */
    boolean isTheFirstAccess()
    {
        return this.link.performExistanceQuery(
            "select username from matrixusercredentials where username = '" +
            this.username + "' and kn = 99");
    }
    
    /**
     * Update last changed value into user credential table
     */
    void updateLastChanged(int value, String newValue)
    {
        try {
            ResultSet rs = this.link.performSelectionQuery(
                    "select matrix from matrixusercredentials where username = '" + 
                    this.username + "'");
            if(rs.next()) {
                String newMatrix;
                if(value != 0)
                     newMatrix = 
                             rs.getString("matrix").substring(0, value-1) + 
                             newValue + 
                             rs.getString("matrix").substring(value+1);
                else 
                    newMatrix = 
                            newValue +
                            rs.getString("matrix").substring(1);
                            
                this.link.performUpdateQuery(
                    "update matrixusercredentials set kn = " + value + 
                    ", matrix = '" + newMatrix + "' where " + "username = '" + 
                    this.username + "'");
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Return last value change from user matrix
     * 
     * @return int - Index of value to retype
     */
    int getLastChanged()
    {
        int last = 0;
        try {
            ResultSet rs = this.link.performSelectionQuery(
                "select kn from matrixusercredentials where username = '" +
                this.username + "'");
            if(rs.next()) {
                last = rs.getInt("kn");
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return last;
    }
    
    
    
    
    
    
}
