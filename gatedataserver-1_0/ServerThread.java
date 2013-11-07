/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.*;
import java.io.*;

/**
 *
 * @author trbrenn
 * @date 07-05-2013
 */

public class ServerThread implements Runnable {
    private Socket          socket;
    private ServerSocket    sSocket;
    private int             ID;
    private int             port;
    private JavaTimeStamp   jts;
    private Thread          thread;
    private GateDataServer  gds;

    /***************************************************************************
     * This the main constructor for the listening server. It will prep all the*
     * values and prepare to run the thread.                                   *
     *                                                                         *
     * @param p is the port number you would like the server to listen on.     *
     * @param count is just of the connections created it is reset at 32000.   *
     **************************************************************************/    
    public ServerThread(GateDataServer g, int p, int count) {
        port = p;
        ID = count;
        gds = g;
        jts = new JavaTimeStamp();
        thread = new Thread(this);

        try{
            sSocket = new ServerSocket(port);
            gds.processText("Waiting on port " + port);
        }
        catch (Exception e) {
            gds.processError("ServerThread error in constructor");
            gds.processError(e.toString());
        }
    }

    public Socket getSocket() {
        return socket;
    }
     public int getID() {
        return ID;
    }
    public void setSocket(Socket s) {
        socket = s;
    }
    public void setID(int id) {
        ID = id;
    }
    
    /***************************************************************************
     * run() This is the thread that listens for messages from the XBEE network*
     **************************************************************************/ 
    @Override
    public void run() {
       String str;
       String xml;
       InputStream is;
       InputStreamReader isr;
       BufferedReader br;
       String now;

       try {
           while(true){
               socket = sSocket.accept();
               is = socket.getInputStream();
               isr = new InputStreamReader(is);
               br = new BufferedReader(isr);
               now = jts.getTimeStampNow();

               gds.processText("Connect! ID =" + ++ID);

               if(ID > 32000){
                   ID = 0;
               }
               
               str = br.readLine();
               xml = str.substring(0,49) + "<TimeStamp>" + now + "</TimeStamp>" + str.substring(49);
               gds.processMsg(xml);
               gds.processText(xml);
               str = br.readLine();
               gds.processText(str);
               gds.processText("Disconnect! ID =" + getID());
               socket.close();
           }
       }
       catch(Exception e) {
           //termWin.getXbeeOutputText().append("Error: "+e);
           gds.processError("ServerThread Thread Error: ");
           gds.processError(e.toString());
           
       }
   }
    
   /****************************************************************************
    * startServer(). starts the thread that listens for incoming messages from *
    * the XBEE network.                                                        *
    ***************************************************************************/
   public void startServer(){
       thread.start();
   }
                
    public static void main(String[] args) {
        int port = 1088;
        int count = 0;
        
        ServerThread server = new ServerThread(new GateDataServer(), port,count);
        server.startServer();
    }
}
