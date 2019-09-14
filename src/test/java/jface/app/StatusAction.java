package jface.app;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.StatusLineManager;

public class StatusAction extends Action {
    StatusLineManager statusLineManager;
    short triggerCount = 0;

    public StatusAction(StatusLineManager statusLineManager){
        super("&Trigger@Ctrl+T", AS_PUSH_BUTTON);
        this.statusLineManager = statusLineManager;
        setToolTipText("Trigger the Action");
//        setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(), "eclipse.gif"));
    }

    public void run(){
        triggerCount++;
        statusLineManager.setMessage("The status action has fired, Count:" + triggerCount);
    }
}
