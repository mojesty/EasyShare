package view;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import controllers.TrayIconServer;
import model.Server;


public class ServerView {
  public static final String APPLICATION_NAME = "Server";
  public static final String ICON_STR = "/files-icon-server.png";
  String msg;
  Server srv;

  public  ServerView(String message, Server server) {
	  msg = message;
	  srv = server;
  }
	
  public void createGUI() {   
	JFrame f = new JFrame(APPLICATION_NAME);	
	f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    f.setMinimumSize(new Dimension(400, 300));
	Label tf = new Label(srv.hist[0]);
	tf.setBounds(50,50,250,20);
	Label tf1 = new Label(srv.hist[1]);
	tf1.setBounds(50,80,250,20);
	Label tf2 = new Label(srv.hist[2]);
	tf2.setBounds(50,110,250,20);
	Label tf3 = new Label(srv.hist[3]);
	tf3.setBounds(50,140,250,20);
	Label tf4 = new Label(srv.hist[3]);
	tf4.setBounds(50,170,250,20);

	f.add(tf);
	f.add(tf1);
	f.add(tf2);
	f.add(tf3);
	f.add(tf4);
	f.setLayout(null);
	f.setVisible(true);
    
  }
  
  public void setTrayIcon() {
	if(! SystemTray.isSupported() ) {
	  System.out.println("something get wrong!");
      return;
    }

    PopupMenu trayMenu = new PopupMenu();
    MenuItem item = new MenuItem("Exit");
    MenuItem settings = new MenuItem("View istory");
    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    settings.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  createGUI();
                }
              });
        }
      });
    trayMenu.add(settings);
    trayMenu.add(item);
    URL imageURL = TrayIconServer.class.getResource(ICON_STR);
    Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
    TrayIcon trayIcon = new TrayIcon(icon, APPLICATION_NAME, trayMenu);
    trayIcon.setImageAutoSize(true);

    SystemTray tray = SystemTray.getSystemTray();
    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      e.printStackTrace();
    }

    trayIcon.displayMessage(APPLICATION_NAME, msg,
                            TrayIcon.MessageType.INFO);
  }
}
