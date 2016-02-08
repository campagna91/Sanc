/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.totp;
// Required packages
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
// Custom packages
import sanc.auth.model.Datalink;
/**
 *
 * @author champ
 */
class TotpTempAutherBuilderWriter {
    
    // Temporary ID 
    String id;
    
    // Database link
    Datalink link;
    
    /**
     * One parameter constructor
     * 
     * @param id - Temporary ID request
     */
    TotpTempAutherBuilderWriter(String id) {
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
     * Insert new temporary signin request
     * 
     * @param ka - 128 bits key
     * @param kb - 128 bits key
     * @param iva - 128 bits random vector
     * @param ivb - 128 bits random vector
     */
    void insertTempSigninRequest(String ka, String kb, String iva, String ivb)
    {
        this.link.performUpdateQuery(
                "insert into totptempsigninrequest values ('" + id + "', '" + ka + "', '" + 
                kb + "', '" + iva + "', '" + ivb + "')");
    }
    
    /**
     * Insert new temporary signin interim request
     * 
     * @param ka - 128 bits key
     * @param iva - 128 bits random vector
     */
    void insertTempSigninInterim(String ka, String iva)
    {
        this.link.performUpdateQuery("delete from totptempsignininterim");
        this.link.performUpdateQuery(
                "insert into totptempsignininterim values ('" + id + "', '" + 
                ka + "', '" + iva + "')");
    }
    
    /**
     * Insert new temporary signup request
     * 
     * @param ka - 128 bits key
     * @param kb - 128 bits key
     * @param kc - 128 bits key
     * @param ks - 256 bits key
     * @param iva - 128 bits random vector
     * @param ivb - 128 bits random vector
     * @param ivc - 128 bits random vector
     */
    void insertTempSignupRequest(String ka, String kb, String kc, String ks,
            String iva, String ivb, String ivc)
    {
        System.out.println("**** Start insertion");
        this.link.performUpdateQuery("delete from totptempsignuprequest");
        this.link.performUpdateQuery(
                "insert into totptempsignuprequest values ('" + this.id + "', '" + ka
                + "', '" + kb + "', '" + kc + "', '" + ks + "', '" + iva + "'"
                + ", '" + ivb + "', '" + ivc + "')");
        System.out.println("**** End insertion");
    }
    
    /**
     * Insert new temporary retire request
     * 
     * @param ka - 128 bits key
     * @param kb - 128 bits key
     * @param iva - 128 bits random vector
     * @param ivb - 128 bits random vector
     */
    void insertTempRetireRequest(String ka, String iva, String kb, String ivb)
    {
        this.link.performUpdateQuery("delete from totptempretirerequest");
        this.link.performUpdateQuery(
                "insert into totptempretirerequest values ('" + id + "', '" + ka + 
                "', '" + kb + "', '" + iva + "', '" + ivb + "')");
    }
    
    /**
     * Insert new temporary retire confirm request
     * 
     * @param ka - 128 bits key
     * @param iva - 128 bits random vector
     */
    void insertTempRetireConfirm(String ka, String iva)
    {
        this.link.performUpdateQuery("delete from totptempretireconfirm");
        this.link.performUpdateQuery(
                "insert into totptempretireconfirm values ('" + this.id + "', '"
                + ka + "', '" + iva + "')");
    }
    
    /**
     * Insert new temporary combine request
     * 
     * @param ka - 128 bits key
     * @param kb - 128 bits key
     * @param iva - 128 bits random vector
     * @param ivb - 128 bits random vector
     * @param ks - 256 bits key
     */
    void insertTempCombineRequest(String ka, String kb, String iva, String ivb, 
            String ks)
    {
        this.link.performUpdateQuery("delete from totptempcombinerequest");
        this.link.performUpdateQuery(
            "insert into totptempcombinerequest values ('" + this.id + "', '" +
            ka + "', '" + kb + "', '" + iva + "', '" + ivb + "', '" + ks + "')");
    }
    
    /**
     * Insert new temporary uncombine request
     * 
     * @param ka - 128 bits key
     * @param iva - 128 bits random vector
     */
    void insertTempUncombineRequest(String ka, String iva)
    {
        this.link.performUpdateQuery("delete from totptempuncombinerequest");
        this.link.performUpdateQuery(
            "insert into totptempuncombinerequest values ('" + this.id + "', '" + 
            ka + "', '" + iva + "')");
    }
    
    /**
     * Insert new temporary renew request
     * 
     * @param ka - 128 bits key
     * @param kb - 128 bits key
     * @param ks - 256 bits key
     * @param iva - 128 bits random vector
     * @param ivb - 128 bits random vector
     */
    void insertTempRenewRequest(String ka, String kb, String ks, String iva, String ivb)
    {
        this.link.performUpdateQuery("delete from totptemprenewrequest");
        this.link.performUpdateQuery(
            "insert into totptemprenewrequest values ('" + this.id + "', '" +
            ka + "', '" + kb + "', '" + ks + "', '" + iva + "', '" + ivb + "')");
    }
    
    /**
     * Says if exist temporary 
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSigninRequest()
    {
        return this.link.performExistanceQuery("select * from totptempsigninrequest where id = '" + id + "'");
    }
    
    /**
     * Says if exist temporary signin interim request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSigninInterim()
    {
        return this.link.performExistanceQuery("select * from totptempsignininterim where id = '" + id + "'");
    }
    
    /**
     * Says if exist temporary signup request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempSignupRequest()
    {
        return this.link.performExistanceQuery("select * from totptempsignuprequest where id = '" + id + "'");
    }
    
    /**
     * Says if exist temporary retire confirm request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempRetireConfirm()
    {
        return this.link.performExistanceQuery(
            "select id from totptempretireconfirm where id = '" + this.id + "'");
    }
    
    /**
     * Says if exist temporary retire request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempRetireRequest()
    {
        return this.link.performExistanceQuery(
                "select id from totptempretirerequest where id = '" + this.id + "'");
    }
    
    /**
     * Says if exist temporary combine request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempCombineRequest()
    {
        return this.link.performExistanceQuery(
            "select id from totptempcombinerequest where id = '" + this.id + "'");
    }
    
    /**
     * Says if exist temporary uncombine request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempUncombineRequest()
    {
        return this.link.performExistanceQuery(
                "select id from totptempuncombinerequest where id = '" + this.id + "'");
    }
    
    /**
     * Says if exist temporary renew request
     * 
     * @return boolean - True if exist, otherwise false
     */
    boolean existTempRenewRequest()
    {
        return this.link.performExistanceQuery(
            "select id from totptemprenewrequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary signin interim request
     */
    void removeTempSigninInterim()
    {
        this.link.performUpdateQuery(
                "delete from totptempsignininterim where id = '" + id + "'");
    }
    
    /**
     * Remove a temporary signup request
     */
    void removeTempSignupRequest()
    {
        this.link.performUpdateQuery(
                "delete from totptempsignuprequest where id = '" + id + "'");
    }
    
    /**
     * Remove a temporary retire request
     */
    void removeRetireRequest()
    {
        this.link.performUpdateQuery(
                "delete from totptempretirerequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary retire confirm request
     */
    void removeRetireConfirm()
    {
        this.link.performUpdateQuery(
                "delete from totptempretireconfirm where id = '" + this.id + "'");
    }
    
    /**
    * Remove a temporary signin request
    */
    void removeTempSigninRequest()
    {
        this.link.performUpdateQuery(
                "delete from totptempsigninrequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary retire request
     */
    void removeTempRetireRequest()
    {
        this.link.performUpdateQuery(
            "delete from totptempretirerequest where id = '" + id + "'");
    }
    
    /**
     * Remove a temporary retire confirm request
     */
    void removeTempRetireConfirm()
    {
        this.link.performUpdateQuery(
            "delete from totptempretireconfirm where id = '" + id + "'");
    }
    
    /**
     * Remove a temporary combine request
     */
    void removeTempCombineRequest()
    {
        this.link.performUpdateQuery(
            "delete from totptempcombinerequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary uncombine request
     */
    void removeTempUncombineRequest()
    {    
        this.link.performUpdateQuery(
            "delete from totptempuncombinerequest where id = '" + this.id + "'");
    }
    
    /**
     * Remove a temporary renew request
     */
    void removeTempRenewRequest()
    {
        this.link.performUpdateQuery(
            "delete from totptemprenewrequest where id = '" + this.id + "'");
    }
    
    /**
     * Return a map containing temporary combine request credentials
     * 
     * @return Map<String, String> - Temporary combine credentials
     */
    Map<String, String> getTempCombineRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, kb, iva, ivb, ks from totptempcombinerequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("kb", rs.getString("kb"));
                aux.put("ks", rs.getString("ks"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ivb", rs.getString("ivb"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary renew request credentials
     * 
     * @return Map<String, String> - Temporary renew credentials
     */
    Map<String, String> getTempRenewRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, kb, iva, ivb, ks from totptemprenewrequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("kb", rs.getString("kb"));
                aux.put("ks", rs.getString("ks"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ivb", rs.getString("ivb"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary retire confirm credentials
     * 
     * @return Map<String, String> - Temporary retire confirm credentials
     */
    Map<String, String> getTempRetireConfirmCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva from totptempretireconfirm where id = '" + id + "'");
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
     * Return a map containing temporary retire request credentials
     * 
     * @return Map<String, String> - Temporary retire request credentials
     */
    Map<String, String> getTempRetireRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, kb, iva, ivb from totptempretirerequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("kb", rs.getString("kb"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ivb", rs.getString("ivb"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary signin interim credentials
     * 
     * @return Map<String, String> - Temporary signin interim credentials
     */
    Map<String, String> getTempSigninInterimCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva from totptempsignininterim where id = '" + id + "'");
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
     * Return a map containing temporary signin request credentials
     * 
     * @return Map<String, String> - Temporary signin request credentials
     */
    Map<String, String> getTempSigninRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, kb, iva, ivb from totptempsigninrequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("kb", rs.getString("kb"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ivb", rs.getString("ivb"));
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
                    "select ka, kb, kc, iva, ivb, ivc, ks " +
                    "from totptempsignuprequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("kb", rs.getString("kb"));
                aux.put("kc", rs.getString("kc"));
                aux.put("ks", rs.getString("ks"));
                aux.put("iva", rs.getString("iva"));
                aux.put("ivb", rs.getString("ivb"));
                aux.put("ivc", rs.getString("ivc"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
    
    /**
     * Return a map containing temporary uncombine request credentials
     * 
     * @return Map<String, String> - Temporary uncombine credentials
     */
    Map<String, String> getTempUncombineRequestCredentials()
    {
        Map<String, String> aux = new HashMap<String, String>();
        try {
            ResultSet rs = link.performSelectionQuery(
                    "select ka, iva from totptempuncombinerequest where id = '" + id + "'");
            if (rs.next()) {
                aux.put("ka", rs.getString("ka"));
                aux.put("iva", rs.getString("iva"));
            }
        } catch(SQLException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return aux;
    }
}