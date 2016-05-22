package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyThread extends Thread{
    private Socket mysocket = null;
    int count = 0;
    int guess = -1;
    int number = (int) (Math.random()*100);
    Server srv = null;
    String buff = null;
    private PrintWriter pwr = null;
    private InputStreamReader isr = null;
    private BufferedReader bfr = null;
    
    public MyThread(Socket insocket, Server server) throws IOException {
    	mysocket = insocket;
    	srv = server;
        pwr = new PrintWriter(mysocket.getOutputStream(),true);
        isr = new InputStreamReader(mysocket.getInputStream());
        bfr = new BufferedReader(isr);
        System.out.println("Successfully connected new client on ip " + insocket.getInetAddress());
    }
    
	public void run(){
		try {
			while(!mysocket.isClosed()) {
				buff =  bfr.readLine();
				if (buff.equals("have"))  {
					srv.buff = bfr.readLine();
					srv.movehist();
					System.out.println("received: "+srv.buff);
				}
				if (buff.equals("want")) {
					pwr.println(srv.buff);
					System.out.println("send: "+srv.buff);
				}
			}
		} catch (IOException e) {
			System.out.println("Client disconnected");
		} 
		
	}
	
}
/*
System.out.println("Hello server ");
while (guess != number){
	guess = Istream.read();
	if (guess > number){
		Ostream.write(1);
	}
	if (guess < number){
		Ostream.write(0);
	}
}
Ostream.write((int)(-1));
System.out.format("success in %d steps\n",count);
mysocket.close();
*/