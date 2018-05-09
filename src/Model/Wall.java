package Model;

import java.io.Serializable;

/**
 * Represents the wall/obstacle
 */
public class Wall extends Square implements Serializable {
    @Override
    public String toString() {
        return "Wall";
    }
}