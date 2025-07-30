package src.gui;


import javax.swing.*;

import src.model.CityRepository;
import src.model.CityResource;
import src.model.TransportUnit;

import java.awt.*;

public class TransportUnitPanel extends JPanel {
    private CityRepository<CityResource> repository;
    private DefaultListModel<TransportUnit> listModel;
    private JList<TransportUnit> transportList;

    private JTextField idField, routeField, capacityField, fuelCostField;
    private JComboBox<String> statusCombo;
    private JButton addBtn, updateBtn, deleteBtn, alertBtn, reportBtn;
    private boolean isAdmin;

    public TransportUnitPanel(CityRepository<CityResource> repository, boolean isAdmin) {
        this.repository = repository;
        this.isAdmin = isAdmin;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        idField = new JTextField();
        routeField = new JTextField();
        capacityField = new JTextField();
        fuelCostField = new JTextField();
        statusCombo = new JComboBox<>(new String[]{
            "Active", "Inactive", "Under Maintenance", "Out of Service"
        });

        idField.setFont(font);
        routeField.setFont(font);
        capacityField.setFont(font);
        fuelCostField.setFont(font);
        statusCombo.setFont(font);

        formPanel.add(styledLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(styledLabel("Route:"));
        formPanel.add(routeField);
        formPanel.add(styledLabel("Capacity:"));
        formPanel.add(capacityField);
        formPanel.add(styledLabel("Fuel Cost/KM:"));
        formPanel.add(fuelCostField);
        formPanel.add(styledLabel("Status:"));
        formPanel.add(statusCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        addBtn = styledButton("Add");
        updateBtn = styledButton("Update");
        deleteBtn = styledButton("Delete");
        alertBtn = styledButton("Send Alert");
        reportBtn = styledButton("Report");

        if (isAdmin) {
            buttonPanel.add(addBtn);
            buttonPanel.add(updateBtn);
            buttonPanel.add(deleteBtn);
        }

        buttonPanel.add(alertBtn);
        buttonPanel.add(reportBtn);

        listModel = new DefaultListModel<>();
        transportList = new JList<>(listModel);
        transportList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        transportList.setSelectionBackground(new Color(0, 123, 255));
        transportList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(transportList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        loadTransportUnits();

        content.add(formPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);
        content.add(scrollPane, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        transportList.addListSelectionListener(e -> {
            TransportUnit tu = transportList.getSelectedValue();
            if (tu != null) {
                idField.setText(tu.getResourceID());
                routeField.setText(tu.getLocation());
                capacityField.setText(String.valueOf(tu.getPassengerCount()));
                fuelCostField.setText(String.valueOf(tu.getFuelCostPerKm()));
                statusCombo.setSelectedItem(tu.getStatus());
            }
        });

        addBtn.addActionListener(e -> {
            String id = idField.getText();
            String route = routeField.getText();
            int capacity = Integer.parseInt(capacityField.getText());
            double fuelCost = Double.parseDouble(fuelCostField.getText());
            String status = (String) statusCombo.getSelectedItem();

            TransportUnit tu = new TransportUnit(id, route, status, capacity, fuelCost);
            repository.addResource(tu);
            listModel.addElement(tu);
            clearForm();
        });

        updateBtn.addActionListener(e -> {
            TransportUnit selected = transportList.getSelectedValue();
            if (selected != null) {
                selected.setLocation(routeField.getText());
                selected.setPassengerCount(Integer.parseInt(capacityField.getText()));
                selected.setFuelCostPerKm(Double.parseDouble(fuelCostField.getText()));
                selected.setStatus((String) statusCombo.getSelectedItem());
                transportList.repaint();
            }
        });

        deleteBtn.addActionListener(e -> {
            TransportUnit selected = transportList.getSelectedValue();
            if (selected != null) {
                repository.removeResource(selected);
                listModel.removeElement(selected);
                clearForm();
            }
        });

        alertBtn.addActionListener(e -> {
            TransportUnit selected = transportList.getSelectedValue();
            if (selected != null) {
                selected.sendEmergencyAlert("Manual alert triggered from GUI");
                JOptionPane.showMessageDialog(this, "Emergency alert sent!\nMessage: Manual alert triggered from GUI", "Alert", JOptionPane.WARNING_MESSAGE);
            }
        });


        reportBtn.addActionListener(e -> {
            TransportUnit selected = transportList.getSelectedValue();
            if (selected != null) {
                String report = selected.generateUsageReport() + "\nMaintenance Cost: Rs. " + selected.calculateMaintenanceCost();
                JOptionPane.showMessageDialog(this, report, "Transport Report", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        if (!isAdmin) {
            disableAdminControls();
        }
    }

    private void loadTransportUnits() {
        for (CityResource res : repository.getAllResources()) {
            if (res instanceof TransportUnit) {
                listModel.addElement((TransportUnit) res);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        routeField.setText("");
        capacityField.setText("");
        fuelCostField.setText("");
        statusCombo.setSelectedIndex(0);
    }

    private void disableAdminControls() {
        idField.setEditable(false);
        routeField.setEditable(false);
        capacityField.setEditable(false);
        fuelCostField.setEditable(false);
        statusCombo.setEnabled(false);
        addBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
}
