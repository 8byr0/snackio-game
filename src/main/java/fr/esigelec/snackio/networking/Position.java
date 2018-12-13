package fr.esigelec.snackio.networking;

public class Position {
    public float x;
    public float y;

    public Position() {
    }

    public Position(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return x + " - " + y;
    }
}
