package saiplayer.triode.com.saiplayer_ib.DataHolder;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Indranil on 04-Oct-17.
 */

public class ArtistSearch {
    ArrayList<ArtistsDataProvider> artistsDataProviders = new ArrayList<ArtistsDataProvider>();
    public ArrayList<ArtistsDataProvider> getArtistDetails(ContentResolver contentResolver){
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistCursor = contentResolver.query(uri, new String[]{MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS}, null, null, null);

        if (artistCursor != null && artistCursor.moveToFirst()) {
            int songArtist = artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int albumCount = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int trackCount = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            do {
                String artistName = artistCursor.getString(songArtist);
                String numberOfAlbums = artistCursor.getString(albumCount);
                String numberOfTracks = artistCursor.getString(trackCount);
                String artistDetails = "";
                int numAlbum = Integer.parseInt(numberOfAlbums);
                int numTrack = Integer.parseInt(numberOfTracks);
                if (numAlbum>1 && numAlbum>1){
                    artistDetails = numberOfAlbums+" Albums\n"+numberOfTracks+" Songs";
                }
                else if (numAlbum<1 && numTrack>1){
                    artistDetails = numberOfAlbums+" Album\n"+numberOfTracks+" Songs";
                }
                else if (numAlbum>1 && numTrack<1){
                    artistDetails = numberOfAlbums+" Albums\n"+numberOfTracks+" Song";
                }
                else {
                    artistDetails = numberOfAlbums+" Album\n"+numberOfTracks+" Song";
                }
                ArtistsDataProvider dataProvider = new ArtistsDataProvider(artistName,artistDetails);
                artistsDataProviders.add(dataProvider);
            } while (artistCursor.moveToNext());

            Collections.sort(artistsDataProviders, new Comparator<ArtistsDataProvider>() {

                @Override
                public int compare(ArtistsDataProvider artistsDataProvider, ArtistsDataProvider t1) {
                    return artistsDataProvider.getArtistName().compareTo(t1.getArtistName());
                }
            });
        }
        return artistsDataProviders;
    }
}
