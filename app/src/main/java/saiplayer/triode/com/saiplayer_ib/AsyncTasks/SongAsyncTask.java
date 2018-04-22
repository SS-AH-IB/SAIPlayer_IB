package saiplayer.triode.com.saiplayer_ib.AsyncTasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 07-Aug-17.
 */

public class SongAsyncTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView imageView;
    private ContentResolver contentResolver;

    public SongAsyncTask (ImageView imageView, ContentResolver contentResolver){
        this.imageView = imageView;
        this.contentResolver = contentResolver;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap;
        if (strings[0] != null){
            Uri albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            Cursor albumArtCursor = contentResolver.query(albumArtUri, new String[]{
                            MediaStore.Audio.Albums.ALBUM,
                            MediaStore.Audio.Albums.ALBUM_ART},
                    "ALBUM_KEY=?",
                    new String[]{strings[0]},
                    null);
            if (albumArtCursor.moveToFirst()){
                int songAlbumArt = albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                String currentAlbumArt = albumArtCursor.getString(songAlbumArt);
                if (currentAlbumArt != null){
                    bitmap = BitmapFactory.decodeFile(currentAlbumArt);
                }
                else {
                    bitmap = null;
                }
            }
            else {
                bitmap = null;
            }
            albumArtCursor.close();
        }
        else {
            bitmap = null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            imageView.setImageResource(R.drawable.blank_song_holder);
        }
    }

}
