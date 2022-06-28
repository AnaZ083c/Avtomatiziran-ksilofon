import java.io.EOFException;
import java.io.File;
import midiparse.MIDIParser;

public class Tester {
    public static final String FILEPATH = "Java MIDI\\Tester\\testFiles\\twinkle.mid"; 

    public static void main(String[] args) {
        MIDIParser midiParser = new MIDIParser(new File(FILEPATH));

        try {
            midiParser.parse();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
