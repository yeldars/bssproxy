/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import kz.bssproxy.connector.CBSConnector;


/**
 *
 * @author BSS
 */
public class CBSProcess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException{
        // TODO code application logic here
        
        CBSProcess p = new CBSProcess("","");
        System.out.println(p.getResponse());
    }
    private String response;

    public String getResponse() {
        return response;
    }
    
    //Выписка по счету
    public CBSProcess(String frmt, String xml)throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "begin colvir.z_200_pkg_ifiz_integr.processrequest(?,?,?); end;";
                
		try {
			dbConnection = CBSConnector.getDBConnection();
                        
                        CallableStatement procout = dbConnection.prepareCall (selectSQL);
                        procout.setString(1, frmt);
                        procout.setString(2, xml);
                        procout.registerOutParameter (3, Types.CHAR);
                        procout.execute ();
                        response = procout.getString (3);
                        //System.out.println ("Out argument is: " + procout.getString (1));
                        procout.close();                        
//			preparedStatement = dbConnection.prepareStatement(selectSQL);
//			preparedStatement.setString(2, frmt);
//                        preparedStatement.setString(3, xml);
//			ResultSet rs = preparedStatement.executeQuery();
//                        //rs.next();
//                        response = rs.getString(1);
                        

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
