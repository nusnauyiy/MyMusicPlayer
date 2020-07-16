package ui.panels;

import javax.swing.*;
import java.awt.*;

//a class that documents the commonalities between AllSongCard and PlaylistCard
abstract class CardPanel extends JPanel {
    JPanel searchPane;
    JTextField keywordField;
    JButton searchButton;

    JLabel overview;
    Component spacer = Box.createRigidArea(new Dimension(0, 10));

    JPanel buttonPanel;

    //constructor for super class
    protected CardPanel() {
        super();
        makeDisplay();
        makeButtonPanel();
    }

    //MODIFIES: this
    //EFFECT: initialize button panel and add it to main panel display
    protected void makeButtonPanel() {
        buttonPanel = new JPanel();
        this.add(buttonPanel);
        initPanelButtons();
    }

    //MODIFIES: this
    //EFFECT: add components and layout to the main display
    protected void makeDisplay() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(searchDisplay());
        this.add(overview);
        this.add(spacer);
    }

    //MODIFIES: this
    //EFFECT: initialize and add components to the search panel
    protected JPanel searchDisplay() {
        searchPane = new JPanel();
        keywordField.setPreferredSize(new Dimension(200, 20));
        searchButton = new JButton("Search");
        initSearchButton();
        searchPane.add(keywordField);
        searchPane.add(searchButton);
        return searchPane;
    }

    //MODIFIES: this
    //EFFECT: add listener to the search button
    protected abstract void initSearchButton();

    //MODIFIES: this
    //EFFECT: add listeners to all buttons to be put in to the button panel
    protected abstract void initPanelButtons();
}
