package search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fileInfo.FileInfoDatabase;

import webServer.CompareFiles;
import webServer.FileType;

import Log.WriteLog;
/**
 * Provide search based recommendation.
 * Default timeout is 10 min.
 * Default No of recommendation=15;
 * Search LAN Continuously and rank search result based on {@link CompareFiles#sort(List, FileInfoDatabase)}
 *  
 * @author kamal
 *
 */
public class SearchBasedRecommendation implements Recommendation {
	
	
	/**
	 * Timeout for repeating search
	 */
	private long timeout = 10*60000;
	private int noOfRec=15;
	private LanUserList lan;
	private SearchKeyword sk;
	private FileInfoDatabase database;
	private Thread tt;
	private SearchLAN sl;
	public SearchBasedRecommendation(LanUserList lan,SearchKeyword sk,FileInfoDatabase database){
		this.lan=lan;
		this.sk=sk;
		this.database=database;
		sl=new SearchLAN();
		tt=new Thread(sl);
	}
	/**
	 * Start searching in new Thread.
	 */
	public void start(){
		tt.start();
	}
	/**
	 * Stop searching
	 */
	public void stop(){
		sl.stop();
	}
	
	@Override
	public List<File> getRecommendation() {
		
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<Math.min(this.noOfRec, r.size());i++){
			l.add(r.get(i));
		}
		return l;
		
	}
	
	@Override
	public List<File> getRecommendedFiles() {
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<r.size();i++){
			File f=r.get(i);
			if(f.isFile()){
				l.add(f);
				if(l.size()==this.noOfRec){
					break;
				}
			}
			
		}
		
		return l;
	}
	@Override
	public List<File> getRecommendedFolders() {
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<r.size();i++){
			File f=r.get(i);
			if(f.isDirectory()){
				l.add(f);
				if(l.size()==this.noOfRec){
					break;
				}
			}
			
		}
		return l;
	}
	
	@Override
	public List<File> getRecommendedVideos() {
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<r.size();i++){
			File f=r.get(i);
			if(f.isDirectory())continue;
			String ext=f.getName();
			if(ext.lastIndexOf(".")==-1)continue;
			ext=ext.substring(ext.lastIndexOf(".")+1);
			if(FileType.getType(ext).toLowerCase().contains("video")){
				l.add(f);
				if(l.size()==this.noOfRec){
					break;
				}
			}
			
		}
		return l;
		
	}
	@Override
	public List<File> getRecommendedEbooks() {
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<r.size();i++){
			File f=r.get(i);
			if(f.isDirectory())continue;
			String ext=f.getName();
			if(ext.lastIndexOf(".")==-1)continue;
			ext=ext.substring(ext.lastIndexOf(".")+1);
			if(FileType.getType(ext).toLowerCase().contains("pdf")){
				l.add(f);
				if(l.size()==this.noOfRec){
					break;
				}
			}
			
		}
		return l;
	}

	@Override
	public List<File> getRecommendedMusic() {
		List<File> r=sl.getList();
		List<File> l=new ArrayList<File>(Math.min(this.noOfRec,r.size()));
		for(int i=0;i<r.size();i++){
			File f=r.get(i);
			if(f.isDirectory())continue;
			String ext=f.getName();
			if(ext.lastIndexOf(".")==-1)continue;
			ext=ext.substring(ext.lastIndexOf(".")+1);
			if(FileType.getType(ext).toLowerCase().contains("audio")){
				l.add(f);
				if(l.size()==this.noOfRec){
					break;
				}
			}
			
		}
		return l;
	}
	
	/**
	 * Set Timeout for Searching LAN for new Recommendation
	 * @param timeout Timeout in Millisecond.
	 */
	public void setTimeOut(long timeout){
		this.timeout=timeout;
	}
	/**
	 * 
	 * @return Current Timeout to repeat search.
	 */
	public long getTimeOut(){
		return this.getTimeOut();
	}
	/**
	 * Sets How Many recommendation returned by Method {@link #getRecommendation()}
	 * @param no No of Recommendation.
	 */
	public void setNoOfRecommedation(int no){
		this.noOfRec=no;
	}
	/**
	 * 
	 * @return No of recommendation returned by Method {@link #getRecommendation()}
	 */
	public int getNoOfRecommendation(){
		return this.noOfRec;
	}
	
	private class SearchLAN implements Runnable{
		List<File> l;
		boolean stop=false;
		
		
		public List<File> getList(){
			return l;
		}

		public void stop(){
			this.stop=true;
		}
		@Override
		public void run() {
			while(!stop){
					
				
				ArrayList<NetFiles> al=lan.getAllUsers();
				StringBuilder sb=new StringBuilder();
				for(NetFiles n:al){
					File f[]=n.getFiles();
					for(File ff:f){
						if(this.isValidFile(ff.getAbsolutePath()))
							sb.append(ff.getAbsolutePath()+":");
					}
				}
				try {
					//System.out.println("Searching=:"+sb);
					List<File> ll=sk.search(sb.toString()+":");
					//System.out.println("searched="+ll);
					ll=CompareFiles.sort(ll, database);
					
					l=ll;
					
					//System.out.println("Result=:"+l);
				} catch (Exception e) {
					WriteLog.println("error:=:"+e);
				}
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e1) {
				}
			}
			
		}
		
		
		private boolean isValidFile(String s){
	        	s=s.toLowerCase();
	            return (!(s.contains("$")||s.contains("users")));//can not access address which contain "$"
	        }
		
	}

	

	
	

}
