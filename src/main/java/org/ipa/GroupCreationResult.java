package org.ipa;

import java.util.List;

/**
 * Contiene la información de la creación del grupo
 * @author martinmineo
 *
 */
public class GroupCreationResult {
	
	String name;
	List<GroupCreationRow> groupCreations;
	
	/**
	 * Constructor
	 * @param name String Nombre del grupo
	 * @param analysisResult List<GroupCreation> Lista de los resultados de la creación del grupo
	 */
	public GroupCreationResult(String name, List<GroupCreationRow> analysisResult) {
		this.name = name;
		this.groupCreations = analysisResult;
	}

	/**
	 * Devuelve el nombre del grupo
	 * @return String Nombre del grupo
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Devuelve el tamaño del grupo
	 * @return int Tamaño del grupo
	 */
	public int getGroupSize() {
		return groupCreations.size() - 1;
	}

	/**
	 * Devuelve los resultados de la creación del grupo
	 * @return List<GroupCreation> Resultados de la creación del grupo 
	 */
	public List<GroupCreationRow> getAnalysisResults() {
		return groupCreations;
	}
}
