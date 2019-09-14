package jface.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BrowserApp {
    public static void main(String[] args) {

        System.out.println(System.getProperty("java.library.path"));
        System.out.println(System.getProperty("org.eclipse.swt.browser.UseWebKitGTK"));

        Display display = new Display();
        Shell shell = new Shell(display);

        try {
            Browser browser = new Browser(shell, SWT.NONE);
            browser.setSize(shell.getSize());
            browser.setUrl("bing.com");
        } catch (SWTError e) {
            e.printStackTrace();
        }

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
    }
}
