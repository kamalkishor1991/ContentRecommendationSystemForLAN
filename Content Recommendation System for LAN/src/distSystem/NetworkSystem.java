package distSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Map;

import Log.WriteLog;
/**
 * This class represent a Network System and provide information about system in a network.
 * @author kamal
 *
 */
public class NetworkSystem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Information about network system.
	 */
    
	private InetAddress add;
	private long lat;
	private byte auth[];
	private double memory=1,memoryFree;
	private String processing;//in HZ 
	private int noCores=0;
	private Map<String, String> systemInfo;
	private long proTime=0;
        /**
         * Set System parameter 
         * @param auth 
         */
	public NetworkSystem(byte auth[]){
		proTime=System.currentTimeMillis();
		
		this.auth=auth;
		SystemInfo si=new SystemInfo();
		memory=si.getTotalPhysicalMemory();
		memoryFree=si.getFreeMemory();
		noCores=Integer.parseInt(System.getenv("NUMBER_OF_PROCESSORS"));
		systemInfo=System.getenv();
		
                
                /*
		Runtime rt=Runtime.getRuntime();
		String s="wmic cpu get name,CurrentClockSpeed";
		//WriteLog.print(s);
		BufferedReader br;
		
		Process pr = null;
		try {
			pr = rt.exec(s);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//WriteLog.println("dasfdsf");
		br=new BufferedReader(new InputStreamReader(pr.getInputStream()));
		try {
			br.readLine();
			//WriteLog.println(a);
			br.readLine();
			//WriteLog.println(a);
			this.processing=br.readLine();
			//WriteLog.println("----processing:"+a);
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		*/
		proTime=System.currentTimeMillis()-proTime;
		WriteLog.println("processingTime"+proTime);
		
	}
	/**
         * This method may take time because using CMD commend "wmic cpu get name,CurrentClockSpeed"; 
         */
        public void setProcessing(){
            Runtime rt=Runtime.getRuntime();
            String s="wmic cpu get name,CurrentClockSpeed";
		//WriteLog.print(s);
		BufferedReader br;
		
		Process pr = null;
		try {
			pr = rt.exec(s);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//WriteLog.println("dasfdsf");
		br=new BufferedReader(new InputStreamReader(pr.getInputStream()));
		try {
			br.readLine();
			//WriteLog.println(a);
			br.readLine();
			//WriteLog.println(a);
			this.processing=br.readLine();
			//WriteLog.println("----processing:"+a);
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        }
        
	public Map<String,String> getSystemInfo(){
		return this.systemInfo;
	}
	
	
	public double getFreeMemory(){
		return this.memoryFree;
	}
	
	public void setInetAddress(InetAddress in){
		add=in;
	}
	public byte[] getAuth(){
		return auth;
	}
	public String toString(){
		return "Processing:"+this.processing+" ,address:"+add.toString()+" ,Lat:"+lat+" ,Memory total:"+memory+" ,Memory free:"+this.memoryFree+" ,NoCores:"+this.noCores;
	}


	public void setLatancy(long l) {
		this.lat=l-proTime;
		
	}
	public double getMemory(){
		return memory;
	}
	
	public InetAddress getAddress(){
		return add;
	}
	
	
}
