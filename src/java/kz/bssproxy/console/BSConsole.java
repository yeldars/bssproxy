/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.bssproxy.console;

/**
 *
 * @author 00009314
 */
public class BSConsole {

    
    private static final String outPath = "C:\\Retail 2.5\\SUBSYS\\XML\\OUT\\";
    private static final String inPath = "C:\\Retail 2.5\\SUBSYS\\XML\\IN\\";
    private static final String ArcPath = "C:\\Retail 2.5\\SUBSYS\\XML\\ARC\\";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        
        String i = "Иванов Иван Иванович";
        //i = i.replace("И", "<<KOSYAK_SYS_I>>");
        String s =(new String(i.getBytes(),"CP1251"));
        System.out.println(s);
        s =(new String(s.getBytes("CP1251"),"UTF-8"));
        s = s.replace("�?","И");
        System.out.println(s);
//        System.out.println((byte)s.charAt(0));
//        System.out.println((byte)s.charAt(1));
//        int i;
//        byte[] b1 = new byte[2];        
//        b1[0]=(byte) 208;
//        b1[1]=(byte) 152;
//        String s1= new String(b1,"UTF-8");
//        
//        byte[] b2 = new byte[2];        
//        b2[0]=(byte) 32;
//        b2[1]=(byte) -3;        
//        String s2= new String(b2,"UTF-8");
//        System.out.println(s2);
//        s = s.replace(s1, s2);
        
        
        //s=  new String((byte) 208);
        //208/152
        //System.out.println(new String(s.getBytes("CP1251"),"UTF-8"));
//        while (true){
//        File outFiles = new File(outPath);
//        
//        
//        String AnswerBSS = "";
//        String w4Answer = "";
//        String capAnswer = "";
//        ArrayList<File> arrOutFiles = new ArrayList<>(Arrays.asList(outFiles.listFiles()));
//            for (File item : arrOutFiles) {
//                
//                ProcessParam p = new ProcessParam(item.getName());
//                if (!p.isOk()) 
//                    continue;
//                
//
//                        String xml = new String(Files.readAllBytes(item.getAbsoluteFile().toPath()));
//                        //System.out.println(item.getName());
//                        Document dom;
//                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//                        DocumentBuilder db = dbf.newDocumentBuilder();
//                        dom = db.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
//                        Element doc = dom.getDocumentElement();
//                        String ID = doc.getAttribute("ID");
//                        String s = "";
//                        if (!p.getRequestXSL().isEmpty())
//                            s = TransformToString(item.getAbsoluteFile().toString(),
//                                    p.getRequestXSL());
//                        
//                        //System.out.println(s);
//                        
//                        //CAP ответил, все, удаляем файл. Лучше перед
//                        if (!item.renameTo(new File(ArcPath+"REQ_"+item.getName())))
//                            throw new IOException("Fail to Rename "+item.getName()+" to "+ArcPath+"REQ_"+item.getName());
//                        
//                        
//                        if (p.getPrefix().equals("CARDLIST"))
//                        {
//                            AnswerBSS = getCardBalanceByIIN(ID,"850811302011");
//                            System.out.println("BSS Answer CARDLIST" +AnswerBSS);
//                        }
//                        else 
//                            if (p.getPrefix().equals("CARDACC"))  {
//                                
//                            w4Answer = Way4Client.SendWay4Request(s);
//                            System.out.println("Way4 Req" +s);
//                            System.out.println("Way4 Answer" +w4Answer);
//                            
//                            AnswerBSS = TransformToString2(w4Answer,
//                                    p.getAnswerXSL());     
//                            System.out.println("BSS Answer" +AnswerBSS);
//                            
//                            } 
//                            
//                        else 
//                            if (p.getPrefix().equals("CARDSTM"))  {
//                                
//                                
//                        //XPathExpression xp = XPathFactory.newInstance().newXPath().compile("//x:BSMessage/BSRequest/Card/[@N]");
//                        //Node n = ;
//                        XPath xPath = XPathFactory.newInstance().newXPath();
//                        XPathExpression xPathExpression = xPath.compile("//BSMessage/BSRequest/Card/@N");
//                        String cardN = (String)xPathExpression.evaluate(dom, XPathConstants.STRING);                                
//                        //String cardN = doc.getFirstChild().getNodeName();
//                        //String cardN = doc.getAttribute("BSRequest/Card/N");    
//                        //System.err.println("Card = "+cardN);
//                        AnswerBSS = getStatementByCard(ID,cardN);  
//                        System.out.println("BSS Answer CARDSTM = "+AnswerBSS);
//                            }                             
//                                
//                        else
//                        {
//                            capAnswer= SendCapRequest(s);                            
//                            AnswerBSS = TransformToString2(capAnswer,
//                                    p.getAnswerXSL()); 
//                            System.out.println("BSS Answer  else " +AnswerBSS);
//                        }
//                           
//
//                        
//                        
//                        
//                        OutputStream os = new FileOutputStream(inPath+p.getPrefix()+ID+".log");
//                        os.write(AnswerBSS.getBytes("Cp1251"));
//                        os.close();
//                        
//                        //break;
//                    }
//                  
//
//        try{
//            Thread.sleep(2000);
//        }
//        catch(InterruptedException exc)
//        {
//        }
//        }//WHILE TruE
        
        //System.out.println("VATA EMES!");        
    }
    
}
