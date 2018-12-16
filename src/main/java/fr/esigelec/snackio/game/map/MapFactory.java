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
                Map desertCastleMap = new Map("maps/snackio.tmx");
                // Add map rooms
                MapRoom cave = new MapRoom("maps/snackio_cave.tmx", "CAVE", desertCastleMap);
                desertCastleMap.addRoom(cave);
                return desertCastleMap;
        }
        return null;
    }
}
