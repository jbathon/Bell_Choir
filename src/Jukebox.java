import java.util.List;
import java.util.Scanner;
import java.awt.Color;

/**
 * Contains @{code main()} which presents the user with a track list of songs.
 * Then waits for a user to pick a song. If the song can be played it gives the
 * song to a {@code Conductor} and tells it to play it. Once done it prompts a
 * track list again until an user powers off the {@code Jukebox}.
 * 
 * @author Jaden C. Bathon
 * 
 */

public class Jukebox {
  public static void main(String[] args) {
    while (true) {
      /*
       * Loads songs in the songs folder into memory
       */
      
      final List<Track> trackList = TrackLoader.getTracks();

      /*
       * Prompts the User to choose a song to play from properly formated songs
       */
      System.out.println("Track List:");
      for (int i = 0; i < trackList.size(); i++) {
        Track track = trackList.get(i);
        System.out.println("   " + (i + 1) + ") " + track.getTitle());
      }
      System.out.println("From the list above, input the track number of the song you want the choir to sing.");
      System.out.println("To power off the jukebox input \"-1\".");

      /*
       * Checks for valid track number
       */
      Scanner consoleScanner = new Scanner(System.in);
      while (!consoleScanner.hasNext("[1-" + trackList.size() + "]|(-1)")) {
        System.out.println("Invaild Track! Please input a number between 1 and " + trackList.size());
        consoleScanner.next();
      }

      /*
       * Takes the track number the user inputed and passes the corresponding song to
       * the conductor to play. If the number inputed was "-1" it powers off the
       * Jukebox. If the song can not be played an error is printed in console and the
       * loop starts over.
       */

      int trackNum = consoleScanner.nextInt() - 1;

      if (trackNum + 1 == -1) {
        System.out.println("Powering Off");
        consoleScanner.close();
        System.exit(-1);
      }

      Track track = trackList.get(trackNum);
      Song song = TrackLoader.getSong(track);

      if (song != null) {
        Conductor c = new Conductor(song);
        System.out.println("Now Playing: " + song.title);
        c.playSong();
      } else {
        System.out.println("Error occured when trying to play \"" + track.getTitle() + "\" please select a different track.");
      }

    }
  }
}
