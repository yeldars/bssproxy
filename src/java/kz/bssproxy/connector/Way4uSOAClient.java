/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Модуль не используется
 */

package kz.bssproxy.connector;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author 00009314
 */
public class Way4uSOAClient {
    
    
    String responseXML;
    
    String respCode;
    
    boolean error;

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }
    
    
    

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
    
    public String getResponseXML() {
        return responseXML;
    }

    public void setResponseXML(String responseXML) {
        this.responseXML = responseXML;
    }
    
    
        public static double GetCardBalance(){
            
            
            
            return 0;
        }
        public static String GenerateXML(String operation, String sourceContractNumber,
                String destContractNumber,
                String reference, String details,
                double amount, String currency, String terminal)
        {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            
            String str1 = "" + "<UFXMsg direction=\"Rq\" msg_type=\"Doc\" scheme=\"WAY4Doc\" version=\"2.0\">";
            if (operation .equals( "TermToCard") || operation.equals("TermToOwnCard"))
                str1 = str1 + "<MsgId>AAA-555-333-EEE-23124161</MsgId>";
            if (operation .equals("CardToTerm") || operation .equals( "CardToOwnTerm"))
                str1 = str1 + "<MsgId>AAA-555-333-EEE-23124161</MsgId>";
            if (operation .equals( "CardToCard") || operation .equals( "CardToOwnCard"))
                str1 = str1 + "<MsgId>AAA-555-333-EEE-55784181</MsgId>";
            if (operation .equals( "CardToAnothersCard"))
                str1 = str1 + "<MsgId>AAA-555-333-EEE-55784204</MsgId>";
            String str2 = str1 + "<Source app=\"SOA\"/><MsgData><Doc><TransType><TransCode>";
            if (operation .equals( "TermToCard" )|| operation .equals( "TermToOwnCard"))
                str2 = str2 + "<MsgCode>SOA-CREDIT</MsgCode>";
            if (operation .equals( "CardToTerm") || operation .equals( "CardToOwnTerm"))
                str2 = str2 + "<MsgCode>SOA-DEBIT</MsgCode>";
            if (operation .equals( "CardToCard") || operation .equals( "CardToOwnCard"))
                str2 = str2 + "<MsgCode>01000F</MsgCode><ServiceCode>P2P</ServiceCode>";
            if (operation .equals( "CardToAnothersCard"))
                str2 = str2 + "<MsgCode>01000F</MsgCode><ServiceCode>P2P</ServiceCode>";
            String str3 = str2 + "</TransCode>" + "<TransCondition>,WEB,ENET,LINE_SECURE,</TransCondition>" 
                    + "<TransRules><TransRule><ParmCode>CreditTransCondition</ParmCode><Value>,WEB,ENET,LINE_SECURE,</Value></TransRule><TransRule><ParmCode>UTP_SIC_GR</ParmCode><Value>R</Value></TransRule><TransRule><ParmCode>UTP_CRD_BRAND</ParmCode><Value/></TransRule><TransRule><ParmCode>UTP_ACQ_BIN</ParmCode><Value/></TransRule></TransRules>" 
                    + "</TransType><DocRefSet><Parm><ParmCode>SRN</ParmCode><Value>" + 
                    reference + "</Value></Parm></DocRefSet><LocalDt>" + date + "</LocalDt>" + "<Description>" +
                    details + "</Description>" + "<Requestor><ContractNumber>" + sourceContractNumber + "</ContractNumber></Requestor>" + 
                    "<ContractFor><ContractNumber>" + sourceContractNumber + "</ContractNumber>";
            if (!operation.equals("CardToAnothersCard"))
                str3 = str3 + "<MemberId>W4C</MemberId>";
            String str4 = str3 + "</ContractFor>" + "<Source><ContractNumber>" + terminal + "</ContractNumber></Source>" + "<Originator><ContractNumber>" + sourceContractNumber + "</ContractNumber>";
            if (!operation.equals("CardToAnothersCard"))
                str4 = str4 + "<MemberId>W4C</MemberId>";
            String str5 = str4 + "</Originator>" + "<Destination>";
            String str6 = !(destContractNumber.isEmpty()) ? str5 + "<ContractNumber>" + destContractNumber + "</ContractNumber>" : str5 + "<ContractNumber/>";
            if (!operation .equals( "CardToAnothersCard"))
                str6 = str6 + "<MemberId>W4C</MemberId>";
            return str6 + "</Destination>" + "<Transaction><Currency>" + 
                    currency + "</Currency><Amount>" + 
                    Double.toString(amount).replace(',', '.') + "</Amount>" +
                    "<Extra><Type>AddInfo</Type><Details>USE_DOC_DATA;PTID=C07;</Details></Extra>"
                    + "</Transaction></Doc></MsgData></UFXMsg>";    
            
            
        }
        
@SuppressWarnings("empty-statement")
//Непосредственный запрос к WAY4U-SOA 
    public void doRequest(String ufxReq) throws  Exception
    {
        try{
         URL url = new URL("http://172.31.3.201:7770/way4u-soa2/httpadapter");
         HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();;
         urlConn.setDoInput (true);
         urlConn.setConnectTimeout(10);
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
         
         System.out.println("REQ "+ufxReq);
         output.write(ufxReq.getBytes("UTF-8"));
         output.flush();
         output.close();
         
        InputStream in = null;

        try{
            
            in = urlConn.getInputStream();        
        }
        catch(IOException eee)
        {
        eee.printStackTrace();
        in =urlConn.getErrorStream();        
        }
         
    int bytesRead;
    
    byte[] buffer = new byte[8192];
    
    StringBuilder sb = new StringBuilder();       
    while((bytesRead = in.read(buffer)) != -1) { // InputStream.read() returns -1 at EOF
        sb.append(new String(buffer,0,bytesRead));
        buffer = new byte[8192];
        bytesRead=0;
    }
    setResponseXML(sb.toString());
    
    Document dom;            
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
    XPath xPath = XPathFactory.newInstance().newXPath();
    DocumentBuilder db = dbf.newDocumentBuilder();
    
    dom = db.parse(new InputSource(new ByteArrayInputStream(getResponseXML().getBytes("UTF-8"))));    
    XPathExpression xPathExpression = xPath.compile("/UFXMsg/@resp_code");
    try{
        setRespCode((String)xPathExpression.evaluate(dom, XPathConstants.STRING));
        System.out.println("Resp_code="+getRespCode());
        setError(!getRespCode().equals("0"));
    }
    catch(XPathExpressionException exc){
        setRespCode("500");
        setError(true);
        exc.printStackTrace();
    }    
    }            
    catch(IOException | ParserConfigurationException | SAXException | XPathExpressionException exc1)
    {
     
        setRespCode("600");
        setError(true);
        exc1.printStackTrace();        
    }
    }
    
}
