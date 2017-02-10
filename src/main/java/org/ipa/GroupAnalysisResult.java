package org.ipa;

import java.util.List;

public class GroupAnalysisResult {
	
	String name;
	List<Object[]> analysisResult;
	
	public GroupAnalysisResult(String name, List<Object[]> analysisResult) {
		this.name = name;
		this.analysisResult = analysisResult;
	}

	public String getName() {
		return name;
	}

	public List<Object[]> getAnalysisResult() {
		return analysisResult;
	}
}
