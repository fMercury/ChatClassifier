package org.ipa;

import java.util.List;

/**
 * Contiene la información del análisis de lo grupo
 * @author martinmineo
 *
 */
public class GroupAnalysisResult {
	
	String name;
	List<GroupAnalysisRow> analysisResults;
	
	/**
	 * Constructor
	 * @param name String Nombre del grupo
	 * @param analysisResult List<AnalysisResult> Lista con los resultados del análisis
	 */
	public GroupAnalysisResult(String name, List<GroupAnalysisRow> analysisResult) {
		this.name = name;
		this.analysisResults = analysisResult;
	}

	/**
	 * Devuelve el nombre del grupo
	 * @return String Nombre del grupo
	 */
	public String getName() {
		return name;
	}

	/**
	 * Devuelve el resultado del análisis del grupo
	 * @return List<AnalysisResult> Análisis del grupo
	 */
	public List<GroupAnalysisRow> getAnalysisResults() {
		return analysisResults;
	}
}
