package org.main;

import org.controler.Controller;
import org.enums.IpaBehavior;
import org.ipa.Group;
import org.view.MainAppWindow;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

public class ExecuteChatClassifier {
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	
    	///////////////////
    	Instances instances = Weka.loadDataset("C:\\Users\\Usuario\\Documents\\Tesis\\ChatClassifier\\datasets\\temp\\20170120 10220031 classify\\Archivo de clasificacion-nombres-sentences-freeling-labeled.arff");
    	
    	Group group = new Group();
    	for (int i = 0; i < instances.numInstances(); i++) {
            // Crea y agrega una nueva instancia a trainDatasetPhase1
            Instance instance = instances.instance(i);
            
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = instance.stringValue(instanceIndex++);
            
            group.addInteraction(nombre, IpaBehavior.stringToIpaBehavior(conducta));
            
        }
    	///////////////////
        
        MainAppWindow view = new MainAppWindow();
        Controller controller = new Controller(view);

        view.setControler(controller);
        controller.initializeView();
    }
}