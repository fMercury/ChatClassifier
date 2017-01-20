package org.ipa;

import java.util.Hashtable;
import java.util.Set;

import org.enums.IpaBehavior;

public class Person {

	private String name;
	private Hashtable<IpaBehavior, Integer> interactions;
	private Hashtable<IpaBehavior, Double> percentages;
	
	public Person(String name) {
		
		this.name = name;
		interactions = new Hashtable<IpaBehavior, Integer>();
		percentages = new Hashtable<IpaBehavior, Double>();		
	}
	
	public String getName() {
		
		return name;
	}
	
	public int getInteractions(IpaBehavior behavior) {
		
		if (interactions.containsKey(behavior))
			return interactions.get(behavior).intValue();
		
		return 0;
	}
	
	public double getPercentage(IpaBehavior behavior) {
		
		if (percentages.containsKey(behavior))
			return percentages.get(behavior).doubleValue();
		
		return 0;
	}
	
	public void addInteraction(IpaBehavior behavior, int amount) {
		
		if (interactions.containsKey(behavior)) {
			int prevAmount = interactions.get(behavior);
			interactions.put(behavior, prevAmount + amount);
		}
		else {
			interactions.put(behavior, amount);
		}
		
		updatePercentages();			
	}
	
	private int getTotalInteractions() {		
		
		int total = 0;
		
		Set<IpaBehavior> keys = interactions.keySet();		
		for(IpaBehavior key: keys){			
            total += interactions.get(key).intValue();
        }
		
		return total;
	}
	
	private void updatePercentages() {
		
		int total = getTotalInteractions();
		for(IpaBehavior key : IpaBehavior.values()){
			
			int interactionAmount = 0;
			if (interactions.containsKey(key))
				interactionAmount = interactions.get(key);
			
			double value = (double)interactionAmount * (double)100 / (double)total;
			percentages.put(key, value);
        }	
	}
	
	
}
