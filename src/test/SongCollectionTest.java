import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

//test class for SongCollection abstract class
public class SongCollectionTest {
    MasterPlaylist master;
    final String defaultArtist = "unknown";
    String artist = "artist";
    final String dir = "data\\Assets\\UprightPianoSamples";
    Song s1 = new Song("song 1", null, defaultArtist);
    Song s2 = new Song("song 2", null, artist);
    Playlist playlist;


    @BeforeEach
    void runBefore() {
        try {
            MasterPlaylist.initializeInstance(dir);
            master = MasterPlaylist.getInstance();
        } catch (IOException e) {

        }
        playlist = new Playlist("", new ArrayList<>(), "");
    }

    @Test
    void testGetterAndSetter() {
        long count = 0;
        try (Stream<Path> files = Files.list(Paths.get(dir))) {
            count = files.count();
        } catch (Exception e) {
            fail("should not have thrown exception");
        }
        //delta for possible json file
        assertEquals(count, master.getSongs().size(), 1);

        master.initializeSongs(new ArrayList<>());
        assertEquals(0, master.getSongs().size());
    }

    @Test
    void testAddSongs() {
        int initialSize = master.getSongs().size();
        master.addSong(s1);
        assertEquals(initialSize + 1, master.getSongs().size());
        master.addSong(s1);
        assertEquals(initialSize + 2, master.getSongs().size());
    }
/*
    @Test
    void testMoveSong() {
        Song s0 = master.getSongs().get(0);
        Song s3 = master.getSongs().get(3);
        Song s4 = master.getSongs().get(4);
        Song sm = master.getSongs().get(master.getSongs().size() - 1);

        assertFalse(master.moveSong(0, SongCollection.UP));
        assertFalse(master.moveSong(0, 0));
        assertFalse(master.moveSong(master.getSongs().size() - 1, SongCollection.DOWN));
        assertFalse(master.moveSong(master.getSongs().size(), SongCollection.UP));
        assertFalse(master.moveSong(-1, SongCollection.DOWN));
        assertFalse(master.moveSong(master.getSongs().size(), SongCollection.UP));

        assertTrue(master.moveSong(3, SongCollection.DOWN));
        assertEquals(s3, master.getSongs().get(4));
        assertEquals(s4, master.getSongs().get(3));
        assertTrue(master.moveSong(4, SongCollection.UP));
        assertEquals(s3, master.getSongs().get(3));
        assertEquals(s4, master.getSongs().get(4));

        assertTrue(master.moveSong(0, SongCollection.DOWN));
        assertEquals(s0, master.getSongs().get(1));
        assertTrue(master.moveSong(master.getSongs().size() - 1, SongCollection.UP));
        assertEquals(sm, master.getSongs().get(master.getSongs().size() - 2));
    }

    @Test
    void testRemoveSong() {

        assertFalse(master.removeSong(-1));
        assertFalse(master.removeSong(master.getSongs().size()));

        s1 = master.getSongs().get(1);
        s2 = master.getSongs().get(2);
        int initialSize = master.getSongs().size();

        assertTrue(master.removeSong(0));
        assertEquals(s1, master.getSongs().get(0));
        assertEquals(initialSize, master.getSongs().size() + 1);

        assertTrue(master.removeSong(0));
        assertEquals(s2, master.getSongs().get(0));
        assertEquals(initialSize, master.getSongs().size() + 2);
    }
*/
    @Test
    void testFindSong() {
        //test find song by name and find song by artist
        playlist.addSong(s1);
        assertEquals(1 , playlist.findSongByArtist(defaultArtist).size());
        assertTrue(playlist.findSongByArtist(defaultArtist).contains(s1));
        assertEquals(0, playlist.findSongByArtist(artist).size());

        assertEquals(1, playlist.findSongByName(s1.getName()).size());
        assertTrue(playlist.findSongByName(s1.getName()).contains(s1));
        assertEquals(0, playlist.findSongByName("song 2").size());

        playlist.addSong(s2);
        s1.setArtist(artist);
        assertEquals(0 , playlist.findSongByArtist(defaultArtist).size());
        assertTrue(playlist.findSongByArtist(artist).contains(s1));
        assertTrue(playlist.findSongByArtist(artist).contains(s2));
        assertEquals(2, playlist.findSongByArtist(artist).size());

        assertEquals(2, playlist.findSongByName("song").size());
        assertTrue(playlist.findSongByName("song").contains(s1));
        assertTrue(playlist.findSongByName("song").contains(s1));
        assertEquals(2, playlist.findSongByName("").size());
        assertTrue(playlist.findSongByName("song").contains(s1));
        assertTrue(playlist.findSongByName("song").contains(s1));
        assertEquals(0, playlist.findSongByName("0").size());
    }

    @Test
    void testRemoveAll() {
        int initialSize = master.getSongs().size();
        master.removeAllSongs();
        assertTrue(initialSize > master.getSongs().size());
        assertEquals(0, master.getSongs().size());
    }

    @Test
    void testAddAllList() {
        ArrayList<Song> list0 = new ArrayList<>();
        ArrayList<Song> list2 = new ArrayList<>(Arrays.asList(s1, s2));

        playlist.addSong(list0);
        assertEquals(0, playlist.getSongs().size());

        playlist.addSong(list2);
        assertEquals(2, playlist.getSongs().size());
        assertEquals(s1, playlist.getSongs().get(0));
        assertEquals(s2, playlist.getSongs().get(1));

        playlist.addSong(list2);
        assertEquals(4, playlist.getSongs().size());
        assertEquals(s1, playlist.getSongs().get(0));
        assertEquals(s2, playlist.getSongs().get(1));
        assertEquals(s1, playlist.getSongs().get(2));
        assertEquals(s2, playlist.getSongs().get(3));
    }

    @Test
    void testAllSongToString() {
        try {
            MasterPlaylist.initializeInstance("data");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        master = MasterPlaylist.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < master.getSongs().size(); i++) {
            result.append("\n\t").append(i + 1).append(". ").append(master.getSongs().get(i).toString());
        }
        stringBuilder.append(master.getDirectory() + "\n"
                + "your songs: \n"
                + result.toString()
                + "\nyour playlists: "
                + master.getAllPlaylists().playlistsToString());

        assertEquals(stringBuilder.toString(), master.toString());

    }

    @Test
    void testGetSongByPath() {
        try {
            MasterPlaylist.initializeInstance(dir);
            master = MasterPlaylist.getInstance();
        } catch (IOException e) {

        }
        Song found = master.getSongByPath(dir + "\\C7vH.wav");
        assertNotNull(found);
        assertEquals(found.getFile(), new File(dir + "\\C7vH.wav"));

        found = master.getSongByPath(dir + "\\saved.json");
        assertNull(found);
    }

}
