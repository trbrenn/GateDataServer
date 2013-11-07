/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author toddb
 */
public class JavaTimeStamp {
    
    public JavaTimeStamp(){
    }
    
    public String getTimeStampNow(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }

    public String getTimeStamp(Date now){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }
    
    public String getDateNow() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now);
    }
    
    public String getDateNow(Date now) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(now);
    }

    public static void main(String[] args) {
        // Make a new Date object. It will be initialized to the
        // current time.
        Date now = new Date();
        JavaTimeStamp jts = new JavaTimeStamp();
        System.out.println("The time is: "+jts.getTimeStamp(now));
        System.out.println("The time is: "+jts.getTimeStampNow());
    }   
    
}
