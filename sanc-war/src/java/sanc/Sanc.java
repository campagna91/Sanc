/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc;
// Required packages
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Custom package
import sanc.auth.AutherDirector;
// Exceptions
import java.io.IOException;
import java.net.ProtocolException;
import javax.servlet.ServletException;
/**
 *
 * @author champ
 */
@WebServlet(name = "Sanc", urlPatterns = {"/Sanc"})
public class Sanc extends HttpServlet {

    /**
     * Processes requests for <code>POST</code> methods.
     *
     * @param request - Servlet request
     * @param response - Servlet response
     * @throws IOException - If an I/O error occurs
     * @throws NullPointerException - If a parameter need is not passed
     * @throws ProtocolException - If requested protocol is not supported
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException 
    {
        // Set max inactive interval for session 2 minutes
        request.getSession().setMaxInactiveInterval(120);
        
        // Create a director and process request
        try {
            
            AutherDirector director;
            if(request.getParameter("protoType") != null)
                director = new AutherDirector(request.getParameter("protoType"));
            else 
                director = new AutherDirector(request.getSession().getAttribute("protoType").toString());
            
            director.process(request, response);
        } catch(ProtocolException | NullPointerException n) {
            System.out.println("ECCEZIONE: " + n.getMessage());
            try {
                response.sendRedirect("index.jsp");
            } catch(IOException e) {
                System.out.println("ECCEZIONE: " + e.getMessage());
            }
        } catch(Exception e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request - Servlet request
     * @param response - Servlet response
     * @throws ServletException - If a servlet-specific error occurs
     * @throws IOException - If an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Deny get request and redirect to error page
        //response.sendRedirect("error.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request - Servlet request
     * @param response - Servlet response
     * @throws ServletException - If a servlet-specific error occurs
     * @throws IOException - If an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // process POST request
        processRequest(request, response);
    }
}