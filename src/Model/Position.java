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

    public int up() {
        return yPos - 1;
    }

    public int down() {
        return yPos + 1;
    }

    public int right() {
        return xPos + 1;
    }

    public int left() {
        return xPos - 1;
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
