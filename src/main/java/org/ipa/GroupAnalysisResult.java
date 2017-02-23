package org.ipa;

import java.util.List;

public class GroupAnalysisResult {
	
	String name;
	List<AnalysisResult> analysisResults;
	
	public GroupAnalysisResult(String name, List<AnalysisResult> analysisResult) {
		this.name = name;
		this.analysisResults = analysisResult;
	}

	public String getName() {
		return name;
	}

	public List<AnalysisResult> getAnalysisResults() {
		return analysisResults;
	}
}
