package model;

import java.util.ArrayList;

//this class represent a collection of songs, with name and description of the collection
public class Playlist extends SongCollection {
    String name;

    private String description;
    //final int maxLoop = 1000;

    //EFFECT: constructs a playlist object
    public Playlist(String name, ArrayList<Song> songs, String description) {
        this.name = name;
        this.description = description;
        super.initializeSongs(songs);
    }

    //MODIFIES: this
    //EFFECT: reset the name to the specified string
    public void setName(String s) {
        name = s;
    }

    //EFFECT return the name of playlist
    public String getName() {
        return name;
    }

    //MODIFIES: this
    //EFFECT: reset the description to the specified string
    public void setDescription(String s) {
        description = s;
    }

    //EFFECT return the description of playlist
    public String getDescription() {
        return description;
    }

    //EFFECT: return a string representation of the playlist
    @Override
    public String toString() {
        return name + ":  " + description;
    }

    //MODIFIES: this
    //EFFECT: add all non-duplicate songs from other playlist to this playlist
    public void addAll(Playlist other) {
        for (Song s: other.getSongs()) {
            if (!this.getSongs().contains(s)) {
                super.addSong(s);
            }
        }
    }

/*
    public boolean play(String mode) {
        int counter = 0;
        do {
            for (Song s: songs) {
                if (!s.play("")) {
                    return false;
                }
            }
            counter++;
        } while (mode.equals(LOOP) && counter < maxLoop);
        return true;
    }

    public void pause(boolean b) {

    }

 */
}
