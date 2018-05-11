package View.LevelSelect;

import Model.GameLoader;
import Model.Sokoban;
import Resources.Colors;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LevelListView extends JFrame {
    private Consumer<Sokoban> actionLevelSelected;

    public LevelListView(File levelList) {
        ArrayList<Sokoban> sokobanList = loadFile(levelList);
        JPanel listPane  = new JPanel();
        listPane.setLayout(new GridLayout(0, 3));
        for(int i = 1; i <= sokobanList.size(); i++) {
            Sokoban sokoban = sokobanList.get(i - 1);
            LevelView levelView = new LevelView("Level " + i, sokoban);
            levelView.setActionLevelSelected(getActionLevelSelected());
            listPane.add(levelView);
        }

        JScrollPane  listScroller = new JScrollPane(listPane);
        listPane.setBackground(Colors.PIED_PIPER_BUTTERLAND.getColor());

        listScroller.setPreferredSize(new Dimension(300, 300));
        listScroller.getVerticalScrollBar().setUnitIncrement(15);
        loadFile(levelList);

        add(listScroller);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocation(0, 0);
        setTitle("Select Level");
    }

    public Supplier<Consumer<Sokoban>>  getActionLevelSelected() {
        return  () -> actionLevelSelected;
    }
    public void setActionLevelSelected(Consumer<Sokoban> actionLevelSelected) {
        this.actionLevelSelected = actionLevelSelected;
    }

    public ArrayList<Sokoban> loadFile(File levelList) {
        ArrayList<Sokoban> sokobanList = new ArrayList<>();
        int levels = GameLoader.getLevelCount(levelList);
        for(int i = 1; i <= levels; i++) {
            sokobanList.add(new Sokoban(levelList, i));
        }

        return sokobanList;
    }
}
