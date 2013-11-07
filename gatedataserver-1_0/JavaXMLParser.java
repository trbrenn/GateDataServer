/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author trbrenn
 */
public class JavaXMLParser {
     private int     msgType;  
     private int     msgLength;
     private int     msgPacketID;
     private String  MsgData;
     private int     msgOffset;
     //private byte[]  receiveDataRaw;
     private String  msgTimeStamp;
     
     public JavaXMLParser(){
     } 
     
     public JavaXMLParser(String xml){
         this.parseXML(xml);
     }
     
     public void parseXML(String xml) {
         try {
            Document doc = loadXMLFromString(xml);
            doc.getDocumentElement().normalize();

            //System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());
            NodeList nodes = doc.getElementsByTagName("ZigBeeMsg");
            //System.out.println("==========================");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    msgTimeStamp = getValue("TimeStamp", element);
                    msgType = Integer.parseInt(getValue("MsgType", element), 16);
                    msgLength = Integer.parseInt(getValue("Length", element),16);
                    msgPacketID = Integer.parseInt(getValue("PacketID", element),16);
                    MsgData = getValue("MsgData", element);
                    msgOffset = Integer.parseInt(getValue("Offset", element),16);
                }
            }
    } 
        catch (Exception ex) {
            System.out.println("Error: "+ex);
        }
    }
         
    private String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }

    private Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
    
    public int getMsgType() {
        return msgType;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public int getMsgPacketID() {
        return msgPacketID;
    }

    public String getMsgData() {
        return MsgData;
    }

    public int getMsgOffset() {
        return msgOffset;
    }

//    public byte[] getReceiveDataRaw() {
//        return receiveDataRaw;
//    }

    public String getMsgTimeStamp() {
        return msgTimeStamp;
    }
    
}
