import model.MasterPlaylist;
import model.Playlist;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.DataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//Class that writes and read saved data via JSON object and files
//REFERENCE: https://cliftonlabs.github.io/json-simple/
public class DataManagerTest {
    DataManager d1;
    DataManager d2;

    String filePath = "data/Assets/saved.json";
    String directoryPath = "data/Assets";

    final String sName1 = "song 1";
    final String sName2 = "song 2";
    final String defaultArtist = "unknown";
    final String artist1 = "Kawai";
    Song song1 = new Song(sName1, new File("data\\Assets\\UprightPianoSamples\\A3vH.wav"), defaultArtist);
    Song song2 = new Song(sName2, new File("data\\Assets\\UprightPianoSamples\\A4vH.wav"), artist1);

    @BeforeEach
    void runBefore() {
        try {
            MasterPlaylist.initializeInstance(directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        d1 = new DataManager(filePath);
        d2 = new DataManager(directoryPath);
    }

    @Test
    void testGetter() {
        assertEquals(d1.getPath(), filePath);
        assertEquals(d2.getPath(), directoryPath);
    }

    @Test
    void testReadWriteEmptyFile() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        try {
            d1.write();
        } catch (IOException e) {
            fail();
        }

        try {
            Object[] readList = d1.read();
            ArrayList <Playlist> readPlayList = ((ArrayList<Playlist>) readList[0]);
            assertEquals(readPlayList.size(), playlists.size());
            assertEquals(readPlayList.size(), 0);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void testReadWriteEmptyDirectory() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        try {
            d2.write();
            assertTrue(new File(d2.getPath() + "\\saved.json").isFile());
        } catch (IOException e) {
            fail();
        }

        try {
            Object[] readList = d2.read();
            fail();
        } catch (Exception e) {

        }

        try {
            Object[] readList = d1.read();
            ArrayList <Playlist> readPlayList = ((ArrayList<Playlist>) readList[0]);
            assertEquals(readPlayList.size(), playlists.size());
            assertEquals(readPlayList.size(), 0);

            ArrayList <Song> readSongList = ((ArrayList<Song>) readList[1]);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testReadWriteValid() {
        ArrayList<Playlist> playlists = MasterPlaylist.getInstance().getAllPlaylists();
        System.out.println(playlists.size());
        Playlist playlist1 = new Playlist("playlist 1", new ArrayList<>(), "notes played");
        playlist1.addSong(song1);
        playlist1.addSong(song2);

        Playlist playlist2 = new Playlist("playlist 2", new ArrayList<>(), "notes played");

        Playlist playlist3 = new Playlist("playlist 3", new ArrayList<>(), "not music");
        //test adding not song file
        playlist3.addSong(new Song("not music file", new File("data\\tobs.jpg"), "tobs"));

        playlists.add(playlist1);
        playlists.add(playlist2);
        playlists.add(playlist3);

        try {
            d1.write();
        } catch (IOException e) {
            fail();
        }

        try {
            Object[] readList = d1.read();
            ArrayList <Playlist> readPlayList = ((ArrayList<Playlist>) readList[0]);
            assertEquals(readPlayList.size(), playlists.size());
            //System.out.println(readPlayList);
            //assertEquals(readPlayList.size(), 3);

            for (int i = readPlayList.size() - 1; i < playlists.size() - 3; i++) {
                assertEquals(playlists.get(i).getName(), readPlayList.get(i).getName());
                assertEquals(playlists.get(i).getDescription(), readPlayList.get(i).getDescription());
                assertEquals(playlists.get(i).getSongs().size() , readPlayList.get(i).getSongs().size());
            }

            //non wav file ignored when read
            assertEquals(playlists.get(playlists.size() - 1).getSongs().size() - 1,
                    readPlayList.get(playlists.size() - 1).getSongs().size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testEditedSongs() {
        MasterPlaylist.getInstance().getSongs().get(0).setName(sName1);
        MasterPlaylist.getInstance().getSongs().get(2).setName(sName2);

        try {
            d1.write();
            Object[] readList = d1.read();
            ArrayList <Song> readSongList = ((ArrayList<Song>) readList[1]);
            assertEquals(readSongList.size(), 2);
            assertEquals(readSongList.get(0).getFile().getPath(), MasterPlaylist.getInstance().getSongs().get(0).getFile().getPath());
            assertEquals(readSongList.get(1).getFile().getPath(), MasterPlaylist.getInstance().getSongs().get(2).getFile().getPath());


        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testClear() {
        MasterPlaylist.getInstance().createPlaylistWithIndex(sName1, new ArrayList<>(), sName1);
        MasterPlaylist.getInstance().createJson();

        assertTrue((new File(directoryPath + "\\saved.json")).length() > 0);
        try {
            d2.clear();
        } catch (Exception e) {
            fail();
        }
        assertEquals((new File(directoryPath + "\\saved.json")).length(), 0);

        MasterPlaylist.getInstance().createPlaylistWithIndex(sName1, new ArrayList<>(), sName1);
        MasterPlaylist.getInstance().createJson();

        assertTrue((new File(directoryPath + "\\saved.json")).length() > 0);
        try {
            d1.clear();
        } catch (Exception e) {
            fail();
        }
        assertEquals((new File(directoryPath + "\\saved.json")).length(), 0);

    }


}
