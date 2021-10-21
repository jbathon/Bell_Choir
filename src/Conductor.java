import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Conductor {

	private final Song song;
	private final AudioFormat af;
	
	Conductor(Song song, AudioFormat af) {
		this.song = song;
		this.af = af;
	}
	
	public void playSong() throws LineUnavailableException {
		try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
			
		line.open();
	    line.start();
	    
		int numNotes = Note.values().length;
		final Member[] members = new Member[numNotes];
		
        for(Note n : Note.values()) {
        	members[n.ordinal()] = new Member(n, line);
        }
        
		for (BellNote bn : song.getBellNotes()) {
			Note note = bn.note;
			NoteLength length = bn.length;
			
			members[note.ordinal()].giveTurn(length);
		}
		
		for(Member m : members) {
			m.stopMember();
		}
	
		line.drain();
		}
	}
}
