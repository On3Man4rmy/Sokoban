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
    private boolean isDone = false; //checks if the game is finished
    int arrayHeight =0;
    int arrayLength =0;
    String[] inputFromFileArray;
    int goalCount=0;
    private Square[][] movableObjectsBackup;
    private Position[][] positionBackup;

    /**
     * Construktor, Creates the gameBoard based on a text file
     * @param file  text File that contains the Game field
     * @param level The level of the version, which should be loaded from the file
     */
    public Sokoban(File file, int level) {
        ArrayList<String> inputFromFileArray = new ArrayList<>();
        BufferedReader br;
        String line;

        try {
            boolean correctLevel=false;

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                //Only saves the correct Level
                if(line.equals("Level "+level)) {
                    correctLevel=true;
                }
                if(correctLevel){
                    if(line.equals("Level "+(level+1))){
                        correctLevel=false;
                    }
                    inputFromFileArray.add(line);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         * removes the first two and last two lines (Level, name and two empty lines)
         */
        inputFromFileArray.remove(0);
        inputFromFileArray.remove(0);
        inputFromFileArray.remove(inputFromFileArray.size()-1);
        inputFromFileArray.remove(inputFromFileArray.size()-1);

        arrayHeight = inputFromFileArray.size();

        /**
         * finds the longest string, to get max lenght
         */
        for (String s : inputFromFileArray) {
            if (s.length() >= arrayLength) {
                arrayLength = s.length();
            }
        }
        this.inputFromFileArray=inputFromFileArray.toArray(new String[0]);
        buildGameBoard();

    }

    /**
     * Builds the gameboard, goes through the String[] and builds the gameboard based on that
     */
    private void buildGameBoard(){
    gameBoard = new Square[arrayLength][arrayHeight][2];
    for (int y = 0; y < arrayHeight; y++) {
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
                    goalCount++;
                    break;
                }
                /**
                 * Crate on Goal
                 */
                case '*': {
                    gameBoard[x][y][0] = new Floor(FloorElement.GOAL);
                    gameBoard[x][y][1] = new Crate(x,y);
                    goalCount++;
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
        /**
         * Creates BacKup of the positions of Players and crates before an update, for undo option
         */
        movableObjectsBackup=new Square[arrayLength][arrayHeight];
        positionBackup=new Position[arrayLength][arrayHeight];  //Created new everytime to delete old one
        for(int x = 0; x< arrayLength; x++){
            for(int y = 0; y< arrayHeight; y++){
                movableObjectsBackup[x][y]=gameBoard[x][y][1];
                if(gameBoard[x][y][1]!=null) {
                    MovableElement temp=(MovableElement)gameBoard[x][y][1];
                    positionBackup[x][y] = new Position(temp.position);
                }
            }
        }
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

    public void setDone(boolean done) {
        isDone = done;
        setChanged();
        notifyObservers();
    }

    public void undo() {
        for(int x = 0; x< arrayLength; x++){
            for(int y = 0; y< arrayHeight; y++){
                gameBoard[x][y][1]=movableObjectsBackup[x][y];
                if(movableObjectsBackup[x][y]!=null){
                    MovableElement temp=(MovableElement)gameBoard[x][y][1];
                    temp.position=positionBackup[x][y];
                }
            }
        }
        setChanged();
        notifyObservers();


    }
    /**
     * Rebuilds board, for exaple when stuck and want to start again
     */
    public void rebuildBoard(){
        buildGameBoard();
        setChanged();
        notifyObservers();

    }

    public int getGoalCount() {
        return goalCount;
    }

    public int getArrayHeight() {
        return arrayHeight;
    }
    public int getArrayLength() {
        return arrayLength;
    }
}



