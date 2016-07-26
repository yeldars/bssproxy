/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author BSS
 */
@WebService(serviceName = "O_BS_EXCHANGE", targetNamespace="http://MM_BS-ESB-PC/I_BS")
public class O_BS_EXCHANGE {

    /**
     * This is a sample web service operation
     * @param xml
     * @return 
     */
    @WebMethod(operationName = "O_BS_EXCHANGE")
    public String hello(@WebParam(name = "OP_BS_Request") String xml) {        
        return ProcessRequest.Process(xml);
    }
}
