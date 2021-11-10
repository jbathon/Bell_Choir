import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final public class TrackLoader {
	public List<Song> getSongs() {

		File dir = new File("songs");

		List<Song> songs = new ArrayList<>();

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					final String path = file.getPath();
					// final String title = path.substring(path.indexOf("\\")+1);
					final String title = path.substring(path.indexOf("/") + 1);
					List<BellNote> bellNotes = getSong(path, title);
					Song song = new Song(bellNotes, title);
					if (song.exists()) {
						songs.add(song);
					}
				}
			}
		}
		if (songs.size() <= 0) {
			System.err.println("No vaild songs found.");
			System.exit(-1);
		}
		return songs;
	}

	public List<BellNote> getSong(String path, String title) {
		List<BellNote> song = new ArrayList<>();
		File file = new File(path);
		if (file.exists()) {
			int index = 1;
			try (Scanner songScanner = new Scanner(file)) {
				while (songScanner.hasNextLine()) {
					String line = songScanner.nextLine();
					try (Scanner lineScanner = new Scanner(line)) {
						Note note = Note.valueOf(lineScanner.next());
						NoteLength length = NoteLength.getNoteLength(lineScanner.nextInt());
						song.add(new BellNote(note, length));
						lineScanner.close();
					}
					index++;
				}
				songScanner.close();
			} catch (Exception e) {
				System.err.println("Invaild song format for " + title + " at line " + index);
				return null;
			}
		} else {
			System.err.println(title + " not found at \"" + path + "\".");
		}

		return song;
	}
}
