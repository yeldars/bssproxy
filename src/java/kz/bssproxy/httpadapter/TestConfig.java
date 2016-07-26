/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.httpadapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.bssproxy.config.BSSProxyConfig;
import static kz.bssproxy.connector.AplatClient.signAplatRequestBody;
import kz.gamma.jce.provider.GammaTechProvider;
import kz.gamma.xmldsig.JCPXMLDSigInit;

/**
 *
 * @author BSS
 */
@WebServlet(name = "TestConfig", urlPatterns = {"/TestConfig"})
public class TestConfig extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        BSSProxyConfig cnfg = BSSProxyConfig.getInstance();
        
                
        
        try (PrintWriter out = response.getWriter()) {
            
            
                
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestConfig</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestConfig at " + request.getContextPath() + "</h1>");
            out.println("<h5>Config File " +cnfg.getFileName() + "</h5>");
            out.println("<h6>APlat Host "  +cnfg.getAplatHost()+ "</h6>");
            out.println("<h6>APlat Port "  +cnfg.getAplatPort()+ "</h6>");
            out.println("<h6>APlat Protocol "  +cnfg.getAplatProtocol()+ "</h6>");
            out.println("<h6>APlat URL "  +cnfg.getAplatUrl()+ "</h6>");
            out.println("<h6>Aplat Tumar Profile "  +cnfg.getAplatTumarProfile()+ "</h6>");
            out.println("<h6>Aplat Tumar Profile Password "  +cnfg.getAplatTumarProfilePassword()+ "</h6>");
            out.println("<h6>Aplat Tumar Serial "  +cnfg.getAplatTumarSerial()+ "</h6>");
            out.println("<h6>Aplat Terminal "  +cnfg.getAplatTerminal()+ "</h6>");
            out.println("<h6>Aplat Terminal User "  +cnfg.getAplatTerminalUser()+ "</h6>");
            out.println("<h6>Aplat Terminal Password "  +cnfg.getAplatTerminalPassword()+ "</h6>");
            
            try{
            Security.addProvider(new GammaTechProvider());
            JCPXMLDSigInit.init();        
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            String body ="<body id=\"signedContent\"><payments><payment><ct><id>000000302</id><date>2014-08-13T10:57:51.0228</date></ct><service><id>228</id><accountId>777777777</accountId><amount>924.00</amount><commission>50.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";
        
//        String body2 = "<body id=\"signedContent\">\n" +
//        "    <serviceId>748</serviceId>\n" +
//"    <accountId>yeldars</accountId>\n" +
//"    <amount>1700</amount>\n" +
//"    <commission>10</commission>" +
//        "  </body>";
            String signStr =  signAplatRequestBody(cnfg.getAplatTumarProfile(),cnfg.getAplatTumarSerial(),body);            
            out.println("<h6>Aplat Test Body "  +body+ "</h6>");
            out.println("<h6>Aplat Test Signed Body "  +signStr+ "</h6>");
            out.println("<h6>CBS DB Driver "  +cnfg.getCbsDBDriver()+ "</h6>");
            out.println("<h6>CBS JDBC "  +cnfg.getCbsJDBC()+ "</h6>");
            out.println("<h6>CBS User "  +cnfg.getCbsUser()+ "</h6>");
            out.println("<h6>CBS Password "  +cnfg.getCbsPassword()+ "</h6>");
            
            out.println("<h6>Way4 DB Driver "  +cnfg.getW4DBDriver()+ "</h6>");
            
            out.println("<h6>Way4 JDBC "  +cnfg.getW4JDBC()+ "</h6>");
            out.println("<h6>Way4 User "  +cnfg.getW4User()+ "</h6>");
            out.println("<h6>Way4 Password "  +cnfg.getW4Password()+ "</h6>");
            
            out.println("<h6>Way4 Contract List Procedure "  +cnfg.getW4ProcGetContract()+ "</h6>");
            out.println("<h6>Way4 Statement Procedure "  +cnfg.getW4ProcGetStmt()+ "</h6>");
            out.println("<h6>Way4 CreditCard Allow List "  +cnfg.getW4AllowCreditList()+ "</h6>");
            
            
//            out.println("<h6>BssProxy DB Driver "  +cnfg.getBpDBDriver()+ "</h6>");
//            out.println("<h6>BssProxy JDBC "  +cnfg.getBpJDBC()+ "</h6>");
//            out.println("<h6>BssProxy User "  +cnfg.getBpUser()+ "</h6>");
//            out.println("<h6>BssProxy Password "  +cnfg.getBpPassword()+ "</h6>");            
//            out.println("<h6>BssProxy Test Connection "  +BSSProxyConnection.testConnection()+ "</h6>");            
            
            out.println("<h6>Cap URL "  +cnfg.getCapUrl()+ "</h6>");            
            
            out.println("</body>");
            out.println("</html>");
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
