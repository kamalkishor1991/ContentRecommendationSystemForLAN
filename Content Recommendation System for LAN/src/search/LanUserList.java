package search;

import java.util.ArrayList;



/**
This Interface is used to get list of all connected users and shared list of the users.

*/
public interface LanUserList{
	/**
	
	@return Return all connected user with shared content.
	*/
	public ArrayList<NetFiles> getAllUsers();
	
	

}