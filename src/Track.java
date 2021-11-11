/**
 * Defines a {@code Track} as a {@code String} title and a {@code String} path.
 * 
 * @author Jaden C. Bathon
 * 
 */

public class Track {
  private final String title;
  private final String path;

  /**
   * Defines a {@code Track} as a {@code String} title and a {@code String} path.
   * 
   * @param The  title of a track.
   * @param path The relative location of the track in the file system.
   */

  Track(String title, String path) {
    this.title = title;
    this.path = path;
  }

  /**
   * @return The title of the track.
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return The {@code String} of relative location of the track in the file
   *         system.
   */
  public String getPath() {
    return path;
  }
}
