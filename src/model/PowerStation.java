package src.model;
import src.interfaces.Alertable;
import src.interfaces.Consumer;
import src.interfaces.Reportable;

import java.io.Serializable;
import java.util.ArrayList;

public class PowerStation extends CityResource implements Reportable, Alertable, Serializable {
    private double energyOutput;
    private ArrayList<Consumer> consumers;
    private CityRepository<CityResource> repository;
    private static double totalEnergyGenerated = 0;


    public PowerStation(String id, String loc, String status, double energyOutput,CityRepository<CityResource> repository) {
        super(id, loc, status);
        this.energyOutput = energyOutput;
        this.consumers = new ArrayList<>();
        this.repository = repository;
        totalEnergyGenerated += energyOutput;

    }

    public static double getTotalEnergyGenerated() {
        return totalEnergyGenerated;
    }


    public void addConsumer(Consumer c) {
        consumers.add(c);
    }

    public double getEnergyOutput() {
        return energyOutput;
    }

    public double getTotalConsumption() {
        double total = 0;
        for (Consumer c : consumers) {
            total += c.getEnergyConsumed();
        }
        return total;
    }

    @Override
    public double calculateMaintenanceCost() {
        return energyOutput * 0.5;  
    }

    @Override
    public String generateUsageReport() {
        return "PowerStation " + getResourceID() + ": Output=" + energyOutput +
               " kWh, Consumption=" + getTotalConsumption() + " kWh";
    }

    @Override
    public void sendEmergencyAlert(String msg) {
        System.out.println("ALERT: PowerStation " + getResourceID() + ": " + msg);
        System.out.println("PowerStation " + resourceID + ": Output=" + energyOutput + " kWh, Consumption=" + getTotalConsumption() + " kWh");

        if (repository != null) {
            for (CityResource res : repository.getAllResources()) {
                if (res instanceof EmergencyService) {
                    EmergencyService es = (EmergencyService) res;
                    if (es.getLocation().equalsIgnoreCase(this.location) || es.getStatus().equalsIgnoreCase("Ready")) {
                        System.out.println("[PowerStation -> Emergency Alert] Notifying Emergency Service: " + es.getResourceID());
                        es.sendEmergencyAlert("Overload at PowerStation " + this.resourceID + " in zone " + this.location);
                    }
                }
            }
        } else {
            System.out.println("⚠️ No repository linked to PowerStation. Cannot notify Emergency Services.");
        }
    }



    @Override
    public String toString() {
        return "PowerStation[" + getResourceID() + ", " + getLocation() + ", " + getStatus() +
               ", Output=" + energyOutput + " kWh, Consumers=" + consumers.size() + "]";
    }

    public ArrayList<Consumer> getConsumers() {
        return consumers;
    }

        public static void decreaseTotalEnergyGenerated(double amount) {
        totalEnergyGenerated -= amount;
        if (totalEnergyGenerated < 0) totalEnergyGenerated = 0;
    }
}