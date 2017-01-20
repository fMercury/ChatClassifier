package org.ipa;

import java.util.Hashtable;
import java.util.Set;

import org.enums.IpaBehavior;

public class Group {

	private Hashtable<String, Person> group;
	private Hashtable<IpaBehavior, Integer> interactions;
	private Hashtable<IpaBehavior, Double> percentages;
	
	public Group() {
		
		group = new Hashtable<String, Person>();
		interactions = new Hashtable<IpaBehavior, Integer>();
		percentages = new Hashtable<IpaBehavior, Double>();
	}
	
	public void addInteraction(String name, IpaBehavior behavior) {
		
		addPersonToGroup(name, behavior);
		addPersonInteractions(name, behavior);
		updatePercentages();
	}
	
	public double getPercentage(IpaBehavior behavior) {
		
		if (percentages.containsKey(behavior))
			return percentages.get(behavior);
		
		return 0;
	}
	
	private void addPersonToGroup(String name, IpaBehavior behavior) {
		
		if (group.containsKey(name)) {	
			
			Person person = group.get(name);
			person.addInteraction(behavior, 1);
			
			group.put(name, person);
		}
		else {
			Person person = new Person(name);
			person.addInteraction(behavior, 1);
			group.put(name, person);
		}
	}
	
	private void addPersonInteractions(String name, IpaBehavior behavior) {
		
		if (interactions.containsKey(behavior)) {
			int prevAmount = interactions.get(behavior);
			interactions.put(behavior, prevAmount + 1);
		}
		else {
			interactions.put(behavior, 1);
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
}
