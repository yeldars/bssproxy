/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import kz.bssproxy.config.BSSProxyConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author BSS
 */
public class DBLogger {
 
    
static Logger log =Logger.getLogger("logfile");

    public static void InsertXMLLog(String data,String ref,String frmt,String system, int ok){  	

        BSSProxyConfig.getInstance().log(data, ref, frmt, system, ok);
//        
//        try{
//                //String nameFile = "WEB-INF/log4j.properties";
//                
//                
//                
//                ;
//                
//                //log.getClassLoader().getResourceAsStream("/WEB-INF/classes/auth.properties")
//                        
//                        
//                //System.err.println("dir="+log.getClass().getResource("/").getPath().toString());
//                //String nameFile = "c:\\logs\\1.txt";
//                
//                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//                Properties p = new Properties();
//                p.load(classLoader.getResourceAsStream("/log4j.properties"));
//                PropertyConfigurator.configure(p);
//
////                Connection dbConnection = null;
////		PreparedStatement preparedStatement = null;
//                
//                log.debug("---------BEGIN----------");
//                log.debug("frmt="+frmt);                
//                log.debug("ref="+ref);                
//                log.debug("system="+system);
//                log.debug("data=");
//                log.debug(data);
//                log.debug("---------END----------");
//                
////                String selectSQL = "insert into z_s03_bss_log (data,ref,frmt,system,ok) values(?,?,?,?,?)";                
//// 
////		try {
////                        
////			dbConnection =  BSSProxyConnection.getDBConnection();
////			preparedStatement = dbConnection.prepareStatement(selectSQL);
////                        java.sql.Clob clob = 
////                            oracle.sql.CLOB.createTemporary(
////                              dbConnection, false, oracle.sql.CLOB.DURATION_SESSION);
////                        clob.setString(1, data);
////                        preparedStatement.setClob(1, clob);                        
////                        preparedStatement.setString(2, ref);                        
////                        preparedStatement.setString(3, frmt);                        
////                        preparedStatement.setString(4, system);                        
////                        preparedStatement.setInt(5, ok);
////                        
////			preparedStatement.execute(); 
////
////                        
////
////                        
////
////
////		}
////                catch (SQLException eee)
////                {
////                    eee.printStackTrace();
////                }
////                finally {
//// 
////                       
////			if (preparedStatement != null) {
////				preparedStatement.close();
////			}
//// 
////			if (dbConnection != null) {
////				dbConnection.close();
////			}
//// 
////		}
//                 
// 
//        }
//        catch(Exception e)
//        {
//            System.err.println("vata="+e.getMessage());
//            e.printStackTrace();
//        }
	}    
}
