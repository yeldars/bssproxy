/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.connector;

/**
 *
 * @author BSS
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import kz.bssproxy.config.BSSProxyConfig;
 
public class CBSConnector {
 
	public static void main(String[] argv) {
 
 
	}
 

        
	public static Connection getDBConnection() {
 
		Connection dbConnection = null;
 
		try {
 
			Class.forName(BSSProxyConfig.getInstance().getCbsDBDriver());
 
		} catch (ClassNotFoundException e) {
 
			System.out.println(e.getMessage());
 
		}
 
		try {
        		dbConnection = DriverManager.getConnection(
                             BSSProxyConfig.getInstance().getCbsJDBC(), 
                                BSSProxyConfig.getInstance().getCbsUser(),
                                BSSProxyConfig.getInstance().getCbsPassword());                            
//			dbConnection = DriverManager.getConnection(
//                             DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;
 
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}
 
		return dbConnection;
 
	}
        
   
 
}