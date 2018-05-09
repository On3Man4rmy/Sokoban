package View;

import Model.Direction;
import Model.Sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class GameView extends JInternalFrame implements Observer {
    Sokoban sokoban;
    BoardView boardView;
    MenuView menuView;
    JMenu[] menus = {new JMenu("Options")};
    JMenuItem[] items = {new JMenuItem("Restart"), new JMenuItem("Save"), new JMenuItem("Load")};
    Container contentPane = getContentPane();


    public GameView(Sokoban sokoban) {
        super("Game", true, true);
        setIconifiable(true);
        setMaximizable(true);
        registerKeyEvents();

        LayoutManager overlay = new OverlayLayout(contentPane);
        contentPane.setLayout(overlay);
        initMenuBar();
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
        if (o == sokoban) {
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
        registerKeyAction(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK, "save", actionEvent ->
                saveGame());
        registerKeyAction(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK, "load", actionEvent ->
                loadGame());
        registerKeyAction(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK, "restart", actionEvent ->
                sokoban.rebuildBoard());
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

    public void initMenuBar() {
        JMenuBar mb = new JMenuBar();

        for (int i = 0; i < items.length; i++) {
            menus[0].add(items[i]);
            final int k = i;
            items[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (k == 0) {
                        sokoban.rebuildBoard();
                    }
                    if (k == 1) {
                        saveGame();
                    }
                    if (k == 2) {
                        loadGame();
                    }

                }
            });
        }


        mb.add(menus[0]);

        setJMenuBar(mb);

    }

    /**
     * Saves the game
     */
    public void saveGame() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        String filename = "Sokoban" + dateFormat.format(date) + ".ser";

        try {
            FileOutputStream fs = new FileOutputStream(filename); // FOS oeffnen
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(sokoban);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the game from Save
     */
    //TODO: fix issue with loading game, where older version of palyer and crate are show, but dissapera when moved into
    public void loadGame() {
        JFileChooser c = new JFileChooser(new File("./"));
        File selectedFile = null;
        int returnValue = c.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = c.getSelectedFile();
        }
        if (selectedFile != null) {
            try {
                FileInputStream fs = new FileInputStream(selectedFile); // FIS oeffnen
                ObjectInputStream is = new ObjectInputStream(fs); // OIS erzeugen
                Sokoban newSokoban = (Sokoban) is.readObject();
                newSokoban.addObserver(this);
                sokoban.deleteObserver(this);
                sokoban = newSokoban;
                contentPane.removeAll();
                boardView = new BoardView(sokoban);
                contentPane.add(boardView);
                setVisible(false);
                setVisible(true);


                is.close();
            } catch (ClassNotFoundException e) { // wenn Klasse nicht gefunden
                System.err.println(e);
            } catch (IOException e) { // wenn IO-Fehler aufgetreten
                System.err.println(e);
            }
        }
    }
}
