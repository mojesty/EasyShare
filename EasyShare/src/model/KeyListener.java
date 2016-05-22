package model;

import java.awt.AWTError;
import java.awt.HeadlessException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.sun.jna.platform.win32.User32;

import model.ClipboardUtils.CustomUser32;

public class KeyListener implements NativeKeyListener, Runnable{
	int bul = 0;
	String str = null;
	Client clt = null;
	ClipboardUtils foo = new ClipboardUtils();
	Logger logger;
	public KeyListener(String string, Client client) {
		str = string;
		clt = client;
	    logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
	    logger.setLevel(Level.WARNING);
	}

	/**
	 * Proccesses KeyEvent when key was pressed
	 */
    public void nativeKeyPressed(NativeKeyEvent e) {
    	//System.out.println(" " +e.getRawCode());
        //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode()==NativeKeyEvent.VC_CONTROL_L)  {
        	bul = 1;
        	//System.out.println("Ctrl pressed");
        }
        if (e.getKeyCode()==clt.copykey/*NativeKeyEvent.VC_Q*/ && bul == 1) {
        	try {
				clt.send(foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE));
				Thread.sleep(100);
			} catch (InterruptedException | HeadlessException | UnsupportedFlavorException | IOException e1) {
				logger.severe("Unable to perform clipboard operation");
				//e1.printStackTrace();
			}
        	catch (AWTError er) {
        		logger.severe("AWT error");
        	}
    		//bul = 0;
        	//System.out.println("Copied" + e.getKeyCode());
        }
        
        if (e.getKeyCode()==clt.pastekey /*NativeKeyEvent.VC_B*/ && bul == 1) {
        	try {
				clt.receive();
				foo.setText(User32.INSTANCE, CustomUser32.INSTANCE, clt.buff);
				Thread.sleep(100);
			} catch (InterruptedException | HeadlessException | UnsupportedFlavorException | IOException e1) {
				logger.severe("Unable to perform clipboard operation");
				//e1.printStackTrace();
			}
        	catch (AWTError er) {
        		logger.severe("AWT error");
        	}
    		bul = 0;
        	//System.out.println("Pasted" + e.getKeyCode());
        }
        /*
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				logger.severe("Unable to register native hook");
			}
        }
        */
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode()==NativeKeyEvent.VC_CONTROL_L) {
        	bul = 0;
        	//System.out.println("Deactivated");
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }
    
	public void run() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            logger.severe("There was a problem registering the native hook.");
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
	}
    /*
    public static void main(String[] args) {
    	Foo foo = new Foo();
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        String a = "";
        GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample(a));
    }*/
}