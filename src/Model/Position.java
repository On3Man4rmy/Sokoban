package Model;

public class Position implements Square{
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
}
