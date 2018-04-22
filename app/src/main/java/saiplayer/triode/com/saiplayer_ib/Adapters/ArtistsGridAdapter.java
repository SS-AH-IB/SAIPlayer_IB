package saiplayer.triode.com.saiplayer_ib.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import saiplayer.triode.com.saiplayer_ib.AsyncTasks.ArtistAsyncTask;
import saiplayer.triode.com.saiplayer_ib.DataHolder.ArtistsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by Indranil on 04-Oct-17.
 */

public class ArtistsGridAdapter extends ArrayAdapter<ArtistsDataProvider> implements View.OnClickListener{
    ArrayList<ArtistsDataProvider> artistsDataProviders = new ArrayList<ArtistsDataProvider>();
    Context context;
    ContentResolver contentResolver;
    int gridID;
    HashMap<String,Integer> alphaIndexer;
    String[] sections;

    private OnArtistClickType onArtistClickType;

    public interface OnArtistClickType{
        void onArtistItemClick(String artistName, ArrayList<String> bitmapContainer);
    }

    public void setOnArtistClickType(final OnArtistClickType onArtistClickType){
        this.onArtistClickType = onArtistClickType;
    }

    public ArtistsGridAdapter(ContentResolver contentResolver,
                              Context context,
                              int gridID,
                              ArrayList<ArtistsDataProvider> artistsDataProviders){
        super(context,gridID,artistsDataProviders);
        this.artistsDataProviders = artistsDataProviders;
        this.contentResolver = contentResolver;
        this.context = context;
        this.gridID = gridID;

    }

    @Override
    public int getCount() {
        return artistsDataProviders.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(gridID,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.artist_art);
            viewHolder.artist_details_container_root = (LinearLayout) convertView.findViewById(R.id.artist_details_container_root);
            viewHolder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
            viewHolder.artistDetails = (TextView) convertView.findViewById(R.id.artist_details);
            viewHolder.artistName.setSelected(true);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.imageView.setImageResource(R.drawable.artist_card_shape);
            viewHolder.artistAsyncTask = (ArtistAsyncTask) new ArtistAsyncTask(viewHolder.imageView,contentResolver)
                    .execute(artistsDataProviders.get(position).getArtistName());
            convertView.setTag(viewHolder);
            viewHolder.artist_details_container_root.setTag(position);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(viewHolder.artistAsyncTask.getStatus()== AsyncTask.Status.RUNNING)
            {
                viewHolder.artistAsyncTask.cancel(true);
            }
        }
        viewHolder.artistName.setText(artistsDataProviders.get(position).getArtistName());
        viewHolder.artistDetails.setText(artistsDataProviders.get(position).getArtistDetails());
        viewHolder.imageView.setImageResource(R.drawable.artist_card_shape);
        viewHolder.artistName.setSelected(true);
        viewHolder.artistAsyncTask = (ArtistAsyncTask) new ArtistAsyncTask(viewHolder.imageView,contentResolver)
                .execute(artistsDataProviders.get(position).getArtistName());
        convertView.setTag(viewHolder);
        viewHolder.artist_details_container_root.setTag(position);
        viewHolder.artist_details_container_root.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder{
        ImageView imageView;
        LinearLayout artist_details_container_root;
        TextView artistName;
        TextView artistDetails;
        ArtistAsyncTask artistAsyncTask;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.artist_details_container_root:
                String currentArtistName = artistsDataProviders.get((Integer) v.getTag()).getArtistName();
                ArrayList<String> bitmap_Container = new ArrayList<String>();
                if (currentArtistName != null){
                    bitmap_Container.clear();
                    Uri albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                    Cursor albumArtCursor = contentResolver.query(albumArtUri,new String[]{
                                    MediaStore.Audio.Albums.ALBUM_ART},
                            "ARTIST=?",
                            new String[]{currentArtistName},
                            null);
                    if (albumArtCursor.moveToFirst()){
                        do {
                            int artistsAlbumArts = albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                            String albumArts = albumArtCursor.getString(artistsAlbumArts);
                            bitmap_Container.add(albumArts);
                        }while (albumArtCursor.moveToNext());
                        albumArtCursor.close();
                        if (bitmap_Container.size()>0){

                            onArtistClickType.onArtistItemClick(artistsDataProviders.get((Integer) v.getTag()).getArtistName(),bitmap_Container);
                        }
                        else
                        {
                            onArtistClickType.onArtistItemClick(artistsDataProviders.get((Integer) v.getTag()).getArtistName(),null);
                        }
                    }
                    else {
                        onArtistClickType.onArtistItemClick(artistsDataProviders.get((Integer) v.getTag()).getArtistName(),null);
                    }
                }
                else {
                    onArtistClickType.onArtistItemClick(artistsDataProviders.get((Integer) v.getTag()).getArtistName(),null);
                }

                break;
        }
    }

}
