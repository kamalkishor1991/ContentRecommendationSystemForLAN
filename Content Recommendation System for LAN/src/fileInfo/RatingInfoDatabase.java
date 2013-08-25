package fileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import Log.WriteLog;

/**
 * Contain table of various users(Rating File table).
 * All Information will be saved as HashMap and HashSet
 * @author kamal
 *
 */
public class RatingInfoDatabase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,Map<String,Double>> map;//mac to map of String(String of File)---->Rating 
	private Map<String,HashSet<String>> file;//File String----> Set of mac address
	private Map<String,HashSet<File>> fileId;//Maping of key of file to real files
	private Map<String,Double> sum;//sum of File key
	/**
	 * Create new empty Database.
	 */
	public RatingInfoDatabase(){
		map=new HashMap<String,Map<String,Double>>();
		file=new HashMap<String,HashSet<String>>();
		fileId=new HashMap<String,HashSet<File>>();
		sum=new HashMap<String,Double>();
	}
	/**
	 * Read Database from InputStream
	 * @param in InputStream from which it read Objects
	 * @throws IOException 
	 * @throws ClassNotFoundException
	 */
	public RatingInfoDatabase(InputStream in) throws IOException, ClassNotFoundException{
		ObjectInputStream oi=new ObjectInputStream(in);
		map=(Map)(oi.readObject());
		file=(Map)(oi.readObject());
		fileId=(Map)(oi.readObject());
		sum=(Map)(oi.readObject());
		oi.close();
	}
	
	/**
	 * Write this Database into OutputStream
	 * @param out OutputStream 
	 * @throws IOException
	 */
	public void writeData(OutputStream out) throws IOException{
		ObjectOutputStream oo=new ObjectOutputStream(out);
		oo.writeObject(map);
		oo.writeObject(file);
		oo.writeObject(fileId);
		oo.writeObject(sum);
		oo.close();
	}
	/**
	 * 
	 * @param mac Mac Address 
	 * @param f File 
	 * @param rating Rating of that file.
	 */
	public void addData(String mac,File f,double rating){
		if(map.containsKey(mac)){
			Map<String, Double> hs=map.get(mac);
			String key=this.createKeyFromFile(f);
			if(hs.containsKey(key)){
				double dd=hs.remove(key);
				hs.put(key, rating);
				WriteLog.println("sum="+sum);
				if(sum.containsKey(key)){
					double ss=sum.remove(key);
					ss-=dd;
					ss+=rating;
					sum.put(key, ss);
				}
				else{
					sum.put(key, rating);
				}
			}
			else{
				hs.put(this.createKeyFromFile(f), rating);
				sum.put(key, rating);
			}
			
		}
		else{
			HashMap<String,Double> hm=new HashMap<String,Double>();
			hm.put(this.createKeyFromFile(f), rating);
			map.put(mac, hm);
		}
		//fill file Map
		String key=this.createKeyFromFile(f);
		if(file.containsKey(key)){
			HashSet<String> hs=file.get(key);
			hs.add(mac);
		}
		else{
			HashSet<String> hs=new HashSet<String>();
			hs.add(mac);
			file.put(key, hs);
		}
		
		//fileId
		if(fileId.containsKey(key)){
			HashSet<File> hs=fileId.get(key);
			hs.add(f);
		}
		else{
			HashSet<File> hs=new HashSet<File>();
			hs.add(f);
			fileId.put(key, hs);
		}
		
	}
	/**
	 * size 2 array with first elements AV rating and second element no of user rated
	 * @param f File
	 * @return size 2 array
	 */
	public double[] getAv(File f){
		String key=this.createKeyFromFile(f);
		if(sum.containsKey(key)){
			double ss=sum.get(key);
			int no=file.get(key).size();
			return new double[]{ss,no};
		}
		else{
			return new double[]{0,0};
		}
	}
	/**
	 * Create key From File.
	 * @param f File
	 * @return key
	 */
	private String createKeyFromFile(File f){
		String ss=f.isFile()?(""+f.getName().toLowerCase()+":"+(f.length())):(f.getName().toLowerCase()+"::");//folder name::no of child
		return ss;
	}
	
}
