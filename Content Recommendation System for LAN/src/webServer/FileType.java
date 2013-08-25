package webServer;

import Log.WriteLog;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * Read INTERNET MEDIA TYPE from file within setting folder in filetype.txt 
 * More extension can be added in this File.
 * @author kamal
 *
 */
public class FileType {
	private static ArrayList<String > a;
	private static ArrayList<String> b;
	/**
	 * Must be called in stating of application or server.
	 * {@link #getType(String)} can be called after it.
	 * @throws IOException
	 */
	public static void setFileType() throws IOException{
		WriteLog.println("set file type called");
		BufferedReader bis=new BufferedReader(new InputStreamReader(new FileInputStream("setting\\filetype.txt")));
		a=new ArrayList<String>();
		b=new ArrayList<String>();
		String line="";
		while((line=bis.readLine())!=null){
			String s[]=line.split(" ");
			a.add(s[0]);
			b.add(s[1]);
		
		}
		bis.close();
		
	}
	
	/***
	 * Return INTERNET MEDIA TYPE of extension
	 *return "*\*" if not found 
	 */
	public static String getType(String ext){
		WriteLog.print("media type:"+","+ext);
                
          for(int i=0;i<a.size();i++){
                    
			if(ext.equals(a.get(i))){
				WriteLog.println(b.get(i));
                
				return b.get(i);
			}
		}
        WriteLog.println(" Internet media type ext not found Not found");
		return "*\\*";
	}
	
	
	
	
}
