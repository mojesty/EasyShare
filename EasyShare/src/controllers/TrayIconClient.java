package controllers;

import org.jnativehook.GlobalScreen;

import model.Client;
import model.KeyListener;
import view.ClientView;

import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;



public class TrayIconClient {
  public static Client client = null;
  private static String ip = "localhost";
  
  //Finds a sever via UDP broadcast of all possible network interfaces except loopback
  private static void findserver() {
	  PrintWriter pwr = null;
	  try {
		pwr = new PrintWriter("log2.txt", "UTF-8");
	  } catch (FileNotFoundException e1) {
		// This exception will never be used, I hope..
		System.out.println("cannot create log file");
	  } catch (UnsupportedEncodingException e) {
		// And this exception also
		  System.out.println("UTF-8 doesnt work :(");
		e.printStackTrace();
	}
	  // Find the server using UDP broadcast
	  try {
	    //Open a random port to send the package
	    DatagramSocket c = new DatagramSocket();
	    c.setBroadcast(true);

	    byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

	    //Try the 255.255.255.255 first
	    try {
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
	      c.send(sendPacket);
	      //System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
	    } catch (IOException e) {
	    	pwr.println("Datagram packet cannot be created");
	    }

	    // Broadcast the message over all the network interfaces
	    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	      NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

	      if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	        continue; // Don't want to broadcast to the loopback interface
	      }
	      for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
	        InetAddress broadcast = interfaceAddress.getBroadcast();
	        if (broadcast == null) {
	          continue;
	        }
	        // Send the broadcast package!
	        try {
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
	          c.send(sendPacket);
	        } catch (IOException e) {
	        	pwr.println("Datagram packet cannot be sent");
	        }
	        pwr.println(">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
	      }
	    }
	    pwr.println(">>> Done looping over all network interfaces. Now waiting for a reply!");
	    //Wait for a response
	    byte[] recvBuf = new byte[15000];
	    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
	    c.receive(receivePacket);

	    //We have a response
	    pwr.println(">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

	    //Check if the message is correct
	    String message = new String(receivePacket.getData()).trim();
	    if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
	      //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
	    	ip = receivePacket.getAddress().toString();
	      //Controller_Base.setServerIp(receivePacket.getAddress());
	    }
	    c.close();
	  } catch (IOException ex) {
		  pwr.println("Can not discover server by UDP broadcasting");
		  ip = null;
		  return;
	    //Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
	  }
	  finally {
		  pwr.close();
	  }
  }
  
  
  public static void main(String[] args) {
	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
	logger.setLevel(Level.WARNING);
	findserver();
    try {
		client = new Client(ip.substring(1, ip.length()));
	} catch (IOException e) {
		logger.severe("Unable connect to server");
		return;
	}
    catch (NullPointerException npe) {
    	logger.severe("UDP broadcasting didnt find server");
    }
	ClientView cltview = new ClientView(client);
    String str = "";
    new KeyListener(str,client).run();
    cltview.setTrayIcon();
  }
  
}




