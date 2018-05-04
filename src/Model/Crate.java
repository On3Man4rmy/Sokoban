package Model;

import java.io.Serializable;

/**
 * Represents a crate
 */
public class Crate extends MovableElement implements Serializable {
    public Crate(int xPos, int yPos){
        position=new Position(xPos,yPos);
    }
}
