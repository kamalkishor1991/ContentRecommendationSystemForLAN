package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;




public class SystemTrayMenuItem implements ActionListener{
	
	
	private JFrame m;
	public SystemTrayMenuItem(JFrame j){
		m= j;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try{
		String s=e.getActionCommand();
		if(s.equalsIgnoreCase("Exit")){
			
			System.exit(0);
		}
		}catch(NullPointerException n){
			m.setVisible(true);
		}
		
	}

}
