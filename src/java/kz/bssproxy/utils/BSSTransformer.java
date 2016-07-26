/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author 00009314
 * Transforming XSL
 */
public class BSSTransformer {
    
    
        public static String TransformToString(String xmlString,String xlsString)  throws Exception {
            
//            if (1==1)
//            {
//                xmlString=xmlString.replace("<capRes", "<ns3:capRes");
//                xmlString=xmlString.replace("<body", "<ns3:body");
//                xmlString=xmlString.replace("<xDataOut", "<ns3:xDataOut");
//                xmlString=xmlString.replace("<xDataOut", "<ns3:xDataOut");
//                xmlString=xmlString.replace("<backoffice_response", "<ns7:backoffice_response");
//                
//            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StringWriter writer = new StringWriter();
            Source xmlDoc = new StreamSource(new StringReader( xmlString ));
            Source xslDoc = new StreamSource(new StringReader( xlsString ));
            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(
            xmlDoc, 
            new javax.xml.transform.stream.StreamResult(writer));            
            System.err.println("xmlString = "+xmlString);
            System.err.println("xlsString = "+xlsString);
            System.err.println("RES = "+writer.toString());
            return writer.toString();
    }
        
        
    
}
