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
import java.sql.Array;
import java.util.*;
import java.util.concurrent.CountDownLatch;


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
    public Event eotEvent;
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

    private void initBasicTrackStuff() throws Exception {
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


        /*
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
        */
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
                al.add(b[i]);
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

    public static int byteScaleToInt(byte num) {
        return (num & 0xFF);
    }

    private ArrayList<Byte> extractMidiEvents() throws Exception {
        FileInputStream fis = new FileInputStream(this.midiFile);
        DataInputStream dis = new DataInputStream(fis);

        ArrayList<Byte> bytes = new ArrayList<>();


        while (dis.available() > 0) {
            byte b = dis.readByte();
            bytes.add(b);
        }
        dis.close();
        fis.close();


        // get the 1st Meta event, that isn't EOT
        ArrayList<Byte> newBytes = new ArrayList<Byte>();
        for (int i = bytes.size()-4; i >= 0; i--) {
            byte tmpByte = bytes.get(i);
            newBytes.add(tmpByte);

            if (tmpByte == (byte)0xff)
                break;
        }

        Collections.reverse(newBytes);
        newBytes.add((byte)0xff);
        newBytes.add((byte)0x2f);
        newBytes.add((byte)0x00);


        String decoded = Event.decodeMeta(new byte[]{
                newBytes.get(0),
                newBytes.get(1),
                newBytes.get(2),
        });

        int metaEventLen = Event.metaEventsNumOfArgs.get(decoded) + decoded.split(" ").length;

        newBytes.subList(0, metaEventLen).clear();

        return newBytes;
    }

    public void extractNoteOnOffEvents() throws Exception {
        ArrayList<Byte> bytes = extractMidiEvents();

        ArrayList<Byte> deltaTime = new ArrayList<>();
        DeltaTime tmpDelta = new DeltaTime();

        boolean isPrevVLV = true;
        boolean isNoteOn = false;
        boolean isNoteOff = false;

        System.out.println(bytes.get(0));
        for (int i = 0; i < bytes.size()-5; i++) {
            byte b = bytes.get(i);
            ArrayList<Byte> eventParams = new ArrayList<>();

            int ib = b & 0xFF;

            if (isPrevVLV && deltaTime.size() < 4) {
                if (ib >= 0x80) {
                    deltaTime.add(b);
                    continue;
                } else {
                    deltaTime.add(b);
                    isPrevVLV = false;
                    tmpDelta.setVlvBytes(deltaTime);
                    deltaTime.clear();
                    continue;
                }
            }

            if (!tmpDelta.getVlvBytes().isEmpty()) {
                eventParams.add(bytes.get(++i));
                eventParams.add(bytes.get(++i));

                int cmpNoteOn1 = Byte.compareUnsigned(b, (byte)0x9f);
                int cmpNoteOn2 = Byte.compareUnsigned(b, (byte)0x90);
                isNoteOn = (ib >= 0x90) && (ib <= 0x9f); //(cmpNoteOn1 <= 0 && cmpNoteOn2 >= 0);

                int cmpNoteOff1 = Byte.compareUnsigned(b, (byte)0x8f);
                int cmpNoteOff2 = Byte.compareUnsigned(b, (byte)0x80);
                isNoteOff = (ib >= 0x80) && (ib <= 0x8f); //(cmpNoteOff1 <= 0 && cmpNoteOff2 >= 0);

                if (isNoteOn) { // Note ON
                    this.noteOnEvents.add(new Event(tmpDelta, b, eventParams));
                } else if (isNoteOff) { // Note OFF
                    this.noteOffEvents.add(new Event(tmpDelta, b, eventParams));
                }
                isPrevVLV = true;
                tmpDelta = new DeltaTime();
            }
        }


        ArrayList<Byte> eotTime = new ArrayList<>();
        ArrayList<Byte> eotCode = new ArrayList<>();

        eotTime.add(bytes.get(bytes.size()-4));
        eotCode.add((byte)0xff);
        eotCode.add((byte)0x2f);
        eotCode.add((byte)0x00);

        this.eotEvent = new Event(new DeltaTime(eotTime), eotCode);

        System.out.println("Note ON events:");
        System.out.println(Event.eventArrayListToString(this.noteOnEvents));
        System.out.println("\nNote OFF events:");
        System.out.println(Event.eventArrayListToString(this.noteOffEvents));
    }

//    public static void printBytes(ArrayList<Byte> bytes) {
//        for (byte b : bytes) {
//            System.out.printf("%x ", b);
//        }
//        System.out.println();
//    }
}