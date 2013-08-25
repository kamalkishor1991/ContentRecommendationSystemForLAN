package Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReadWriteFile {
	private File listOfFiles[];
	private File folder;
	
	
	/**
	 * open any folder.
	 * @param folder make a file of folder to be opened.
	 */
	public ReadWriteFile(File folder){
		this.folder=folder;
		listOfFiles=folder.listFiles();
	}
	public ReadWriteFile(String  fol){
		this.folder=new File(fol);
		listOfFiles=folder.listFiles();
	}
	/**
	 * 
	 * @return return all name of opened folder.
	 */
	public String[] getAllName(){
	  	  return folder.list();
	}
	/**
	 * 
	 * @return only Files.
	 */
	public String[] getAllFilesName(){
		int l=0;
			for (int i = 0; i < listOfFiles.length; i++) {
      		if (listOfFiles[i].isFile()) {
      			l++;
           		} 
           			
			}
			
      		String s[]=new String[l];
      		int j=0;
      		for (int ii = 0; ii < listOfFiles.length; ii++) {
      		if (listOfFiles[ii].isFile()) {
      			s[j]=listOfFiles[ii].getName();
      			j++;
           		} 
  	  		}
  		  	return s;
	}
	/**
	 * 
	 * @return all files in dir.
	 */
	public File[] getAllFiles(){
		return this.listOfFiles;
	}
	/**
	 * 
	 * @return only directory.
	 */
	public String[] getAllDirectory() throws NullPointerException{
		int l=0;
			for (int ii = 0; ii < listOfFiles.length; ii++) {
      		if (listOfFiles[ii].isDirectory()) {
      			l++;
           		} 
			}
			
      		String s[]=new String[l];
      		int j=0;
      		for (int i = 0; i < listOfFiles.length; i++) {
      		if (listOfFiles[i].isDirectory()) {
      			s[j]=listOfFiles[i].getName();
      			j++;
           		} 
  	  		}
  		  	return s;
	}
	/**
	 * 
	 * @param s name of File.
	 * @return true if file is deleted.
	 */
	public boolean deleteFile(String s){
		for(int i=0;i<listOfFiles.length;i++){
			if(listOfFiles[i].getName().equals(s)){
				listOfFiles[i].delete();
				return  true;
			}
		}
		return false;
	}
	public boolean renameFile(String s){
	for(int i=0;i<listOfFiles.length;i++){
			if(listOfFiles[i].getName().equals(s)){
				listOfFiles[i].renameTo(new File("s"));
				return  true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param fName folder name.
	 * @return true if deleted.
	 */
	public boolean deleteFolder(String fName){
		File list[];
		if(folder.getName().equals(fName)){
		list=folder.listFiles();
				for(int j=0;j<list.length;j++){
					if(list[j].isFile())
					list[j].delete();
					else if(list[j].isDirectory()){
					ReadWriteFile r=new ReadWriteFile(list[j]);
					r.deleteFolder(list[j].getName());
					}
				}
				folder.delete();
				return  true;
	
		}
		for(int i=0;i<listOfFiles.length;i++){
			if(listOfFiles[i].getName().equals(fName)){
				list=listOfFiles[i].listFiles();
				for(int j=0;j<list.length;j++){
					if(list[j].isFile())
					list[j].delete();
					else if(list[j].isDirectory()){
					ReadWriteFile r=new ReadWriteFile(list[j]);
					r.deleteFolder(list[j].getName());
					}
				}
				listOfFiles[i].delete();
				return  true;
			}
		}
		return false;
	}
	/**
	 * return InputStream of file we can read by this InputStream.
	 * @param fName
	 * @return InputStream
	 * @throws FileNotFoundException
	 */
	public InputStream getFile(String fName) throws FileNotFoundException{
		for(int i=0;i<listOfFiles.length;i++){
			if(listOfFiles[i].getName().equals(fName)){
				//listOfFiles[i].delete();
				FileInputStream fr=new FileInputStream(listOfFiles[i]);
				return fr;
			}
		}
		throw (new FileNotFoundException());
	}
	/**
	 * 
	 * @return size of opened folder in bytes.
	 */
	public long getFolderSize(){
		//File f[]=this.getAllFiles();
		long l=0;
		for(int i=0;i<listOfFiles.length;i++){
			l=l+listOfFiles[i].length();
		}
		return l;
	}
	
}

