package src.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SmartGrid implements Serializable {
    private ArrayList<PowerStation> stations;

    public SmartGrid() {
        stations = new ArrayList<>();
    }

    public void addPowerStation(PowerStation ps) {
        stations.add(ps);
    }

    public ArrayList<PowerStation> getAllStations() {
        return stations;
    }

    public double getTotalEnergyOutput() {
        double total = 0;
        for (PowerStation ps : stations) {
            total += ps.getEnergyOutput();
        }
        return total;
    }

    public double getTotalConsumption() {
        double total = 0;
        for (PowerStation ps : stations) {
            total += ps.getTotalConsumption();
        }
        return total;
    }

    public String generateGridReport() {
        StringBuilder sb = new StringBuilder("=== Smart Grid Report ===\n");
        for (PowerStation ps : stations) {
            sb.append(ps.generateUsageReport()).append("\n");
        }
        sb.append("Total Energy Output: ").append(getTotalEnergyOutput()).append(" kWh\n");
        sb.append("Total Energy Consumption: ").append(getTotalConsumption()).append(" kWh\n");
        return sb.toString();
    }
}
