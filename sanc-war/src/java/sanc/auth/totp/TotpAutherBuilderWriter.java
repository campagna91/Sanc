/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.totp;
// Required packages
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
// Custom packages
import sanc.auth.AutherCrypto;
import sanc.auth.model.Datalink;
/**
 *
 * @author champ
 */
public class TotpAutherBuilderWriter {
    
    // Actual username
    private String username;
    
    // Databese link
    private Datalink link = new Datalink();
    
    /**
     * One parameter constructor
     * 
     * @param username - Actual username
     */
    TotpAutherBuilderWriter(String username)
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
     * Say if passphrase passed as parameter match with user passphrase
     * 
     * @param passphraseHash - Hash passphrase sent
     * @return boolean - True if passphrase match, otherwise false
     */
    boolean userPassphraseMatch(String passphraseHash)
    {
        return this.link.performExistanceQuery(
                "select username from totpusercredentials where username = '" + 
                this.username + "' and passphrase = '" + passphraseHash + "'");
    }
    
    /**
     * Say if temporary crdentials passed as parameter match with user
     *  tempCredential field (tempoary credentials)
     * 
     * @param passwordHash - Temporary credential sent
     * @return boolean - True if temporary credential match, otherwise false
     */
    boolean userTempCredentialMatch(String passwordHash)
    {
        return this.link.performExistanceQuery(
                "select username from totpusercredentials where username = '" + 
                this.username + "' and tempCredential = '" + passwordHash + "'");
    }
    
    /**
     * Say if user pin passed as parameter match pin calculated with user key
     * 
     * @param plainPin - Plain pin sent
     * @return boolean - True if pin match, otherwise false
     */
    boolean userPinMatch(String plainPin)
    {
        boolean matched = false;
        try {
            ResultSet rs = this.link.performSelectionQuery(
                    "select deviceKey from totpuserdevices where username = '" +
                    username + "'");
            while(rs.next()) {
                if(this.totpPinMatch(rs.getString("deviceKey"), plainPin))
                    return true;
            }
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        }
        return matched;
    }
    
    /**
     * Say if a calculated pin passed as parameter with his key match
     * 
     * @param secret - 256 bits key
     * @param plainPin - Plain pin
     * @return boolean - True if pin match, otherwise false
     */
    boolean totpPinMatch(String secret, String plainPin)
    {
        boolean matched = false;
        long t = new Date().getTime() / TimeUnit.SECONDS.toMillis(30);
        try {
            matched = TotpChecker.checkCode(secret, Long.parseLong(plainPin), t);
        } catch(Exception e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return matched;
    }
    
    /**
     * Say if user has withdraw all key for his devices
     * 
     * @return boolean - True if user has withdraw keys, otherwise false
     */
    boolean hasWithdrewKeys()
    {
        return this.link.performExistanceQuery(
            "select username from totpusercredentials where username = '" + 
            this.username + "' and interimState = 1");
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
     * Save a signup request into users database
     * 
     * @param passwordHash - 128 bits hash password sent
     * @param passphraseHash - 128 bits hash passphrse sent
     * @param secret - 256 bits key
     */
    void saveSignupRequest(String passwordHash, String passphraseHash, String secret)
    {
        this.link.performUpdateQuery(
                "insert into users (username, password) values " +
                "('" + this.username + "', '" + passwordHash + "')");
        this.link.performUpdateQuery(
                "insert into totpusercredentials values ('" + this.username + 
                "', '" + passphraseHash + "', '" + AutherCrypto.hash256("") +
                "', 0)");
        
        // Derive time of update for exipration of temporary credentials 
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatted.format(cal.getTime());
        
        // Save user device
        this.link.performUpdateQuery(
                "insert into totpuserdevices values ('" + this.username + "', '" +
                secret + "', 'default', '" + date + "')");        
    }
    
    /**
     * Withdraws all the keys of user's devices
     */
    void withdrawnAllKey()
    {
        this.link.performUpdateQuery(
                "update totpuserdevices set deviceKey = '' where username = '" +
                this.username + "'");
    }
    
    /**
     * Set user interim state to @flag parameter passed
     * 
     * @param flag - Active (1) or not (0) interim state
     */
    void setInterimStateTo(int flag)
    {
        this.link.performUpdateQuery("update totpusercredentials set interimState = " + 
                flag + " where username = '" + this.username + "'");
    }
   
    /**
     * Return user's devices list
     * 
     * @return String - List of user's devices
     */
    String getUserDeviceList()
    {
        String aux = "";
        try {
            ResultSet rs = this.link.performSelectionQuery(
                    "select alias, date from totpuserdevices where username = '" +
                    this.username + "'");
            while(rs.next()) {
                aux += rs.getString("alias") + "," + rs.getString("date") + "|";
            }
        } catch(SQLException s) {
            System.out.println("ECCEZIONE: " + s.getMessage());
        }
        return aux;
    }
    
    /**
     * Insert new device into totpuserdevices table
     * 
     * @param alias - Name of new device
     * @param secret - 256 bits key
     */
    void combineNewDevice(String alias, String secret)
    {        
        // Derive time of update for exipration of temporary credentials 
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatted.format(cal.getTime());
        this.link.performUpdateQuery(
            "insert into totpuserdevices values ('" + this.username + "'" +
            ", '" + secret + "', '" + alias + "', '" + date + "')");
    }
    
    /**
     * Say if a device named as @alias parameter already exist into totpuserdevices
     *  table
     * 
     * @param alias - Name to search
     * @return boolean - True if device called as @alias exist, otherwise false
     */
    boolean userDeviceAliasExist(String alias)
    {
        return this.link.performExistanceQuery(
            "select alias from totpuserdevices where username = '" + this.username + 
            "' and alias = '" + alias + "'");
    }
    
    /**
     * Uncombine a combined user's device named @alias
     * 
     * @param alias - Device of device to uncombine
     */
    void uncombineUserDevice(String alias)
    {
        this.link.performUpdateQuery(
                "delete from totpuserdevices where username = '" + this.username + 
                "' and alias = '" + alias + "'");
    }
    
    /**
     * Say if user has more than one device combined
     * 
     * @return boolean - True if user has 2 or more devices combined, otherwise false
     */
    boolean userHasMoreThanOneDevice()
    {
        return this.link.performExistanceQuery(
            "select count(alias) as num from totpuserdevices where username = '" +
            this.username + "' group by username having num > 1");
    }
    
    /**
     * Update user's device key for combined devices named @alias
     * 
     * @param alias - Name of device
     * @param secret - 256 bits key
     */
    void updateDeviceKey(String alias, String secret)
    {
        this.link.performUpdateQuery(
            "update totpuserdevices set deviceKey = '" + secret + "' where " +
            "username = '" + this.username + "' and alias = '" + alias + "'");
    }
    
    /**
     * Updaste user temporary credentials
     * 
     * @param passwordHash - Hash password sent
     */
    void updateTemporaryCredentials(String passwordHash)
    {
        this.link.performUpdateQuery(
                "update totpusercredentials set tempCredential = '" + 
                passwordHash + "' where username = '" + this.username + "'");
    }   
}
