import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Defines a {@code Conductor} as a {@code Song} and an {@code AudioFormat}. To
 * play a song the {@code Conductor} create a list of {@code Members} and tells
 * them when it is there turn to play there assigned note.
 * 
 * @author Jaden C. Bathon
 * 
 * @see AudioFormat
 * @see Song
 * @see BellNotes
 * @see Member
 */

public class Conductor {

  private final Song song;
  public final AudioFormat af;

  /**
   * Defines a {@code Conductor} as a Song and an {@code AudioFormat}.
   * 
   * @param song A {@code Song} object
   * 
   * @see AudioFormat
   * @see Song
   */
  Conductor(Song song) {
    this.song = song;
    af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
  }

  /**
   * Plays the song assigned to the {@code Conductor}. To do this the
   * {@code Conductor} creates a array of {@code Members} for every {@code Note}.
   * The {@code Conductor} then assign {@code Members} there {@code Note}. The
   * {@code Conductor} then cycles through every {@code BellNote} in the songs and
   * tell the {@code Member} with the corresponding {@code Note} to play there
   * {@code Note} the {@code NoteLength} of the note.
   * 
   * @see BellNote
   * @see Note
   * @see NoteLength
   */
  public void playSong() {
    try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {

      line.open();
      line.start();

      int numNotes = Note.values().length;
      final Member[] members = new Member[numNotes];

      for (Note n : Note.values()) {
        members[n.ordinal()] = new Member(n, line);
      }

      for (BellNote bn : song.getBellNotes()) {
        Note note = bn.note;
        NoteLength length = bn.length;

        members[note.ordinal()].giveTurn(length);
      }

      for (Member m : members) {
        m.stopMember();
      }

      line.drain();
    } catch (LineUnavailableException e) {
      System.err.println("Fatal Error: occured when trying to play " + song.title);
      System.exit(-1);
    }
  }
}
