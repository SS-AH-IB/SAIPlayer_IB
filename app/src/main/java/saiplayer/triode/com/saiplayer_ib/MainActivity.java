package saiplayer.triode.com.saiplayer_ib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import saiplayer.triode.com.saiplayer_ib.Fragments.ArtistsFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.PermissionFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.PlaylistsFragment;
import saiplayer.triode.com.saiplayer_ib.Fragments.SongsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int STORAGE_PERMISSION_REQUEST = 1;
    boolean Permission_Status = false;
    ActionBarDrawerToggle actionBarDrawerToggle;

    DrawerLayout main_drawer_container;

    ImageView activity_main_background;
    ImageButton plalists_button;
    ImageButton artists_button;
    ImageButton songs_button;

    ArtistsFragment artistsFragment;
    SongsFragment songsFragment;
    PlaylistsFragment playlistsFragment;

    View drawerView;
    View drawerContent;
    View mainContent;
    View drawer_top_section_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckPermission();

        main_drawer_container = (DrawerLayout) findViewById(R.id.main_drawer_container);
        drawerView = (View)findViewById(R.id.drawer_view);
        drawerContent = (View)findViewById(R.id.drawer_content);
        mainContent = (View)findViewById(R.id.main_content);
        drawer_top_section_view = (View)findViewById(R.id.drawer_top_section_view);
        activity_main_background = (ImageView)findViewById(R.id.activity_main_background);
        plalists_button = (ImageButton)findViewById(R.id.playlists_button);
        artists_button = (ImageButton)findViewById(R.id.artists_button);
        songs_button = (ImageButton)findViewById(R.id.songs_button);
        plalists_button.setOnClickListener(this);
        artists_button.setOnClickListener(this);
        songs_button.setOnClickListener(this);

        Bitmap default_original = BitmapFactory.decodeResource(getResources(),R.drawable.reside_background_1);
        Bitmap deafult_blurred = BlurBitmap.blur(this, default_original);
        BitmapDrawable default_drawable = new BitmapDrawable(getResources(),deafult_blurred);
        activity_main_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        activity_main_background.setImageDrawable(default_drawable);

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

    }

    public void CheckPermission(){

        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);
            }
            else {
                Permission_Status = true;
                playlistsFragment = new PlaylistsFragment();
                artistsFragment = new ArtistsFragment();
                songsFragment = new SongsFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.category_content, songsFragment);
                fragmentTransaction.commit();
            }
        }
        else {
            Permission_Status = true;
            playlistsFragment = new PlaylistsFragment();
            artistsFragment = new ArtistsFragment();
            songsFragment = new SongsFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.category_content, songsFragment);
            fragmentTransaction.commit();
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
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
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
                    fragmentTransaction.replace(R.id.category_content,playlistsFragment);
                    break;
                case R.id.artists_button:
                    fragmentTransaction.replace(R.id.category_content,artistsFragment);
                    break;
                case R.id.songs_button:
                    fragmentTransaction.replace(R.id.category_content,songsFragment);
                    break;
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Permission_Status){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.category_content,songsFragment);
            fragmentTransaction.commit();
        }
        else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.category_content,new PermissionFragment());
            fragmentTransaction.commit();
        }
    }

}