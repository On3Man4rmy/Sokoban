package View;

import Model.ConstructionModel;
import Model.FloorElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * A window, in which a new game can be constructed;
 */
//TODO: Open new window in DesktopPane with ConstructiobView
public class ConstructionView extends JInternalFrame implements Observer {
    int cols = 20;
    int rows = 10;
    private int floorElementIndex = 0;
    private SquareView[][] squareViews;
    public ConstructionModel constructionModel;
    JMenu[] menus = {new JMenu("Element")};
    JMenuItem[] items = { // Array mit Menue-Eintraegen
            new JMenuItem("Wall"), new JMenuItem("Goal"),
            new JMenuItem("Crate"), new JMenuItem("Player")};


    public ConstructionView() {
        setVisible(true);
        Container cp = getContentPane();
        squareViews = new SquareView[cols][rows];
        constructionModel = new ConstructionModel(rows, cols);
        for (int i = 0; i < items.length; i++) {
            items[i].add(items[i]);
            final int k = i;

            items[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    floorElementIndex = k;
                }
            });
        }

        cp.removeAll();
        cp.setLayout(new GridLayout(cols, rows));
        this.constructionModel.addObserver(this);

        /**
         * Adds the Fields to the layout and adds a mouselistener to them
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int x=j;
                final int y=i;
                SquareView newSquareView = new SquareView();
                squareViews[j][i] = newSquareView;
                squareViews[j][i].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    constructionModel.setCell(x,y,FloorElement.getFloorElement(floorElementIndex));
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                cp.add(newSquareView);
            }
        }
    }


    public void updateBoard() {
        char[][] field = constructionModel.getGammeBoard();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (field[j][i] == '.') {
                    squareViews[j][i].setBackground(Color.CYAN);
                } else {
                    squareViews[j][i].setBackground(Color.LIGHT_GRAY);
                }

                if (field[j][i] == '@') {
                    squareViews[j][i].setBackground(Color.RED);
                }
                if (field[j][i] == '#') {
                    squareViews[j][i].setBackground(Color.DARK_GRAY);
                }
                if (field[j][i] == '$') {
                    squareViews[j][i].setBackground(Color.GREEN);
                }
                if (field[j][i] == '+') {
                    squareViews[j][i].setBackground(Color.ORANGE);
                }
                if (field[j][i] == '*') {
                    squareViews[j][i].setBackground(Color.YELLOW);
                }
            }
        }
    }



    @Override
    public void update(Observable o, Object arg) {
        if (o == constructionModel) {
            updateBoard();
        }
    }
}

