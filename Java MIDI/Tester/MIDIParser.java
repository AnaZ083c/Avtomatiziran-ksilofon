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


/**
 * MIDI DOCS: http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html
 */
public class MIDIParser {
    // short => 16 bits => 2 bytes
    

    /**
     * MIDI format:
     * 0 - the file contains a single multi-channel track
     * 1 - the file contains 1 or more simultaneous tracks (or MIDI outputs) of a sequence
     * 3 - the file contains 1 or more sequentially independent single-track patterns
     */
    public short format;
    public short ntrks;     // the number of track in the file; 1 or 0
    public short division;  // meaning of delta-times 


    /**
     * Delta Time Format:
     * bit 15 |     bits 14 thru 8     | bits 7 thru 0
     * =======+========================+================
     *     0  | ticks per quarter-note 
     * -------+------------------------+----------------     
     *     1  | negative SMPTE format  | ticks per frame
     */
    public int deltaTimeFormat;
    public byte[] timeSignature = new byte[4]; // numerator, denominator, MIDI clocks in a metronome click, number of notated 32nd-notes in a MIDI quarter-note (24 MIDI clocks)
    public boolean endOfTrack = false;

    private File midiFile;
    private final boolean IS_SINGLE_MULTI_CHANNEL_TRACK = false;
    
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
        int headerChunkType = dis.readInt();
        int headerLen = dis.readInt();

        // Header Data -> 3 x 16 bit words => 6 bytes
        this.format = dis.readShort();
        this.ntrks = dis.readShort();
        this.division = dis.readShort();
        this.deltaTimeFormat = getBitFromWord(shortToInt(this.division), 1, 15);

        if (headerLen > 6) // Honour the length (see MIDI docs: 2.2 last paragraph, last sentence)
            dis.skipBytes(headerLen - 6);

        // Track Stuff
        int trackChunkType = dis.readInt();
        int trackHeaderLen = dis.readInt(); // length to the EOF
        while (dis.available() > 0) {
            byte[] b = new byte[4];
            dis.read(b);
            
            // Handle Events
            
            // End Of Track
            this.endOfTrack = (b[0] == 0xFF && b[1] == 0x2F && b[2] == 0x00);
            System.out.printf("%x %x %x %x\n", b[0], b[1], b[2], b[3]);

            // Time Signature
            if (b[0] == 0xFF && b[1] == 0x58 && b[2] == 0x04) {
                this.timeSignature[0] = dis.readByte(); // nn
                this.timeSignature[1] = dis.readByte(); // dd
                this.timeSignature[2] = dis.readByte(); // cc
                this.timeSignature[3] = dis.readByte(); // bb
                break;
            }

        }
        
        System.out.printf("%x %x %x %x\n", this.timeSignature[0], this.timeSignature[1], this.timeSignature[2], this.timeSignature[3]);

        dis.close();
        fis.close();

    }

    public void getHeaderChunk(FileInputStream fis, DataInputStream dis) throws Exception {
        // TODO
    }

    public void getTrackChunks(FileInputStream fis, DataInputStream dis) throws Exception {
        // TODO
    }

    public static void printArrayAsHex(byte[] arr) {
        for (int i = 0; i < arr.length; i++)
            System.out.printf("%x ", arr[i]);
        System.out.println();
    }

    public static int getBitFromByte(int ind, int packed) {
        int shifted = packed >> (8 * ind);
        int masked = 0x000000FF & shifted;
        return masked;
    }


    /**
     * 
     * @param num number to extract from
     * @param k number of bits to extract
     * @param p starting position
     * @return extracted short number
     */
    public static int getBitFromWord(int num, int k, int p) {
        return (((1 << k) - 1) & (num >> (p - 1)));
    }

    public static int shortToInt(short num) {
        return (0xFFFF & num);
    }
}