package com.vityarthi.academic.util;

import com.vityarthi.academic.strategy.GradingStrategy;
import com.vityarthi.academic.strategy.HonorsGradingStrategy;
import com.vityarthi.academic.strategy.StandardGradingStrategy;

/**
 * Singleton Pattern: ConfigManager ensures a single unified configuration context
 * across the application lifecycle.
 */
public class ConfigManager {
    private static volatile ConfigManager instance;

    private String institutionName;
    private String currentSemester;
    private String dataDirectory;
    private GradingStrategy activeGradingStrategy;

    private ConfigManager() {
        // Private constructor prevents direct instantiation
        this.institutionName = "VIT Bhopal / VITyarthi University";
        this.currentSemester = "Fall 2026-27";
        this.dataDirectory = "data";
        this.activeGradingStrategy = new HonorsGradingStrategy();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public GradingStrategy getActiveGradingStrategy() {
        return activeGradingStrategy;
    }

    public void setActiveGradingStrategy(GradingStrategy activeGradingStrategy) {
        if (activeGradingStrategy != null) {
            this.activeGradingStrategy = activeGradingStrategy;
        }
    }

    public void toggleGradingStrategy() {
        if (activeGradingStrategy instanceof HonorsGradingStrategy) {
            activeGradingStrategy = new StandardGradingStrategy();
        } else {
            activeGradingStrategy = new HonorsGradingStrategy();
        }
    }
}
