package View;

import Model.*;
import Resources.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

public class BoardView extends JPanel implements Observer {
    private Sokoban sokoban;
    private SquareView[][] squareViews;
    int rows;
    int cols;
    JMenu[] menus={new JMenu("Construction"), new JMenu("Reset"),new JMenu("Save/Load")};
    JMenuItem[] items={new JMenuItem("Save"),new JMenuItem("Load")};

    public BoardView(Sokoban sokoban) {
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
        cols = sokoban.gameBoard.length;
        rows = cols > 0 ? sokoban.gameBoard[0].length : 1;
        squareViews = new SquareView[cols][rows];

        removeAll();
        setLayout(new GridLayout(cols, rows));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                SquareView newSquareView = new SquareView();
                squareViews[j][i] = newSquareView;
                add(newSquareView);
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
                            squareViews[j][i].setBackground(Colors.WILD_VIOLET_PETAL.getColor());
                            squareViews[j][i].setText(".");
                        } else {
                            squareViews[j][i].setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());
                            squareViews[j][i].setText("");
                        }
                    }
                    if(content instanceof Player) {
                        squareViews[j][i].setBackground(Colors.CANARINHO.getColor());
                        squareViews[j][i].setText("@");
                    }
                    if(content instanceof Wall) {
                        squareViews[j][i].setBackground(Colors.SURRENDER_V2.getColor());
                        squareViews[j][i].setText("#");
                    }
                    if(content instanceof Crate) {
                        squareViews[j][i].setBackground(Colors.A_SWING_TRUMPET_V2.getColor());
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
