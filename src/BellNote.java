import javax.sound.sampled.AudioFormat;

/**
 * Defines a {@code BellNote} as a {@code Note} and a {@code NoteLength}.
 * 
 * @author Jaden C. Bathon
 * 
 * @see Note
 * @see NoteLength
 */

public class BellNote {
  final Note note;
  final NoteLength length;

  BellNote(Note note, NoteLength length) {
    this.note = note;
    this.length = length;
  }

  /**
   * Returns a String that represents a {@code BellNote} in the format
   * {@code Note} + " " + {@code NoteLength}.
   */
  @Override
  public String toString() {
    return note.toString() + " " + length.toString();
  }
}