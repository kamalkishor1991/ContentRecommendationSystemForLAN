/*
 * static Data related to application
 */
package Log;

import distSystem.PingInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *Static Data of Application
 * @author kamal
 */
public class Data {
    public static final long PING_TIMEOUT = 1000;
	public static final int PORT=10000;
    public static final int PORTREC=10001;
    public static String MAC_ADDRESS;
    
    public static PingInfo PI;
    /**
     * Must call before application start.
     * assign static values 
     * @throws SocketException 
     */
    public static  void start() throws SocketException{
        PI=new PingInfo(Data.PORT,Data.PORTREC);
        PI.startPingReq();
        PI.startPingServer();
        InetAddress ip;
		try {
	 
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			Data.MAC_ADDRESS=sb.toString();
	 
		} catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		} catch (SocketException e){
	 
			e.printStackTrace();
	 
		}
    }
    
    
    public static void  stopPingSer(){
        PI.stop();
    }
      
}
