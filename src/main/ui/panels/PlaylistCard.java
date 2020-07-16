package ui.panels;

import model.*;
import ui.sound.WavPlayer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

//JPanel class that displays all playlists and handle methods when play list tab is selected
public class PlaylistCard extends CardPanel {
    JPanel listPane;
    JList<Playlist> list;
    JList<Song> songJList;

    JButton playSelected;
    JButton deleteSelected;
    JButton newPlaylist;

    //constructor for playlist panel
    public PlaylistCard() {
        super();
    }

    //MODIFIES: this
    //EFFECT: make display of all components on panel except the bottom buttons
    @Override
    protected void makeDisplay() {
        listPaneDisplay();
        super.makeDisplay();
        this.add(listPane);
    }

    //MODIFIES: this
    //EFFECT: set up list pane with songjlist and playlist jlist
    private void listPaneDisplay() {
        listPane = new JPanel();
        List<Playlist> playlists = MasterPlaylist.getInstance().getAllPlaylists();
        overview = new JLabel("You have " + playlists.size() + " playlists.");
        list = new JList(playlists.toArray());
        list.setSelectedIndex(0);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        playlistJListAddListener();

        songJList = new JList<>();
        songJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songJListAddListener();

        listPane.setLayout(new BoxLayout(listPane, BoxLayout.LINE_AXIS));
        listPane.add(new JScrollPane(list));
        listPane.add(new JScrollPane(songJList));
    }

    //MODIFIES: this
    //EFFECT: display the search bar and initialize its function
    @Override
    protected JPanel searchDisplay() {
        keywordField = new JTextField("search playlist name or description");
        return super.searchDisplay();
    }

    //MODIFIES: this
    //EFFECT: add search functionality to the search button
    @Override
    protected void initSearchButton() {
        searchButton.addActionListener(e -> {
            Set<Playlist> result = new HashSet<>();
            result.addAll(MasterPlaylist.getInstance().getAllPlaylists().findByKeywords(keywordField.getText()));
            result.addAll(MasterPlaylist.getInstance().getAllPlaylists().findByName(keywordField.getText()));
            updateSongJList(new ArrayList<>());

            if (result.size() == 0) {
                overview.setText("No result matches keyword");
                updatePlaylistJlist(new ArrayList<>());
            } else if (keywordField.getText().equals("")) {
                updatePlaylistJlist(MasterPlaylist.getInstance().getAllPlaylists());
                overview.setText("You have " + MasterPlaylist.getInstance().getAllPlaylists().size() + " playlists.");
            } else {
                updatePlaylistJlist(result);
                overview.setText("There are " + result.size() + " matching results");
            }
        });
    }

    //MODIFIES: this
    //EFFECT: make display of the button panel which is consisted
    @Override
    protected void makeButtonPanel() {

        playSelected = new JButton("Play Selected Playlist");
        deleteSelected = new JButton("Delete Selected Playlist");
        newPlaylist = new JButton("New");

        super.makeButtonPanel();

        buttonPanel.add(playSelected);
        buttonPanel.add(deleteSelected);
        buttonPanel.add(newPlaylist);
    }

    //MODIFIES: this
    //EFFECT: add action listeners to all the buttons in the button panel
    @Override
    protected void initPanelButtons() {
        //all songs in the selected playlist will be played
        playSelected.addActionListener(e -> {
            // play selected playlists in the jlist
            for (int i: list.getSelectedIndices()) {
                for (Song s: list.getModel().getElementAt(i).getSongs()) {
                    WavPlayer.play(s, "");
                }
            }
        });

        //all selected playlists will be deleted, songJlist will be cleared
        deleteSelected.addActionListener(e -> {
            // delete selected playlists in the jlist
            int[] indices = list.getSelectedIndices();
            Arrays.sort(indices);
            for (int i = indices.length - 1; i >= 0; i--) {
                MasterPlaylist.getInstance().getAllPlaylists().remove(indices[i]);
            }
            updateSongJList(new ArrayList<>());
            updatePlaylistJlist(MasterPlaylist.getInstance().getAllPlaylists());
            overview.setText("You have " + list.getModel().getSize() + " playlists");
        });

        //prompt user to create a new playlist
        newPlaylist.addActionListener(e -> {
            Playlist result = CustomJOptionPane.addNewPlaylist(new ArrayList<Song>(), null, null);
            MasterPlaylist.getInstance().getAllPlaylists().add(result);
            updatePlaylistJlist(MasterPlaylist.getInstance().getAllPlaylists());
            overview.setText("You have " + list.getModel().getSize() + " playlists");
        });
    }

/*
    private boolean contains(int i, int[] list) {
        for (int l: list) {
            if (i == l) {
                return true;
            }
        }
        return false;
    }*/

    //MODIFIES: this
    //EFFECT: update the display of the playlist j list
    private void updatePlaylistJlist(Collection<Playlist> playlist) {
        DefaultListModel<Playlist> model = new DefaultListModel<>();
        for (Playlist p: playlist) {
            model.addElement(p);
        }
        list.setModel(model);
    }

    //MODIFIES: this
    //EFFECT: update the display of the song j list
    private void updateSongJList(Collection<Song> songs) {
        DefaultListModel<Song> model = new DefaultListModel<>();
        for (Song p: songs) {
            model.addElement(p);
        }
        songJList.setModel(model);
    }

    //MODIFIES: this
    //EFFECT: add clickable behavior to songJList
    private void songJListAddListener() {
        songJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                try {
                    if (evt.getClickCount() == 2) {
                        // Double-click detected: allow editing of song information
                        CustomJOptionPane.editSongInformation(songJList.getModel().getElementAt(index), null);
                    } else if (evt.getClickCount() == 1) {
                        //single-click detected: play the song
                        WavPlayer.playOverlap(songJList.getModel().getElementAt(index), "");
                    }
                } catch (Exception e) {
                    System.out.println("unexpected click");
                }
            }
        });
    }

    //MODIFIES: this
    //EFFECT: add clickable behaviour to playlistJlist
    private void playlistJListAddListener() {
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList source = (JList)evt.getSource();
                int index = source.locationToIndex(evt.getPoint());
                try {
                    if (evt.getClickCount() == 2) {
                        // Double-click detected: allow editing of the song
                        CustomJOptionPane.editPlaylistInfo(list.getModel().getElementAt(index), null, null);
                        //updatePlaylistJlist(master.getAllPlaylists());
                    } else if (evt.getClickCount() == 1) {
                        //single-click detected: update song list on the right to display songs in the selected playlist
                        updateSongJList(PlaylistCard.this.list.getModel().getElementAt(index).getSongs());
                    }
                } catch (Exception e) {
                    System.out.println("unexpected click");
                }
            }
        });
    }

}
