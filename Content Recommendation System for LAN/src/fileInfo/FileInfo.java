package fileInfo;

import java.io.Serializable;
/**
 * Information of file. 
 * Like-No of downloads rating etc.
 * This class is Serializable so can be converted into Stream of bits. 
 * @author kamal
 *
 */
public class FileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * file name:size folder name::
	 * 
	 */
	private String file;
	/**
	 * For folder downloads represent no of times folder opened.
	 */
	private long downloads=0;
	private double myrating=0;
	private double avrating=0;
	private long noOfUser=0;
	/**
	 * Blank FileInfo downloads and myrating 0
	 */
	public FileInfo(){
		
	}
	/**
	 *@param file name:size ,folder name::noOfChild
	 */
	public FileInfo(String file){
		this.file=file.toLowerCase();
		this.downloads=1;
	}
	/**
	 * 
	 * @param file name:size folder name::
	 * @param downloads number of starting download.
	 */
	public FileInfo(String file,int downloads){
		this.file=file.toLowerCase();
		this.downloads=downloads;
	}
	
	public int hashCode(){
		return file.toLowerCase().hashCode();
	}
	
	public boolean equals(Object f){
		return this.file.toLowerCase().equals(f);
	}
	
	public double getRating() {
		return myrating;
	}
	protected void setMyRating(double rating) {
		this.myrating = rating;
	}
	public long getDownloads() {
		return downloads;
	}
	protected void setDownloads(long downloads) {
		this.downloads = downloads;
	}
	
	public boolean isFile(){
		return !file.contains("::");
	}
	
	/**
	 * download increment
	 */
	public void incrementDownload(){
		downloads++;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	
	public String toString(){
		return "file="+file+"  ,downloads="+downloads+", rating="+myrating;
	}
	
	public void syncValues(FileInfo fi) {
		this.downloads=Math.max(this.downloads, fi.getDownloads());
		
	}
	public double getAvRating() {
		return avrating;
	}
	public void setAvRating(double avrating) {
		this.avrating = avrating;
	}
	public long getNoOfUser() {
		return noOfUser;
	}
	public void setNoOfUser(long noOfUser) {
		this.noOfUser = noOfUser;
	}
	
}
