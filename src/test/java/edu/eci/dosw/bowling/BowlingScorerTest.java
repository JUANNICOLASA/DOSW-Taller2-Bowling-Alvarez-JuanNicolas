package edu.eci.dosw.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Modulo B - bonos de spare y de strike en {@link BowlingScorer#calculate(java.util.List)}.
 */
@DisplayName("BowlingScorer - modulo B (calculate)")
class BowlingScorerTest {

    @Test
    @DisplayName("B1 - un juego con todos los tiros en cero puntua 0")
    void allZeroRolls_scoresZero() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);
        BowlingScorer scorer = new BowlingScorer();

        assertEquals(0, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B2 - un juego sin strikes ni spares suma los pinos derribados")
    void gameWithoutBonuses_scoresSumOfPins() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 4);

        assertEquals(80, game.score());
    }

    @Test
    @DisplayName("B3 - un spare suma 10 mas el primer tiro del frame siguiente")
    void spareInFirstFrame_addsNextRollAsBonus() {
        BowlingGame game = new BowlingGame();
        game.roll(5);
        game.roll(5);
        game.roll(3);
        game.roll(0);
        rollMany(game, 16, 0);

        assertEquals(16, game.score());
    }

    @Test
    @DisplayName("B4 - un strike suma 10 mas los dos tiros siguientes")
    void strikeInFirstFrame_addsNextTwoRollsAsBonus() {
        BowlingGame game = new BowlingGame();
        game.roll(10);
        game.roll(4);
        game.roll(3);
        rollMany(game, 16, 0);

        assertEquals(24, game.score());
    }

    @Test
    @DisplayName("B5 - dos strikes seguidos acumulan bien el bono del primero")
    void twoConsecutiveStrikes_accumulateBonusCorrectly() {
        BowlingGame game = new BowlingGame();
        game.roll(10);
        game.roll(10);
        game.roll(5);
        game.roll(0);
        rollMany(game, 14, 0);

        assertEquals(45, game.score());
    }

    @Test
    @DisplayName("B6 - todos spares con un tiro final de 5 puntua 150")
    void allSparesWithFiveBonus_scores150() {
        BowlingGame game = new BowlingGame();
        rollAllSpares(game, 5);

        assertEquals(150, game.score());
    }

    @Test
    @DisplayName("B7 - juego perfecto: 12 strikes puntua 300")
    void perfectGame_scores300() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);

        assertEquals(300, game.score());
    }

    @Test
    @DisplayName("B8 - score() antes de terminar el juego lanza IllegalStateException")
    void scoreBeforeGameIsComplete_throwsIllegalStateException() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 10, 4);

        assertThrows(IllegalStateException.class, game::score);
    }

    @Test
    @DisplayName("Borde - calculate(null) lanza IllegalArgumentException")
    void calculateWithNullFrames_throwsIllegalArgumentException() {
        BowlingScorer scorer = new BowlingScorer();

        assertThrows(IllegalArgumentException.class, () -> scorer.calculate(null));
    }

    @Test
    @DisplayName("Borde - un juego abierto que termina en spare en el frame 10")
    void openGameEndingWithSpareInTenthFrame_scoresCorrectly() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 3);
        game.roll(6);
        game.roll(4);
        game.roll(7);

        assertEquals(71, game.score());
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

    /** Todos los frames en spare, con un tiro de bono final. */
    private void rollAllSpares(BowlingGame game, int lastBonus) {
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        game.roll(lastBonus);
    }
}
