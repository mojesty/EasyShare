package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
	public static final String WANT_BUFFER = "want";
	public static final String HAVE_BUFFER = "have";
	public String buff = "default buffer";
	public String [] hist = {"", "", "", "", ""};
	
	public void movehist() {
		hist[4] = hist[3];
		hist[3] = hist[2];
		hist[2] = hist[1];
		hist[1] = hist[0];
		hist[0] = buff;
	}
	
    public void go() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        while(!serverSocket.isClosed()) {
        	while(!serverSocket.isClosed()){
        		Socket socket = serverSocket.accept(); 
        		new MyThread(socket, this).start();
        	}
        }
    }   

}