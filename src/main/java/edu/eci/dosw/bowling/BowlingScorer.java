package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Calcula el puntaje total de un juego de bowling a partir de sus frames.
 * Es una clase sin estado: recibe datos y devuelve un numero.
 */
public class BowlingScorer {

    /** Tiros de bono que otorga un spare. */
    private static final int SPARE_BONUS_ROLLS = 1;

    /** Tiros de bono que otorga un strike. */
    private static final int STRIKE_BONUS_ROLLS = 2;

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
            return Frame.MAX_PINS + sumOfNextRolls(frames, index, STRIKE_BONUS_ROLLS);
        }
        if (frame.isSpare()) {
            return Frame.MAX_PINS + sumOfNextRolls(frames, index, SPARE_BONUS_ROLLS);
        }
        return frame.pinsKnockedDown();
    }

    private int sumOfNextRolls(List<Frame> frames, int index, int howMany) {
        List<Integer> upcoming = rollsAfter(frames, index);
        int sum = 0;
        for (int i = 0; i < howMany && i < upcoming.size(); i++) {
            sum += upcoming.get(i);
        }
        return sum;
    }

    /** Todos los tiros que vienen despues del frame indicado, en orden. */
    private List<Integer> rollsAfter(List<Frame> frames, int index) {
        List<Integer> upcoming = new ArrayList<>();
        for (int i = index + 1; i < frames.size(); i++) {
            upcoming.addAll(frames.get(i).getRolls());
        }
        return upcoming;
    }
}
