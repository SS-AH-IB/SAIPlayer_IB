package saiplayer.triode.com.saiplayer_ib.DataHolder;

/**
 * Created by dell on 05-Aug-17.
 */

public class ArtistsDataProvider {
    private String artistName;
    private String artistDetails;

    public ArtistsDataProvider(String artistName, String artistDetails) {
        this.artistName = artistName;
        this.artistDetails = artistDetails;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistDetails() {
        return artistDetails;
    }

    public void setArtistDetails(String artistDetails) {
        this.artistDetails = artistDetails;
    }
}
