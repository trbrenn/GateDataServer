/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

/**
 *
 * @author trbrenn
 */
public class SQLGenerator {

    public SQLGenerator(){
        
    }
        public String insertZigBeeMsg(JavaXMLParser jXMLParser){
        String   sqlStr = "INSERT INTO MessageData (TimeStamp, Length, MsgType, PacketID, MsgData, Offset) VALUES (\"";        
        sqlStr = sqlStr + jXMLParser.getMsgTimeStamp()+"\", ";
        sqlStr = sqlStr + jXMLParser.getMsgLength()+", ";
        sqlStr = sqlStr + jXMLParser.getMsgType()+", ";
        sqlStr = sqlStr + jXMLParser.getMsgPacketID()+", \"";
        sqlStr = sqlStr + jXMLParser.getMsgData()+"\", ";
        sqlStr = sqlStr + jXMLParser.getMsgOffset()+")";
        
        return sqlStr;       
    }
    
    public String insertGateData(GateData gd){
        String   sqlStr = "INSERT INTO GateData (TimeStamp, ACVolts, SolarVolts, Temp, Open) VALUES (\"";
        sqlStr = sqlStr + gd.getTimeStamp() + "\", ";
        sqlStr = sqlStr + gd.getAcVolts() + ", ";
        sqlStr = sqlStr + gd.getSolarVolts() + ", ";
        sqlStr = sqlStr + gd.getTemp() + ", ";
        if (gd.isOpen()) {
            sqlStr = sqlStr + "1)";
        }
        else {
            sqlStr = sqlStr + "0)";    
        }
        
        return sqlStr;
    }
}
