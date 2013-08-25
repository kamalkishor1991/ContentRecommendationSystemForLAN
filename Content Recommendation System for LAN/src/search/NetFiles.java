package search;

import java.io.File;
import java.net.InetAddress;
/**
 * Information of shared Files.
 * @author kamal
 *
 */
public class NetFiles{
	private InetAddress add;
	private File smb[];
	/**
	 * Create NetFiles with Files and address 
	 * @param f Files
	 * @param add Address of computer where this File Located
	 */
	public NetFiles(File f[],InetAddress add){
		this.smb=f;
		this.add=add;
	}
	/**
	 * 
	 * @return Address of computer where {@link #getFiles()} Located
	 */
	public InetAddress getAdd() {
		return add;
	}
	/**
	 * @param add Sets address
	 */
	public void setAdd(InetAddress add) {
		this.add = add;
	}
	
	/**
	 * 
	 * @return array of Files of Computer {@link #getAdd()} 
	 */
	public File[] getFiles() {
		return smb;
	}
	/**
	 * 
	 * @param smb Sets Files of remote Computer.
	 */
	public void setFiles(File smb[]) {
		this.smb = smb;
	}
        @Override
    public String toString(){
            String s="[";
            for(int i=0;i<smb.length;i++){
                s+=smb[i].toString()+" ,";
            }
            return s+"]";
    }
}
