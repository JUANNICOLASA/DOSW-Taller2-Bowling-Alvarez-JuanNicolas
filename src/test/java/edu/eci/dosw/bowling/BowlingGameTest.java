package edu.eci.dosw.bowling;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Modulo A - validaciones y estado de {@link BowlingGame#roll(int)}.
 * Modulo C - reglas de cierre de {@link BowlingGame#isComplete()}.
 */
@DisplayName("BowlingGame - modulos A (roll) y C (isComplete)")
class BowlingGameTest {

    @Test
    @DisplayName("A1 - roll(0) registra el tiro sin lanzar excepcion")
    void rollZeroPins_registersRollInFrame() {
        BowlingGame game = new BowlingGame();

        assertDoesNotThrow(() -> game.roll(0));

        assertEquals(1, game.getFrames().size());
        assertEquals(0, game.getFrames().get(0).getRolls().get(0));
    }

    @Test
    @DisplayName("A2 - roll(-1) lanza IllegalArgumentException")
    void rollNegativePins_throwsIllegalArgumentException() {
        BowlingGame game = new BowlingGame();

        assertThrows(IllegalArgumentException.class, () -> game.roll(-1));
    }

    @Test
    @DisplayName("A3 - roll(11) lanza IllegalArgumentException")
    void rollMoreThanTenPins_throwsIllegalArgumentException() {
        BowlingGame game = new BowlingGame();

        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }

    // ------------------------------------------------------------- helpers

    /** Juega N tiros iguales. */
    private void rollMany(BowlingGame game, int times, int pins) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }

    /** Juego perfecto: 12 strikes. */
    private void rollPerfectGame(BowlingGame game) {
        for (int i = 0; i < 12; i++) {
            game.roll(10);
        }
    }

    /** Ultimo frame registrado hasta el momento. */
    private Frame lastFrame(BowlingGame game) {
        List<Frame> frames = game.getFrames();
        return frames.get(frames.size() - 1);
    }
}
