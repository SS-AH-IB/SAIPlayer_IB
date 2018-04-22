package saiplayer.triode.com.saiplayer_ib.Fragments;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import saiplayer.triode.com.saiplayer_ib.Adapters.SongsListAdapter;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongSearch;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by Indranil on 12-Oct-17.
 */

public class ArtistsDetailHolderFragment extends Fragment {
    ArrayList<String> bitmapStringContainer = new ArrayList<String>();
    ArrayList<Bitmap> bitmapsContainer = new ArrayList<Bitmap>();
    String artistNameString = null;

    ImageView artist_art_holder;
    TextView artist_name_holder;
    ImageButton artist_details_container_back;
    ImageView artist_image_holder;
    TextView artist_details_holder;
    LinearLayout artist_songs_container;
    ContentResolver resolver;

    OnArtistDetailHolderClickListener onArtistDetailHolderClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.artist_detail_holder_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resolver = getActivity().getContentResolver();
        artist_art_holder = (ImageView) view.findViewById(R.id.artist_art_holder);
        artist_name_holder = (TextView) view.findViewById(R.id.artist_name_holder);
        artist_details_container_back = (ImageButton) view.findViewById(R.id.artist_details_container_back);
        artist_image_holder = (ImageView) view.findViewById(R.id.artist_image_holder);
        artist_details_holder = (TextView) view.findViewById(R.id.artist_details_holder);
        artist_songs_container = (LinearLayout) view.findViewById(R.id.artist_songs_container);

        artistNameString = getArguments().getString("ARTIST_NAME");
        bitmapStringContainer = getArguments().getStringArrayList("ARTIST_BITMAP");

        artist_details_container_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onArtistDetailHolderClickListener.backButtonPressed();
            }
        });

        artist_name_holder.setText(artistNameString);
        artist_name_holder.setSelected(true);
        if (bitmapStringContainer!=null){
            for (int i=0; i<bitmapStringContainer.size(); i++){
                Bitmap bitmap = BitmapFactory.decodeFile(bitmapStringContainer.get(i));
                if (bitmap!=null){
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,400,400,false);
                    bitmapsContainer.add(scaledBitmap);
                }
            }
            if (bitmapsContainer.size()>1){
                int width = 0;
                for (int i=0; i<bitmapsContainer.size(); i++){
                    if (!bitmapsContainer.get(i).equals(null)){
                        width += bitmapsContainer.get(i).getWidth()/(i+1);
                    }
                }
                int height = bitmapsContainer.get(0).getHeight();
                Bitmap combinedBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(combinedBitmap);
                for (int i=bitmapsContainer.size()-1; i>=0; i--){
                    if (!bitmapsContainer.get(i).equals(null)) {
                        int subLength = 0;
                        for (int j = bitmapsContainer.size() - 1; j > i; j--) {
                            subLength += bitmapsContainer.get(j).getWidth() / (j + 1);
                        }
                        subLength += bitmapsContainer.get(i).getWidth();
                        canvas.drawBitmap(bitmapsContainer.get(i), (width - subLength), 0.0f, null);
                    }
                }
                artist_art_holder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                artist_art_holder.setImageBitmap(combinedBitmap);
                artist_image_holder.setScaleType(ImageView.ScaleType.FIT_XY);
                artist_image_holder.setImageBitmap(combinedBitmap);
            }
            else if(bitmapsContainer.size()>0){
                artist_art_holder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                artist_art_holder.setImageBitmap(bitmapsContainer.get(0));
                artist_image_holder.setScaleType(ImageView.ScaleType.FIT_XY);
                artist_image_holder.setImageBitmap(bitmapsContainer.get(0));
            }
            else
            {
                artist_art_holder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                artist_art_holder.setImageResource(R.drawable.blank_song_holder);
                artist_image_holder.setScaleType(ImageView.ScaleType.FIT_XY);
                artist_image_holder.setImageResource(R.drawable.blank_song_holder);
            }
        }
        else {
            artist_art_holder.setScaleType(ImageView.ScaleType.CENTER_CROP);
            artist_art_holder.setImageResource(R.drawable.blank_song_holder);
            artist_image_holder.setScaleType(ImageView.ScaleType.FIT_XY);
            artist_image_holder.setImageResource(R.drawable.blank_song_holder);
        }
        Uri artistDetailUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistDetailCursor = resolver.query(artistDetailUri,new String[]{
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS},
                "ARTIST=?",
                new String[]{artistNameString},null);
        if (artistDetailCursor !=null && artistDetailCursor.moveToFirst()){
            int albumCount = artistDetailCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int trackCount = artistDetailCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            String numberOfAlbums = artistDetailCursor.getString(albumCount);
            String numberOfTracks = artistDetailCursor.getString(trackCount);
            String artistDetails = "";
            int numAlbum = Integer.parseInt(numberOfAlbums);
            int numTrack = Integer.parseInt(numberOfTracks);
            if (numAlbum>1 && numAlbum>1){
                artistDetails = artistNameString+" \n"+numberOfAlbums+" Albums | "+numberOfTracks+" Songs";
            }
            else if (numAlbum<1 && numTrack>1){
                artistDetails = artistNameString+" \n"+numberOfAlbums+" Album | "+numberOfTracks+" Songs";
            }
            else if (numAlbum>1 && numTrack<1){
                artistDetails = artistNameString+" \n"+numberOfAlbums+" Albums | "+numberOfTracks+" Song";
            }
            else {
                artistDetails = artistNameString+" \n"+numberOfAlbums+" Album | "+numberOfTracks+" Song";
            }
            artist_details_holder.setText(artistDetails);
        }
        else {
            artist_details_holder.setText("Oops! No data found");
        }


        Uri artistArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor artistArtCursor = resolver.query(artistArtUri,new String[]{
                        MediaStore.Audio.Albums.ALBUM,
                        MediaStore.Audio.Albums.ALBUM_KEY,
                        MediaStore.Audio.Albums.ALBUM_ART},
                "ARTIST=?",
                new String[]{artistNameString},
                null);
        if (artistArtCursor != null && artistArtCursor.moveToFirst()){
            do {
                int artistSongAlbumCol = artistArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                int artistAlbumCol = artistArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY);
                int artistArtCol = artistArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                final String artistSongAlbum = artistArtCursor.getString(artistSongAlbumCol);
                final String artistAlbum = artistArtCursor.getString(artistAlbumCol);
                String artistArt = artistArtCursor.getString(artistArtCol);
                Bitmap artistArtBitmap = BitmapFactory.decodeFile(artistArt);
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewArtistArt = layoutInflater.inflate(R.layout.artist_songs_with_album,artist_songs_container,false);
                ImageView artist_individual_album = viewArtistArt.findViewById(R.id.artist_individual_album);
                artist_individual_album.setImageBitmap(artistArtBitmap);
                LinearLayout artist_albums_song_container = viewArtistArt.findViewById(R.id.artist_albums_song_container);
                TextView artist_song_album_holder = viewArtistArt.findViewById(R.id.artist_song_album_holder);
                artist_song_album_holder.setText(artistSongAlbum);
                artist_song_album_holder.setSelected(true);

                Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor songCursor = resolver.query(songUri,new String[]{
                                MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media.YEAR,
                                MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.DATA},
                        "ALBUM_KEY=?",
                        new String[]{artistAlbum},
                        null);
                if (songCursor != null && songCursor.moveToFirst()){
                    int songTitleCol = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int songArtistCol = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int songYearCol = songCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
                    int songDurationCol = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    int songDataCol = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    do {
                        View viewArtistSongs = layoutInflater.inflate(R.layout.artist_inflated_single_song,artist_albums_song_container,false);
                        TextView artist_songs_title_on_songs = viewArtistSongs.findViewById(R.id.artist_songs_title_on_songs);
                        TextView artist_songs_detail_on_songs = viewArtistSongs.findViewById(R.id.artist_songs_detail_on_songs);
                        LinearLayout artist_song_item_container_root = viewArtistSongs.findViewById(R.id.artist_song_item_container_root);
                        String currentTitle = songCursor.getString(songTitleCol);
                        String currentArtist = songCursor.getString(songArtistCol);
                        String currentYear = songCursor.getString(songYearCol);
                        String currentDuration = null;
                        final String songDataPath = songCursor.getString(songDataCol);
                        double totalDuration=0.0;
                        if(songCursor.getString(songDurationCol)!=null) {
                            totalDuration = Double.parseDouble(songCursor.getString(songDurationCol)) / 60000.0;
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
                        artist_songs_title_on_songs.setText(currentTitle);
                        artist_songs_title_on_songs.setSelected(true);
                        artist_songs_detail_on_songs.setText(currentDetailString);
                        artist_songs_detail_on_songs.setSelected(true);
                        artist_song_item_container_root.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onArtistDetailHolderClickListener.onArtistSongItemClick(artistAlbum,songDataPath);
                            }
                        });
                        artist_albums_song_container.addView(viewArtistSongs);
                    }while (songCursor.moveToNext());
                    songCursor.close();
                }
                artist_songs_container.addView(viewArtistArt);
            }while (artistArtCursor.moveToNext());
            artistArtCursor.close();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onArtistDetailHolderClickListener = (OnArtistDetailHolderClickListener) activity;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnArtistDetailHolderClickListener{
        void onArtistSongItemClick(String album_key,String dataPath);
        void backButtonPressed();
    }


}
