/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.console;

import java.util.logging.Level;
import java.util.logging.Logger;
import kz.bssproxy.utils.BSSTransformer;

/**
 *
 * @author BSS
 */
public class XSLTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<x:BSMessage xmlns:x=\"BS_R_ACCACC\" Version=\"STD1.0\" ID=\"2735378752109350\" "
                    + "ReqDateTime=\"2014-05-25T20:53:30.0935\"><BSHead CustomerID=\"26991400001166\" "
                    + "SubSys=\"IC\" BranchID=\"\"/><BSRequest CUR=\"KZT\" CRD=\"398\" D=\"2014-05-25\" N=\" "
                    + "  35\" A=\"500.00\" PACC=\"KZ136011063100106750\" RACC=\"KZ246011063100106746\" "
                    + "GND=\"Внутрибанковский перевод на собственный счет клиента. НДС не облагается.\" "
                    + "PNAME=\"Саумбаев Ельдар Карияевич\" RNAME=\"Саумбаев Ельдар Карияевич\" PINN=\"850210301899\" "
                    + "OPERCODE=\"60070\"/></x:BSMessage>";
            
             xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><x:BSMessage xmlns:x=\"BS_R_CREDITPAY\" Version=\"STD1.0\" ID=\"2735382401895980\" ReqDateTime=\"2014-05-29T11:09:49.5980\">\n" +
"	<BSHead CustomerID=\"26991400001166\" SubSys=\"IC\" BranchID=\"\"/><BSRequest Acc=\"1324.1143075397\" RefTDN=\"\"></BSRequest>\n" +
"</x:BSMessage>";            
            
            
            String xsl="<?xml version=\"1.0\"?>\n" +
"<xsl:stylesheet xmlns:x=\"BS_R_CREDITPAY\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" +
"  <xsl:template match=\"/\" xmlns:utils=\"kz.bssproxy.extend.XSLUtils\">\n" +
"    <xsl:for-each select=\"x:BSMessage\">\n" +
                    "<test><xsl:value-of \n" +
"       select=\"utils:CurrCode2ISO('398')\" /></test>"+

"    </xsl:for-each>\n" +
"  </xsl:template>\n" +
"</xsl:stylesheet>";
            //ProcessParam p = new ProcessParam("ACCACC");
            
            String s = BSSTransformer.TransformToString(xml,
                                    xsl);  
            System.out.println(s);
            
            
        } catch (Exception ex) {
            Logger.getLogger(XSLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
