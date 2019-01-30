package fr.esigelec.snackio.game.character;

import fr.esigelec.snackio.core.exceptions.UnhandledCharacterTypeException;
import fr.esigelec.snackio.game.character.texture.AnimatedCharacterSkin;
import fr.esigelec.snackio.game.character.texture.TextureMapping;

/**
 * Factory to create a Character
 */
public class CharacterFactory {
    /**
     * Available characters
     * (Update when you add a new SpriteSheet to assets)
     */
    public enum CharacterType {
        INSPECTOR,
        BALD_MAN,
        NUDE_MAN,
        BASE_NUDE_MAN,
        INDIANA,
        GOLDEN_KNIGHT
    }

    public static final float WALKING_FRAME_DURATION = 0.15f;
    public static final float ATTACKING_FRAME_DURATION = 0.15f;

    /**
     * Generate a character
     *
     * @param type The character texture to use
     * @return Character newly created
     */
    public static Character getCharacter(CharacterType type) throws UnhandledCharacterTypeException {
        AnimatedCharacterSkin walkingSkin = null;

        switch (type) {
            case INDIANA:
                walkingSkin = new AnimatedCharacterSkin("sprites/character.png", 9, 4, WALKING_FRAME_DURATION);
                break;
            case INSPECTOR:
                walkingSkin = new AnimatedCharacterSkin("sprites/inspector.png", 9, 4, WALKING_FRAME_DURATION);
                break;
            case NUDE_MAN:
                walkingSkin = new AnimatedCharacterSkin("sprites/nude_man.png", 9, 4, WALKING_FRAME_DURATION);
                break;
            case BASE_NUDE_MAN:
                TextureMapping walkingMapping = new TextureMapping("sprites/john/walking.png", 9, 4,
                        new TextureMapping.AnimationMapping(0, 1),
                        new TextureMapping.AnimationMapping(1, 9),
                        new TextureMapping.TextureRepartition(1, 3, 2, 4));
                walkingSkin = new AnimatedCharacterSkin(walkingMapping, WALKING_FRAME_DURATION);
                break;
            case BALD_MAN:
                walkingSkin = new AnimatedCharacterSkin("sprites/bald_man.png", 9, 4, WALKING_FRAME_DURATION);
                break;
            case GOLDEN_KNIGHT:
                walkingSkin = new AnimatedCharacterSkin("sprites/golden_knight.png", 9, 4, WALKING_FRAME_DURATION);
                break;
            default:
                throw new UnhandledCharacterTypeException(type);
        }
        TextureMapping weaponMapping = new TextureMapping("sprites/john/spear.png", 8, 4,
                new TextureMapping.AnimationMapping(0, 1),
                new TextureMapping.AnimationMapping(0, 1),
                new TextureMapping.AnimationMapping(1, 8),
                new TextureMapping.TextureRepartition(1, 3, 2, 4));

        AnimatedCharacterSkin weapon = new AnimatedCharacterSkin(weaponMapping, ATTACKING_FRAME_DURATION);
        TextureMapping attackingMapping = new TextureMapping("sprites/john/attacking.png", 8, 4,
                new TextureMapping.AnimationMapping(0, 1),
                new TextureMapping.AnimationMapping(0, 2),
                new TextureMapping.AnimationMapping(1, 8),
                new TextureMapping.TextureRepartition(1, 3, 2, 4));

        AnimatedCharacterSkin attackingSkin = new AnimatedCharacterSkin(attackingMapping, ATTACKING_FRAME_DURATION);

        Character test = new Character(walkingSkin, attackingSkin);
        test.addWeapon(weapon);
        return test;
    }
}
