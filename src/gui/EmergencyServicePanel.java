package src.gui;

import javax.swing.*;

import src.model.CityRepository;
import src.model.CityResource;
import src.model.EmergencyService;

import java.awt.*;

public class EmergencyServicePanel extends JPanel {
    private CityRepository<CityResource> repository;
    private DefaultListModel<EmergencyService> listModel;
    private JList<EmergencyService> serviceList;

    private JTextField idField, locationField, statusField, typeField, timeField;
    private JButton addBtn, updateBtn, deleteBtn, alertBtn, reportBtn;
    private boolean isAdmin;

    public EmergencyServicePanel(CityRepository<CityResource> repository, boolean isAdmin) {
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
        locationField = new JTextField();
        statusField = new JTextField();
        typeField = new JTextField();
        timeField = new JTextField();

        idField.setFont(font);
        locationField.setFont(font);
        statusField.setFont(font);
        typeField.setFont(font);
        timeField.setFont(font);

        formPanel.add(styledLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(styledLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(styledLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(styledLabel("Service Type:"));
        formPanel.add(typeField);
        formPanel.add(styledLabel("Response Time:"));
        formPanel.add(timeField);

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
        serviceList = new JList<>(listModel);
        serviceList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        serviceList.setSelectionBackground(new Color(0, 123, 255));
        serviceList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(serviceList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        listModel.clear();
        loadEmergencyServices();

        content.add(formPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);
        content.add(scrollPane, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        serviceList.addListSelectionListener(e -> {
            EmergencyService es = serviceList.getSelectedValue();
            if (es != null) {
                idField.setText(es.getResourceID());
                locationField.setText(es.getLocation());
                statusField.setText(es.getStatus());
                typeField.setText(es.getServiceType());
                timeField.setText(String.valueOf(es.getResponseTime()));
            }
        });

        addBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String location = locationField.getText();
                String status = statusField.getText();
                String type = typeField.getText();
                int time = Integer.parseInt(timeField.getText());

                EmergencyService es = new EmergencyService(id, location, status, type, time);
                repository.addResource(es);
                listModel.addElement(es);
                clearForm();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Response time must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            EmergencyService selected = serviceList.getSelectedValue();
            if (selected != null) {
                try {
                    selected.setLocation(locationField.getText());
                    selected.setStatus(statusField.getText());
                    selected.setServiceType(typeField.getText());
                    selected.setResponseTime(Integer.parseInt(timeField.getText()));
                    serviceList.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Response time must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            EmergencyService selected = serviceList.getSelectedValue();
            if (selected != null) {
                repository.removeResource(selected);
                listModel.removeElement(selected);
                clearForm();
            }
        });

        alertBtn.addActionListener(e -> {
            EmergencyService selected = serviceList.getSelectedValue();
            if (selected != null) {
                selected.sendEmergencyAlert("Manual alert from GUI");
                JOptionPane.showMessageDialog(this,
                        "Alert Sent!\n[" + selected.getServiceType() + "] - Manual alert from GUI",
                        "Alert Notification",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        reportBtn.addActionListener(e -> {
            EmergencyService selected = serviceList.getSelectedValue();
            if (selected != null) {
                String report = selected.generateUsageReport();
                double cost = selected.calculateMaintenanceCost();
                JOptionPane.showMessageDialog(this,
                        report + "\nMaintenance Cost: Rs. " + String.format("%.2f", cost),
                        "Usage Report",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });


        if (!isAdmin) {
            disableAdminControls();
        }
    }

    private void loadEmergencyServices() {
        for (CityResource res : repository.getAllResources()) {
          System.out.println("Type of resource: " + res.getClass().getName());
            if (res instanceof EmergencyService) {
                listModel.addElement((EmergencyService) res);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        locationField.setText("");
        statusField.setText("");
        typeField.setText("");
        timeField.setText("");
    }

    private void disableAdminControls() {
        idField.setEditable(false);
        locationField.setEditable(false);
        statusField.setEditable(false);
        typeField.setEditable(false);
        timeField.setEditable(false);
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