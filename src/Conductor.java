import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Conductor {

  private final Song song;
  private final Tone t;

  /*
   * Defines Conductor as a Song and Tone
   */
  Conductor(Song song, Tone t) {
    this.song = song;
    this.t = t;
  }

  public void playSong() {
    try (final SourceDataLine line = AudioSystem.getSourceDataLine(t.af)) {

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
      System.err.println("Fatal Error occured when trying to play " + song.title);
      System.exit(-1);
    }
  }
}
