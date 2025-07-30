package src.gui;


import javax.swing.*;

import src.model.CityRepository;
import src.model.CityResource;
import src.model.PowerStation;
import src.model.SmartGrid;

import java.awt.*;
import java.util.ArrayList;

public class PowerStationPanel extends JPanel {
    private CityRepository<CityResource> repository;
    private DefaultListModel<PowerStation> listModel;
    private JList<PowerStation> stationList;

    private JTextField idField, locationField, statusField, outputField;
    private JButton addBtn, updateBtn, deleteBtn, alertBtn, reportBtn;
    private boolean isAdmin;
    private SmartGrid grid;

    public PowerStationPanel(CityRepository<CityResource> repository, boolean isAdmin, SmartGrid grid) {
        this.repository = repository;
        this.grid = grid;
        this.isAdmin = isAdmin;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        idField = new JTextField();
        locationField = new JTextField();
        statusField = new JTextField();
        outputField = new JTextField();

        idField.setFont(font);
        locationField.setFont(font);
        statusField.setFont(font);
        outputField.setFont(font);

        formPanel.add(styledLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(styledLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(styledLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(styledLabel("Energy Output (kWh):"));
        formPanel.add(outputField);

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
        stationList = new JList<>(listModel);
        stationList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        stationList.setSelectionBackground(new Color(0, 123, 255));
        stationList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(stationList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        loadPowerStations();

        content.add(formPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);
        content.add(scrollPane, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        stationList.addListSelectionListener(e -> {
            PowerStation ps = stationList.getSelectedValue();
            if (ps != null) {
                idField.setText(ps.getResourceID());
                locationField.setText(ps.getLocation());
                statusField.setText(ps.getStatus());
                outputField.setText(String.valueOf(ps.getEnergyOutput()));
            }
        });

        addBtn.addActionListener(e -> {
            String id = idField.getText();
            String loc = locationField.getText();
            String status = statusField.getText();
            double output = Double.parseDouble(outputField.getText());

            PowerStation ps = new PowerStation(id, loc, status, output, repository);
            repository.addResource(ps);
            listModel.addElement(ps);
            clearForm();
        });

        updateBtn.addActionListener(e -> {
            PowerStation selected = stationList.getSelectedValue();
            if (selected != null) {
                selected.setLocation(locationField.getText());
                selected.setStatus(statusField.getText());
                stationList.repaint();
            }
        });

        deleteBtn.addActionListener(e -> {
            PowerStation selected = stationList.getSelectedValue();
            if (selected != null) {
                repository.removeResource(selected);
                listModel.removeElement(selected);
                clearForm();
            }
        });

        alertBtn.addActionListener(e -> {
            PowerStation selected = stationList.getSelectedValue();
            if (selected != null) {
                selected.sendEmergencyAlert("Manual alert triggered from GUI");
                JOptionPane.showMessageDialog(this,
                    "Emergency alert sent from PowerStation " + selected.getResourceID(),
                    "Alert Sent", JOptionPane.WARNING_MESSAGE);
            }
        });


        reportBtn.addActionListener(e -> {
            PowerStation selected = stationList.getSelectedValue();
            if (selected != null) {
                JOptionPane.showMessageDialog(this, selected.generateUsageReport(), "Usage Report", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        if (!isAdmin) {
            disableAdminControls();
        }
    }

    public void loadPowerStations() {
        for (CityResource res : repository.getAllResources()) {
            if (res instanceof PowerStation) {
                listModel.addElement((PowerStation) res);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        locationField.setText("");
        statusField.setText("");
        outputField.setText("");
    }

    private void disableAdminControls() {
        idField.setEditable(false);
        locationField.setEditable(false);
        statusField.setEditable(false);
        outputField.setEditable(false);
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
