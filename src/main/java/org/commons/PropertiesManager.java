package org.commons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesManager {
    private Properties prop;
    private InputStream input;
    private String filename;

    public PropertiesManager(String filename) {
        
        this.filename = filename;
        prop = new Properties();
        input = null;
        try {

            input = new FileInputStream(filename);
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProperty(String property) {

        return prop.getProperty(property);
    }
    
    public void storeProperty(String key, String value) {
        
        prop.setProperty(key, value);
        
        OutputStream out;
        try {
            out = new FileOutputStream( filename );
            prop.store(out, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Enumeration<Object> getKeys() {

        return prop.keys();
    }

}