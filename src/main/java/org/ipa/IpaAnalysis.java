package org.ipa;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.enums.IpaBehavior;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

public class IpaAnalysis {
	
	private Vector<Group> groups;
	private Hashtable<String, Person> persons;
	
	Vector<GroupAnalysisResult> analizeGroupResults;
	Vector<PersonAnalysisResult> analizePersonsResults;
    
    public IpaAnalysis(List<String> fileNames) {
    	
    	groups = new Vector<Group>();
    	persons = new Hashtable<String, Person>();
    	
    	for (String file : fileNames) {    		
    		loadGroupInteractions(file);
        } 
    }
    
    public void analize() {
    	
    	analizeGroupResults = new Vector<GroupAnalysisResult>();
    	
    	for (Group group : groups) {
    		GroupAnalysisResult groupAnalysisResult = new GroupAnalysisResult(group.getGroupName(), analyzeGroup(group));
    		analizeGroupResults.addElement(groupAnalysisResult);
    	}
    	
    	analizePersonsResults = new Vector<PersonAnalysisResult>();
    	
    	Set<String> keys = persons.keySet();	
    	for (String name : keys) {
    		PersonAnalysisResult personAnalysisResult = new PersonAnalysisResult(persons.get(name).getName(), analizePerson(persons.get(name)));
    		analizePersonsResults.addElement(personAnalysisResult);
    	}
    }
    
    public Vector<GroupAnalysisResult> getGroupAnalysisResults() {
    	return analizeGroupResults;
    }
    
    public Vector<PersonAnalysisResult> getPersonsAnalysisResults() {
    	return analizePersonsResults;
    }
    
    private void loadGroupInteractions(String file) {
    	
    	Group group = loadDataset(file);
		groups.addElement(group);
    }
    
	private void addPersonInteractions(String name, IpaBehavior behavior) {
		
		if (persons.containsKey(name)) {	
			
			Person person = persons.get(name);
			person.addInteraction(behavior, 1);
			
			persons.put(name, person);
		}
		else {
			Person person = new Person(name);
			person.addInteraction(behavior, 1);
			persons.put(name, person);
		}
	}
    
    private Vector<Object[]> analyzeGroup(Group group) {
           	
    	Vector<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.addElement(analize(behavior, group.getPercentage(behavior)));
        }
        
        return vector;
    }
    
    private Vector<Object[]> analizePerson(Person person) {
		
    	Vector<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.addElement(analize(behavior, person.getPercentage(behavior)));
        }
        
        return vector;
	}
    
    private Object[] analize(IpaBehavior behavior, double percentage) {
    	
        String conflict = behavior.getConflict();
        String behaviorDescription = behavior.getCode() + ". " +  behavior.getDescription();
        int infLimit = IpaEdgeValues.getInferiorLimit(behavior);
        int supLimit = IpaEdgeValues.getSuperiorLimit(behavior);
        String risk = "";
        
        Object[] objects;
        
        if (percentage < infLimit || percentage > supLimit) {
            risk = "Sí";
            objects = new Object[] {colorRed(conflict), colorRed(behaviorDescription),  colorRed(infLimit + " %"), colorRed(supLimit + " %"), colorRed(String.format("%.2f", percentage) + " %"), colorRed(risk)};
        }
        else {
            objects = new Object[] {conflict, behaviorDescription,  infLimit + " %", supLimit + " %", String.format("%.2f", percentage) + " %", risk};
        }
            
    	return objects;
    }
    
    private Group loadDataset(String dataset) {
    	
    	Instances instances = Weka.loadDataset(dataset);
        
        Group group = new Group(dataset);
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            
            int instanceIndex = 0;
            String conducta = instance.stringValue(instanceIndex++);
            String nombre = instance.stringValue(instanceIndex++);
            
            group.addInteraction(nombre, IpaBehavior.stringToIpaBehavior(conducta));
            addPersonInteractions(nombre, IpaBehavior.stringToIpaBehavior(conducta));
        }
        
        return group;
    }
    
    private String colorRed(String str) {
        
        return "<html><b style=\"color:#FF0000\">" + str +"</b><html>";
    }

}
