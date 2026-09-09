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
