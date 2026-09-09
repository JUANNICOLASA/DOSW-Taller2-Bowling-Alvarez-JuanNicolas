package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Un frame de un juego de bowling.
 *
 * <p>Conoce su numero, los tiros que recibio, cuantos pinos siguen en pie
 * y cuando queda cerrado. El frame 10 tiene reglas propias: si termina en
 * strike o en spare gana un tercer tiro y los pinos se reinician.</p>
 */
public class Frame {

    /** Pinos disponibles en un frame. */
    public static final int MAX_PINS = 10;

    /** Numero del ultimo frame del juego. */
    public static final int LAST_FRAME_NUMBER = 10;

    /** Tiros de un frame corriente. */
    private static final int REGULAR_ROLLS = 2;

    /** Tiros maximos del frame 10 cuando hay bono. */
    private static final int LAST_FRAME_ROLLS = 3;

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
        return pins > standingPins();
    }

    /** true cuando el frame ya no admite mas tiros. */
    public boolean isComplete() {
        if (isStrike() && !isLastFrame()) {
            return true;
        }
        return rolls.size() == expectedRolls();
    }

    /** true si el primer tiro derribo los 10 pinos. */
    public boolean isStrike() {
        return !rolls.isEmpty() && rolls.get(0) == MAX_PINS;
    }

    /** true si los 10 pinos cayeron en dos tiros del mismo frame. */
    public boolean isSpare() {
        return !isStrike()
                && rolls.size() >= REGULAR_ROLLS
                && rolls.get(0) + rolls.get(1) == MAX_PINS;
    }

    /** Estado del frame segun los tiros registrados. */
    public FrameStatus getStatus() {
        if (isStrike()) {
            return FrameStatus.STRIKE;
        }
        if (isSpare()) {
            return FrameStatus.SPARE;
        }
        return FrameStatus.OPEN;
    }

    /** Cantidad de tiros que necesita este frame para quedar cerrado. */
    private int expectedRolls() {
        if (isLastFrame() && earnedBonusRoll()) {
            return LAST_FRAME_ROLLS;
        }
        return REGULAR_ROLLS;
    }

    /** Solo el frame 10 gana un tiro extra, y solo si cerro con strike o spare. */
    private boolean earnedBonusRoll() {
        return isStrike() || isSpare();
    }

    /** Pinos que siguen en pie justo antes del proximo tiro de este frame. */
    private int standingPins() {
        if (isLastFrame() && pinsWereReset()) {
            return MAX_PINS;
        }
        if (isLastFrame() && isStrike()) {
            return MAX_PINS - rolls.get(1);
        }
        return MAX_PINS - pinsKnockedDown();
    }

    /** En el frame 10 los pinos se levantan de nuevo tras un strike o un spare. */
    private boolean pinsWereReset() {
        if (isStrike()) {
            return rolls.size() == 1 || rolls.get(1) == MAX_PINS;
        }
        return isSpare();
    }
}
