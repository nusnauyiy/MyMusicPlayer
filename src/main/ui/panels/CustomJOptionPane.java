package ui.panels;

import model.MasterPlaylist;
import model.Playlist;
import model.Song;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

//Class that holds static methods all involving jOptionPanes
public class CustomJOptionPane {

    //EFFECT: creates a series of JOptionPanel that prompts the player an action
    //          regarding their selected songs
    public static Playlist addToPlaylistOptions(ArrayList<Song> songs) {
        if (MasterPlaylist.getInstance().getAllPlaylists().size() == 0) {
            return addNewPlaylist(songs, null, null);
        }
        //creates 2 options via radio buttons
        JRadioButton option1 = new JRadioButton("Add to existing playlist");
        JRadioButton option2 = new JRadioButton("Create a new playlist");

        //button group allows only one to be selected at a time
        ButtonGroup optionGroup = new ButtonGroup();

        optionGroup.add(option1);
        option1.setSelected(true);
        optionGroup.add(option2);

        //make a new panel that could be displayed within the JOption Panel
        JPanel myPanel = new JPanel();
        myPanel.add(option1);
        myPanel.add(option2);

        //depending on user's input either add new playlist or add to existing playlist
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Add to playlist", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (option1.isSelected()) {
                addToExistingPlaylist(songs);
            } else {
                return addNewPlaylist(songs, null, null);
            }
        }
        return null;
    }

    //MODIFIES: this.master
    //EFFECT: returns a new playlist is user chooses to construct one
    public static Playlist addNewPlaylist(ArrayList<Song> songs, String warning, String oldDescription) {

        //create a JPanel with 2 text fields of name and description of the new playlist
        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(oldDescription, 20);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
        myPanel.add(new JLabel(warning));
        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Description:"));
        myPanel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Create a new playlist", JOptionPane.OK_CANCEL_OPTION);
        return editSongInfoHelper(songs, nameField.getText(), descriptionField.getText(), result);
    }

    //MODIFIES: this
    //EFFECT: handles user interation with the JOptionPane
    private static Playlist editSongInfoHelper(ArrayList<Song> songs, String name, String description, int result) {
        final String EMPTY_NAME_WARNING = "Playlist name cannot be empty";
        final String PLAYLIST_EXISTS_WARNING = "Playlist with this name already exists";

        if (result == JOptionPane.OK_OPTION) {
            //relaunch method if input does not adhere to requirement
            if (name.equals("")) {
                return addNewPlaylist(songs, EMPTY_NAME_WARNING, description);
            } else if (extractPlaylistNames().contains(name)) {
                return addNewPlaylist(songs, PLAYLIST_EXISTS_WARNING, description);
            } else {
                successDialogue("Create a new playlist");
                return new Playlist(name, songs, description);
            }
        }
        return null;
    }

    //MODIFIES this.master
    //EFFECT: prompts user to add songs to an existing playlist
    public static void addToExistingPlaylist(ArrayList<Song> songs) {
        final String title = "Add to existing playlist";
        JComboBox<String> box = new JComboBox<>(new Vector<>(extractPlaylistNames()));
        JLabel label = new JLabel("Choose a playlist to add to:");

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
        myPanel.add(label);
        myPanel.add(box);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            //relaunch method if input does not adhere to requirement
            MasterPlaylist.getInstance().getAllPlaylists().get(box.getSelectedIndex()).addSong(songs);
            successDialogue(title);
        }
    }

    //EFFECT returns the name of all playlist as an arraylist
    private static ArrayList<String> extractPlaylistNames() {
        ArrayList<String> result = new ArrayList<>();
        for (Playlist p: MasterPlaylist.getInstance().getAllPlaylists()) {
            result.add(p.getName());
        }
        return result;
    }

    //MODIFIES: song
    //EFFECT: prompts the user to change the song's name and artist
    public static void editSongInformation(Song song, String warning) {
        final String EMPTY_NAME_WARNING = "Song name cannot be empty";

        JTextField nameField = new JTextField(song.getName(), 10);
        JTextField artistField = new JTextField(song.getArtist(),10);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
        if (warning != null) {
            myPanel.add(new JLabel(warning));
        }
        myPanel.add(new JLabel("Name: "));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Artist: "));
        myPanel.add(artistField);
        myPanel.add(new JLabel(song.getFile().getPath()));

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Song Information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            //relaunch method if input does not adhere to requirement
            if (nameField.getText().equals("")) {
                editSongInformation(song, EMPTY_NAME_WARNING);
            } else {
                song.setName(nameField.getText());
                song.setArtist(artistField.getText());
            }
        }
    }

    //MODIFIES: playlist
    //EFFECT: prompts the user to change the playlist's name and artist
    public static void editPlaylistInfo(Playlist playlist, String warning, String oldDescription) {

        JTextField nameField = new JTextField(playlist.getName(), 15);
        JTextField descriptionField = new JTextField(playlist.getDescription(), 15);
        if (oldDescription != null) {
            descriptionField = new JTextField(oldDescription, 15);
        }

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
        if (warning != null) {
            myPanel.add(new JLabel(warning));
        }
        myPanel.add(new JLabel("Name: "));
        myPanel.add(nameField);
        myPanel.add(new JLabel("Description: "));
        myPanel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Song Information", JOptionPane.OK_CANCEL_OPTION);

        editPlaylistInfoHelper(playlist, nameField.getText(), descriptionField.getText(), result);
    }

    //MODIFIES: playlist
    //EFFECT: handles the outcome of user interaction with the JOptionPane
    private static void editPlaylistInfoHelper(Playlist playlist, String nameField, String descriptionField, int res) {
        final String EMPTY_NAME_WARNING = "Song name cannot be empty";
        final String PLAYLIST_EXISTS_WARNING = "Playlist with this name already exists";

        if (res == JOptionPane.OK_OPTION) {
            //relaunch method if input does not adhere to requirement
            if (nameField.equals("")) {
                editPlaylistInfo(playlist, EMPTY_NAME_WARNING, descriptionField);
            } else if (extractPlaylistNames().contains(nameField) && !nameField.equals(playlist.getName())) {
                editPlaylistInfo(playlist, PLAYLIST_EXISTS_WARNING, descriptionField);
            } else {
                playlist.setName(nameField);
                playlist.setDescription(descriptionField);
            }
        }
    }

    //EFFECT: shows a "success" message
    private static void successDialogue(String title) {
        JOptionPane.showMessageDialog(null, "Success!", title, JOptionPane.INFORMATION_MESSAGE);
    }

}