import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;


public class Tone {
	
    public static void main(String[] args) throws Exception {
    	
    	final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, false);
    	Tone t = new Tone(af);
    	
    	TrackLoader loader = new TrackLoader();
    	final List<Song> songs = loader.getSongs();

    	System.out.println("Track List:");
    	for (int i = 0; i < songs.size(); i++) {
    		Song song = songs.get(i);
    			System.out.println("   "+ (i+1) + ") " + song.title);
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

    

}



