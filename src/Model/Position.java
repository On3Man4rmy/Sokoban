package Model;

/**
 * Saves the position of a movable Element
 */
public class Position{
    int xPos;
    int yPos;

    public Position(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public int up(){
        return yPos-1;
    }
    public int down(){
        return yPos+1;
    }
    public int right(){
        return xPos+1;
    }
    public int left(){
        return xPos-1;
    }

    @Override
    public String toString() {
        return "{" + xPos + "; " + yPos + "}";
    }
}
