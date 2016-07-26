package kz.bssproxy.console;

import kz.gamma.jce.provider.GammaTechProvider;
import kz.gamma.xmldsig.JCPXMLDSigInit;
import java.security.*;
import static kz.bssproxy.connector.AplatClient.aplatHttpRequest;
import static kz.bssproxy.connector.AplatClient.signAplatRequestBody;

/**
 * Created by Yeldar Saumbayev
 * Date: 12.08.2014
 * Time: 11:01:48
 */
public class TestAvangardHalyk {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        Security.addProvider(new GammaTechProvider());
        JCPXMLDSigInit.init();        
        String body ="<body id=\"signedContent\"><payments><payment><ct><id>000000302</id><date>2014-08-13T10:57:51.0228</date></ct><service><id>228</id><accountId>777777777</accountId><amount>924.00</amount><commission>50.00</commission><parameters/><subservices><subservice><parameters/></subservice></subservices></service></payment></payments></body>";
        
//        String body2 = "<body id=\"signedContent\">\n" +
//        "    <serviceId>748</serviceId>\n" +
//"    <accountId>yeldars</accountId>\n" +
//"    <amount>1700</amount>\n" +
//"    <commission>10</commission>" +
//        "  </body>";
        String signStr =  signAplatRequestBody("FSystemMGR","1F4559902460F29AA9C16147539357ED6D5AFF929282AAD2916822F9773E9775",body);
        //String signStr2 = signAplatRequestBody("FSystemMGR","1F4559902460F29AA9C16147539357ED6D5AFF929282AAD2916822F9773E9775",body2);
               
        String str= "<request><header><security>"+signStr+"</security></header>"+body+"</request>";
        //String str2= "<request><header><security>"+signStr2+"</security></header>"+body2+"</request>";
        //System.out.println(str2);  
        int i = aplatHttpRequest(str,2,"1039","1389","2a27179561");
        
    }

}
