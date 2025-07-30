package src.gui;
import javax.swing.*;

import src.model.CityRepository;
import src.model.CityResource;
import src.model.CityZone;
import src.model.Household;
import src.model.Industry;
import src.model.SmartGrid;

import java.awt.*;
import java.util.ArrayList;

public class SmartCityDashboard extends JFrame {
  private boolean isAdmin;
  private CityRepository<CityResource> repository;
  private SmartGrid smartGrid;
  private ArrayList<CityZone> zones;
  private ArrayList<Household> households;
  private ArrayList<Industry> industries;

  public SmartCityDashboard(boolean isAdmin) {
    this.isAdmin = isAdmin;
    this.repository = new CityRepository<>();
    this.households = new ArrayList<>();
    this.industries = new ArrayList<>();

    setTitle("Smart City Dashboard - " + (isAdmin ? "Admin" : "Public"));
    setSize(1000, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    getContentPane().setBackground(new Color(204, 255, 255)); 
    tabbedPane.setOpaque(false);

    FileManagerFront f = new FileManagerFront(repository, zones, smartGrid, households, industries, isAdmin);
    JPanel emergencyPanel = new EmergencyServicePanel(repository, isAdmin);
    JPanel transportPanel = new TransportUnitPanel(repository, isAdmin);
    JPanel consumerPanel = new ConsumerPanel(households, industries, isAdmin);
    JPanel powerPanel = new PowerStationPanel(repository, isAdmin, smartGrid);

    JPanel backPanel = new BackPanel(this, f);
    
    tabbedPane.addTab("Power Stations", powerPanel);
    tabbedPane.addTab("Emergency Services", emergencyPanel);
    tabbedPane.addTab("Transport Units", transportPanel);
    tabbedPane.addTab("Consumers", consumerPanel);
    tabbedPane.addTab("Back", backPanel);

    tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    add(tabbedPane);
    setVisible(true);

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        f.saveData();
      }
    });
  }
}
