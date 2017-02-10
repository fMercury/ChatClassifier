package org.ipa;

public abstract class IPAItem {
	private String name;
	
	public IPAItem(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract double getTotalDeviation();
}
