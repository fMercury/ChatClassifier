package org.ipa;

import org.enums.IpaBehavior;

public abstract class IpaItem {
	private String name;
	
	public IpaItem(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract int getBehaviorInteractions(IpaBehavior behavior);
	public abstract double getTotalDeviation();
}
