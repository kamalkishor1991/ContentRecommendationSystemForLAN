package webServer;

import Log.WriteLog;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Make your computer a web server on specified port.
 * 
 * @author kamal
 *
 */
public class WebServer implements Runnable {
	private ServerSocket ss;
	private  ProcessHttpReq proHttpReq;
        private Thread t;;
	/**
	 * Open web server on specified port.
	 * @param port port for opening.
	 * @throws IOException 
	 */
	public WebServer(int port,ProcessHttpReq proHttpReq) throws IOException{
		ss=new ServerSocket(port);
		this.proHttpReq=proHttpReq;
		t=new Thread(this);
		
	}
	
    public void start(){
       t.start(); 
    }
	public ProcessHttpReq getProcessHttpReq() {
		return proHttpReq;
	}
	/**
	 * set  ProcessHttpReq to run get and post request.
	 * @param proHttpReq
	 */
	public void setProcessHttpReq(ProcessHttpReq proHttpReq) {
		this.proHttpReq = proHttpReq;
	}
	
	
	@Override
	public void run() {
		ProcessRequest pr=new ProcessRequest(this.proHttpReq);
		WriteLog.println("server started ");
		while(true){
			try {
				
				Socket s=ss.accept();
				pr.start(s);
			} catch (IOException e) {
			}
			
		}
	}
}
