package org.main;

import org.commons.GoogleHangoutsJsonParser;
import org.controler.Controller;
import org.view.MainAppWindow;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	
    	new GoogleHangoutsJsonParser("C:\\Users\\Usuario\\Desktop\\Hangouts.json");
        
        MainAppWindow view = new MainAppWindow();
        Controller controller = new Controller(view);

        view.setControler(controller);
        controller.initializeView();
    }
}