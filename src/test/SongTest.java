import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

//test class for Song class
public class SongTest {
    File file1;
    Song song1;
    Song song2;
    Song song3;
    Song song4;
    final String name1 = "new song";
    final String name2 = "another new song";
    final String name3 = "old song";
    final String name4 = "another old song";
    final String artist1 = "Kawai";
    final String artist2 = "Kawai upright piano";
    final String dir = "data\\Assets\\UprightPianoSample\\A3vH.wav";
    final String defaultArtist = "unknown";
    final int framPosition1 = 10;
    final int framPosition2 = 10;

    @BeforeEach
    void RunBefore() {
        file1 = new File(dir);
        song1 = new Song(name1, file1, artist1);
        song2 = new Song(name2, file1, null);
        song3 = new Song(name1, file1, artist1, true);
        song4 = new Song(name2, file1, null, false);
    }

    @Test
    void testConstructor() {
        assertNotNull(song1);
        assertNotNull(song2);
        assertNotNull(song3);
        assertNotNull(song4);
    }

    @Test
    void testGetters() {
        assertEquals(name1, song1.getName());
        assertEquals(name2, song2.getName());
        assertEquals(name1, song3.getName());
        assertEquals(name2, song4.getName());

        assertEquals(artist1, song1.getArtist());
        assertEquals(defaultArtist, song2.getArtist());
        assertEquals(artist1, song3.getArtist());
        assertEquals(defaultArtist, song4.getArtist());

        assertEquals(file1, song1.getFile());
        assertEquals(file1, song2.getFile());

        assertFalse(song1.hasEdited());
        assertFalse(song2.hasEdited());
        assertTrue(song3.hasEdited());
        assertFalse(song4.hasEdited());
    }

    @Test
    void testSetters() {
        song1.setName(name1);
        assertFalse(song1.hasEdited());

        song1.setArtist(artist1);
        assertFalse(song1.hasEdited());

        song1.setName(name3);
        song2.setName(name4);
        assertEquals(name3, song1.getName());
        assertEquals(name4, song2.getName());

        assertTrue(song1.hasEdited());
        assertTrue(song2.hasEdited());

        song1.setArtist(defaultArtist);
        song2.setArtist(artist2);
        assertEquals(defaultArtist, song1.getArtist());
        assertEquals(artist2,song2.getArtist());
        assertTrue(song1.hasEdited());
        assertTrue(song2.hasEdited());

        song1.setLastFramePosition(framPosition1);
        song2.setLastFramePosition(framPosition2);
        assertEquals(framPosition1, song1.getLastFramePosition());
        assertEquals(framPosition2,song2.getLastFramePosition());
    }

    @Test
    void testToString(){
        assertEquals(name1 + " by " + artist1, song1.toString());
        assertEquals(name2 + " by " + defaultArtist, song2.toString());
    }


}
