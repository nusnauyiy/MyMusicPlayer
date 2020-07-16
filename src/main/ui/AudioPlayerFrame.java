package ui;

import model.MasterPlaylist;
import persistence.DataManager;
import ui.panels.Tabs;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.Year;

//JFrame class that holds all the GUI of application in the window
public class AudioPlayerFrame extends JFrame {
    final String quitMessage = "Would you like to save your changes?";
    final String promptStartNew = "You may have data saved previously in this directory, "
            + "would you like to discard the data? ";

    //constructor for audio player frame
    public AudioPlayerFrame(String path) {
        //set up window for the gui
        super("My Music Player");
        setSize(425, 300);
        setTitle(Main.title);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DataManager dm = new DataManager(path);
        try {
            if ((new File(path + "\\saved.json")).length() > 0 && startNew()) {
                dm.clear();
            }
            MasterPlaylist.initializeInstance(path);
            //MasterPlaylist.getInstance().createPlaylist("Favourites", "Your favourite songs go here");
        } catch (Exception e) {
            //e.printStackTrace();
        }

        //add tabs to the gui
        Tabs.addComponentToPane(this);
        setVisible(true);

        saveDataOnClose(MasterPlaylist.getInstance());
    }

    //EFFECT: save all edited data in master upon closing
    private void saveDataOnClose(MasterPlaylist masterPlaylist) {
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null, quitMessage, Main.title,
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    masterPlaylist.createJson();
                }
            }
        });
    }

    //EFFECT: return true if user chooses to not reload previously saved data, false otherwise
    private boolean startNew() {
        int result = JOptionPane.showConfirmDialog(null, promptStartNew, Main.title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

}
