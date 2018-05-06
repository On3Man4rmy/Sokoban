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
    JMenu[] menus={new JMenu("Reset"),new JMenu("Save/Load")};
    JMenuItem[] items={new JMenuItem("Restart"), new JMenuItem("Save"),new JMenuItem("Load")};


    public GameView(Sokoban sokoban) {
        super ("Game", true, true);
        setIconifiable (true); setMaximizable (true);
        registerKeyEvents();

        Container contentPane = getContentPane();
        LayoutManager overlay = new OverlayLayout(contentPane);
        contentPane.setLayout(overlay);
        initMenuBar();
        this.sokoban     = sokoban;
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
    public void initMenuBar(){
        JMenuBar mb = new JMenuBar();
        //TODO: make this actually work
        menus[0].add(items[0]);

        items[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sokoban.rebuildBoard();
            }
        });

        for(int i=1;i<items.length;i++){
            menus[1].add(items[i]);
            final int k=i;
            items[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(k==0){
                        saveGame();
                    }
                    if(k==1){
                        loadGame();
                    }

                }
            });
        }



        mb.add(menus[0]);
        mb.add(menus[1]);

        setJMenuBar(mb);

    }
    public void saveGame(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        String filename="Sokoban"+dateFormat.format(date)+".ser";

        try {
            FileOutputStream fs = new FileOutputStream (filename); // FOS oeffnen
            ObjectOutputStream os = new ObjectOutputStream (fs);
            os.writeObject(sokoban);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadGame(){
        JFileChooser c = new JFileChooser (new File("./"));
        File selectedFile=null;
        int returnValue = c.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = c.getSelectedFile();
        }
        if(selectedFile!=null){
            try {
                FileInputStream fs = new FileInputStream (selectedFile); // FIS oeffnen
                ObjectInputStream is = new ObjectInputStream(fs); // OIS erzeugen
                sokoban=(Sokoban)is.readObject();
                is.close();
                boardView.loadBoard();
            } catch (ClassNotFoundException e) { // wenn Klasse nicht gefunden
                System.err.println (e);
            } catch (IOException e) { // wenn IO-Fehler aufgetreten
                System.err.println (e);
            }
        }
    }
}
