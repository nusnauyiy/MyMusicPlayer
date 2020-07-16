# My Music Player

## A custom music player that allows convenient playlist creation and multiple play modes

The proposed application is a custom music player that allows convenient playlist creation and multiple play modes.

**the songs**

The songs are audio files stored on the user's devices. When imported, the user can edit information of the song.

**the playlists**

There is a master playlist that includes all the songs imported by the user. Each playlist may contain sub-playlists.
A playlist may also be sorted by artists or song titles.


**the play modes**

Currently the following potential play modes and functionalities have been lined up:

- regular play
- shuffle play
- backward play
- skip songs
- loop songs/playlists

**connections and users**

The project is of interest to me because I have come across many music players, but they don't seem to suffice my need to organize local music,
especially when they're flooded with other recommendations from the cloud.

Anyone who wishes to arrange their local music in an orderly manner and play them in various modes should find this application handy.

## User Stories
- As a user, I want to be able to specify a path/directory and import all the audio files from it.
- As a user, I want to be able to view and play all of my songs.
- As a user, I want to be able to create a new playlist and specify a title and a description.
- As a user, I want to be able to view all the playlists created and all the songs in them.
- As a user, I want tp be able to edit the name and description of the playlist
- As a user, I want to be able to remove a playlist and all the songs in it.
- As a user, I want to be able to add songs to a playlist.
- As a user, I want to be able to play a song.
- As a user, I want to be able to play a playlist.
- As a user, I want to be able to search songs and playlists.
- As a user, I want to be able to save the playlists I created.
- As a user, I want to be able to save the changes I made to the songs and playlists.
- As a user, I want to be able to reload my saved playlists.

**Instructions for Grader Phase 3**

set up
- When you start the program from Main.main(), the program always loads from a directory. 
You can input "data" or any of its sub directories (separated by "\\") to get audio files
- You may or may not get a prompt to discard data depending on the directory input
- You can select listed songs and playlists by clicking, and select multiple songs by using *shift + click* or *ctrl + click*

events
- You can generate the first required event by clicking [Add to Playlist], where you will be prompted to add
selected songs to a playlist. 
(You can view playlists by switching to the tab [All Playlist] at the top; as well, you can view the songs added to it
by clicking on the playlist)
- You can generate the second required event by *double clicking* any elements in the JList. 
(You will be prompted to change the information of the playlist or song)
- You can also enter keywords to search for song and playlist. Search results will be displayed once [Search] is clicked.


audio component
- You can trigger it by clicking the [play all] or [play selected] button located at [All Songs] tab or
the [play selected playlist] button in the [All Playlists] tab
(Note the program will freeze until all songs finished playing as I have yet to figure out how to pause them...)
- You can trigger it also by clicking on songs displayed after clicking on a playlist. 
(Note that these songs can be played and paused upon clicking another one of such song)

data persistence
- Data is saved as a json file in the input directory 
(e. g. if you saved playlists in "data/Assets", you can retrieve the playlists only when you load songs from "data/Assets" again)
- You will only be prompted to discard changes if there is data saved at the directory you have input.
If you choose "no", saved state of the application will be reloaded from last usage.
- You can save the state of my application by closing the window (as opposed to stopping it in intelliJ).
You will be prompted whether you want to save the changes or not.

**Phase 4: Task 2**

The model class has a type hierarchy that consists of SongCollection (abstract class), MasterPlaylist (extends SongCollection), and Playlist (extends SongCollection).
Both subclasses overrides the toString() method in the super class. 
Another type hierarchy in ui.panels consists of CardPanel (abstract class), AllSongCard and PlaylistCard (subclasses that both override multiple methods) was introduced during Task 3 below.

(Additionally, MasterPlaylist class is robust. The constructor of MasterPlaylist reads in the data from a specified directory, and thus
throws IOException when th directory is invalid or other errors are encountered during reading of the files.
The test class (MasterPlaylistTest) checks the exception throwing in testConstructorWithException(), and checks the normal operation in testConstructorWithoutException().)


**Phase 4: Task 3**

- There are a lot of coupling between AllSongPanel and PlaylistPanel (in ui.panels) since they're designed very similarly
  - An abstract class (CardPanel) is created to capture the shared structures of the two classes
- MasterPlaylist (in model) had poor cohesion. It has two distinct groups of methods: one relates to reading files from directory and the other managing the songs
  - New class PlaylistManager is created to handle most methods manipulating the playlists and MasterPlaylist handles loading, saving, and managing all songs