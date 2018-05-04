package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class BoardView extends JInternalFrame implements Observer {
    private Sokoban sokoban;
    private SquareView[][] squareViews;
    int rows;
    int cols;

    public BoardView(Sokoban sokoban) {
        super ("Game", true, true);
        setIconifiable (true); setMaximizable (true);

        this.sokoban = sokoban;
        this.sokoban.addObserver(this);
        loadBoard();
        updateBoard();
        registerKeyEvents();

        setVisible(true);
    }

    public void registerKeyEvents() {
        registerKeyAction("W", "moveUp", actionEvent -> {
                    sokoban.moveElement(Direction.UP, sokoban.player);
            });
        registerKeyAction("A", "moveLeft", actionEvent ->
                sokoban.moveElement(Direction.LEFT, sokoban.player));
        registerKeyAction("S", "moveDown", actionEvent ->
                sokoban.moveElement(Direction.DOWN, sokoban.player));
        registerKeyAction("D", "moveRight", actionEvent ->
                sokoban.moveElement(Direction.RIGHT, sokoban.player));
    }

    public void loadBoard() {
        Container cp = getContentPane();
        cols = sokoban.gameBoard.length;
        rows = cols > 0 ? sokoban.gameBoard[0].length : 1;
        squareViews = new SquareView[cols][rows];

        cp.removeAll();
        cp.setLayout(new GridLayout(cols, rows));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                SquareView newSquareView = new SquareView();
                squareViews[j][i] = newSquareView;
                cp.add(newSquareView);
            }
        }
    }

    public void updateBoard() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Square[] squareContent = sokoban.gameBoard[j][i];
                for(Square content : squareContent) {
                    if(content instanceof Floor) {
                        if(((Floor) content).goal) {
                            squareViews[j][i].setBackground(Color.CYAN);
                            squareViews[j][i].setText(".");
                        } else {
                            squareViews[j][i].setBackground(Color.LIGHT_GRAY);
                            squareViews[j][i].setText("");
                        }
                    }
                    if(content instanceof Player) {
                        squareViews[j][i].setBackground(Color.RED);
                        squareViews[j][i].setText("@");
                    }
                    if(content instanceof Wall) {
                        squareViews[j][i].setBackground(Color.DARK_GRAY);
                        squareViews[j][i].setText("#");
                    }
                    if(content instanceof Crate) {
                        squareViews[j][i].setBackground(Color.GREEN);
                        squareViews[j][i].setText("$");
                    }
                }
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
