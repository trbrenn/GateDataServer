/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 * @author trbrenn
 */
public class GateData {
    private String  timeStamp;
    private double  acVolts;
    private double  solarVolts;
    private int     temp;
    private boolean open;
    
    public GateData(){
        this.timeStamp = new JavaTimeStamp().getTimeStampNow();
        this.acVolts = (double)0.00;
        this.solarVolts = (double)0.00;
        this.temp = 0;
        this.open = false;        
    }

    public GateData(String timeStamp) {
        this.timeStamp = timeStamp;
        this.acVolts = (double)0.00;
        this.solarVolts = (double)0.00;
        this.temp = 0;
        this.open = false;
    }

    public GateData(String timeStamp, double acVolts) {
        this.timeStamp = timeStamp;
        this.acVolts = acVolts;
        this.solarVolts = (double)0.00;
        this.temp = 0;
        this.open = false;
    }

    public GateData(String timeStamp, double acVolts, double solarVolts) {
        this.timeStamp = timeStamp;
        this.acVolts = acVolts;
        this.solarVolts = solarVolts;
        this.temp = 0;
        this.open = false;
    }

    public GateData(String timeStamp, double acVolts, double solarVolts, int temp) {
        this.timeStamp = timeStamp;
        this.acVolts = acVolts;
        this.solarVolts = solarVolts;
        this.temp = temp;
        this.open = false;
    }

    public GateData(String timeStamp, double acVolts, double solarVolts, int temp, boolean open) {
        this.timeStamp = timeStamp;
        this.acVolts = acVolts;
        this.solarVolts = solarVolts;
        this.temp = temp;
        this.open = open;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public double getAcVolts() {
        return acVolts;
    }

    public double getSolarVolts() {
        return solarVolts;
    }

    public int getTemp() {
        return temp;
    }

    public boolean isOpen() {
        return open;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAcVolts(double acVolts) {
        this.acVolts = acVolts;
    }

    public void setSolarVolts(double solarVolts) {
        this.solarVolts = solarVolts;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    public void processGateData(String msg){
        int step = 1;
        int index = 0;
        String hex;
        int arrayLen;
        int len = msg.length();
        byte rawData[];
        
        arrayLen = (len - 1) / 3;
        rawData = new byte[arrayLen];
        
        while(step < len){
            hex = msg.substring(step, step+2);
            rawData[index++] = (byte)Integer.parseInt(hex,16); 
            step = step +3;
        }  
        
        hex = new String(rawData);
        
        try{
            StringTokenizer st = new StringTokenizer(hex, "=,");
            while(st.hasMoreTokens()) {
                String key = st.nextToken();
                String val = st.nextToken();
                if(key.contains("Temp")){
                    temp = Integer.parseInt(val.trim());
                }
                else if(key.contains("AC")){
                    acVolts = Double.parseDouble(val.trim());
                }
                else if(key.contains("Solar")){
                    solarVolts = Double.parseDouble(val.trim());
                }     
            }     
        }
        catch(NoSuchElementException e){
            //I just catch it because it always fires.
            //JOptionPane.showMessageDialog(new Frame(),"Error: "+e,"No Such Element Exception",JOptionPane.ERROR_MESSAGE);
        }
        
        if(hex.toUpperCase().contains("OPEN")){
            open = true;
            PlaySound ps = new PlaySound();
            ps.MakeSound("C:\\WINDOWS\\Media\\sphinx1.wav");
        }
        else
            open = false;
        
        timeStamp = new JavaTimeStamp().getTimeStampNow();
        
    }
    
    public boolean isGataData(String msg){
        //|47|61|74|65|20|4F|70|65|6E|
        //|47|61|74|65|20|52|65|70|6F|72|74|
        if(msg.contains("|47|61|74|65|20|4F|70|65|6E|") || msg.contains("|47|61|74|65|20|52|65|70|6F|72|74|"))
            return true;
        else
            return false;
    }
    
    public String toString(){
        String str = "";
        
        str = str + "TimeStamp   = "+timeStamp+"\n";
        str = str + "AC Volts    = "+acVolts+"\n";
        str = str + "Solar Volts = "+solarVolts+"\n";
        str = str + "Temp        = "+temp+"\n";    
        if (open)
            str = str + "Gate opened\n";
        else
            str = str + "Gate reported\n";

        
        return str;
    }
    
    public static void main(String ARGV[]) {
        String test = "|00|13|A2|00|40|A0|42|C9|FF|FE|01|54|65|6D|70|3D|31|30|31|2C|20|41|43|3D|32|31|2E|37|35|2C|20|53|6F|6C|61|72|3D|31|38|2E|32|34|2C|20|47|61|74|65|20|4F|70|65|6E|";

        GateData gd = new GateData();
        
        if(gd.isGataData(test))
            System.out.println("It's a gate message!");
        else 
            System.out.println("Nope, it no good!");
        
        gd.processGateData(test);
        System.out.println(gd);
    }
}