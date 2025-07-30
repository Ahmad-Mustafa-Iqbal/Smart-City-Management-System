package src.gui;
import javax.swing.*;

import src.LoginFrame;

import java.awt.*;

public class BackPanel extends JPanel {
  
  FileManagerFront f;

  public BackPanel(JFrame currentFrame, FileManagerFront ff) {
    this.f = ff;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

    JLabel msg = new JLabel("Save & Go Back to Login?");
    msg.setAlignmentX(Component.CENTER_ALIGNMENT);
    msg.setFont(new Font("Arial", Font.BOLD, 16));

    JButton backBtn = new JButton("Logout");
    backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    backBtn.setBackground(new Color(0, 123, 255));
    backBtn.setForeground(Color.WHITE);
    backBtn.setFont(new Font("Arial", Font.BOLD, 14));
    backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    backBtn.setFocusPainted(false);
    backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    backBtn.addActionListener(e -> {
      saveAll();
      currentFrame.dispose();
      new LoginFrame();
    });

    add(msg);
    add(Box.createVerticalStrut(20));
    add(backBtn);
  }

  private void saveAll() {
    System.out.println("Saving all data before exiting............................");
    f.saveData();
  }
}
