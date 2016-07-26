/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

/**
 *
 * @author ELDARS
 */
public class MyKazWin {
    
    public static String UTF8To1048Convert(String s){
        return 
              s.replace("ә", "ј").
                replace("ң", "ѕ").
                replace("ғ", "є").
                replace("ү", "ї").
                replace("ұ", "ў").
                replace("қ", "ќ").
                replace("ө", "ґ").                
                replace("һ", "ћ").
                replace("Ә", "[Ј]").
                replace("Ң", "[Ѕ]").
                replace("Ғ", "[Є]").
                replace("Ү", "[Ї]").
                replace("Ұ", "[Ў]").
                replace("Қ", "[Ќ]").
                replace("Ө", "[Ґ]").
                replace("Һ", "[Ћ]");
        
                        
    }
    
}
