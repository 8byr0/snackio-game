package fr.esigelec.snackio.game.character.motion;

import fr.esigelec.snackio.game.character.Character;

/**
 * Interface to implement in character motion controllers
 * (Keyboard, Network, Gamepad...)
 */
public interface iCharacterController {
    /**
     * Method triggered in render method of the Character
     * @param character Character to control
     */
    void execute(Character character);
}
