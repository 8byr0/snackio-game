package fr.esigelec.snackio.game.pois.bonuses;

import fr.esigelec.snackio.game.character.Character;
import org.mockito.Mockito;

class SpeedBonusTest {
    private SpeedBonus sb = null;
    private Character mockCharacter = null;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        sb = new SpeedBonus();
        mockCharacter = Mockito.mock(Character.class);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void testNotAccumulableSpeedBonus() {
        sb.execute(mockCharacter);
        sb.execute(mockCharacter);
        Mockito.times(1);
    }
}