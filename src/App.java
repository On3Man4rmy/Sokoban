import Model.Sokoban;
import View.BoardView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class App extends JFrame {
    private JDesktopPane desk;
    public static void main(String[] args) {
        App app = new App();
        Sokoban sokoban = new Sokoban(new File("C:\\Users\\Tobias\\IdeaProjects\\Sokoban\\src\\Resources\\minicosmos.txt"));
        BoardView boardView = new BoardView(sokoban);
        app.addChild(boardView, 0, 0);
    }

    public App(){
        desk = new JDesktopPane();
        desk.setDesktopManager(new DefaultDesktopManager());
        setContentPane(desk);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocation(0, 0);
        setTitle("Sokoban");
        setVisible(true);
    }

    public void addChild(JInternalFrame child, int x, int y) {
        child.setLocation(x, y);
        child.setSize(200, 400);
        child.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        desk.add(child);
        child.setVisible(true);
    }
}
