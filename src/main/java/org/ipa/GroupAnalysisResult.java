package org.ipa;

import java.util.Vector;

public class GroupAnalysisResult {
	
	String name;
	Vector<Object[]> analysisResult;
	
	public GroupAnalysisResult(String name, Vector<Object[]> analysisResult) {
		this.name = name;
		this.analysisResult = analysisResult;
	}

	public String getName() {
		return name;
	}

	public Vector<Object[]> getAnalysisResult() {
		return analysisResult;
	}
}
