package fileInfo;

import java.io.File;
import java.io.IOException;
/**
 * Write setting in every Timeout interval.
 * It runs in a new Thread.
 * @author kamal
 *
 */
public class WriteSetting implements Runnable{
	private FileInfoDatabase database;
	private Thread t;
	private boolean isStop;
	private long timeOut=10000;
	private File file;
	public WriteSetting(FileInfoDatabase f,File file){
		this.database=f;
		this.file=file;
		t=new Thread(this);
		isStop=false;
	}
	
	public void start(){
		t.start();
	}
	
	public void stop(){
		isStop=true;
	}
	/**
	 * Timeout for repeating setting writing.
	 * @param timeout
	 */
	public void setTimeout(long timeout){
		this.timeOut=timeout;
	}
	@Override
	public void run() {
		while(!isStop){
			//TO DO first copy file into backup folder
			try {
				Thread.sleep(timeOut);
				database.writeFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}

}
