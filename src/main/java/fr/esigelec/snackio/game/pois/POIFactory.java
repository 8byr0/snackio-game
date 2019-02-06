package fr.esigelec.snackio.game.pois;

import fr.esigelec.snackio.game.pois.bonuses.Cookie;
import fr.esigelec.snackio.game.pois.bonuses.SpeedBonus;
import fr.esigelec.snackio.game.pois.maluses.Freeze;
import fr.esigelec.snackio.game.pois.maluses.Paralysis;
import fr.esigelec.snackio.game.pois.maluses.SpeedMalus;

import java.util.Random;

public class POIFactory {
    public enum POIList {
        SPEED_BONUS,
        SPEED_MALUS,
        PARALYSIS,
        FREEZE,
        COOKIE,
        BOMB,
        RANDOM;

        public static POIList getRandom() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public static iPoi getRandom() {
        iPoi poi = null;
        switch (POIList.getRandom()) {
            case SPEED_BONUS:
                poi = new SpeedBonus();
                break;
            case SPEED_MALUS:
                poi = new SpeedMalus();
                break;
            case PARALYSIS:
                poi = new Paralysis();
                break;
            case FREEZE:
                poi = new Freeze();
                break;
            case BOMB:
                poi = new Bomb();
                break;
            case COOKIE:
                poi = new Cookie();
                break;
        }
        return poi;
    }
}
