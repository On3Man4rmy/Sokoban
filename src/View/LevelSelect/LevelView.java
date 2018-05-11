package View.LevelSelect;

import Model.Sokoban;
import Resources.Colors;
import View.BoardView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LevelView extends JPanel {
    Border defaultBorder = new EmptyBorder(10, 10, 10, 10);
    Border hoverBorder = new CompoundBorder(
            new LineBorder(Colors.CANARINHO.getColor(), 5),
            new EmptyBorder(5, 5, 5, 5)
    );
    Border clickedBorder = new CompoundBorder(
            new LineBorder(Colors.A_SWING_TRUMPET_V2.getColor(), 5),
            new EmptyBorder(5, 5, 5, 5)
    );
    Sokoban sokoban;
    Supplier<Consumer<Sokoban>> actionLevelSelected;

    public LevelView(String title, Sokoban sokoban) {
        BoardView boardView = new BoardView(sokoban);
        JLabel lblTitle = new JLabel(title);
        Font oldFont = lblTitle.getFont();
        this.sokoban = sokoban;
        boardView.disableMouseListener();
        lblTitle.setFont(new Font(oldFont.getName(), Font.PLAIN, 20));

        setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        setFocusable(true);
        setMaximumSize(new Dimension(200, 350));
        registerMouseEvents();

        add(lblTitle, BorderLayout.WEST);
        add(boardView, BorderLayout.SOUTH);
    }

    private void registerMouseEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(defaultBorder);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("enter");
                setBorder(hoverBorder);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setBorder(clickedBorder);
                if(actionLevelSelected.get() != null) {
                    actionLevelSelected.get().accept(sokoban);
                }
            }
        });
    }

    public void setActionLevelSelected(Supplier<Consumer<Sokoban>> actionLevelSelected) {
        this.actionLevelSelected = actionLevelSelected;
    }
}
