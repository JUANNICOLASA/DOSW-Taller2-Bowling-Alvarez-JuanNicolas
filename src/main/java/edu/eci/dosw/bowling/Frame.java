package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Un frame de un juego de bowling. Guarda los tiros que se hicieron en el,
 * conoce las reglas de capacidad de pinos y su propio estado.
 */
public class Frame {

    /** Pinos disponibles en un frame. */
    public static final int MAX_PINS = 10;

    private final List<Integer> rolls = new ArrayList<>();

    public void addRoll(int pins) {
        rolls.add(pins);
    }

    public List<Integer> getRolls() {
        return List.copyOf(rolls);
    }

    public int pinsKnockedDown() {
        int total = 0;
        for (int pins : rolls) {
            total += pins;
        }
        return total;
    }

    public boolean wouldExceedPins(int pins) {
        return pinsKnockedDown() + pins > MAX_PINS;
    }

    /** true si el primer tiro derribo los 10 pinos. */
    public boolean isStrike() {
        return !rolls.isEmpty() && rolls.get(0) == MAX_PINS;
    }

    /** true cuando el frame ya no admite mas tiros. */
    public boolean isComplete() {
        return isStrike() || rolls.size() == 2;
    }

    /** Estado del frame segun los tiros registrados. */
    public FrameStatus getStatus() {
        if (isStrike()) {
            return FrameStatus.STRIKE;
        }
        return FrameStatus.OPEN;
    }
}
