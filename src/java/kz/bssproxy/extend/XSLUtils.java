/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.extend;

import kz.bssproxy.utils.CapAccInfo;

/**
 *
 * @author BSS
 */
public class XSLUtils{
    
    
public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}

    public static boolean accExists(String acc) throws Exception
    {
        return new CapAccInfo(acc).isExists();
    }
    public static final String CurrCode2ISO(String e)
    {
        if (e==null) return "KZT";
        
        switch (e) {
            case "124": return	"CAD";
            case "756": return	"CHF";
            case "156": return	"CNY";
            case "978": return	"EUR";
            case "826": return	"GBP";
            case "344": return	"HKD";
            case "392": return	"JPY";
            case "398": return	"KZT";
            case "643": return	"RUB";
            case "840": return	"USD";
            default: return "KZT";
        }
    }
    
    public static final String CurrISO2Code(String e)
    {
        return "398";
    }
    
    
public static final String escapeHTML(String s){
   StringBuilder sb = new StringBuilder();
   int n = s.length();
   for (int i = 0; i < n; i++) {
      char c = s.charAt(i);
      switch (c) {
         case '<': sb.append("&lt;"); break;
         case '>': sb.append("&gt;"); break;
         case '&': sb.append("&amp;"); break;
         case '"': sb.append("&quot;"); break;
         case 'à': sb.append("&agrave;");break;
         case 'À': sb.append("&Agrave;");break;
         case 'â': sb.append("&acirc;");break;
         case 'Â': sb.append("&Acirc;");break;
         case 'ä': sb.append("&auml;");break;
         case 'Ä': sb.append("&Auml;");break;
         case 'å': sb.append("&aring;");break;
         case 'Å': sb.append("&Aring;");break;
         case 'æ': sb.append("&aelig;");break;
         case 'Æ': sb.append("&AElig;");break;
         case 'ç': sb.append("&ccedil;");break;
         case 'Ç': sb.append("&Ccedil;");break;
         case 'é': sb.append("&eacute;");break;
         case 'É': sb.append("&Eacute;");break;
         case 'è': sb.append("&egrave;");break;
         case 'È': sb.append("&Egrave;");break;
         case 'ê': sb.append("&ecirc;");break;
         case 'Ê': sb.append("&Ecirc;");break;
         case 'ë': sb.append("&euml;");break;
         case 'Ë': sb.append("&Euml;");break;
         case 'ï': sb.append("&iuml;");break;
         case 'Ï': sb.append("&Iuml;");break;
         case 'ô': sb.append("&ocirc;");break;
         case 'Ô': sb.append("&Ocirc;");break;
         case 'ö': sb.append("&ouml;");break;
         case 'Ö': sb.append("&Ouml;");break;
         case 'ø': sb.append("&oslash;");break;
         case 'Ø': sb.append("&Oslash;");break;
         case 'ß': sb.append("&szlig;");break;
         case 'ù': sb.append("&ugrave;");break;
         case 'Ù': sb.append("&Ugrave;");break;         
         case 'û': sb.append("&ucirc;");break;         
         case 'Û': sb.append("&Ucirc;");break;
         case 'ü': sb.append("&uuml;");break;
         case 'Ü': sb.append("&Uuml;");break;
         case '®': sb.append("&reg;");break;         
         case '©': sb.append("&copy;");break;   
         case '€': sb.append("&euro;"); break;
         // be carefull with this one (non-breaking whitee space)
         
         default:  sb.append(c); break;
      }
   }
   return sb.toString();
}

}
