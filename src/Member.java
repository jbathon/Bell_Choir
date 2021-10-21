import javax.sound.sampled.SourceDataLine;

public class Member implements Runnable {
	private final Note note;
	private final Thread t;
	private NoteLength length;
    private volatile boolean running;
    private boolean myTurn;
    private SourceDataLine line;
	
	Member (Note note, SourceDataLine line) {
		this.note = note;
		this.line = line;
		length = null;
        t = new Thread(this, note.toString());
        t.start();
	}
	
    public void giveTurn(NoteLength length) {
        synchronized (this) {
            if (myTurn) {
                throw new IllegalStateException("Attempt to give a turn to a player who's hasn't completed the current turn");
            }
            myTurn = true;
            this.length = length; 
            notify();
            while (myTurn) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }
        }
    }
	
    public void stopMember() {
        running = false;
    }
    

	@Override
    public void run() {
        running = true;
        synchronized (this) {
            do {
                // Wait for my turn
                while (!myTurn) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {}
                }

                // My turn!
                playNote();

                // Done, complete turn and wakeup the waiting process
                myTurn = false;
                length = null;
                notify();
            } while (running);
        }
    }
	
	private void playNote() {
        final int ms = Math.min(length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
        final int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.sample(), 0, length);
        line.write(Note.REST.sample(), 0, 50);
	}
}
