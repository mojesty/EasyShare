package buttons;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Client;

public class ClientPasteListener implements ActionListener {
	TextField t;
	Client clt = null;
	String buff = null;
	Label lb = null;
	public ClientPasteListener(TextField tf, Label label, Client client){
		t = tf;
		lb = label;
		clt = client;
	}


	public void actionPerformed(ActionEvent e ){
		String buf = null;
		int curr = 0;
		//l.setText(t.getText());
		//txf.setClipboardContents(t.getText());
		buf = t.getText().toUpperCase();
		if (buf.length()!=1) return;
		int a = (int)buf.charAt(0);
		//if (a < 97 || a > 122) return;
		//System.out.println("Will now scan for char " + (char)a);
		curr = clt.scan((char)a);
		if (curr == clt.copykey) {
			lb.setText("Already in use as paste letter.");
			return;
		}
		if (a == 'C' || a == 'V' || a == 'Z' || a == 'X' || a == 'W' || a == 'O' || a == 'S') {
			lb.setText("This letter is not recommended.");
			clt.pastekey = curr;
			return;
		}
		lb.setText("Paste button set successfully");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			System.out.println("Thread sleep error");
			//e1.printStackTrace();
		}
		lb.setText("");
		clt.pastekey = curr;
		return;
	}

}
