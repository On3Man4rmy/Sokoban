package Model;

import java.io.Serializable;

/**
 * An Element that can be moved, either a player or a crate
 */
public abstract class MovableElement extends Square implements Serializable {
    public Position position;

}
