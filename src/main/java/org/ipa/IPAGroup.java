package org.ipa;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.enums.IpaBehavior;

public class IPAGroup {

	private String name;
	private List<String> members;
	private Hashtable<IpaBehavior, Integer> interactions;
	private Hashtable<IpaBehavior, Double> percentages;
	private Hashtable<IpaBehavior, Double> deviations;
	
	public IPAGroup(String groupName) {
		
		this.name = groupName.substring(groupName.lastIndexOf(File.separator) + 1, groupName.length());
		interactions = new Hashtable<IpaBehavior, Integer>();
		percentages = new Hashtable<IpaBehavior, Double>();
		members = new Vector<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addInteraction(String name, IpaBehavior behavior) {
		
		addPersonInteractions(name, behavior, 1);
		updatePercentages();
		updateDeviations();
	}
	
	public void addPerson(IPAPerson person) {
		
		for(IpaBehavior key : IpaBehavior.values()) {
			addPersonInteractions(person.getName(), key, person.getInteractions(key));
        }
		updatePercentages();
	}
	
	public IpaBehavior getMoreDeviatedBehavior() {
		
		updateDeviations();
		IpaBehavior deviatedBehavior = null;
		double deviation = 0;
		for(IpaBehavior key : IpaBehavior.values()){		
			if (deviation < Math.abs(deviations.get(key))){
				deviation = Math.abs(deviations.get(key));
				deviatedBehavior = key;
			}
		}
	
		return deviatedBehavior;
	}
	
	public double getBehaviorDeviation(IpaBehavior behavior) {
		
		if (deviations.containsKey(behavior))
			return deviations.get(behavior);
		return 0;
	}
	
	public double getPercentage(IpaBehavior behavior) {
		
		if (percentages.containsKey(behavior))
			return percentages.get(behavior);
		
		return 0;
	}
	
	public List<String> getMembers() {
		
		return members;
	}
	
	private void addPersonInteractions(String name, IpaBehavior behavior, int amount) {
		
		if (!members.contains(name))
			members.add(name);
		
		if (interactions.containsKey(behavior)) {
			int prevAmount = interactions.get(behavior);
			interactions.put(behavior, prevAmount + amount);
		}
		else {
			interactions.put(behavior, amount);
		}
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
	
	private void updateDeviations() {
		
		deviations = new Hashtable<IpaBehavior, Double>();
		for(IpaBehavior key : IpaBehavior.values()){
			
			double percentage = percentages.get(key);
			
			double inferiorLimit, superiorLimit;
			inferiorLimit = IpaEdgeValues.getInferiorLimit(key);
			superiorLimit = IpaEdgeValues.getSuperiorLimit(key);
			
			if (percentage < inferiorLimit) {
				deviations.put(key, inferiorLimit - percentage);
			}
			else {
				if (percentage > superiorLimit) {
					deviations.put(key, percentage - superiorLimit);
				}
				else 
					deviations.put(key, new Double(0));
			}
        }
	}
}
