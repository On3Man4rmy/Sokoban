import Model.GameLoader;
import Model.Sokoban;
import View.BoardView;
import View.LevelSelect.LevelListView;
import View.GameView;
import View.LevelSelect.LevelView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

// TODO Refactor whole application
public class App extends JFrame {
    private JDesktopPane desk;
    JMenu[] menus = {new JMenu("Options")};
    JMenuItem[] items = {new JMenuItem("New Game")};
    LevelListView levelListView;

    public static void main(String[] args) {
        App app = new App();
    }

    public App(){
        desk = new JDesktopPane();
        desk.setDesktopManager(new DefaultDesktopManager());
        setContentPane(desk);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocation(0, 0);
        setTitle("Sokoban");
        initMenuBar();

        Sokoban sokoban = new Sokoban(new File("src/Resources/minicosmos.txt"),1);
        GameView gameView = new GameView(sokoban);
        addChild(gameView, 0, 0);

        setVisible(true);
    }

    private void loadLevelListView() {
        JFileChooser c = new JFileChooser(new File("src/Resources/"));
        File selectedFile = null;
        int returnValue = c.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = c.getSelectedFile();
        }
        if(selectedFile != null) {
            levelListView = new LevelListView(selectedFile);
            levelListView.setActionLevelSelected(sokoban1 ->
            {
                try {
                    addChild(new GameView((Sokoban) sokoban1.clone()), 0,0);
                    levelListView.setVisible(false);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            });
            levelListView.setVisible(true);
        }
    }

    public void addChild(JInternalFrame child, int x, int y) {
        child.setLocation(x, y);
        child.setSize(200, 400);
        child.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        desk.add(child);
        child.setVisible(true);
    }

    public void newGame() {
        loadLevelListView();
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
                        newGame();
                    }
                }
            });
        }


        mb.add(menus[0]);

        setJMenuBar(mb);
    }
}
