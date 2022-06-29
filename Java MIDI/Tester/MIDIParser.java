/**
 * MIDIParser - class for parsing MIDI files
 */
package midiparse;

/*import DeltaTime;
import Event;*/

import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
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

    public ArrayList<Event> events = new ArrayList<>();
    public ArrayList<Event> noteOnEvents = new ArrayList<>();
    public ArrayList<Event> noteOffEvents = new ArrayList<>();
    
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
        FileInputStream fis = new FileInputStream(this.midiFile);
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
        // byte[] deltaTime;
        int trackChunkType = dis.readInt();
        int trackHeaderLen = dis.readInt(); // length to the EOF
        /*while (dis.available() > 0 && !this.endOfTrack) {
            byte[] b = new byte[4];
            dis.read(b);
            
            // Handle Events
            
            // End Of Track
            this.endOfTrack = (b[1] == (byte)0xFF && b[2] == (byte)0x2F && b[3] == (byte)0x00);
            // System.out.printf("%x %x %x %x\n", b[0], b[1], b[2], b[3]);

            // Time Signature
            if (b[1] == (byte)0xFF && b[2] == (byte)0x58 && b[3] == (byte)0x04) {
                this.timeSignature[0] = dis.readByte(); // nn
                this.timeSignature[1] = dis.readByte(); // dd
                this.timeSignature[2] = dis.readByte(); // cc
                this.timeSignature[3] = dis.readByte(); // bb
                break;
            }

            deltaTime = getDeltaTime(b);
        }*/

        ArrayList<Byte> bytes = new ArrayList<>();
        ArrayList<Byte> deltaTime = new ArrayList<>();
        DeltaTime tmpDelta = new DeltaTime();

        boolean isPrevVLV = false;
        boolean isNoteOn = false;
        boolean isNoteOff = false;

        while (dis.available() > 0) {
            byte b = dis.readByte();
            bytes.add(b);
            ArrayList<Byte> eventParams = new ArrayList<>();

            // Construct a delta time of event using VLV
            if (deltaTime.size() < 4) {
                if (b >= (byte)0x80 && !isPrevVLV) {
                    deltaTime.add(b);
                    System.out.printf("HERE 1: %2x \n", b);
                    isPrevVLV = true;
                } else if (b >= (byte)0x80 && isPrevVLV) {
                    deltaTime.add(b);
                    System.out.printf("HERE 2: %x \n", b);
                    // System.out.printf("%x ", b);
                    isPrevVLV = true;
                } else if (b < (byte)0x80 && isPrevVLV) {
                    deltaTime.add(b);
                    System.out.printf("HERE 3: %x \n", b);
                    System.out.println(deltaTime);
                    
                    //System.out.println();

                    // System.out.printf("%x \n", b);
                    isPrevVLV = false;
                    tmpDelta.setVlvBytes(deltaTime);
                    deltaTime.clear();
                    continue;
                }
            }

            // Cunstruct the event; Note On, Note Off
            if (tmpDelta != null) {
                // System.out.println(tmpDelta.toString());
                if (b <= (byte)0x8F && b >= (byte)0x80) { // Note OFF
                    eventParams.add(dis.readByte()); // note number
                    eventParams.add(dis.readByte()); // note velocity
                    this.noteOffEvents.add(new Event(tmpDelta, b, eventParams));
                } else if (b <= (byte)0x9F && b >= (byte)0x90) { // Note ON
                    eventParams.add(dis.readByte()); // note number
                    eventParams.add(dis.readByte()); // note velocity
                    this.noteOnEvents.add(new Event(tmpDelta, b, eventParams));
                }
            }

            if (bytes.size() >= trackHeaderLen) {
                if (bytes.get(bytes.size()-1) == (byte)0x00 &&
                    bytes.get(bytes.size()-2) == (byte)0x2F &&
                    bytes.get(bytes.size()-3) == (byte)0xFF   ) {
                        break; // reached End Of Track
                    }
            }
        }

        // System.out.printf("%x %x %x %x\n", this.timeSignature[0], this.timeSignature[1], this.timeSignature[2], this.timeSignature[3]);

        dis.close();
        fis.close();

    }

    public void getHeaderChunk(FileInputStream fis, DataInputStream dis) throws Exception {
        // TODO
    }

    public void getTrackChunks(FileInputStream fis, DataInputStream dis) throws Exception {
        // TODO
    }

    public static byte[] getDeltaTime(byte[] b) {
        ArrayList<Byte> al = new ArrayList<>();
        for (int i = 0; i < b.length; i++) {
            int cmp = Byte.compare(b[i], (byte)0x80);
            if (cmp == 0 && cmp > 0) {
                System.out.printf("%x ", b[i]);
                al.add(new Byte(b[i]));
            }
        }

        byte[] result = new byte[al.size()];
        for (int i = 0; i < al.size(); i++) {
            result[i] = al.get(i).byteValue();
            
        }
        System.out.println();

        return result;
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

    public static int byteToInt(byte b) {
        return (b & 0xFF);
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

    public void getLastMeta() throws Exception {
        FileInputStream fis = new FileInputStream(midiFile);
        DataInputStream dis = new DataInputStream(fis);

        ArrayList<Byte> bytes = new ArrayList<>();

        int ffCount = 0;
        

        while (dis.available() > 0) {
            byte b = dis.readByte();
            bytes.add(b);
        }

        for (Byte b : bytes) {
            System.out.printf("%x ", b.byteValue());
        }
        System.out.println();

        byte[] tmpBytes = new byte[3];
        for (int i = bytes.size()-4; i >= 0; i--) {
            tmpBytes[0] = bytes.get(i).byteValue();
            if (tmpBytes[0] == (byte)0xff) {
                tmpBytes[1] = bytes.get(i+1).byteValue();
                tmpBytes[2] = bytes.get(i+2).byteValue();
                break;
            }
        }

        int index = 0;
        for (int i = 0; i < bytes.size(); i++) {
            boolean cmp1 = Byte.compare(bytes.get(i  ).byteValue(), tmpBytes[0]) == 0;
            boolean cmp2 = Byte.compare(bytes.get(i+1).byteValue(), tmpBytes[1]) == 0;
            boolean cmp3 = Byte.compare(bytes.get(i+2).byteValue(), tmpBytes[2]) == 0;

            if (cmp1 && cmp2 && cmp3) {
                index += i;
                break;
            }
        }

        System.out.println("Index of last Meta: " + index);

        dis.close();
        fis.close();
    }
}