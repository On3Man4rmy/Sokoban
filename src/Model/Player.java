package Model;

import java.io.Serializable;

/**
 * Represents the Player
 */
public class Player extends MovableElement implements Serializable {

    public Player(int xPos, int yPos){
        position=new Position(xPos,yPos);
    }

}
