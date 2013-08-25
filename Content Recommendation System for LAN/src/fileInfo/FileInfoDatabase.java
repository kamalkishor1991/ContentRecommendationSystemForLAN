package fileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Log.WriteLog;
import utility.Utility;

/**
 * This class will responsible for getting file information like rating ,number of downloads etc. 
 * Implemented Using HashMap and RatingInfoDatabase.
 * HashMap is used as String to FileInfo.
 * where String represent key for File.
 * Protected Method {@link #createKeyFromFile(File)}create key from File  
	Thread is running to get new File information which accept UDP packets.
 * @author kamal
 */
public class FileInfoDatabase implements Serializable,Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String,FileInfo> hs;
	private boolean isChanged=false;
	private final int portSend=34564;
	private Thread t;
	private boolean isStop;
	private RatingInfoDatabase ratingInfo;
	/**
	 * Create new HashMap{@link HashMap}
	 */
	public FileInfoDatabase(){
		hs=new HashMap<String,FileInfo>();
		ratingInfo=new RatingInfoDatabase();
		// this can be used to sycn hs=Collections.synchronizedMap(hs);
		t=new Thread(this);
		
	}
	
	/**
	 * Stop Thread
	 * @throws IOException 
	 */
	public void stop() throws IOException{
		isStop=true;
		byte send[]={0,0};
		DatagramPacket dp=new DatagramPacket(send,send.length,InetAddress.getLocalHost(),portSend);
		DatagramSocket ds=new DatagramSocket();
		ds.send(dp);
		ds.close();
	}
	
	/**
	 * Start Thread
	 */
	public void start(){
		t.start();
	}
	/**
	 * Write Hash map into output stream 
	 * @param out This output Stream in output stream of any network or FileOutputStream etc.
	 * @throws IOException
	 */
	public synchronized void writeHashMap(OutputStream out) throws IOException{
		ObjectOutputStream oo=new ObjectOutputStream(out);
		oo.writeObject(hs);
		oo.close();
	}
	/**
	 * Sets rating of file
	 * @param f File
	 * @param rating Rating of File.
	 */
	public synchronized void setRating(File f,int rating){
		String key=this.createKeyFromFile(f);
		if(hs.containsKey(key)){
			FileInfo value=hs.get(key);
			value.setMyRating(rating);
			
		}
		else{
			FileInfo value=new FileInfo(key);
			value.setMyRating(rating);
			hs.put(key, value);
		}
		isChanged=true;
	}
	/**
	 * Update rating table
	 * Called every time when any user rate any item in Lan.
	 * @param f
	 * @param rating
	 * @param mac
	 */
	private synchronized void updateRatingTable(File f,int rating,String mac){
		//TO Do rating table creation and AV rating calculation
		/*if(ratingInfo==null)
			ratingInfo=new RatingInfoDatabase();
			*/
		ratingInfo.addData(mac, f, rating);
		isChanged=true;
	}
	
	
	/**
	 * Sync HashMap with specified Set 
	 */
	public synchronized void syncHashSet(Set<Map.Entry<String,FileInfo>> set){
		Iterator<Entry<String, FileInfo>> sit=set.iterator();
		while(sit.hasNext()){
			Entry<String,FileInfo> et=sit.next();
			String key=et.getKey();
			if(hs.containsKey(key)){
				FileInfo fi1=hs.get(key);
				FileInfo fi2=et.getValue();
				fi1.syncValues(fi2);
			}
			else{
				hs.put(key, et.getValue());
			}
			
		}
	}
	/**
	 * Read database from specified file 
	 * @param f File which contain database
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public FileInfoDatabase(File f) throws IOException, ClassNotFoundException,Exception{
		FileInputStream fs=new FileInputStream(f);
		ObjectInputStream os=new ObjectInputStream(fs);
		Object obj=(os.readObject());
		try{
			if(obj instanceof HashMap)
				hs=(HashMap<String,FileInfo>)obj;
				ratingInfo=new RatingInfoDatabase(fs);	
		}
		catch(ClassCastException e){
			throw new Exception();
		}
		t=new Thread(this);
		os.close();
		fs.close();
		
	}
	
	public synchronized void writeFile(File f) throws IOException{
		if(!isChanged)return;
		
		FileOutputStream fs=new FileOutputStream(f);
		ObjectOutputStream oo=new ObjectOutputStream(fs);
		oo.writeObject(hs);
		ratingInfo.writeData(fs);
		fs.close();
		oo.close();
		isChanged=false;
	}
	/**
	 * Provide Key From File
	 * @param f File
	 * @return Key
	 */
	protected String createKeyFromFile(File f){
		String ss=f.isFile()?(""+f.getName().toLowerCase()+":"+(f.length())):(f.getName().toLowerCase()+"::");//folder name::no of child
		return ss;
	}
	
	/**
	 * Create new file if not exist
	 * else increment download count.
	 * @param f
	 */
	public  synchronized void addDownload(File f){
		String ss=this.createKeyFromFile(f);
		boolean b=hs.containsKey(ss);
		if(b){
			FileInfo fi=hs.get(ss);
			fi.incrementDownload();
		}
		else{
			hs.put(ss, new FileInfo(ss,1));
		}
		isChanged=true;
		
	}
	/**
	 * 
	 * @param f File
	 * @return increment download count and also return FileInfo 
	 */
	public synchronized FileInfo incDownload(File f){
		String s=this.createKeyFromFile(f);
		boolean b=hs.containsKey(s);
		isChanged=true;
		if(b){
			FileInfo fi=hs.get(s);
			fi.incrementDownload();
			return fi;
		}
		else{
			FileInfo fi=new FileInfo(s,1);
			hs.put(s, fi);
			return fi;
		}
		
	}
	
	/**
	 * 
	 * @param f
	 * @return FileInfo with that file.
	 * @throws FileNotFoundException if File not found.
	 */
	public synchronized FileInfo getFile(File f) throws FileNotFoundException{
		String key=this.createKeyFromFile(f);//
		if(hs.containsKey(key)){
			
			FileInfo fi=hs.get(key);
			double av[]=ratingInfo.getAv(f);
			fi.setAvRating(av[0]);
			fi.setNoOfUser((long)av[1]);
			
			return fi;
		}
		else{
			FileInfo fr=new FileInfo();
			double av[]=ratingInfo.getAv(f);
			fr.setAvRating(av[0]);
			fr.setNoOfUser((long)av[1]);
			return fr;
		}
	}
	
	
	
	
	/**
	 * Send a UDP broadcast to increment key.
	 * @throws IOException 
	 */
	public void notifyAllSystems(File file) throws IOException{
		byte send[];
		BroadCastInfo b=new BroadCastInfo(file);
		send=b.getBytes();
		DatagramPacket dp=new DatagramPacket(send,send.length,InetAddress.getByName("255.255.255.255"),portSend);
		DatagramSocket ds=new DatagramSocket();
		ds.send(dp);
		ds.close();
	}

	/**
	 * Send UDP packet broadcast to Inform rating to everyone.
	 * @param f File
	 * @param rating rating of file
	 * @throws IOException
	 */
	public void notifyRating(File f,int rating) throws IOException{
		byte send[];
		BroadCastInfo b=new BroadCastInfo(f,rating);
		send=b.getBytes();
		DatagramPacket dp=new DatagramPacket(send,send.length,InetAddress.getByName("255.255.255.255"),portSend);
		DatagramSocket ds=new DatagramSocket();
		ds.send(dp);
		ds.close();
	}
	
	
	private class BroadCastInfo{
		
		private File file;
		private byte b[];
		private int rating=0; 
		BroadCastInfo(File file){
			this.file=(file);
			String s=file.getAbsolutePath();
			b=s.getBytes();
		}
		
		public BroadCastInfo(byte b[]){
			this.b=b;
			String s=new String(b);
			if(s.contains(":")){
				String ss[]=s.split(":");
				file=new File(ss[0]);
				rating=Integer.parseInt(ss[1]);
				return;
			}
			file=new File(s);
			
		}
		
		public boolean isRatingInfo(){
			return rating!=0;
		}
		
		public BroadCastInfo(File f, int rating) {
			this.file=(f);
			String s=file.getAbsolutePath();
			s+=":"+rating;
			b=s.getBytes();
		}

		public byte[] getBytes(){
			return b;
		}
		
		public File getFile() {
			return this.file;
		}

		public int getRating() {
			
			return rating;
		}
		
	}

	@Override
	public void run() {
		
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(portSend);
			while(true){
				
				try {
					byte b[]=new byte[8000];//max length of packet is assumed 8000 bytes
					DatagramPacket dp=new DatagramPacket(b,b.length);
					ds.receive(dp);
					if(isStop){
						break;
					}
					WriteLog.println("Packet Received Rating /Opened");
					byte bb[]=new byte[dp.getLength()];
					System.arraycopy(dp.getData(), dp.getOffset(), bb, 0, bb.length);
				
					BroadCastInfo bi=new BroadCastInfo(bb);
					if(!bi.isRatingInfo()){
						WriteLog.println("Opened Packet Received"+bi.getFile());
						addDownload(bi.getFile());
					}
					else{
						//WriteLog.println("rating Packet Received File-"+bi.getFile()+", rating =:"+bi.getRating()+","+dp.getAddress()+","+InetAddress.getLocalHost());
						if(Utility.getMac(dp.getAddress()).equals(Utility.getMac(InetAddress.getLocalHost()))){
							//WriteLog.println("rated by me"+bi.getFile()+","+bi.getRating());
							setRating(bi.getFile(),bi.getRating());
						}
						updateRatingTable(bi.getFile(),bi.getRating(),Utility.getMac(dp.getAddress()));
					}
				} catch (IOException e) {	
					
				}
			}
			} catch (SocketException e1) {
				
			}
			ds.close();
			
	}//end run
}
