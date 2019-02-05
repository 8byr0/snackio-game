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
                Map castleMap = new Map("maps/map01_castle.tmx", "CASTLE");
                // Add map rooms
                MapRoom inside = new MapRoom("maps/map01_inside.tmx", "INSIDE", castleMap);
                castleMap.addRoom(inside);
                return castleMap;
            /*case DESERT_CASTLE:
                Map desertCastleMap = new Map("maps/snackio.tmx", "DESERT_CASTLE");
                // Add map rooms
                MapRoom cave = new MapRoom("maps/snackio_cave.tmx", "CAVE", desertCastleMap);
                desertCastleMap.addRoom(cave);
                MapRoom castle = new MapRoom("maps/snackio_castle.tmx", "CASTLE", desertCastleMap);
                desertCastleMap.addRoom(castle);
                return desertCastleMap;*/
        }
        return null;
    }
}
