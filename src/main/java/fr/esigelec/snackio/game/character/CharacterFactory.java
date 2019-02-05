package fr.esigelec.snackio.game.character;

import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;

/**
 * Factory to create a Character
 */
public class CharacterFactory {
    /**
     * Available characters
     * (Update when you add a new SpriteSheet to assets)
     */
    public enum CharacterType{
        INSPECTOR,
        BALD_MAN,
        NUDE_MAN,
        INDIANA,
        GOLDEN_KNIGHT
    }

    /**
     * Generate a character
     * @param type The character texture to use
     * @return Character newly created
     */
    public static Character getCharacter(CharacterType type) throws UnhandledCharacterTypeException {

        AnimatedCharacterSkin skin = null;

        switch(type){
            case INDIANA:
                skin = new AnimatedCharacterSkin("sprites/character.png",9,4);
                break;
            case INSPECTOR:
                skin = new AnimatedCharacterSkin("sprites/inspector.png",9,4);
                break;
            case NUDE_MAN:
                skin = new AnimatedCharacterSkin("sprites/nude_man.png",9,4);
                break;
            case BALD_MAN:
                skin = new AnimatedCharacterSkin("sprites/bald_man.png",9,4);
                break;
            case GOLDEN_KNIGHT:
                skin = new AnimatedCharacterSkin("sprites/golden_knight.png",9,4);
                break;
            default:
                throw new UnhandledCharacterTypeException(type);
        }

        return new Character(skin);
    }
}
