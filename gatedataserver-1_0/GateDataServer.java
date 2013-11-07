/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trbrenn
 */
public class GateDataServer {
    private ServerThread      sThread;
    private JavaXMLParser     jXMLParser;
    private GateData          gateDataMsg;
    private SQLGenerator      sqlGen;
    private DataBaseConnData  DBCD;
    private DataBaseConnection DBC;
//    private LogFileGenerator logGen;
//    private LogFileGenerator errGen;
    private int               port = 1088;
    private int               cnt = 0;
    
    public GateDataServer(){
        sThread = new ServerThread(this, port, cnt);
        sThread.startServer();
        jXMLParser = new JavaXMLParser();
        gateDataMsg = new GateData();
        sqlGen = new SQLGenerator();
        DBCD = new DataBaseConnData();
        DBC = new DataBaseConnection(DBCD);
        //logGen = new LogFileGenerator("logfile.txt", "C:\\Users\\trbrenn\\Desktop");
        //logGen.open();
        //errGen = new LogFileGenerator("errfile.txt", "C:\\Users\\trbrenn\\Desktop");
        //errGen.open();

    }
    
    void processMsg(String msg){
        String xbeeSQL;
        String gateSQL;
        
        jXMLParser.parseXML(msg);
        xbeeSQL = sqlGen.insertZigBeeMsg(jXMLParser);
        this.processText(xbeeSQL);
        try {
            DBC.connect(); //Connect to the database.
            //insert the message to the database.
            DBC.executeInsert(xbeeSQL);
        } catch (Exception ex) {
            System.err.println("Error: "+ex);
        }
        
        String tempMsg = jXMLParser.getMsgData();
        if((gateDataMsg.isGataData(tempMsg)) && (jXMLParser.getMsgType() == 0x90)) {
            gateDataMsg.processGateData(tempMsg);
            gateSQL = sqlGen.insertGateData(gateDataMsg);
            this.processText(gateSQL);
            this.processText(gateDataMsg.toString());
            //insert gate data into the database.
            try {
                //insert the message to the database.
                DBC.executeInsert(gateSQL);
            } catch (Exception ex) {
                System.err.println("Error: "+ex);
            } 
        }
        
        try {
            DBC.close();
        }
        catch(Exception e){
            System.err.println("Could not connect to the database. "+e );
        }
    }
    
    void processText(String txt){
        System.out.println(txt);
        //logGen.writeln(txt);
    }
    
    void processError(String err){
        System.err.println(err);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GateDataServer gds = new GateDataServer();// TODO code application logic here
    }

}
