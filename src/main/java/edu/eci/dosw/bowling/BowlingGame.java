package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    /** Frames que tiene un juego completo. */
    public static final int TOTAL_FRAMES = 10;

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /** Registra pinos derribados. Valida rango, capacidad del frame y fin del juego. */
    public void roll(int pins) {
        validatePinRange(pins);
        if (isComplete()) {
            throw new IllegalStateException("El juego ya termino: no se admiten mas tiros");
        }
        Frame frame = currentFrameOrCreate();
        if (frame.wouldExceedPins(pins)) {
            throw new IllegalArgumentException(
                    "Un frame no puede derribar mas de " + Frame.MAX_PINS + " pinos");
        }
        frame.addRoll(pins);
        if (frame.isStrike() || frame.getRolls().size() == 2) {
            currentFrame++;
        }
    }

    private void validatePinRange(int pins) {
        if (pins < 0 || pins > Frame.MAX_PINS) {
            throw new IllegalArgumentException(
                    "El numero de pinos debe estar entre 0 y " + Frame.MAX_PINS + ", pero fue: " + pins);
        }
    }

    private Frame currentFrameOrCreate() {
        if (currentFrame >= frames.size()) {
            frames.add(new Frame());
        }
        return frames.get(currentFrame);
    }

    /** Puntaje total. Lanza IllegalStateException si el juego no esta completo. */
    public int score() {
        // TODO: implementar con TDD
        return 0;
    }

    /** true cuando los 10 frames han sido completados. */
    public boolean isComplete() {
        return frames.size() == TOTAL_FRAMES
                && frames.get(TOTAL_FRAMES - 1).getRolls().size() == 2;
    }

    public List<Frame> getFrames() { return List.copyOf(frames); }
}
