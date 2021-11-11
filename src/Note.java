/**
 * {@code Note} assigns a frequency value to musical notes A through G from
 * octaves 0 to 8.
 * 
 * @author Jaden C. Bathon
 */

public enum Note {
  // REST Must be the first 'Note'
  REST, A0, A0S, B0, C0, C0S, D0, D0S, E0, F0, F0S, G0, G0S, A1, A1S, B1, C1, C1S, D1, D1S, E1, F1, F1S, G1, G1S, A2,
  A2S, B2, C2, C2S, D2, D2S, E2, F2, F2S, G2, G2S, A3, A3S, B3, C3, C3S, D3, D3S, E3, F3, F3S, G3, G3S, A4, A4S, B4, C4,
  C4S, D4, D4S, E4, E4S, F4, F4S, G4, G4S, A5, A5S, B5, C5, C5S, D5, D5S, E5, F5, F5S, G5, G5S, A6, A6S, B6, C6, C6S,
  D6, D6S, E6, F6, F6S, G6, G6S, A7, A7S, B7, C7, C7S, D7, D7S, E7, F7, F7S, G7, G7S, A8, A8S, B8, C8;

  public static final int SAMPLE_RATE = 192 * 1024; // ~192KHz so the sound is smoother.
  public static final int MEASURE_LENGTH_SEC = 2; // ~120 bpm.

  // Circumference of a circle divided by # of samples
  private static final double step_alpha = (2.0d * Math.PI) / SAMPLE_RATE;

  private final double FREQUENCY_A_HZ = 27.5d; // Starting frequency of Note A0.
  private final double MAX_VOLUME = 30.0d; // Turned this down it was too loud and blowing out my speakers.

  private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

  /*
   * Assigns a frequency value to each {@code Note}.
   */
  private Note() {
    int n = this.ordinal();
    if (n > 0) {
      // Calculate the frequency!
      final double halfStepUpFromA = n - 1;
      final double exp = halfStepUpFromA / 12.0d;
      final double freq = FREQUENCY_A_HZ * Math.pow(2.0d, exp);

      // Create sinusoidal data sample for the desired frequency.
      final double sinStep = freq * step_alpha;
      for (int i = 0; i < sinSample.length; i++) {
        sinSample[i] = (byte) (Math.sin(i * sinStep) * MAX_VOLUME);
      }
    }
  }

  public byte[] sample() {
    return sinSample;
  }
}

/**
 * {@code NoteLength} defines the different lengths musical notes can be.
 * 
 * @author Jaden C. Bathon
 */

enum NoteLength {
  WHOLE(1.0f), HALF(0.5f), THIRD(0.375f), QUARTER(0.25f), SIXTH(0.1875f), EIGTH(0.125f), SIXTEENTH(0.0625f);

  private final int timeMs;

  private NoteLength(float length) {
    timeMs = (int) (length * Note.MEASURE_LENGTH_SEC * 1000);
  }

  public int timeMs() {
    return timeMs;
  }

  /**
   * {@code getNoteLength()} returns a {@code NoteLength} equivalent to the given
   * integer.
   * 
   * @param n length of not in integer form.
   * @return A {@code NoteLength} equivalent to the given integer.
   * @throws RuntimeException No {@code NoteLength} was found for the given
   *                          integer.
   */

  public static NoteLength getNoteLength(int n) {
    switch (n) {
    case 1:
      return NoteLength.WHOLE;
    case 2:
      return NoteLength.HALF;
    case 3:
      return NoteLength.THIRD; // QUARTER with dot.
    case 4:
      return NoteLength.QUARTER;
    case 6:
      return NoteLength.SIXTH; // EIGTH with dot.
    case 8:
      return NoteLength.EIGTH;
    case 16:
      return NoteLength.SIXTEENTH;
    default:
      throw new RuntimeException("Invalid value for NoteLength " + n);
    }
  }
}
