
package Log;

/**
 *Provide static methods for Logging.
 *Default logging is off.
 * @author kamal
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

public class WriteLog {
    
    public static boolean LOGING=false;
	public static String LOG_DIR="";
	private static SyncBufferedWriter bw;
	private static long i=0;
    private static long bufferSize=0;//1000;
       
	public static void close() throws IOException{
		bw.close();
	}
	public static String getDate(){
		Date d=new Date();
		String date=d.toString();
		date=date.substring(date.indexOf(" ")+1);
		
		String s=date.substring(date.lastIndexOf(" ")+1);
		//String s1=date.substring(0,date.indexOf(" ")+1);
		date=date.substring(0,date.indexOf(" ",date.indexOf(" ")+1));
		return date+" "+s;
		//System.out.println("s="+date+" "+ s);
	}
	public static void createLogFile(){

		LOG_DIR="Log\\";
		File f=new File(LOG_DIR);
		if(!f.isDirectory()){
			f.mkdir();
		}
		File fd=new File(LOG_DIR+"\\"+getDate());
		if(!fd.isDirectory()){
			fd.mkdir();
		}
		int i=0;
		File buff=null;
		while(true){
			
				buff=new File(LOG_DIR+"\\"+getDate(),getDate()+"_"+i+".log");
			if(!buff.isFile())
				break;
			i++;
		}
		try {
			
			OutputStream buffWriter=new FileOutputStream(buff);
			WriteLog.bw=new WriteLog().new SyncBufferedWriter(buffWriter);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//date=date.subString(date.substring(arg0))
	}
        
    /**
     * Print Logging Information in File.    
     * @param str LogInfo
     */
	public synchronized static void println(Object str){
       if(!LOGING)return;  
       try {
    	   	bw.write(""+new Date()+"  :");
			bw.write(str.toString());
			bw.write("\r\n");
			if(i==bufferSize){
				bw.flush();
				i=0;
			}
		} catch (IOException e) {
		
		}
           
		i++;	
     }
	 /**
     * Print Logging Information in File.    
     * @param str LogInfo
     */
	public static void print(Object str){
		if(!WriteLog.LOGING)return;
		try {
			bw.write(str.toString());
			if(i==bufferSize){
				bw.flush();
				i=0;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		i++;
	}
	 /**
     * New Line
     */    
    public static void println(){
        if(!WriteLog.LOGING)return;
    	try {
            bw.write("\r\n");
        } catch (IOException ex) {
            
        }
    }
    
    
    private class SyncBufferedWriter{
    	PrintStream out;
    	public SyncBufferedWriter(OutputStream br){

    		out=new PrintStream(br);
    	}
    	
    	public void flush() throws IOException {
			out.flush();
		}

		public void close() throws IOException {
			out.close();
		}

		public synchronized void write(String s) throws IOException{
    		//System.out.print(s);
    		synchronized(out){
    			out.print(s);
    		}
    	}
    	
    
    }

}
