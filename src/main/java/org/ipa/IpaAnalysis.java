package org.ipa;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.enums.IpaBehavior;
import org.weka.Weka;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Realiza todo el análisis de los datos
 * @author martinmineo
 *
 */
public class IpaAnalysis {
	
	private List<IpaGroup> groups;
	
	private static int BEHAVIOUR_INDEX = 0;
	private static int NAME_DIRECT_PROCCESING_INDEX = 1;
	private static int NAME_PHASES_PROCCESING_INDEX = 3;
			
	
	/**
	 * Constructor
	 * @param fileNames List<String> Lita de String de los archivos a analizar
	 */
    public IpaAnalysis(List<String> fileNames) {
    	
    	groups = new Vector<IpaGroup>();
    	
    	for (String file : fileNames) {    		
    		loadGroupInteractions(file);
        } 
    }
    
    /**
     * Analiza los grupos
     * @return List<GroupAnalysisResult> Lista de GroupAnalysisResult con los resultados del análisis
     */
    public List<GroupAnalysisResult> analizeGroups() {
    	
        List<GroupAnalysisResult> analizeGroupResults = new Vector<GroupAnalysisResult>();
    	
    	for (IpaGroup group : groups) {
    		GroupAnalysisResult groupAnalysisResult = new GroupAnalysisResult(group.getName(), getGroupAnalysisRowInfo(group));
    		analizeGroupResults.add(groupAnalysisResult);
    	}
    	
    	return analizeGroupResults;
    }
    
    /**
     * Carga en groups los datos del grupo pasado por parámetro
     * @param file String Archivo que contiene los datos del grupo
     */
    private void loadGroupInteractions(String file) {
    	
    	IpaGroup group = loadDatasetIntoGroup(file);
		groups.add(group);
    }
    
    /**
     * Dado un dataset, carga los datos en un IpaGroup
     * @param dataset String Archivo que contiene el dataset
     * @return IpaGroup Grupo con los datos cargados
     */
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
    
    /**
     * Analiza al grupo pasado por parámetro y genera una lista de AnalysisResult con los resultados del análisis
     * @param group IpaGroup Grupo a analizar
     * @return List<AnalysisResult> Lista con los resultados del análisis del grupo
     */
    private List<GroupAnalysisRow> getGroupAnalysisRowInfo(IpaGroup group) {
       	
    	List<GroupAnalysisRow> vector = new Vector<GroupAnalysisRow>();
        
        for (IpaBehavior behavior : IpaBehavior.values()) {        	
        	vector.add(getGroupAnalysisRow(behavior, group.getBehaviorPercentage(behavior)));
        }
        
        return vector;
    }
    
    /**
     * Genera cada item de la lista para generar el análisis de cada grupo 
     * @param behavior IpaBehavior Comportamiento
     * @param percentage double Porcentaje
     * @return AnalysisResult Item del análisis del grupo
     */
    private GroupAnalysisRow getGroupAnalysisRow(IpaBehavior behavior, double percentage) {
    	
        String conflict = behavior.getConflict();
        String behaviorDescription = behavior.getCode() + ". " +  behavior.getDescription();
        int infLimit = IpaEdgeValues.getInferiorLimit(behavior);
        int supLimit = IpaEdgeValues.getSuperiorLimit(behavior);

        GroupAnalysisRow analysisResult = new GroupAnalysisRow(conflict, behaviorDescription, infLimit, supLimit, percentage);
        
    	return analysisResult;
    }
    
    /**
     * Crea grupos del tamaño deseado nivelando los conflictos 
     * @param size int Tamaño de cada grupo
     * @return List<GroupCreationResult> Lista de GroupCreationResult con la creación de los grupos
     */
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
      
      List<GroupCreationResult> groupCreationResults = new Vector<GroupCreationResult>();
      // pasar toda la info en el grupo a la lista 'groupCreationResults'
      for (IpaGroup group : groupList) {
    	  GroupCreationResult groupCreationResult = new GroupCreationResult(group.getName(), getGroupCreationRowInfo(group));
    	  groupCreationResults.add(groupCreationResult);
      }    
      
      return groupCreationResults;
    }
    
    /**
     * Genera una lista que contiene los resultados de la creación del grupo
     * @param group IpaGroup Grupo que se utilizará para generar la lista
     * @return List<GroupCreation> Lista con los datos de la creación del grupo
     */
    private List<GroupCreationRow> getGroupCreationRowInfo(IpaGroup group) {
    	
    	List<GroupCreationRow> vector = new Vector<GroupCreationRow>();
    	Set<String> groupMembers = group.getGroupMembersNames();
    	
    	for (String name : groupMembers) {
    		IpaPerson person = group.getPerson(name);
    		vector.add(getItemInfoRow(person, false));
    	}
    	vector.add(getItemInfoRow(group, true));    	
        
        return vector;
    }
    
    /**
     * Genera la información de un IpaItem (IpaPreson o IpaGroup)
     * @param item IpaItem Item a convertir a un objeto GroupCreation 
     * @param paintItBlack boolean Determina si este item se debe pintar de negro o no
     * @return GroupCreation Devuelve la creación del objeto a partir del IpaItem
     */
    private GroupCreationRow getItemInfoRow(IpaItem item, boolean paintItBlack) {
    	
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
    	
    	GroupCreationRow groupCreation = new GroupCreationRow (item.getName(), c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, totalDeviation, paintItBlack);
    	
    	return groupCreation;
    }
}
