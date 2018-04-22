package saiplayer.triode.com.saiplayer_ib.AsyncTasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by Indranil on 04-Oct-17.
 */

public class ArtistAsyncTask extends AsyncTask<String,Void,Bitmap>{
    private ImageView imageView;
    private ContentResolver contentResolver;
    private ArrayList<Bitmap> bitmapHolder = new ArrayList<Bitmap>();

    public ArtistAsyncTask(ImageView imageView, ContentResolver contentResolver) {
        this.imageView = imageView;
        this.contentResolver = contentResolver;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params[0] != null){
            bitmapHolder.clear();
            Uri albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            Cursor albumArtCursor = contentResolver.query(albumArtUri,new String[]{
                            MediaStore.Audio.Albums.ALBUM_ART},
                    "ARTIST=?",
                    new String[]{params[0]},
                    null);
            if (albumArtCursor.moveToFirst()){
                do {
                    int artistsAlbumArts = albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                    String albumArts = albumArtCursor.getString(artistsAlbumArts);
                    Bitmap bitmap=BitmapFactory.decodeFile(albumArts);
                    if (bitmap!=null){
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,400,400,false);
                        bitmapHolder.add(scaledBitmap);
                    }
                }while (albumArtCursor.moveToNext());
                albumArtCursor.close();
                if (bitmapHolder.size()>1){
                    int width = 0;
                    for (int i=0; i<bitmapHolder.size(); i++){
                        if (!bitmapHolder.get(i).equals(null)){
                            width += bitmapHolder.get(i).getWidth()/(i+1);
                        }
                    }
                    int height = bitmapHolder.get(0).getHeight();
                    Bitmap combinedBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(combinedBitmap);
                    for (int i=bitmapHolder.size()-1; i>=0; i--){
                        if (!bitmapHolder.get(i).equals(null)) {
                            int subLength = 0;
                            for (int j = bitmapHolder.size() - 1; j > i; j--) {
                                subLength += bitmapHolder.get(j).getWidth() / (j + 1);
                            }
                            subLength += bitmapHolder.get(i).getWidth();
                            canvas.drawBitmap(bitmapHolder.get(i), (width - subLength), 0.0f, null);
                        }
                    }
                    return combinedBitmap;
                }
                else if(bitmapHolder.size()>0){
                    return bitmapHolder.get(0);
                }
                else
                {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        else {
            return  null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageView != null && bitmap != null) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmap);
        }
        else {
            imageView.setImageResource(R.drawable.blank_song_holder);
        }
    }

}

