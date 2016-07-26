/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author ELDARS
 */
public class LogTest {

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) throws FileNotFoundException, IOException {

    
    Logger log =Logger.getLogger("logfile");
        
                String nameFile = "c:\\logs\\1.txt";
                PropertyConfigurator.configure(nameFile);

                Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
                
                log.debug("---------BEGIN----------");
                log.debug("system=");
                log.debug("data=");
                log.debug("ref=");
                log.debug("frmt=");
                log.debug("---------END----------");
}
    
}
