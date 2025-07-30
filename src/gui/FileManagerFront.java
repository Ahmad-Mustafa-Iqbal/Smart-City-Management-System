package src.gui;

import javax.swing.*;

import src.model.CityRepository;
import src.model.CityResource;
import src.model.CityZone;
import src.model.FileManager;
import src.model.Household;
import src.model.Industry;
import src.model.PowerStation;
import src.model.SmartGrid;

import java.util.ArrayList;

public class FileManagerFront extends JPanel {
    private CityRepository<CityResource> repository;
    private ArrayList<Household> households;
    private ArrayList<Industry> industries;
    private boolean isAdmin;

    private final String RESOURCE_FILE = "resources.dat";
    private final String ZONE_FILE = "zones.dat";
    private final String GRID_FILE = "grid.dat";
    private final String HOUSEHOLD_FILE = "households.dat";
    private final String INDUSTRY_FILE = "industries.dat";

    public FileManagerFront(CityRepository<CityResource> repository, ArrayList<CityZone> zones, SmartGrid smartGrid,
                            ArrayList<Household> households, ArrayList<Industry> industries, boolean isAdmin) {
        this.repository = repository;
        this.households = households;
        this.industries = industries;
        this.isAdmin = isAdmin;
        loadData();
    }

    public void saveData() {
        try {
            if (!isAdmin) return;
            FileManager.saveToFile(new ArrayList<>(repository.getAllResources()), RESOURCE_FILE);
            FileManager.saveToFile(households, HOUSEHOLD_FILE);
            FileManager.saveToFile(industries, INDUSTRY_FILE);
            System.out.println("Saved Yippe.......................");
        } catch (Exception ex) {
            System.out.println("Error saving data: " + ex.getMessage());
        }
    }

    public void loadData() {
        try {
            ArrayList<CityResource> loadedResources = FileManager.loadFromFile(RESOURCE_FILE);
            ArrayList<CityZone> loadedZones = FileManager.loadFromFile(ZONE_FILE);
            ArrayList<PowerStation> loadedGrid = FileManager.loadFromFile(GRID_FILE);
            ArrayList<Household> loadedHouseholds = FileManager.loadFromFile(HOUSEHOLD_FILE);
            ArrayList<Industry> loadedIndustries = FileManager.loadFromFile(INDUSTRY_FILE);

            repository.clear();
            for (CityResource res : loadedResources) repository.addResource(res);

            households.clear();
            households.addAll(loadedHouseholds);

            industries.clear();
            industries.addAll(loadedIndustries);

            System.out.println("Data loaded successfully");
        } catch (Exception ex) {
            System.out.println("Error loading data: " + ex.getMessage());
        }
    }
}
