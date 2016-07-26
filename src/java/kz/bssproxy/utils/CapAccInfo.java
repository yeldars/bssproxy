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
public class CapAccInfo {
    
    
String balance;
boolean exists = false;

    public String getBalance() {
        return balance;
    }

    public boolean isExists() {
        return exists;
    }

    
    public CapAccInfo(String accCode) throws Exception{
        
        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://bus.colvir.com/module/service/cap/types\" xmlns:exp=\"http://bus.colvir.com/exp\" xmlns:data=\"http://bus.colvir.com/exp/data\">\n" +
"  <soapenv:Header/>\n" +
"   <soapenv:Body>\n" +
"<capReq capType=\"BALN\" version=\"1.0\" xmlns=\"http://bus.colvir.com/module/service/cap/types\">\n" +
"            <header>\n" +
"                     <typ:extId>BALN</typ:extId>\n" +
"                       <typ:extDt>2014-04-18T10:00:00</typ:extDt>                             \n" +
"                        <channel>IC</channel>\n" +
"                        <lang>en</lang>\n" +
"            </header>\n" +
"            <body>\n" +
"                     <typ:trnType>3006</typ:trnType>\n" +
"<idnType>IBN</idnType>                \n" +
"<idnCode>"+accCode+"</idnCode>\n" +
"\n" +
"            </body>\n" +
"</capReq>\n" +
"   </soapenv:Body>\n" +
"</soapenv:Envelope>";
    CapConnector cc = new CapConnector(request);
    String resp =cc.getResponse();
        
    Document dom;            
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    XPath xPath = XPathFactory.newInstance().newXPath();
    DocumentBuilder db = dbf.newDocumentBuilder();
    
    dom = db.parse(new InputSource(new ByteArrayInputStream(resp.getBytes("UTF-8"))));    
    XPathExpression xPathExpressionIIN = xPath.compile("Envelope/Body/capRes/body/finRes/bal/text()");            

    this.balance = (String)xPathExpressionIIN.evaluate(dom, XPathConstants.STRING);
    this.exists = ! cc.isError();
        
    }

    
}
