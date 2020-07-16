package ui;


import persistence.DataManager;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

//Main class that fetches music directory and run application
public class Main {
    static String title = "My Music Player";
    static String welcome = "Welcome to " + title + "!";
    static String directoryPrompt = "Please input the directory to your files";
    static String directoryNotFound = "Directory not found, please try again!";

    // EFFECT: prompt user to enter a valid directory and program in that directory
    public static void main(String[] args) {
        //JOptionPane.showMessageDialog(null, welcome, title, JOptionPane.PLAIN_MESSAGE);

        boolean running = false;
        // while the user interface is not launched, prompt user to enter valid directory
        while (!running) {
            String path = JOptionPane.showInputDialog(null, directoryPrompt, welcome, JOptionPane.QUESTION_MESSAGE);
            if (path == null) {
                return;
            } else if (path.equals("")) {
                continue;
            } else {
                try {
                    new AudioPlayerFrame(path);
                    running = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, directoryNotFound, title, JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}
