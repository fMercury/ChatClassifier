package org.ipa;

import java.util.List;

public class GroupCreationResult {
	
	String name;
	List<GroupCreation> groupCreations;
	
	public GroupCreationResult(String name, List<GroupCreation> analysisResult) {
		this.name = name;
		this.groupCreations = analysisResult;
	}

	public String getName() {
		return name;
	}
	
	public int getGroupSize() {
		return groupCreations.size() - 1;
	}

	public List<GroupCreation> getAnalysisResults() {
		return groupCreations;
	}
}
