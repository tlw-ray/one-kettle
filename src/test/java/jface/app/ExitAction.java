package jface.app;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

public class ExitAction extends Action {
    ApplicationWindow applicationWindow;
    public ExitAction(ApplicationWindow applicationWindow){
        this.applicationWindow = applicationWindow;
        setText("E&xit@Ctrl+W");
        setToolTipText("Exit the application");
    }
    public void run(){
        applicationWindow.close();
    }

    public ApplicationWindow getApplicationWindow() {
        return applicationWindow;
    }

    public void setApplicationWindow(ApplicationWindow applicationWindow) {
        this.applicationWindow = applicationWindow;
    }
}
