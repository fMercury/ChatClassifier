package org.main;

import org.controler.WekaControler;
import org.view.MainAppWindow;
import org.weka.MachineLearningClassifierTechnique;

public class ExecuteChatClassifier {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MainAppWindow view = new MainAppWindow();
        MachineLearningClassifierTechnique model = null;
        WekaControler controler = new WekaControler(model, view);

        view.setControler(controler);
        controler.initializeView();
    }

}
