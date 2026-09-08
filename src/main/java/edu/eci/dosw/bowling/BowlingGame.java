package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    /** Pinos disponibles en cada tiro inicial. */
    private static final int MAX_PINS = 10;

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /** Registra pinos derribados. Valida el rango y la capacidad del frame. */
    public void roll(int pins) {
        validatePinRange(pins);
        Frame frame = currentFrameOrCreate();
        int alreadyDown = 0;
        for (int rolled : frame.getRolls()) {
            alreadyDown += rolled;
        }
        if (alreadyDown + pins > MAX_PINS) {
            throw new IllegalArgumentException(
                    "Un frame no puede derribar mas de " + MAX_PINS + " pinos");
        }
        frame.addRoll(pins);
    }

    private void validatePinRange(int pins) {
        if (pins < 0 || pins > MAX_PINS) {
            throw new IllegalArgumentException(
                    "El numero de pinos debe estar entre 0 y " + MAX_PINS + ", pero fue: " + pins);
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
        // TODO: implementar con TDD
        return false;
    }

    public List<Frame> getFrames() { return List.copyOf(frames); }
}
