package webServer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import fileInfo.FileInfo;
import fileInfo.FileInfoDatabase;
/**
 * Provide static methods to sort Files according to FileInfoDatabase
 * @author kamal
 *
 */
public class CompareFiles implements Comparable<CompareFiles>{
	private File f;
	private FileInfoDatabase database;
	public CompareFiles(File f,FileInfoDatabase database){
		this.f=f;
		this.database=database;
	}
	@Override
	public int compareTo(CompareFiles cf) {
		Long noOfDownloads1=(long) 0;
		Long noOfDownloads2=(long)0;
		double rating1=0;
		double rating2=0;
		double avRating1=0;
		double avRating2=0;
		long noOfUsers1=0;
		long noOfUsers2=0;
		try {
			FileInfo fi=database.getFile(f);
			noOfDownloads1=fi.getDownloads();
			rating1=fi.getRating();
			avRating1=fi.getAvRating();
			noOfUsers1=fi.getNoOfUser();
			
		} catch (FileNotFoundException e) {
		}
		try {
			FileInfo fi=database.getFile(cf.getFile());
			rating2=fi.getRating();
			noOfDownloads2 = fi.getDownloads();
			avRating2=fi.getAvRating();
			noOfUsers2=fi.getNoOfUser();
		} catch (FileNotFoundException e) {	
		}
		
		double v1=0.25*noOfDownloads1+.25*rating1+.25*avRating1+.25*noOfUsers1;
		double v2=0.25*noOfDownloads2+.25*rating2+.25*avRating2+.25*noOfUsers2;
		return Double.compare(v2, v1);
	}
	
	public File getFile(){
		return f;
	}
	
	public static void sort(File f[],FileInfoDatabase database){
		CompareFiles cf[]=new CompareFiles[f.length];
		for(int i=0;i<f.length;i++){
			cf[i]=new CompareFiles(f[i],database);
		}
		Arrays.sort(cf);
		File r[]=new File[f.length];
		for(int i=0;i<f.length;i++){
			r[i]=cf[i].getFile();
		}
		System.arraycopy(r, 0, f, 0, r.length);
		
		
	}
	/**
	 * 
	 * @param ff
	 * @param database
	 * @return Sorted List
	 */
	public static List<File> sort(List<File> ff,FileInfoDatabase database){
		CompareFiles cf[]=new CompareFiles[ff.size()];
		Iterator<File> it=ff.iterator();
		for(int i=0;i<ff.size();i++){
			cf[i]=new CompareFiles(it.next(),database);
		}
		Arrays.sort(cf);
		LinkedList<File> l=new LinkedList<File>();
		for(int i=0;i<cf.length;i++){
			l.add(cf[i].getFile());
		}
		return l;
	}
	

}
