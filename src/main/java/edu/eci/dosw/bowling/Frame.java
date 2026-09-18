package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

// Un frame del juego. Sabe cuantos pinos quedan en pie y cuando queda cerrado.
// El frame 10 tiene reglas propias: con strike o spare gana un tercer tiro.
public class Frame {

    public static final int MAX_PINS = 10;
    public static final int LAST_FRAME_NUMBER = 10;

    private static final int REGULAR_ROLLS = 2;
    private static final int LAST_FRAME_ROLLS = 3;

    private final int number;
    private final List<Integer> rolls = new ArrayList<>();

    public Frame(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

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
        return pins > standingPins();
    }

    public boolean isComplete() {
        if (isStrike() && !isLastFrame()) {
            return true;
        }
        return rolls.size() == expectedRolls();
    }

    public boolean isStrike() {
        return !rolls.isEmpty() && rolls.get(0) == MAX_PINS;
    }

    public boolean isSpare() {
        return !isStrike()
                && rolls.size() >= REGULAR_ROLLS
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

    private int expectedRolls() {
        if (isLastFrame() && earnedBonusRoll()) {
            return LAST_FRAME_ROLLS;
        }
        return REGULAR_ROLLS;
    }

    private boolean earnedBonusRoll() {
        return isStrike() || isSpare();
    }

    // En el frame 10 los pinos se levantan de nuevo tras un strike o un spare.
    private int standingPins() {
        if (isLastFrame() && pinsWereReset()) {
            return MAX_PINS;
        }
        if (isLastFrame() && isStrike()) {
            return MAX_PINS - rolls.get(1);
        }
        return MAX_PINS - pinsKnockedDown();
    }

    private boolean pinsWereReset() {
        if (isStrike()) {
            return rolls.size() == 1 || rolls.get(1) == MAX_PINS;
        }
        return isSpare();
    }
}
