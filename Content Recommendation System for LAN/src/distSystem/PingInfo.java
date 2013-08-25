package distSystem;

import Log.WriteLog;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class gives information about systems which are running this class.
 * and also provide method {@link #ping(long)} to Ping other systems.
 * @author kamal
 *
 */
public class PingInfo implements Runnable {
	
	private int port,portRec;
	private DatagramSocket ds;
	private byte b[];
	private PingThread pt;
	/**
	 * flag to determine receiving thread is receiving or not.
	 */
	private boolean isRec=false;
	/**
	 * for calculating latency.
	 */
	private long currentTime=0;
	
	private int authDataLength=10;
	private boolean stop1=false,stop2=false,isStop=false;
	/**
	 * To open a port so that other can Ping.
	 * @param port  port to use.
	 * @param portRec Reply port.
	 * @throws SocketException - if the socket could not be opened, or the socket could not bind to the specified local port. 
       
	 */
	public PingInfo(int port,int portRec) throws SocketException{
		this.port=port;
		this.portRec=portRec;	
	}
        public void startPingServer() throws SocketException{
            ds=new DatagramSocket(port);
            Thread tt=new Thread(this);
            tt.start();
        }
        
        public void startPingReq() throws SocketException{
            DatagramSocket dd=new DatagramSocket(portRec);
            pt=new PingThread(dd);
            Thread t=new Thread(pt);
            t.start();
        }
        
        
	/**
	 * @param timeout timeout for ping.
	 * @return All ping replies.
	 * @throws SocketException - if the socket could not be opened, or the socket could not bind to the specified local port. 
	 * @throws UnknownHostException 
       
	 */
	public LinkedList<NetworkSystem> ping(long timeout) throws SocketException, UnknownHostException{
		LinkedList<NetworkSystem> in=new LinkedList<NetworkSystem>();
		DatagramSocket dd=new DatagramSocket();
		b=new byte[this.authDataLength];
		DatagramPacket dp=new DatagramPacket(b,b.length, InetAddress.getByName("255.255.255.255"),port);
		pt.setList(in);
		
		for(int i=0;i<b.length;i++){
			b[i]=(byte)(int)(Math.random()*128);
		}
		this.currentTime=System.currentTimeMillis();
		try {
			dd.send(dp);
		} catch (IOException e) {
		}
		
		isRec=true;
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
		
		isRec=false;//flag to determine receiving thread is receiving or not.
		dd.close();
		return in;
	}
	/**
	 * Threads stopped or not.
	 * @return true if stopped.
	 */
	public boolean isStopped(){
		return (stop1&&stop2);
	}
	
	
	/**
	 * to stop receiving threads.
	 * 
	 */
	public void stop() {
		try {
			DatagramSocket dd=new DatagramSocket();
			b=new byte[1];
			b[0]=127;
			isStop=true;
			while(true){
				DatagramPacket dp=new DatagramPacket(b,b.length, InetAddress.getLocalHost(),port);
				dd.send(dp);
				dp=new DatagramPacket(b,b.length, InetAddress.getLocalHost(),this.portRec);
				dd.send(dp);
				
				if(stop1&&stop2){
					break;
				}
			}
			dd.close();
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		isStop=false;
		//to stop
	}
	
	private class PingThread implements Runnable{
		DatagramSocket d;
		LinkedList<NetworkSystem> in;
		
		//int pr;
		PingThread(DatagramSocket d){
			this.d=d;	
		}
		public void setList(LinkedList<NetworkSystem> in ){
			this.in=in;
		}
		
		
		/**
		 * Ping response Thread.
		 */
        public void run() {
			byte bd[]=new byte[5000];
			DatagramPacket dp=new DatagramPacket(bd,bd.length);
			while(true){
				try {
					d.receive(dp);
					if(isStop){
						stop1=true;
						return;
					}
						
					WriteLog.println("Ping response Packet Received ");
					if(isRec){
						
						byte bb[]=dp.getData();
						
						
						NetworkSystem n=(NetworkSystem) Serializer.deserialize(bb);
						n.setInetAddress(dp.getAddress());
						n.setLatancy(System.currentTimeMillis()-currentTime);
						byte a[]=n.getAuth();
					
						if(Arrays.equals(a, b)){
							in.add(n);
						}
						
					}
				} catch (Exception e) {
				}
			}
			
		}
		
		
		
	}
	
	
	
	/**
	 * Ping request receiving Thread
	 */
	@Override
	public void run() {
		DatagramSocket dd=null;
		try {
			dd=new DatagramSocket();
		} catch (SocketException e1) {
		}
		byte b[]=new byte[this.authDataLength];
		DatagramPacket dp=new DatagramPacket(b,b.length);
		WriteLog.println("Ping Started");
		while(true){
			try {
				ds.receive(dp);
				WriteLog.println("Ping request Packet Received ");
				if(isStop){
					stop2=true;
					dd.close();
					return ;
				}
				//send confirmation
				NetworkSystem ns=new NetworkSystem(dp.getData());
				byte send[]=Serializer.serialize(ns);
				DatagramPacket dpp=new DatagramPacket(send,send.length, dp.getAddress(),this.portRec);
			
				dd.send(dpp);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
