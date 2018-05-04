package View;

import Resources.Colors;

import javax.swing.*;
import java.awt.*;

public class SquareView extends JPanel {
    JLabel label = new JLabel("", SwingConstants.CENTER);
    public SquareView() {
        setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
    }

    public void setText(String label) {
        this.label.setText(label);
    }
}
