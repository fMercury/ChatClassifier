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
	public abstract double getBehaviorDeviation(IpaBehavior behavior);
	protected abstract boolean absoluteDeviation();
	
    public double getTotalDeviation() {
    	  
        double totalDeviation = 0;
    
        for(IpaBehavior behavior : IpaBehavior.values()){
        	if (absoluteDeviation())
        		totalDeviation += Math.abs(getBehaviorDeviation(behavior));
        	else
        		totalDeviation += getBehaviorDeviation(behavior);
        }
  
        return totalDeviation;
    }   
}
