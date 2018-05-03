package View;

import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class BoardView extends JInternalFrame implements Observer {
    private Sokoban sokoban;
    private SquareView[][] squareViews;
    int rows;
    int cols;
    JMenu[] menus={new JMenu("Construction"), new JMenu("Reset"),new JMenu("Save/Load")};
    JMenuItem[] items={new JMenuItem("Save"),new JMenuItem("Load")};

    public BoardView(Sokoban sokoban) {
        super ("Game", true, true);
        setIconifiable (true); setMaximizable (true);
        JMenuBar mb = new JMenuBar();
        //TODO: Open new window for Construction
        menus[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        //TODO: make this actually work
        menus[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sokoban.rebuildBoard();
            }
        });

        for(int i=0;i<items.length;i++){
            menus[2].add(items[i]);
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
        mb.add(menus[2]);

        setJMenuBar(mb);

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
                    if(sokoban.gameBoard[j][i][0] instanceof Floor) {
                        if(((Floor) sokoban.gameBoard[j][i][0]).goal) {
                            squareViews[j][i].setBackground(Color.CYAN);
                        } else {
                            squareViews[j][i].setBackground(Color.LIGHT_GRAY);
                        }
                    }
                    if(sokoban.gameBoard[j][i][1] instanceof Player) {
                        squareViews[j][i].setBackground(Color.RED);
                    }
                    if(sokoban.gameBoard[j][i][0] instanceof Wall) {
                        squareViews[j][i].setBackground(Color.DARK_GRAY);
                    }
                    if(sokoban.gameBoard[j][i][1] instanceof Crate) {
                        squareViews[j][i].setBackground(Color.GREEN);
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
            loadBoard();
        } catch (ClassNotFoundException e) { // wenn Klasse nicht gefunden
            System.err.println (e);
        } catch (IOException e) { // wenn IO-Fehler aufgetreten
            System.err.println (e);
        }
        }
    }
}
