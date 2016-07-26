/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kz.bssproxy.connector.CBSConnector;


/**
 *
 * @author BSS
 */
public class Stm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException{
        // TODO code application logic here
        
        Stm p = new Stm("");
        System.out.println(p.getResponse());
    }
    private String response;

    public String getResponse() {
        return response;
    }
    
    //Выписка по счету
    public Stm(String xml)throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "select colvir.z_200_pkg_ifiz_integr.stm(?) result from dual";
                
		try {
			dbConnection = CBSConnector.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setString(1, xml);
			ResultSet rs = preparedStatement.executeQuery();
                        rs.next();
                        response = rs.getString("result");

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
    
}
