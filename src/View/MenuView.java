package View;

import Resources.Colors;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JPanel {
    JLabel label = new JLabel("", SwingConstants.CENTER);
    public MenuView() {
        setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());
        setLayout(new BorderLayout());
        label.setForeground(Colors.A_LIFETIME_AGO.getColor());
        add(label, BorderLayout.CENTER);
    }

    public void setText(String label) {
        this.label.setText(label);
    }
}
