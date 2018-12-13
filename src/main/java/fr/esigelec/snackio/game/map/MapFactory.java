package fr.esigelec.snackio.game.map;

public class MapFactory {
    public enum MapType{
        DESERT_CASTLE
    }

    public static Map getMap(MapType type){
        switch(type){
            case DESERT_CASTLE:
                return new Map("maps/snackio.tmx");
        }
        return null;
    }
}
