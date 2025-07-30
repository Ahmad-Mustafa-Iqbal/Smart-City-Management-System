package src.gui;

import javax.swing.*;

import src.model.Household;
import src.model.Industry;

import java.awt.*;
import java.util.ArrayList;

public class ConsumerPanel extends JPanel {
    private ArrayList<Household> households;
    private ArrayList<Industry> industries;
    private DefaultListModel<String> householdModel = new DefaultListModel<>();
    private DefaultListModel<String> industryModel = new DefaultListModel<>();
    private boolean isAdmin;
    private JLabel totalLabel; 

    public ConsumerPanel(ArrayList<Household> households, ArrayList<Industry> industries, boolean isAdmin) {
        this.households = households;
        this.industries = industries;
        this.isAdmin = isAdmin;

        for (Household h : households) {
            householdModel.addElement(h.toString());
        }

        for (Industry i : industries) {
            industryModel.addElement(i.toString());
        }

        JPanel householdPanel = buildConsumerPanel("Households", householdModel, households, true);
        JPanel industryPanel = buildConsumerPanel("Industries", industryModel, industries, false);

        totalLabel = styledLabel("🔋 Total Energy Consumed: " + getOverallConsumption() + " kWh");
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        listsPanel.setBackground(Color.WHITE);
        listsPanel.add(householdPanel);
        listsPanel.add(industryPanel);

        add(listsPanel, BorderLayout.CENTER);
        add(totalLabel, BorderLayout.SOUTH);  
    }

    private JPanel buildConsumerPanel(String title, DefaultListModel<String> model, ArrayList<?> dataList, boolean isHousehold) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title));

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Monospaced", Font.PLAIN, 14));
        list.setSelectionBackground(new Color(0, 123, 255));
        list.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        if (isAdmin) {
            JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
            form.setBackground(Color.WHITE);
            JTextField idField = new JTextField();
            JTextField consumedField = new JTextField();

            JButton addBtn = styledButton("Add");
            JButton delBtn = styledButton("Delete");

            form.add(styledLabel("ID:"));
            form.add(idField);
            form.add(styledLabel("Energy Consumed:"));
            form.add(consumedField);
            form.add(addBtn);
            form.add(delBtn);
            panel.add(form, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                try {
                    String id = idField.getText();
                    double consumed = Double.parseDouble(consumedField.getText());

                    if (isHousehold) {
                        Household h = new Household(id, consumed);
                        households.add(h);
                        model.addElement(h.toString());
                    } else {
                        Industry i = new Industry(id, consumed);
                        industries.add(i);
                        model.addElement(i.toString());
                    }

                    updateTotalLabel();  
                    idField.setText("");
                    consumedField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid numeric value for energy!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            });

                delBtn.addActionListener(e -> {
                    int index = list.getSelectedIndex();
                    if (index >= 0) {
                        if (isHousehold) {
                            Household removed = households.remove(index);
                            Household.decreaseTotal(removed.getEnergyConsumed());
                        } else {
                            Industry removed = industries.remove(index);
                            Industry.decreaseTotal(removed.getEnergyConsumed());
                        }
                        model.remove(index);
                        updateTotalLabel(); 
                    }
                });

        }

        return panel;
    }

    private void updateTotalLabel() {
        if (totalLabel != null) {
            totalLabel.setText("🔋 Total Energy Consumed: " + getOverallConsumption() + " kWh");
        }
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    public static double getOverallConsumption() {
        return Household.getTotalConsumed() + Industry.getTotalConsumed();
    }
}
