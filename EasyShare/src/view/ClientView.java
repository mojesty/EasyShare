package view;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import buttons.ClientCopyListener;
import buttons.ClientPasteListener;
import controllers.TrayIconClient;
import model.Client;

public class ClientView {
	public static final String APPLICATION_NAME = "Client";
	public static final String ICON_STR = "/files-icon-client.png";
	Client client;
	
	public ClientView(Client clt) {
		client = clt;
	}
	
	public void createGUI() {   
		JFrame f = new JFrame(APPLICATION_NAME);	
	    f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    f.setMinimumSize(new Dimension(300, 200));
		Button b = new Button("Copy button");
		b.setBounds(30,100,120,20);
		Button b2 = new Button("Paste button");
		b2.setBounds(30, 150, 120, 20);
		TextField tf = new TextField("Q by default");
		tf.setBounds(200,100,120,20);
		Label l = new Label("Ctrl-Key1 to copy, Ctrl-Key2 to paste. Type Key1 and key2 below. "
				+ "Note that some letters could be already in use.");
		l.setBounds(30,30,350,20);
		Label l2 = new Label("Note that some letters could be already in use.");
		l2.setBounds(30,50,350,20);
		Label ldisc = new Label("");
		ldisc.setBounds(30, 200, 350, 20);
		TextField tf2 = new TextField("B by default");
		tf2.setBounds(200,150,120,20);
		ClientCopyListener mal = new ClientCopyListener(tf, ldisc, client);
		ClientPasteListener mal2 = new ClientPasteListener(tf2, ldisc, client);
		b.addActionListener(mal);
		b2.addActionListener(mal2);
		f.add(tf);
		f.add(b);
		f.add(b2);
		f.add(l);
		f.add(tf2);
		f.add(ldisc);
		f.add(l2);
		f.setSize(400,300);
		f.setLayout(null);
		f.setVisible(true);
	    
	  }

  public void setTrayIcon() {
	if(! SystemTray.isSupported() ) {
	  System.out.println("something got wrong in setTrayIcon!");
      return;
    }

    PopupMenu trayMenu = new PopupMenu();
    MenuItem item = new MenuItem("Exit");
    MenuItem settings = new MenuItem("Settings");
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
    URL imageURL = TrayIconClient.class.getResource(ICON_STR);

    Image icon = Toolkit.getDefaultToolkit().getImage(imageURL);
    TrayIcon trayIcon = new TrayIcon(icon, APPLICATION_NAME, trayMenu);
    trayIcon.setImageAutoSize(true);

    SystemTray tray = SystemTray.getSystemTray();
    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
    	System.out.println("Tray error");
      //e.printStackTrace();
    }

    trayIcon.displayMessage(APPLICATION_NAME, "Client is running.",
                            TrayIcon.MessageType.INFO);
  }
	
}
