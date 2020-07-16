package model;

import persistence.DataManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//This class is the initial collection that contains all songs in a directory, and regulate lists of playlists
public class MasterPlaylist extends SongCollection {

    private static MasterPlaylist theMaster;
    private PlaylistManager allPlaylists;
    private String directory;
    private DataManager dataManager;

    //EFFECT constructs a master playlist with all files from the directory, throws IO exception for invalid directory
    //https://mkyong.com/java/java-files-walk-examples/
    private MasterPlaylist(String directory) throws IOException {
        this.directory = directory;
        dataManager = new DataManager(directory);
        super.initializeSongs(new ArrayList<>());

        List<String> allFilesPaths;
        Stream<Path> walk = Files.walk(Paths.get(directory));

        allFilesPaths = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

        for (String currentPath : allFilesPaths) {
            if (currentPath.endsWith(".wav")) {
                super.addSong(new Song(currentPath.substring(currentPath.lastIndexOf("\\") + 1),
                        new File(currentPath), null));//!!! artist is null here so always "unknown"
            }
            if (currentPath.endsWith(directory + "\\saved.json")) {
                dataManager = new DataManager(currentPath);
            }
        }
    }

    //EFFECT: instantiate the singleton instance of master
    public static void initializeInstance(String directory) throws IOException {
        if (theMaster == null) {
            theMaster = new MasterPlaylist(directory);
            theMaster.loadSavedData();
        }
    }

    //REQUIRES: initializeInstance must have been called already and theMaster is not null
    //EFFECT: returns the singleton master
    public static MasterPlaylist getInstance() {
        return theMaster;

    }

    //MODIFIES: this
    //EFFECT: reads saved data from file
    private void loadSavedData() {
        try {
            Object[] savedData = dataManager.read();
            ArrayList<Song> editedSongs = (ArrayList<Song>) savedData[1];
            replaceEditedSongs(editedSongs);
            savedData = dataManager.read();
            allPlaylists = new PlaylistManager((ArrayList<Playlist>) savedData[0]);

        } catch (Exception e) {
            //e.printStackTrace();
            allPlaylists = new PlaylistManager();
        }
    }


    //EFFECT returns the directory of where the song come from
    public String getDirectory() {
        return directory;
    }

    //EFFECT: return the playlist manager
    public PlaylistManager getAllPlaylists() {
        return allPlaylists;
    }


    //REQUIRES: all numbers in indexes must be valid indexes from the master playlist
    //EFFECT create new playlist with indexes from master playlist
    public Playlist createPlaylistWithIndex(String name, ArrayList<Integer> indexes, String descriptions) {
        ArrayList<Song> result = new ArrayList<>();
        for (int i: indexes) {
            result.add(getSongs().get(i));
        }
        return allPlaylists.create(name, result, descriptions);
    }


    //EFFECT: creates a JSON file at directory that saves data of current playlists
    public void createJson() {
        try {
            dataManager.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //should be private, but made public for testing purpose
    //MODIFIES: this
    //EFFECT: replaces songs that has been edited with the edited version
    public void replaceEditedSongs(ArrayList<Song> savedData) {
        for (int i = 0; i < getSongs().size(); i++) {
            for (Song d: savedData) {
                if (getSongs().get(i).getFile().getPath().equals(d.getFile().getPath())) {
                    getSongs().set(i, d);
                }
            }
        }
    }

    @Override
    //EFFECT: return a string representation of the master playlist
    public String toString() {
        String stringBuilder = directory + "\n"
                + "your songs: \n"
                + super.toString()
                + "\nyour playlists: "
                + allPlaylists.playlistsToString();
        return stringBuilder;
    }

}
