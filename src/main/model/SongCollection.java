package model;

import java.io.File;
import java.util.ArrayList;


//this class specify common characteristics of Playlist and MasterPlaylsit,
// with methods to manipulate and search for songs
public abstract class SongCollection {
    private ArrayList<Song> allSongs;
    public static final int UP = -1;
    public static final int DOWN = 1;

    //EFFECT return all songs in playlist
    public ArrayList<Song> getSongs() {
        return allSongs;
    }

    //EFFECT initialize songs to the specified list
    public void initializeSongs(ArrayList<Song> s) {
        allSongs = s;
    }

    //MODIFIES: this
    //EFFECT: add a new song to the playlist
    public void addSong(Song s) {
        allSongs.add(s);
    }

    //MODIFIES: this
    //EFFECT: add a new song to the playlist
    public void addSong(ArrayList<Song> s) {
        allSongs.addAll(s);
    }

    /*
    //MODIFIES: this
    //EFFECT: move song up/down the list by one as specified. return true if successful and false otherwise
    public boolean moveSong(int index, int direction) {
        if ((direction == UP && index == 0)
                || (direction == DOWN && index == allSongs.size() - 1)
                || (direction != UP && direction != DOWN)
                || (index >= allSongs.size() || index < 0)) {
            return false;
        }

        int targetIndex = index + direction;

        Song temp = allSongs.get(index);
        allSongs.set(index, allSongs.get(targetIndex));
        allSongs.set(targetIndex, temp);
        return true;
    }



    //MODIFIES: this
    //EFFECT: remove the song of specified index. If successful, return true; return false otherwise
    public boolean removeSong(int index) {
        if (index >= allSongs.size() || index < 0) {
            return false;
        }
        allSongs.remove(index);
        return true;
    }*/


    //EFFECT return a list of song with the specified artist
    public ArrayList<Song> findSongByArtist(String artist) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song s: allSongs) {
            if (s.getArtist().toLowerCase().equals(artist.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    //EFFECT return a list of song with the specified artist
    public ArrayList<Song> findSongByName(String name) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song s: allSongs) {
            if (s.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    //MODIFIES: this
    //EFFECT: remove all the songs in the list
    public void removeAllSongs() {
        allSongs.clear();
    }

    //EFFECT return a string representation of all the songs
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getSongs().size(); i++) {
            result.append("\n\t").append(i + 1).append(". ").append(getSongs().get(i).toString());
        }
        return result.toString();
    }

    //EFFECT: search through all songs and return a song that matches input path
    public Song getSongByPath(String path) {
        for (Song s: allSongs) {
            if (s.getFile().equals(new File(path))) {
                return s;
            }
        }
        return null;
    }


}
