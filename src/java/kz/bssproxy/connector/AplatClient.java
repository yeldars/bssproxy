/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.connector;

/**
 *
 * @author BSS
 */

import kz.gamma.jce.provider.GammaTechProvider;
import kz.gamma.xmldsig.JCPXMLDSigInit;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.xml.transform.OutputKeys;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import kz.bssproxy.aplat.TrustAllX509TrustManager;
import kz.bssproxy.config.BSSProxyConfig;
import static kz.bssproxy.utils.DBLogger.InsertXMLLog;
import kz.gamma.util.encoders.Base64;
import org.w3c.dom.Element;

/**
 * Created by Yeldar Saumbayev
 * Date: 12.08.2014
 * Time: 11:01:48
 */
public class AplatClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        
        BSSProxyConfig.getInstance("C:\\Projects\\BSSPROXY\\bssproxy.conf");
        
        //Security.addProvider(new GammaTechProvider());
        Security.addProvider(new GammaTechProvider());
        JCPXMLDSigInit.init();        
        String body ="<body id=\"signedContent\"><payments><payment><ct><id>000000302</id><date>2014-08-13T10:57:51.0228</date></ct><service><id>228</id><accountId>777777777</accountId><amount>924.00</amount><commission>50.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";
        //String signStr = signAplatRequestBody("FSystemMGR","1F4559902460F29AA9C16147539357ED6D5AFF929282AAD2916822F9773E9775",body);
        String signStr = signAplatRequestBody("FSystem","8938AC8B5977A4235C814B6A78DE6D954B5F8B5A241B5606E8C203EB9A9B9E1B",body);
        String str= "<request><header><security>"+signStr+"</security></header>"+body+"</request>";
        System.out.println(str);
        //tint i = aplatHttpRequest(str,2,"1039","1389","2a27179561");        
        //int i = aplatHttpRequest(str,2,"10296","15866","2a27179561");        
    }
    
    public static int SendRequest(String body,int operation) {
    
        try{
            
        Security.addProvider(new GammaTechProvider());
        
        }
        catch(Exception e)
        {
            
        }
        JCPXMLDSigInit.init();  
        
        String signStr = signAplatRequestBody(BSSProxyConfig.getInstance().getAplatTumarProfile(),BSSProxyConfig.getInstance().getAplatTumarSerial(),body);
        String str= "<request><header><security>"+signStr+"</security></header>"+body+"</request>";
        try {
            return aplatHttpRequest(str,operation,
                    BSSProxyConfig.getInstance().getAplatTerminal(),
                    BSSProxyConfig.getInstance().getAplatTerminalUser(),
                    BSSProxyConfig.getInstance().getAplatTerminalPassword());
        }catch(Exception exc){            
            
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);        
        exc.printStackTrace(pw);
        exc.printStackTrace();
            
            InsertXMLLog( sw.toString(),"0","PUPAY","CAP",0);
            
            return 9999;
        }
        
    }

    /**
     *
     * @param profile
     * @param serial    */
    public static String signAplatRequestBody(String profile,String serial,String xmlStr) {
        try {
            // Формируем класс хранилища ключей, будут доступны все профайлы криптопровайдера.
            
            KeyStore store = loadKeyStore(profile, "");
            //Получение списка ключей
            Enumeration en = store.aliases();
            while (en.hasMoreElements()) {
                en.nextElement();                
            }
            // Получение закрытого ключа по серийному номеру сертификата
            PrivateKey prvKey =  (PrivateKey)store.getKey(serial, null);
            if (prvKey != null) {
                //Получение сертификата по серийному номеру 
                Certificate cert = store.getCertificate(serial);
                if (cert != null) {
                    
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    DocumentBuilder builder = dbf.newDocumentBuilder();
                    InputSource source = null;
                    source = new InputSource(new StringReader( xmlStr ));
                    // Подписываем XML документ
                    
                    String sigDoc = signXML(builder.parse(source), cert, prvKey);
                    //System.out.println(sigDoc);
                    return sigDoc;
                    // Проверяем подпись XML документа
                    //if (!validateXML(sigDoc))
                    //    throw new Exception("Подпись не прошла проверку");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Создаем экземпляр класса для работы с TumarCSP.
     * Данный метод загружает ключи из выбранного профайла, при этом можно задать пароль на профайл
     *
     * @param profileName
     * @param pass
     * @return
     * @throws NoSuchProviderException
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     */
    private static KeyStore loadKeyStore(String profileName, String pass) throws NoSuchProviderException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore store = KeyStore.getInstance("GKS", "GAMMA");
        store.load(new ByteArrayInputStream(profileName.getBytes()), pass.toCharArray());
        
        return store;
    }

    /**
     * Метод формирования подписи xml документа
     *
     * @param doc
     * @param cert
     * @param privKey
     * @return
     * @throws Exception
     */    
  
private static String signXML(Document doc, Certificate cert, PrivateKey privKey)
            throws Exception {
        String signMethod = "http://www.w3.org/2001/04/xmldsig-more#gost34310-gost34311";
        String digestMethod = "http://www.w3.org/2001/04/xmldsig-more#gost34311";
        XMLSignature sig = new XMLSignature(doc,"#signedContent", signMethod);
        
        Transforms transforms = new Transforms(doc);
        transforms.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        transforms.addTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");        
        sig.addDocument("#signedContent", transforms, digestMethod);
        sig.sign(privKey);
        sig.addKeyInfo((X509Certificate) cert);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(sig.getElement());
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();
        return xmlString;

    
    }



public static int aplatHttpRequest(String req,int operation,String group,String terminal,String password) throws Exception
{
InsertXMLLog(req,"0","PUPAY","APLAT_REQ",1);
String httpsURL =  BSSProxyConfig.getInstance().getAplatUrl(); // "https://91.195.226.20:8080";


SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());

HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
    public boolean verify(String string,SSLSession ssls) {
        return true;
    }
});

URL myurl = new URL(httpsURL);
HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();

con.setRequestMethod("POST");

con.setConnectTimeout(120000);

con.setRequestProperty("Content-length", String.valueOf(req.length())); 
con.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8"); 
con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
con.setRequestProperty("OperationType", Integer.toString(operation)); 
String pwd = new String(Base64.encode((group+'|'+terminal+':'+password).getBytes()));
con.setRequestProperty("Authorization", "Basic "+pwd); 

con.setDoOutput(true); 
con.setDoInput(true); 

try (DataOutputStream output = new DataOutputStream(con.getOutputStream())) {
    output.writeBytes(req);
}

StringBuffer inputLine; 
        try (DataInputStream input = new DataInputStream( con.getInputStream() )) {
            inputLine = new StringBuffer();
            String tmp;
            while(input.available()>0)
            {
                // reads characters encoded with modified UTF-8
                inputLine.append(input.readUTF());
                
                // print
                
            }  while (null != (tmp = input.readLine())) {            
             inputLine.append(tmp);
             //System.out.println(tmp);
         }
        } 

            System.err.println(inputLine);
            InsertXMLLog(inputLine.toString(),"0","PUPAY","APLAT_RES",1);
            
            Document dom;            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            XPath xPath = XPathFactory.newInstance().newXPath();
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(new InputSource(new ByteArrayInputStream(inputLine.toString().getBytes())));
            Element doc = dom.getDocumentElement();            
            
            String status = "9999";
            if  (operation==1){
                status =(String)xPath.compile("//response/body/data/er").evaluate(dom, XPathConstants.STRING);
               if (status.isEmpty())
                    status =(String)xPath.compile("//response/body/state").evaluate(dom, XPathConstants.STRING);
            }
            else if  (operation==2)                                    
            {                
                status = (String)xPath.compile("//response/body/data/payments/payment[1]/status").evaluate(dom, XPathConstants.STRING);             
            }
            
System.out.println("Resp Code:"+con .getResponseCode()); 
System.out.println("Resp Status:"+status); 
System.out.println("Resp Message:"+ con .getResponseMessage());     

return Integer.parseInt(status);
}


static {
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                // ip address of the service URL(like.23.28.244.244)
                if (hostname.equals(BSSProxyConfig.getInstance().getAplatHost()))
                    return true;
                return false;
            }
        });
}

}

