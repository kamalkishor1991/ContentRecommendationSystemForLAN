package search;

import Log.WriteLog;
import java.io.File;
import java.util.LinkedList;
import java.util.Stack;
/**
 * Provide static Methods for Folder Searching.
 * Implemented {@see <a href="http://en.wikipedia.org/wiki/Depth-first_search">DFS</a>} for searching.
 * @author kamal
 *
 */
public class SearchFolder {
	/**
	 * search accurate word or folder.(complete name + extension)
	 * @param ff folder in which we have to search
	 * @param keyword Query
	 */
	public static LinkedList<File> search(File ff,String keyword){
		LinkedList<File> ll=new LinkedList<File>();
		
		Stack<File> ss=new Stack<File>();
		ss.push(ff);
		
		long ii=System.currentTimeMillis();
		while(!ss.isEmpty()){
			File c=ss.pop();
			//WriteLog.println(ii++);
			if(c.getName().equalsIgnoreCase(keyword)){
				ll.add(c);
			}
			if(c.isDirectory()){
				File af[]=c.listFiles();
				if(af==null){
					continue;
				}
				for(int i=0;i<af.length;i++){
					ss.push(af[i]);
				}	
			}
			
			
		}
		WriteLog.println("search time="+(System.currentTimeMillis()-ii));
		return ll;
		
	}
	
	/**
	 * search word.
	 * @param ff folder in which we have to search
	 * @param keyword Query
	 */
	public static LinkedList<File> searchPart(File ff,String keyword){
		LinkedList<File> ll=new LinkedList<File>();
		
		Stack<File> ss=new Stack<File>();
		ss.push(ff);
		
		keyword=keyword.toLowerCase();
		long ii=System.currentTimeMillis();
		while(!ss.isEmpty()){
			File c=ss.pop();
			//WriteLog.println(ii++);
			if(c.getName().toLowerCase().indexOf(keyword)!=-1){
				ll.add(c);
			}
			if(c.isDirectory()){
				File af[]=c.listFiles();
				if(af==null){
					continue;
				}
				for(int i=0;i<af.length;i++){
					ss.push(af[i]);
				}	
			}
			
			
		}
		WriteLog.println("search time="+(System.currentTimeMillis()-ii));
		return ll;
		
	}
	
	
	
	
	
	
	
	
}
