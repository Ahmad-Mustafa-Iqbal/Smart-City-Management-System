package src.model;

import java.io.Serializable;

import src.interfaces.Alertable;
import src.interfaces.Reportable;

public class TransportUnit extends CityResource implements Reportable,Serializable,Alertable {
    private int passengerCount;
    private double fuelCostPerKm;

    public TransportUnit(String resourceID, String location, String status, int passengerCount, double fuelCostPerKm) {
        super(resourceID, location, status);
        this.passengerCount = passengerCount;
        this.fuelCostPerKm = fuelCostPerKm;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public double getFuelCostPerKm() {
        return fuelCostPerKm;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public void setFuelCostPerKm(double fuelCostPerKm) {
        this.fuelCostPerKm = fuelCostPerKm;
    }

    public void sendEmergencyAlert(String message) {
        System.out.println("[TransportUnit Alert] " + message);
    }

    @Override
    public double calculateMaintenanceCost() {
        return fuelCostPerKm * 100;
    }

    @Override
    public String generateUsageReport() {
        return "Transport Unit - Passengers: " + passengerCount + ", Fuel/km: " + fuelCostPerKm;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Transport, Passengers: " + passengerCount + ", Fuel Cost/km: " + fuelCostPerKm;
    }
}