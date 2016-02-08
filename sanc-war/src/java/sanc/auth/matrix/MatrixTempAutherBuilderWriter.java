/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.matrix;
// Custom packges
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import sanc.auth.model.Datalink;

/**
 *
 * @author champ
 */
class MatrixTempAutherBuilderWriter {
    
    // Temporary ID 
    String id;
    
    // Database link
    Datalink link;
    
    /**
     * One parameter constructor
     * 
     * @param id - Temporary ID request
     */
    MatrixTempAutherBuilderWriter(String id) {
        this.id = id;
        this.link = new Datalink();
    }
    
    /**
     * Get temporary ID request
     * 
     * @return String - ID - Temporary ID request
     */
    String getID()
    {
        return this.id;
    }
    
    /**
     * Set temporary ID request
     * 
     * @param id - ID to set
     */
    void setID(String id)
    {
        this.id = id;
    }
    
    /**
     * Insert new temporary signup request
     * 
     * @param ka - 128 bits key
     * @param iva - 128 bits random vector
     */
    void insertTempSignupRequest(String ka, String iva)
    {
        this.link.performUpdateQuery("delete from matrixtempsignuprequest");
        this.link.performUpdateQuery(
                "insert into matrixtempsignuprequest values ('" + this.id + "', '" + ka
                + "', '" + iva + "')");
    }
    
    /**
     * Insert new temporary signin request
     * 
     * @param ka - 128 bits key
     * @param iva - 128 bits random vector
     */
    void insertTempSigninRequest(String ka, String iva)
    {
        this.link.performUpdateQuery("delete from matrixtempsigninrequest");
        this.link.performUpdateQuery(
                "insert into matrixtempsigninrequest values ('" + id + "', '" + 
                ka + "', '" + iva + "')");
    }
    
    /**
     * Insert new temporary signin request
     * 
     * @param ka - 128 bits key
     * @param ks - Matrix sequence
     * @param kn - Row and column to update
     * @param iva - 128 bits random vector
     */
    void insertTempSigninConfirm(String ka, String kn, String ks, String iva)
    {
        this.link.performUpdateQuery("delete from matrixtempsigninconfirm");
        this.link.performUpdateQuery(
                "insert into matrixtempsigninconfirm values ('" + id + "', '" + 
                ka + "', '" + iva + "', '" + ks + "', '" + kn + "')");
    }
    
    /**
     * Says if exist temporary signup request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSignupRequest()
    {
        return this.link.performExistanceQuery("select id from matrixtempsignuprequest where id = '" + 
                this.id + "'");
    }
    
    /**
     * Says if exist temporary 
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSigninRequest()
    {
        return this.link.performExistanceQuery("select id from matrixtempsigninrequest where id = '" + 
                this.id + "'");
    }
    
    /**
     * Say if exist temporary signin confirm request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSigninConfirm()
    {
        return this.link.performExistanceQuery(
                "select id from matrixtempsigninconfirm where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary signup request
     */
    void removeTempSignupRequest()
    {
        this.link.performUpdateQuery(
                "delete from matrixtempsignuprequest where id = '" + this.id + "'");
    }
    
    /**
    * Remove a temporary signin request
    */
    void removeTempSigninRequest()
    {
        this.link.performUpdateQuery(
                "delete from matrixtempsigninrequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary signin confirm
     */
    void removeTempSigninConfirm()
    {
        this.link.performUpdateQuery(
            "delete from matrixtempsigninconfirm where id = '" + this.id + "'");
    }
    
    /**
     * Return a map containing temporary signin request credentials
     * 
     * @return Map<String, String> - Temporary signin request credentials
     */
    Map<String, String> getTempSigninRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva from matrixtempsigninrequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("iva", rs.getString("iva"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary signup request credentials
     * 
     * @return Map<String, String> - Temporary signup credentials
     */
    Map<String, String> getTempSignupRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva " +
                    "from matrixtempsignuprequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("iva", rs.getString("iva"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary signin confirm credentials
     * 
     * @return Map<String, String> - Temporary signin credentials
     */
    Map<String, String> getTempSigninConfirmCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva, ks, kn " +
                    "from matrixtempsigninconfirm where id = '" + this.id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ks", rs.getString("ks"));
                aux.put("kn", rs.getString("kn"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
}
