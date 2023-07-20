package com.example.mamooddiary;

import java.util.ArrayList;

public class MockData {

    public static ArrayList<MockNote> getMockNotes() {
        ArrayList<MockNote> notes = new ArrayList<>();

        notes.add(new MockNote(22, 3, 2023, "This is a note", "happy"));
        notes.add(new MockNote(1, 4, 2023, "This is a note","sad" ));
        notes.add(new MockNote(15, 4, 2023, "This is a note", "notok"));
        notes.add(new MockNote(2, 5, 2023, "This is a note", "smile"));
        notes.add(new MockNote(4, 5, 2023, "This is a note", "normal"));

        return notes;
    }
}

