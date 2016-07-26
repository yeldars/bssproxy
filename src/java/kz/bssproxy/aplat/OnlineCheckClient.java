/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.aplat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import kz.bssproxy.config.BSSProxyConfig;
import static kz.bssproxy.utils.DBLogger.InsertXMLLog;
import kz.bssproxy.utils.MyKazWin;

/**
 *
 * @author ELDARS
 */
public class OnlineCheckClient {
    
    
    private String response;

    public String getResponse() {
        return response;
    }
    
    
    public OnlineCheckClient(String capReq) throws IOException{
        this.response = "";
        InsertXMLLog( capReq,"0","APLATONLINECHECK_REQ","APLAT",1);          

         
         URL url = new URL(BSSProxyConfig.getInstance().getAplatOnlineCheckUrl());            
         HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
         urlConn.setDoInput (true);
         urlConn.setConnectTimeout(30000);         
         // Let the RTS know that we want to do output.
         urlConn.setDoOutput (true);
         // No caching, we want the real thing.
         urlConn.setUseCaches (false);
         urlConn.setRequestMethod("POST");         
         urlConn.connect();
         //DataOutputStream output = null;

        
         DataOutputStream output;
         output = new DataOutputStream(urlConn.getOutputStream());
         output.write(capReq.getBytes("UTF-8"));
         output.flush();
         output.close();
         
         
         //DataInputStream input = new DataInputStream (urlConn.getInputStream());
        
        InputStream in;
        

        in = urlConn.getInputStream();        

        
        String encoding = urlConn.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
                
        //byte[] buffer = new byte[urlConn.getContentLength()];
        //int bytesRead = in.read(buffer);
        //String page = new String(buffer, 0, bytesRead, "UTF-8");                
         
        
         
         
    int bytesRead;
    
    byte[] buffer = new byte[8192];
    
    StringBuilder sb = new StringBuilder();       
    while((bytesRead = in.read(buffer)) != -1) { // InputStream.read() returns -1 at EOF
        sb.append(new String(buffer,0,bytesRead,"UTF-8"));
        buffer = new byte[8192];
        bytesRead=0;
    }
    this.response = MyKazWin.UTF8To1048Convert(sb.toString());    
    
    InsertXMLLog( this.response,"0","APLATONLINECHECK_RES","APLAT",1);
    
    
    }
}
