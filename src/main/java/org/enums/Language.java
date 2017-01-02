package org.enums;

import java.io.File;

import org.commons.PropertiesManager;

public enum Language {

    ENGLISH("english", "views-texts-en.properties"),
    SPANISH("spanish", "views-texts-es.properties");

    private final String filename;
    private final String language;
    private static final String LANGUAGE = "language";
    private static PropertiesManager preferencesProp;
    private static final String RESOURCES = "src" + File.separator + "main" + File.separator + "resources";
    private static final String PREFERENCES_FILE = "preferences.properties";

    Language(String language, String filename) {

        this.language = language;
        this.filename = filename;
    }

    public String getFilename() {

        return this.filename;
    }

    public static Language getSelectedLanguage() {

        Language language;
        preferencesProp = new PropertiesManager(RESOURCES + File.separator + PREFERENCES_FILE);
        String selectedLanguage = preferencesProp.getProperty(LANGUAGE);
        switch (selectedLanguage) {
        case "english":
            language = ENGLISH;
            break;
        case "spanish":
            language = SPANISH;
            break;
        default:
            language = ENGLISH;
        }

        return language;
    }
    
    public void storeLanguage () {
         
        preferencesProp.storeProperty(LANGUAGE, this.language);
    }
}
