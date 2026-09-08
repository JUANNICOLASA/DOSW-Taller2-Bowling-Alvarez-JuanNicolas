package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /** Registra pinos derribados. Lanza IllegalArgumentException si pins < 0 o > 10. */
    public void roll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("El numero de pinos debe estar entre 0 y 10: " + pins);
        }
        currentFrameOrCreate().addRoll(pins);
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
