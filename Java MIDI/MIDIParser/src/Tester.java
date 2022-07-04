import java.io.EOFException;
import java.io.File;
import java.util.Arrays;

import midiparse.Event;
import midiparse.MIDIParser;

public class Tester {
    public static final String FILEPATH = "testFiles\\twinkle.mid";

    public static void main(String[] args) {
        MIDIParser midiParser = new MIDIParser(new File(FILEPATH));


        /*byte b = 0x00;

        if (b >= 0x80) {
            System.out.println();
        }*/
        // System.out.println(String.format("%x", b));

        try {
            midiParser.extractNoteOnOffEvents();

            /*for (Event evnt : midiParser.noteOnEvents) {
                System.out.println(evnt.toString());
            }*/

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}