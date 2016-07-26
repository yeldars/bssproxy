/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

/**
 *
 * @author BSS
 */
public class CardUtils {
    
    public static String MaskCard(String cardN){                
        return cardN.substring(0,6)+"******"+cardN.substring(12,16);
    }
    
}
