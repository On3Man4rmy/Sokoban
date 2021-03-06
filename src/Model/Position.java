package Model;

import java.io.Serializable;

/**
 * Saves the position of a movable Element
 */
public class Position implements Serializable {
    int xPos;
    int yPos;

    public Position(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Copy constructor
     * @param position  Position object that is copied
     */
    public Position(Position position){
        xPos=position.xPos;
        yPos=position.yPos;
    }

    @Override
    public String toString() {
        return "{" + xPos + "; " + yPos + "}";
    }

    public static Position movePosition(Direction direction, Position position) {
        Position returnPosition;
        switch (direction) {
            case UP:
                returnPosition = new Position(position.xPos, position.yPos - 1);
                break;
            case DOWN:
                returnPosition = new Position(position.xPos, position.yPos + 1);
                break;
            case LEFT:
                returnPosition = new Position(position.xPos - 1, position.yPos);
                break;
            case RIGHT:
                returnPosition = new Position(position.xPos + 1, position.yPos);
                break;
            default:
                returnPosition = null;
        }
        return returnPosition;
    }
}
