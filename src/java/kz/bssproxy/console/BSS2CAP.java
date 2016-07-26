/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.console;

import static kz.bssproxy.utils.BSSTransformer.TransformToString;
import kz.bssproxy.utils.CapClient;

/**
 *
 * @author 00009314
 */
public class BSS2CAP {

    
//    private static String outPath = "\\\\172.27.149.40\\c$\\Retail 2.5\\SUBSYS\\XML\\OUT\\";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try
        {
            
            String s = TransformToString("d:\\projects\\files\\ACCLIST\\BS_R_ACCLIST_EXAMPLE.xml",
                    "d:\\projects\\files\\ACCLIST\\BS_R_ACCLIST_TO_CAP_REQ_1722.xsl");
            
//            String s = TransformToString("d:\\projects\\files\\ACCACC\\BS_R_ACCACC_EXAMPLE.xml",
//                    "d:\\projects\\files\\ACCACC\\BS_R_ACCACC_TO_CAP_REQ_1711.xsl");
            
//            String s = TransformToString("d:\\projects\\files\\BS_R_STM_EXAMPLE.xml",
//                    "d:\\projects\\files\\BS_R_STM_TO_CAP_REQ_1721.xsl");            
            
            
            //System.out.println(s);
            
            
            CapClient capClient = new CapClient(); 
            capClient.doRequest(s);
            
            String capAnswer= capClient.getResponseXML();
            
            //System.out.println("CAP Answer " +capAnswer); 

            String AnswerBSS = TransformToString(capAnswer,"d:\\projects\\files\\ACCLIST\\CAP_RES_1722_TO_BS_A_ACCLIST.xsl");  
            //String AnswerBSS = TransformToString2(capAnswer,"d:\\projects\\files\\CAP_RES_1721_TO_BS_A_STM.xsl");  
                    
//            String AnswerBSS = TransformToString2(capAnswer,
//                    "d:\\projects\\files\\ACCACC\\CAP_RES_1711_TO_BS_A_ACCACC.xsl");                        

//            
            //System.out.println("BSS Answer " +AnswerBSS); 
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        

        
        //System.out.println("VATA EMES!");        
    }
    
}
