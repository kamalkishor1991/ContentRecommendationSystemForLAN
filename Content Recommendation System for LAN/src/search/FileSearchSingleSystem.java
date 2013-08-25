
package search;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;

import Log.WriteLog;

/**
 *Search keywords etc using single system.
 *system that is running web server will search only 
 *Searching Files using simple DFS.
 *No ordering is provided hare so must be done externally.
 *Using static methods From {@link SearchFolder}
 * @author kamal
 */
public class FileSearchSingleSystem implements SearchKeyword {
   
    public FileSearchSingleSystem(){     
    }
    
    
    @Override
    public LinkedList<File> search(String req) throws Exception {
        File arr[];
        try {
            req=URLDecoder.decode(req,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            WriteLog.println("error="+ex);
        }
        WriteLog.println("searching req="+req);
        int v=1;
        if(req.charAt(req.length()-1)==':'&&req.charAt(req.length()-2)==':'){
        	v=0;	
        }
        String s[]=req.split(":");
        arr=new File[s.length-v];
        for(int i=0;i<arr.length;i++){
        	if(!(s[i].contains("//")||s[i].contains("\\\\"))){ //to check if accessing network or not.
        		throw new Exception();
        	}
            arr[i]=new File(s[i]);
        }
        String keyword=v==1?s[s.length-1]:"";
        LinkedList<File> ret=new LinkedList<File>();
        for(int i=0;i<arr.length;i++){
        	LinkedList<File> l=SearchFolder.searchPart(arr[i], keyword);
        	ret.addAll(l);
        }
        return ret;
    }

    @Override
    public LinkedList<File> searchComp(String req) throws Exception {
    	File arr[];
        try {
            req=URLDecoder.decode(req,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            WriteLog.println("error="+ex);
        }
        
        String s[]=req.split(":");
        arr=new File[s.length-1];
        for(int i=0;i<arr.length;i++){
        	if(!s[i].contains("//")){ //to check if accessing network or not.
        		throw new Exception();
        	}
        	
            arr[i]=new File(s[i]);
        }
        String keyword=s[s.length-1];
        LinkedList<File> ret=new LinkedList<File>();
        for(int i=0;i<arr.length;i++){
        	LinkedList<File> l=SearchFolder.search(arr[i], keyword);
        	ret.addAll(l);///TO Do--- improve performance
        }
        return ret;
    }
    
    
}
