package org.ipa;

import java.io.File;
import java.util.Hashtable;
import java.util.Set;

import org.enums.IpaBehavior;


public class IpaGroup extends IpaItem{
    private Hashtable<String, IpaPerson> members;
    
    
    public IpaGroup(String name) {
        super(name.substring(name.lastIndexOf(File.separator) + 1, name.length()));
        
        members = new Hashtable<String, IpaPerson>();
    }
    
    public void addInteraction(String name, IpaBehavior behavior) {
        
        IpaPerson person;
        if (members.contains(name)) {
            person = members.get(name);
        }
        else {
            person = new IpaPerson(name);
        }
        
        person.addInteraction(behavior, 1);
        members.put(name, person);
    }
    
    public void addPerson(IpaPerson newPerson) {
        
        if (members.contains(newPerson.getName())) {
            IpaPerson person = members.get(newPerson.getName());
            for (IpaBehavior behavior : IpaBehavior.values())
                newPerson.addInteraction(behavior, person.getBehaviorInteractions(behavior));
        }
        
        members.put(newPerson.getName(), newPerson);
    }
    
    public void addGroup(IpaGroup group) {
        
        for (String name : group.getGroupNames()) {
            addPerson(group.getPerson(name));
        }
    }
    
    public Set<String> getGroupNames() {
        
        return members.keySet();
    }
    
    public IpaPerson getPerson(String name) {
        
        return members.get(name);
    }
    
    private int getTotalInteractions() {
        
        int total =  0;
        
        Set<String> membersNames = members.keySet();
        
        for (String name : membersNames) {
            
            IpaPerson person = members.get(name);
            total += person.getTotalInteractions();
        }
        
        return total;
    }
    
    public int getBehaviorInteractions(IpaBehavior behavior) {
        
        int total =  0;
        
        Set<String> membersNames = members.keySet();
        
        for (String name : membersNames) {
            
            IpaPerson person = members.get(name);
            total += person.getBehaviorInteractions(behavior);
        }
        
        return total;
    }
    
    public double getBehaviorPercentage(IpaBehavior behavior) {
        
        int totalInteractions = getTotalInteractions();
        
        return getBehaviorInteractions(behavior) * 100.0 / totalInteractions;
    }
    
    public double getBehaviorDeviation(IpaBehavior behavior) {
        
        double deviation = 0;
        Set<String> membersNames = members.keySet();
        for (String name : membersNames) {
            deviation += members.get(name).getBehaviorDeviation(behavior);
        }
        
        return deviation;
    }
    
    public IpaPerson getMoreDeviatedPerson() {
        
        IpaPerson person = null;
        double deviation = -1;
        
        Set<String> membersNames = members.keySet();
        for (String name : membersNames) {
            if (deviation < members.get(name).getTotalDeviation()) {
                deviation = members.get(name).getTotalDeviation();
                person = members.get(name);
            }
        }
        
        return person;
    }
    
    public IpaBehavior getMoreDeviatedBehavior() {
        
        IpaBehavior behavior = null;
        double deviation = 0;
                
        for (IpaBehavior key : IpaBehavior.values()) {
            if (deviation < Math.abs(getBehaviorDeviation(key))) {
                deviation = Math.abs(getBehaviorDeviation(key));
                behavior = key;
            }
        }
        
        return behavior;
    }
    
    public void removePerson(IpaPerson person) {
        
        members.remove(person.getName());
    }
    
    public int size() {
        
        return members.size();
    }
    
    public IpaPerson getPersonToLevelDeviation(IpaBehavior behavior, double deviation) {
        
        IpaPerson person = null;
        double levelDeviation = Double.MAX_VALUE;
        
        Set<String> membersNames = members.keySet();
        for (String name : membersNames) {
            if (levelDeviation > Math.abs(members.get(name).getBehaviorDeviation(behavior) + deviation)) {
                levelDeviation = Math.abs(members.get(name).getBehaviorDeviation(behavior) + deviation);
                person = members.get(name);
                if (levelDeviation == 0)
                    break;
            }
        }
        
        return person;
    }
   
    @Override
    public double getTotalDeviation() {
        return 0;
    }


}


/*package org.ipa;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.enums.IpaBehavior;

public class IpaGroup extends IpaItem{

	private Hashtable<String, IpaPerson> members;
	private Hashtable<IpaBehavior, Integer> interactions;
	private Hashtable<IpaBehavior, Double> percentages;
	private Hashtable<IpaBehavior, Double> deviations;
	
	public IpaGroup(String name) {
		super(name.substring(name.lastIndexOf(File.separator) + 1, name.length()));
		interactions = new Hashtable<IpaBehavior, Integer>();
		percentages = new Hashtable<IpaBehavior, Double>();
		members = new Vector<IpaItem>();
	}
	
	public void addInteraction(String name, IpaBehavior behavior) {
		
		addPersonInteractions(name, behavior, 1);
		updatePercentages();
		updateDeviations();
	}
	
	public void addPerson(IpaPerson person) {
		
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
	
	public List<IpaItem> getMembers() {
		
		return members;
	}
	
	private void addPersonInteractions(String name, IpaBehavior behavior, int amount) {
		
		if (!members.contains(name)) {
		    IpaPerson person = new IpaPerson(name);
		    person.addInteraction(behavior, amount);
		    members.put(name, person);
		}
		else {
		    IpaPerson person = members.get(name);
		    person.addInteraction(behavior, amount);
		}
		
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
}*/
