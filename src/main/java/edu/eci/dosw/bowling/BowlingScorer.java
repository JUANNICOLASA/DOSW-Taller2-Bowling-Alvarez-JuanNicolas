package edu.eci.dosw.bowling;

import java.util.ArrayList;
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
        if (frame.isStrike()) {
            return Frame.MAX_PINS + sumOfNextRolls(frames, index, 2);
        }
        if (frame.isSpare()) {
            return Frame.MAX_PINS + sumOfNextRolls(frames, index, 1);
        }
        return frame.pinsKnockedDown();
    }

    private int sumOfNextRolls(List<Frame> frames, int index, int howMany) {
        List<Integer> upcoming = new ArrayList<>();
        for (int i = index + 1; i < frames.size(); i++) {
            upcoming.addAll(frames.get(i).getRolls());
        }
        int sum = 0;
        for (int i = 0; i < howMany && i < upcoming.size(); i++) {
            sum += upcoming.get(i);
        }
        return sum;
    }
}
