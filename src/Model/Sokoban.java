package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Main logic class
 */
public class Sokoban extends Observable implements Serializable {

    public Square[][][] gameBoard;  //Array of Game Elements. Third Dimension for Players and Crates on Fields
    public Player player;
    public ArrayList<Crate> crates;
    private boolean isDone = false;
    int arrayHeigth;
    int arrayLenght;
    String[] inputFromFileArray;

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
        int arrayHeight = inputFromFileArray.size();
        int arrayLength = 0;
        arrayHeigth = inputFromFileArray.size();

        for (String s : inputFromFileArray) {
            if (s.length() >= arrayLength) {
                arrayLength = s.length();
            }
        }
        this.inputFromFileArray=inputFromFileArray.toArray(new String[0]);
        gameBoard = new Square[arrayLenght][arrayHeigth][2];
        buildGameBoard();

    }


private void buildGameBoard(){
    for (int y = 0; y < arrayHeigth; y++) {
        char[] temp = inputFromFileArray[y].toCharArray();

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
        System.out.println("Player Postion: " + player.position);

        if (element==null) return false;
        Position position=Position.movePosition(direction, element.position);
        System.out.println(element.position);
        if(!(gameBoard[position.xPos][position.yPos][0] instanceof Wall)) {  //Not trying to move into wall
            MovableElement move=(MovableElement) gameBoard[position.xPos][position.yPos][1];
            if(((gameBoard[position.xPos][position.yPos][0] instanceof Crate)&&element instanceof Player) //Player moving a crate
                    ||gameBoard[position.xPos][position.yPos][0] instanceof Floor){ //Element moving to empty square
                if(move==null||moveElement(direction,move)){
                    gameBoard[element.position.xPos][element.position.yPos][1]=null;
                    element.position=position;
                    gameBoard[element.position.xPos][element.position.yPos][1]=element;
                    setChanged();
                    notifyObservers();
                    return true;
                }
                System.out.println("not wall");

            }

        }
        System.out.println("Wall");
        return false;
    }
    public boolean moveElement (Direction direction){
        return moveElement(direction, player);
    }

    public boolean isDone() {
        return isDone;
    }

    // TODO set isDone true when all crates are on goalCrates
    public void setDone(boolean done) {
        isDone = done;
        setChanged();
        notifyObservers();
    }

    // TODO implement undo
    public void undo() {

    }
    /**
     * Rebuilds board, for exaple when stuck and want to start again
     */
    public void rebuildBoard(){
        buildGameBoard();
    }
}



