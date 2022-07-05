import java.io.File;
import java.util.*;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;

public class Tester {
    public static final String FILEPATH = "testFiles\\twinkle.mid";
    public static final int[][] RANGES = new int[][] {
            { 24, 44 },
            { 36, 56 },
            { 48, 68 },
            { 60, 80 },
            { 72, 92 },
            { 84, 104},
            { 96, 116},
            {108, 128},
    };
    public static final String[] NOTESNAMES = new String[] {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#"
    };

    public static HashMap<String, Integer[]> notesNumbers = new HashMap<>();
    public static ArrayList<String> notesAndDurs = new ArrayList<>();
    public static int[] notes;
    public static int[] durs;

    public static void main(String[] args) {
        fillNotesHashMap();

        for (String key : notesNumbers.keySet()) {
            System.out.println(key + " : " + Arrays.toString(notesNumbers.get(key)));
        }

        try {
            readMidi(new File(FILEPATH));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readMidi(File midiFile) throws Exception {
        MidiFile midi = new MidiFile(midiFile);

        if (midi.getTrackCount() > 1)
            throw new InvalidMidi("This MIDI file has more than 1 track! Please, choose a different MIDI.");

        MidiTrack track = midi.getTracks().get(0);
        Iterator<MidiEvent> it = track.getEvents().iterator();

        List<MidiEvent> midiEvents = new ArrayList<>();

        while (it.hasNext()) {
            MidiEvent event = it.next();

            if (event instanceof NoteOn || event instanceof NoteOff) {
                midiEvents.add(event);
                // System.out.println(event.getDelta());
            }
        }

        for (int i = 0; i < midiEvents.size()-1; i+=2) {
            MidiEvent event = midiEvents.get(i);
            MidiEvent nextEvent = midiEvents.get(i+1);

            long noteDuration = ((NoteOn)event).getDelta();
            int noteVal = ((NoteOn)event).getNoteValue();

            if (nextEvent instanceof NoteOn) {
                if (((NoteOn) nextEvent).getVelocity() == 0) {
                    noteDuration += nextEvent.getDelta();
                }
            } else if (nextEvent instanceof NoteOff) {
                noteDuration += nextEvent.getDelta();
            }

            notesAndDurs.add(String.format("%d %d", noteVal, noteDuration));
        }

        notes = getNotesFromString();
        durs = getNotesDurationsFromString();

        boolean isMidiInRange = isMidiInRange();
        if (!isMidiInRange)
            throw new InvalidMidi("This MIDI file is not in range of the Xylophone. Please, choose a different file.");
    }

    public static int[] getNoteOnOffCount(MidiTrack track) {
        int sumOff = 0;
        int sumOn = 0;
        for (MidiEvent event : track.getEvents()) {
            if (event instanceof NoteOff)
                sumOff++;
            else if (event instanceof NoteOn)
                sumOn++;
        }

        return new int[]{sumOn, sumOff};
    }

    public static int[] getNotesFromString() {
        int[] notes = new int[notesAndDurs.size()];

        for (int i = 0; i < notes.length; i++) {
            String[] tmp = notesAndDurs.get(i).split(" ");
            notes[i] = Integer.parseInt(tmp[0]);
        }

        return notes;
    }

    public static int[] getNotesDurationsFromString() {
        int[] durs = new int[notesAndDurs.size()];

        for (int i = 0; i < durs.length; i++) {
            String[] tmp = notesAndDurs.get(i).split(" ");
            durs[i] = Integer.parseInt(tmp[1]);
        }

        return durs;
    }

    public static int getMaxNoteFromString() {
        int max = Integer.MIN_VALUE;

        for (int note : notes) {
            if (note > max)
                max = note;
        }

        return max;
    }

    public static int getMinNoteFromString() {
        int min = Integer.MAX_VALUE;

        for (int note : notes) {
            if (note < min)
                min = note;
        }

        return min;
    }

    public static boolean isMidiInRange() {
        int minNote = getMinNoteFromString();
        int maxNote = getMaxNoteFromString();

        boolean minInRange = false;
        boolean maxInRange = false;

        int minRangeInd = 0;
        int maxRangeInd = 0;

        // System.out.printf("min: %d, max: %d\n", minNote, maxNote);

        for (int i = 0; i < RANGES.length; i++) {
            // check if minNote is in range
            if (isNumBetween(minNote, RANGES[i][0], RANGES[i][1])) {
                minInRange = true;
                minRangeInd += i;
            }

            // check if maxNote is in range
            if (isNumBetween(maxNote, RANGES[i][0], RANGES[i][1])) {
                maxInRange = true;
                maxRangeInd += i;
            }
        }

        boolean areBothInRange = (minRangeInd == maxRangeInd);
        if (!areBothInRange) {
//            areBothInRange = (
//                    isNumBetween(minNote, RANGES[maxRangeInd][0], RANGES[maxRangeInd][1]) ||
//                    isNumBetween(maxNote, RANGES[minRangeInd][0], RANGES[minRangeInd][1])
//            );

            if (isNumBetween(minNote, RANGES[maxRangeInd][0], RANGES[maxRangeInd][1]))
                minRangeInd = maxRangeInd;

            areBothInRange = (minRangeInd == maxRangeInd);
        }

        return (minInRange && maxInRange) && areBothInRange;
    }

    public static boolean isNumBetween(int num, int min, int max) {
        return (num >= min && num <= max);
    }

    public static void fillNotesHashMap() {
        for (int i = 0; i < 11; i++) {
            int startNote = i + 24;
            Integer[] allNoteNums = getAllNumbersOfNote(startNote);

            notesNumbers.put(NOTESNAMES[i], allNoteNums);
        }
    }

    public static Integer[] getAllNumbersOfNote(int note) {
        ArrayList<Integer> noteNums = new ArrayList<>();
        int noteNum = note;
        while (noteNum < 128) {
            noteNums.add(noteNum);
            noteNum += 12;
        }

        return noteNums.toArray(new Integer[0]);
    }
}

class InvalidMidi extends Exception {
    public InvalidMidi(String errorMessage) {
        super(errorMessage);
    }
}