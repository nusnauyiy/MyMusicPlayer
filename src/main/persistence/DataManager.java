package persistence;

import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

//Class that reads and write JSON file to save playlist data
public class DataManager {
    private String path;

    //REQUIRES: file path exists
    //EFFECT: constructs a dataManager object
    public DataManager(String path) {
        this.path = path;
    }

    //EFFECT returns the path
    public String getPath() {
        return path;
    }

/*
    public ArrayList<Playlist> read() throws IOException, ParseException {
        ArrayList<Playlist> result = new ArrayList<>();

        JSONParser parser = new JSONParser();
        FileReader reader;
        JSONObject jsonObject;

        reader = new FileReader(path);
        jsonObject = (JSONObject) parser.parse(reader);
        JSONArray playlists = (JSONArray)jsonObject.get("playlists");

        for (Object p: playlists) {
            result.add(jsonToPlaylist((JSONObject)p));
        }

        return result;
    }*/

    //EFFECt retrieve playlist from path, throws exception if data cannot be read
    public Object[] read() throws IOException, ParseException {
        Object[] result = new Object[2];

        JSONParser parser = new JSONParser();
        FileReader reader;
        JSONObject jsonObject;

        reader = new FileReader(path);
        jsonObject = (JSONObject) parser.parse(reader);
        JSONArray jsonPlaylists = (JSONArray)jsonObject.get("playlists");

        ArrayList<Playlist> playlists = new ArrayList<>();
        for (Object p: jsonPlaylists) {
            playlists.add(jsonToPlaylist((JSONObject)p));
        }

        JSONArray jsonSongs = (JSONArray)jsonObject.get("edited songs");
        ArrayList<Song> songs = new ArrayList<>();
        for (Object s: jsonSongs) {
            songs.add(jsonToSong((JSONObject)s));
        }

        result[0] = playlists;
        result[1] = songs;
        //System.out.println(result);
        return  result;
    }

/*
    //EFFECT: rewrite or create JSON file in path that contains all playlist information
    public void write(ArrayList<Playlist> master) throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray playlists = new JSONArray();
        for (Playlist p: master) {
            playlists.add(playlistToJson(p));
        }
        jsonObject.put("playlists", playlists);

        FileWriter fileWriter;
        if (new File(path).isFile()) {
            fileWriter = new FileWriter(path, false);
            fileWriter.write(jsonObject.toJSONString());
        } else {
            fileWriter = new FileWriter(path + "/saved.json", false);
            fileWriter.write(jsonObject.toJSONString());
        }
        fileWriter.close();
    }
*/

    //EFFECT: rewrite or create JSON file at the directory specified that contains playlists and edited songs
    public void write() throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray playlists = new JSONArray();
        for (Playlist p: MasterPlaylist.getInstance().getAllPlaylists()) {
            playlists.add(playlistToJson(p));
        }
        jsonObject.put("playlists", playlists);

        JSONArray editedSongs = new JSONArray();
        for (Song s: MasterPlaylist.getInstance().getSongs()) {
            if (s.hasEdited()) {
                editedSongs.add(songToJson(s));
            }
        }
        jsonObject.put("edited songs", editedSongs);

        FileWriter fileWriter;
        if (new File(path).isFile()) {
            fileWriter = new FileWriter(path, false);
            fileWriter.write(jsonObject.toJSONString());
        } else {
            fileWriter = new FileWriter(path + "/saved.json", false);
            fileWriter.write(jsonObject.toJSONString());
        }
        fileWriter.close();
    }

    //EFFECT: turns a song object into a JSON representation
    private JSONObject songToJson(Song song) {
        JSONObject obj = new JSONObject();
        obj.put("path", song.getFile().getPath());
        obj.put("name", song.getName());
        obj.put("artist", song.getArtist());
        obj.put("isEdited", song.hasEdited());
        return obj;
    }

    //EFFECT:create blank new JSON file at the directory
    public void clear() throws IOException {
        FileWriter fileWriter;
        if (new File(path).isFile()) {
            fileWriter = new FileWriter(path, false);
        } else {
            fileWriter = new FileWriter(path + "/saved.json", false);
        }
        fileWriter.close();
    }

    //EFFECT: turns JSON representation of a song into Song object, return null if path is not a file
    private Song jsonToSong(JSONObject obj) {
        Song result = null;
        File file = new File((String)obj.get("path"));
        if (file.exists() && file.getPath().endsWith(".wav")) {
            result = new Song((String)obj.get("name"), file, (String) obj.get("artist"), (boolean)obj.get("isEdited"));
        }
        return result;
    }

    //EFFECT turns Playlist into its JSON representation
    private JSONObject playlistToJson(Playlist playlist) {
        JSONObject obj = new JSONObject();
        obj.put("name", playlist.getName());
        obj.put("description", playlist.getDescription());

        JSONArray songsPaths = new JSONArray();
        for (Song s: playlist.getSongs()) {
            songsPaths.add(s.getFile().getPath());
        }
        obj.put("songs", songsPaths);

        return obj;
    }

    //EFFECT: turns JSON representation of playlists into Playlist objects
    private Playlist jsonToPlaylist(JSONObject obj) {
        Playlist playlist = new Playlist((String)obj.get("name"), new ArrayList<>(), (String)obj.get("description"));
        JSONArray songs = (JSONArray)obj.get("songs");
        for (Object s: songs) {
            Song target = MasterPlaylist.getInstance().getSongByPath((String) s);
            if (target != null) {
                playlist.addSong(target);
            }
        }
        return playlist;
    }

}
