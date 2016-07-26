/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import kz.bssproxy.connector.CapConnector;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 *
 * @author 00009314
 */
public class CapClient {
    
    int capId;
    boolean error;

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }
    
    

    String errorCode;
    String errorText;

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorText() {
        return errorText;
    }
    
    

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
    
    
    public int getCapId() {
        return capId;
    }

    public void setCapId(int capId) {
        this.capId = capId;
    }
    
    
            
    String responseXML;

    public String getResponseXML() {
        return responseXML;
    }

    public void setResponseXML(String responseXML) {
        this.responseXML = responseXML;
    }
    
    public CapClient() {
        
    }
    
    public void doReverse() throws Exception{
        
        doRequest("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://bus.colvir.com/module/service/cap/types\" xmlns:exp=\"http://bus.colvir.com/exp\" xmlns:data=\"http://bus.colvir.com/exp/data\">\n" +
"   <soapenv:Header/>\n" +
"   <soapenv:Body>\n" +
"      <typ:capReq capType=\"RVSL\" version=\"1.0\" xmlns:typ=\"http://bus.colvir.com/module/service/cap/types\">\n" +
"         <typ:header>\n" +
"            <typ:extId>1002</typ:extId>\n" +
"            <typ:extDt>2012-07-16T12:59:59</typ:extDt>\n" +
"            <typ:channel>IC</typ:channel>\n" +
"            <typ:lang>en</typ:lang>\n" +
"         </typ:header>\n" +
"         <typ:body>\n" +
"            <typ:trnType>2399</typ:trnType>\n" +
"            <typ:dscr>General reversal</typ:dscr>\n" +
"            <typ:srcCapId>"+ Integer.toString(getCapId())+ "</typ:srcCapId>\n" +
"            <typ:rvslType>0</typ:rvslType>\n" +
"         </typ:body>\n" +
"      </typ:capReq>\n" +
"\n" +
"   </soapenv:Body>\n" +
"</soapenv:Envelope>");
    }
    
    

    @SuppressWarnings("empty-statement")
    public void doRequest(String capReq) throws  Exception
    {
    CapConnector cc = new CapConnector(capReq);    
    String resp = cc.getResponse();
    setResponseXML(resp);
    Document dom;            
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    XPath xPath = XPathFactory.newInstance().newXPath();
    DocumentBuilder db = dbf.newDocumentBuilder();
    
    dom = db.parse(new InputSource(new ByteArrayInputStream(getResponseXML().getBytes("UTF-8"))));    
    XPathExpression xPathExpression = xPath.compile("/Envelope/Body/capRes/header/capId/text()");
    XPathExpression xPathExpressionErrorCode = xPath.compile("/Envelope/Body/capRes/header/errCode/text()");
    XPathExpression xPathExpressionErrorText = xPath.compile("/Envelope/Body/capRes/header/errText/text()");
    
    //if (((String)xPathExpression.evaluate(dom, XPathConstants.STRING)).isEmpty())
    if ((!((String)xPathExpressionErrorCode.evaluate(dom, XPathConstants.STRING)).isEmpty())
        &&
    (!((String)xPathExpressionErrorCode.evaluate(dom, XPathConstants.STRING)).equals("0")))
                
    {
        setCapId(0);
        setError(true);
        setErrorCode((String)xPathExpressionErrorCode.evaluate(dom, XPathConstants.STRING));
        setErrorText((String)xPathExpressionErrorText.evaluate(dom, XPathConstants.STRING));
        
    }
    else if (!((String)xPathExpression.evaluate(dom, XPathConstants.STRING)).isEmpty())
    {
        setCapId(Integer.parseInt((String)xPathExpression.evaluate(dom, XPathConstants.STRING)));
        setError(false);
        setErrorCode("0");
        
    }
    else
    {
        setCapId(0);
        setError(true);
        setErrorCode("999");        
    }
    
        System.err.println("ErrorCode="+getErrorCode());
    
    
         
    }

   

    
    
}
