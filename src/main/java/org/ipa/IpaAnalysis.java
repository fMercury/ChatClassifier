package org.ipa;

import java.util.List;
import java.util.Vector;

import org.enums.IpaBehavior;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

public class IpaAnalysis {
	
	private List<IpaGroup> groups;
	
    public IpaAnalysis(List<String> fileNames) {
    	
    	groups = new Vector<IpaGroup>();
    	
    	for (String file : fileNames) {    		
    		loadGroupInteractions(file);
        } 
    }
    
    public List<GroupAnalysisResult> analizeGroups() {
    	
        List<GroupAnalysisResult> analizeGroupResults = new Vector<GroupAnalysisResult>();
    	
    	for (IpaGroup group : groups) {
    		GroupAnalysisResult groupAnalysisResult = new GroupAnalysisResult(group.getName(), analyzeGroup(group));
    		analizeGroupResults.add(groupAnalysisResult);
    	}
    	
    	return analizeGroupResults;
    }
    
    public List<PersonAnalysisResult> createGroups(int size) {
        
      IpaGroup bigGroup = new IpaGroup("");
      
      for (IpaGroup group : groups) {
          bigGroup.addGroup(group);
      }
      
      List<IpaGroup> groupList = new Vector<IpaGroup>();
      int groupNumber = 1;
      while (bigGroup.size() > 0) {
          IpaPerson moreDeviatedPerson = bigGroup.getMoreDeviatedPerson();
          IpaGroup newGroup = new IpaGroup("Grupo" + groupNumber);
          newGroup.addPerson(moreDeviatedPerson);
          bigGroup.removePerson(moreDeviatedPerson);
          
          while (newGroup.size() < size && bigGroup.size() > 0) {
              IpaBehavior behavior = newGroup.getMoreDeviatedBehavior();
              IpaPerson personToLevelDeviation = bigGroup.getPersonToLevelDeviation(behavior, newGroup.getBehaviorDeviation(behavior));
              newGroup.addPerson(personToLevelDeviation);
              bigGroup.removePerson(personToLevelDeviation);
          }
          groupList.add(newGroup);
      }
      
      List<PersonAnalysisResult> analizePersonsResults = new Vector<PersonAnalysisResult>();
      // pasar toda la info en el grupo a la lista 'analizePersonsResults'
      
      return analizePersonsResults;
    }
    
    private void loadGroupInteractions(String file) {
    	
    	IpaGroup group = loadDatasetIntoGroup(file);
		groups.add(group);
    }
    
    private List<Object[]> analyzeGroup(IpaGroup group) {
           	
    	List<Object[]> vector = new Vector<Object[]>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.add(analizeGroupPercentages(behavior, group.getBehaviorPercentage(behavior)));
        }
        
        return vector;
    }
    
    private Object[] analizeGroupPercentages(IpaBehavior behavior, double percentage) {
    	
        String conflict = behavior.getConflict();
        String behaviorDescription = behavior.getCode() + ". " +  behavior.getDescription();
        int infLimit = IpaEdgeValues.getInferiorLimit(behavior);
        int supLimit = IpaEdgeValues.getSuperiorLimit(behavior);
        
        Object[] objects;
        objects = new Object[] {conflict, behaviorDescription,  infLimit + " %", supLimit + " %", String.format("%.2f", percentage) + " %"};

        if (percentage < infLimit || percentage > supLimit) {
            objects = paintItRed(objects);
        }
        
    	return objects;
    }
    
    private Object[] paintItRed(Object[] objects) {
        
        Object[] newObjects = objects;
        for (int i = 0; i < newObjects.length; i++) {
            newObjects[i] = "<html><b style=\"color:#FF0000\">" + newObjects[i] + "</b><html>";
        }

        return newObjects;
    }
    
    private IpaGroup loadDatasetIntoGroup(String dataset) {
    	
    	Instances instances = Weka.loadDataset(dataset);
        
        IpaGroup group = new IpaGroup(dataset);
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            
            int instanceIndex = 0;
            String behavior = instance.stringValue(instanceIndex++);
            String name = instance.stringValue(instanceIndex++);
            
            group.addInteraction(name, IpaBehavior.stringToIpaBehavior(behavior));
        }
        
        return group;
    }
}
