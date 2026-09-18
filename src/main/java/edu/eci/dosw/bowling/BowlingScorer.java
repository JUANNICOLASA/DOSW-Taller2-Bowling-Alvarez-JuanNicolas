package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

// Calcula el puntaje total a partir de los frames jugados. No guarda estado.
public class BowlingScorer {

    private static final int SPARE_BONUS_ROLLS = 1;
    private static final int STRIKE_BONUS_ROLLS = 2;

    public int calculate(List<Frame> frames) {
        if (frames == null) {
            throw new IllegalArgumentException("La lista de frames no puede ser nula");
        }
        int total = 0;
        for (int index = 0; index < frames.size(); index++) {
            total += frameScore(frames, index);
        }
        return total;
    }

    // El frame 10 ya contiene sus propios tiros de bono, por eso suma directo.
    private int frameScore(List<Frame> frames, int index) {
        Frame frame = frames.get(index);
        if (frame.isLastFrame()) {
            return frame.pinsKnockedDown();
        }
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

    private List<Integer> rollsAfter(List<Frame> frames, int index) {
        List<Integer> upcoming = new ArrayList<>();
        for (int i = index + 1; i < frames.size(); i++) {
            upcoming.addAll(frames.get(i).getRolls());
        }
        return upcoming;
    }
}
