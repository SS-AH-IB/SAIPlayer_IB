package saiplayer.triode.com.saiplayer_ib.DataHolder;

/**
 * Created by dell on 05-Aug-17.
 */

public class SongsDataProvider {
    private String songTitle;
    private String songDetails;
    private String songPath;
    private String songAlbum;

    public SongsDataProvider(String songTitle,
                             String songDetails,
                             String songPath,
                             String songAlbum) {

        this.songTitle = songTitle;
        this.songDetails = songDetails;
        this.songPath = songPath;
        this.songAlbum = songAlbum;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDetails() {
        return songDetails;
    }

    public void setSongDetails(String songDetails) {
        this.songDetails = songDetails;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }
}
