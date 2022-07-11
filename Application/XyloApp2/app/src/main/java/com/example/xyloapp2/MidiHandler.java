package com.example.xyloapp2;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.List;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.util.MidiUtil;

public class MidiHandler {
    public static Tempo tempo;


    public static final int[][] RANGES = new int[][] {
            { 24, 43 },
            { 36, 55 },
            { 48, 67 },
            { 60, 79 },
            { 72, 91 },
            { 84, 103},
            { 96, 115},
            {108, 127},
    };
    public static final String[] NOTESNAMES = new String[] {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#"
    };

    public int rangeIndex = 0;
    public HashMap<Integer, Character> btSignalMap = new HashMap<>();
    public HashMap<String, Integer[]> notesNumbers = new HashMap<>();
    public ArrayList<String> notesAndDurs = new ArrayList<>();
    public int[] notes;
    public long[] durs;
    public long[] dursMs;


    private static ToneGenerator beeper = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private ArrayList<MidiEvent> noteOnOffs = new ArrayList<>();
    private ArrayList<Long> noteOnOffsDurs = new ArrayList<>();
    private Uri midiUri;
    private ContentResolver cr;


    // CONSTRUCTOR

    public MidiHandler(ContentResolver cr, Uri midiUri) throws Exception {
        this.midiUri = midiUri;
        this.cr = cr;

        fillNotesHashMap();
        this.readMidi(cr.openInputStream(midiUri));
        mapToSignal();
    }


    // METHODS

    public Uri getMidiUri() {
        return midiUri;
    }

    public void setMidiUri(Uri midiUri) {
        this.midiUri = midiUri;
    }

    public ContentResolver getCr() {
        return cr;
    }

    public void setCr(ContentResolver cr) {
        this.cr = cr;
    }

    private void readMidi(InputStream midiInputStream) throws Exception {
        MidiFile midi = new MidiFile(midiInputStream);

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
            } else if (event instanceof Tempo) {
                tempo = (Tempo)event;
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
            this.noteOnOffs.add(event);
            this.noteOnOffsDurs.add(noteDuration);

        }

        /* Convert a delta time of a note into milliseconds */
        int counter = 0;
        for (MidiEvent event : noteOnOffs) {
            long deltaTime = noteOnOffsDurs.get(counter);
            long ms = MidiUtil.ticksToMs(deltaTime, tempo.getBpm(), midi.getResolution());
            // System.out.printf("Note: %d, Delta: %d -- DeltaToMs: %d\n", notes[counter], deltaTime, ms);

            notesAndDurs.set(counter, notesAndDurs.get(counter) + " " + ms);

            counter++;
        }
        /* ****************************** */

        notes = getNotesFromString();
        durs = getNotesDurationsFromString();
        dursMs = getNotesDursMsFromString();

        boolean isMidiInRange = isMidiInRange();
        if (!isMidiInRange)
            throw new InvalidMidi("This MIDI file is not in range of the Xylophone. Please, choose a different file.");
    }

    public int[] getNoteOnOffCount(MidiTrack track) {
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

    public int[] getNotesFromString() {
        int[] notes = new int[notesAndDurs.size()];

        for (int i = 0; i < notes.length; i++) {
            String[] tmp = notesAndDurs.get(i).split(" ");
            notes[i] = Integer.parseInt(tmp[0]);
        }

        return notes;
    }

    public long[] getNotesDurationsFromString() {
        long[] durs = new long[notesAndDurs.size()];

        for (int i = 0; i < durs.length; i++) {
            String[] tmp = notesAndDurs.get(i).split(" ");
            durs[i] = Long.parseLong(tmp[1]);
        }

        return durs;
    }

    public long[] getNotesDursMsFromString() {
        long[] dursMs = new long[notesAndDurs.size()];

        for (int i = 0; i < dursMs.length; i++) {
            String tmp[] = notesAndDurs.get(i).split(" ");
            dursMs[i] = Long.parseLong(tmp[2]);
        }

        return dursMs;
    }

    public int getMaxNoteFromString() {
        int max = Integer.MIN_VALUE;

        for (int note : notes) {
            if (note > max)
                max = note;
        }

        return max;
    }

    public int getMinNoteFromString() {
        int min = Integer.MAX_VALUE;

        for (int note : notes) {
            if (note < min)
                min = note;
        }

        return min;
    }

    public boolean isMidiInRange() {
        int minNote = this.getMinNoteFromString();
        int maxNote = this.getMaxNoteFromString();

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

        rangeIndex = minRangeInd;

        return (minInRange && maxInRange) && areBothInRange;
    }

    public static boolean isNumBetween(int num, int min, int max) {
        return (num >= min && num <= max);
    }

    public void fillNotesHashMap() {
        for (int i = 0; i < 11; i++) {
            int startNote = i + 24;
            Integer[] allNoteNums = getAllNumbersOfNote(startNote);

            notesNumbers.put(NOTESNAMES[i], allNoteNums);
        }
    }

    public void mapToSignal() {
        char c = 97; // start at 'a'
        int start = RANGES[rangeIndex][0], end = RANGES[rangeIndex][1];

        // map the non-sharps first
        for (int i = start; i <= end; i++) {
            if (!isSharp(i)) {
                btSignalMap.put(i, c);
                c++;
            }
        }

        c = 109; // start at 'm'

        // map the sharps
        for (int i = start; i <= end; i++) {
            if (c == 'q')
                c++;

            if (isSharp(i)) {
                btSignalMap.put(i, c);
                c++;
            }
        }

//        System.out.println("start: " + start + " end: " + end);
//        for (int note : btSignalMap.keySet()) {
//            System.out.println("note: " + note + " mapped: " + btSignalMap.get(note));
//        }

    }

    public boolean isSharp(int note) {
        for (String key : notesNumbers.keySet()) {
            if (key.length() > 1 && key.charAt(1) == '#') {
                for (Integer val : notesNumbers.get(key)) {
                    if (val == note)
                        return true;
                }
            }
        }
        return false;
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

    public void tickTest(boolean printDelays, Context context) throws Exception {
        for (String s : notesAndDurs) {
            String[] tmp = s.split(" ");
            long delay = Long.parseLong(tmp[1]); //Long.parseLong(tmp[2]) / (long)Math.pow(10, 6);

            if (printDelays) Toast.makeText(context, String.format("%d", delay), Toast.LENGTH_LONG).show();
            beep();
            Thread.sleep(delay);
        }
    }

    public static void beep() {
        beeper.startTone(ToneGenerator.TONE_CDMA_PIP, 44);
    }
}

class InvalidMidi extends Exception {
    public InvalidMidi(String errorMessage) {
        super(errorMessage);
    }
}