package fr.esigelec.snackio.game.character.texture;

import com.badlogic.gdx.graphics.Texture;
import fr.esigelec.snackio.game.character.Character;

public class TextureMapping {
    public String pathToTexture;
    public int cols;
    public int rows;
    public AnimationMapping staticMapping;
    public AnimationMapping attackingMapping;
    public AnimationMapping movingMapping;
    public TextureRepartition repartition;

    public TextureMapping(String pathToTexture, int cols, int rows, AnimationMapping staticMapping, AnimationMapping movingMapping, TextureRepartition repartition) {
        this.pathToTexture = pathToTexture;
        this.cols = cols;
        this.rows = rows;
        this.repartition = repartition;
        this.staticMapping = staticMapping;
        this.movingMapping = movingMapping;
        this.attackingMapping = movingMapping;
    }

    public TextureMapping(String pathToTexture,
                          int cols,
                          int rows,
                          AnimationMapping staticMapping,
                          AnimationMapping movingMapping,
                          AnimationMapping attackingMapping,
                          TextureRepartition repartition) {
        this.pathToTexture = pathToTexture;
        this.cols = cols;
        this.rows = rows;
        this.repartition = repartition;
        this.staticMapping = staticMapping;
        this.movingMapping = movingMapping;
        this.attackingMapping = attackingMapping;
    }

    public int getIndexOfStatus(Character.CharacterStatus status) {
        switch (status) {
            case MOVING_EAST:
            case STATIC_EAST:
            case ATTACKING_EAST:
                return repartition.eastIndex;
            case MOVING_NORTH:
            case STATIC_NORTH:
            case ATTACKING_NORTH:
                return repartition.northIndex;
            case MOVING_WEST:
            case STATIC_WEST:
            case ATTACKING_WEST:
                return repartition.westIndex;
            case MOVING_SOUTH:
            case STATIC_SOUTH:
            case ATTACKING_SOUTH:
                return repartition.southIndex;
        }
        return -1;
    }

    public static class TextureRepartition {

        public int northIndex;
        public int southIndex;
        public int westIndex;
        public int eastIndex;

        public TextureRepartition(int northIndex, int southIndex, int westIndex, int eastIndex) {
            this.northIndex = northIndex - 1;
            this.southIndex = southIndex - 1;
            this.eastIndex = eastIndex - 1;
            this.westIndex = westIndex - 1;
        }
    }

    public static class AnimationMapping {
        public int firstFrameIndex;
        public int lastFrameIndex;

        public AnimationMapping(int firstFrameIndex, int lastFrameIndex) {
            this.firstFrameIndex = firstFrameIndex;
            this.lastFrameIndex = lastFrameIndex;
        }
    }
}
