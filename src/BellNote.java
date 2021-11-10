
public class BellNote {
	final Note note;
	final NoteLength length;

	BellNote(Note note, NoteLength length) {
		this.note = note;
		this.length = length;
	}

	@Override
	public String toString() {
		return note.toString() + " " + length.toString();
	}
}
