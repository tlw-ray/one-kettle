package jface.app;

import org.eclipse.jface.action.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class MyApplication extends ApplicationWindow {

    public static void main(String[] args){
        MyApplication myApplication = new MyApplication();
        myApplication.setBlockOnOpen(true);
        myApplication.open();
        Display.getCurrent().dispose();
    }

    private ExitAction exitAction = new ExitAction(this);
    private StatusLineManager statusLineManager = new StatusLineManager();
    private StatusAction statusAction = new StatusAction(statusLineManager);
    private ActionContributionItem statusActionContributionItem = new ActionContributionItem(statusAction);


    public MyApplication() {
        super(null);
        this.addMenuBar();
        this.addStatusLine();
        this.addToolBar(SWT.FLAT | SWT.WRAP);
    }

    protected Control createContents(Composite parent){
        getShell().setText("JFace Example");
        setStatus("JFace Example v1.0");
        return parent;
    }

    protected void initializeBounds(){
        getShell().setSize(300, 200);
    }

    protected MenuManager createMenuManager(){
        MenuManager menuManager = new MenuManager("");
        MenuManager fileManager = new MenuManager("&File");
        MenuManager helpManager = new MenuManager("&Help");
        menuManager.add(fileManager);
        menuManager.add(helpManager);

        fileManager.add(exitAction);
        helpManager.add(statusActionContributionItem);
        return menuManager;
    }

    protected StatusLineManager createStatusLineManager(){
        statusLineManager.setMessage("Hello, world!");
        return statusLineManager;
    }

    protected ToolBarManager createToolBarManager(int style){
        ToolBarManager toolBarManager = new ToolBarManager(style);
        toolBarManager.add(exitAction);
        return toolBarManager;
    }

}
