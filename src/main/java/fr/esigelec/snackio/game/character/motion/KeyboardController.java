package fr.esigelec.snackio.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.esigelec.snackio.game.GameRenderer;
import fr.esigelec.snackio.networking.Position;

public class KeyboardController implements iCharacterController {

    @Override
    public void execute(Character character) {
        character.setMoving(false);
        GameRenderer engine = GameRenderer.getInstance();

        // Store position for faster access
        Position position = character.getPosition();
        int speed = character.getSpeed();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            character.setDirection(Direction.WEST);

            if (!engine.isCharacterColliding(character, character.getFullProjection(position.x - speed + 16, position.y),
                    character.getFeetsProjection(position.x - speed + 16, position.y))) {

                position.x -= speed;
                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            character.setDirection(Direction.EAST);

            if (!engine.isCharacterColliding(character, character.getFullProjection(position.x + speed + 16, position.y),
                    character.getFeetsProjection(position.x + speed + 16, position.y))) {
                position.x += speed;

                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            character.setDirection(Direction.SOUTH);

            if (!engine.isCharacterColliding(character, character.getFullProjection(position.x + 16, position.y - speed),
                    character.getFeetsProjection(position.x + 16, position.y - speed))) {
                position.y -= speed;

                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            character.setDirection(Direction.NORTH);

            if (!engine.isCharacterColliding(character, character.getFullProjection(position.x + 16, position.y + speed),
                    character.getFeetsProjection(position.x + 16, position.y + speed))) {
                position.y += speed;

                character.setMoving(true);
            }
        }

    }
}
