package Model;

import java.io.Serializable;

/**
 * An unmovable Floor, can either be a goal or empty
 */
public class Floor extends Square implements Serializable {
    public boolean goal=false;

    public Floor(FloorElement floorElement){
        switch (floorElement){
            case GOAL: goal=true;
            break;
            case EMPTY: break;
        }

    }
}
