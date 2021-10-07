import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import java.io.File;

public class Tone {
	
    public static void main(String[] args) throws Exception {
    	
    	final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
    	Tone t = new Tone(af);
    	
    	final List<Song> songs = t.getSongs();
    	System.out.println("Track List:");
    	for (int i = 0; i < songs.size(); i++) {
    		System.out.println("   "+ (i+1) + ") " + songs.get(i).title);
    	}
    	System.out.println("From the list above, input the track number of the song you want the choir to sing.");
    	
    	Scanner s = new Scanner(System.in);
    	
    	while(!s.hasNext("[1-" + songs.size() +"]")) {
    		System.out.println("Invaild Track! Please input a number between 1 and " + songs.size());
    		s.next();
    	}
    	
    	int trackNum = s.nextInt() - 1;
    	Song song = songs.get(trackNum);
        
        t.playSong(songs.get(trackNum).getBellNotes());
    }

    private final AudioFormat af;

    Tone(AudioFormat af) {
        this.af = af;
    }

    void playSong(List<BellNote> song) throws LineUnavailableException {
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            line.open();
            line.start();

            for (BellNote bn: song) {
                playNote(line, bn);
            }
            line.drain();
        }
    }
    
    private  List<Song> getSongs() {
    	
    	File dir = new File("songs");
    	
    	List<Song> songs = new ArrayList<Song>();
    	
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
	        for(File file : files){
	        	if(file.isFile()) {
	        		final String path = file.getPath();
	        		final String title = path.substring(path.indexOf("\\")+1);
	        		List<BellNote> song;
					song = getSong(path);
					songs.add(new Song(song, title));
	        	}
	        }
    	}
    	return songs;
    }
    
    private List<BellNote> getSong(String path) {
    	List<BellNote> song = new ArrayList<BellNote>();
    	File file = new File(path);
    	Scanner scanny;
    	Note note;
    	NoteLength length;
    	try {
		    scanny = new Scanner(file);
		    while(scanny.hasNextLine()) {
		        note = Note.valueOf(scanny.next());
		        length = NoteLength.getNoteLength(scanny.nextInt());
		        song.add(new BellNote(note,length));
		        scanny.nextLine();
		    }
    	} catch (Exception ignore) {}
    	return song;
    }
    
   
    private void playNote(SourceDataLine line, BellNote bn) {
        final int ms = Math.min(bn.length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(bn.note.sample(), 0, length);
        line.write(Note.REST.sample(), 0, 50);
    }
}

class BellNote {
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

class Song {
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

enum NoteLength {
    WHOLE(1.0f),
    HALF(0.5f),
    QUARTER(0.25f),
    EIGTH(0.125f);

    private final int timeMs;

    private NoteLength(float length) {
        timeMs = (int)(length * Note.MEASURE_LENGTH_SEC * 1000);
    }

    public int timeMs() {
        return timeMs;
    }
    
    public static NoteLength getNoteLength(int n) {
    	switch (n) {
    	case 1:
    		return NoteLength.WHOLE;
    	case 2:
    		return NoteLength.HALF;
    	case 4:
    		return NoteLength.QUARTER;
    	case 8:
    		return NoteLength.EIGTH;
    	default:
    		throw new RuntimeException("Invalid value for NoteLength " + n);
    	}
    }
}

enum Note {
    // REST Must be the first 'Note'
    REST,
    A4,
    A4S,
    B4,
    C4,
    C4S,
    D4,
    D4S,
    E4,
    F4,
    F4S,
    G4,
    G4S,
    A5;

    public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
    public static final int MEASURE_LENGTH_SEC = 1;

    // Circumference of a circle divided by # of samples
    private static final double step_alpha = (2.0d * Math.PI) / SAMPLE_RATE;

    private final double FREQUENCY_A_HZ = 440.0d;
    private final double MAX_VOLUME = 127.0d;

    private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

    private Note() {
        int n = this.ordinal();
        if (n > 0) {
            // Calculate the frequency!
            final double halfStepUpFromA = n - 1;
            final double exp = halfStepUpFromA / 12.0d;
            final double freq = FREQUENCY_A_HZ * Math.pow(2.0d, exp);

            // Create sinusoidal data sample for the desired frequency
            final double sinStep = freq * step_alpha;
            for (int i = 0; i < sinSample.length; i++) {
                sinSample[i] = (byte)(Math.sin(i * sinStep) * MAX_VOLUME);
            }
        }
    }

    public byte[] sample() {
        return sinSample;
    }
}