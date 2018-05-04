package View;

import Model.Direction;
import Model.Sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class GameView extends JInternalFrame implements Observer {
    Sokoban sokoban;
    BoardView boardView;
    MenuView menuView;

    public GameView(Sokoban sokoban) {
        super ("Game", true, true);
        setIconifiable (true); setMaximizable (true);
        registerKeyEvents();

        Container contentPane = getContentPane();
        LayoutManager overlay = new OverlayLayout(contentPane);
        contentPane.setLayout(overlay);

        this.sokoban = sokoban;
        this.sokoban.addObserver(this);

        boardView = new BoardView(sokoban);
        menuView = new MenuView();
        menuView.setText("Game Won!");
        menuView.setVisible(false);

        contentPane.add(menuView);
        contentPane.add(boardView);

        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == sokoban) {
            menuView.setVisible(sokoban.isDone());
            boardView.setVisible(!sokoban.isDone());
        }
    }

    public void registerKeyEvents() {
        registerKeyAction("W", "moveUp", actionEvent ->
                sokoban.moveElement(Direction.UP));
        registerKeyAction("A", "moveLeft", actionEvent ->
                sokoban.moveElement(Direction.LEFT));
        registerKeyAction("S", "moveDown", actionEvent ->
                sokoban.moveElement(Direction.DOWN));
        registerKeyAction("D", "moveRight", actionEvent ->
                sokoban.moveElement(Direction.RIGHT));
        // TODO only there for debugging purposes
        registerKeyAction("C", "changeDone", actionEvent ->
                sokoban.setDone(!sokoban.isDone()));
        registerKeyAction(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, "undo", actionEvent ->
                sokoban.undo());
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

    public void registerKeyAction(int keyCode, int modifiers, String actionName, Consumer<ActionEvent> callback) {
        getInputMap().put(KeyStroke.getKeyStroke(keyCode, modifiers), actionName);
        getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(actionName);
                callback.accept(e);
            }
        });
    }
}
