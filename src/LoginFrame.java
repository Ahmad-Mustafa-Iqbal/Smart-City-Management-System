package src;

import javax.swing.*;

import src.gui.SmartCityDashboard;

import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
  private JComboBox<String> roleCombo;
  private JButton loginButton;

  public LoginFrame() {
    setTitle("Smart City Login");
    setSize(350, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JPanel centerPanel = new JPanel();
    centerPanel.setOpaque(false);
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); 


    JLabel roleLabel = new JLabel("Select Role:");
    roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);       
    roleLabel.setHorizontalAlignment(JLabel.CENTER);

    roleCombo = new JComboBox<>(new String[]{"Admin", "Public"});
    roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
    roleCombo.setMaximumSize(new Dimension(150, 25));
    roleCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

    centerPanel.add(Box.createVerticalStrut(10)); 
    centerPanel.add(roleLabel);
    centerPanel.add(Box.createVerticalStrut(7));
    centerPanel.add(roleCombo);
    centerPanel.add(Box.createVerticalStrut(10)); 

    add(centerPanel, BorderLayout.CENTER);

    loginButton = new JButton("Login");
    loginButton.setBackground(new Color(0, 123, 255));
    loginButton.setForeground(Color.WHITE);
    loginButton.setFont(new Font("Arial", Font.BOLD, 14));
    loginButton.setFocusPainted(false);
    loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    loginButton.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        loginButton.setBackground(new Color(30, 144, 255));
      }

      public void mouseExited(MouseEvent e) {
        loginButton.setBackground(new Color(0, 123, 255));
      }
    });

    JButton logout = new JButton("Exit");
    logout.setBackground(new Color(255, 51, 51));
    logout.setForeground(Color.WHITE);
    logout.setFont(new Font("Arial", Font.BOLD, 14));
    logout.setFocusPainted(false);
    logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
    logout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    logout.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        logout.setBackground(new Color(204, 0, 0)); 
      }

      public void mouseExited(MouseEvent e) {
        logout.setBackground(new Color(255, 52, 52));
      }
    });

    logout.addActionListener(e -> System.exit(0));


    JPanel bottomPanel = new JPanel();
    bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40)); 
    bottomPanel.add(loginButton);
    bottomPanel.add(logout);
    add(bottomPanel, BorderLayout.SOUTH);

    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String role = (String) roleCombo.getSelectedItem();
        boolean isAdmin = role.equalsIgnoreCase("Admin");
        new SmartCityDashboard(isAdmin);
        dispose();
      }
    });

    setVisible(true);
  }

  public static void main(String[] args) {
    new LoginFrame();
  }
}