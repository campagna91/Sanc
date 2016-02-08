/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth;
// Required packages
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.ProtocolException;
// Custom packages
import sanc.auth.totp.TotpAutherBuilder;
import sanc.auth.matrix.MatrixAutherBuilder;
/**
 *
 * @author champ
 */
public class AutherDirector {
    
    // Concrete auther builder
    AutherBuilder builder = null;
    
    /**
     * Create a concrete AutherBuilder for requested protocol
     * 
     * @param protoType - Type of procolo
     * @throws ProtocolException - If requested protocol is not supported
     */
    public AutherDirector(String protoType) 
        throws ProtocolException 
    {
        switch(protoType) {
            case("totp"):
                builder = new TotpAutherBuilder();
                break;
                
            case("matrix"):
                builder = new MatrixAutherBuilder();
                break;
                
            default:
                // unsupported protocol
                throw new ProtocolException();     
        }
    }
    
    /**
     * Forward request processing to competent subclass
     * 
     * @param request - Servlet request
     * @param response - Servlet response
     */
    public final void process(HttpServletRequest request, HttpServletResponse response) 
    {
        builder.process(request, response);
    }
}
