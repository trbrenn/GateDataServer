/*
 * This function send a message in XBEE message format to the server so it 
 * can be transmitted on the XBEE network.
 */
package gatedataserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author trbrenn
 * @date 07-05-2013
 */

public class SendRawMessage {
    private Socket socket;
    private byte message[];
    private int start;
    private int len;
    private String ipAddress = "192.168.21.72";
    private int port = 1089;

    public SendRawMessage(byte[] myByteArray) {
        message = myByteArray;
        start = 0;
        len = myByteArray.length;
    }
    
    public SendRawMessage() {
        message = null;
        start = 0;
        len = 0;
    }

    public SendRawMessage(byte[] myByteArray, int st, int l) {
        message = myByteArray;
        start = st;
        len = l;    
    } 
    
    public SendRawMessage(byte[] myByteArray, String ipa) {
        message = myByteArray;
        start = 0;
        len = myByteArray.length;
        ipAddress = ipa;
    }

    public SendRawMessage(byte[] myByteArray, String ipa, int pt) {
        message = myByteArray;
        start = 0;
        len = myByteArray.length;
        ipAddress = ipa;
        port = pt;
    }

    public SendRawMessage(byte[] myByteArray, int st, int l, String ipa) {
        message = myByteArray;
        start = st;
        len = l;    
        ipAddress = ipa;
    } 
    
    public SendRawMessage(byte[] myByteArray, int st, int l, String ipa, int pt) {
        message = myByteArray;
        start = st;
        len = l;    
        ipAddress = ipa;
        port = pt;
    } 
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void SendData() {
        try{
            if (len < 0)
                throw new IllegalArgumentException("Negative length not allowed");
            if (start < 0 || start >= message.length)
                throw new IndexOutOfBoundsException("Out of bounds: " + start);
            // Other checks if needed.

            socket = new Socket(ipAddress, port);
            try (OutputStream out = socket.getOutputStream()) {
                out.write(message, start, len);
            }
            socket.close();
        }
        catch(IOException e){
            System.err.println("Network Error: " + e);
        }
    }   
    
    public void SendData(byte[] msg) {
        message = msg;
        start = 0;
        len = message.length;
        
        try{
            if (len < 0)
                throw new IllegalArgumentException("Negative length not allowed");
            if (start < 0 || start >= message.length)
                throw new IndexOutOfBoundsException("Out of bounds: " + start);
            // Other checks if needed.

            socket = new Socket(ipAddress, port);
            OutputStream out = socket.getOutputStream();
            out.write(message, start, len);
            out.close();
            socket.close();
        }
        catch(IOException e){
            System.err.println("Network Error: " + e);
        }
    }   
    
    public static void main(String ARGV[]){
        //Send "R" to the gate so it will report.
        //byte[] msg = new byte[] {(byte)0x7E, (byte)0x00, (byte)0x0F, (byte)0x10, (byte)0x10, (byte)0x00, (byte)0x13, (byte)0xA2, (byte)0x00, (byte)0x40, (byte)0xA0, (byte)0x42, (byte)0xC9, (byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00, (byte)0x52, (byte)0xf0};
        //Send "TX Data" to the garage reciever.
        //byte[] msg = new byte[] {(byte)0x7E, (byte)0x00, (byte)0x15, (byte)0x10, (byte)0x10, (byte)0x00, (byte)0x13, (byte)0xa2, (byte)0x00, (byte)0x40, (byte)0x80, (byte)0xc0, (byte)0xa6, (byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00, (byte)0x54, (byte)0x58, (byte)0x20, (byte)0x44, (byte)0x61, (byte)0x74, (byte)0x61, (byte)0xc1};
        //Send a remote AT command to set D0 output to low.//
        //byte[] msg = new byte[] {(byte)0x7E, (byte)0x00, (byte)0x10, (byte)0x17, (byte)0x05, (byte)0x00, (byte)0x13, (byte)0xa2, (byte)0x00, (byte)0x40, (byte)0x7b, (byte)0x88, (byte)0xe5, (byte)0xFF, (byte)0xFE, (byte)0x02, (byte)0x44, (byte)0x30, (byte)0x04, (byte)0x8f};
        //Send a remote AT command to set D0 output to high.
        //byte[] msg = new byte[] {(byte)0x7E, (byte)0x00, (byte)0x10, (byte)0x17, (byte)0x05, (byte)0x00, (byte)0x13, (byte)0xa2, (byte)0x00, (byte)0x40, (byte)0x7b, (byte)0x88, (byte)0xe5, (byte)0xFF, (byte)0xFE, (byte)0x02, (byte)0x44, (byte)0x30, (byte)0x05, (byte)0x8e};
        //ring gate bell
        //byte[] msg = new byte[] {(byte)0x7E, (byte)0x00, (byte)0x10, (byte)0x17, (byte)0x05, (byte)0x00, (byte)0x13, (byte)0xa2, (byte)0x00, (byte)0x40, (byte)0x80, (byte)0xc0, (byte)0xa6, (byte)0xFF, (byte)0xFE, (byte)0x02, (byte)0x44, (byte)0x30, (byte)0x04, (byte)0x91};
        
        CreateTransmitRequest tr = new CreateTransmitRequest();
        tr.setTxData("R");
        byte[] add = new byte[] {(byte)0x00, (byte)0x13, (byte)0xA2, (byte)0x00, (byte)0x40, (byte)0xA0, (byte)0x42, (byte)0xC9,}; 
        tr.setAddress(add);
        tr.buildMessage();
        SendRawMessage ntc = new SendRawMessage(tr.getMsgData(),"192.168.21.72");
        ntc.SendData();
    }
}
