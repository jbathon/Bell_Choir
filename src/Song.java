import java.util.List;

public class Song {
	final List<BellNote> bellNotes;
	final String title;

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
		String str = "";
		for (BellNote n : bellNotes) {
			str += n.toString() + "\n";
		}
		return str;
	}

}