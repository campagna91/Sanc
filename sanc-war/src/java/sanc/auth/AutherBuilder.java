/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth;
// Required packages
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author champ
 */
public abstract class AutherBuilder {
    
    // Servlet request
    protected HttpServletRequest request;
    
    // Servlet response
    protected HttpServletResponse response;
    
    /**
     * Process an incoming request with the appropriate concrete class
     * 
     * @param request - Servlet request
     * @param response - Servlet response
     */
    public abstract void process(HttpServletRequest request, HttpServletResponse response);
}
