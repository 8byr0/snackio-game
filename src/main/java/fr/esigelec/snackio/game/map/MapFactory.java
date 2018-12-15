package fr.esigelec.snackio.game.map;

/**
 * Factory to create a Map
 */
public class MapFactory {
    /**
     * Available Maps
     * Add new values when you declare a new Map in assets
     */
    public enum MapType{
        DESERT_CASTLE
    }

    /**
     * Load a map instance
     * @param type Type of Map to instantiate
     * @return newly created Map
     */
    public static Map getMap(MapType type){
        switch(type){
            case DESERT_CASTLE:
                return new Map("maps/snackio.tmx");
        }
        return null;
    }
}
