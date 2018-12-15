package fr.esigelec.snackio.game.character;

import fr.esigelec.snackio.game.character.motion.iCharacterController;

public class NetworkController implements iCharacterController {
    @Override
    public void execute(Character character) {
        // NetworkController does not need implementation as long
        // as character data is updated automatically by server
    }
}
