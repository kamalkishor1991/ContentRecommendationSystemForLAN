package webServer;

import java.io.OutputStream;
import java.util.ArrayList;
/**
 * In implementing this interface do not update any class variable because different thread are calling methods at same time. 
 * @author kamal
 *
 */
public interface ProcessHttpReq {

	/**
	 * Get request.
	 * @param header header array.
	 * @param out In this stream output can be written..
	 */
	public void doGet(ArrayList<String > header,OutputStream out);
	
	/**
	 * /**
	 * Post request.
	 * @param header
	 * @param out In this stream output can be written.
	 */
	public void doPost(ArrayList<String > header,OutputStream out);
	
}
