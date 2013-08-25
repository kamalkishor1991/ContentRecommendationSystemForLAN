import java.io.File;
import java.io.IOException;

import fileInfo.FileInfoDatabase;

/**
 * Create new Thread during shutdown of application and save setting. 
 * @author kamal
 *
 */
public class ShutDown implements Runnable{
	private FileInfoDatabase database;
	public ShutDown(FileInfoDatabase f) {
		this.database=f;
	}

	

	@Override
	public void run() {
		System.out.println("shuting down");
		try {
			database.writeFile(new File("setting/database.txt"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
}
