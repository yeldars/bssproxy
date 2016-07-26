/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import kz.bssproxy.connector.CapConnector;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author BSS
 */
public class CapClientInfo {
    
    
String iin;
String birthDay;

    public String getIin() {
        return iin;
    }

    private void setIin(String iin) {
        this.iin = iin;
    }

    public String getBirthDay() {
        return birthDay;
    }

     
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }


    public CapClientInfo(String cliCode) throws Exception{
        
        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://bus.colvir.com/module/service/cap/types\" xmlns:exp=\"http://bus.colvir.com/exp\" xmlns:data=\"http://bus.colvir.com/exp/data\">\n" +
"  <soapenv:Header/>\n" +
"   <soapenv:Body>\n" +
"<capReq capType=\"INFO\" version=\"1.0\" xmlns=\"http://bus.colvir.com/module/service/cap/types\">\n" +
"            <header>\n" +
"                     <typ:extId>INFO1</typ:extId>\n" +
"                       <typ:extDt>2014-04-18T10:00:00</typ:extDt>                             \n" +
"                        <channel>IC</channel>\n" +
"                        <lang>en</lang>\n" +
"            </header>\n" +
"            <body>\n" +
"                     <typ:trnType>1720</typ:trnType>\n" +
"<idnType>CLI</idnType>                \n" +
"<idnCode>"+cliCode+"</idnCode>\n" +
"\n" +
"            </body>\n" +
"</capReq>\n" +
"   </soapenv:Body>\n" +
"</soapenv:Envelope>";
        String resp = new CapConnector(request).getResponse();
        
        
    Document dom;            
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    XPath xPath = XPathFactory.newInstance().newXPath();
    DocumentBuilder db = dbf.newDocumentBuilder();
    
    dom = db.parse(new InputSource(new ByteArrayInputStream(resp.getBytes("UTF-8"))));    
    XPathExpression xPathExpressionIIN = xPath.compile("Envelope/Body/capRes/body/xDataOut/backoffice_response/customer_details_response/TAX_ID/text()");            

    setIin(
    (String)xPathExpressionIIN.evaluate(dom, XPathConstants.STRING));
        
    }

    
}
