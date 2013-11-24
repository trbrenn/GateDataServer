/*
 * This class is used to create transmit requests on the xBee Digimesh network. 
 */
package gatedataserver;

/**
 *
 * @author trbrenn
 */
public class CreateTransmitRequest {
    private byte   length = 0x0;          //The message length encoded in the header not the total length of the packet.
    private byte   frameID = 0x1;         //The frame id of the message. Cannot be over 254.
    private byte[] address;               //The destination address of the module you want to send the data too.
    private byte[] txData;                //The string data to be sent.
    private byte   offset = 0x0;          //This is calculated when the message is created.
    private byte[] msgData;               //This is the whole packet ready to send.
    private byte   transmitOpts = 0x0;    //0 = Disable ACK. 1 = Don't attempt route Discovery.
    private byte   broadcastRaduis = 0x0; //Sets maximum number of hops a broadcast transmission can occur. If set to 0,
                                          //the broadcast radius will be set to the maximum hops value
    
    //Create an empty message. 
    public CreateTransmitRequest() {
        address = new byte[] {(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0};
        txData = new byte[] {(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0,(byte)0x0};
    }
    
    //Create a message and set the address and message as byte arrays.
    public CreateTransmitRequest(byte[] add, byte[] msg) {
        address = add;
        txData = msg;
    }

    //Create a message and set the FrameID as a byte and address, message as byte arrays.
    public CreateTransmitRequest(byte fid, byte[] add, byte[] msg) {
        frameID = fid;
        address = add;
        txData = msg;
    }
    
    //This method must be called to create a usable message.
    public int buildMessage() {
        if(length == 0)
            return -1;
        
        int i;                              //used for loop control.
        int msgLen = txData.length;         //stores the total length of the packet and adds the transmit data length.
        msgLen = msgLen + address.length;   //adds the address length to the total length of the packet.
        msgLen = msgLen + 6;                //adds the remaining data to teh total length of the packet.
        
        length = (byte)msgLen;              //stores the total length of the data area of the packet.
        msgLen = msgLen + 4;                //Adds the missing bytes to the total length of the packet.
        
        msgData = new byte[msgLen];      //creates the msgData array of the correct length.
        msgData[0] = (byte) 0x7e;        //Always set to 0x7e as this is the Start Delimiter of all xBee messages.
        msgData[1] = (byte) 0x0;         //Length can only be a maximum 0xFF so this always 0
        msgData[2] = (byte) length;      //Length of the data in the packet.
        msgData[3] = (byte) 0x10;        //Always set to 0x10 as this is the Frame Type.
        msgData[4] = (byte) frameID;     //This can be set from 0x0 to 0xFF. 
        
        for(i=0; i < 8; i++) {           //Set to the 64-bit address of the destination device. The following address 
            msgData[i+5] = address[i];   //is also supported: 0x000000000000FFFF - Broadcast address
        }
        
        msgData[13] = (byte) 0xff;       //The reserved is always set to 0xFFFE.
        msgData[14] = (byte) 0xfe;
        msgData[15] = (byte) broadcastRaduis;  //can only be a maximum 0xFF.
        msgData[16] = (byte) transmitOpts;     //The only valid values are 0x0 0x1
        
        int dataLen = txData.length;     //Get the length of the transmit data byte array.
        
        for(i=0; i < dataLen; i++) {     //Copy the transmit byte array to the message packet.
            msgData[i+17] = txData[i];
        }
        
        int len = msgLen;               //Get the length of the packet.
        int msgCnt = 0;                 //Set msgCnt to 0 this will be used to add the total bits for the offset calc.
        for(i=3; i < len; i++) {        //loop thru just the data area of the packet.
            msgCnt = msgCnt + msgData[i];  //Add up all the bytes in the packet data area 
        }
        
        msgCnt = msgCnt & 0x00ff;          //knock off all the data but the last 2 bits.
        offset = (byte)((byte)0xff - (byte)msgCnt);  //calculate the offset by subtracting 0xFF from the total.
        msgData[msgLen - 1] = offset;      //Write the offset to the last byte of the packet.
        
        return msgLen;                     //return the completed packet.
    }
    
    public byte getTransmitOpts() {
        return transmitOpts;
    }

    public void setTransmitOpts(byte tOpts) {
        this.transmitOpts = tOpts;
    }

    public byte getBroadcastRaduis() {
        return broadcastRaduis;
    }

    public void setBroadcastRaduis(byte bRaduis) {
        this.broadcastRaduis = bRaduis;
    }

    public byte getLength() {
        return length;
    }

    public byte getFrameID() {
        return frameID;
    }

    public byte[] getAddress() {
        return address;
    }

    public byte[] getTxData() {
        return txData;
    }

    public byte getOffset() {
        return offset;
    }
    
    //Returns the message packet if it has been created. Otherwise it returns two 0 bytes.
    public byte[] getMsgData() {
        if (length > 0)       //check to see if the length has been calculated.
            return msgData;   //return the packet if it is valid.
        else
            return new byte[] {0x0,0x0}; //return an valid data array that is blank.
    }

    public void setFrameID(byte frameID) {
        this.frameID = frameID;
    }

    //convert an integer to a byte byte value. It limits the value to a maximum of 254.
    //returns the value Frame ID was set too.
    public int setFrameID(int fid) {
        if (fid > 254) {
            this.frameID = (byte)0x1;
            return 1;
        } else {
            this.frameID = (byte)fid;
            return fid;
        }
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public void setTxData(byte[] txData) {
        this.txData = txData;
    }

    //Convert a string to byte array so it can be copied into the message packet.
    public void setTxData(String tx) {
        int step = 0;
        char temp[] = tx.toCharArray();
        
        int len = temp.length;
        txData = new byte[len];
        
        while(step < len){
            txData[step] = (byte)temp[step++];
        }
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
            temp = temp + "  Start Delimiter = " + formatByte(msgData[0]) + "\n";
            temp = temp + "           Length = 00, " + formatByte(msgData[2])+"\n";
            temp = temp + "       Frame Type = " + formatByte(msgData[3]) + "\n";
            temp = temp + "         Frame ID = " + formatByte(msgData[4]) + "\n";
            temp = temp + "      Destination = ";
            for(i = 5; i < 13; i++) {
                temp = temp + formatByte(msgData[i]);
                if (i < 12)
                    temp = temp + ", ";
            }
            temp = temp + "\n";
            temp = temp + "         Reserved = FF, FE\n";
            temp = temp + " Broadcast Radius = " + formatByte(msgData[15]) + "\n";          
            temp = temp + " Transmit Options = " + formatByte(msgData[16]) + "\n";
            temp = temp + "          RF Data = ";            
            for(i = 17; i <= msgLen-2; i++) {
                temp = temp + formatByte(msgData[i]);
                if(i < msgLen-2)
                    temp = temp + ", ";
            }
            temp = temp + "\n";
            temp = temp + "           Offset = " + formatByte(msgData[msgLen-1]) + "\n";
            return temp;
        }
    }
    
    public static void main(String ARGV[]) {
        CreateTransmitRequest tr = new CreateTransmitRequest();
        tr.setTxData("R");
        byte[] add = new byte[] {(byte)0x00, (byte)0x13, (byte)0xA2, (byte)0x00, (byte)0x40, (byte)0xA0, (byte)0x42, (byte)0xC9,}; 
        tr.setAddress(add);
        tr.buildMessage();
        System.out.println(tr);   
    }
}
