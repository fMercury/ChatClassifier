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
	
	private static int BEHAVIOUR_INDEX = 0;
	private static int NAME_DIRECT_PROCCESING_INDEX = 1;
	private static int NAME_PHASES_PROCCESING_INDEX = 3;
			
	
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
            
            String behavior;
            String name;
            
            behavior = instance.stringValue(BEHAVIOUR_INDEX);
            if (instances.numAttributes() == 3 || instances.numAttributes() == 15)
            	name = instance.stringValue(NAME_DIRECT_PROCCESING_INDEX);
            else
            	name = instance.stringValue(NAME_PHASES_PROCCESING_INDEX);
            
            group.addInteraction(name, IpaBehavior.stringToIpaBehavior(behavior));
        }
        
        return group;
    }
    
    private List<AnalysisResult> getGroupAnalysisRowInfo(IpaGroup group) {
       	
    	List<AnalysisResult> vector = new Vector<AnalysisResult>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.add(analizeGroupPercentages(behavior, group.getBehaviorPercentage(behavior)));
        }
        
        return vector;
    }
    
    private AnalysisResult analizeGroupPercentages(IpaBehavior behavior, double percentage) {
    	
        String conflict = behavior.getConflict();
        String behaviorDescription = behavior.getCode() + ". " +  behavior.getDescription();
        int infLimit = IpaEdgeValues.getInferiorLimit(behavior);
        int supLimit = IpaEdgeValues.getSuperiorLimit(behavior);

        AnalysisResult analysisResult = new AnalysisResult(conflict, behaviorDescription, infLimit, supLimit, percentage);
        
    	return analysisResult;
    }
    
    public List<GroupCreationResult> createGroups(int size) {
        
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
      
      List<GroupCreationResult> analizePersonsResults = new Vector<GroupCreationResult>();
      // pasar toda la info en el grupo a la lista 'analizePersonsResults'
      for (IpaGroup group : groupList) {
    	  GroupCreationResult groupCreationResult = new GroupCreationResult(group.getName(), getGroupCreationRowInfo(group));
    	  analizePersonsResults.add(groupCreationResult);
      }    
      
      return analizePersonsResults;
    }
    
    private List<GroupCreation> getGroupCreationRowInfo(IpaGroup group) {
    	
    	List<GroupCreation> vector = new Vector<GroupCreation>();
    	Set<String> groupMembers = group.getGroupMembersNames();
    	
    	for (String name : groupMembers) {
    		IpaPerson person = group.getPerson(name);
    		vector.add(getItemInfoRow(person, false));
    	}
    	vector.add(getItemInfoRow(group, true));    	
        
        return vector;
    }
    
    private GroupCreation getItemInfoRow(IpaItem item, boolean paintItBlack) {
    	
    	String c1 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C1));
    	String c2 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C2));
    	String c3 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C3));
    	String c4 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C4));
    	String c5 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C5));
    	String c6 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C6));
    	String c7 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C7));
    	String c8 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C8));
    	String c9 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C9));
    	String c10 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C10));
    	String c11 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C11));
    	String c12 = String.format("%.2f", item.getBehaviorDeviation(IpaBehavior.C12));
    	String totalDeviation = String.format("%.2f", item.getTotalDeviation());
    	
    	GroupCreation groupCreation = new GroupCreation (item.getName(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, totalDeviation, paintItBlack);
    	
    	return groupCreation;
    }
}
