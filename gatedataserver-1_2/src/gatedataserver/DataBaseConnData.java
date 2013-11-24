/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gatedataserver;

/**
 *
 * @author Todd Brenneman
 */
public class DataBaseConnData {
    
    private String URL;
    private String UserName;
    private String PassWord;
    private String CL;
	
    public DataBaseConnData(){
        URL = "jdbc:mysql://192.168.21.2:3306/xbeedata";
	UserName = "XbeeUser";
	PassWord = "GateData!";
	CL = "com.mysql.jdbc.Driver";
    }

    public DataBaseConnData(String url, String cl){
	URL = url;
	UserName = "XbeeUser";
	PassWord = "GateData!";
	CL = cl;
    }

    public DataBaseConnData(String url, String user, String passwd, String cl){
	URL = url;
	UserName = user;
	PassWord = passwd;
	CL = cl;
    }
		
    public String getCL(){
	return CL;
    }

    public void setCL(String cl){
	CL = cl;
    }

    public String getPassWord(){
	return PassWord;
    }

    public void setPassWord(String passWord){
	PassWord = passWord;
    }

    public String getURL(){
	return URL;
    }

    public void setURL(String url){
	URL = url;
    }

    public String getUserName(){
	return UserName;
    }

    public void setUserName(String userName){
	UserName = userName;
    }    
}