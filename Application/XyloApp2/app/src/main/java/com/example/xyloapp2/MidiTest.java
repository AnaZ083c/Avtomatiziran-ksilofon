package com.example.xyloapp2;

/*
 * TEST FAILED UNSUCCESSFULLY
 */

import com.leff.midi.MidiFile;

import java.io.File;

public class MidiTest {
    public static void main(String[] args) {
        File input = new File("twinkle.mid");

        try {
            MidiFile midi = new MidiFile(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
