/*
 * 
 * 
 */
package gatedataserver;

/**
 *
 * @author trbrenn
 */
public class CreateATCommand {
    private boolean query      = false;
    private boolean queue      = false;
    private byte    length     = 0x05;
    private byte    frameID    = 0x0;
    private byte[]  ATcmd      = new byte[2];
    private byte    paramValue = 0x0;
    private byte    offset     = 0x0;
    private byte[]  msgData;
    
    //creates a blank AT command message
    public CreateATCommand() { 
        ATcmd[0] = 0x0;
    }
    
    //creates an AT command to query the value of the command register.
    public CreateATCommand(byte[] cmd) {
        ATcmd[0] = cmd[0];
        ATcmd[1] = cmd[1];
        query = true;
    }

    //creates an AT command to query the value of the command register and set a frame ID.
    public CreateATCommand(byte frmID, byte[] cmd) {
        frameID = frmID;
        ATcmd[0] = cmd[0];
        ATcmd[1] = cmd[1];
        query = true;
    }
    
    //creates an AT command to change that value of the command register.
    public CreateATCommand(byte[] cmd, byte par) {
        ATcmd[0] = cmd[0];
        ATcmd[1] = cmd[1];
        paramValue = par;
    }
    
    //creates an AT command to change that value of the command register and set a frame ID.
    public CreateATCommand(byte frmID, byte[] cmd, byte par) {
        frameID = frmID;
        ATcmd[0] = cmd[0];
        ATcmd[1] = cmd[1];
        paramValue = par;        
    }

    //returns the query value
    public boolean isQuery() {
        return query;
    }

    //allows  user to set a query.
    public void setQuery(boolean query) {
        this.query = query;
        if(query)
            msgData = new byte[8];
        else
            msgData = new byte[9];
    }

    //Returns the queue value.
    public boolean isQueue() {
        return queue;
    }

    //allows user to change the queue value.
    public void setQueue(boolean queue) {
        this.queue = queue;
    }

    //returns the frame ID of the message
    public byte getFrameID() {
        return frameID;
    }

    //allows the user to set the frame ID
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
    
    //builds the message after the user has set all values.
    public int buildMessage() {
        if(ATcmd[0] == 0)
            return -1;
        
        int i;                           //used for loop control.
        int msgLen = 9;                  //set the length to include the parameter value.
        if(query)
            msgLen = 8;                  //set the length to exclude the parameter value.
        msgData = new byte[msgLen];      //create the message data byte array.
        
        msgData[0] = (byte) 0x7e;        //Always set to 0x7e as this is the Start Delimiter of all xBee messages.
        msgData[1] = (byte) 0x0;         //Length can only be a maximum 0xFF so this always 0
        msgData[2] = (byte) length;      //Length of the data in the packet.
        if(queue)                        //determine if the command should be applied immeditaly.
            msgData[3] = (byte) 0x9;     //do not apply the setting until told too.
        else 
            msgData[3] = (byte) 0x8;     //apply this command now.
        msgData[4] = (byte) frameID;     //This can be set from 0x0 to 0xFF. 
        msgData[5] = ATcmd[0];           //Set the first bit of the AT command.
        msgData[6] = ATcmd[1];           //Set the second bit of the AT command.
        
        if(!query)                       //check to see if the parameter is included.
            msgData[7] = paramValue;     //Set the parameter value.
        
        int msgCnt = 0;                  //Set msgCnt to 0 this will be used to add the total bits for the offset calc.
        for(i=3; i < msgLen-1; i++) {    //loop thru just the data area of the packet.
            msgCnt = msgCnt + msgData[i];//Add up all the bytes in the packet data area 
        }
        
        msgCnt = msgCnt & 0x00ff;          //knock off all the data but the last 2 bits.
        offset = (byte)((byte)0xff - (byte)msgCnt);  //calculate the offset by subtracting 0xFF from the total.
        msgData[msgLen - 1] = offset;      //Write the offset to the last byte of the packet.
        
        return msgLen;                     //return the completed packet.
    }
    
    //Returns the message packet if it has been created. Otherwise it returns two 0 bytes.
    public byte[] getMsgData() {
        if (ATcmd[0] > 0)       //check to see if the length has been calculated.
            return msgData;   //return the packet if it is valid.
        else
            return new byte[] {0x0,0x0}; //return an valid data array that is blank.
    }

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
            temp = temp + " Start Delimiter = " + formatByte(msgData[0]) + "\n";
            temp = temp + "          Length = 00, " + formatByte(msgData[2])+"\n";
            temp = temp + "      Frame Type = " + formatByte(msgData[3]) + "\n";
            temp = temp + "        Frame ID = " + formatByte(msgData[4]) + "\n";
            temp = temp + "      AT Command = \"" + (char)msgData[5] + (char)msgData[6] + "\"\n";          
            if(!queue)
                temp = temp + " Parameter Value = " + formatByte(msgData[7]) + "\n";
            temp = temp + "          Offset = " + formatByte(msgData[msgLen-1]) + "\n";
            return temp;
        }
    }
    
    public static void main(String ARGV[]) {
        byte[] cmd = new byte[] { (byte)'D', (byte)'0' };
        byte parm = 0x05;
        
        CreateATCommand catc = new CreateATCommand();
        catc.setATcmd(cmd);
        catc.setParamValue(parm);        
        catc.setQueue(false);
        catc.buildMessage();
        System.out.println(catc);   
    }
}
