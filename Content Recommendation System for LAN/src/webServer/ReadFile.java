package webServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
/**
 * This class is used to read File from root folder {@link #root}
 * This class provides security to system.
 * 
 * 
 * @author kamal
 *
 */
public class ReadFile {
	
	/**
	 * Root folder index.html should present hare.
	 */
	public static String root="site";//"C:\\Users\\Kamal\\Google Drive\\data\\Project\\Report\\Site\\site";//"C:\\Users\\kamal\\Google Drive\\Data\\Project\\Report";
	private FileInputStream fs;
	private File f;
	private String ext;
	/**
         * 
         * Read file with base folder Root.
         * @param file
         * @throws FileNotFoundException 
         */
	public ReadFile(String file) throws FileNotFoundException{
		
		if(file.equals("/"))
			file="/index.html";
		ext=file.substring(file.lastIndexOf('.')+1);
		f=new File(root+file);
		fs=new FileInputStream(f);
		
		
	}
        
        public ReadFile(File f) throws FileNotFoundException{
            this.f=f;
            ext=f.getName().substring(f.getName().lastIndexOf('.')+1);
            fs=new FileInputStream(f);
        }
	
	
	
	public void processPage(){
		
	}
	
	
	public String getContentType(){
		//WriteLog.println("ext:"+ext);
		return FileType.getType(ext);
	}
	/**
	 * 
	 * @return File length.
	 */
	public long getSize(){
		return f.length();
	}
	/**
	 * 
	 * @return read single in from file.
	 * @throws IOException
	 */
	public int read() throws IOException{
		
		return fs.read();
	}
	/**
	 * 
	 * @param b byte array
	 * @return no of bytes read by method.
	 * @throws IOException
	 */
	public int readByteArray(byte b[]) throws IOException{
        	return fs.read(b);
	}
	/**
	 * 
	 * @return opened File
	 */
	public File getFile(){
		return f;
	}
	/**
	 * 
	 * @return InputStream of File.
	 */
	public InputStream getInputStream(){
		return fs;
	}
    /**
     *  Close InputStream.   
     * @throws IOException
     */
    public void close() throws IOException{
            fs.close();
    }
	
}
