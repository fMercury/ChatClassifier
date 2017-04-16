package org.ipa;

import java.io.File;
import java.util.Hashtable;
import java.util.Set;

import org.enums.IpaBehavior;


/**
 * Contiene todos los datos del grupo
 * @author martinmineo
 *
 */
public class IpaGroup extends IpaItem{
    private Hashtable<String, IpaPerson> members;
    
    /**
     * Constructor
     * @param name String Nombre del grupo
     */
    public IpaGroup(String name) {
        super(name.substring(name.lastIndexOf(File.separator) + 1, name.length()));
        
        members = new Hashtable<String, IpaPerson>();
    }
    
    /**
     * Agrega una interacción al grupo
     * @param personName String nombre de la persona 
     * @param behavior IpaBehavior Comportamiento
     */
    public void addInteraction(String personName, IpaBehavior behavior) {
        
        IpaPerson person;
        if (members.containsKey(personName)) {
            person = members.get(personName);
        }
        else {
            person = new IpaPerson(personName);
        }
        
        person.addInteraction(behavior, 1);
        members.put(personName, person);
    }
    
    /**
     * Agrega una persona al grupo
     * @param newPerson IpaPerson Persona
     */
    public void addPerson(IpaPerson newPerson) {
        
        if (members.containsKey(newPerson.getName())) {
            IpaPerson person = members.get(newPerson.getName());
            for (IpaBehavior behavior : IpaBehavior.values())
                newPerson.addInteraction(behavior, person.getBehaviorInteractions(behavior));
        }
        
        members.put(newPerson.getName(), newPerson);
    }
    
    /**
     * Agrega un grupo de personas al grupo
     * @param group IpaGroup Grupo
     */
    public void addGroup(IpaGroup group) {
        
        for (String name : group.getGroupMembersNames()) {
            addPerson(group.getPerson(name));
        }
    }
    
    /**
     * Devuelve el nombre de los miembros del grupo
     * @return Set<String> Nombre de los miembros del grupo
     */
    public Set<String> getGroupMembersNames() {
        
        return members.keySet();
    }
    
    /**
     * Devuelve el IpaPerson deseado
     * @param name String Nombre de la persona buscada
     * @return IpaPerson Persona
     */
    public IpaPerson getPerson(String name) {
        
        return members.get(name);
    }
    
    /**
     * Devuelve el total de interacciones del grupo
     * @return int Total de interacciones
     */
    private int getTotalInteractions() {
        
        int total =  0;
        
        Set<String> membersNames = members.keySet();
        
        for (String name : membersNames) {
            
            IpaPerson person = members.get(name);
            total += person.getTotalInteractions();
        }
        
        return total;
    }
    
    /**
     * Devuelve el total de interacciones para un Comportamiento
     * @return int Total de interacciones para un Comportamiento
     */
    public int getBehaviorInteractions(IpaBehavior behavior) {
        
        int total =  0;
        
        Set<String> membersNames = members.keySet();
        
        for (String name : membersNames) {
            
            IpaPerson person = members.get(name);
            total += person.getBehaviorInteractions(behavior);
        }
        
        return total;
    }
    
    /**
     * Devuelve el porcentaje de interacciones para un Comportamiento
     * @param behavior IpaBehavior Comportamiento
     * @return double Porcentaje de interacciones
     */
    public double getBehaviorPercentage(IpaBehavior behavior) {
        
        int totalInteractions = getTotalInteractions();
        
        return (double)getBehaviorInteractions(behavior) * 100.0 / (double)totalInteractions;
    }
    
    /**
     * Devuelve la desviación para un comportamiento
     * @param behavior IpaBehavior Comportameinto
     * @return double Desviación
     */
    public double getBehaviorDeviation(IpaBehavior behavior) {
        
        double deviation = 0;
        Set<String> membersNames = members.keySet();
        for (String name : membersNames) {
            deviation += members.get(name).getBehaviorDeviation(behavior);
        }
        
        return deviation;
    }
    
    /**
     * Devuelve la persona con mayor desvío en el grupo
     * @return IpaPerson Persona con mayor desvío
     */
    public IpaPerson getMoreDeviatedPerson() {
        
        IpaPerson person = null;
        double deviation = -1;
        
        Set<String> membersNames = members.keySet();
        for (String name : membersNames) {
            if (deviation < members.get(name).getTotalDeviation(false)) {
                deviation = members.get(name).getTotalDeviation(false);
                person = members.get(name);
            }
        }
        
        if (person == null) {
            person = members.elements().nextElement();
        }
        
        return person;
    }
    
    /**
     * Devuelve el comportamiento más desviado en el grupo
     * @return IpaBehavior Comportamiento con mayor desvío en el grupo
     */
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
    
    /**
     * Elimina una persona del grupo
     * @param person IpaPerson Persona a eliminar del grupo
     */
    public void removePerson(IpaPerson person) {
        
        members.remove(person.getName());
    }
    
    /**
     * Devuelve el tamaño del grupo
     * @return int Tamaño del grupo
     */
    public int size() {
        
        return members.size();
    }
    
    /**
     * Busca la persona más indicada para nivelar la desviación del grupo
     * @param behavior IpaBehavior Comportamiento
     * @param deviation double Valor del desvío
     * @return IpaPerson Persona que mejor nivela al grupo en el Comportamiento pasado por parámetro
     */
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