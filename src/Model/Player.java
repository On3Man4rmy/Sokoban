package Model;

public class Player extends MovableElement implements Square{

    public Player(int xPos, int yPos){
        position=new Position(xPos,yPos);
    }

}
