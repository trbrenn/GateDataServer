/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

/**
 *
 * @author trbrenn
 */
public class CreateRemoteATCommand {
    private boolean query      = false;
    private boolean queue      = false;    
    private byte    length     = 0x0;
    private byte    frameID    = 0x0;
    private byte[]  ATcmd      = new byte[2];
    private byte    paramValue = 0x0;
    private byte    offset     = 0x0;
    private byte[]  address    = new byte[8]; //The destination address of the module you want to send the data too.
    private byte[]  msgData;

    public CreateRemoteATCommand() {
        ATcmd[0] = 0x0;
        address = new byte[] {(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0};
    }
    
    public CreateRemoteATCommand(byte[] add, byte[] cmd) {
        query = true;
        address = add;
        ATcmd = cmd;
        query = true;
    }
    
    public CreateRemoteATCommand(byte[] add, byte[] cmd, byte param) {
        query = true;
        address = add;
        ATcmd = cmd;
        paramValue = param;
    }
    
    public CreateRemoteATCommand(byte[] add, byte[] cmd, byte param, boolean que) {
        query = true;
        address = add;
        ATcmd = cmd;
        paramValue = param;
        queue = que;
    }

    public boolean isQuery() {
        return query;
    }

    public void setQuery(boolean query) {
        this.query = query;
    }

    public boolean isQueue() {
        return queue;
    }

    public void setQueue(boolean queue) {
        this.queue = queue;
    }

    public byte getFrameID() {
        return frameID;
    }

    public void setFrameID(byte frameID) {
        this.frameID = frameID;
    }

    public byte[] getATcmd() {
        return ATcmd;
    }

    public void setATcmd(byte[] ATcmd) {
        this.ATcmd = ATcmd;
    }

    public byte getParamValue() {
        return paramValue;
    }

    public void setParamValue(byte paramValue) {
        this.paramValue = paramValue;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public byte[] getMsgData() {
        if (ATcmd[0] > 0)       //check to see if the length has been calculated.
            return msgData;   //return the packet if it is valid.
        else
            return new byte[] {0x0,0x0}; //return an valid data array that is blank.
    }

    //Creates the message after the value have been set.
    public int buildMessage() {
        if(ATcmd[0] == 0)
            return -1;
        
        int i;                           //used for loop control.
        int msgLen = 20;                 //set the length to include the parameter value.
        if(query)
            msgLen = 19;                 //set the length to exclude the parameter value.
        
        length = (byte)(msgLen - 4);     //stores the total length of the data area of the packet.
                                         //Adds the missing bytes to the total length of the packet.
        msgData = new byte[msgLen];      //creates the msgData array of the correct length.
        msgData[0] = (byte) 0x7e;        //Always set to 0x7e as this is the Start Delimiter of all xBee messages.
        msgData[1] = (byte) 0x0;         //Length can only be a maximum 0xFF so this always 0
        msgData[2] = (byte) length;      //Length of the data in the packet.
        msgData[3] = (byte) 0x17;        //Always set to 0x10 as this is the Frame Type.
        msgData[4] = (byte) frameID;     //This can be set from 0x0 to 0xFF. 
        
        for(i=0; i < 8; i++) {           //Set to the 64-bit address of the destination device. The following address 
            msgData[i+5] = address[i];   //is also supported: 0x000000000000FFFF - Broadcast address
        }
        
        msgData[13] = (byte) 0xff;       //The reserved is always set to 0xFFFE.
        msgData[14] = (byte) 0xfe;
        if(!queue)
            msgData[15] = (byte)0x02;
        else
            msgData[15] = (byte)0x0;
        
        msgData[16] = ATcmd[0];     //The only valid values are 0x0 0x1
        msgData[17] = ATcmd[1];     //The only valid values are 0x0 0x1
        if(!query)
            msgData[18] = paramValue;
        
        int msgCnt = 0;                 //Set msgCnt to 0 this will be used to add the total bits for the offset calc.
        for(i=3; i < msgLen-1; i++) {        //loop thru just the data area of the packet.
            msgCnt = msgCnt + msgData[i];  //Add up all the bytes in the packet data area 
        }
        
        msgCnt = msgCnt & 0x00ff;          //knock off all the data but the last 2 bits.
        offset = (byte)((byte)0xff - (byte)msgCnt);  //calculate the offset by subtracting 0xFF from the total.
        msgData[msgLen - 1] = offset;      //Write the offset to the last byte of the packet.
        
        return msgLen;                     //return the completed packet.
    }
        
    //Changes bytes into a pretty string for output in the toString method.
    private String formatByte(byte num) {
        String txt; 
        
        txt = (Integer.toHexString(num).toUpperCase());  //convert the byte to a valid string.
        if(txt.length() > 2){                            //if the byte looks negative it will show up as longer than 2 characters.
            txt = txt.substring(6);                      //Cut off everything but the last two characters.
        }
        if(txt.length() < 2)                             //if the byte is only one digit
            txt = "0" + txt;                             //Add a zero to the single character.
        return txt;                                      //return the valid string.
    }
    
    //used to create a formatted version of the message packet.
    @Override
    public String toString() {
        
        String temp = "";
        int msgLen = msgData.length;
        
        if(length < 1) {
            return temp;
        } else {
            int i;
            temp = temp + "        Start Delimiter = " + formatByte(msgData[0]) + "\n";
            temp = temp + "                 Length = 00, " + formatByte(msgData[2])+"\n";
            temp = temp + "             Frame Type = " + formatByte(msgData[3]) + "\n";
            temp = temp + "               Frame ID = " + formatByte(msgData[4]) + "\n";
            temp = temp + "            Destination = ";
            for(i = 5; i < 13; i++) {
                temp = temp + formatByte(msgData[i]);
                if (i < 12)
                    temp = temp + ", ";
            }
            temp = temp + "\n";
            temp = temp + "               Reserved = FF, FE\n";
            temp = temp + " Remote Command Options = " + formatByte(msgData[15]) + "\n";          
            temp = temp + "             AT Command = \"" + (char)msgData[16] + (char)msgData[17] + "\"\n";
            if(!queue)
                temp = temp + "      Command Parameter = " + formatByte(msgData[18]) + "\n";
            temp = temp + "                 Offset = " + formatByte(msgData[msgLen-1]) + "\n";
            return temp;
        }
    }
    
    public static void main(String ARGV[]) {
        byte[] cmd = new byte[] { (byte)'D', (byte)'0' };
        byte parm = 0x05;
        CreateRemoteATCommand tr = new CreateRemoteATCommand();
        byte[] add = new byte[] {(byte)0x00, (byte)0x13, (byte)0xA2, (byte)0x00, (byte)0x40, (byte)0xA0, (byte)0x42, (byte)0xC9,}; 
        tr.setAddress(add);
        tr.setATcmd(cmd);
        tr.setParamValue(parm);
        tr.setQueue(true);
        tr.buildMessage();
        System.out.println(tr);   

    }
}
