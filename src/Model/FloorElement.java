package Model;

public enum FloorElement {
    WALL(0),GOAL(1),CRATE(2),PLAYER(3),EMPTY(4);
    private int index;

    FloorElement(int i) {
        index=i;
    }
    public static FloorElement getFloorElement(int index){
        for(FloorElement f:FloorElement.values()){
            if(f.index==index) return f;
        }
        throw new IllegalArgumentException("Element not found");
    }

}
