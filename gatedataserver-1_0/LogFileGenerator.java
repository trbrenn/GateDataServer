/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.awt.Frame;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
/**
 *
 * @author trbrenn
 */
public class LogFileGenerator {
    private PrintWriter dataStreamOut;
    private String path = "C:\\Users\\trbrenn\\Desktop\\";
    private String filename;
    
    public LogFileGenerator(){
        JavaTimeStamp jts = new JavaTimeStamp();
        filename = "GateLog-" + jts.getDateNow();
    }
    
    public LogFileGenerator(String file){
        filename = file;
    }
    
    public LogFileGenerator(String file, String p){
        try{
            filename = file;
            path = p;
        }catch(Exception e) { 
            JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"Error in LogFileGenerator Consdtructor",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void open(){
        try 
        {
           File fileOut = new File(path+filename);
           dataStreamOut = new PrintWriter(fileOut);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"Error in LogFileGenerator Consdtructor",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void close(){
        try {
            dataStreamOut.close();
        }
        catch(Exception e){
          JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"Error in LogFileGenerator Close",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void write(String msg) {
        try {
            dataStreamOut.print(msg);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"Error in LogFileGenerator Write",JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    public void writeln(String msg) {
        try {
            dataStreamOut.println(msg);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"Error in LogFileGenerator Write",JOptionPane.ERROR_MESSAGE);
        }   
    }
    
    public static void main(String ARGV[]){
        LogFileGenerator lfc = new LogFileGenerator("test.txt","C:\\Users\\trbrenn\\Desktop\\");
        lfc.open();
        lfc.writeln("This is the test of the data loging fuctions.");
        lfc.write("This is the ");
        lfc.writeln("second line.");
        lfc.close();
    }    
}
