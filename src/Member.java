import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

/**
 * A {@code Member} is a Tread that constantly wait for its turn to play it
 * {@code Note}. The {@code Member} is told when and for how long to play a
 * {@code Note} by the {@code Conductor}.
 * 
 * @author Jaden C. Bathon
 * 
 * @see Conductor
 * @See Note
 * @See NoteLength
 */

public class Member implements Runnable {
  private final Note note;
  private final Thread t;
  private NoteLength length;
  private volatile boolean running;
  private boolean myTurn;
  private SourceDataLine line;

  /**
   * Defines a {@code Member} as a {@code Note}, {@code SourceDataLine},
   * {@code NoteLength}, and Daemon {@code Thread}.
   * 
   * @param note A {@code Note} object.
   * @param line A {@code SourceDataLine} object
   * 
   * @see Note
   * @see NoteLength
   * @see Thread
   * @see SourceDataLine
   */

  Member(Note note, SourceDataLine line) {
    this.note = note;
    this.line = line;
    length = null;
    t = new Thread(this, note.toString());
    t.setDaemon(true); // Kills the thread after it is done running.
    t.start();
    myTurn = false;
  }

  /**
   * Set {@code myTurn} to {@code true} when the {@code Conductor} tell the tread
   * it is their turn. By setting {@code myTurn} to {@code true} the
   * {@code Member} is able to play their note.
   * 
   * @param length A {@code NoteLength} Object.
   * 
   * @throws IllegalStateException When the {@code Member} already has their turn.
   * 
   * @see Conductor
   * @see NoteLength
   */

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
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  /**
   * Tells the {@code Thread} to stop running and die.
   */

  public void stopMember() {
    running = false;
  }

  /**
   * Has the {@code Member} wait until the {@code Conductor} notify them that it
   * is their turn to play their {@code Note}. Once it the {@code Member}'s turn
   * the they play their {@code Note} the Member adds thier {@code Note} to the
   * {@code line} and then sets {@code myTurn} to {@code false} and then notify
   * the other {@code Members}. When exits the do while loop the {@code Thread}
   * dies.
   * 
   * @see Thread
   */
  @Override
  public void run() {
    running = true;
    synchronized (this) {
      do {
        // Wait for my turn
        while (!myTurn) {
          try {
            wait();
          } catch (InterruptedException ignored) {
          }
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
