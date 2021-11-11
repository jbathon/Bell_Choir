import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;

public class Tone {

  public static void main(String[] args) {

    /*
     * Creates new AudioFormat
     */
    final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
    Tone t = new Tone(af);

    /*
     * Loads songs in the songs folder into memory
     */
    TrackLoader loader = new TrackLoader();
    final List<Song> songs = loader.getSongs();

    /*
     * Prompts the User to choose a song to play from properly formated songs
     */
    System.out.println("Track List:");
    for (int i = 0; i < songs.size(); i++) {
      Song song = songs.get(i);
      System.out.println("   " + (i + 1) + ") " + song.title);
    }
    System.out.println("From the list above, input the track number of the song you want the choir to sing.");

    /*
     * Checks for valid track number
     */
    try (Scanner consoleScanner = new Scanner(System.in)) {
      while (!consoleScanner.hasNext("[1-" + songs.size() + "]")) {
        System.err.println("Invaild Track! Please input a number between 1 and " + songs.size());
        consoleScanner.next();
      }

      /*
       * Takes the track number the user inputed and passes the corresponding song to
       * the conductor to play
       */

      int trackNum = consoleScanner.nextInt() - 1;
      Song song = songs.get(trackNum);

      Conductor c = new Conductor(song, t);

      System.out.println("Now Playing: " + song.title);

      c.playSong();
      consoleScanner.close();
    }

  }

  /*
   * Defines a Tone as AudioFormat
   */
  public final AudioFormat af;

  Tone(AudioFormat af) {
    this.af = af;
  }

}
