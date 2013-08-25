package gui;

import java.awt.AWTException;
import java.awt.Dimension;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import search.FindLANShared;
import search.SearchBasedRecommendation;

import Log.WriteLog;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static int HEIGHT=0;
	public static int WIDTH=0;
	
	//private ButtonActionListener bal;	
	private TrayIcon icon;
	private SystemTray tray;
	private MenuItem defaultItem ;
	private Image img;
	private PopupMenu pop;
	private SystemTrayMenuItem menuItem;
	private TextField timeout;
	private TextField rec;
	private JButton ok;
	private ButtonGroup log;
	private SearchBasedRecommendation sbr;
	private FindLANShared fln;
	public MainWindow(FindLANShared fln,SearchBasedRecommendation sbr){
		this.setTitle("Recommendation System");
		this.sbr=sbr;
		this.fln=fln;
		HEIGHT =(int)(getScreenSize().height-getScreenSize().height/1.6);
		WIDTH=(int)(getScreenSize().width-getScreenSize().width/1.6);
		setBounds(getScreenSize().width/3,getScreenSize().height/5,WIDTH,HEIGHT);
		iconSet("icon.jpg");
		this.setResizable(false);
		this.setLayout(null);
		//setLayout(null);
		this.setTrayMenu("icon.jpg");
		JLabel jl=new JLabel("Timeout for Lan Scanning(sec)");
		timeout=new TextField(""+(fln.getTimeout()/1000));
		JLabel lrec=new JLabel("No of Recommendation");
		log=new ButtonGroup();
		JRadioButton jy=new JRadioButton("YES");
		JRadioButton jn=new JRadioButton("NO");
		log.add(jy);
		log.add(jn);
		rec=new TextField(""+(sbr.getNoOfRecommendation()));
		ok=new JButton("OK");
		jl.setBounds(new Rectangle(0,0,200,30));
		timeout.setBounds(210,0,200,30);
		lrec.setBounds(new Rectangle(0,50,200,30));
		rec.setBounds(new Rectangle(210,50,200,30));
		ok.setBounds(new Rectangle(300,200,100,30));
		jy.setBounds(new Rectangle(200,100,50,30));
		jn.setBounds(new Rectangle(300,100,50,30));
		JLabel lr=new JLabel("Enable Logging");
		lr.setBounds(new Rectangle(0,100,200,30));
		if(WriteLog.LOGING){
			jy.setSelected(true);
		}
		else{
			jn.setSelected(true);
		}
		log.getSelection();
		ok.addActionListener(this);
		this.add(jl);
		this.add(ok);
		this.add(timeout);
		this.add(lrec);
		this.add(rec);
		this.add(jy);
		this.add(jn);
		this.add(lr);
		repaint();
		
	}
	
	private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
	
	public static Dimension getScreenSize(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	/**
	 * this sets the icon to String s
	 * @param s s is the name of image.
	 */
	protected void iconSet(String s){
      	ImageIcon icon;
      	Image ic;
      	icon=new ImageIcon(s);
		ic=icon.getImage();
		setIconImage(ic);
      }
	/**
	 * set a icon in system tray where s is Image name.
	 * @param s name of Image.
	 */
	protected void setTrayMenu(String s){
		menuItem=new SystemTrayMenuItem(this);
		pop=new PopupMenu();
		defaultItem = new MenuItem("Exit");
		img=Toolkit.getDefaultToolkit().getImage(s);		
		tray=SystemTray.getSystemTray();
		defaultItem.addActionListener(menuItem);
		pop.add(defaultItem);
		try{
		icon=new TrayIcon(img,"Recommendation System",pop);
		icon.addActionListener(menuItem);
		}
		catch(Exception e){}

		icon.setImageAutoSize(true);
		
		 try {
    		    tray.add(icon);
 		   } catch (AWTException e) {
 		     
  		  }

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		long time1=Long.parseLong(timeout.getText())*1000;
		int no=Integer.parseInt(rec.getText());
		String s=this.getSelectedButtonText(log);
		if(s.equals("YES")){
			WriteLog.LOGING=true;
		}
		else{
			WriteLog.LOGING=false;
		}
		sbr.setNoOfRecommedation(no);
		fln.setTimeout(time1);
		System.out.println("clicked");
		
	}
}
