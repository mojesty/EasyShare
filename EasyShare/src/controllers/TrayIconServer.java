package controllers;

import java.io.IOException;
import java.net.*;

import model.DiscoveryThread;
import model.Server;
import view.ServerView;



public class TrayIconServer {
  
  public static void main(String[] args){
	String msg = "";
	try {
		msg= "Server started on ip " + Inet4Address.getLocalHost().getHostAddress() + " port 8081";
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		msg = "Unable to find Inet4Address, seems that network is not working";
	}
    Server server = null;
    Thread discoveryThread = null;
    discoveryThread = new Thread(DiscoveryThread.getInstance());
	discoveryThread.start();
    server = new Server();
    ServerView srview = new ServerView(msg, server);
    srview.setTrayIcon();
    try {
    	server.go();
    }
    catch (IOException e) {
    	System.out.println("Cannot run server");
    }
  }

}
