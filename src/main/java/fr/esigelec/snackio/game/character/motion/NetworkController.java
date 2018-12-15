package fr.esigelec.snackio.game.character.motion;

import fr.esigelec.snackio.game.character.Character;

/**
 * Control a character with network server (MultiPlayer)
 * This controller is used to control remote characters.
 * TODO see if stuff can be moved here
 */
public class NetworkController implements iCharacterController {
    @Override
    public void execute(Character character) {
        // NetworkController does not need implementation as long
        // as character data is updated automatically by server
    }
}
