package org.ipa;

import java.util.List;
import java.util.Set;
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
    		GroupAnalysisResult groupAnalysisResult = new GroupAnalysisResult(group.getName(), getGroupAnalysisRowInfo(group));
    		analizeGroupResults.add(groupAnalysisResult);
    	}
    	
    	return analizeGroupResults;
    }
    
    private void loadGroupInteractions(String file) {
    	
    	IpaGroup group = loadDatasetIntoGroup(file);
		groups.add(group);
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
    
    private List<Object[]> getGroupAnalysisRowInfo(IpaGroup group) {
       	
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
    
    public List<PersonAnalysisResult> createGroups(int size) {
        
      IpaGroup bigGroup = new IpaGroup("");
      
      for (IpaGroup group : groups) {
          bigGroup.addGroup(group);
      }
      
      List<IpaGroup> groupList = new Vector<IpaGroup>();
      int groupNumber = 1;
      while (bigGroup.size() > 0) {
          IpaPerson moreDeviatedPerson = bigGroup.getMoreDeviatedPerson();
          IpaGroup newGroup = new IpaGroup("Grupo" + groupNumber++);
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
      for (IpaGroup group : groupList) {
    	  PersonAnalysisResult personAnalysisResult = new PersonAnalysisResult(group.getName(), getGroupCreationRowInfo(group));
    	  analizePersonsResults.add(personAnalysisResult);
      }    
      
      return analizePersonsResults;
    }
    
    private List<Object[]> getGroupCreationRowInfo(IpaGroup group) {
    	
    	List<Object[]> vector = new Vector<Object[]>();
    	Set<String> groupMembers = group.getGroupMembersNames();
    	
    	for (String name : groupMembers) {
    		IpaPerson person = group.getPerson(name);
    		vector.add(getItemInfoRow(person));
    	}
    	vector.add(paintItBlack(getItemInfoRow(group)));    	
        
        return vector;
    }
    
    private Object[] getItemInfoRow(IpaItem item) {
    	
    	String c1 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C1));
    	String c2 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C2));
    	String c3 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C3));
    	String c4 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C4));
    	String c5 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C5));
    	String c6 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C6));
    	String c7 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C7));
    	String c8 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C8));
    	String c9 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C9));
    	String c10 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C10));
    	String c11 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C11));
    	String c12 = String.valueOf(item.getBehaviorDeviation(IpaBehavior.C12));
    	String totalDeviation = String.valueOf(item.getTotalDeviation());
    	
    	Object[] objects = new Object[] {item.getName(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, totalDeviation};
    	
    	return objects;
    }
    
    private Object[] paintIt(Object[] objects, String color) {
        
        Object[] newObjects = objects;
        for (int i = 0; i < newObjects.length; i++) {
            newObjects[i] = "<html><b style=\"color:" + color + "\">" + newObjects[i] + "</b><html>";
        }

        return newObjects;
    }
    
    private Object[] paintItRed(Object[] objects) {
        
    	return paintIt(objects, "#FF0000");
    }
    
    private Object[] paintItBlack(Object[] objects) {
        
    	return paintIt(objects, "#000000");
    }
}
