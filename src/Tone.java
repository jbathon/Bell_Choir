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
    	
    	try (Scanner s = new Scanner(System.in)) {
			while(!s.hasNext("[1-" + songs.size() +"]")) {
				System.out.println("Invaild Track! Please input a number between 1 and " + songs.size());
				s.next();
			}
			
			int trackNum = s.nextInt() - 1;
			Song song = songs.get(trackNum);
			
			Conductor c = new Conductor(song, af);
			
			c.playSong();
		}
        
        
    }

    private final AudioFormat af;

    Tone(AudioFormat af) {
        this.af = af;
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
}



