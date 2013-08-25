package fileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import distSystem.NetworkSystem;

import Log.Data;

public class SynchronizeFileInfoDatabase {

	private final int  port=45678;//This port must be same as SynchronizedResponse
	
	public  void syncDatabase(FileInfoDatabase fd) throws SocketException, UnknownHostException{
		LinkedList<NetworkSystem> ll=Data.PI.ping(Data.PING_TIMEOUT);
		ArrayList<HashMap<String,FileInfo>> al=new ArrayList<HashMap<String,FileInfo>>(ll.size());
		Iterator<NetworkSystem> it=ll.iterator();
		for(int i=0;i<ll.size();i++){
			NetworkSystem ns=it.next();
			try {
				Socket soc=new Socket(ns.getAddress(),port);
				InputStream is=soc.getInputStream();
				ObjectInputStream obj=new ObjectInputStream(is);
				al.add((HashMap<String,FileInfo>)obj.readObject());
				soc.close();
			} catch (IOException e) {
				
			} catch (ClassNotFoundException e) {
				//fake reply
				e.printStackTrace();
			}	
		}
		
		createMap(fd,al);
		
		
	}
	
	private void createMap(FileInfoDatabase fi,ArrayList<HashMap<String,FileInfo>> al){

		for(HashMap<String,FileInfo> hm:al){
			Set<Entry<String,FileInfo>> set=hm.entrySet();
			fi.syncHashSet(set);
		}	
	}
	
	
}
