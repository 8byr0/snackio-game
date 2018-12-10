package fr.esigelec.snackio.networking;

public class Position {
    public String x;
    public String y;

    public Position() {
    }

    public Position(String x, String y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return x + " - " + y;
    }
}
