import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TrackLoader contains to method {@code getSongs()} and {@code getSong()} these
 * methods are used to read files stored in the {@link /songs} directory. These
 * after being read these files are being converted into the {@code Song}
 * object.
 * 
 * @author Jaden C. Bathon
 * @see Song
 * @see BellNote
 */
final public class TrackLoader {

  /**
   * Returns a list of Songs. Theses Songs are loaded in from files in the songs
   * directory. this method will exit the program if no songs where found or not formated correctly.
   * 
   * @return A list of the Songs Object.
   * @see Song
   */

  public List<Song> getSongs() {

    File dir = new File("songs");

    List<Song> songs = new ArrayList<>();

    if (dir.isDirectory()) {
      File[] files = dir.listFiles(); // An array of all the files in songs.
      for (File file : files) {
        if (file.isFile()) {
          final String path = file.getPath(); // Gets the path of the file.
          String name = file.getName(); // Gets File Name.
          int extIndex = name.lastIndexOf(".");
          final String title = name.substring(0, extIndex == -1 ? name.length() : extIndex); // Removes extension.
          Song song = getSong(path, title); // Converts the file at the given path to a Song
          if (song.exists()) { // Checks if the song exist before adding to songs
            songs.add(song);
          }
        }
      }
    }
    /*
     * Before returning songs the method checks if there are actually songs for the
     * program to play. If no songs are available the an error message is printed.
     */
    if (songs.size() <= 0) {
      System.err.println("No vaild songs found.");
      System.exit(-1);
    }
    return songs;
  }

  /**
   * Reads the file at the given {@code path} and tries to convert the file to a
   * list of {@code BellNotes} and create a new {@code Song} from the list of
   * {@code BellNotes}. If fails to convert the file the program notifies the user
   * of the line where the error occurred and returns an nonexistent song.
   * 
   * @param path  a {@code String} that is the location of a file.
   * @param title a {@code String} that is the title of the file passed.
   * @return a {@code Song} if the file was formated correctly otherwise returns
   *         null.
   * @see Song
   */

  public Song getSong(String path, String title) {
    List<BellNote> song = new ArrayList<>();
    File file = new File(path);
    if (file.exists()) { // Checks if the file passed exists.
      int index = 1; // To keep track of line numbers.
      try (Scanner songScanner = new Scanner(file)) {
        while (songScanner.hasNextLine()) { // Runs while there are lines to read
          String line = songScanner.nextLine(); // Gets a line.
          try (Scanner lineScanner = new Scanner(line)) {
            Note note = Note.valueOf(lineScanner.next()); // Gets the note of that line
            NoteLength length = NoteLength.getNoteLength(lineScanner.nextInt()); // Gets the Length of that note
            song.add(new BellNote(note, length));
            lineScanner.close(); // Closes the Scanner.
          }
          index++; // Increase Line Number
        }
        songScanner.close(); // Closes the Scanner.
      } catch (Exception e) { // If file format was invalid it prints the title and line.
        System.err.println("Invaild song format for " + title + " at line " + index); 
        return null; // Returns nonexistent song.
      }
    } else { // If file does not exist.
      System.err.println(title + " not found at \"" + path + "\".");
      return null; // Returns nonexistent song.
    }

    return new Song(song, title); // Returns Song if file format was valid.
  }
}
