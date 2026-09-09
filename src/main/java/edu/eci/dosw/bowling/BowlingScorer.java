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
        return frames.get(index).pinsKnockedDown();
    }
}
