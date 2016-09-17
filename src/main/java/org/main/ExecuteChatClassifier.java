package org.main;

import org.controler.Controller;
import org.view.MainAppWindow;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        ////////////Borrar despues de usar/////////////
//        SMOClassifierTechnique separar = new SMOClassifierTechnique();
//        String filename ="/Users/martinmineo/Google Drive/Tesis/Tesis, carpeta interna/Clasificaciones nuestras/Archivo de entrenamiento (2).arff"; 
//        separar.splitSentencesBorrar(filename);
        /////////////////////////
        
        
        MainAppWindow view = new MainAppWindow();
//        MachineLearningClassifierTechnique model = null;
        Controller controller = new Controller(view);

        view.setControler(controller);
        controller.initializeView();
    }
}