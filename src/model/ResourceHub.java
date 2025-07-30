package src.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResourceHub implements Serializable{
    private String hubName;
    private ArrayList<CityResource> resources;

    public ResourceHub(String hubName) {
        this.hubName = hubName;
        this.resources = new ArrayList<>();
    }

    public String getHubName() {
        return hubName;
    }

    public ArrayList<CityResource> getResources() {
        return resources;
    }

    public void addResource(CityResource resource) {
        resources.add(resource);
    }

    public void removeResource(CityResource resource) {
        resources.remove(resource);
    }

    public void displayAllResources() {
        System.out.println("Resources in Hub: " + hubName);
        for (CityResource resource : resources) {
            System.out.println(resource);
        }
    }
}