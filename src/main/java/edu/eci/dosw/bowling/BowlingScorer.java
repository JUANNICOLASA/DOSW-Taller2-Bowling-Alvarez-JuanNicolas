package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Calcula el puntaje total de un juego de bowling a partir de sus frames.
 *
 * <p>Es una clase sin estado: recibe la lista de frames ya jugados y devuelve
 * un numero. Los frames 1 a 9 reciben el bono de spare o de strike a partir de
 * los tiros siguientes; el frame 10 ya contiene sus propios tiros de bono, asi
 * que su puntaje es la suma directa de lo que derribo.</p>
 */
public class BowlingScorer {

    /** Tiros de bono que otorga un spare. */
    private static final int SPARE_BONUS_ROLLS = 1;

    /** Tiros de bono que otorga un strike. */
    private static final int STRIKE_BONUS_ROLLS = 2;

    /**
     * Calcula el puntaje total de los frames recibidos.
     *
     * @param frames frames jugados, en orden.
     * @return puntaje acumulado del juego.
     * @throws IllegalArgumentException si la lista es nula.
     */
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

    /** Puntaje de un frame, incluyendo el bono que le corresponda. */
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

    /** Suma los primeros {@code howMany} tiros posteriores al frame indicado. */
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
