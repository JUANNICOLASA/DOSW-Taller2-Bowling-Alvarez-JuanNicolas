package edu.eci.dosw.bowling;

import java.util.List;

/**
 * Calcula el puntaje total de un juego de bowling a partir de sus frames.
 * Es una clase sin estado: recibe datos y devuelve un numero.
 */
public class BowlingScorer {

    /** Puntaje total del juego. */
    public int calculate(List<Frame> frames) {
        int total = 0;
        for (int index = 0; index < frames.size(); index++) {
            total += frameScore(frames, index);
        }
        return total;
    }

    private int frameScore(List<Frame> frames, int index) {
        Frame frame = frames.get(index);
        if (frame.isSpare()) {
            return Frame.MAX_PINS + firstRollOfNextFrame(frames, index);
        }
        return frame.pinsKnockedDown();
    }

    private int firstRollOfNextFrame(List<Frame> frames, int index) {
        int next = index + 1;
        if (next >= frames.size()) {
            return 0;
        }
        List<Integer> rolls = frames.get(next).getRolls();
        return rolls.isEmpty() ? 0 : rolls.get(0);
    }
}
