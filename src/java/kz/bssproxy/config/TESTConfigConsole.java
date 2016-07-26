/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.config;

/**
 *
 * @author ELDARS
 */
public class TESTConfigConsole {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BSSProxyConfig.getInstance("C:\\Projects\\BSSPROXY\\bssproxy.conf");
        System.out.println(BSSProxyConfig.getInstance().getXSLRequest("ACCLIST"));
        System.out.println(
        BSSProxyConfig.getInstance().isCbsFormat("ACCLIST"));
    }
    
}
