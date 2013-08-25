package fileInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SynchronizedResponse implements Runnable {
	
	private FileInfoDatabase database;
	private Thread t;
	private int port=45678;
	private boolean isStop=false;
	public SynchronizedResponse(FileInfoDatabase database){
		this.database=database;
		t=new Thread(this);
	}
	/**
	 * Start response Thread
	 */
	public void start(){
		t.start();
	}
	
	
	/**
	 * Stops response Thread.
	 */
	public void stop(){
		try {
			isStop=true;
			Socket sc=new Socket("localhost",port);
			
			sc.close();
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
		
	}
	
	@Override
	public void run(){
		try {
			ServerSocket ss=new ServerSocket(port);
			while(true){
				Socket soc=ss.accept();
				if(isStop){
					soc.close();
					break;
				}
				OutputStream out=soc.getOutputStream();
				database.writeHashMap(out);
				soc.close();
			}
			ss.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
	}

}
