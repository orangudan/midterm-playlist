public class App {
    public static void main(String[] args) throws Exception {
        Playlist<String> playlist = new Playlist<>();

        playlist.add("Song 1");
        playlist.add("Song 2");
        playlist.add("Song 3");
        playlist.add("Song 4");
        playlist.add("Song 5");

        System.out.println(playlist);
        playlist.play(0);
        playlist.playNext();
        playlist.loopLastPlayed();
        System.out.println(playlist.printQueue());

        playlist.removeSongs(0,2);

        System.out.println(playlist);
        playlist.add("New Song");
        System.out.println(playlist);

    }
}
