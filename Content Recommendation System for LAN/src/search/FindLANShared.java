package search;

import Log.WriteLog;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;


/**
 * Find shared list so may take time.
 * 
 * @author kamal
 *
 */
public class FindLANShared implements LanUserList {
	/**
         * Time out in millisecond.
     */
   private long timeout=15000;
	public FindLANShared(){	
	}
	/**
	 * 
	 * @return Timeout of Method{@link #getAllUsers()}
	 */
	public long getTimeout(){
		return timeout;
	}
	/**
	 * 
	 * @param timeout Sets Timeout of method {@link #getAllUsers()}
	 */
	public void setTimeout(long timeout){
		this.timeout=timeout;
	}
	
	/**
	 * Return after {@link #TIME_OUT} Time.
	 */
	@Override
	public ArrayList<NetFiles> getAllUsers() {
		ArrayList<NetFiles> r=new ArrayList<NetFiles>();
		int ptimeout=2000;
		try {
			InetAddress myip=InetAddress.getLocalHost();
			String ss=myip.getHostAddress();
			
			String stg=ss.substring(0,ss.lastIndexOf('.')+1);
			WriteLog.println(stg);
			GetPingInfo gt[]=new GetPingInfo[254];
			for(int i=1;i<255;i++){
				gt[i-1]=new GetPingInfo(stg+i,ptimeout);
				
			}
			Thread.sleep(ptimeout);
			ArrayList<InetAddress> al=new ArrayList<InetAddress>();
			
			for(int i=0;i<254;i++){
				if(gt[i].isReachable()){
					al.add(gt[i].getInetAddress());
				}
			}
			
			WriteLog.println("ping result="+al);
			//ping complete.
			
		GetSharedList gl[]=new GetSharedList[al.size()];
		
		for(int i=0;i<gl.length;i++){
			gl[i]=new GetSharedList(al.get(i).getHostAddress());
			
		}
		Thread.sleep(timeout);//wait for time out sec.
		for(int i=0;i<gl.length;i++){
			if(gl[i].isComplete()){
				r.add(new NetFiles(gl[i].getList(),al.get(i)));
			}
		}	
		} catch (Exception e) {
			
			//e.printStackTrace();
		}
		return r;
	}
        
	private class  GetPingInfo implements Runnable{
		
		private String ip;
		private int timeout;
		private boolean isR=false;
		public GetPingInfo(String ip,int timeout){
			this.ip=ip;
			this.timeout=timeout;
			Thread t=new Thread(this);
			t.start();
		}
		
		public boolean isReachable(){
			
			return isR;
		}
		
		public InetAddress getInetAddress() throws UnknownHostException{
			return InetAddress.getByName(ip);
		}
		
		@Override
		public void run() {
			try {
				isR=InetAddress.getByName(ip).isReachable(timeout);
			} catch (IOException e) {
				isR=false;
			}
			
		}
		
	}


	private class  GetSharedList implements Runnable{
		
		private String ip;
		
		private boolean isR=false;
		private File f[];
		public GetSharedList(String ip){
			this.ip=ip;
			
			Thread t=new Thread(this);
			t.start();
		}
		
		public File[] getList(){
			
			return f;
		}
		/**
                 * successful then true.
                 * @return 
                 */
		private boolean isComplete(){
			return isR;
		}
		
		
		@Override
		public void run() {
			SmbFile file = null;
			try {
				file = new SmbFile( "smb://"+ip );
			} catch (MalformedURLException e) {
			}//ip address of computer
			
			SmbFile sf[] = null;
			try {
                                
				sf = file.listFiles();
                                WriteLog.println("List="+sf);
			} catch (SmbException e) {
                            //return with fail.
                           //fail to retreave list 
                            return;     
			}//this line may take time;
			f=new File[sf.length];
			for(int i=0;i<sf.length;i++){
				WriteLog.println("shared list="+sf[i].getUncPath());
				f[i]=new File(sf[i].getUncPath());
			}
			isR=true;
			
		}
		
	}

	
	
}
