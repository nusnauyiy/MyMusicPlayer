import model.Playlist;
import model.PlaylistManager;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class PlaylistManagerTest {

    Song song1;
    Song song2;
    Song song3;
    final String sName1 = "song 1";
    final String sName2 = "song 2";
    final String sName3 = "song 3";
    final String defaultArtist = "unknown";
    final String artist1 = "Kawai";
    final String artist2 = "Kawai upright piano";
    File file1;
    File file2;
    final String dir1 = "data\\Assets\\UprightPianoSamples\\A3vH.wav";
    final String dir2 = "data\\Assets\\UprightPianoSamples\\A4vH.wav";
    Playlist playlist1;
    Playlist playlist2;
    String pName1 = "playlist 1";
    String pName2 = "playlist 2";
    String description1 = "amazing notes played on piano";
    String description2 = "lovely notes played on Kawai";

    PlaylistManager manager1;
    PlaylistManager manager2;

    @BeforeEach
    void runBefore() {
        file1 = new File(dir1);
        file2 = new File(dir2);

        song1 = new Song(sName1, file1, defaultArtist);
        song2 = new Song(sName2, file2, artist1);
        song3 = new Song(sName3, file2, artist2);

        playlist1 = new Playlist(pName1, new ArrayList<>(), description1);
        playlist2 = new Playlist(pName2, new ArrayList<>(), description2);
        playlist2.addSong(song1);
        playlist2.addSong(song2);
        playlist2.addSong(song3);

        manager1 = new PlaylistManager();
        manager2 = new PlaylistManager(new ArrayList<Playlist>() {{
            add(playlist1);
            add(playlist2);
        }});
    }

    @Test
    void testConstructor() {
        assertNotNull(manager1);
        assertNotNull(manager2);
        assertEquals(manager1.size(), 0);
        assertEquals(manager2.size(), 2);
        assertEquals(manager2.get(0), playlist1);
        assertEquals(manager2.get(1), playlist2);
    }


    @Test
    void testSearchMethods() {
        assertEquals(0, manager1.findByName(pName1).size());
        assertEquals(0, manager1.findByKeywords(description1).size());


        assertEquals(1, manager2.findByName(pName1).size());
        assertEquals(1, manager2.findByName(pName2).size());
        assertEquals(0, manager2.findByName(description1).size());
        assertEquals(1, manager2.findByKeywords(description1).size());
        assertEquals(1, manager2.findByKeywords(description1.substring(5)).size());
        assertEquals(1, manager2.findByKeywords(description2).size());
        assertEquals(1, manager2.findByKeywords(description2.substring(5)).size());
        assertEquals(2, manager2.findByKeywords("notes").size());
        assertEquals(0, manager2.findByKeywords(pName1).size());

    }

    @Test
    void testPlaylistsToString() {
       assertEquals(manager1.playlistsToString(), "");

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < manager2.size(); i++) {
            result.append(i + 1).append(". ").append(manager2.get(i).getName()).append("\n");
        }

       assertEquals(manager2.playlistsToString(), result.toString());
    }

    @Test
    void testCreatePlaylist() {
        manager1.create(pName1, description1);
        assertEquals(manager1.size(), 1);
        assertEquals(manager1.get(0).getName(), pName1);
        assertEquals(manager1.get(0). getDescription(), description1);
        assertEquals(manager1.get(0).getSongs().size(), 0);

        manager1.create(pName2, playlist2.getSongs(), description2);
        assertEquals(manager1.size(), 2);
        assertEquals(manager1.get(0).getName(), pName1);
        assertEquals(manager1.get(0). getDescription(), description1);
        assertEquals(manager1.get(0).getSongs().size(), 0);
        assertEquals(manager1.get(1).getName(), pName2);
        assertEquals(manager1.get(1). getDescription(), description2);
        assertEquals(manager1.get(1).getSongs().size(), 3);
        assertEquals(manager1.get(1).getSongs().get(0), song1);
        assertEquals(manager1.get(1).getSongs().get(1), song2);
        assertEquals(manager1.get(1).getSongs().get(2), song3);
    }

}
