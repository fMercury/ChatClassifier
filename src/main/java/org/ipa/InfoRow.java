package org.ipa;

public class InfoRow {

    protected Object[] paintIt(Object[] objects, String color) {
        
        Object[] newObjects = objects;
        for (int i = 0; i < newObjects.length; i++) {
            newObjects[i] = "<html><b style=\"color:" + color + "\">" + newObjects[i] + "</b><html>";
        }

        return newObjects;
    }
}
