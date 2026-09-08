package edu.eci.dosw.bowling;

/**
 * Estado con el que cierra un frame de bowling.
 *
 * <ul>
 *   <li>{@link #OPEN}   - quedaron pinos en pie.</li>
 *   <li>{@link #SPARE}  - los 10 pinos cayeron en dos tiros.</li>
 *   <li>{@link #STRIKE} - los 10 pinos cayeron en el primer tiro.</li>
 * </ul>
 */
public enum FrameStatus {
    OPEN,
    SPARE,
    STRIKE
}
