import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;

/**
 * A class that stores music in a playlist and generates queues based on settings for playing music.
 * 
 * @author dsadeghi
 * 
 */
public class Playlist<FileType> {
    private List<FileType> playlist;

    //The queue that determines which songs are played. The top of the queue represents the next song (currently playing songs are popped and returned to the caller)
    private Deque<FileType> playQueue;


    //Tracks the currently selected song in the playlist
    private int currentIndex;

    //When any songs are played (returned), this should be set to the playing song
    private FileType lastPlayedSong;

    //Settings for generating play queues
    private boolean shuffle;
    private boolean loop;

    public Playlist() {
        playlist = new ArrayList<>();
        playQueue = new ArrayDeque<>();
        shuffle = false;
        loop = false;
        lastPlayedSong = null;
    }

    /**
     * Returns next song in queue to the caller. If the queue is empty, generates a new queue
     * @return
     */
    public FileType playNext() {

        //If queue is empty and is set to loop, generate a new queue
        if (playQueue.size() == 0 && loop == true) {
            generateNewQueue(0);
        }
        
        lastPlayedSong = playQueue.poll();

        return lastPlayedSong;
    }


    /**
     * Returns the song that is currently selected and generates a new play queue
     * @return the song currently selected in playlist
     */
    public FileType playCurrentSelection() {
        generateNewQueue(currentIndex);
        lastPlayedSong = playQueue.poll();

        return lastPlayedSong;
    }

    /**
     * Returns the song at the given index and generates a new play queue
     * @param atIndex the index choosing which song to play from playlist
     * @return the song selected from the index
     */
    public FileType play(int atIndex) {
        generateNewQueue(atIndex);
        lastPlayedSong = playlist.get(atIndex);

        return lastPlayedSong;
    }

    /**
     * Sets the currently selected song to the given index.
     * @param atIndex the index to be selected
     */
    public void selectSong(int atIndex) {
        //Uses an iterator as an easy way to check for boundary errors
        ListIterator<FileType> iter = playlist.listIterator(atIndex);
        currentIndex = iter.nextIndex();
    }

    /**
     * Adds a song to the playlist.
     * @param song the song to be added
     */
    public void add(FileType song) {
        playlist.add(song);
    }


    /**
     * Adds a list of songs to the playlist
     * @param playlist the list of songs to be added
     */
    public void addSongs(List<FileType> playlist) {
        for (FileType song : playlist) {
            add(song);
        }
    }

    /**
     * Removes a song from the playlist at the given index
     * @param index the index of the song to be removed
     */
    public void remove(int index) {
        playlist.remove(index);
    }
    

    /**
     * Removes the song at the current index in the playlist
     */
    public void removeCurrentSelection() {
        playlist.remove(currentIndex);
    }

    /**
     * Removes a range of songs in the playlist from starting index to ending index (not inclusive)
     * @param startIndex the starting index from which to remove (inclusive)
     * @param endIndex the ending index where removal will stop (not inclusive)
     */
    public void removeSongs(int startIndex, int endIndex) {
        if (endIndex < 0 || endIndex > playlist.size()) {
            throw new IndexOutOfBoundsException();
        }

        ListIterator<FileType> iter = playlist.listIterator(startIndex);

        for (int i = startIndex; i < endIndex; i++) {
            iter.next();
            iter.remove();
        }
    }

    /**
     * The setting that changes the behavior of how new queues are generated when playing songs. Setting this to true will make newly generated queues to be randomized
     * @param bool
     */
    public void setShuffle(boolean bool) {
        shuffle = bool;
    }

    /**
     * Setting that signals playNext() to generate a new queue (looping the playlist) when the queue is empty
     * @param bool
     */
    public void setLoop(boolean bool) {
        loop = bool;
    }
    
    /**
     *  Adds the lastplayed song to the front of the queue
     */
    public void loopLastPlayed() {
        playQueue.offerFirst(lastPlayedSong);
    }

    /**
     * Adds the song at the current selection to the front of the queue
     */
    public void loopCurrentSelection() {
        playQueue.offerFirst(playlist.get(currentIndex));
    }

    /**
     * Prints the current queue
     * @return
     */
    public String printQueue() {
        Deque<FileType> tempQueue = new ArrayDeque<>();
        tempQueue.addAll(playQueue);
        String str = "PLAY QUEUE\n\n";
        
        for (int i = 0; i < playQueue.size(); i++) {
            str += i + ": " + tempQueue.poll() + "\n";
        }

        return str;
    }

    public String toString() {
        List<FileType> tempPlayList = new ArrayList<>();
        tempPlayList.addAll(playlist);
        String str = "PLAYLIST\n\n";

        ListIterator<FileType> iter = tempPlayList.listIterator();
        for (int i = 0; i < playlist.size(); i++) {
            str += i + ": " + iter.next() + "\n";
        }

        return str;
    }


    /**
     * Generates a new queue of songs based on shuffle settings.
     * <p>
     * Shuffle on: The queue is randomly generated. 
     * <p>
     * Shuffle off: The queue follows the order of the playlist starting from the currently selected song
     * 
     */
    private void generateNewQueue(int fromIndex) {

        Deque<FileType> newQueue = new ArrayDeque<>();

        //If the setting is set to shuffle, generates a randomized queue
        if (shuffle) {
            List<FileType> tempPlaylist = new ArrayList<>();
            tempPlaylist.addAll(playlist);

            ListIterator<FileType> iter;

            //Loops equal to the number of songs in the playlist
            for (int i = 0; i < playlist.size(); i++) {
                //set iterator to a random spot in the temporary playlist
                iter = tempPlaylist.listIterator((int)(Math.random() * tempPlaylist.size()));

                //Add the randomly selected song to the new queue
                newQueue.offer(iter.next());

                //Remove the song from the temporary playlist so it doesn't get selected again in subsequent loops
                iter.remove();
            }
        } 
        //If the setting is off, generates a queue from the passed index
        else {
            //Temporary iterator
            ListIterator<FileType> iter = playlist.listIterator(fromIndex);
            while (iter.hasNext()) {
                newQueue.offer(iter.next());
            }
        }
        
        playQueue = newQueue;
    }

}
