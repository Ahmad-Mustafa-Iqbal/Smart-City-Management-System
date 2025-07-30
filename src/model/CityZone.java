package src.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CityZone implements Serializable {
    private String zoneName;
    private ArrayList<ResourceHub> hubs;

    public CityZone(String zoneName) {
        this.zoneName = zoneName;
        this.hubs = new ArrayList<>();
    }

    public String getZoneName() {
        return zoneName;
    }

    public ArrayList<ResourceHub> getHubs() {
        return hubs;
    }

    public void addHub(ResourceHub hub) {
        hubs.add(hub);
    }

    public void removeHub(ResourceHub hub) {
        hubs.remove(hub);
    }

    public void displayZoneResources() {
        System.out.println("City Zone: " + zoneName);
        for (ResourceHub hub : hubs) {
            hub.displayAllResources();
        }
    }
}
