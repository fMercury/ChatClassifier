package org.ipa;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.enums.IpaBehavior;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

public class IpaAnalysis {
	
	private List<IPAGroup> groups;
	private Hashtable<String, IPAPerson> personsHash;
	
	List<GroupAnalysisResult> analizeGroupResults;
	List<PersonAnalysisResult> analizePersonsResults;
    
    public IpaAnalysis(List<String> fileNames) {
    	
    	groups = new Vector<IPAGroup>();
    	personsHash = new Hashtable<String, IPAPerson>();
    	
    	for (String file : fileNames) {    		
    		loadGroupInteractions(file);
        } 
    }
    
    public void analize() {
    	
    	analizeGroupResults = new Vector<GroupAnalysisResult>();
    	
    	for (IPAGroup group : groups) {
    		GroupAnalysisResult groupAnalysisResult = new GroupAnalysisResult(group.getName(), analyzeGroup(group));
    		analizeGroupResults.add(groupAnalysisResult);
    	}
    	
    	analizePersonsResults = new Vector<PersonAnalysisResult>();
    	
    	Set<String> keys = personsHash.keySet();	
    	for (String name : keys) {
    		PersonAnalysisResult personAnalysisResult = new PersonAnalysisResult(personsHash.get(name).getName(), analizePerson(personsHash.get(name)));
    		analizePersonsResults.add(personAnalysisResult);
    	}
    }
    
    public double getPersonDeviation(String name) {
    	
    	return personsHash.get(name).getTotalDeviation();
    }
    
    public List<GroupAnalysisResult> getGroupAnalysisResults() {
    	return analizeGroupResults;
    }
    
    public List<PersonAnalysisResult> getPersonsAnalysisResults() {
    	
    	return analizePersonsResults;
    }
    
    public List<IPAGroup> createGroups(int groupSize) {
    	
    	List<IPAPerson> personList = new Vector<IPAPerson>();
    	
    	Set<String> keys = personsHash.keySet();		
		for(String key: keys){
			personList.add(personsHash.get(key));
        }
		
		Collections.sort(personList, Collections.reverseOrder());
		List<IPAGroup> groupList = new Vector<IPAGroup>();
		
		int groupNumber = 1;
		while (personList.size() > 0) {
			IPAPerson first = personList.remove(0);
			IPAGroup newGroup = new IPAGroup("Grupo " + groupNumber++);
			int groupMembers = 1;
			
			newGroup.addPerson(first);
			while (groupMembers < groupSize && personList.size() > 0) {
				IpaBehavior deviatedBehavior = newGroup.getMoreDeviatedBehavior();
				double deviation = newGroup.getBehaviorDeviation(deviatedBehavior);
				
				newGroup.addPerson(getPersonToLevelDeviation(personList, deviatedBehavior, deviation));				
				groupMembers++;
			}
			groupList.add(newGroup);
		}
		
		return groupList;
    }
    
    private IPAPerson getPersonToLevelDeviation(List<IPAPerson> list, IpaBehavior behavior, double deviation) {
    	
    	double deviationToLevel;
    	if (deviation > 0)
    		deviationToLevel = 1;
    	else 
    		deviationToLevel = -1;
    	
    	int index = 0;
    	for (int i = 0; i < list.size(); i++) {
    		IPAPerson p = list.get(i);
    		if (deviation > 0) {
    			if (deviationToLevel > p.getDeviation(behavior)) {
    				deviationToLevel = p.getDeviation(behavior);
    				index = i;
    			}
    		}
    		else {
    			if (deviationToLevel < p.getDeviation(behavior)) {
    				deviationToLevel = p.getDeviation(behavior);
    				index = i;
    			}
    		}
    	}
    	
    	return list.remove(index);
    }
    
    private void loadGroupInteractions(String file) {
    	
    	IPAGroup group = loadDataset(file);
		groups.add(group);
    }
    
	private void addPersonInteractions(String name, IpaBehavior behavior) {
		
		if (personsHash.containsKey(name)) {	
			
			IPAPerson person = personsHash.get(name);
			person.addInteraction(behavior, 1);
			
			personsHash.put(name, person);
		}
		else {
			IPAPerson person = new IPAPerson(name);
			person.addInteraction(behavior, 1);
			personsHash.put(name, person);
		}
	}
    
    private List<Object[]> analyzeGroup(IPAGroup group) {
           	
    	List<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.add(analize(behavior, group.getPercentage(behavior)));
        }
        
        return vector;
    }
    
    private List<Object[]> analizePerson(IPAPerson person) {
		
    	List<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.add(analize(behavior, person.getPercentage(behavior)));
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
        	risk = "No";
            objects = new Object[] {conflict, behaviorDescription,  infLimit + " %", supLimit + " %", String.format("%.2f", percentage) + " %", risk};
        }
            
    	return objects;
    }
    
    private IPAGroup loadDataset(String dataset) {
    	
    	Instances instances = Weka.loadDataset(dataset);
        
        IPAGroup group = new IPAGroup(dataset);
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
