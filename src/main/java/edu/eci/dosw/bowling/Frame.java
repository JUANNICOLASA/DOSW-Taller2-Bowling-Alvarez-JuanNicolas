package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Un frame de un juego de bowling. Guarda los tiros que se hicieron en el
 * y conoce las reglas de capacidad de pinos.
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

    /** Total de pinos derribados dentro de este frame. */
    public int pinsKnockedDown() {
        int total = 0;
        for (int pins : rolls) {
            total += pins;
        }
        return total;
    }

    /** true si agregar {@code pins} derribaria mas pinos de los que hay en pie. */
    public boolean wouldExceedPins(int pins) {
        return pinsKnockedDown() + pins > MAX_PINS;
    }
}
