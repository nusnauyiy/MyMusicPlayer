import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// test class for class MasterPlaylist
public class MasterPlaylistTest {

    String pName1 = "our playlist 1";
    String pName2 = "our playlist 2";
    String pName = "playlist";
    String description = "notes played";
    String description1 = "amazing notes played on piano";
    String description2 = "lovely notes played on Kawai";

    final String dir1 = "data\\Assets\\UprightPianoSamples";
    final String dir2 = "data";
    final String dir3 = "src\\main\\model";
    final String dir4 = "data\\saved.json";
    ArrayList<Integer> indexes = new ArrayList<>(Arrays. asList(0,10,20));

    MasterPlaylist master;

    @Test
    void testConstructorWithException() {
        new Thread(() -> {
            try {
                MasterPlaylist.initializeInstance("???");
                fail();
            } catch (IOException e){
            }
        }).start();


    }

    @Test
    void testConstructorWithoutException() {
        new Thread(() -> {
            try {
                MasterPlaylist.initializeInstance(dir1);
                master = MasterPlaylist.getInstance();
            } catch (IOException e){
                fail();
            }

            assertNotNull(master);
            List<String> allFilesPaths = new ArrayList<>();
            try (Stream<Path> files = Files.walk(Paths.get(dir1))) {
                allFilesPaths = files.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            } catch (Exception e) {
                fail();
            }
            assertTrue(allFilesPaths.size() >= master.getSongs().size());

            for (String paths: allFilesPaths) {
                assertTrue(paths.startsWith(dir1));
            }
            for (Song s: master.getSongs()) {
                //assertTrue(s.getName().endsWith(".wav")); //not always true since name may change
                assertTrue(s.getFile().getPath().startsWith(dir1));
                //assertEquals(s.getName(), s.getFile().getPath().substring(s.getFile().getPath().lastIndexOf("\\") + 1));
                assertTrue(allFilesPaths.contains(s.getFile().getPath()));
                //assertEquals(s.getArtist(), "unknown"); // not always true since artist can change
            }
            File dataFile = new File(dir1+"\\saved.json");
            assertTrue(dataFile.exists());
            assertTrue(dataFile.isFile());
        }).start();
    }

    @Test
    void testGetter() {
        try {
            MasterPlaylist.initializeInstance(dir1);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        master = MasterPlaylist.getInstance();
        master.getAllPlaylists().clear();
        assertEquals(dir1, master.getDirectory());
        assertEquals(0, master.getAllPlaylists().size());

    }



    @Test
    void testCreatePlaylist() {
        //System.out.println(master.allSongsToString());
        try {
            MasterPlaylist.initializeInstance(dir1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        master = MasterPlaylist.getInstance();

        Playlist newPlaylist = master.createPlaylistWithIndex(pName1, new ArrayList<Integer>(), description1);
        assertEquals(0, newPlaylist.getSongs().size());
        assertEquals(master.getAllPlaylists().size() - 1, master.getAllPlaylists().indexOf(newPlaylist));

        assertEquals(1, master.getAllPlaylists().size());
        assertEquals(pName1, master.getAllPlaylists().get(0).getName());
        assertEquals(description1, master.getAllPlaylists().get(0).getDescription());

        assertEquals(0, master.createPlaylistWithIndex(pName2, new ArrayList<>(), description2).getSongs().size());
        assertEquals(2, master.getAllPlaylists().size());
        assertEquals(pName2, master.getAllPlaylists().get(1).getName());
        assertEquals(description2, master.getAllPlaylists().get(1).getDescription());

        master.createPlaylistWithIndex(pName1, indexes, description1);
        //check if each element is distinct in the newly created list
        Set<Song> testSet = new HashSet<>(master.getAllPlaylists().get(2).getSongs());
        assertEquals(master.getAllPlaylists().get(2).getSongs().size(), testSet.size());
        //System.out.println(master.getAllPlaylistsToString());
    }


/*
    @Test
    void testRemoveAllEmpty() {
        assertEquals(0, master.getAllPlaylists().size());
        master.removeEmptyPlaylist();
        assertEquals(0, master.getAllPlaylists().size());

        master.createPlaylist("1", "1");
        master.createPlaylist("2", new ArrayList<>(),"2");
        master.createPlaylist("3", new ArrayList<>(Arrays.asList(1,2,3)),"3");
        master.createPlaylist("4", "4");
        master.createPlaylist("5", new ArrayList<>(Arrays.asList(6,5,4)),"5");

        assertEquals(master.getSongs().get(1), master.getAllPlaylists().get(2).getSongs().get(0));
        assertEquals(master.getSongs().get(2), master.getAllPlaylists().get(2).getSongs().get(1));
        assertEquals(master.getSongs().get(3), master.getAllPlaylists().get(2).getSongs().get(2));

        assertEquals(master.getSongs().get(6), master.getAllPlaylists().get(4).getSongs().get(0));
        assertEquals(master.getSongs().get(5), master.getAllPlaylists().get(4).getSongs().get(1));
        assertEquals(master.getSongs().get(4), master.getAllPlaylists().get(4).getSongs().get(2));

        assertEquals(5, master.getAllPlaylists().size());
        master.removeEmptyPlaylist();
        assertEquals(2, master.getAllPlaylists().size());
        for(Playlist p: master.getAllPlaylists()) {
            assertFalse(p.getSongs().size() == 0);
        }

        master.removeEmptyPlaylist();
        assertEquals(2, master.getAllPlaylists().size());
        for(Playlist p: master.getAllPlaylists()) {
            assertFalse(p.getSongs().size() == 0);
        }
    }*/

    @Test
    void testCreateJSON1() {
        try {
            MasterPlaylist.initializeInstance(dir1);
            //(new File(dir1 + "\\saved.json")).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        master = MasterPlaylist.getInstance();
        master.getAllPlaylists().clear();

        master.createJson();
        assertTrue(new File(master.getDirectory() + "\\saved.json").exists());
        assertTrue(new File(master.getDirectory() + "\\saved.json").isFile());
    }

    @Test
     void writeFile() {
        JSONObject obj = new JSONObject();
        obj.put("path", "data\\Assets\\UprightPianoSamples\\A3vH.wav");
        obj.put("name", "new name");
        obj.put("artist", "new artist");
        obj.put("isEdited", true);

        JSONObject obj2 = new JSONObject();
        obj2.put("path", "data\\Assets\\UprightPianoSamples\\A5vH.wav");
        obj2.put("name", "new name2");
        obj2.put("artist", "new artist2");
        obj2.put("isEdited", true);


        JSONObject jsonObject = new JSONObject();
        JSONArray playlists = new JSONArray();
        jsonObject.put("playlists", playlists);

        JSONArray editedSongs = new JSONArray();
        editedSongs.add(obj);
        editedSongs.add(obj2);

        jsonObject.put("edited songs", editedSongs);

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(dir1 + "/saved.json", false);
            fileWriter.write(jsonObject.toJSONString());

            fileWriter.close();
        } catch (IOException e) {
            fail();
        }

        try {
            MasterPlaylist.initializeInstance(dir1);
            //(new File(dir1 + "\\saved.json")).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        master = MasterPlaylist.getInstance();
        assertEquals("new name", master.getSongs().get(0).getName());
        assertEquals("new artist", master.getSongs().get(0).getArtist());
        assertTrue(master.getSongs().get(0).hasEdited());

        assertEquals("new name2", master.getSongs().get(2).getName());
        assertEquals("new artist2", master.getSongs().get(2).getArtist());
        assertTrue(master.getSongs().get(2).hasEdited());
    }

    @Test
    void testReplaceEditedSong() {
        Song e1 = new Song("e1", new File("data\\Assets\\UprightPianoSamples\\A4vH.wav"), pName, true);
        Song e2 = new Song("e2", new File("data\\Assets\\UprightPianoSamples\\B2vH.wav"), pName1, true);

        ArrayList<Song> editedList = new ArrayList<>();
        editedList.add(e1);
        editedList.add(e2);

        try {
            MasterPlaylist.initializeInstance(dir1);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        MasterPlaylist.getInstance().replaceEditedSongs(editedList);
        assertEquals(e1, MasterPlaylist.getInstance().getSongByPath("data\\Assets\\UprightPianoSamples\\A4vH.wav"));
        assertEquals(e2, MasterPlaylist.getInstance().getSongByPath("data\\Assets\\UprightPianoSamples\\B2vH.wav"));

    }

    @Test
    void testToString() {
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

}
