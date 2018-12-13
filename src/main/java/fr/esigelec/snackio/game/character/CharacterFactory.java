package fr.esigelec.snackio.game.character;

public class CharacterFactory {
    public enum CharacterType{
        INSPECTOR,
        BALD_MAN,
        NUDE_MAN,
        INDIANA
    }

    public static Character getCharacter(CharacterType type){
        Character character = new Character();

        switch(type){
            case INDIANA:
                character.configureRendering("sprites/character.png");
                break;
            case INSPECTOR:
                character.configureRendering("sprites/inspector.png");
                break;
            case NUDE_MAN:
                character.configureRendering("sprites/nude_man.png");
                break;
            case BALD_MAN:
                character.configureRendering("sprites/bald_man.png");
                break;
        }
        return character;
    }
}
