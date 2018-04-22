package saiplayer.triode.com.saiplayer_ib.DataHolder;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dell on 07-Aug-17.
 */
public class SongSearch {
    ArrayList<SongsDataProvider> songsDataProviders = new ArrayList<SongsDataProvider>();
    public ArrayList<SongsDataProvider> getMusic(ContentResolver contentResolver) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, new String[]{MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_KEY,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA}, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
            int songYear = songCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songPosition = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentSongPath = songCursor.getString(songPosition);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentArtist = songCursor.getString(songArtist);
                String currentYear = songCursor.getString(songYear);
                String currentDuration = null;
                double totalDuration=0.0;
                if(songCursor.getString(songDuration)!=null) {
                    totalDuration = Double.parseDouble(songCursor.getString(songDuration)) / 60000.0;
                }
                long durationMinute = (long) totalDuration;
                long durationSecond = (long) ((totalDuration - durationMinute) * 60);
                if (durationSecond>9){
                    currentDuration = String.valueOf(durationMinute) + ":" + String.valueOf(durationSecond);
                }
                else {
                    currentDuration = String.valueOf(durationMinute) + ":0" + String.valueOf(durationSecond);
                }
                String currentDetailString = currentArtist + " | " + currentYear + " | " + currentDuration;
                SongsDataProvider dataProvider = new SongsDataProvider(currentTitle,
                        currentDetailString,
                        currentSongPath,
                        currentAlbum);
                songsDataProviders.add(dataProvider);
            } while (songCursor.moveToNext());

            Collections.sort(songsDataProviders, new Comparator<SongsDataProvider>() {
                @Override
                public int compare(SongsDataProvider songsDataProvider, SongsDataProvider t1) {
                    return songsDataProvider.getSongTitle().compareTo(t1.getSongTitle());
                }
            });
        }
        return  songsDataProviders;
    }
}