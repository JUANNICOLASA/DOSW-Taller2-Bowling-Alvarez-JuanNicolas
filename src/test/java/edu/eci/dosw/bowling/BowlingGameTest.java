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

    @Test
    @DisplayName("A4 - dos tiros de un frame no pueden sumar mas de 10")
    void twoRollsExceedingTenPins_throwsIllegalArgumentException() {
        BowlingGame game = new BowlingGame();
        game.roll(7);

        assertThrows(IllegalArgumentException.class, () -> game.roll(6));
    }

    @Test
    @DisplayName("A5 - roll() con el juego terminado lanza IllegalStateException")
    void rollAfterGameIsComplete_throwsIllegalStateException() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);

        assertThrows(IllegalStateException.class, () -> game.roll(0));
    }

    @Test
    @DisplayName("A6 - roll(10) marca STRIKE y avanza al siguiente frame")
    void rollTenPins_marksStrikeAndAdvancesFrame() {
        BowlingGame game = new BowlingGame();

        game.roll(10);
        assertEquals(FrameStatus.STRIKE, game.getFrames().get(0).getStatus());

        game.roll(3);
        assertEquals(2, game.getFrames().size());
    }

    @Test
    @DisplayName("A7 - roll(5) + roll(5) marca SPARE")
    void twoRollsCompletingTenPins_marksSpare() {
        BowlingGame game = new BowlingGame();

        game.roll(5);
        game.roll(5);

        assertEquals(FrameStatus.SPARE, game.getFrames().get(0).getStatus());
    }

    @Test
    @DisplayName("A8 - el frame 10 con strike acepta tres tiros")
    void tenthFrameWithStrike_acceptsThreeRolls() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);

        assertDoesNotThrow(() -> {
            game.roll(10);
            game.roll(10);
            game.roll(10);
        });

        assertEquals(3, lastFrame(game).getRolls().size());
    }

    @Test
    @DisplayName("C1 - isComplete() es false al iniciar el juego")
    void isComplete_atGameStart_isFalse() {
        BowlingGame game = new BowlingGame();

        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C2 - isComplete() es false con nueve frames completos")
    void isComplete_afterNineFrames_isFalse() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 4);

        assertEquals(9, game.getFrames().size());
        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C3 - isComplete() es true con diez frames normales")
    void isComplete_afterTenRegularFrames_isTrue() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 4);

        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C4 - el spare del frame 10 exige el tiro de bono")
    void isComplete_withSpareInTenthFrame_requiresBonusRoll() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);
        game.roll(5);
        game.roll(5);

        assertFalse(game.isComplete(), "un spare en el frame 10 otorga un tiro extra");

        game.roll(7);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C5 - el strike del frame 10 exige los dos tiros de bono")
    void isComplete_withStrikeInTenthFrame_requiresTwoBonusRolls() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);

        game.roll(10);
        assertFalse(game.isComplete());

        game.roll(10);
        assertFalse(game.isComplete());

        game.roll(10);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C6 - el juego perfecto queda completo tras el doceavo strike")
    void isComplete_afterPerfectGame_isTrue() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);

        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("Borde - tras un strike en el frame 10 los pinos solo se reinician una vez")
    void tenthFrameAfterStrike_validatesRemainingPins() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);
        game.roll(10);
        game.roll(7);

        assertThrows(IllegalArgumentException.class, () -> game.roll(5));
    }

    @Test
    @DisplayName("Borde - los frames se numeran de 1 a 10 y el ultimo se reconoce")
    void frames_areNumberedFromOneToTen() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 3);

        List<Frame> frames = game.getFrames();
        assertEquals(1, frames.get(0).getNumber());
        assertFalse(frames.get(0).isLastFrame());
        assertEquals(10, frames.get(9).getNumber());
        assertTrue(frames.get(9).isLastFrame());
    }

    @Test
    @DisplayName("Borde - getFrames() devuelve una copia inmutable")
    void getFrames_returnsImmutableCopy() {
        BowlingGame game = new BowlingGame();
        game.roll(4);
        List<Frame> frames = game.getFrames();
        Frame extra = new Frame(2);

        assertThrows(UnsupportedOperationException.class, () -> frames.add(extra));
    }

    @Test
    @DisplayName("Borde - un frame que deja pinos en pie queda OPEN")
    void frameWithStandingPins_isOpen() {
        BowlingGame game = new BowlingGame();
        game.roll(3);
        game.roll(4);

        assertEquals(FrameStatus.OPEN, game.getFrames().get(0).getStatus());
    }

    @Test
    @DisplayName("Borde - FrameStatus expone los tres estados del dominio")
    void frameStatus_hasThreeValues() {
        assertEquals(3, FrameStatus.values().length);
        assertEquals(FrameStatus.SPARE, FrameStatus.valueOf("SPARE"));
        assertEquals(FrameStatus.OPEN, FrameStatus.valueOf("OPEN"));
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
