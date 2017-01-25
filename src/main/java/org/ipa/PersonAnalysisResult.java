package org.ipa;

import java.util.Vector;

public class PersonAnalysisResult {
	
	String name;
	Vector<Object[]> analysisResult;
	
	public PersonAnalysisResult(String name, Vector<Object[]> analysisResult) {
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
