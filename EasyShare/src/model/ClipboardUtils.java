package model;

import java.awt.AWTError;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

public class ClipboardUtils implements ClipboardOwner {
    public interface CustomUser32 extends StdCallLibrary {
        CustomUser32 INSTANCE = (CustomUser32) Native.loadLibrary("user32", CustomUser32.class);
        HWND GetForegroundWindow();
        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // dummy: needed for `ClipboardOwner`
    }

    /**Emulates Control-C command to force copying into clipboard
     * 
     * @param customUser32
     */
    void controlC(CustomUser32 customUser32) {
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
    }
    
  /**
   * Emulates Control-V command to paste from clipboard
   * @param customUser32
   */
    void controlV(CustomUser32 customUser32) {
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x56 /* 'V' */, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x56 /* 'V' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
    }

    String getClipboardText() throws HeadlessException , UnsupportedFlavorException , IOException {
		return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    void setClipboardText(String data) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data), this);
    }

    /**
     * Perform copying into shared buffer, saves condition of the system clipboard
     * @param user32
     * @param customUser32
     * @return
     * @throws AWTError
     * @throws InterruptedException
     * @throws HeadlessException
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    String getSelectedText(User32 user32, CustomUser32 customUser32) throws AWTError, InterruptedException ,
    HeadlessException , UnsupportedFlavorException , IOException{
        HWND hwnd = customUser32.GetForegroundWindow();
        char[] windowText = new char[512];
        user32.GetWindowText(hwnd, windowText, 512);
        //String windowTitle = Native.toString(windowText);
        //System.out.println("Will take selected text from the following window: [" + windowTitle + "]");
        String before;
		before = getClipboardText();
        controlC(customUser32); // emulate Ctrl C
        Thread.sleep(100); // give it some time
        String text = getClipboardText();
        //System.out.println("Currently in clipboard: " + text);
        // restore what was previously in the clipboard
        setClipboardText(before);
        return text;
    }

    /**
     * Performs paste using system clipboard, saves its condition
     * @param user32
     * @param customUser32
     * @param text
     * @return
     * @throws AWTError
     * @throws InterruptedException
     * @throws HeadlessException
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    String setText(User32 user32, CustomUser32 customUser32, String text) throws AWTError, InterruptedException ,
    HeadlessException , UnsupportedFlavorException , IOException{
        HWND hwnd = customUser32.GetForegroundWindow();
        char[] windowText = new char[512];
        user32.GetWindowText(hwnd, windowText, 512);
        //String windowTitle = Native.toString(windowText);
        //System.out.println("Will take selected text from the following window: [" + windowTitle + "]");
        String before;
		before = getClipboardText();
		setClipboardText(text);
        controlV(customUser32); // emulate Ctrl C
        Thread.sleep(100); // give it some time
        //System.out.println("Currently in clipboard: " + text);
        // restore what was previously in the clipboard
        setClipboardText(before);
        return text;
    }
    
    /*public static void main(String[] args) throws Exception {
        ClipboardUtils foo = new ClipboardUtils();
        while(true) {
        Thread.sleep(500); // take some time for you to select something anywhere
        System.out.println(foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE));
        }
    }*/
}