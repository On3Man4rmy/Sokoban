package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Main logic class
 */
public class Sokoban extends Observable {

    public Square[][][] gameBoard;  //Array of Game Elements. Third Dimension for Players and Crates on Fields
    public Player player;
    public ArrayList<Crate> crates;

    /**
     * Construktor, Creates the gameBoard based on a text file
     * @param file  text File that contains the Game field
     */
    public Sokoban(File file) {
        ArrayList<String> inputFromFileArray = new ArrayList<>();
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                inputFromFileArray.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int arrayHeigth = inputFromFileArray.size();
        int arrayLenght = 0;

        for (String s : inputFromFileArray) {
            if (s.length() >= arrayLenght) {
                arrayLenght = s.length();
            }
        }
        gameBoard = new Square[arrayLenght][arrayHeigth][2];
        for (int y = 0; y < arrayHeigth; y++) {
            char[] temp = inputFromFileArray.get(y).toCharArray();

            for (int x = 0; x < temp.length; x++) {
                switch (temp[x]) {
                    case '#': {
                        gameBoard[x][y][0] = new Wall();
                        break;
                    }
                    case '$': {
                        gameBoard[x][y][0] = new Floor(FloorElement.EMPTY);
                        gameBoard[x][y][1]=new Crate(x,y);

                        break;
                    }
                    case '.': {
                        gameBoard[x][y][0] = new Floor(FloorElement.GOAL);
                        break;
                    }
                    /**
                     * Crate on Goal
                     */
                    case '*': {
                        gameBoard[x][y][0] = new Floor(FloorElement.GOAL);
                        gameBoard[x][y][1] = new Crate(x,y);
                        break;
                    }
                    /**
                     * Player on Goal
                     */
                    case '+': {

                        gameBoard[x][y][0] = new Floor(FloorElement.GOAL);
                        break;
                    }
                    case '@': {
                        gameBoard[x][y][0] = new Floor(FloorElement.EMPTY);
                        player=new Player(x,y);
                        gameBoard[x][y][1]= player;
                        break;
                    }
                    case ' ': {
                        gameBoard[x][y][0] = new Floor(FloorElement.EMPTY);
                        break;
                    }

                }
            }
        }
    }

    /**
     * Returns Both Layers of a Game Square
     * @param x x Position
     * @param y y Position
     * @return  Array of two Sqaures, the Field square and a potential Crate/Player
     */
    public Square[] getSquare(int x, int y){
        Square[] sqr={gameBoard[x][y][0],gameBoard[x][y][1]};
        return sqr;
    }

    /**
     * moves a Movable Element (player of square) in a given direction
     * @param direction Direction in which the Element is to be moved
     * @param element   The Element to be moved
     * @return          if the move was sucessful or not (not moving against wall or moving multiple crates/crates against wall)
     */
    public boolean moveElement (Direction direction, MovableElement element){
        if (element==null) return false;
        Position position;
        switch (direction){
            case UP: position=new Position(element.position.xPos,element.position.up());
            break;
            case DOWN:position=new Position(element.position.xPos,element.position.down());
            break;
            case LEFT:position=new Position(element.position.xPos,element.position.left());
            break;
            case RIGHT:position=new Position(element.position.xPos,element.position.right());
            break;
            default: position=null;
        }
        if(!(gameBoard[position.xPos][position.yPos][0] instanceof Wall)) {  //Not trying to move into wall
            MovableElement move=(MovableElement) gameBoard[position.xPos][position.yPos][1];
            if(((gameBoard[position.xPos][position.yPos][0] instanceof Crate)&&element instanceof Player) //Player moving a crate
                    ||gameBoard[position.xPos][position.yPos][0] instanceof Floor){ //Element moving to empty square
                if(move==null||moveElement(direction,move)){
                    gameBoard[element.position.xPos][element.position.yPos][1]=null;
                    element.position=position;
                    gameBoard[element.position.xPos][element.position.yPos][1]=move;
                    setChanged();
                    notifyObservers();
                    return true;
                }

            }

        }
        return false;
    }


}



