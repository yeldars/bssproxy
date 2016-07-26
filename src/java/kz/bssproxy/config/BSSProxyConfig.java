/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author BSS
 */
public class BSSProxyConfig {
    
    private static BSSProxyConfig instance;
    private String aplatHost;
    private String aplatPort;
    private String aplatProtocol;
    private String aplatUrl;    
    private String aplatTumarProfile;    
    private String aplatTumarProfilePassword;    
    private String aplatTumarSerial;    
    private String aplatTerminal;    
    private String aplatTerminalUser;    
    private String aplatTerminalPassword;  
    private Logger log =Logger.getLogger("logfile");
    
    
    
    
    
    private final String fileName;
    private String cbsDBDriver;
    private String cbsJDBC;
    private String cbsUser;
    private String cbsPassword;
    private String w4DBDriver;
    private String w4JDBC;
    private String w4User;
    private String w4Password;
    private String w4ProcGetContract;
    private String w4ProcGetStmt;
    private String w4AllowCreditList;
    private String capUrl;
    private String aplatOnlineCheckUrl;
    private String cbsFormats;
    private String capFormats;
    Map<String, String> xslMap;
    private String xslPath;
    
    private String ANSWER_PREF="BS_A_";
    private String REQUEST_PREF="BS_R_";

    public String getANSWER_PREF() {
        return ANSWER_PREF;
    }

    public String getAplatOnlineCheckUrl() {
        return aplatOnlineCheckUrl;
    }

    
    public void setAplatOnlineCheckUrl(String aplatOnlineCheckUrl) {
        this.aplatOnlineCheckUrl = aplatOnlineCheckUrl;
    }
    
    
    
    public String getXSLRequest(String bsRA)
    {
        return  xslMap.get(REQUEST_PREF+bsRA);
    }
    
    public String getXSLAnswer(String bsRA)
    {
        return  xslMap.get(ANSWER_PREF+bsRA);
    }    
    
    private String readXSLFile(String filename) {
            File f = new File(filename);
            try {
                byte[] bytes = Files.readAllBytes(f.toPath());
                return new String(bytes,"UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
    }    
    private void readXSL(){
        xslMap = new HashMap<>();
        
         
        String[] s = this.capFormats.split(",");
        for (int i=0;i<s.length;i++)
        {
           xslMap.put(REQUEST_PREF+s[i], readXSLFile(this.xslPath+REQUEST_PREF+s[i]+".xml"));
           xslMap.put(ANSWER_PREF+s[i], readXSLFile(this.xslPath+ANSWER_PREF+s[i]+".xml"));
        }
        
    }
    public boolean isCbsFormat(String aFormat ){
        //System.out.println(this.cbsFormats);
        return (this.cbsFormats.replace(" ","")+',').contains(aFormat+',');
    }
    
    public boolean isCapFormat(String aFormat ){
        return (this.capFormats.replace(" ","")+',').contains(aFormat+',');
    }    

    public String getFileName() {
        return fileName;
    }    

    public String getAplatHost() {
        return aplatHost;
    }

    public String getAplatPort() {
        return aplatPort;
    }

    public String getAplatProtocol() {
        return aplatProtocol;
    }

    public String getAplatUrl() {
        return aplatUrl;
    }

    public String getAplatTumarProfile() {
        return aplatTumarProfile;
    }

    public String getAplatTumarProfilePassword() {
        return aplatTumarProfilePassword;
    }

    public String getAplatTumarSerial() {
        return aplatTumarSerial;
    }

    public String getAplatTerminal() {
        return aplatTerminal;
    }

    public String getAplatTerminalUser() {
        return aplatTerminalUser;
    }

    public String getAplatTerminalPassword() {
        return aplatTerminalPassword;
    }

    public String getCbsDBDriver() {
        return cbsDBDriver;
    }
    
    
           
    public String getCbsJDBC() {
        return cbsJDBC;
    }

    public String getCbsPassword() {
        return cbsPassword;
    }

    public String getCbsUser() {
        return cbsUser;
    }

    public String getW4DBDriver() {
        return w4DBDriver;
    }

    
    
    public String getW4JDBC() {
        return w4JDBC;
    }

    public String getW4User() {
        return w4User;
    }

    public String getW4Password() {
        return w4Password;
    }

    public String getW4ProcGetContract() {
        return w4ProcGetContract;
    }

    public String getW4ProcGetStmt() {
        return w4ProcGetStmt;
    }

    public String getW4AllowCreditList() {
        return w4AllowCreditList;
    }

    
    public String getCapUrl() {
        return capUrl;
    }
    
    
    
    public BSSProxyConfig(String afileName){
        
        
//        if (System.getProperty("kz.bssproxy.configfile")==null)
//            this.fileName="C:\\Projects\\BSSPROXY\\bssproxy.conf";
//        else
        this.fileName=afileName;//System.getProperty("kz.bssproxy.configfile");
        
        //Security.addProvider(new GammaTechProvider());
                
        Properties prop = new Properties();
        try
        {
            
        
        
        prop.load(new FileInputStream(this.fileName));
        this.aplatHost        = prop.getProperty("aplathost");
        this.aplatPort       = prop.getProperty("aplatport");
        this.aplatProtocol    = prop.getProperty("aplatprotocol");   
        this.aplatUrl = this.aplatProtocol+"://"+this.aplatHost+":"+aplatPort;
        this.aplatOnlineCheckUrl = prop.getProperty("aplatonlinecheckurl"); 
        
        this.aplatTumarProfile = prop.getProperty("aplattumarprofile");   
        this.aplatTumarProfilePassword = prop.getProperty("aplattumarprofilepassword");   
        this.aplatTumarSerial = prop.getProperty("aplattumarserial");   
        this.aplatTerminal = prop.getProperty("aplatterminal");   
        this.aplatTerminalUser = prop.getProperty("aplatterminaluser");   
        this.aplatTerminalPassword = prop.getProperty("aplatterminalpassword");   

    
        this.cbsDBDriver      = prop.getProperty("cbsdbdriver");
        this.cbsJDBC          = prop.getProperty("cbsjdbc");
        this.cbsUser          = prop.getProperty("cbsuser");
        this.cbsPassword      = prop.getProperty("cbspassword");
        this.w4DBDriver       = prop.getProperty("w4dbdriver");
        this.w4JDBC           = prop.getProperty("w4jdbc");
        this.w4User           = prop.getProperty("w4user");
        this.w4Password       = prop.getProperty("w4password");
        this.w4ProcGetContract= prop.getProperty("w4procgetcontract");
        this.w4ProcGetStmt    = prop.getProperty("w4procgetstmt");
        this.w4AllowCreditList    = prop.getProperty("w4allowcreditlist");//532456,542364,543271,520376,515453,536532,517327,547414
        this.capUrl           = prop.getProperty("capurl");
        this.capFormats       = prop.getProperty("capformats");
        this.cbsFormats       = prop.getProperty("cbsformats");
        this.xslPath       = prop.getProperty("xslpath");
        readXSL();
        
        
        try{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties p = new Properties();
        p.load(classLoader.getResourceAsStream("/log4j.properties"));
        PropertyConfigurator.configure(p);        
        }
        catch (Exception ex)//Пофиг
        {
            ex.printStackTrace();
        }
        
      
        }
        catch (IOException ex)
                {
                    ex.printStackTrace();
                }
        
    }
    
    
 public static synchronized BSSProxyConfig getInstance() {
        if (instance == null) {
            instance = new BSSProxyConfig(System.getProperty("kz.bssproxy.configfile"));
        }
        return instance;
    }    
 
 public static synchronized BSSProxyConfig getInstance(String fileName) {
        if (instance == null) {
            instance = new BSSProxyConfig(fileName);
        }
        return instance;
    }     
 
    public void log(String data,String ref,String frmt,String system, int ok){  	

        
        try{
            log.debug("---------BEGIN----------");
            log.debug("frmt="+frmt);                
            log.debug("ref="+ref);                
            log.debug("system="+system);
            log.debug("data=");
            log.debug(data);
            log.debug("---------END----------");
                
        }
        catch(Exception e)
        {
            //
            e.printStackTrace();
        }
    }
}
