package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

// Motor de un juego de bowling para un jugador. Controla el estado del juego:
// que tiros son validos y cuando termina. El puntaje lo calcula BowlingScorer.
public class BowlingGame {

    public static final int TOTAL_FRAMES = 10;

    private final List<Frame> frames;
    private final BowlingScorer scorer;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.scorer = new BowlingScorer();
        this.currentFrame = 0;
    }

    public void roll(int pins) {
        validatePinRange(pins);
        validateGameIsOpen();
        Frame frame = currentFrameOrCreate();
        validateFrameCapacity(frame, pins);
        frame.addRoll(pins);
        advanceIfClosed(frame);
    }

    public int score() {
        if (!isComplete()) {
            throw new IllegalStateException(
                    "El juego no esta completo: aun no se puede calcular el puntaje");
        }
        return scorer.calculate(frames);
    }

    public boolean isComplete() {
        return frames.size() == TOTAL_FRAMES
                && frames.get(TOTAL_FRAMES - 1).isComplete();
    }

    public List<Frame> getFrames() {
        return List.copyOf(frames);
    }

    private void validatePinRange(int pins) {
        if (pins < 0 || pins > Frame.MAX_PINS) {
            throw new IllegalArgumentException(
                    "El numero de pinos debe estar entre 0 y " + Frame.MAX_PINS + ", pero fue: " + pins);
        }
    }

    private void validateGameIsOpen() {
        if (isComplete()) {
            throw new IllegalStateException("El juego ya termino: no se admiten mas tiros");
        }
    }

    private void validateFrameCapacity(Frame frame, int pins) {
        if (frame.wouldExceedPins(pins)) {
            throw new IllegalArgumentException(
                    "Un frame no puede derribar mas de " + Frame.MAX_PINS + " pinos");
        }
    }

    private Frame currentFrameOrCreate() {
        if (currentFrame >= frames.size()) {
            frames.add(new Frame(currentFrame + 1));
        }
        return frames.get(currentFrame);
    }

    private void advanceIfClosed(Frame frame) {
        if (frame.isComplete()) {
            currentFrame++;
        }
    }
}
