
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ui.panels;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;

//Class that controls the change of tabs in the GUI
public class Tabs {
    static final String ALL_SONG_PANEL = "All songs";
    static final String PLAYLIST_PANEL = "All playlists";
    static final int extraWindowWidth = 100;
    static AllSongCard allSongCard;
    static PlaylistCard playlistCard;

    //MODIFIES: pane
    //EFFECT: add tab components to the frame
    public static void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        allSongCard = new AllSongCard();
        playlistCard = new PlaylistCard();

        //Create the "cards".
        tabbedPane.addTab(ALL_SONG_PANEL, allSongCard);
        tabbedPane.addTab(PLAYLIST_PANEL, playlistCard);

        ChangeListener changeListener = changeEvent -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            //System.out.println("Tab: " + sourceTabbedPane.getTitleAt(index));
            tabbedPane.setComponentAt(0, allSongCard);
            tabbedPane.setComponentAt(1, playlistCard);
        };

        tabbedPane.addChangeListener(changeListener);

        pane.add(tabbedPane, BorderLayout.CENTER);
    }
}
