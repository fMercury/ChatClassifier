package org.ipa;

import java.util.Vector;

import org.enums.IpaBehavior;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

public class IpaAnalysis {
    
    public IpaAnalysis() {    }
    
    public Vector<Object[]> analyze(String labeledFileName) {
        
        Group group = loadDatasetIntoGroup(labeledFileName);
        
        Vector<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {
            String conflict = behavior.getConflict();
            String behaviorDescription = behavior.getCode() + ". " +  behavior.getDescription();
            int infLimit = IpaEdgeValues.getInferiorLimit(behavior);
            int supLimit = IpaEdgeValues.getSuperiorLimit(behavior);
            double percentage = group.getPercentage(behavior);
            String risk = "";
            
            Object[] objects;
            
            if (percentage < infLimit || percentage > supLimit) {
                risk = "Sí";
                objects = new Object[] {colorRed(conflict), colorRed(behaviorDescription),  colorRed(infLimit + " %"), colorRed(supLimit + " %"), colorRed(String.format("%.2f", percentage) + " %"), colorRed(risk)};
            }
            else {
                objects = new Object[] {conflict, behaviorDescription,  infLimit + " %", supLimit + " %", String.format("%.2f", percentage) + " %", risk};
            }
            
            vector.addElement(objects);
        }
        
        return vector;
    }
    
    public Group loadDatasetIntoGroup(String dataset) {
    	
    	Instances instances = Weka.loadDataset(dataset);
        
        Group group = new Group();
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = instance.stringValue(instanceIndex++);
            
            group.addInteraction(nombre, IpaBehavior.stringToIpaBehavior(conducta));
        }
        
        return group;
    }
    
    private String colorRed(String str) {
        
        return "<html><b style=\"color:#FF0000\">" + str +"</b><html>";
    }

}
