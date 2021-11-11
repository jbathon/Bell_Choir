import java.util.List;

/**
 * Song Consist of a list of {@code BellNotes} bellNotes and a {@code Sting} title. Songs can check if they exist (not {@value null}), return bellNotes, or return a string of the song.
 * 
 * @author Jaden C. Bathon
 * @see BellNotes
 */

public class Song {
  
  private final List<BellNote> bellNotes;
  public final String title;
 
  /**
   * Defines a song as a list of {@code BellNotes} and a title that is a {@code String} 
   * 
   * @param bellNotes A list of {@code BellNotes}.
   * @param title A String referring to the title of the song.
   * @see BellNotes
   */
  Song(List<BellNote> bellNotes, String title) {
    this.bellNotes = bellNotes;
    this.title = title;
  }

  public List<BellNote> getBellNotes() {
    return bellNotes;
  }

  public boolean exists() {
    return bellNotes != null;
  }

  @Override
  public String toString() {
    String str = title + "\n";
    for (BellNote n : bellNotes) {
      str += n.toString() + "\n";
    }
    return str;
  }

}