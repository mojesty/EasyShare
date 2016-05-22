package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Hashtable;

import org.jnativehook.keyboard.NativeKeyEvent;
 
public class Client {
	public static final String WANT_BUFFER = "want";
	public static final String HAVE_BUFFER = "have";
	public int copykey = 0x10;
	public int pastekey = 0x30;
    private Socket socket = null;
    private PrintWriter pwr = null;
    private InputStreamReader isr = null;
    private BufferedReader bfr = null;
    public String buff = null;
	private Dictionary<Integer, Character> dict = new Hashtable<Integer, Character>();
   
    public Client(String ip) throws IOException{
        socket = new Socket(ip, 8081);
        pwr = new PrintWriter(socket.getOutputStream(),true);
        isr = new InputStreamReader(socket.getInputStream());
        bfr = new BufferedReader(isr);
        System.out.println("Connection successful");
        makeDict();
    }
	public void makeDict () {
		int i = 0x10;
		char letter = ' ';
		//NativeKeyEvent e = new NativeKeyEvent(0, 1, 1, i, 1, 'a');
		while (i<0x32) {
			letter = NativeKeyEvent.getKeyText(i).charAt(0);
			dict.put(i, letter);
			//System.out.println("dict create" + i + ' ' + dict.get(i));
			i++;
		}		
	}
	
	public int scan(char c) {
		int i = 0x10;
		while(i<0x32) {
			if (dict.get(i)==c)  {
				//System.out.println("Found entry key code "+ i);
				return i;
			}
			i++;
		}
		return 0x10;
	}
	
    public void send(String msg){
    	pwr.println(HAVE_BUFFER);
    	pwr.println(msg);
    }
    
    public void receive(){
    	pwr.println(WANT_BUFFER);
		try {
			buff =  bfr.readLine();
		} catch (IOException e) {
			System.out.println("Cannot receive message from server");			
			//e.printStackTrace();
		}
    }
}