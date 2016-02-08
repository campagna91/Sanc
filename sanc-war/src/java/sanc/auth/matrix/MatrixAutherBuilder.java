/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.matrix;
// Request packages
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Custom packages
import sanc.auth.AutherBuilder;
import sanc.auth.AutherCrypto;
import sanc.auth.matrix.MatrixAutherBuilderWriter;
import sanc.auth.matrix.MatrixTempAutherBuilderWriter;
/**
 *
 * @author champ
 */
public class MatrixAutherBuilder extends AutherBuilder {
    
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
     * Allow user to register into system
     */
    public void signup() {
        String state = request.getParameter("state");
        String id, ka, ks, iva;
        String aux = "";
        MatrixAutherBuilderWriter writer;
        MatrixTempAutherBuilderWriter temp;
        switch(state) {
            
            // Generate temporary credential
            case("one"):
                    writer = new MatrixAutherBuilderWriter(request.getParameter("username"));
                    
                    // username must be unique
                    if (!writer.userExist()) {
                        temp = new MatrixTempAutherBuilderWriter(AutherCrypto.generateID());
                        id = temp.getID();
                        ka = AutherCrypto.generateAESKey();
                        iva = AutherCrypto.generateAESIV();
                        temp.insertTempSignupRequest(ka, iva);
                        aux =   "<r>"
                                    + "<id>" + id + "</id>"
                                    + "<ka>" + ka + "</ka>"
                                    + "<iva>" + iva + "</iva>"
                                + "</r>";
                    } else aux = "<r><e>user is not available</e></r>";
                break;
            
            // System try to insert user into users table 
            case("two"):
                writer = new MatrixAutherBuilderWriter(request.getParameter("username"));
                temp = new MatrixTempAutherBuilderWriter(request.getParameter("id"));

                // Signup request must be authorized
                if (temp.existTempSignupRequest()) {
                    Map<String, String> tempsignuprequest = temp.getTempSignupRequestCredentials();
                    ks = AutherCrypto.generateMatrix();

                    // Save new user
                    writer.saveSignupRequest(
                            AutherCrypto.hash256(
                                    AutherCrypto.decrypt(
                                            request.getParameter("password"),
                                            tempsignuprequest.get("ka"),
                                            tempsignuprequest.get("iva")
                                    )
                            ),
                            ks
                    );
                    
                    
                    temp.removeTempSignupRequest();  
                    aux =   "<r>" +
                                "<ks>" + ks + "</ks>" +
                            "</r>";
                    
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
     * Try to authenticate user into system
     */
    public void signin() {
        String state = request.getParameter("state");
        String id, ka, ks, kn, iva;
        String aux = "";
        MatrixAutherBuilderWriter writer;
        MatrixTempAutherBuilderWriter temp;
        switch(state) {
            
            // Temporary credentials are generated
            case("one"):
                temp = new MatrixTempAutherBuilderWriter(AutherCrypto.generateID());
                id = temp.getID();
                ka = AutherCrypto.generateAESKey();
                iva = AutherCrypto.generateAESIV();
                temp.insertTempSigninRequest(ka, iva);
                aux =   "<r>" +
                            "<id>" + id + "</id>" +
                            "<ka>" + ka + "</ka>" +
                            "<iva>" + ka + "</iva>" +
                        "</r>";
                break;
                
            // System try to authenticate user ad generate temporary credentials
            case("two"):
                temp = new MatrixTempAutherBuilderWriter(request.getParameter("id"));
                
                // Request must be known
                if(temp.existTempSigninRequest()) {
                    writer = new MatrixAutherBuilderWriter(request.getParameter("username"));
                    
                    // User must be registered
                    if(writer.userExist()) {
                        Map<String, String> tempsigninrequest = temp.getTempSigninRequestCredentials();
                        
                        // User hash password must match
                        if(writer.userPasswordMatch(AutherCrypto.hash256(
                                AutherCrypto.decrypt(
                                        request.getParameter("password"),
                                        tempsigninrequest.get("ka"),
                                        tempsigninrequest.get("iva")
                                )
                        ))) {
                            
                            ka = AutherCrypto.generateAESKey();
                            iva = AutherCrypto.generateAESIV();
                            id = AutherCrypto.generateID();
                            ks = AutherCrypto.generateRndSequence();
                            
                            if(writer.isTheFirstAccess()) {    
                                kn = ks.split("-")[4];
                            } else {
                                kn = Integer.toString(writer.getLastChanged());
                                ks =    kn + "-" +
                                        ks.split("-")[1] + "-" +
                                        ks.split("-")[2] + "-" +
                                        ks.split("-")[3] + "-" +
                                        ks.split("-")[4];
                            }
                            temp.setID(id);
                            temp.insertTempSigninConfirm(ka, kn, ks, iva);
                            aux =   "<r>" + 
                                        "<id>" + id + "</id>" +
                                        "<ka>" + ka + "</ka>" +
                                        "<iva>" + iva + "</iva>" +
                                        "<ks>" + ks + "</ks>" +
                                    "</r>";
                           
                        } else {
                            aux = "<r><e>wrong credentials</e>";
                            writer.increaseThrottlingCounter();
                            if(writer.isThreatThrottling())
                                aux += "<c></c>";
                            aux += "</r>";
                        }
                    } else aux = "<r><e>user not found</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
                
            case("three"):
                temp = new MatrixTempAutherBuilderWriter(request.getParameter("id"));
                
                // Request must ben known
                if(temp.existTempSigninConfirm()) {
                    
                    writer = new MatrixAutherBuilderWriter(request.getParameter("username"));
                    
                    // User must be registered
                    if(writer.userExist()) {
                        
                        Map<String, String> tempsigninconfirm = temp.getTempSigninConfirmCredentials();
                        
                        // Sequence sent must be match
                        if(writer.userSequenceMatch(
                                AutherCrypto.decrypt(
                                        request.getParameter("sequence"),
                                        tempsigninconfirm.get("ka"),
                                        tempsigninconfirm.get("iva")
                                ), tempsigninconfirm.get("ks")
                        )) {
                            
                            // User is correctly logged
                            writer.resetThrottlingCounter();
                            temp.removeTempSigninConfirm();
                            
                            // Change last request
                            writer.updateLastChanged(
                                    Integer.parseInt(tempsigninconfirm.get("kn")), 
                                    request.getParameter("sequence").split("-")[4]);
                            
                        } else {
                            aux = "<r><e>wrong credentials</e>";
                            writer.increaseThrottlingCounter();
                            if(writer.isThreatThrottling())
                                aux += "<c></c>";
                            
                            temp.removeTempSigninConfirm();
                            ka = AutherCrypto.generateAESKey();
                            iva = AutherCrypto.generateAESIV();
                            id = AutherCrypto.generateID();
                            ks = AutherCrypto.generateRndSequence();
                            
                            if(writer.isTheFirstAccess()) {    
                                kn = ks.split("-")[4];
                            } else {
                                kn = Integer.toString(writer.getLastChanged());
                                ks =    kn + "-" +
                                        ks.split("-")[1] + "-" +
                                        ks.split("-")[2] + "-" +
                                        ks.split("-")[3] + "-" +
                                        ks.split("-")[4];
                            }
                            temp.setID(id);
                            temp.insertTempSigninConfirm(ka, kn, ks, iva);
                            aux +=      "<id>" + id + "</id>" +
                                        "<ka>" + ka + "</ka>" +
                                        "<iva>" + iva + "</iva>" +
                                        "<ks>" + ks + "</ks>";
                            aux += "</r>";
                        }
                    } else aux = "<r><e>user doesn't exist</e></r>";
                } else aux = "<r><e>unhautorized request</e></r>";
                break;
        }
        try {
            response.getWriter().write(aux);
        } catch(IOException e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }
}
