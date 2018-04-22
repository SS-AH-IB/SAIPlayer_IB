package saiplayer.triode.com.saiplayer_ib;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;
import org.michaelevans.colorart.library.ColorArt;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.LogRecord;

import saiplayer.triode.com.saiplayer_ib.Adapters.ArtistsGridAdapter;
import saiplayer.triode.com.saiplayer_ib.DataHolder.ArtistsDataProvider;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.Fragments.ArtistsDetailHolderFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.ArtistsFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.PermissionFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.PlaylistsFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.SongsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SongsFragment.OnSongPressedListener,
        ArtistsFragment.OnArtistPressedListener,
        ArtistsDetailHolderFragment.OnArtistDetailHolderClickListener{

    private static final int STORAGE_PERMISSION_REQUEST = 1;
    boolean Permission_Status = false;
    int pixelColorPrimary = 0;
    int pixelColorSecondary = 0;
    int fragmentNumber = 0;
    ActionBarDrawerToggle actionBarDrawerToggle;

    DrawerLayout main_drawer_container;

    ImageView activity_main_background;
    ImageButton plalists_button;
    ImageButton artists_button;
    ImageButton songs_button;

    ArtistsFragment artistsFragment;
    SongsFragment songsFragment;
    PlaylistsFragment playlistsFragment;
    ArtistsDetailHolderFragment artistsDetailHolderFragment;

    RelativeLayout category_playlist;
    RelativeLayout category_artist;
    RelativeLayout category_song;
    LinearLayout side_category_button_holder;
    FrameLayout category_container;

    View drawerView;
    View drawerContent;
    View mainContent;
    View drawer_top_section_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_drawer_container = (DrawerLayout) findViewById(R.id.main_drawer_container);
        drawerView = (View)findViewById(R.id.drawer_view);
        drawerContent = (View)findViewById(R.id.drawer_content);
        mainContent = (View)findViewById(R.id.main_content);
        drawer_top_section_view = (View)findViewById(R.id.drawer_top_section_view);
        activity_main_background = (ImageView)findViewById(R.id.activity_main_background);
        category_playlist = (RelativeLayout)findViewById(R.id.category_playlist);
        category_artist = (RelativeLayout)findViewById(R.id.category_artist);
        category_song = (RelativeLayout)findViewById(R.id.category_song);
        plalists_button = (ImageButton)findViewById(R.id.playlists_button);
        artists_button = (ImageButton)findViewById(R.id.artists_button);
        songs_button = (ImageButton)findViewById(R.id.songs_button);
        category_container = (FrameLayout)findViewById(R.id.category_content);
        side_category_button_holder = (LinearLayout)findViewById(R.id.side_category_button_holder);
        plalists_button.setOnClickListener(this);
        artists_button.setOnClickListener(this);
        songs_button.setOnClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,main_drawer_container,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerContent.setX(drawerView.getWidth()*(1-slideOffset));
                mainContent.setX(drawerView.getWidth()*slideOffset);
            }
        };
        main_drawer_container.setDrawerListener(actionBarDrawerToggle);
        main_drawer_container.closeDrawer(drawerView);
        drawerContent.setX(drawerView.getWidth());

        //Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.reside_background_1);
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperdrawable = wallpaperManager.getDrawable();
        Bitmap defaultBitmap = ((BitmapDrawable)wallpaperdrawable).getBitmap();
        Bitmap deafult_blurred = BlurBitmap.blur(this, defaultBitmap);
        BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
        activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        activity_main_background.setBackground(default_drawable);
        ColorArt colorArt = new ColorArt(defaultBitmap);
        int background_alpha = Color.alpha(colorArt.getPrimaryColor());
        int background_red = Color.red(colorArt.getPrimaryColor());
        int background_green = Color.green(colorArt.getPrimaryColor());
        int background_blue = Color.blue(colorArt.getPrimaryColor());
        background_alpha*=0.25;
        pixelColorPrimary = Color.argb(background_alpha,background_red,background_green,background_blue);
        background_alpha = Color.alpha(colorArt.getSecondaryColor());
        background_red = Color.red(colorArt.getSecondaryColor());
        background_green = Color.green(colorArt.getSecondaryColor());
        background_blue = Color.blue(colorArt.getSecondaryColor());
        background_alpha*=0.25;
        pixelColorSecondary = Color.argb(background_alpha,background_red,background_green,background_blue);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);
        }
        else {
            Permission_Status = true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Permission_Status = true;
                    artistsFragment = new ArtistsFragment();
                    songsFragment = new SongsFragment();
                    playlistsFragment = new PlaylistsFragment();
                }
                else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.category_content,new PermissionFragment());
                    fragmentNumber = 10;
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        GradientDrawable gradientDrawable1;
        GradientDrawable gradientDrawable2;
        GradientDrawable gradientDrawable3;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (playlistsFragment == null && artistsFragment == null && songsFragment == null){
                playlistsFragment = new PlaylistsFragment();
                artistsFragment = new ArtistsFragment();
                songsFragment = new SongsFragment();
            }
            switch (view.getId()){
                case R.id.playlists_button:
                    category_playlist.setBackgroundResource(R.drawable.category_shadow_selected);
                    gradientDrawable3 = (GradientDrawable)category_playlist.getBackground();
                    gradientDrawable3.setColor(pixelColorSecondary);
                    category_artist.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable2 = (GradientDrawable)category_artist.getBackground();
                    gradientDrawable2.setColor(Color.parseColor("#64ffffff"));
                    category_song.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable1 = (GradientDrawable)category_song.getBackground();
                    gradientDrawable1.setColor(Color.parseColor("#64ffffff"));
                    fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                    fragmentTransaction.replace(R.id.category_content,playlistsFragment);
                    fragmentNumber = 1;
                    break;
                case R.id.artists_button:
                    category_artist.setBackgroundResource(R.drawable.category_shadow_selected);
                    gradientDrawable3 = (GradientDrawable)category_artist.getBackground();
                    gradientDrawable3.setColor(pixelColorSecondary);
                    category_playlist.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable2 = (GradientDrawable)category_playlist.getBackground();
                    gradientDrawable2.setColor(Color.parseColor("#64ffffff"));
                    category_song.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable1 = (GradientDrawable)category_song.getBackground();
                    gradientDrawable1.setColor(Color.parseColor("#64ffffff"));
                    fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                    fragmentTransaction.replace(R.id.category_content,artistsFragment);
                    fragmentNumber = 2;
                    break;
                case R.id.songs_button:
                    category_song.setBackgroundResource(R.drawable.category_shadow_selected);
                    gradientDrawable3 = (GradientDrawable)category_song.getBackground();
                    gradientDrawable3.setColor(pixelColorSecondary);
                    category_artist.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable2 = (GradientDrawable)category_artist.getBackground();
                    gradientDrawable2.setColor(Color.parseColor("#64ffffff"));
                    category_playlist.setBackgroundResource(R.drawable.category_shadow);
                    gradientDrawable1 = (GradientDrawable)category_playlist.getBackground();
                    gradientDrawable1.setColor(Color.parseColor("#64ffffff"));
                    fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                    fragmentTransaction.replace(R.id.category_content,songsFragment);
                    fragmentNumber = 3;
                    break;
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (Permission_Status){
            category_artist.setBackgroundResource(R.drawable.category_shadow_selected);
            GradientDrawable gradientDrawable = (GradientDrawable)category_artist.getBackground();
            gradientDrawable.setColor(pixelColorSecondary);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.category_content,new ArtistsFragment());
            fragmentTransaction.commit();
            fragmentNumber = 2;
        }
        else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.category_content,new PermissionFragment());
            fragmentTransaction.commit();
            fragmentNumber = 10;
        }
    }

    @Override
    public void onSongPressed(int songPosition, ArrayList<SongsDataProvider> arrayList) {
        Uri albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Bitmap albumBitmap = null;
        if (arrayList.get(songPosition).getSongAlbum() != null){
            Cursor albumArtCursor = getContentResolver().query(albumArtUri, new String[]{
                            MediaStore.Audio.Albums.ALBUM,
                            MediaStore.Audio.Albums.ALBUM_ART},
                    "ALBUM_KEY=?",
                    new String[]{arrayList.get(songPosition).getSongAlbum()},
                    null);
            if (albumArtCursor.moveToFirst()){
                int songAlbumArt = albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                String currentAlbumArt = albumArtCursor.getString(songAlbumArt);
                if (currentAlbumArt != null){
                    albumBitmap = BitmapFactory.decodeFile(currentAlbumArt);
                }
            }
        }
        if (albumBitmap != null){
            Bitmap deafult_blurred = BlurBitmap.blur(this, albumBitmap);
            BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
            activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            activity_main_background.setImageDrawable(default_drawable);
        }
        else if (albumBitmap == null){
            //albumBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.reside_background_1);
            Bitmap deafult_blurred = BlurBitmap.blur(this, albumBitmap);
            BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
            activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            activity_main_background.setBackground(default_drawable);
        }

        categoryButtonBackgroundController(albumBitmap);

    }

    public void categoryButtonBackgroundController(Bitmap albumBitmap){
        ColorArt colorArt = new ColorArt(albumBitmap);
        int background_alpha = Color.alpha(colorArt.getPrimaryColor());
        int background_red = Color.red(colorArt.getPrimaryColor());
        int background_green = Color.green(colorArt.getPrimaryColor());
        int background_blue = Color.blue(colorArt.getPrimaryColor());
        background_alpha*=0.25;
        pixelColorPrimary = Color.argb(background_alpha,background_red,background_green,background_blue);
        background_alpha = Color.alpha(colorArt.getSecondaryColor());
        background_red = Color.red(colorArt.getSecondaryColor());
        background_green = Color.green(colorArt.getSecondaryColor());
        background_blue = Color.blue(colorArt.getSecondaryColor());
        background_alpha*=0.25;
        pixelColorSecondary = Color.argb(background_alpha,background_red,background_green,background_blue);

        GradientDrawable gradientDrawable = (GradientDrawable)drawer_top_section_view.getBackground();
        gradientDrawable.setColor(colorArt.getPrimaryColor());
        drawerContent.setBackgroundColor(pixelColorPrimary);

        RippleDrawable rippleDrawable1 = (RippleDrawable) plalists_button.getBackground();
        RippleDrawable rippleDrawable2 = (RippleDrawable) artists_button.getBackground();
        RippleDrawable rippleDrawable3 = (RippleDrawable) songs_button.getBackground();
        int[][] states = new int[][] { new int[] { android.R.attr.state_enabled} };
        int[] colors = new int[] { colorArt.getSecondaryColor() };
        ColorStateList colorStateList = new ColorStateList(states,colors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable1.setColor(colorStateList);
            rippleDrawable2.setColor(colorStateList);
            rippleDrawable3.setColor(colorStateList);
        }
    }


    @Override
    public void onArtistPressed(String artistName, ArrayList<String> bitmapContainer) {
        fragmentNumber = 4;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        artistsDetailHolderFragment = new ArtistsDetailHolderFragment();
        Bundle artistBundle = new Bundle();
        artistBundle.putString("ARTIST_NAME",artistName);
        artistBundle.putStringArrayList("ARTIST_BITMAP",bitmapContainer);
        artistsDetailHolderFragment.setArguments(artistBundle);
        fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
        fragmentTransaction.replace(R.id.category_content,artistsDetailHolderFragment);
        fragmentTransaction.commit();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                side_category_button_holder.setVisibility(View.GONE);
                LinearLayout.LayoutParams sideButtonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,0.0f);
                side_category_button_holder.setLayoutParams(sideButtonParam);
                LinearLayout.LayoutParams categoryContentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,100.0f);
                category_container.setLayoutParams(categoryContentParam);
            }
        },200);

    }

    @Override
    public void onBackPressed() {
        if (fragmentNumber != 2 ){
            fragmentNumber = 2;
            side_category_button_holder.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams sideButtonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,88.0f);
            side_category_button_holder.setLayoutParams(sideButtonParam);
            LinearLayout.LayoutParams categoryContentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,12.0f);
            category_container.setLayoutParams(categoryContentParam);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (artistsFragment != null){
                fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                fragmentTransaction.replace(R.id.category_content,artistsFragment);
                fragmentTransaction.commit();
            }
            else {
                fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                fragmentTransaction.replace(R.id.category_content,new ArtistsFragment());
                fragmentTransaction.commit();
            }
        }
        else if (artistsDetailHolderFragment != null && artistsDetailHolderFragment.isVisible()){
            fragmentNumber = 2;
            side_category_button_holder.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams sideButtonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,88.0f);
            side_category_button_holder.setLayoutParams(sideButtonParam);
            LinearLayout.LayoutParams categoryContentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,12.0f);
            category_container.setLayoutParams(categoryContentParam);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (artistsFragment != null){
                fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                fragmentTransaction.replace(R.id.category_content,artistsFragment);
                fragmentTransaction.commit();
            }
            else {
                fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
                fragmentTransaction.replace(R.id.category_content,new ArtistsFragment());
                fragmentTransaction.commit();
            }
        }
        else if (fragmentNumber == 2){
            super.onBackPressed();
        }
    }

    @Override
    public void onArtistSongItemClick(String album_key, String dataPath) {
        Uri albumArtUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Bitmap albumBitmap = null;
        if (album_key != null){
            Cursor albumArtCursor = getContentResolver().query(albumArtUri, new String[]{
                            MediaStore.Audio.Albums.ALBUM,
                            MediaStore.Audio.Albums.ALBUM_ART},
                    "ALBUM_KEY=?",
                    new String[]{album_key},
                    null);
            if (albumArtCursor.moveToFirst()){
                int songAlbumArt = albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                String currentAlbumArt = albumArtCursor.getString(songAlbumArt);
                if (currentAlbumArt != null){
                    albumBitmap = BitmapFactory.decodeFile(currentAlbumArt);
                }
            }
        }
        if (albumBitmap != null){
            Bitmap deafult_blurred = BlurBitmap.blur(this, albumBitmap);
            BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
            activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            activity_main_background.setImageDrawable(default_drawable);
        }
        else if (albumBitmap == null){
            //albumBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.reside_background_1);
            Bitmap deafult_blurred = BlurBitmap.blur(this, albumBitmap);
            BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
            activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            activity_main_background.setBackground(default_drawable);
        }

        categoryButtonBackgroundController(albumBitmap);
    }

    @Override
    public void backButtonPressed() {
        side_category_button_holder.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams sideButtonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,88.0f);
        side_category_button_holder.setLayoutParams(sideButtonParam);
        LinearLayout.LayoutParams categoryContentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,12.0f);
        category_container.setLayoutParams(categoryContentParam);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentNumber = 2;
        if (artistsFragment != null){
            fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
            fragmentTransaction.replace(R.id.category_content,artistsFragment);
            fragmentTransaction.commit();
        }
        else {
            fragmentTransaction.setCustomAnimations(R.anim.scale_up,R.anim.scale_down);
            fragmentTransaction.replace(R.id.category_content,new ArtistsFragment());
            fragmentTransaction.commit();
        }
    }




}
