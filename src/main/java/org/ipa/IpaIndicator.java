package org.ipa;

import org.enums.IpaBehavior;

public class IpaIndicator {	

	private IpaBehavior behavior;
	private int inferiorLimit;
	private int superiorLimit;
	
	public IpaIndicator(IpaBehavior behavior, int inferiorLimit, int superiorLimit) {
		
		this.behavior = behavior;
		this.inferiorLimit = inferiorLimit;
		this.superiorLimit = superiorLimit;
	}
	
	public String getCode() {
		return behavior.getCode();
	}

	public String getBehavior() {
		return behavior.getDescription();
	}

	public int getInferiorLimit() {
		return inferiorLimit;
	}

	public int getSuperiorLimit() {
		return superiorLimit;
	}	
}
