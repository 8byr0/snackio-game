package fr.esigelec.snackio.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
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
            if (!engine.isCharacterColliding(character.getFullProjection(position.x - speed + 16, position.y),
                    character.getFeetsProjection(position.x - speed + 16, position.y))) {

                position.x -= speed;
                character.setDirection(Direction.WEST);
                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!engine.isCharacterColliding(character.getFullProjection(position.x + speed + 16, position.y),
                    character.getFeetsProjection(position.x + speed + 16, position.y))) {
                position.x += speed;

                character.setDirection(Direction.EAST);
                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (!engine.isCharacterColliding(character.getFullProjection(position.x + 16, position.y - speed),
                    character.getFeetsProjection(position.x + 16, position.y - speed))) {
                position.y -= speed;

                character.setDirection(Direction.SOUTH);
                character.setMoving(true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!engine.isCharacterColliding(character.getFullProjection(position.x + 16, position.y + speed),
                    character.getFeetsProjection(position.x + 16, position.y + speed))) {
                position.y += speed;

                character.setDirection(Direction.NORTH);
                character.setMoving(true);
            }
        }
    }
}
