package model;

import java.io.File;


//this class represents a single song with a name, artist, and .wav file
public class Song {
    private String name;
    private String artist;
    private File file;
    private int lastFramePosition;
    private boolean isEdited;
    final String defaultArtist = "unknown";
    public static final String LOOP = "LOOP";

    //REQUIRE: the File exists
    //EFFECT: constructs a song object
    public Song(String name, File file, String artist) {
        this.name = name;
        this.file = file;
        if (artist == null) {
            this.artist = defaultArtist;
        } else {
            this.artist = artist;
        }

        isEdited = false;
        this.lastFramePosition = 0;
    }

    //REQUIRE: the File exists
    //EFFECT: constructs a song object
    public Song(String name, File file, String artist, boolean isEdited) {
        this(name, file, artist);
        this.isEdited = isEdited;
    }

    //MODIFIES: this
    //EFFECT: change the song name into the specified string
    public void setName(String s) {
        if (!this.name.equals(s)) {
            isEdited = true;
        }
        this.name = s;
    }

    //EFFECT return the name of the song
    public String getName() {
        return name;
    }

    //EFFECT: returns the music file
    public File getFile() {
        return file;
    }

    //EFFECT returns the artist
    public String getArtist() {
        return artist;
    }

    //MODIFIES: this
    //EFFECT: reset the artist name into the given string
    public void setArtist(String artist) {
        if (!this.artist.equals(artist)) {
            isEdited = true;
        }
        this.artist = artist;
    }

    //EFFECT returns the artist
    public int getLastFramePosition() {
        return lastFramePosition;
    }

    //MODIFIES: this
    //EFFECT: reset the artist name into the given string
    public void setLastFramePosition(int position) {
        this.lastFramePosition = position;
    }

    //EFFECT: return whether song information has been edited
    public boolean hasEdited() {
        return isEdited;
    }

    //EFFECT: return a string representation of the song
    @Override
    public String toString() {
        return name + " by " + artist;
    }


}
