/**
 * MIDIParser - class for parsing MIDI files
 */
package midiparse;


import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.Arrays;

public class MIDIParser {
    public byte format;

    private File midiFile;
    
    public MIDIParser(File midiFile) {
        this.midiFile = midiFile;
    }

    public File getMidiFile() {
        return midiFile;
    }

    public void setMidiFile(File midiFile) {
        this.midiFile = midiFile;
    }

    /**
     * INT -> 4 B
     */
    public void readHex() throws Exception {
        // TODO
        FileInputStream fis = new FileInputStream(midiFile);
        while (fis.available() > 0) {
            int data = fis.read();
            System.out.printf("%x   ", data);
        }
        fis.close();
    }

    public void parse() throws Exception {
        FileInputStream fis = new FileInputStream(midiFile);
        DataInputStream dis = new DataInputStream(fis);
        
        // Header Stuff
        int header = dis.readInt();
        int headerLen = dis.readInt();
        byte[] headerData = new byte[headerLen];

        for (int i = 0; i < headerData.length; i++)
            headerData[i] = dis.readByte();
        
        /**
         * MIDI format:
         * 0 - the file contains a single multi-channel track
         * 1 - the file contains 1 or more simultaneous tracks (or MIDI outputs) of a sequence
         * 3 - the file contains 1 or more sequentially independent single-track patterns
         */
        this.format = headerData[0];

        
        // Track Stuff


        
    }

    public void getHeaderChunk() throws Exception {
        // TODO
    }

    public void getTrackChunks() throws Exception {
        // TODO
    }

    public static void printArrayAsHex(byte[] arr) {
        for (int i = 0; i < arr.length; i++)
            System.out.printf("%x ", arr[i]);
        System.out.println();
    }
}