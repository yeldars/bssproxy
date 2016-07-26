/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.console;

import java.util.logging.Level;
import java.util.logging.Logger;
import kz.bssproxy.connector.Way4uSOAClient;

/**
 *
 * @author 00009314
 */
public class BSS2Way4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
//4402573587679707 KZ546010002005889514
//6016963054986656 KZ256010002005703796
        //ELDARS KZ696010002000892677
        //String operation = "CardToAnothersCard";
        String operation = "TermToCard";        
        String sourceContractNumber = "4402573587679707";
        //String sourceContractNumber = "";
        String destContractNumber = "4402573587679707";
        //String destContractNumber = "";
        String reference = "00009314XXXX03";
        String details = "test";
        double amount = 1;
        String currency  = "KZT";
        //String terminal  =   "13566666";
        String terminal  =   "88882222";
        //String terminal  = "13538471";
        
        
        
        String xml = Way4uSOAClient.GenerateXML(operation,  sourceContractNumber,
                destContractNumber,
                 reference,  details,
                amount,  currency,  terminal);
        
        System.out.println(xml);
        String out = null;
        try {
            Way4uSOAClient way4Client = new Way4uSOAClient();
            way4Client.doRequest(xml);
            out = way4Client.getResponseXML();
        } catch (Exception ex) {
            Logger.getLogger(BSS2Way4.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Way4 Answer = "+out);
                
    }
    
}
