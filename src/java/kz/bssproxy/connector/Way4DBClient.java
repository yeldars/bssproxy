/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import kz.bssproxy.config.BSSProxyConfig;
import kz.bssproxy.extend.XSLUtils;
import kz.bssproxy.utils.CardUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ELDARS
 */
public class Way4DBClient {
    
    
        
public static Connection getDBConnection() {
 
		Connection dbConnection = null;
 
		try {
 
			Class.forName(BSSProxyConfig.getInstance().getW4DBDriver());
 
		} catch (ClassNotFoundException e) {
 
			System.out.println(e.getMessage());
 
		}
 
		try {
 
			dbConnection = DriverManager.getConnection(
                             BSSProxyConfig.getInstance().getW4JDBC(), BSSProxyConfig.getInstance().getW4User(),BSSProxyConfig.getInstance().getW4Password());
			return dbConnection;
 
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}
 
		return dbConnection;
 
	}

	public static String getCardIban(String cardnum) throws SQLException {
                
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
                
                
 
		//String selectSQL = "begin bwx.opt_spm.kansas_get_contracts(?,?); end;";
                String iban = getCardIbanDB(cardnum);
                
                String result =
                        "<x:BSMessage xmlns:x=\"BS_A_GETCARDIBAN\" Version=\"STD1.0\" ID=\"1234567\" AnsDateTime=\"2008-03-21T00:00:25.0830\">\n" +
                        "<BSHead CustomerID=\"2\" OutSysID=\"Invoretail\" ABSMessage=\"\">\n" +
                        "<Errors>\n" +
                        "<m l=\"0\" t=\"0\" s=\"0\" c=\"0\" e=\"\"/>\n" +
                        "</Errors>\n" +
                        "</BSHead>\n" +
                        "<BSAnswer Iban=\""+iban+"\" />" +
                        "</x:BSMessage>";
                return result;
        }
	public static String getCardList(String cardnum) throws SQLException {
                
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
                
                
 
		String selectSQL = "begin bwx.opt_spm.kansas_get_contracts(?,?); end;";
                
                String result =
                        "<x:BSMessage xmlns:x=\"BS_A_CARDLIST\" Version=\"STD1.0\" ID=\"1234567\" AnsDateTime=\"2008-03-21T00:00:25.0830\">\n" +
                        "<BSHead CustomerID=\"2\" OutSysID=\"Invoretail\" ABSMessage=\"\">\n" +
                        "<Errors>\n" +
                        "<m l=\"0\" t=\"0\" s=\"0\" c=\"0\" e=\"\"/>\n" +
                        "</Errors>\n" +
                        "</BSHead>\n" +
                        "<BSAnswer>" +
                        "<Cards>";
                
                if (cardnum != null)
 
		try {
			dbConnection = getDBConnection();
			CallableStatement c = dbConnection.prepareCall(selectSQL);
                        
                        c.setString(1, cardnum);
			c.registerOutParameter(2, oracle.jdbc.driver.OracleTypes.CURSOR);
                        c.execute();
                        ResultSet rs = (ResultSet) c.getObject(2);
                        
 
			// execute select SQL stetement
			//ResultSet rs = preparedStatement.executeQuery();
                        
                        String iban;
                        String ptrm;
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");
 
			while (rs.next()) {
                            
                            //ptrm = "1991-01-01";
                            try{
                            if (rs.getObject("DEADLINE")==null)
                                ptrm = "1990-01-01";
                            else
                                
                                ptrm = df.format(df2.parse(rs.getString("DEADLINE")));
                                 
                            }catch (Exception nulle)
                            {
                                 ptrm = "1990-01-01";
                            }
                                    
                            if (rs.getObject("RBS_NUMBER")==null)
                                iban = rs.getString("IBAN");
                            else
                                iban = rs.getString("RBS_NUMBER");
                            
//                            if 
////                                    (
////                                    (rs.getString("SUBTYPE").equals("EC/MC Standard HSBC (Казахстан)"))
////                                    ||
////                                    (rs.getString("SUBTYPE").equals("EC/MC Platinum HSBC (Казахстан)"))
////                                    ||
////                                    (rs.getString("SUBTYPE").equals("EC/MC Debit HSBC (Казахстан)"))
////                                    
////                                    )
//                                    (rs.getString("CONTRACT_NUMBER").length()==16)                                    
                                    
				result += "<N CN=\"" + rs.getString("Contract_Number") + "\""
                                        + " CNM=\"" + CardUtils.MaskCard(rs.getString("Contract_Number")) +"\""
                                        + " CA=\"" + iban +"\""
                                        + " CRI=\"" + rs.getString("CURRENCY_NAME") +"\""
                                        + " CRD=\"" + rs.getString("CURRENCY_CODE") +"\""
                                        + " STATUS=\""+((rs.getString("CONTRACT_NUMBER").length()==16)?"0":"1")+"\""
                                        + " FORM=\""+((rs.getString("IBAN").startsWith("KZ")==true)?"2":"1")+"\""
                                        + " TYPENAME=\"" + rs.getString("SUBTYPE") +"\""
                                        + " NNAME=\"" + rs.getString("CARD_NAME") +"\""
                                        + " ENAME=\"" + rs.getString("CARD_NAME") +"\""                                        
                                        + " EXP=\"" + rs.getString("CARD_EXPIRE") +"\""
                                        + " CBLNG=\"2\""
                                        + " CRLM=\"" + rs.getString("CREDITLIMIT") +"\""
                                        + " PTRM=\"" + ptrm +"\""                                        
                                        + " PC=\"\"" //Процентная ставка
                                        + " OPC=\"\"" //Процентная ставка за просроченную задолженность                                        
                                        + " TSM=\"" + rs.getString("TOTAL_DEBT") +"\"" //Сумма планового платежа 
                                        + " PRCA=\"\"" //Сумма начисленных процентов
                                        + " MNP=\"" + rs.getString("MIN_PAYMENT") +"\""  //Минимальная сумма погашения
                                        + " DBT=\"" + rs.getString("TOTAL_BALANCE") +"\"" //Сумма задолженности по основному долгу
                                        //+ " PRCP=\"\"" //Сумма планового платежа по процентам
                                        + " ODBT=\"" + rs.getString("OVERDUE_DEBT") +"\"" //Сумма просроченной задолженности
                                        + " PRCO=\"" + rs.getString("OVERDRAFT") +"\"" //Сумма просроченных процентов
                                        + " OFN=\"" + rs.getString("PENALTY") +"\""  //Пеня за несвоевременное исполнение обязательств
                                        + " FN=\"\"" //Штраф за факт образования просроченной задолженности
                                        + " R=\"" + rs.getString("AVAILABLE") +"\""
                                        + " BONUSR=\"" + rs.getString("NOMAD_BONUS") +"\""
                                        + ">"+""
                                        + "</N>"; 
			}

		} finally {
 
                       
			if (preparedStatement != null) {
				preparedStatement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
 
		}
                 result +="</Cards>\n" +
                    "</BSAnswer>\n" +
                    "</x:BSMessage>";
                 return result;
 
	}
 
        
public static double getCardBalance(String cardN) throws SQLException {
                
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
                double result  = 0;
                
                
 
		String selectSQL = "begin bwx.opt_spm.kansas_get_contracts(?,?); end;";
                
 
		try {
			dbConnection = getDBConnection();
			CallableStatement c = dbConnection.prepareCall(selectSQL);
                        
                        c.setString(1, cardN);
			c.registerOutParameter(2, oracle.jdbc.driver.OracleTypes.CURSOR);
                        c.execute();
                        ResultSet rs = (ResultSet) c.getObject(2);
                        
                        
			
                        while (rs.next()
                                ){
                         if (rs.getString("CONTRACT_NUMBER").equals(cardN))
                         {
                             result = rs.getDouble("AVAILABLE");
                             break;
                         }
                        }
                        
			}

		 finally {
 
                       
			if (preparedStatement != null) {
				preparedStatement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
 
		}
                
                 return result;
 
	}

        
public static String getCardIbanDB(String cardN) throws SQLException {
                
    
                
                if   (BSSProxyConfig.getInstance().getW4AllowCreditList().contains(cardN.substring(0, 5))){
                } else {
                    return  "";
                }
                
                String result = "";
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
                
                
 
		String selectSQL = "begin bwx.opt_spm.kansas_get_contracts(?,?); end;";
                
 
		try {
			dbConnection = getDBConnection();
			CallableStatement c = dbConnection.prepareCall(selectSQL);
                        
                        c.setString(1, cardN);
			c.registerOutParameter(2, oracle.jdbc.driver.OracleTypes.CURSOR);
                        c.execute();
                        ResultSet rs = (ResultSet) c.getObject(2);
                        
                        
			
                        while (rs.next()
                                ){
                            

                            
                         if (rs.getString("CONTRACT_NUMBER").equals(cardN))
                         {
                            if (rs.getObject("RBS_NUMBER")==null)
                                result = rs.getString("IBAN");
                            else
                              if (rs.getString("RBS_NUMBER").startsWith("KZ"))
                                  result = rs.getString("RBS_NUMBER");
                            else      
                                  result = rs.getString("IBAN");
                             break;
                         }
                        }
                        
			}

		 finally {
 
                       
			if (preparedStatement != null) {
				preparedStatement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
 
		}
                

                 return result;
 
	}    	      
        
	public static String getStatementByCard(String id,Document dom) throws SQLException,XPathExpressionException {

                    XPath xPath = XPathFactory.newInstance().newXPath();
                    XPathExpression xPathExpressionCards = xPath.compile("//BSMessage/BSRequest/Card");
                    Object ocards = xPathExpressionCards.evaluate(dom, XPathConstants.NODESET);
                    NodeList cards = (NodeList) ocards;
                    
                    XPathExpression xPathExpressionCustomerId = xPath.compile("//BSMessage/BSHead/@CustomerID");
                    String customerId = (String)xPathExpressionCustomerId.evaluate(dom, XPathConstants.STRING);                                        
                    
                    XPathExpression xPathExpressionBDate = xPath.compile("//BSMessage/BSRequest/@BDate");
                    String bDate = (String)xPathExpressionBDate.evaluate(dom, XPathConstants.STRING); 
                    
                    XPathExpression xPathExpressionEDate = xPath.compile("//BSMessage/BSRequest/@EDate");
                    String eDate = (String)xPathExpressionEDate.evaluate(dom, XPathConstants.STRING);                                        
                    
                    
		Connection dbConnection = null;
		CallableStatement preparedStatement = null;
                dbConnection = getDBConnection();
                
                              
 
		//String selectSQL = "select distinct x.*,TO_CHAR(x.trans_date,'DD.MM.RRRR') D,TO_CHAR(x.POSTING_DATE,'DD.MM.RRRR') PD  from BWX.OPT_IN_OUT_OPERATIONS@CRDPROD x where contract_number=? \n" +
                //"and trans_date between TO_DATE(?,'RRRR-MM-DD') and TO_DATE(?,'RRRR-MM-DD')";
                String selectSQL = "begin bwx.opt_spm.kansas_get_stmt (?,?,?,?); end; ";
                
                String result ="";
                String body = "";
                
                try{  
                    for (int i = 0; i < cards.getLength(); i++) {
                        Node n = cards.item(i);
                        if (n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                            Element product = (Element) n;
                            NodeList nodes = (NodeList)  xPathExpressionCards.evaluate(product,XPathConstants.NODESET); //Find the 'title' in the 'product'
                            System.out.println("TITLE: " + nodes.item(i).getAttributes().getNamedItem("N").getNodeValue()); // And here is the title 
                            
                            String cardN = nodes.item(i).getAttributes().getNamedItem("N").getNodeValue();
                            String cardCur = nodes.item(i).getAttributes().getNamedItem("CUR").getNodeValue();

                            
                        //start
                            

		try {
			
			CallableStatement c = dbConnection.prepareCall(selectSQL);
			c.setString(1, cardN);
                        c.setDate(2, Date.valueOf(bDate));
                        c.setDate(3, Date.valueOf(eDate));
			c.registerOutParameter(4, oracle.jdbc.driver.OracleTypes.CURSOR);
                        c.execute();
                        ResultSet rs = (ResultSet) c.getObject(4);
 
			// execute select SQL stetement
			//ResultSet rs = preparedStatement.executeQuery();
                        body = "";
 
			while (rs.next()) {
				body += "<D"
                                        + " D=\"" + rs.getString("POSTING_DATE") +"\""
                                        + " PD=\"" + rs.getString("POSTING_DATE") +"\""
                                        + " PA=\"" + rs.getString("AMOUNT") +"\""
                                        + " CUR=\""+XSLUtils.CurrCode2ISO(rs.getString("TRANS_CURRENCY"))+"\" \n"
                                        + " A=\"" + rs.getString("AMOUNT") +"\""
                                        + " REF=\"" + rs.getString("RET_REF_NUMBER") +"\""
                                        + " GND=\""+ XSLUtils.escapeHTML(rs.getString("STMT_DETAILS")) +"\" \n"
                                        + " PLC=\"\"/>";
                                        
                                        
			}

		} finally {
 
                       
			
 
		}
                 result +=
                         
                         "<Card \n" +
                        "N=\""+cardN+"\"\n" +
                        "NM=\""+CardUtils.MaskCard(cardN)+"\" \n" +
                        "BC=\""+cardN+"\"\n" +
                        "BCM=\""+CardUtils.MaskCard(cardN)+"\" \n" +
                        "Acc=\""+cardN+"\" \n" +
                        "CUR=\""+cardCur+"\" \n" +
                        "TYPE=\"VISA Classic, VISA\" \n" +
                        "HC=\"Y\"><Add CID=\"T3\" V=\""+Double.toString(getCardBalance(cardN))+"\" />"+
                         body+
                         "</Card>";
                 
                        //end
                                    
                            
                        }
                    } 
                    
//                   XPathExpression xPathExpression = xPath.compile("//BSMessage/BSRequest/Card/@N");
//                   String cardN = (String)xPathExpression.evaluate(dom, XPathConstants.STRING);                    
//                    
//                    
//                    
//                    
//                    
//                    
//                    XPathExpression xPathExpressionCur = xPath.compile("//BSMessage/BSRequest/Card/@CUR");
//                    String cardCur = (String)xPathExpressionCur.evaluate(dom, XPathConstants.STRING);                    
//                    

                    
                    

                
                result =
                        "<x:BSMessage xmlns:x=\"BS_A_CARDSTM\" Version=\"STD1.0\" ID=\""+id+"\" AnsDateTime=\"2008-03-21T00:00:25.0830\">\n" +
                        "<BSHead CustomerID=\""+customerId+"\" OutSysID=\"OW\" ABSMessage=\"\">\n" +
                        "<Errors>\n" +
                        "<m l=\"0\" t=\"0\" s=\"0\" c=\"0\" e=\"\"/>\n" +
                        "</Errors>\n" +
                        "</BSHead>\n" +
                        "<BSAnswer \n" +
                        "BDate=\""+bDate+"\" \n" +
                        "EDate=\""+eDate+"\">\n" +
                        "Format=\"2\">\n" +
                        "<TRES>\n" +
                        "<T CID=\"T1\" \n" +
                        "T=\"Остаток собственных средств на начало периода\"/>\n" +
                        "<T CID=\"T2\" \n" +
                        "T=\"Остаток собственных средств на конец периода\"/>\n" +
                        "<T CID=\"T3\" \n" +
                        "T=\"Остаток собственных средств на текущую дату\"/>\n" +
                        "<T CID=\"T4\" \n" +
                        "T=\"Доступный лимит кредита\"/>\n" +
                        "<T CID=\"T5\" \n" +
                        "T=\"Текущий платежный лимит\"/>\n" +
                        "</TRES>"+result+"</BSAnswer></x:BSMessage>";
 
            }
            finally{
                if (preparedStatement != null) {
				preparedStatement.close();
			}
 
			if (dbConnection != null) {
				dbConnection.close();
			}
            }
                 return result;
 
	}        
}
