import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

//test class for Playlist class
public class PlaylistTest {

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

    @BeforeEach
    void RunBefore() {
        file1 = new File(dir1);
        file2 = new File(dir2);

        song1 = new Song(sName1, file1, defaultArtist);
        song2 = new Song(sName2, file2, artist1);
        song3 = new Song(sName3, file2, artist2);

        playlist1 = new Playlist(pName1, new ArrayList<>(), description1);
        playlist2 = new Playlist(pName2, new ArrayList<>(), description2);
    }

    @Test
    void testConstructor() {
        assertNotNull(playlist1);
        assertNotNull(playlist2);
    }

    @Test
    void testNameGettersAndSetters() {
        assertEquals(pName1, playlist1.getName());
        assertEquals(pName2, playlist2.getName());
        playlist1.setName(pName2);
        playlist2.setName(pName1);
        assertEquals(pName1, playlist2.getName());
        assertEquals(pName2, playlist1.getName());

    }

    @Test
    void testDescriptionGetterAndSetter() {
        assertEquals(description1, playlist1.getDescription());
        assertEquals(description2, playlist2.getDescription());
        playlist1.setDescription(description2);
        playlist2.setDescription(description1);
        assertEquals(description1, playlist2.getDescription());
        assertEquals(description2, playlist1.getDescription());
    }

    @Test
    void testToString() {
        assertEquals(pName1 + ":  " + description1, playlist1.toString());

        playlist1.addSong(song1);
        assertEquals(pName1 + ":  " + description1, playlist1.toString());

        playlist1.addSong(song2);
        assertEquals(pName1 + ":  " + description1, playlist1.toString());
    }

    @Test
    void testAddAll() {
        ArrayList<Song> list3 = new ArrayList<>(Arrays.asList(song1, song2, song3));

        playlist1.addSong(list3);
        assertEquals(3, playlist1.getSongs().size());
        assertEquals(song1, playlist1.getSongs().get(0));
        assertEquals(song2, playlist1.getSongs().get(1));
        assertEquals(song3, playlist1.getSongs().get(2));

        playlist2.addAll(playlist1);
        assertEquals(3, playlist2.getSongs().size());
        assertEquals(song1, playlist2.getSongs().get(0));
        assertEquals(song2, playlist2.getSongs().get(1));
        assertEquals(song3, playlist2.getSongs().get(2));

        playlist2.addAll(playlist1);
        assertEquals(3, playlist2.getSongs().size());
        assertEquals(song1, playlist2.getSongs().get(0));
        assertEquals(song2, playlist2.getSongs().get(1));
        assertEquals(song3, playlist2.getSongs().get(2));
    }

}
