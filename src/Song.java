import java.util.List;

public class Song {
	final List<BellNote> song;
	final String title;
	
	Song(List<BellNote> song, String title) {
		this.song = song;
		this.title = title;
	}
	
	public List<BellNote> getBellNotes() {
		return song;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(BellNote n : song) {
			str += n.toString() + "\n";
		}
		return str;
	}
}