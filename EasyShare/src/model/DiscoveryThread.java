package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryThread implements Runnable {
	  
	  DatagramSocket socket = null;
	  
	  @Override	  
	  public void run() {
		  PrintWriter pwr = null;
		  try {
			pwr = new PrintWriter("log-server.txt", "UTF-8");
		  } catch (FileNotFoundException e1) {
			// This exception will never be used, I hope..
			System.out.println("cannot create log file");
		  } catch (UnsupportedEncodingException e) {
			// And this exception also
			  System.out.println("UTF-8 doesnt work :(");
			e.printStackTrace();
		}
	    try {
	      //Keep a socket open to listen to all the UDP trafic that is destined for this port
	      socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
	      socket.setBroadcast(true);

	      while (true) {
	        pwr.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

	        //Receive a packet
	        byte[] recvBuf = new byte[15000];
	        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
	        socket.receive(packet);

	        //Packet received
	        pwr.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
	        pwr.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));

	        //See if the packet holds the right command (message)
	        String message = new String(packet.getData()).trim();
	        if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
	          byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

	          //Send a response
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
	          socket.send(sendPacket);

	          pwr.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
	        }
	      }
	    } catch (IOException ex) {
	      Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    finally {
	    	pwr.close();
	    }
	  }

	  public static DiscoveryThread getInstance() {
	    return DiscoveryThreadHolder.INSTANCE;
	  }

	  private static class DiscoveryThreadHolder {
	    private static final DiscoveryThread INSTANCE = new DiscoveryThread();
	  }

	}
