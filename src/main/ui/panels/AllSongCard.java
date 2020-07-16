package ui.panels;

import model.*;
import ui.sound.WavPlayer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//JPanel class that display all songs and handles event when all song tab is selected
public class AllSongCard extends CardPanel {
    static AllSongCard allSongCard;
    JList<Song> list;

    JButton playAll;
    JButton playSelected;
    JButton addToPlaylist;

    //constructor for all song panel
    public AllSongCard() {
        super();
    }

    //MODIFIES: this
    //EFFECT: display the search bar and initialize its function
    @Override
    protected JPanel searchDisplay() {
        keywordField = new JTextField("search artist or song title");
        return super.searchDisplay();
    }

    //MODIFIES: this
    //EFFECT: add search functionality to the search button
    @Override
    protected void initSearchButton() {
        searchButton.addActionListener(e -> {
            Set<Song> result = new HashSet<>();
            result.addAll(MasterPlaylist.getInstance().findSongByName(keywordField.getText()));
            result.addAll(MasterPlaylist.getInstance().findSongByArtist(keywordField.getText()));
            DefaultListModel<Song> model = new DefaultListModel<>();

            if (result.size() == 0) {
                overview.setText("No result matches keyword");
            } else if (keywordField.getText().equals("")) {
                for (Song val: MasterPlaylist.getInstance().getSongs()) {
                    model.addElement(val);
                }
                overview.setText("You have " + MasterPlaylist.getInstance().getSongs().size() + " songs.");
            } else {
                for (Song val : result) {
                    model.addElement(val);
                }
                overview.setText("There are " + result.size() + " matching results");
            }
            list.setModel(model);
        });
    }

    //MODIFIES: this
    //EFFECT: make display of all components on panel except the bottom buttons
    @Override
    protected void makeDisplay() {
        overview = new JLabel("You have " + MasterPlaylist.getInstance().getSongs().size() + " songs.");
        list = new JList(MasterPlaylist.getInstance().getSongs().toArray());
        list.setSelectedIndex(0);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        super.makeDisplay();
        this.add(new JScrollPane(list));
        addJListDoubleClick();
    }

    //MODIFIES: this
    //EFFECT: make display of the button panel which is consisted
    @Override
    protected void makeButtonPanel() {
        playAll = new JButton("Play All");
        playSelected = new JButton("Play Selected");
        addToPlaylist = new JButton("Add To Playlist");

        super.makeButtonPanel();

        buttonPanel.add(playAll);
        buttonPanel.add(playSelected);
        buttonPanel.add(addToPlaylist);
    }

    //MODIFIES: this
    //EFFECT: add action listeners to all the buttons in the button panel
    @Override
    protected void initPanelButtons() {
        //playAll button plays all songs displayed
        playAll.addActionListener(e -> {
            // get all elements in the jlist and play songs
            for (int i = 0; i <= list.getModel().getSize(); i++) {
                list.setSelectedIndex(i);
                System.out.println(list.getSelectedValue());
                WavPlayer.play(list.getSelectedValue(), "");
            }
        });

        //playSelected button plays all songs that are highlighted by the user
        playSelected.addActionListener(e -> {
            // play selcted songs in the jlist
            for (int i: list.getSelectedIndices()) {
                WavPlayer.play(list.getModel().getElementAt(i), "");
            }
        });

        //addToPlaylist adds selected songs to a playlist
        addToPlaylist.addActionListener(e -> {
            //add all selected song to a new playlist
            ArrayList<Song> songs = new ArrayList<>();
            for (int i: list.getSelectedIndices()) {
                songs.add(list.getModel().getElementAt(i));
            }
            Playlist temp = CustomJOptionPane.addToPlaylistOptions(songs);
            if (temp != null) {
                MasterPlaylist.getInstance().getAllPlaylists().add(temp);
            }
        });
    }

    //MODIFIES: this
    //EFFECT: added double clickable behavior, edit name and fields, for items in the jlist
    private void addJListDoubleClick() {
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    CustomJOptionPane.editSongInformation(AllSongCard.this.list.getModel().getElementAt(index), null);
                }
            }
        });
    }


}

