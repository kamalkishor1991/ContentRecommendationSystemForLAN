
package search;

import java.io.File;
import java.util.LinkedList;

/**
 *Must be implemented properly because multiple threads accessing it in same time.
 *So must be synchronized 
 * @author kamal
 */
public interface SearchKeyword {
    /**
     * 
     * Address of files colon(:) separated.
     * Last represent Query.
     * Search request from browser 
     * if Last query is empty then return all folders and files example of empty Query-("add:add::)
     * @param req  Search for substring in file...
     * 
     * @return LinkedList of Files
     * @throws Exception 
     */
    public LinkedList<File> search(String req) throws Exception;
    
    /**
     * Address of files colon(:) separated.
     * Last represent Query.
     * Search request from browser 
     * @param req Search for complete file name.
     * @return Linked List of Files
     * @throws Exception 
     */
    public LinkedList<File> searchComp(String req) throws Exception;
    
}
