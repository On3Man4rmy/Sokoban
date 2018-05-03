package Model;
public class Crate extends MovableElement implements Square{
    public Crate(int xPos, int yPos){
        position=new Position(xPos,yPos);
    }
}
