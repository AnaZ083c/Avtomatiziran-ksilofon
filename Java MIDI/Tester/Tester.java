import java.io.EOFException;
import java.io.File;
import java.util.Arrays;

import midiparse.Event;
import midiparse.MIDIParser;

public class Tester {
    public static final String FILEPATH = "Java MIDI\\Tester\\testFiles\\twinkle.mid"; 

    public static void main(String[] args) {
        //MIDIParser midiParser = new MIDIParser(new File(FILEPATH));
        

        byte b = (byte)0xAB;
        System.out.println(String.format("%x", b));
        /*
        try {
            midiParser.getLastMeta();

            /*for (Event evnt : midiParser.noteOnEvents) {
                System.out.println(evnt.toString());
            }

        } catch(Exception e) {
            e.printStackTrace();
        }*/
    }
}
