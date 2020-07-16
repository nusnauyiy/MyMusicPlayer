package model;

import java.util.ArrayList;
import java.util.List;

//This class is an extended arraylist that holds all the playlists
public class PlaylistManager extends ArrayList<Playlist> {

    //constructor for playlist manager that creates an empty list
    public PlaylistManager() {
        super();
    }

    //constructor for playlist manager with existing list
    public PlaylistManager(ArrayList<Playlist> playlists) {
        super();
        super.addAll(playlists);
    }

    //EFFECT create empty new playlist
    public Playlist create(String name, String descriptions) {
        Playlist playlist = new Playlist(name, new ArrayList<>(), descriptions);
        super.add(playlist);
        return playlist;
    }

    //EFFECT create empty new playlist
    public Playlist create(String name, ArrayList<Song> songs, String descriptions) {
        Playlist playlist = new Playlist(name, songs, descriptions);
        super.add(playlist);
        return playlist;
    }

    //EFFECT: return a list of playlists that contains the name
    public List<Playlist> findByName(String name) {
        List<Playlist> result = new ArrayList<>();

        for (Playlist p: this) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    //EFFECT: return a list of playlist that contains the keyword in description
    public List<Playlist> findByKeywords(String keywords) {
        List<Playlist> result = new ArrayList<>();
        for (Playlist p: this) {
            if (p.getDescription().toLowerCase().contains(keywords.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }
/*
    //MODIFIES: this
    //EFFECT: remove all empty playlists
    public void removeEmptyPlaylist() {
        for (int i = 0; i < getAllPlaylists().size(); i++) {
            if (getAllPlaylists().get(i).getSongs().size() == 0) {
                getAllPlaylists().remove(i);
                i--;
            }
        }
    }*/


    //EFFECT: return a string representation of all the playlists
    public String playlistsToString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append(i + 1).append(". ").append(get(i).getName()).append("\n");
        }
        return result.toString();
    }
}
