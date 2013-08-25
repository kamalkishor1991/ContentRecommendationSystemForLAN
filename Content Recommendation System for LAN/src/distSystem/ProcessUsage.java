package distSystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ProcessUsage implements Runnable {
	private BufferedReader br;
	private boolean isStop=false;
	private double pu=0;
	private Process pr;
	/**
	 * create a new Thread and Run 
	 * "cmd /c "+"typeperf "+(char)(34)+"\\processor(_total)\\% processor time"+(char)(34);
	 * @throws IOException 
		
	 */
	public ProcessUsage() throws IOException{
			Runtime rt=Runtime.getRuntime();
			String s="cmd /c "+"typeperf "+(char)(34)+"\\processor(_total)\\% processor time"+(char)(34);
			//WriteLog.print(s);
			pr=rt.exec(s);
			//WriteLog.println("dasfdsf");
			br=new BufferedReader(new InputStreamReader(pr.getInputStream()));
			br.readLine();
			br.readLine();
			Thread t=new Thread(this);
			
			t.start();
		
			
		
	}
	/**
	 * for stoping the Thread
	 */
	public void stopThread(){
		isStop=true;
	}
	
	
	public double getProcessUsage(){
		return pu;
	}
	
	public void run(){
		while(true){
			if(isStop){
				pr.destroy();
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ;
			}
			try {
				String ss=br.readLine();
				
				int in=ss.indexOf(',')+2;
				ss=ss.substring(in);
				
				pu=new Double(ss.substring(0,ss.length()-2));
				//WriteLog.println(ss+","+pu);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
