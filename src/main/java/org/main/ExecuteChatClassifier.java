package org.main;

import org.controler.Controller;
import org.view.MainAppWindow;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) {
        
        MainAppWindow view = new MainAppWindow();
        Controller controller = new Controller(view);

        view.setControler(controller);
        controller.initializeView();
    }
}