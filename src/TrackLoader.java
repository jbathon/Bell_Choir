import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TrackLoader contains to method {@code getTracks()} and {@code getSong()}
 * these methods are used to read files stored in the {@link /songs} directory.
 * 
 * @author Jaden C. Bathon
 * @see Track
 * @see Song
 * @see BellNote
 */
final public class TrackLoader {

  /**
   * Returns a list of {@code Track}. Theses {@code Track} are loaded in from
   * files in the songs directory. This method will exit the program if the song
   * directory was not found.
   * 
   * @return A list of {@code Track} Object.
   * @see Track
   */

  public List<Track> getTracks() {

    File dir = new File("songs"); // loads the songs directory

    /*
     * Checks if the songs directory exists if it does not the jukebox powers off.
     */

    if (!dir.exists()) {
      System.err.println("Fatal Error: \"/songs\" directory was not found.");
      System.exit(-1);
    }

    List<Track> tracks = new ArrayList<>();

    File[] files = dir.listFiles(); // An array of all the files in songs.
    for (File file : files) {
      if (file.isFile()) {
        final String path = file.getPath(); // Gets the path of the file.
        final String name = file.getName(); // Gets File Name.

        final int extIndex = name.lastIndexOf("."); // Finds the index of a file extension.
        final String title = name.substring(0, extIndex == -1 ? name.length() : extIndex); // Removes extension
                                                                                           // if present.
        tracks.add(new Track(title, path));
      }
    }

    return tracks;
  }

  /**
   * Reads the file at the given {@code path} and tries to convert the file to a
   * list of {@code BellNotes} and create a new {@code Song} from the list of
   * {@code BellNotes}. If fails to convert the file the program notifies the user
   * of the line where the error occurred and returns null.
   * 
   * @param track An object with a {@code path} and {@code title}.
   * @return A {@code Song} if the file was formated correctly otherwise returns
   *         null.
   * @see Track
   * @see Song
   */

  public Song getSong(Track track) {

    File file = new File(track.getPath());

    if (file.exists()) { // Checks if the file passed exists.
      List<BellNote> song = new ArrayList<>();
      int index = 1; // To keep track of line numbers.
      try (Scanner songScanner = new Scanner(file)) {
        while (songScanner.hasNextLine()) { // Runs while there are lines to read
          String line = songScanner.nextLine(); // Gets a line.
          if (!line.isEmpty()) {
            try (Scanner lineScanner = new Scanner(line)) {
              Note note = Note.valueOf(lineScanner.next()); // Gets the note of that line
              NoteLength length = NoteLength.getNoteLength(lineScanner.nextInt()); // Gets the Length of that
                                                                                   // note
              song.add(new BellNote(note, length));
              lineScanner.close(); // Closes the Scanner.
            }
          }
          index++; // Increase Line Number
        }
        songScanner.close(); // Closes the Scanner.

        return new Song(song, track.getTitle());

      } catch (Exception e) { // If file format was invalid it prints the title and line.
        System.err.println("Invaild song format for \"" + track.getTitle() + "\" at line " + index);
        return null; // Returns nonexistent song.
      }
    } else { // If file does not exist.
      System.err.println("\"" + track.getTitle() + "\" not found at \"" + track.getPath() + "\".");
      return null; // Returns nonexistent song.
    }

    // Returns Song if file format was valid.
  }
}
