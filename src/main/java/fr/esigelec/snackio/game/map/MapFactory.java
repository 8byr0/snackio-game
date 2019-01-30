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
        DESERT_CASTLE,
        CASTLE
    }

    /**
     * Load a map instance
     * @param type Type of Map to instantiate
     * @return newly created Map
     */
    public static Map getMap(MapType type){
        switch(type){
            case DESERT_CASTLE:
                Map desertCastleMap = new Map("maps/snackio.tmx", "DESERT_CASTLE");
                // Add map rooms
                MapRoom cave = new MapRoom("maps/snackio_cave.tmx", "CAVE", desertCastleMap);
                desertCastleMap.addRoom(cave);
                MapRoom castle = new MapRoom("maps/snackio_castle.tmx", "CASTLE", desertCastleMap);
                desertCastleMap.addRoom(castle);
                return desertCastleMap;
            case CASTLE:
                Map map01CastleMap = new Map("maps/map01_castle.tmx", "MAP01_CASTLE");
                // Add map rooms
                MapRoom map01Inside = new MapRoom("maps/map01_inside.tmx", "MAP01_INSIDE", map01CastleMap);
                map01CastleMap.addRoom(map01Inside);
                return map01CastleMap;
        }
        return null;
    }
}
