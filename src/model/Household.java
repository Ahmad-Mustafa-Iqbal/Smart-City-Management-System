package src.model;
import src.interfaces.Consumer;

import java.io.Serializable;

public class Household implements Consumer, Serializable {
    private String consumerID;
    private double energyConsumed;
    private static double totalConsumed = 0;

    public Household(String id, double consumed) {
        this.consumerID = id;
        this.energyConsumed = consumed;
        totalConsumed += consumed;
    }

    public String getConsumerID() {
        return consumerID;
    }

    public double getEnergyConsumed() {
        return energyConsumed;
    }

    public static double getTotalConsumed() {
        return totalConsumed;
    }

    public static void decreaseTotal(double amount) {
        totalConsumed -= amount;
        if (totalConsumed < 0) totalConsumed = 0;
    }

    @Override
    public String toString() {
        return "Household[" + consumerID + " - Consumed: " + energyConsumed + " kWh]";
    }
}
