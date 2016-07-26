/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.connector;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import kz.bssproxy.config.BSSProxyConfig;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author BSS
 */
public class CapConnector {
    
    String response = "";
    private String errorCode;
    private boolean error;
    private int capId;

    public String getResponse() {
        return response;
    }
    
    
    public CapConnector(String capReq) throws Exception{
          

         
         //URL url = new URL("http://172.27.149.34:8181/cxf/SOAPService");         
         URL url = new URL(BSSProxyConfig.getInstance().getCapUrl());        
         //URL url = new URL("http://172.30.124.191:8181/cxf/SOAPService");
         //URL url = new URL("http://172.30.121.190:8080/colvir-cap-service/SOAPService");
         //URL url = new URL("http://172.27.129.101:8080/colvir-cap-service/SOAPService");         
         HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
         urlConn.setDoInput (true);
         urlConn.setConnectTimeout(300);
         urlConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
         // Let the RTS know that we want to do output.
         urlConn.setDoOutput (true);
         // No caching, we want the real thing.
         urlConn.setUseCaches (false);
         urlConn.setRequestMethod("POST");         
         urlConn.connect();
         //DataOutputStream output = null;

        
         DataOutputStream output;
         output = new DataOutputStream(urlConn.getOutputStream());
         output.write(capReq.getBytes("UTF-8"));
         output.flush();
         output.close();
         
         
         //DataInputStream input = new DataInputStream (urlConn.getInputStream());
        
        InputStream in;
        
        
        try{
            
            in = urlConn.getInputStream();        
        }
        catch(IOException eee)
        {
        in =urlConn.getErrorStream(); 
        eee.printStackTrace();
        }

        
        String encoding = urlConn.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
                
        //byte[] buffer = new byte[urlConn.getContentLength()];
        //int bytesRead = in.read(buffer);
        //String page = new String(buffer, 0, bytesRead, "UTF-8");                
         
        
         
         
    int bytesRead;
    
    byte[] buffer = new byte[8192];
    
    StringBuilder sb = new StringBuilder();       
    while((bytesRead = in.read(buffer)) != -1) { // InputStream.read() returns -1 at EOF
        sb.append(new String(buffer,0,bytesRead,"UTF-8"));
        buffer = new byte[8192];
        bytesRead=0;
    }
    this.response = sb.toString();
    
    
    
    Document dom;            
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    XPath xPath = XPathFactory.newInstance().newXPath();
    DocumentBuilder db = dbf.newDocumentBuilder();
    
    dom = db.parse(new InputSource(new ByteArrayInputStream(this.response.getBytes("UTF-8"))));    
    XPathExpression xPathExpression = xPath.compile("/Envelope/Body/capRes/header/capId/text()");
    XPathExpression xPathExpressionErrorCode = xPath.compile("/Envelope/Body/capRes/header/errCode/text()");
    
    if (((String)xPathExpression.evaluate(dom, XPathConstants.STRING)).isEmpty())
    {
        setCapId(0);
        setError(true);
        setErrorCode((String)xPathExpressionErrorCode.evaluate(dom, XPathConstants.STRING));
    }
    else
    {
        setCapId(Integer.parseInt((String)xPathExpression.evaluate(dom, XPathConstants.STRING)));
        setError(false);
        setErrorCode("0");
        
    }    
    
    }

    private void setCapId(int i) {
        this.capId = i;
    }

    private void setError(boolean b) {
        this.error=b;
    }

    private void setErrorCode(String ec) {
        this.errorCode=ec;
    }

    public int getCapId() {
        return capId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isError() {
        return error;
    }
    
    
}
