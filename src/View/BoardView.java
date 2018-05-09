package View;

import Model.*;
import Resources.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class BoardView extends JPanel implements Observer {
    public Sokoban sokoban;
    private SquareView[][] squareViews;

    int rows;
    int cols;

    public BoardView(Sokoban sokoban) {

        this.sokoban = sokoban;
        this.sokoban.addObserver(this);
        loadBoard();
        updateBoard();
        registerMouseEvents();

        setVisible(true);
    }



    private void registerMouseEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double x = e.getX();
                double y = e.getY();
                double width = getWidth();
                double height = getHeight();
                double ratio = height / width;

                // Is mouseclick in top left half
                boolean topLeftHalf = y < (width - x) * ratio;
                boolean bottomLeftHalf = y > x * ratio;

                if(topLeftHalf && bottomLeftHalf) {
                    sokoban.moveElement(Direction.LEFT);
                } else if(topLeftHalf) {
                    sokoban.moveElement(Direction.UP);
                } else if(bottomLeftHalf) {
                    sokoban.moveElement(Direction.DOWN);
                } else {
                    sokoban.moveElement(Direction.RIGHT);
                }
            }
        });
    }

    public void loadBoard() {
        cols = sokoban.getArrayLength();
        rows = sokoban.getArrayHeight();

        squareViews = new SquareView[cols][rows];

        removeAll();
        setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                SquareView newSquareView = new SquareView();
                squareViews[j][i] = newSquareView;
                add(newSquareView);
            }
        }
    }

    public void updateBoard() {
        int cratesOnGoalCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Square[] squareContent = sokoban.gameBoard[j][i];
                for (Square content : squareContent) {
                    if (content instanceof Floor) {
                        if (((Floor) content).goal) {
                            squareViews[j][i].setBackground(Colors.WILD_VIOLET_PETAL.getColor());
                            squareViews[j][i].setText(".");
                        } else {
                            squareViews[j][i].setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());
                            squareViews[j][i].setText("");
                        }
                    }
                    if (content instanceof Player) {
                        squareViews[j][i].setBackground(Colors.CANARINHO.getColor());
                        squareViews[j][i].setText("@");
                    }
                    if (content instanceof Wall) {
                        squareViews[j][i].setBackground(Colors.SURRENDER_V2.getColor());
                        squareViews[j][i].setText("#");
                    }
                    if (content instanceof Crate) {
                        squareViews[j][i].setBackground(Colors.A_SWING_TRUMPET_V2.getColor());
                        squareViews[j][i].setText("$");
                        /**
                         * checks if Crate is on Goal, if yes, changes backgroundcolor and icon to indicate this
                         * also increases goal count
                         */
                        if (sokoban.gameBoard[j][i][0] instanceof Floor) {
                            Floor temp = (Floor) sokoban.gameBoard[j][i][0];
                            if (temp.goal) {
                                cratesOnGoalCount++;
                                squareViews[j][i].setBackground(Color.RED);
                                squareViews[j][i].setText("*");

                            }
                        }
                    }

                }
            }
        }
        if (!sokoban.isDone()) {
            if (cratesOnGoalCount == sokoban.getGoalCount()) {
                sokoban.setDone(true);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == sokoban) {
            updateBoard();
        }
    }


    public void registerKeyAction(String key, String actionName, Consumer<ActionEvent> callback) {
        getInputMap().put(KeyStroke.getKeyStroke(key), actionName);
        getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(actionName);
                callback.accept(e);
            }
        });

    }

}
