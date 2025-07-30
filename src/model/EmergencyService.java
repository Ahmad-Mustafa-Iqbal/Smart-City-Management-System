package src.model;

import java.io.Serializable;

import src.interfaces.Alertable;
import src.interfaces.Reportable;

public class EmergencyService extends CityResource implements Serializable,Alertable, Reportable {
    private String serviceType; 
    private int responseTime;
    private static int totalResponseTime = 0;
    private static int emergencyServiceCount = 0;


    public EmergencyService(String resourceID, String location, String status, String serviceType, int responseTime) {
        super(resourceID, location, status);
        this.serviceType = serviceType;
        this.responseTime = responseTime;
        totalResponseTime += responseTime;
        emergencyServiceCount++;
    }

    public EmergencyService(String resourceID, String location, String status, String serviceType) {
        this(resourceID, location, status, serviceType, 5); 

    }


    public String getServiceType() {
        return serviceType;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public double calculateMaintenanceCost() {
        return 500 + (10 * responseTime);
    }

    @Override
    public void sendEmergencyAlert(String message) {
        System.out.println("[" + serviceType + " Unit Alert] " + message);
    }

    @Override
    public String generateUsageReport() {
        return "Emergency Service (" + serviceType + ") - Response Time: " + responseTime + " min";
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Emergency (" + serviceType + "), Response Time: " + responseTime + " min";
    }

    public static double getAverageResponseTime() {
        if (emergencyServiceCount == 0) return 0;
        return (double) totalResponseTime / emergencyServiceCount;
    }

}
