package fr.esigelec.snackio.networking;

/**
 * This class defines a Position by its coordinates
 */
public class Position {
    public float x;
    public float y;

    /**
     * Class constructor
     */
    public Position() {
    }

    /**
     * Class constructor with coordinates as param
     * @param x x
     * @param y y
     */
    public Position(float x, float y){
        this.x = x;
        this.y = y;
    }
}
