/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.totp;
// Required packages
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
// Custom package
import sanc.auth.AutherBuilder;
import sanc.auth.AutherCrypto;
// Exceptions
import java.io.IOException;
/**
 *
 * @author champ
 */
public class TotpAutherBuilder extends AutherBuilder {
    
    /**
     * Process request and call competent method with reflection
     * 
     * @param request - Servlet request
     * @param response - Servlet response
     */
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
        super.request = request;
        super.response = response;
        
        Method method; 
        try {
            method = this.getClass().getMethod(request.getParameter("requestType"));
            method.invoke(this);
        } catch (Exception e) {
            System.out.println("ECCEZIONE: " + e.getMessage() + " " + e.getCause());
            try {
                response.getWriter().write("<r><e>invalid request type</e></r>");
            } catch(IOException i) {
                System.out.println("ECCEZIONE: " + i.getMessage());
            }
        }
    }
    
    /**
     * Try to authenticate user into system
     */
    public void signin() {

        String aux, id, ka, kb, iva, ivb;
        aux = "";
        TotpTempAutherBuilderWriter temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
        TotpAutherBuilderWriter writer;
        
        switch(request.getParameter("state")) {
            
            // Generate temporary credentials 
            case("one"):
                id = temp.getID();
                ka = AutherCrypto.generateAESKey();
                kb = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                ivb = AutherCrypto.generateAESIV();
                temp.insertTempSigninRequest(ka, kb, iva, ivb);
                aux =   "<r>"
                            + "<id>" + id + "</id>"
                            + "<ka>" + ka + "</ka>"
                            + "<kb>" + kb + "</kb>"
                            + "<iva>" + iva + "</iva>"
                            + "<ivb>" + ivb + "</ivb>"
                        + "</r>";
                break;
                
            // System try to authenticate user into system
            case("two"):
                
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));
                writer = new TotpAutherBuilderWriter(request.getParameter("username"));

                // Temporary request must exist
                if (temp.existTempSigninRequest()) {

                    // User must be registered 
                    if (writer.userExist()) {

                        // Check possible bruteforce attacks
                        // TODO: true stay for captcha check 
                        boolean isThreatThrottling = writer.isThreatThrottling();

                        if ((isThreatThrottling && true) || !isThreatThrottling) {
                            Map<String, String> tempsignin = temp.getTempSigninRequestCredentials();
                            
                            // User password must match
                            if(writer.userPasswordMatch(
                                    AutherCrypto.hash256(
                                        AutherCrypto.decrypt(
                                                request.getParameter("password"),
                                                tempsignin.get("ka"),
                                                tempsignin.get("iva")
                                        )
                                    )
                            )) {
                                
                                // User is in interim state
                                if(writer.hasWithdrewKeys()) {
                                    
                                    // User temp credential must match
                                    if(writer.userTempCredentialMatch(
                                            AutherCrypto.hash256(
                                                AutherCrypto.decrypt(
                                                    request.getParameter("pin"),
                                                    tempsignin.get("kb"),
                                                    tempsignin.get("ivb")
                                                )
                                            )
                                    )) {
                                        
                                        /** 
                                         * User is correctly logged and have 
                                         * to change temporary credentials
                                         */
                                        writer.resetThrottlingCounter();
                                        temp.removeTempSigninRequest();
                                        temp.setID(AutherCrypto.generateID());
                                        id = temp.getID();
                                        ka = AutherCrypto.generateAESKey();
                                        iva = AutherCrypto.generateAESIV();
                                        temp.insertTempSigninInterim(ka, iva);
                                        aux =   "<r>" +
                                                    "<id>" + id +  "</id>" +
                                                    "<ka>" + ka + "</ka>" +
                                                    "<iva>" + iva + "</iva>" +
                                                "</r>";
                                        
                                    } else {
                                        writer.increaseThrottlingCounter();
                                        aux = "<r><e>wrong credentials</e>";
                                        if(writer.isThreatThrottling())
                                            aux += "<c></c>";
                                        aux += "</r>";
                                    }
                                    
                                } else {
                                    
                                    // User pin must match
                                    if(writer.userPinMatch(AutherCrypto.decrypt(
                                            request.getParameter("pin"),
                                            tempsignin.get("kb"),
                                            tempsignin.get("ivb")
                                    ))) {
                                        
                                        // User is correctly logged
                                        temp.removeTempSigninRequest();
                                        writer.resetThrottlingCounter();
                                        aux =   "<r>" +
                                                    "<sessionSid>" +
                                                        AutherCrypto.generateID() +
                                                    "</sessionSID>" +
                                                    "<user>" +
                                                        writer.getUsername() +
                                                    "</user>" +
                                                    "<devices>" +
                                                        writer.getUserDeviceList() +
                                                    "</devices>" +
                                                "</r>";
                                        
                                    } else {
                                        writer.increaseThrottlingCounter();
                                        aux = "<r><e>wrong credentials</e>";
                                        if(writer.isThreatThrottling())
                                            aux += "<c></c>";
                                        aux += "</r>";
                                    }
                                }
                                
                            } else {
                                writer.increaseThrottlingCounter();
                                aux = "<r><e>wrong credentials</e>";
                                if(writer.isThreatThrottling())
                                    aux += "<c></c>";
                                aux += "</r>";
                            }
                        } else aux = "<r><e>invalid captcha code</e></r>";
                    } else aux = "<r><e>user not found</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
                
            // User is in interim state and must ben insert new temporary credentials
            case("three"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));

                // Request must be known
                if(temp.existTempSigninInterim()) {

                    writer = new TotpAutherBuilderWriter(request.getParameter("username"));

                    Map<String, String> tempsignininterim = temp.getTempSigninInterimCredentials();
                    String tempCredential = AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                    request.getParameter("temporaryCredentials"),
                                    tempsignininterim.get("ka"),
                                    tempsignininterim.get("iva")));
                     /**
                     * New temporary credentials must be different from actual pasword
                     * and from previous temporary credentials
                     */ 
                    if(!writer.userPasswordMatch(tempCredential) && 
                            !writer.userTempCredentialMatch(tempCredential)) {
                        
                        // Saving new temporary credentials
                        writer.updateTemporaryCredentials(tempCredential);
                        
                        // Remove temporary request
                        temp.removeTempSigninInterim();
                        writer.resetThrottlingCounter();
                        aux =   "<r>" +
                                    "<sessionSid>" +
                                        AutherCrypto.generateID() +
                                    "</sessionSID>" +
                                    "<user>" +
                                        writer.getUsername() +
                                    "</user>" +
                                    "<devices>" +
                                        writer.getUserDeviceList() +
                                    "</devices>" +
                                    "<message>temporary credentials</message>" +
                                "</r>";
                    } else {
                        
                        /**
                         * New temporary credentials is not different and 
                         * system generate other credendials
                         */
                        temp.removeTempSigninInterim();
                        temp.setID(AutherCrypto.generateID());
                        id = temp.getID();
                        ka = AutherCrypto.generateAESKey();
                        iva = AutherCrypto.generateAESIV();
                        temp.insertTempSigninInterim(ka, iva);
                        aux = "<r>"
                                + "<id>" + id + "</id>"
                                + "<ka>" + ka + "</ka>"
                                + "<iva>" + iva + "</iva>"
                                + "<e>new temporary credentials must be different from other</e>"
                            + "</r>";
                    }
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Block all user's combined device and substitute login with temporary credential
     */
    public void retire() {
        TotpTempAutherBuilderWriter temp;
        TotpAutherBuilderWriter writer;
        String id, ka, kb, iva, ivb;
        String aux = "";
        
        switch(request.getParameter("state")) {
            
            // Generate temporary credential
            case("one"):
                temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
                id = temp.getID();
                ka = AutherCrypto.generateAESKey();
                kb = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                ivb = AutherCrypto.generateAESIV();
                temp.insertTempRetireRequest(ka, kb, iva, ivb);
                aux =   "<r>"
                            + "<id>" + id + "</id>"
                            + "<ka>" + ka + "</ka>"
                            + "<kb>" + kb + "</kb>"
                            + "<iva>" + iva + "</iva>"
                            + "<ivb>" + ivb + "</ivb>"
                        + "</r>";
                break;
                
            // System try to authenticate user with credentials passed
            case("two"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));

                // Request must be known
                if (temp.existTempRetireRequest()) {

                    // User must be registered
                    writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    if (writer.userExist()) {

                        Map<String, String> tempretirerequest = temp.getTempRetireRequestCredentials();
                        
                        // Credentials must match
                        if (writer.userPasswordMatch(AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                request.getParameter("password"),
                                tempretirerequest.get("ka"),
                                tempretirerequest.get("iva")
                            )
                        )) && writer.userPassphraseMatch(AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                request.getParameter("passphrase"),
                                tempretirerequest.get("kb"),
                                tempretirerequest.get("ivb")
                            )
                        ))) {
                                
                            // User is recognized and all keys are withdrawn
                            temp.removeRetireRequest();
                            writer.withdrawnAllKey();
                            writer.setInterimStateTo(1);

                            // Generate temp retire confirm credentials
                            id = AutherCrypto.generateID();
                            ka = AutherCrypto.generateAESKey();
                            iva = AutherCrypto.generateAESIV();
                            temp.setID(id);
                            temp.insertTempRetireConfirm(ka, iva);
                            aux =   "<r>"
                                        + "<id>" + id + "</id>"
                                        + "<ka>" + ka + "</ka>"
                                        + "<iva>" + iva + "</iva>"
                                    + "</r>";
                            
                        } else {
                            temp.removeTempRetireRequest();
                            aux = "<r><e>wrong credentials</e></r>";
                        }
                    } else {
                        temp.removeTempRetireRequest();
                        aux = "<r><e>user not found</e></r>";
                    }
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
            
            // User must insert temporary credential to authenticate into system
            case("three"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));

                // Request must be known
                if(temp.existTempRetireConfirm()) {
                    
                    Map<String, String> tempretireconfirm = temp.getTempRetireConfirmCredentials();
                    writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    String tempCredential = AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                    request.getParameter("temporaryCredentials"),
                                    tempretireconfirm.get("ka"),
                                    tempretireconfirm.get("iva")));
                    
                    /**
                     * Temporary credentials must be different from main password
                     * and actual temporary credential
                     */
                    if(!writer.userPasswordMatch(tempCredential) && 
                            !writer.userTempCredentialMatch(tempCredential)) {
                        
                        writer.updateTemporaryCredentials(tempCredential);
                        temp.removeRetireConfirm();
                        
                    } else {
                        temp.removeTempRetireConfirm();
                        temp.setID(AutherCrypto.generateID());
                        
                        // Generate temp retire confirm credentials
                        id = temp.getID();
                        ka = AutherCrypto.generateAESKey();
                        iva = AutherCrypto.generateAESIV();
                        temp.insertTempRetireConfirm(ka, iva);
                        aux =   "<r>" +
                                    "<id>" + id + "</id>" +
                                    "<ka>" + ka + "</ka>" +
                                    "<iva>" + iva + "</iva>" +
                                    "<e>temporary credential must be different from main password and actual temporary credential</e>" +
                                "</r>";
                    }
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Allow user to register into system
     */
    public void signup() {
        String state = request.getParameter("state");
        String id, ka, kb, kc, iva, ivb, ivc, ks;
        String aux = "";
        TotpAutherBuilderWriter writer;
        TotpTempAutherBuilderWriter temp;
        switch(state) {
            
            // Generate temporary credential
            case("one"):
                    writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    
                    // username must be unique
                    if (!writer.userExist()) {
                        temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
                        id = temp.getID();
                        ka = AutherCrypto.generateAESKey();
                        kb = AutherCrypto.generateAESKey();
                        kc = AutherCrypto.generateAESKey();
                        iva = AutherCrypto.generateAESIV();
                        ivb = AutherCrypto.generateAESIV();
                        ivc = AutherCrypto.generateAESIV();
                        ks = AutherCrypto.generateKey();
                        temp.insertTempSignupRequest(ka, kb, kc, ks, iva, ivb, ivc);
                        aux =   "<r>"
                                    + "<u>" + writer.getUsername() + "</u>"
                                    + "<id>" + id + "</id>"
                                    + "<ka>" + ka + "</ka>"
                                    + "<kb>" + kb + "</kb>"
                                    + "<kc>" + kc + "</kc>"
                                    + "<ks>" + ks + "</ks>"
                                    + "<iva>" + iva + "</iva>"
                                    + "<ivb>" + ivb + "</ivb>"
                                    + "<ivc>" + ivc + "</ivc>"
                                + "</r>";
                    } else aux = "<r><e>user is not available</e></r>";
                break;
            
            case("two"):
                System.out.println("**** Start setting params");
                writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                System.out.println("**** End setting params username");
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));
                System.out.println("**** End setting id");
                // Signup request must be authorized
                if (temp.existTempSignupRequest()) {
                    System.out.println("**** Check existance");
                    Map<String, String> tempsignuprequest = temp.getTempSignupRequestCredentials();
                    // Totp received must match pin calculated
                    if (writer.totpPinMatch(
                            tempsignuprequest.get("ks"),
                            AutherCrypto.decrypt(
                                request.getParameter("pin"),
                                tempsignuprequest.get("kb"),
                                tempsignuprequest.get("ivb")
                    ))) {

                        // Save new user
                        writer.saveSignupRequest(
                                AutherCrypto.hash256(
                                        AutherCrypto.decrypt(
                                                request.getParameter("password"),
                                                tempsignuprequest.get("ka"),
                                                tempsignuprequest.get("iva")
                                        )
                                ),
                                AutherCrypto.hash256(
                                        AutherCrypto.decrypt(
                                                request.getParameter("passphrase"),
                                                tempsignuprequest.get("kb"),
                                                tempsignuprequest.get("ivb")
                                        )
                                ),
                                tempsignuprequest.get("ks")
                        );
                        temp.removeTempSignupRequest();
                        
                    } else aux = "<r><e>wrong pin</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Allow authenticated user to combine new device
     */
    public void combine()
    {
        TotpTempAutherBuilderWriter temp;
        TotpAutherBuilderWriter writer;
        String id, ka, kb, iva, ivb, ks;
        String aux = "";
        
        switch(request.getParameter("state")) {
            
            // Generate temporary credential
            case("one"):
                temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
                id = temp.getID();
                ks = AutherCrypto.generateKey();
                ka = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                kb = AutherCrypto.generateAESKey();
                ivb = AutherCrypto.generateAESIV();
                temp.insertTempCombineRequest(ka, kb, iva, ivb, ks);
                aux =   "<r>" +
                            "<id>" + id + "</id>" +
                            "<ka>" + ka + "</ka>" +
                            "<iva>" + iva + "</iva>" +
                            "<kb>" + kb + "</kb>" +
                            "<ivb>" + ivb + "</ivb>" +
                            "<ks>" + ks + "</ks>" +
                        "</r>";
                break;
                
            // System try to authenticate user and then insert new device
            case("two"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));
                
                // Request must ben known
                if(temp.existTempCombineRequest()) {
                    
                    writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    Map<String, String> tempcombinerequest = temp.getTempCombineRequestCredentials();
                    
                    if(writer.userPinMatch(AutherCrypto.decrypt(
                        request.getParameter("twoFactor"),
                        tempcombinerequest.get("ka"),
                        tempcombinerequest.get("iva")
                    ))) {
                        
                        // Pin set must match with calculated
                        if(writer.totpPinMatch(
                                tempcombinerequest.get("ks"),
                                AutherCrypto.decrypt(
                                    request.getParameter("newTwoFactor"),
                                    tempcombinerequest.get("kb"),
                                    tempcombinerequest.get("ivb")
                        ))) {

                            // New device alias must be unique
                            if(!writer.userDeviceAliasExist(request.getParameter("alias"))) {

                                // Adding user device 
                                writer.combineNewDevice(
                                        request.getParameter("alias"),
                                        tempcombinerequest.get("ks"));
                                temp.removeTempCombineRequest();
                                aux =   "<r>" +
                                            "<devices>" +
                                                writer.getUserDeviceList() +
                                            "</devices>" +
                                        "</r>";
                            } else aux = "<r><e>device name already exist</e></r>";
                        } else aux = "<r><e>wrong pin</e></r>";
                    } else aux = "<r><e>wrong pin</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Allow user to uncombine a combined device
     */
    public void uncombine()
    {
        String id, ka, iva;
        String aux = "";
        TotpTempAutherBuilderWriter temp; 
        
        switch(request.getParameter("state")) {
            
            // Generate temporar credentials
            case("one"):
                temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
                id = temp.getID();
                ka = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                temp.insertTempUncombineRequest(ka, iva);
                aux =   "<r>" +
                            "<id>" + id + "</id>" +
                            "<ka>" + ka + "</ka>" +
                            "<iva>" + iva + "</iva>" +
                        "</r>";
                break;
            
            // System try to authenticate user into system and then add device
            case("two"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));
                
                // Request must known
                if(temp.existTempUncombineRequest()) {
                    
                    // Device exist and it will be remove
                    TotpAutherBuilderWriter writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    
                    Map<String, String> tempuncombinerequest = temp.getTempUncombineRequestCredentials();
                    // User credentials must match
                    if(writer.userPassphraseMatch(AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                request.getParameter("passphrase"),
                                tempuncombinerequest.get("ka"),
                                tempuncombinerequest.get("iva")
                            )
                    ))) {
                    
                        if(writer.userHasMoreThanOneDevice()) {
                        
                            // User device will remove
                            writer.uncombineUserDevice(request.getParameter("alias"));
                            temp.removeTempUncombineRequest();
                            aux =   "<r>" +
                                        "<devices>" +
                                            writer.getUserDeviceList() +
                                        "</devices>" +
                                    "</r>";
                            
                        } else aux = "<r><e>it's not possible remove device becase there is only one device combine</e></r>";
                    } else aux = "<r><e>wrong credentials</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
    
    /**
     * Allow user to remove interim state and recreate credentials to calculate
     *  authentication pin
     */
    public void renew()
    {
        String id, ks, ka, kb, iva, ivb;
        String aux = "";
        TotpTempAutherBuilderWriter temp;
        
        switch(request.getParameter("state")) {
            
            // Generate temporary credentials
            case("one"):
                temp = new TotpTempAutherBuilderWriter(AutherCrypto.generateID());
                id = temp.getID();
                ks = AutherCrypto.generateKey();
                ka = AutherCrypto.generateAESKey();
                kb = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                ivb = AutherCrypto.generateAESIV();
                temp.insertTempRenewRequest(ka, kb, ks, iva, ivb);
                aux =   "<r>" +
                            "<id>" + id + "</id>" +
                            "<ka>" + ka + "</ka>" +
                            "<ks>" + ks + "</ks>" +
                            "<iva>" + iva + "</iva>" +
                            "<kb>" + kb + "</kb>" +
                            "<ivb>" + ivb + "</ivb>" +
                        "</r>";
                break;
                
            // System try to authenticate user into system and the retire interim state
            case("two"):
                temp = new TotpTempAutherBuilderWriter(request.getParameter("id"));
                
                // Request must be known
                if(temp.existTempRenewRequest()){
                    
                    Map<String, String> temprenewrequest = temp.getTempRenewRequestCredentials();
                    TotpAutherBuilderWriter writer = new TotpAutherBuilderWriter(request.getParameter("username"));
                    
                    // Temporary encrypted pin must match with calculated
                    if(writer.userPassphraseMatch(AutherCrypto.hash256(
                            AutherCrypto.decrypt(
                                    request.getParameter("passphrase"),
                                    temprenewrequest.get("kb"),
                                    temprenewrequest.get("ivb")
                            )
                    ))) {
                        if(writer.totpPinMatch(
                                temprenewrequest.get("ks"),
                                AutherCrypto.decrypt(
                                    request.getParameter("twoFactor"),
                                    temprenewrequest.get("ka"),
                                    temprenewrequest.get("iva")
                                )
                        )) {
                            
                            // Change interim state
                            writer.setInterimStateTo(0);

                            // Renew device key
                            writer.updateDeviceKey(
                                    request.getParameter("alias"),
                                    temprenewrequest.get("ks"));
                            aux =   "<r>" +
                                        "<devices>" +
                                            writer.getUserDeviceList() +
                                        "</devices>" + 
                                    "</r>";
                            
                            // Delete temporary renew request
                            temp.removeTempRenewRequest();
                            
                        } else aux = "<r><e>wrong credentials</e></r>";
                    } else aux = "<r><e>wrong credentials</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
}
