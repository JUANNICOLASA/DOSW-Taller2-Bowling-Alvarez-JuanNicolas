package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Un frame de un juego de bowling. Conoce su numero, sus tiros,
 * las reglas de capacidad de pinos y su propio estado.
 */
public class Frame {

    /** Pinos disponibles en un frame. */
    public static final int MAX_PINS = 10;

    /** Numero del ultimo frame del juego. */
    public static final int LAST_FRAME_NUMBER = 10;

    private final int number;
    private final List<Integer> rolls = new ArrayList<>();

    public Frame(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    /** true si este es el frame 10, que tiene reglas propias. */
    public boolean isLastFrame() {
        return number == LAST_FRAME_NUMBER;
    }

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
        if (isLastFrame() && isStrike()) {
            if (rolls.size() == 1 || rolls.get(1) == MAX_PINS) {
                return pins > MAX_PINS;
            }
            return rolls.get(1) + pins > MAX_PINS;
        }
        return pinsKnockedDown() + pins > MAX_PINS;
    }

    /** true cuando el frame ya no admite mas tiros. */
    public boolean isComplete() {
        if (isLastFrame()) {
            if (isStrike()) {
                return rolls.size() == 3;
            }
            return rolls.size() == 2;
        }
        return isStrike() || rolls.size() == 2;
    }

    public boolean isStrike() {
        return !rolls.isEmpty() && rolls.get(0) == MAX_PINS;
    }

    public boolean isSpare() {
        return !isStrike()
                && rolls.size() >= 2
                && rolls.get(0) + rolls.get(1) == MAX_PINS;
    }

    public FrameStatus getStatus() {
        if (isStrike()) {
            return FrameStatus.STRIKE;
        }
        if (isSpare()) {
            return FrameStatus.SPARE;
        }
        return FrameStatus.OPEN;
    }
}
