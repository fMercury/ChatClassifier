package org.ipa;

import java.util.Hashtable;
import java.util.Set;

import org.enums.IpaBehavior;

public class IPAPerson implements Comparable<IPAPerson> {

	private String name;
	private Hashtable<IpaBehavior, Integer> interactions;
	private Hashtable<IpaBehavior, Double> percentages;
	private Hashtable<IpaBehavior, Double> deviation;
	private double totalDeviation;
	
	public IPAPerson(String name) {
		
		this.name = name;
		interactions = new Hashtable<IpaBehavior, Integer>();
		percentages = new Hashtable<IpaBehavior, Double>();	
		totalDeviation = 0;
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
	
	public double getTotalDeviation() {
		
		updateDeviation();
		return totalDeviation;
	}
	
	public double getDeviation(IpaBehavior behavior) {
		
		if (deviation.contains(behavior))
			return deviation.get(behavior);
		
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
	
	private void updateDeviation() {
		
		totalDeviation = 0;
		deviation = new Hashtable<IpaBehavior, Double>();
		
		for(IpaBehavior key : IpaBehavior.values()){
			
			double percentage = percentages.get(key);
			
			double inferiorLimit, superiorLimit;
			inferiorLimit = IpaEdgeValues.getInferiorLimit(key);
			superiorLimit = IpaEdgeValues.getSuperiorLimit(key);
			
			if (percentage < inferiorLimit) {
				deviation.put(key, inferiorLimit - percentage);
				totalDeviation += inferiorLimit - percentage;
			}
			else {
				if (percentage > superiorLimit) {
					deviation.put(key, percentage - superiorLimit);
					totalDeviation += percentage - superiorLimit;
				}
			}
        }	
	}

	@Override
	public int compareTo(IPAPerson p) {

		if (totalDeviation < p.getTotalDeviation()) return -1;
		if (totalDeviation == p.getTotalDeviation()) return 0;
		if (totalDeviation > p.getTotalDeviation()) return 1;
		
		return 0;
	}
	
	
	
	
	
}
