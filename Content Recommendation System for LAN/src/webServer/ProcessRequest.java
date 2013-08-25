package webServer;

import Log.WriteLog;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Process Http request in new Thread.
 * @author kamal
 *
 */
public class ProcessRequest  {

	private  ProcessHttpReq proHttpReq;
       
	public ProcessRequest(ProcessHttpReq proHttpReq){
		
		this.proHttpReq=proHttpReq;
		//t=new Thread(this);	
	}
        
        /**
         *to start new Thread. 
         */ 
    public void start(Socket soc){
    	ProcessReqThread tt=new ProcessReqThread(soc);
        Thread t=new Thread(tt); 
        t.start();
    }
	
	
	private class ProcessReqThread implements Runnable{
            private Socket soc;
            ProcessReqThread(Socket soc){
            this.soc=soc;
    }
           
    /**
     * ArrayList contains all lines in header.
     * @param header
     * @throws IOException
     */        
   private void processHeader(ArrayList<String> header) throws IOException{
		OutputStream out=soc.getOutputStream();
		if(header.isEmpty()){
                    return;
                }
		String s=header.get(0);
		String ss[]=s.split(" ");
		if(ss[0].equalsIgnoreCase("get")){
                        //get request 
			proHttpReq.doGet(header, out);
		}
		else if(ss[0].equalsIgnoreCase("post")){
			proHttpReq.doPost(header, out);
		}
                System.gc();
		
		
	}
	 
	@Override
	public void run() {
		BufferedReader br=null;
		try {
			InputStream in=soc.getInputStream();
			br=new BufferedReader(new InputStreamReader(in));
		} catch (IOException e) {
                    WriteLog.println("error:"+e);
                }
		try {
			ArrayList<String> al=new ArrayList<String>();
			while(true){
				String s=br.readLine();
				//WriteLog.println(s);
				if(s==null||s.equals("")){
					WriteLog.println(al);
					processHeader(al);
					break;
				}
					
				al.add(s);
			}
		} catch (IOException e) {
			//catch for complete while
                    System.out.println("error.....");
			WriteLog.println("error:"+e);
		}
              // System.out.println("http request completed");
               
               System.gc();
            }//end of run
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
	
}
