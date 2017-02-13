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
        
        for (String name : group.getGroupMembersNames()) {
            addPerson(group.getPerson(name));
        }
    }
    
    public Set<String> getGroupMembersNames() {
        
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
	protected boolean absoluteDeviation() {		
		return false;
	}
   
}