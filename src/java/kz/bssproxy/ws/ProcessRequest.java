/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.ws;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import kz.bssproxy.config.BSSProxyConfig;
import kz.bssproxy.connector.AplatClient;
import kz.bssproxy.connector.Way4DBClient;
import kz.bssproxy.extend.XSLUtils;
import kz.bssproxy.utils.BSSTransformer;
import kz.bssproxy.utils.CBSGetCardNumberByABS;
import kz.bssproxy.utils.CBSProcess;
import kz.bssproxy.utils.CapClient;
import static kz.bssproxy.utils.DBLogger.InsertXMLLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


/**
 *
 * @author ELDARS
 */
public class ProcessRequest {
    
    public static String Process(String xml){
        
        
        String AnswerBSS;
        String capAnswer;
        String REQTYPE = null;
        String ID = null;
        String customerId;
      
        try
        {                
            
            xml =new String(xml.getBytes("CP1251"),"UTF8");            
            xml = xml.replace("�?","И");//BUG TOMCAT FIX
            Document dom;            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            XPath xPath = XPathFactory.newInstance().newXPath();
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
            Element doc = dom.getDocumentElement();
            ID = doc.getAttribute("ID");     
            
            customerId = (String)xPath.compile("//BSMessage/BSHead/@CustomerID").evaluate(dom, XPathConstants.STRING);            
            REQTYPE = doc.getAttribute("xmlns:x").substring(5);
            InsertXMLLog(xml,ID,REQTYPE,"IC_OUT",1);
            CapClient capClient;


            if (BSSProxyConfig.getInstance().isCbsFormat(REQTYPE))
            {
                CBSProcess stm = new CBSProcess(REQTYPE,xml);
                AnswerBSS = stm.getResponse();
            }
            else
            {
                
            String s = "";
            
            if (BSSProxyConfig.getInstance().isCapFormat(REQTYPE))
            {
                s = BSSTransformer.TransformToString(xml,
                    BSSProxyConfig.getInstance().getXSLRequest(REQTYPE));                
            }
                            
            switch (REQTYPE) {
                case "GETCARDIBAN": 
                    String pGETCARDIBAN = (String)xPath.compile("//BSMessage/BSRequest/@CardNum").evaluate(dom, XPathConstants.STRING);                                
                    AnswerBSS = Way4DBClient.getCardIban(pGETCARDIBAN); 
                    break;                
                case "CARDLIST":                    
                    String CARDLISTCardNum = new CBSGetCardNumberByABS(customerId).getCardNumber();
                    AnswerBSS = Way4DBClient.getCardList(CARDLISTCardNum); 
                    break;
                case "CARDSTM":                    
                    AnswerBSS = Way4DBClient.getStatementByCard(ID,dom);
                    break;
                case "APLAT":                    
                //Реализация оплаты без ПС24                               
                int iAPLAT =8888;
                String PUTYPEAPLAT_EXTPAYID = (String)xPath.compile("//BSMessage/BSRequest/@EXTPAYID").evaluate(dom, XPathConstants.STRING);            
                
                if (PUTYPEAPLAT_EXTPAYID.equals("10")){                    
                String body = "<body id=\"signedContent\">"+(String)xPath.compile("//BSMessage/BSRequest/PSPAYDATA/text()").evaluate(dom, XPathConstants.STRING)+"</body>";            
                System.err.println("!!!BODY!!!"+body);
                //String body ="<body id=\"signedContent\"><payments><payment><ct><id>+"+PUTYPE_PSREF+"</id><date>"+PUTYPE_ReqDateTime+"</date></ct><service><id>"+PUTYPE_PSPUREF+"</id><accountId>"+PUTYPE_ACC+"</accountId><amount>"+PUTYPE_A+"</amount><commission>0.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";                
                iAPLAT= AplatClient.SendRequest(body,2);               
                }
                AnswerBSS = "<x:BSMessage xmlns:x=\"BS_A_"+REQTYPE+"\" Version=\"STD1.0\" ID=\"2735375534244490\""
                    + " AnsDateTime=\"2014-05-22T14:50:24.0614\"><BSHead CustomerID=\"2\" OutSysID=\"WebSphere\" ABSMessage=\""+Integer.toString(iAPLAT)+"\">\n" +
                    "<Errors><m l=\"0\" t=\"0\" c=\"BS:2\" e=\"\" s=\"0\"/></Errors></BSHead><BSAnswer/></x:BSMessage>\n";                
                                    
                break;                   
                case "PUPAYTEST":                    
                    String bodyPUAVAI = "<body id=\"signedContent\">\n" +
                        "    <serviceId>8</serviceId>\n" +
                        "    <accountId>5054583</accountId>\n" +
                        "    <amount>123</amount>\n" +
                        "    <commission>10</commission>\n" +
                        "  </body>";            
                    System.out.println("!!!BODY!!!"+bodyPUAVAI);
                    //String body ="<body id=\"signedContent\"><payments><payment><ct><id>+"+PUTYPE_PSREF+"</id><date>"+PUTYPE_ReqDateTime+"</date></ct><service><id>"+PUTYPE_PSPUREF+"</id><accountId>"+PUTYPE_ACC+"</accountId><amount>"+PUTYPE_A+"</amount><commission>0.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";                
                    int iPUAVAI = AplatClient.SendRequest(bodyPUAVAI,1);                     
                    //throw new Exception("PUAVAIABLE TEST"+Integer.toString(iPUAVAI)+" ");
                    AnswerBSS = "<x:BSMessage xmlns:x=\"BS_A_"+REQTYPE+"\" Version=\"STD1.0\" ID=\"2735375534244490\""
                    + " AnsDateTime=\"2014-05-22T14:50:24.0614\"><BSHead CustomerID=\"2\" OutSysID=\"WebSphere\" ABSMessage=\""+Integer.toString(iPUAVAI)+"\">\n" +
                    "<Errors><m l=\"0\" t=\"0\" c=\"BS:2\" e=\"\" s=\"0\"/></Errors></BSHead><BSAnswer/></x:BSMessage>\n";
                    break;
                case "PUPAY":
                    
                //Реализация простой оплаты
                capClient = new CapClient();
                capClient.doRequest(s);
                capAnswer =capClient.getResponseXML();    
                if (capClient.isError()) 
                    throw new Exception(capClient.getErrorText());
                InsertXMLLog(capAnswer,ID,REQTYPE,"CAP",1);
                
                
                String PUTYPE_EXTPAYID = (String)xPath.compile("//BSMessage/BSRequest/@EXTPAYID").evaluate(dom, XPathConstants.STRING);            
                
                if (PUTYPE_EXTPAYID.equals("10")){
                    
                String body = "<body id=\"signedContent\">"+(String)xPath.compile("//BSMessage/BSRequest/PSPAYDATA/text()").evaluate(dom, XPathConstants.STRING)+"</body>";            
                System.err.println("!!!BODY!!!"+body);
                //String body ="<body id=\"signedContent\"><payments><payment><ct><id>+"+PUTYPE_PSREF+"</id><date>"+PUTYPE_ReqDateTime+"</date></ct><service><id>"+PUTYPE_PSPUREF+"</id><accountId>"+PUTYPE_ACC+"</accountId><amount>"+PUTYPE_A+"</amount><commission>0.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";
                
                int i= AplatClient.SendRequest(body,2);
                if ((i!=0)&&(i!=-1))
                    {      
                        //Откат проводки
                        capClient.doReverse();
                        throw new Exception("Произошла ошибка при проведении в АвангардПлат. Код ошибки "+Integer.toString(i));
                    }                

                }
                AnswerBSS = BSSTransformer.TransformToString(capAnswer,BSSProxyConfig.getInstance().getXSLAnswer(REQTYPE));
                break;
                    
                default:
                    capClient = new CapClient();
                    capClient.doRequest(s);
                    capAnswer =capClient.getResponseXML();                    
                    InsertXMLLog(capAnswer,ID,REQTYPE,"CAP",1);
                    AnswerBSS = BSSTransformer.TransformToString(capAnswer,BSSProxyConfig.getInstance().getXSLAnswer(REQTYPE));
                    
                    

                        break;
            }
            }
            AnswerBSS = AnswerBSS.replace("BSS_CUSTOMER_ID", customerId).replace("BSS_ID", ID);
            InsertXMLLog(AnswerBSS,ID,REQTYPE,"IC_IN",1);
            return AnswerBSS;
        }   
    
    catch(Exception e)
    {
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);        
        e.printStackTrace(pw);
        e.printStackTrace();
        InsertXMLLog( sw.toString(),ID,REQTYPE,"CAP",0);

        
  return 
                "<x:BSMessage xmlns:x=\"BS_A_"+REQTYPE+"\" Version=\"STD1.0\" ID=\"2735375534244490\""
+ " AnsDateTime=\"2014-05-22T14:50:24.0614\"><BSHead CustomerID=\"2\" OutSysID=\"WebSphere\" ABSMessage=\""+XSLUtils.escapeHTML(e.getMessage())  +"\">\n" +
"<Errors><m l=\"0\" t=\"0\" c=\"BS:2\" e=\"\" s=\"0\"/></Errors></BSHead><BSAnswer/></x:BSMessage>\n";
    }        
    }
}
