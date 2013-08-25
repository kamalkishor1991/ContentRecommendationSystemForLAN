
import Log.Data;
import Log.WriteLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.JOptionPane;

import fileInfo.FileInfoDatabase;
import fileInfo.SynchronizeFileInfoDatabase;
import fileInfo.SynchronizedResponse;
import fileInfo.WriteSetting;
import gui.MainWindow;

import search.FileSearchSingleSystem;
import search.FindLANShared;
import search.SearchBasedRecommendation;
import search.SearchKeyword;
import jcifs.smb.SmbFile;
import webServer.FileType;
import webServer.ProcessHttp;
import webServer.WebServer;

public class MainClass implements Runnable {
	public static void main(String args[]){
		
		try {
			start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Some Error occured "+e);
		}
		
	}
    
	public static FileInfoDatabase fileInfoTesting() throws ClassNotFoundException, IOException{
		FileInfoDatabase f=null;
		try{
		f=new FileInfoDatabase(new File("setting/database.txt"));
		}catch(Exception e){
			f=new FileInfoDatabase();
		}
		File ff=new File("D:\\music");
		File a[]=ff.listFiles();
		for(int j=0;j<4;j++){
		for(int i=0;i<a.length;i++){
			if(a[i].isFile())f.addDownload(a[i]);
		}
		}
		for(int i=0;i<a.length;i++){
			
				try {
					System.out.println(f.getFile(a[i]));
				} catch (FileNotFoundException e) {
					//System.out.println("Not found"+","+a[i]);
				}
		}
		
		for(int j=0;j<4;j++){
			for(int i=0;i<a.length;i++){
				f.addDownload(a[i]);
			}
		}
		f.addDownload(new File("D:\\music\\IHaveADream.mp3"));
		for(int i=0;i<a.length;i++){
			
				try {
					System.out.println(f.getFile(a[i]));
				} catch (FileNotFoundException e) {
					System.out.println("Not found"+","+a[i]);
				}
			
		}
		return f;
		
	}
	
	
	
	public static void start() throws Exception{
		 	WriteLog.createLogFile();
		 	try{
		 		Data.start();
		 	}catch(java.net.BindException e){
		 		JOptionPane.showMessageDialog(null, "Already running.....");
		 		return;
		 	}
            FileType.setFileType();
            SearchKeyword sk=new FileSearchSingleSystem();
            FileInfoDatabase f=null;
            WriteSetting ws=null;
			try {
				f = new FileInfoDatabase(new File("setting/database.txt"));
				f.start();
				ws=new WriteSetting(f,new File("setting/database.txt"));
				ws.start();
			} catch (Exception e) {
				
				f=new FileInfoDatabase();
				f.start();
				ws=new WriteSetting(f,new File("setting/database.txt"));
				ws.start();
				//e.printStackTrace();
			};
			//System.out.println(f);
			SynchronizedResponse sr=new SynchronizedResponse(f);
			sr.start();
			SynchronizeFileInfoDatabase info=new SynchronizeFileInfoDatabase();
			info.syncDatabase(f);
			FindLANShared lul=new FindLANShared();

			SearchBasedRecommendation sbr=new SearchBasedRecommendation(lul,sk,f);
			sbr.start();
			
            WebServer wss=new WebServer(80,new ProcessHttp(sk,f,sbr));
            wss.start();
            
            ShutDown sd=new ShutDown(f);
            Thread t=new Thread(sd);
            Runtime.getRuntime().addShutdownHook(t);
            MainWindow mw=new MainWindow(lul,sbr);
            mw.repaint();
    		//mw.setVisible(true);
            //dist system start
            
        }
	
	/*public static void replyDemo() throws Exception, IOException{
		Socket soc=new Socket("google.com",80);
		PrintStream out=new PrintStream(soc.getOutputStream());
		out.println("GET / HTTP/1.1");
		out.println();
		BufferedReader br=new BufferedReader(new InputStreamReader(soc.getInputStream()));
		
		while(true){
			WriteLog.println(br.readLine());
		}
	}
	*/
	
	public static void testDistSystem() throws SocketException, Exception{
		//jcifs.Config.registerSmbURLHandler();
		//jcifs.Config.registerSmbURLHandler();
		//PingInfo pi=new PingInfo(45895,45896);
		//WriteLog.println(pi.ping(3000));
		//pi.stop();
		SmbFile file = new SmbFile( "smb://kamal-VAIO/");
	//	File file=new File("//192.168.1.164//music");

		WriteLog.println(file);
        long t1 = System.currentTimeMillis();
        String[] files = file.list();
      //  File[] files = file.listFiles();
        long t2 = System.currentTimeMillis() - t1;

        for( int i = 0; i < files.length; i++ ) {
            WriteLog.println( " " + files[i] );
        }
        WriteLog.println();
        WriteLog.println( files.length + " files in " + t2 + "ms" );
		
		
        
        
		//pi.stop();
		
		//WriteLog.println(System.getenv());
		

		/*
		DatagramSocket dd=new DatagramSocket();
		byte[] b=new byte[10];
		DatagramPacket dp=new DatagramPacket(b,b.length, InetAddress.getByName("192.168.1.255"),2000);
		for(int i=0;i<b.length;i++){
			b[i]=(byte)(int)(Math.random()*128);
		}
		try {
			dd.send(dp);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void run() {
		
	}
        
}
