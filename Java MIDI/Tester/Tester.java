import java.io.File;
import midiparse.MIDIParser;

public class Tester {
    public static final String FILEPATH = "Java MIDI\\Tester\\testFiles\\twinkle.mid"; 

    public static void main(String[] args) throws Exception {
        MIDIParser midiParser = new MIDIParser(new File(FILEPATH));

        midiParser.parse();
    }
}
